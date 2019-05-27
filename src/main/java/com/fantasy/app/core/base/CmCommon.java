package com.fantasy.app.core.base;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.fantasy.app.core.component.cache.CacheService;
import com.fantasy.app.core.component.cache.factory.CacheFactory;
import com.fantasy.app.core.component.db.factory.DaoFactory;
import com.fantasy.app.core.util.StrUtil;


/**
 * 服务提供类,
 * 注：spring加载完成之后才可使用，如要在spring加载期间使用某个服务，需使用注入方式注入BaseService.或者extends BaseService例如：@autoware BaseService
 * 
 * @日期：2012-12-14下午11:22:45
 * @作者：公众号：18岁fantasy
 */
public class CmCommon {

	/**
	 * 获取数据库操作类
	 * 
	 * @return
	 */
	public static Dao getDao() {
		return (Dao) DaoFactory.getDao();
	}
	/**
	 * 获取数据库操作类
	 * 
	 * @return
	 */
	public static Dao getDao(String dateSourceName) {
		if(StrUtil.isBlank(dateSourceName)){
			return getDao();
		}
		return (Dao) DaoFactory.getDao(dateSourceName);
	}


	/**
	 * 获取缓存服务类
	 * 
	 * @return
	 */
	public static CacheService getCacheService() {
		return CacheFactory.getCacheService();
	}
	// -----------------------------------------------------
	public static Object getBeanByName(String beanName) {
		return context.getBean(beanName);
	}

	public static <T> Map<String, T> getBeanByType(Class<T> type) {
		return context.getBeansOfType(type);
	}

	public static <T> T getBean(Class<T> type) {
		return context.getBean(type);
	}

	private static ApplicationContext context;

	/**
	 * inject by @InitListener after spring context inited...
	 * 
	 * @param contextinject
	 */
	public static void setContext(ApplicationContext contextinject) {
		if (context != null) {
			return;
		}
		context = contextinject;
	}
}
