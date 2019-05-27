package com.fantasy.app.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
/**
 * 时间util
 * @author 公众号：18岁fantasy
 * 2017年7月5日 上午8:55:46
 */
public class DateUtil {
	public static final String FMT_yyyymmdd = "yyyy-MM-dd";
	public static final String FMT_yyyymmddHHmmss = "yyyy-MM-dd HH:mm:ss";
	public static final String FMT_yyyymmddhh24mmssSSS = "yyyy-MM-dd HH:mm:ss.sss";
	public static final String FMT_DB_yyyymmddhh24miss = "yyyy-MM-dd hh24:mi:ss";

	public static final String FMT_DB_yyyymmddhh24missff = "yyyy-MM-dd hh24:mi:ss.ff";
	
	
	
	public  static String formatDate(Date date,String pattern){
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}
	public static Date parseDate(String source,String pattern){
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		try {
			return dateFormat.parse(source);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**type GregorianCalendar.YEAR 年
	 *      
	 * 日期的加减运算 
	 * @param date
	 * @param type GregorianCalendar.YEAR,GregorianCalendar.Month
	 * @param num 如果是加，num就是整数，如果是减，num就是负数
	 * @return
	 */
	public static Date operation(Date date,int type,int num){
		GregorianCalendar gc=new GregorianCalendar();
		gc.setTime(date);
		gc.add(type, num);
		return gc.getTime();
	}
	/**
	 * 默认：将日期字符串转换为日期类型
	 * 
	 * @param dateStr
	 *            yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date toDefaultDateTime(String dateStr) {
		DateFormat dateFormat = new SimpleDateFormat(FMT_yyyymmddHHmmss);
		try {
			return  dateFormat.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 默认：将日期字符串转换为日期类型
	 * 
	 * @param dateStr
	 *            yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date toDefaultDate(String dateStr) {
		DateFormat dateFormat = new SimpleDateFormat(FMT_yyyymmdd);
		try {
			return  dateFormat.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 默认：将日期格式化为字符串 yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String toDefaultDateStr(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(FMT_yyyymmdd);
		return  dateFormat.format(date);
	}
	/**
	 * 默认：将日期格式化为字符串 yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String toDefaultDateTimeStr(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(FMT_yyyymmddHHmmss);
		return  dateFormat.format(date);
	}
//	public static void main(String[] args) {
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//		System.out.println(sdf.format(DateUtil.operation(new Date(), GregorianCalendar.DAY_OF_MONTH, -11)));
//		System.out.println(sdf.format(DateUtil.operation(new Date(), GregorianCalendar.MONTH, -1)));
//	}
}
