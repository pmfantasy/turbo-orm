package com.fantasy.app.core.component.db.orm.valuebean;

import org.springframework.util.StringUtils;

/**
 * 将给定的值封装成like值
 * @日期：2012-12-18下午5:15:46
 * @作者：公众号：18岁fantasy
 */
public class LikeValue{
	
	public static String beforeLike(String value){
		if(!StringUtils.hasText(value))return null;
		return "%"+value;
	} 
	public static String afterLike(String value){
		if(!StringUtils.hasText(value))return null;
		return value+"%";
	}
	public static String roundLike(String value){
		if(!StringUtils.hasText(value))return null;
		return "%"+value+"%";
	}
}
