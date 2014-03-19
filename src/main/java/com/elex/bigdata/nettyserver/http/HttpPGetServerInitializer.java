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

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpPGetServerInitializer extends ChannelInitializer<SocketChannel> {

  private String sequencedCategory;

  public HttpPGetServerInitializer(String sequencedCategory) {
    this.sequencedCategory = sequencedCategory;
  }

  @Override
  public void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline p = ch.pipeline();
    p.addLast("codec", new HttpServerCodec());
    p.addLast("handler", new HttpPGetServerHandler(sequencedCategory));
  }
}
