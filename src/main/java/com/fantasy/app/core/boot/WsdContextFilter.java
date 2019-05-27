package com.fantasy.app.core.boot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.component.log.Log;
import com.fantasy.app.core.filter.AppInitFilter;


/**
 * filter管理类
 * @author 公众号：18岁fantasy
 * 2017-5-27 下午5:09:34
 */
public class WsdContextFilter implements Filter{
    
	private static  Logger logger = Log.getLogger(LogType.SYSINNER);

	private static List<AppInitFilter> registeredInterceptorBeans = new ArrayList<AppInitFilter>();
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		
	}

	@Override
	public void destroy() {
	
		
	}

}
