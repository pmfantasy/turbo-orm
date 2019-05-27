package com.fantasy.app.core.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.fantasy.app.core.base.InterceptorBean.SIGNAL;
import com.fantasy.app.core.boot.BootParam;
import com.fantasy.app.core.exception.InitException;


/**
 * 项目自定义filter接口
 * @author 公众号：18岁fantasy
 * 2017-5-27 下午5:14:05
 */
public interface AppInitFilter {

	/**
	 * 初始化
	 * @param contextEvent
	 * @param bootParam
	 * @throws InitException
	 */
	public void init(FilterConfig filterConfig, BootParam bootParam) throws InitException;

	/**
	 * 关闭
	 */
	public void destroy() throws InitException;
	/**
	 * 执行filter
	 * @param contextEvent
	 * @param bootParam
	 * @throws InitException
	 */
	public void doFilter(ServletRequest request, ServletResponse response, BootParam bootParam) throws InitException;
	/**
	 * filter成功之后是否继续后续的filter
	 * @param contextEvent
	 * @param bootParam
	 * @return
	 * @throws InitException
	 */
	public SIGNAL onSuccess(ServletContextEvent contextEvent, BootParam bootParam) throws InitException;
	/**
	 * filter失败之后是否继续后续的filter
	 * @param contextEvent
	 * @param bootParam
	 * @return
	 * @throws InitException
	 */
	public SIGNAL onError(ServletContextEvent contextEvent, BootParam bootParam) throws InitException;
}
