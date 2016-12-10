package com.hunk.idworker.rpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <pre>
 * 基于RPC的远程调用服务器端处理handler
 * </pre>
 * 
 * @author: xiongchengwei
 * @date: 2016年3月17日 下午3:44:59
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<Object> {

  private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

  @Override
  protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
    
  }
}
