package com.fantasy.app.core.listener;

import java.util.List;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fantasy.app.core.base.CmCommon;
import com.fantasy.app.core.base.InterceptorBean;
import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.boot.AppInterceptor;
import com.fantasy.app.core.boot.BootParam;
import com.fantasy.app.core.component.log.Log;
import com.fantasy.app.core.interceptor.inner.ParamSetInterceptor;
import com.fantasy.app.core.interceptor.inner.RefererInterceptor;
import com.fantasy.app.core.util.CollectionUtil;
import com.fantasy.app.core.util.StrUtil;


/**
 * 应用初始化类，在spring容器之后执行
 * @日期：2012-12-14下午11:23:21
 * @作者：公众号：18岁fantasy
 */
public class AfterSpringListener{
	static Logger logger = Log.getLogger(LogType.SYSINNER);

	public void contextInitialized(ServletContextEvent contextEvent) {
		try {
			contextEvent.getServletContext().log("AfterSpringListener Initializing....");
			BootParam bootParam = BootParam.getBootParam();
			if (logger.isInfoEnabled()) {
				logger.info("AfterSpringListener:" + bootParam.getAfterSpringListener());
			}

			ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(contextEvent.getServletContext());
			
			//设置SpringContext
			CmCommon.setContext(context);
			contextEvent.getServletContext().log("CM init successed.");
			//注册拦截器
			registerInterceptor(bootParam);
			
			//启动自定义的监听
			initListener(contextEvent,bootParam);
		} catch (Exception e) {
			contextEvent.getServletContext().log("Initializing fail:", e);
		}
	}

	public void contextDestroyed(ServletContextEvent contextEvent) {
		try {
			contextEvent.getServletContext().log("Destroyed Initializing....");
			BootParam bootParam = BootParam.getBootParam();
			if (logger.isInfoEnabled()) {
				logger.info("AfterSpringListener:" + bootParam.getAfterSpringListener());
			}

			String msg = "";
			List<String> afterSpringListener = bootParam.getAfterSpringListener();
			if (afterSpringListener != null && afterSpringListener.size() > 0) {
				for (String listener : afterSpringListener) {
					listener = StrUtil.trim(listener);
					AppInitListener appInitListener = null;
					Class<?> objClazz = Class.forName(listener);
					Object obj = objClazz.newInstance();

					if (logger.isInfoEnabled()) {logger.info("AfterSpringListener: Class.ForName(" + listener + ") and newInstance >>>"+ obj.getClass().getName());
					}
					if (obj instanceof AppInitListener) {
						appInitListener = (AppInitListener) obj;
						appInitListener.destroyed(contextEvent, bootParam);
					} else {
						msg = listener + "is Not implements cn.com.dhcc.app.core.web.listener.AppInitListener";
						contextEvent.getServletContext().log(msg);
					}
				}
			}

		} catch (Exception e) {
			logger.error("Destroyed fail:" + e.fillInStackTrace(), e);
		}
	}

	
	/**
	 * 注册监听器
	 * @param contextEvent
	 * @param bootParam
	 * @throws Exception
	 */
	private void initListener(ServletContextEvent contextEvent, BootParam bootParam) throws Exception {
		String msg = "";
		List<String> afterSpringListener = bootParam.getAfterSpringListener();
		if (afterSpringListener != null && afterSpringListener.size() > 0) {
			for (String listener : afterSpringListener) {
				listener = StrUtil.trim(listener);
				AppInitListener appInitListener = null;
				Class<?> objClazz = Class.forName(listener);
				Object obj = objClazz.newInstance();

				if (logger.isInfoEnabled()) {
					logger.info("AfterSpringListener: Class.ForName(" + listener + ") and newInstance >>>"+ obj.getClass().getName());
				}
				if (obj instanceof AppInitListener) {
					appInitListener = (AppInitListener) obj;
					appInitListener.init(contextEvent, bootParam);
				} else {
					msg = listener + "is Not implements cn.com.dhcc.app.core.web.listener.AppInitListener";
					contextEvent.getServletContext().log(msg);
				}
			}
		}
	}
	
	
	
	//注册拦截器链
	@SuppressWarnings("unchecked")
	private void registerInterceptor(BootParam bootParam) throws ClassNotFoundException {
		int order = 0;
		List<String> interceptors = bootParam.getInterceptors();
		if(CollectionUtil.hasElement(interceptors)){
			for (int i = 0; i < interceptors.size(); i++) {
				String className = interceptors.get(i);
				className = StrUtil.trim(className);
				Class<? extends InterceptorBean> clazz = (Class<? extends InterceptorBean>) Class.forName(className);
				AppInterceptor.register(clazz, i);
				order++;
			}
		}
		//内置的拦截器
		AppInterceptor.register(RefererInterceptor.class, order + 1);
		AppInterceptor.register(ParamSetInterceptor.class, order + 2);
		
		AppInterceptor.sort();
	}
}
