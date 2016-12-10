package com.hunk.idworker.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hunk.idworker.annotations.RequestMapping;
import com.hunk.idworker.handler.HttpResponseHandler;

/**
 * <pre>
 * </pre>
 * 
 * @author: xiongchengwei
 * @date: 2016年3月15日 下午2:13:41
 */
public class HandlerFactory {
  private static final Logger logger = LoggerFactory.getLogger(HandlerFactory.class);

  private HandlerFactory() {}

  private static final String packageName = "cc.uworks.reactor.idmaker.handler";
  private static final Map<String, HttpResponseHandler> allHandlers =
      new HashMap<String, HttpResponseHandler>();

  static {
    Set<Class<?>> classies = loadHandlers(packageName);
    for (Class<?> clazz : classies) {
      RequestMapping mapping = clazz.getAnnotation(RequestMapping.class);
      // String name = mapping.name();
      String uri = mapping.uri();
      if (uri != null && !"".equals(uri)) {
        try {
          allHandlers.put(uri, (HttpResponseHandler) clazz.newInstance());
        } catch (InstantiationException e) {
          logger.info("InstantiationException", e);
        } catch (IllegalAccessException e) {
          logger.info("IllegalAccessException", e);
        }
      }
    }
  }

  public static Set<Class<?>> loadHandlers(String packageName) {
    Reflections reflections = new Reflections(packageName);
    Set<Class<?>> urlHandlers = reflections.getTypesAnnotatedWith(RequestMapping.class);
    return urlHandlers;
  }

  public static Map<String, HttpResponseHandler> requestHandlerList() {
    return allHandlers;
  }
}
