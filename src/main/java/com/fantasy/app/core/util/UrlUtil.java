package com.fantasy.app.core.util;

import java.net.URL;

import org.springframework.util.StringUtils;
/**
 * url 相关处理
 * @author 公众号：18岁fantasy
 * @2014-6-30 @下午4:09:44
 */
public class UrlUtil {

	/**
	 * 去掉url后面的“/”和空格
	 * @param nodeInstallUrl
	 * @return
	 */
	public static String removeSlashAndBlack(String url){
		if(StringUtils.hasText(url)){
			url = url.trim();
			if(url.trim().endsWith("/")||url.trim().endsWith("\\")){
				url = url.substring(0, url.length()-1);
				return removeSlashAndBlack(url);
			}else{
				return url;
			}
		}
		return url;
	}
	public static String getIpFromUrl(String urlStr) throws Exception{
		URL url;
		try {
    		url = new URL(urlStr);
    		return url.getHost();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
