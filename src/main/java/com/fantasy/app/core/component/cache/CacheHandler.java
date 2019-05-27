package com.fantasy.app.core.component.cache;

import org.apache.log4j.Logger;
import org.springframework.cache.support.AbstractCacheManager;

import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.component.log.Log;


/**
 * 基于ConcurrentMap的内存缓存策略
 * @日期：2013-1-12上午12:59:22
 * @作者：公众号：18岁fantasy
 */
public abstract class CacheHandler extends AbstractCacheManager{

	private static Logger logger = Log.getLogger(LogType.CACHE);

	protected void putData(String desc,String cacheName, String key, Object value) {
		super.getCache(cacheName).put(key, value);
		printPutTrace(desc, cacheName, key, value);
	}
	protected Object getData(String desc,String cacheName,  String key) {
		Object rs = super.getCache(cacheName).get(key).get();
		printGetTrace(desc, cacheName, key);
		return rs;
	}
	protected void removeData(String desc,String cacheName, String key) {
		super.getCache(cacheName).evict(key);
		printRemoveTrace(desc, cacheName, key);
	}
	/**
	 * 打印语句
	 * @param sql
	 */
	public static void printPutTrace(String desc,String cacheName, String key, Object value) {
		StringBuffer trace = new StringBuffer();
		trace.append("--cache print:").append(desc).append("\r\n");
		trace.append("put into cache[").append(cacheName).append("] [key:").append(key).append("] [value:")
				.append(value).append("]");
		trace(trace.toString());
	}

	/**
	 * 打印语句
	 * @param sql
	 */
	public static void printGetTrace(String desc, String cacheName, String key) {
		StringBuffer trace = new StringBuffer();
		trace.append("--cache print:").append(desc).append("\r\n");
		trace.append("get from cache[").append(cacheName).append("] [key:").append(key).append("]");
		trace(trace.toString());
	}

	/**
	 * 打印语句
	 * @param sql
	 */
	public static void printRemoveTrace(String desc, String cacheName, String key) {
		StringBuffer trace = new StringBuffer();
		trace.append("--cache print:").append(desc).append("\r\n");
		trace.append("remove from cache[").append(cacheName).append("] [key:").append(key).append("]");
		trace(trace.toString());
	}

	private static void trace(String trace) {
		if (logger.isInfoEnabled() || logger.isDebugEnabled()) {
			logger.info(trace);
		} else {
			//System.out.println(trace);
		}
	}
	public static enum CacheOperType{
		INSERT("1","新增"),
		UPDATE("2","修改"),
		DELETE("3","删除");
		private String code;
		private String name;
		CacheOperType(String code,String name){
			this.code = code;
			this.name = name;
		}
		public String getCode() {
			return code;
		}
		public String getName() {
			return name;
		}
	}
}
