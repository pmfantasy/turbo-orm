package com.fantasy.app.core.util;


/**
 * 负载均衡，hash取模
 * @author 公众号：18岁fantasy
 * @2015-4-20 @下午4:04:09
 */
public class HashSelect {

	/**
	 * 
	 * @param key 存储对象的
	 * @param targetCount 待选择目的地的个数
	 * @param defaultTarget 默认选择的目的地
	 * @return 目的地
	 */
	public static int target(String[] key,int targetCount,int defaultTarget){
		if(targetCount!=0&&key != null&&key.length!=0 ){
			StringBuffer keys = new StringBuffer();
			for (int i = 0; i < key.length; i++) {
				keys.append(key[i]);
			}
			int hash = keys.toString().hashCode();
			return Math.abs(hash%targetCount);
		}
		return defaultTarget;
	}
	public static int target(String[] key,int targetCount){
		return target(key, targetCount,0);
	}
}
