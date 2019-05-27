package com.fantasy.app.core.component.cache;

import java.util.List;
import java.util.Map;

import com.fantasy.app.core.base.cache.QueryContent;

/**
 * 缓存接口
 * @author 公众号：18岁fantasy
 * 2017-5-25 下午3:21:15
 */
public interface CacheService {

	/**
	 * 启动缓存服务
	 */
	public void startCacheService();

	/**
	 * 关闭缓存服务
	 */
	public void shutdownService();

	// 缓存服务管理 结束

	// 查询操作
	/**
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public <T> T getFromCacheByKey(String cacheName,String desc, Object key);

	/**
	 * 
	 * @param cacheName
	 *            cacheName 缓存组名称
	 * @param query
	 * @return
	 */
	public <T> T getFromCacheByQuery(String cacheName, String desc,QueryContent query);

	/**
	 * 条件查询缓存聚合信息
	 * 
	 * @param cacheName
	 *            缓存组名称
	 * @param query
	 *            查询条件对象
	 * @return
	 */
	public Object[] queryAggregators(String cacheName, String desc,String[] fieldsToQuery,QueryContent query);
	
	/**
	 * 条件查询缓存信息
	 * 
	 * @param cacheName
	 *            缓存组名称
	 * @param query
	 *            查询条件对象
	 * @return
	 */
	public <T> List<T> queryValues(String cacheName, String desc,QueryContent query);

	/**
	 * 获取当前缓存组中的所有缓存对象，前提是：缓存对象类型唯一
	 * 
	 * @param cacheName
	 *            缓存名称
	 * @param query
	 *            查询条件
	 * @return
	 */
	public <T> List<T> getAllSingleTypeObjectsFromCache(String cacheName,String desc,
			Class<? extends T> clazz);

	/**
	 * 获取当前缓存组中的所有缓存对象，对象队形可以不唯一
	 * 
	 * @param cacheName
	 *            缓存名称
	 * @param query
	 *            查询条件
	 * @return
	 */
	public List<Object> getMultTypeObjectsFromCache(String cacheName,String desc);

	// 查询操作 结束

	// 插入操作
	/**
	 * 插入单个对象
	 * 
	 * @param cacheName
	 *            缓存名
	 * @param key
	 *            要换成对象的key值
	 * @param value
	 *            要缓存的对象
	 */
	public void insertIntoCache(String cacheName,String desc, Object key, Object value);

	/**
	 * 批量插入缓存对象
	 * 
	 * @param cacheName
	 *            缓存名
	 * @param keyValues
	 *            缓存对象
	 */
	public void batchInsertIntoCache(String cacheName,String desc,
			Map<Object, Object> keyValues);

	/**
	 * 批量插入对象到缓存，
	 * 
	 * @param cacheName
	 *            缓存名称
	 * @param values
	 *            对象列表
	 * @param keyFieldName
	 *            要作为主键的对象属性，将通过反射获取此属性的值，并将其作为缓存的对象的key
	 */
	public void batchInsertIntoCache(String cacheName,String desc, List<?> values,
			String keyFieldName);

	// 插入操作 结束

	// 删除操作
	public void removeFromCache(String cacheName,String desc, Object key);

	public void removeAllFromCache(String cacheName,String desc);
}
