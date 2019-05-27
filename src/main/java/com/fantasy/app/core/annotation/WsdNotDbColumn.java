package com.fantasy.app.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标示此属性非数据库字段
 * @日期：2012-12-14下午11:05:10
 * @作者：公众号：18岁fantasy
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WsdNotDbColumn {

}