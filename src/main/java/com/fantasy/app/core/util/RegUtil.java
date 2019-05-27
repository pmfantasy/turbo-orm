package com.fantasy.app.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;


/**
 * 正则表达式工具类
 * @日期：2013-3-11下午4:20:57
 * @作者：公众号：18岁fantasy
 */
public class RegUtil {
	
	/**
	 * 完全匹配
	 * @param toMatchStr
	 * @param regStr
	 * @return
	 */
	public static boolean match(String toMatchStr,String regStr) {
		if(StringUtils.hasText(toMatchStr)&&StringUtils.hasText(regStr)){
			Pattern pattern = Pattern.compile(regStr);
			Matcher matcher = pattern.matcher(toMatchStr);
			return matcher.matches();
		}
		return false;
	}
	/**
	 * 查找对应的字符串
	 * @param toMatchStr
	 * @param regStr
	 * @return
	 */
	public static List<String> findStr(String toMatchStr,String regStr) {
		List<String> rs = new ArrayList<String>();
		if(StringUtils.hasText(toMatchStr)&&StringUtils.hasText(regStr)){
			Pattern pattern = Pattern.compile(regStr);
			Matcher matcher = pattern.matcher(toMatchStr);
			while(matcher.find()){
				rs.add(matcher.group());
			}
		}
		return rs;
	}
	/**
	 * 包含匹配
	 * @param toMatchStr
	 * @param regStr
	 * @return
	 */
	public static boolean find(String toMatchStr,String regStr) {
		if(StringUtils.hasText(toMatchStr)&&StringUtils.hasText(regStr)){
			Pattern pattern = Pattern.compile(regStr);
			Matcher matcher = pattern.matcher(toMatchStr);
			return matcher.find();
		}
		return false;
	}
	/**
	 * 通配符匹配：* 多个 或者0个任意字符 ，？一个任意字符。
	 * @param toMatchStr
	 * @param wildcard
	 * @return
	 */
	public static boolean matchWildcard(String toMatchStr,String wildcard){
		return FilenameUtils.wildcardMatch(toMatchStr, wildcard);
	}
}
