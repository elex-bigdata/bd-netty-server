package com.elex.bigdata.nettyserver.echoserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

/**
 * User: Z J Wu Date: 14-3-3 Time: 下午3:52 Package: com.elex.bigdata.nettyserver
 */
public class EchoServer {
  private static final Logger LOGGER = Logger.getLogger(EchoServer.class);
  private int port;

  public EchoServer(int port) {
    this.port = port;
  }

  public void startServer() throws Exception {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup);
      b.channel(NioServerSocketChannel.class);
      b.childHandler(new ChannelInitializer<SocketChannel>() {
        @Override
        public void initChannel(SocketChannel ch) throws Exception {
          ch.pipeline().addLast(new EchoServerHandler());
        }
      });
      b.option(ChannelOption.SO_BACKLOG, 128);
      b.childOption(ChannelOption.SO_KEEPALIVE, true);
      ChannelFuture f = b.bind(port).sync();
      f.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
  }

  public static void main(String[] args) throws Exception {
    new EchoServer(9527).startServer();
    LOGGER.info("[echoserver]: Server started.");
  }
}
