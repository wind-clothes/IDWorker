package com.hunk.idworker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hunk.idworker.id.IdMaker;

public class IdMakerTest {

  private static final Logger logger = LoggerFactory.getLogger(IdMakerTest.class);
  final IdMaker idMaker = new IdMaker(1, 1);
  final ConcurrentMap<Object, Object> map = new ConcurrentHashMap<Object, Object>();

  //@Test(invocationCount = 100000, threadPoolSize = 10, groups = {"test"})
  public void test() throws InterruptedException {
    long id = idMaker.nextId();
    if (!map.containsKey(id)) {
      map.put(id, id);
      logger.info("map size {},id {}",map.size(),id);
    }else {
      logger.info("Repeat is id {}", id);
      System.exit(200);
    }
  }
}
