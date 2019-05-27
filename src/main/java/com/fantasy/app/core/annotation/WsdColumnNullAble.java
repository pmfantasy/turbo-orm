package com.fantasy.app.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 如果数据库的字段不能为空，用此字段标示，并设置值为false
 * @日期：2012-12-14下午11:04:59
 * @作者：公众号：18岁fantasy
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WsdColumnNullAble {

	/**
	 * 用来标示此字段在数据库是否可为空
	 * @return
	 */
	boolean value() default true;
	
}

