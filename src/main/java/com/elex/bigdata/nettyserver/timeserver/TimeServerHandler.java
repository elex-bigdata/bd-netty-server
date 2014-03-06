package com.elex.bigdata.nettyserver.timeserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * User: Z J Wu Date: 14-3-3 Time: 下午6:19 Package: com.elex.bigdata.nettyserver.timeserver
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelActive(final ChannelHandlerContext ctx) throws Exception {
    final ByteBuf time = ctx.alloc().buffer(8);
    time.writeLong(System.currentTimeMillis());
    final ChannelFuture cf = ctx.writeAndFlush(time);
    cf.addListener(ChannelFutureListener.CLOSE);

  }
}
