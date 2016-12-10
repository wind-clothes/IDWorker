package com.hunk.idworker;

import com.hunk.idworker.config.NettyConfig;
import com.hunk.idworker.server.HttpServer;

/**
 * <pre>
 * 服务端启动程序
 * </pre>
 * 
 * @author: xiongchengwei
 * @date: 2016年3月14日 下午2:33:07
 */
public class Bootstrap {

  /**
   * @param args
   * @return void
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    new HttpServer(new NettyConfig()).run();
  }

}
