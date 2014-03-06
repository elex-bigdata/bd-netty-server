package com.elex.bigdata.nettyserver.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Z J Wu Date: 14-3-4 Time: 上午11:39 Package: com.elex.bigdata.nettyserver.client
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ByteBuf m = (ByteBuf) msg; // (1)
    try {
      long currentTimeMillis = m.readLong();
      System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date(currentTimeMillis)));
      ctx.close();
    } finally {
      ReferenceCountUtil.release(msg);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
