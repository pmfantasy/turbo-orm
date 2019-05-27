package com.fantasy.app.core.listener.inner;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

import com.fantasy.app.core.base.InterceptorBean.SIGNAL;
import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.boot.BootParam;
import com.fantasy.app.core.component.cache.CacheService;
import com.fantasy.app.core.component.cache.factory.CacheFactory;
import com.fantasy.app.core.component.log.Log;
import com.fantasy.app.core.exception.InitException;
import com.fantasy.app.core.listener.AppInitListener;


/**
 * 缓存linstener
 * @author 公众号：18岁fantasy
 * 2017-4-19 下午4:52:06
 */
public class CacheInitListener implements AppInitListener {
	static Logger logger = Log.getLogger(LogType.SYSINNER);
	@Override
	public void init(ServletContextEvent contextEvent, BootParam bootParam) throws InitException {
        CacheService cacheService = CacheFactory.createCacheService();
        contextEvent.getServletContext().log("初始化缓存监听["+cacheService+"]成功.");
	}
	@Override
	public void destroyed(ServletContextEvent contextEvent, BootParam bootParam) throws InitException {
		 CacheFactory.closeCacheService();
		 contextEvent.getServletContext().log("关闭缓存监听成功.");
	}
	@Override
	public SIGNAL onError(ServletContextEvent contextEvent, BootParam bootParam)
			throws InitException {
		return SIGNAL.STOP;
	}
}
