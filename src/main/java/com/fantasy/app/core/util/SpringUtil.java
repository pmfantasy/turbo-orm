package com.fantasy.app.core.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
/**
 * spring util
 * @author 公众号：18岁fantasy
 * @2015-7-21 @上午11:56:12
 */
public class SpringUtil {

	/**
	 * @TODO 通过spring获取request，多线程下有问题，因为是放在request线程的threadlocal里面的
	 * @return
	 */
	public static HttpServletRequest getRequest(){
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		return servletRequestAttributes!=null?servletRequestAttributes.getRequest():null;  
	}
	
	/**
	 * @TODO 通过spring获取sesssion，多线程下有问题，因为是放在request线程的threadlocal里面的
	 * @return
	 */
	public static HttpSession getSession(){
		HttpServletRequest request = getRequest();
		return request!=null?request.getSession():null;
	}
}
