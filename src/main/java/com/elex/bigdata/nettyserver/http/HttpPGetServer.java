/*
 * Copyright 2012 The Netty Project
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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;

/**
 * An HTTP server that sends back the content of the received HTTP request in a pretty plaintext form.
 */
public class HttpPGetServer {

  private static final Logger LOGGER = Logger.getLogger(HttpPGetServer.class);

  private final int port;

  public HttpPGetServer(int port) {
    this.port = port;
  }

  public void run() throws Exception {
    // Configure the server.
    EventLoopGroup bossGroup = new NioEventLoopGroup(5);
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.option(ChannelOption.SO_BACKLOG, 1024);
      b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
       .childHandler(new HttpPGetServerInitializer());

      LOGGER.info("Server(" + port + ") started.");
      Channel ch = b.bind(port).sync().channel();
      ch.closeFuture().sync();
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }

  public static void main(String[] args) throws Exception {
    int port = 80;
    if (ArrayUtils.isNotEmpty(args)) {
      port = Integer.parseInt(args[0]);
    }
    NDC.push(String.valueOf(port));
    new HttpPGetServer(port).run();
  }
}
