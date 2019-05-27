package com.fantasy.app.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 *  添加此注解，表示不需要登陆就可以访问资源
 * @日期：2012-12-14下午11:04:47
 * @作者：公众号：18岁fantasy
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WsdNoNeedLogin {

	
}