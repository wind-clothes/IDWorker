package com.hunk.idworker.server;


import java.util.Map;

import com.hunk.idworker.handler.HttpResponseHandler;
import com.hunk.idworker.handler.HttpServerHandler;
import com.hunk.idworker.util.HandlerFactory;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * <pre>
 * 绑定相关的handler
 * </pre>
 * 
 * @author: xiongchengwei
 * @date: 2016年3月14日 下午2:53:18
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline channelPipeline = ch.pipeline();
    Map<String, HttpResponseHandler> allHandlers = HandlerFactory.requestHandlerList();
    // 超时时间
    channelPipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(70));
    // 编码与解码
    channelPipeline.addLast("serverCodec", new HttpServerCodec());
    //
    channelPipeline.addLast("deflater", new HttpContentCompressor());
    // 自定义处理handler
    channelPipeline.addLast("HttpServerHandler", new HttpServerHandler(allHandlers));
  }

}
