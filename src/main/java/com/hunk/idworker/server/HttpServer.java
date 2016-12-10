package com.hunk.idworker.server;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hunk.idworker.config.NettyConfig;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * <pre>
 * 基于netty的tcp / IP协议栈上的HTTP协议栈的服务端
 * </pre>
 * 
 * @author: xiongchengwei
 * @date: 2016年3月14日 上午10:53:20
 * @since 1.0.0
 */
public class HttpServer {

  private static Logger logger = LoggerFactory.getLogger(HttpServer.class);

  private final NettyConfig nettyConfig;

  public HttpServer(NettyConfig nettyConfig) {
    super();
    this.nettyConfig = nettyConfig;
  }

  public void run() throws Exception {
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    EventLoopGroup bosserGroup = new NioEventLoopGroup(
        availableProcessors * nettyConfig.getBossEventLoopsAvailableProcessorsMultiple());
    // 线程执行器，默认使用的是fork-join框架
    // new DefaultExecutorServiceFactory(getClass()).newExecutorService(nEventLoops);
    EventLoopGroup workerGroup = new NioEventLoopGroup(
        availableProcessors * nettyConfig.getWorkerEventLoopsAvailableProcessorsMultiple());
    try {
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      // NioServerSocketChannel 是父类的 channel
      serverBootstrap.group(bosserGroup, workerGroup).channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, nettyConfig.getBacklog())
          .childHandler(new HttpServerInitializer());
      Channel channel = serverBootstrap.bind(nettyConfig.getPort()).sync().channel();
      logger.info(String.format("httpServer is start ip:【%s】;port:【%s】",
          InetAddress.getLocalHost().getHostAddress(), nettyConfig.getPort()));
      channel.closeFuture().sync();
    } finally {
      bosserGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }
}
