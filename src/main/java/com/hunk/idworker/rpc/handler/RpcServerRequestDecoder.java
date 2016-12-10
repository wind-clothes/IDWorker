package com.hunk.idworker.rpc.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * <pre>
 *  对请求进行的解码
 * </pre>
 * 
 * @author: xiongchengwei
 * @date: 2016年3月17日 下午4:19:38
 */
public class RpcServerRequestDecoder extends ByteToMessageDecoder {

  private static final Logger logger = LoggerFactory.getLogger(RpcServerRequestDecoder.class);

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    
  }

}
