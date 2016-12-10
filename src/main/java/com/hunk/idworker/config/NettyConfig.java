package com.hunk.idworker.config;

import com.hunk.idworker.util.Constants;

/**
 * <pre>
 * </pre>
 * 
 * @author: xiongchengwei
 * @date: 2016年3月28日 下午12:09:09
 */
public class NettyConfig {

  /**
   * 默认端口
   */
  private final static int DEFAULT_PORT = 80;
  private final static int DEFAULT_NETTY_BACKLOG = 1024;
  private final static int DEFAULT_NETTY_WORKER_NEVENTLOOPS_AVAILABLEPROCESSORS_MULTIPLE = 2;
  private final static int DEFAULT_NETTY_BOSS_NEVENTLOOPS_AVAILABLEPROCESSORS_MULTIPLE = 2;

  private final int port;
  private final int backlog;
  private final int workerEventLoopsAvailableProcessorsMultiple;
  private final int bossEventLoopsAvailableProcessorsMultiple;

  public NettyConfig() {
    String portStr = PropertiesUtil.getValue(Constants.NETTY_SERVER_PORT);
    port = (portStr == null || "".equals(portStr)) ? DEFAULT_PORT : Integer.valueOf(portStr);
    String backlogStr = PropertiesUtil.getValue(Constants.NETTY_BACKLOG);
    backlog = (backlogStr == null || "".equals(backlogStr)) ? DEFAULT_NETTY_BACKLOG
        : Integer.valueOf(backlogStr);

    String workerEventLoopsAvailableProcessorsMultipleStr =
        PropertiesUtil.getValue(Constants.NETTY_WORKER_NEVENTLOOPS_AVAILABLEPROCESSORS_MULTIPLE);
    workerEventLoopsAvailableProcessorsMultiple =
        (workerEventLoopsAvailableProcessorsMultipleStr == null
            || "".equals(workerEventLoopsAvailableProcessorsMultipleStr))
                ? DEFAULT_NETTY_WORKER_NEVENTLOOPS_AVAILABLEPROCESSORS_MULTIPLE
                : Integer.valueOf(workerEventLoopsAvailableProcessorsMultipleStr);

    String bossEventLoopsAvailableProcessorsMultipleStr =
        PropertiesUtil.getValue(Constants.NETTY_BOSS_NEVENTLOOPS_AVAILABLEPROCESSORS_MULTIPLE);
    bossEventLoopsAvailableProcessorsMultiple =
        (bossEventLoopsAvailableProcessorsMultipleStr == null
            || "".equals(bossEventLoopsAvailableProcessorsMultipleStr))
                ? DEFAULT_NETTY_BOSS_NEVENTLOOPS_AVAILABLEPROCESSORS_MULTIPLE
                : Integer.valueOf(bossEventLoopsAvailableProcessorsMultipleStr);
  }

  public int getPort() {
    return port;
  }

  public int getBacklog() {
    return backlog;
  }

  public int getWorkerEventLoopsAvailableProcessorsMultiple() {
    return workerEventLoopsAvailableProcessorsMultiple;
  }

  public int getBossEventLoopsAvailableProcessorsMultiple() {
    return bossEventLoopsAvailableProcessorsMultiple;
  }

}
