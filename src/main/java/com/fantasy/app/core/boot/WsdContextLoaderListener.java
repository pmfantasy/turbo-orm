package com.fantasy.app.core.boot;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;

import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.component.log.Log;
import com.fantasy.app.core.exception.InitException;
import com.fantasy.app.core.listener.AfterSpringListener;
import com.fantasy.app.core.listener.BeforeSpringListener;
import com.fantasy.app.core.listener.inner.CacheInitListener;
import com.fantasy.app.core.listener.inner.CoreParamInitListener;
import com.fantasy.app.core.listener.inner.DaoInitListener;

/**
 * 项目监听器，无需另外配置spring的监听
 * @author 公众号：18岁fantasy
 * 2017-5-26 下午2:00:00
 */
public class WsdContextLoaderListener extends ContextLoaderListener{
	private Logger logger = Log.getLogger(LogType.SYSINNER);
	
	private BeforeSpringListener beforeSpringListener = null;
	private AfterSpringListener afterSpringListener = null;
	/**
	 * 启动监听
	 */
	public void contextInitialized(ServletContextEvent event) {
		try {
			//初始化boot配置文件
			BootParam bootParam = BootParam.getBootParam();
			//初始化内部需要在spring之前初始化的listener
			initBeforeSpringCoreListener(event,bootParam);
			//spring之前监听-用户配置的
			beforeSpringListener = new BeforeSpringListener();
			beforeSpringListener.contextInitialized(event);
			//spring 监听
			super.contextInitialized(event);
			//初始化内部需要在spring之后初始化的listener
			initAfterSpringCoreListener(event,bootParam);
			//spring之后监听-用户配置的
			afterSpringListener = new AfterSpringListener();
			afterSpringListener.contextInitialized(event);
		} catch (Exception e) {
			logger.error("由于项目监听启动失败,已停止项目启动，请检查问题后重新启动...",e);
			contextDestroyed(event);
		}
	}
    /**
	 * 关闭监听
	 */
	public void contextDestroyed(ServletContextEvent event) {
		try {
			//spring之前监听-用户配置的
			beforeSpringListener.contextDestroyed(event);
			//spring 监听
			super.contextDestroyed(event);
			//spring之后监听-用户配置的
			afterSpringListener.contextDestroyed(event);
			
			//内部listener，在所有监听之后关闭
			destoryCoreListener(event);
		} catch (Exception e) {
			logger.error("关闭监听失败.",e);
			//只能成功不能失败
		}
		
	} 
	//before
	private CoreParamInitListener coreParamInitListener = null;
	private CacheInitListener cacheInitListener = null;
	//after
	private DaoInitListener daoInitListener = null;
	/**
	 * 初始化内置的
	 * @param event
	 * @throws InitException 
	 */
	private void initBeforeSpringCoreListener(ServletContextEvent event,BootParam bootParam) throws InitException {
		try {
			coreParamInitListener = new CoreParamInitListener();
			coreParamInitListener.init(event,bootParam);
			
			cacheInitListener = new CacheInitListener();
			cacheInitListener.init(event, bootParam);
		} catch (InitException e) {
			throw e;
		}
	}
	private void initAfterSpringCoreListener(ServletContextEvent event,BootParam bootParam) throws InitException {
		try {
			daoInitListener = new DaoInitListener();
			daoInitListener.init(event,bootParam);
		} catch (InitException e) {
			throw e;
		}
	}
	private void destoryCoreListener(ServletContextEvent event) throws InitException {
		coreParamInitListener.destroyed(event, null);
		daoInitListener.destroyed(event, null);
		cacheInitListener.destroyed(event,null);
	}
}
