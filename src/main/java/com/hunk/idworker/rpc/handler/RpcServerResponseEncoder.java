package com.hunk.idworker.rpc.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * <pre>
 * </pre>
 * 
 * @author: xiongchengwei
 * @date: 2016年3月17日 下午4:19:38
 */
public class RpcServerResponseEncoder extends MessageToByteEncoder<Object> {

  private static final Logger logger = LoggerFactory.getLogger(RpcServerResponseEncoder.class);

  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

  }



}
