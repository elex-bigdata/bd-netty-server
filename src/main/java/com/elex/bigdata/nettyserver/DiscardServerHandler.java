package com.elex.bigdata.nettyserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * User: Z J Wu Date: 14-3-3 Time: 下午3:14 Package: com.elex.bigdata.nettyserver
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf bb = (ByteBuf) msg;
    try {
      while (bb.isReadable()) {
        System.out.print(bb.readChar());
      }
      System.out.flush();
    } finally {
      ReferenceCountUtil.release(msg);
    }
  }

}
