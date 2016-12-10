package com.hunk.idworker.rpc.handler;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

/**
 * <pre>
 * 编解码处理器
 * </pre>
 * 
 * @author: xiongchengwei
 * @date: 2016年3月17日 下午4:19:38
 */
public final class RpcServerCodec extends ByteToMessageCodec<Object> {

  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

  }

}
