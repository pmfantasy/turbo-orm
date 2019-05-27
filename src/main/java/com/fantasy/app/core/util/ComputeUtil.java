package com.fantasy.app.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 计算器
 * @author 公众号：18岁fantasy
 * @2014-11-7 @下午2:29:45
 */
public class ComputeUtil {

	static enum SIZE{
		B(1,1024),
		K(1024,1048576),
		M(1048576,1073741824),
		G(1073741824,1099511627776d),
		T(1099511627776d,1125899906842624d),
		P(1125899906842624d,1.152921504606847e+18);
		double min, max;
		SIZE(double min,double max){
			this.min = min;
			this.max = max;
		}
	}
	/**
	 * 根据字节优化显示
	 * @param byteVal
	 * @return
	 */
	public static String formatByte(String byteVal){
		
		DecimalFormat df1 = new DecimalFormat("#.##"); 
		BigDecimal bigValue = null;
		try {
			bigValue = new BigDecimal(byteVal);
		} catch (Exception e) {
			return byteVal;
		}
		
		double byteValue = bigValue.doubleValue();
		if(byteValue<=0)return byteVal+SIZE.B.name();
		
		SIZE[] ss = SIZE.values();
		for (int i = 0; i < ss.length; i++) {
			if(byteValue>=ss[i].min&&byteValue<ss[i].max){
				return df1.format(byteValue/ss[i].min)+ss[i].name();
			}
		}
		return byteVal;//too long to support!!
		
	}
//	public static void main(String[] args) {
//		System.out.println(formatByte("112400"));
//		System.out.println(formatByte("1073741824"));
//	}
}
