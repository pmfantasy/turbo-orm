package com.fantasy.app.core.listener;

import javax.servlet.ServletContextEvent;

import com.fantasy.app.core.base.InterceptorBean.SIGNAL;
import com.fantasy.app.core.boot.BootParam;
import com.fantasy.app.core.exception.InitException;

/**
 * 项目启动器接口
 * @author 公众号：18岁fantasy
 * 2017-5-25 下午3:19:15
 */
public interface AppInitListener {
	public void init(ServletContextEvent contextEvent, BootParam bootParam) throws InitException;

	public void destroyed(ServletContextEvent contextEvent, BootParam bootParam) throws InitException;
	
	public SIGNAL onError(ServletContextEvent contextEvent, BootParam bootParam) throws InitException;
}
