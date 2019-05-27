package com.fantasy.app.core.util;

import java.util.UUID;

import org.apache.commons.lang.math.RandomUtils;

/**
 * UUID生成器
 * @author 公众号：18岁fantasy
 * @date 2019年5月5日 下午1:19:48
 */
public class UUIDGenerator {
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	
	public static String getUUIDUpperCase() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}

	public static String getRandomCode() {
		return getRandomCode(6);
	}

	public static String getRandomCode(int length) {
		int index = 0;
		if (length > 0 && length < 32) {
			index = RandomUtils.nextInt(getUUID().length() - length);
		}

		return getUUID().substring(index, index + length);
	}

//	public static void main(String[] args) {
//		System.out.println(getRandomCode());
//		System.out.println(getRandomCode(10).toUpperCase());
//		System.out.println(getRandomCode(10).length());
//	}

}
