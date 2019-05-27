package com.fantasy.app.core.listener;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.boot.BootParam;
import com.fantasy.app.core.component.log.Log;
import com.fantasy.app.core.util.StrUtil;


/**
 * Spring之前的监听器
 * 该监听器会加载classpath下的boot.xml
 * 并按照boot.xml定义的监听器链加载一系列监听器
 */
public class BeforeSpringListener implements ServletContextListener {
	static Logger logger = Log.getLogger(LogType.SYSINNER);


	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		String msg = "";
		try {
			contextEvent.getServletContext().log("Initializing beforeSpringListener....");
			BootParam bootParam = BootParam.getBootParam();
			if (logger.isInfoEnabled()) {
				logger.info("ModulePackage:" + bootParam.getModulePackage());
				logger.info("BeforeSpringListener:" + bootParam.getBeforeSpringListener());
			}
			//初始化APP自定义的监听器
			List<String> beforeSpringListener = bootParam.getBeforeSpringListener();
			if (null == beforeSpringListener || beforeSpringListener.size() <= 0){
				return;
			}
			for (String listener : bootParam.getBeforeSpringListener()) {
				listener = StrUtil.trim(listener);
				AppInitListener appInitListener = null;
				Class<?> objClazz = Class.forName(listener);
				Object obj = objClazz.newInstance();

				if (logger.isInfoEnabled()) {
					logger.info("BeforeSpringListener: Class.ForName(" + listener + ") and newInstance >>>"
							+ obj.getClass().getName());
				}
				if (obj instanceof AppInitListener) {
					appInitListener = (AppInitListener) obj;
					appInitListener.init(contextEvent, bootParam);
				} else {
					msg = listener + "is Not implements cn.com.dhcc.app.core.web.listener.AppInitListener";
					contextEvent.getServletContext().log(msg);
				}
			}
		} catch (Exception e) {
			contextEvent.getServletContext().log("Initializing beforeSpringListener fail...", e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		try {
			contextEvent.getServletContext().log("Destroying....");
			BootParam bootParam = BootParam.getBootParam();
			if (logger.isInfoEnabled()) {
				logger.info("BeforeSpringListener:" + bootParam.getBeforeSpringListener());
			}

			String msg = "";
			List<String> listeners = bootParam.getBeforeSpringListener();
			if (null == listeners || listeners.size() <= 0){
				return;
			}
			for (String listener : listeners) {
				listener = StrUtil.trim(listener);
				AppInitListener appInitListener = null;
				Class<?> objClazz = Class.forName(listener);
				Object obj = objClazz.newInstance();

				if (logger.isInfoEnabled()) {
					logger.info("AfterSpringListener: Class.ForName(" + listener + ") and newInstance >>>"+ obj.getClass().getName());
				}
				if (obj instanceof AppInitListener) {
					appInitListener = (AppInitListener) obj;
					appInitListener.destroyed(contextEvent, bootParam);
				} else {
					msg = listener + "is Not implements cn.com.dhcc.app.core.web.listener.AppInitListener";
					contextEvent.getServletContext().log(msg);
				}
			}
		} catch (Exception e) {
			logger.error("Destroyed fail:" + e.fillInStackTrace(), e);
		}
	}
}
