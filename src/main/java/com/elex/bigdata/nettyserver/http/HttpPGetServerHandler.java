/*
 * Copyright 2013 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.elex.bigdata.nettyserver.http;

import static com.elex.bigdata.nettyserver.Errors.ERROR_RESULT;
import static com.elex.bigdata.nettyserver.Errors.NO_SUCH_P_FOR_THIS_USER;
import static com.elex.bigdata.nettyserver.NettyServerConstants.CURRENT_PORT;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Values;
import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import com.elex.bigdata.hashing.BDMD5;
import com.elex.bigdata.nettyserver.NettyServerConstants;
import com.elex.bigdata.nettyserver.NettyServerUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class HttpPGetServerHandler extends ChannelInboundHandlerAdapter {

  private static final Logger LOGGER = Logger.getLogger(HttpPGetServerHandler.class);

  private boolean isValid(String uri) {
    return StringUtils.isNotBlank(uri) && !NettyServerConstants.USELESS_URI.equals(uri);
  }

  private static String extractResult(String result) {
    if (StringUtils.isBlank(result)) {
      return null;
    }
    char dot = '.', sep = ',';
    int factor = 3;
    int len = result.length();
    if (len % 3 != 0) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    char type = result.charAt(0);
    sb.append(type);
    sb.append(dot);
    sb.append(result.charAt(1));
    sb.append(result.charAt(2));

    for (int i = 1; i < len / factor; i++) {
      sb.append(sep);
      type = result.charAt(i * factor);
      sb.append(type);
      sb.append(dot);
      sb.append(result.charAt(i * factor + 1));
      sb.append(result.charAt(i * factor + 2));
    }
    return sb.toString();
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (msg instanceof HttpRequest) {
      HttpRequest req = (HttpRequest) msg;

      String uri = req.getUri();
      if (is100ContinueExpected(req)) {
        ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
      }
      boolean keepAlive = isKeepAlive(req);
      LOGGER.info("[" + CURRENT_PORT + "] uri - " + uri);
      byte[] bytes;
      if (isValid(uri)) {
        String uid;
        try {
          uid = NettyServerUtils.extractURI2UID(uri);
          String md5UID = BDMD5.getInstance().toMD5(uid);
          String result = NettyServerUtils.REDIS_OPERATION.get(md5UID);
          if (StringUtils.isBlank(result)) {
            bytes = NO_SUCH_P_FOR_THIS_USER.getReturnContentBytes();
          } else {
            try {
              bytes = extractResult(result).getBytes();
            } catch (Exception e) {
              LOGGER.warn("[" + CURRENT_PORT + "] Invalid result - " + result);
              bytes = ERROR_RESULT.getReturnContentBytes();
            }
          }
        } catch (Exception e) {
          bytes = ERROR_RESULT.getReturnContentBytes();
          LOGGER.warn("[" + CURRENT_PORT + "]Invalid uid - " + uri);
        }
      } else {
        LOGGER.warn("[" + CURRENT_PORT + "]Invalid url - " + uri);
        bytes = ERROR_RESULT.getReturnContentBytes();
      }

      FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(bytes));
      response.headers().set(CONTENT_TYPE, "text/plain");
      response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

      if (!keepAlive) {
        ctx.write(response).addListener(ChannelFutureListener.CLOSE);
      } else {
        response.headers().set(CONNECTION, Values.KEEP_ALIVE);
        ctx.write(response);
      }
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }

}
