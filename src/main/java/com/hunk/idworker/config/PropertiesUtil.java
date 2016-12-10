package com.hunk.idworker.config;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * </pre>
 * 
 * @author: xiongchengwei
 * @date: 2016年3月14日 下午1:20:52
 */
public class PropertiesUtil {
  public static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
  
  private PropertiesUtil(){}

  // 为了读了properties文件
  private static Properties properties = null;

  static {
    loadFile("config.properties");
  }

  /**
   * 处理propertise文件
   * 
   * @param fileConfig 文件的明（路径）
   */
  public static void loadFile(String fileConfig) {
    // 获取输入流
    try {
      String[] files = fileConfig.split(",");
      properties = new Properties();
      InputStream in = null;
      for (String file : files) {
        in = PropertiesUtil.class.getClassLoader().getResourceAsStream(file);
        properties.load(in);
      }
      in.close();
    } catch (Exception e) {
      logger.warn("------读取properties配置文件" + fileConfig + "时，出错:" + e.getMessage().toString());
    }
  }

  /**
   * 读取properties文件，根据key获取key值所对应的value值
   * 
   * @param key
   * @return value 值
   */
  public static String getValue(String key) {
    String value = null;
    try {
      value = properties.getProperty(key);
    } catch (Exception e) {
      logger.warn("根据properties的key值获取value值时出错:" + e);
    }
    return value;
  }

}
