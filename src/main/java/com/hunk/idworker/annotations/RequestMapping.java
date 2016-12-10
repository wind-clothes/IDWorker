package com.hunk.idworker.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <pre>
 * </pre>
 * 
 * @author: xiongchengwei
 * @date: 2016年3月15日 下午2:00:50
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
@Target({ElementType.TYPE})
public @interface RequestMapping {
  String name() default "";

  String uri() default "";
}
