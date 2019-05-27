package com.fantasy.app.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标示表名称，如果没有此注解，会将类名作为表名，
 * @日期：2012-12-14下午11:05:20
 * @作者：公众号：18岁fantasy
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WsdTable {

	/**
	 * 此表在数据的schema名称
	 * @return
	 */
	 String schema()default "";
	/**
	 * 表名称
	 * @return
	 */
	String name() default "";
	/**
	 * 是否sharding 分表 需要在class-path 的名为table_sharding.xml里面查找和当前表名对应的分区配置
	 * @return
	 */
	boolean sharding() default false;
	
}
