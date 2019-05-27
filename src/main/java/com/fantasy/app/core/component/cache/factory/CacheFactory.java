package com.fantasy.app.core.component.cache.factory;

import com.fantasy.app.core.component.cache.CacheService;
import com.fantasy.app.core.component.cache.impl.EhCacheServiceImpl;

/**
 * 创建cache的工厂
 * @author 公众号：18岁fantasy
 * 2017-5-26 下午3:12:00
 */
public class CacheFactory {

	private static CacheService cacheService = null;
	
	public static CacheService getCacheService(){
		if(cacheService==null){
			createCacheService();
		}
		return cacheService;
	}
	public static CacheService createCacheService(){
		cacheService = new EhCacheServiceImpl();
		cacheService.startCacheService();
		return cacheService;
	} 
	public static void closeCacheService(){
		if(cacheService!=null){
			cacheService.shutdownService();
		}
	}
}
