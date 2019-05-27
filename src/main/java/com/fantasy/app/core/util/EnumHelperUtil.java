package com.fantasy.app.core.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 枚举帮助类
 *
 * @author 公众号：18岁fantasy
 * @date 2017年08月25日 下午 04:00
 */
public class EnumHelperUtil {

	/**
	 * @param clazz
	 *            枚举类
	 * @param getTypeCodeMethodName
	 *            code的方法名字
	 * @param typeCode
	 *            status
	 * @param getTargetCodeMethodName
	 *            要取的目标值的方法 一般是getter方法
	 * @description
	 * @author 颜培轩
	 * @date 2017/08/26
	 */
	public static <T extends Enum<T>> String getNameByStringTypeCode(Class<T> clazz, String getTypeCodeMethodName,
			String typeCode, String getTargetCodeMethodName) {
		if (typeCode == null) {
			return "";
		}
		String targetCodeVal = null;
		try {
			T[] arr = clazz.getEnumConstants();
			Method typeCodeMethod = clazz.getDeclaredMethod(getTypeCodeMethodName);
			Method targetCodeMethod = clazz.getDeclaredMethod(getTargetCodeMethodName);
			for (T entity : arr) {
				String typeCodeVal = typeCodeMethod.invoke(entity).toString();
				if (typeCodeVal.contentEquals(typeCode)) {
					targetCodeVal = targetCodeMethod.invoke(entity).toString();
				}
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return targetCodeVal;
	}
}
