package com.fantasy.app.core.base;

/**
 * 如果implements 此类，将会被注入 request请求头中的的referer，可用于返回前页使用
 * @日期：2012-12-14下午11:21:04
 * @作者：公众号：18岁fantasy
 */
public interface RefererAble {

	public void setReferer(String referer);
	public String getReferer();
	
}
