package com.hunk.idworker.id;

import java.util.Random;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <pre>
 * 64位的id序列号，(1位符号位)41位时间戳，10位工作机器id(5位节点Id,5位机器id)，12位的序列号
 * 
 * 参考：https://github.com/twitter/snowflake
 * 
 * 
 * 线程安全需要确保的是：操作原子性，内存的一致性，机器指令的重排
 * </pre>
 * 
 * @author: xiongchengwei
 * @date: 2016年3月8日 下午3:38:11
 */
@ThreadSafe
public class IdMaker {

  private static final Logger logger = LoggerFactory.getLogger(IdMaker.class);

  // 时间戳的起始值
  private final static long twepoch = 1457971200000L;
  // 机器序列位数
  private final static long workerIdBits = 5L;
  // 机器节点位数
  private final static long datacenterIdBits = 5L;
  // 机器序列最大的值 31
  private final static long MaxWorkId = -1L ^ (-1L << workerIdBits);
  // 机器节点位数最大的值 31
  private final static long MaxDatacenterId = -1L ^ (-1L << datacenterIdBits);
  // 序列号的位数
  private final static long sequenceBits = 12L;
  // 序列号的最大值 4095
  private final static long sequenceData = -1L ^ (-1L << sequenceBits);

  // 机器序列的左移位数 12
  private final static long workerShift = sequenceBits;
  // 机器节点的左移位数 17
  private final static long datacenterIdShift = sequenceBits + datacenterIdBits;
  // 时间戳的左移位数 22
  private final static long twepochShift = sequenceBits + workerIdBits + datacenterIdBits;

  private long lastTimestamp = -1L;
  private LongAdder sequence;
  private final long workerId;
  private final long datacenterId;
  private final ReentrantLock reentrantLock = new ReentrantLock();

  public IdMaker(long workerId, long datacenterId) {
    if (workerId > MaxWorkId || workerId < 0) {
      throw new IllegalArgumentException(String.format("workerId is error max:%s", MaxWorkId));
    }
    if (datacenterId > MaxDatacenterId || datacenterId < 0) {
      throw new IllegalArgumentException(
          String.format("datacenterId is error max:%s", MaxDatacenterId));
    }
    this.datacenterId = datacenterId;
    this.workerId = workerId;
    this.sequence = new LongAdder();
    this.sequence.add(new Random().nextInt(4096));
  }

  public long nextId() {
    try {
      // FIXME 需要重写进行优化，currentTime，与lastTimestamp的依赖关系导致锁的力度必须得加大,或者采用非阻塞算法降低锁的竞争和切换
      reentrantLock.lock();
      long currentTime = System.currentTimeMillis();
      if (lastTimestamp == currentTime) {
        // 当在同一毫秒内时，进行计数器累加，当sequence的指加完之后等于4096之后就会使sequence得值置为0
        // ，开始重新计数，程序等待直到下一毫秒
        sequence.increment();
        if (((sequence.longValue()) & sequenceData) == 0) {
          currentTime = nextCurrentTime(currentTime);
        }
      } else {
        sequence.reset();
        sequence.add(new Random().nextInt(4096));
      }
      if (currentTime < lastTimestamp) {
        logger.info("currentTime is error {} , lastTimestamp is {}", currentTime, lastTimestamp);
        throw new IllegalArgumentException("currentTime is error");
      }
      lastTimestamp = currentTime;
      long nextId =
          ((lastTimestamp - twepoch) << twepochShift) | (datacenterId << datacenterIdShift)
              | (workerId << workerShift) | this.sequence.longValue();
      return nextId;
    } finally {
      reentrantLock.unlock();
    }
  }

  private long nextCurrentTime(long currentTime) {
    long nextCurrentTime = System.currentTimeMillis();
    while (nextCurrentTime <= currentTime) {
      nextCurrentTime = System.currentTimeMillis();
    }
    return nextCurrentTime;
  }
}
