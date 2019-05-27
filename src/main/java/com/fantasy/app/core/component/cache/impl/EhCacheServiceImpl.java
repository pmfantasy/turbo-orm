package com.fantasy.app.core.component.cache.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Direction;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;
import net.sf.ehcache.search.aggregator.Aggregator;
import net.sf.ehcache.search.expression.Between;
import net.sf.ehcache.search.expression.Criteria;
import net.sf.ehcache.search.expression.EqualTo;
import net.sf.ehcache.search.expression.GreaterThan;
import net.sf.ehcache.search.expression.GreaterThanOrEqual;
import net.sf.ehcache.search.expression.ILike;
import net.sf.ehcache.search.expression.InCollection;
import net.sf.ehcache.search.expression.IsNull;
import net.sf.ehcache.search.expression.LessThan;
import net.sf.ehcache.search.expression.LessThanOrEqual;
import net.sf.ehcache.search.expression.NotEqualTo;
import net.sf.ehcache.search.expression.NotNull;

import org.apache.log4j.Logger;

import com.fantasy.app.core.base.cache.QueryContent;
import com.fantasy.app.core.base.cache.QueryItem;
import com.fantasy.app.core.base.cache.QueryItem.QueryType;
import com.fantasy.app.core.component.cache.CacheService;

/**
 * encache 缓存实现
 * @author 公众号：18岁fantasy
 * 2017-4-17 下午4:32:11
 */
public class EhCacheServiceImpl implements CacheService {
	
	static Logger logger = Logger.getLogger("CACHE");
	
	private static CacheManager cacheManager;

	/**
	 * 启动缓存服务
	 */
	public synchronized void startCacheService() {
		//默认寻找名问ehcache.xml的缓存配置文件
		if(cacheManager==null){
			cacheManager = CacheManager.create();
			logger.info("cache service is started...");
		}
	}

	/**
	 * 关闭缓存服务
	 */
	public synchronized void shutdownService() {
		//默认寻找名问ehcache.xml的缓存配置文件
		if(cacheManager!=null){
			cacheManager.shutdown();
			logger.info("cache service is shutdown...");
		}
	}

	/**
	 * 根据名称获取缓存
	 * @param cacheName
	 * @return
	 */
	protected  Cache getCache(String cacheName) {
		return cacheManager.getCache(cacheName);
	}
	// 缓存服务管理 结束

	// 查询操作
	/**
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T  getFromCacheByKey(String cacheName,String desc, Object key) {
		Element element = getCache(cacheName).get(key); 
		if(element!=null){
			return (T)element.getObjectValue();
		}
		return null;
	}
	/**
	 * 
	 * @param cacheName cacheName 缓存组名称
	 * @param query
	 * @return
	 */
	public <T> T getFromCacheByQuery(String cacheName,String desc, QueryContent queryContent) {
		List<T> rs = queryValues(cacheName,desc, queryContent);
		return rs.get(0);
	}
	/**
	 * 条件查询缓存聚合信息
	 * @param cacheName 缓存组名称
	 * @param fieldsToQuery 除了聚合值意外，还需要查询的值
	 * @param queryContent 查询条件对象
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object[] queryAggregators(String cacheName,String desc,String[] fieldsToQuery, QueryContent queryContent){
		List<Object> list = new ArrayList<Object>();
		Query query  = getCache(cacheName).createQuery();
		addGroupBy(query, queryContent);
		addAggregator(query,queryContent);
		addCriteria(query, queryContent);
		addOrderBy(query, queryContent);
		List<Attribute> attributes = addAttribute(query,fieldsToQuery);
		Results rs = query.execute();
		if(rs.size()!=0){
			List<Result> results = rs.all();
			if(rs.hasAggregators()){
				//如果有分组会有多条记录
				for (Result result : results) {
					if(rs.hasAttributes()&&attributes!=null){
						for (Iterator<Attribute> iterator = attributes.iterator(); iterator
								.hasNext();) {
							Attribute attribute =  iterator.next();
							list.add(result.getAttribute(attribute));
						}
					}
					list.add(result.getAggregatorResults());
				 }
			}
		}
		return list.toArray();
	}
	/**
	 * 条件查询缓存值信息
	 * @param cacheName 缓存组名称
	 * @param query 查询条件对象
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> queryValues(String cacheName,String desc, QueryContent queryContent){
		List<T> list = new ArrayList<T>();
		Query query  = getCache(cacheName).createQuery();
		query.includeValues();
		addCriteria(query, queryContent);
		addGroupBy(query, queryContent);
		addOrderBy(query, queryContent);
		Results rs = query.execute();
		if(rs.size()!=0){
			List<Result> results = rs.all();
			if(rs.hasValues()){
				 for (Result result : results) {
					list.add((T)result.getValue());
				 }
			}
		}
		return list;
	}
	/**
	 * 获取当前缓存组中的所有缓存对象，前提是：缓存对象类型唯一
	 * @param cacheName 缓存名称 
	 * @param query 查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getAllSingleTypeObjectsFromCache(String cacheName,String desc,Class<? extends T> clazz) {
		Cache cache = getCache(cacheName);
		@SuppressWarnings("rawtypes")
		List keys = cache.getKeys();
		Map<Object, Element> kvs = cache.getAll(keys);
		if(kvs!=null&&!kvs.isEmpty()){
			List<T> rs = new ArrayList<T>();
			for (Iterator<Element> iterator = kvs.values().iterator(); iterator.hasNext();) {
				Element e = (Element) iterator.next();
				rs.add((T)e.getObjectValue());
			}
			return rs;
		}
		return null;
	}
	/**
	 * 获取当前缓存组中的所有缓存对象，对象队形可以不唯一
	 * @param cacheName 缓存名称 
	 * @param query 查询条件
	 * @return
	 */
	public List<Object> getMultTypeObjectsFromCache(String cacheName,String desc) {
		Cache cache = getCache(cacheName);
		@SuppressWarnings("rawtypes")
		List keys = cache.getKeys();
		Map<Object, Element> kvs = cache.getAll(keys);
		if(kvs!=null&&!kvs.isEmpty()){
			List<Object> rs = new ArrayList<Object>();
			for (Iterator<Element> iterator = kvs.values().iterator(); iterator.hasNext();) {
				Element e = (Element) iterator.next();
				rs.add(e.getObjectValue());
			}
			return rs;
		}
		return null;
	}
	// 查询操作 结束

	// 插入操作
	/**
	 * 插入单个对象
	 * @param cacheName 缓存名
	 * @param key 要换成对象的key值
	 * @param value 要缓存的对象
	 */
	public void insertIntoCache(String cacheName,String desc, Object key, Object value) {
		Cache cache = getCache(cacheName);
		Element element = new Element(key, value);
		cache.put(element);
	}
 
	/**
	 * 批量插入缓存对象
	 * @param cacheName 缓存名
	 * @param keyValues 缓存对象
	 */
	public void batchInsertIntoCache(String cacheName,String desc,
			Map<Object, Object> keyValues) {
		Cache cache = getCache(cacheName);
		if(keyValues!=null&&!keyValues.isEmpty()){
			for (Iterator<Object> iterator = keyValues.keySet().iterator(); iterator.hasNext();) {
				Object k = (Object) iterator.next();
				Element element = new Element(k,keyValues.get(k));
				cache.put(element);
			}
		}
	}

	/**
	 * 批量插入对象到缓存，
	 * @param cacheName 缓存名称 
	 * @param values 对象列表
	 * @param keyFieldName 要作为主键的对象属性，将通过反射获取此属性的值，并将其作为缓存的对象的key
	 */
	public void batchInsertIntoCache(String cacheName, String desc,List<?> values,
			String keyFieldName) {
		List<Element> es = convertToElement(values, keyFieldName);
		if(es!=null){
			Cache cache = getCache(cacheName);
			cache.putAll(es);
		}
	}
	/**
	 * 转换为缓存的element
	 * @param values 对象列表
	 * @param keyFieldName 对象主键名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<Element> convertToElement(List values,String keyFieldName){
		List<Element> elements = null;
		if(values!=null&&!values.isEmpty()){
			elements = new ArrayList<Element>();
			try {
				Class<?> objType = values.get(0).getClass();
				Field field = objType.getDeclaredField(keyFieldName);
				PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(),  
						objType); 
				Method getMethod = descriptor.getReadMethod();
				for (Iterator<?> iterator = values.iterator(); iterator.hasNext();) {
					Object object = (Object) iterator.next();
					Object key = getMethod.invoke(object, new Object[]{});
					elements.add(new Element(key, object));
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			}catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return elements;
	}

	// 插入操作 结束

	// 删除操作
	public void removeFromCache(String cacheName,String desc, Object key) {

	}

	public void removeAllFromCache(String cacheName,String desc) {

	}
	// 删除操作 结束
	
	/**
	 * 为query 添加查询条件
	 * @param query
	 * @param content
	 */
	public static void addCriteria(Query query,QueryContent content){
		List<Criteria> criterias = convertToCriteria(content);
		if(criterias!=null){
			for (Iterator<Criteria> iterator = criterias.iterator(); iterator.hasNext();) {
				Criteria criteria = (Criteria) iterator.next();
				query.addCriteria(criteria);
			}
		}
	}
	/**
	 * 聚合查询时，可添加额外的查询返回字段，如:求最大值是，出返回最大值外，还可以返回与这个最大值记录对应的其他属性
	 * @param query
	 * @param fieldsToQuery
	 */
	@SuppressWarnings("rawtypes")
	private List<Attribute> addAttribute(Query query, String[] fieldsToQuery) {
		List<Attribute> attributes = null;
		if(fieldsToQuery!=null&&fieldsToQuery.length!=0){
			attributes = new ArrayList<Attribute>();
			for (int i = 0; i < fieldsToQuery.length; i++) {
				String fieldName = fieldsToQuery[i];
				if(fieldName!=null&&!("".equals(fieldName.trim()))){
					Attribute attribute = new Attribute(fieldsToQuery[i]);
					query.includeAttribute(attribute);
					attributes.add(attribute);
				}
			}
		}
		return attributes;
	}
	/**
	 * 为query 添加分组
	 * @param query
	 * @param content
	 */
	public static void addGroupBy(Query query,QueryContent content){
		List<Attribute<?>> gourpBys = convertToGroupBy(content);
		if(gourpBys!=null){
			for (Iterator<Attribute<?>> iterator = gourpBys.iterator(); iterator.hasNext();) {
				Attribute<?> attribute = iterator.next();
				query.addGroupBy(attribute);
			}
		}
	}
	/**
	 * 为query 添加排序
	 * @param query
	 * @param content
	 */
	public static void addOrderBy(Query query,QueryContent content){
		Map<Attribute<?>,Direction> orders = convertToOrderBy(content);
		if(orders!=null){
			for (Iterator<Map.Entry<Attribute<?>,Direction>> iterator = orders.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<Attribute<?>,Direction> entry = iterator.next();
				query.addOrderBy(entry.getKey(),entry.getValue());
			}
		}
	}
	/**
	 * 添加聚合函数查询返回值
	 * @param query
	 * @param queryContent
	 */
	private static void addAggregator(Query query, QueryContent queryContent) {
		List<Aggregator> aggregators = convertToAggregator(queryContent);
		if(aggregators!=null){
			for (Iterator<Aggregator> iterator = aggregators.iterator(); iterator.hasNext();) {
				Aggregator aggregator = iterator.next();
				query.includeAggregator(aggregator);
			}
		}
	}
	/**
	 * 转换成ehcache识别的的聚合函数
	 * @param queryContent
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static List<Aggregator> convertToAggregator(
			QueryContent queryContent) {
		List<Aggregator> aggregators = null;
		if(queryContent!=null){
			ArrayList<QueryItem> aggregatorsItem = queryContent.getAggregators();
			if(!aggregatorsItem.isEmpty()){
				aggregators = new ArrayList<Aggregator>();
				for (Iterator<QueryItem> iterator = aggregatorsItem.iterator(); iterator
						.hasNext();) {
					QueryItem queryItem = (QueryItem) iterator.next();
					QueryType queryType = queryItem.getQueryType();
					String attributeName = queryItem.getFieldName();
					Attribute attribute = new Attribute(attributeName);
					if(queryType == QueryType.MIN){
						aggregators.add(attribute.min());
						continue;
					}else if(queryType == QueryType.MAX){
						aggregators.add(attribute.max());
						continue;
					}if(queryType == QueryType.COUNT){
						aggregators.add(attribute.count());
						continue;
					}if(queryType == QueryType.SUM){
						aggregators.add(attribute.sum());
						continue;
					}if(queryType == QueryType.AVERAGE){
						aggregators.add(attribute.average());
						continue;
					}else{
						//not support type
					}
				}
			}
		}
		return aggregators;
	}

	/**
	 * 转换成ehcache识别的分组
	 * @param queryContent
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static List<Attribute<?>> convertToGroupBy(QueryContent queryContent) {
		List<Attribute<?>> groups = null;
		if(queryContent!=null){
			ArrayList<QueryItem> groupsItem = queryContent.getGroups();
			if(!groupsItem.isEmpty()){
				groups = new ArrayList<Attribute<?>>();
				for (Iterator<QueryItem> iterator = groupsItem.iterator(); iterator
						.hasNext();) {
					QueryItem queryItem = (QueryItem) iterator.next();
					QueryType queryType = queryItem.getQueryType();
					String attributeName = queryItem.getFieldName();
					if(queryType == QueryType.GROUPBY){
						groups.add(new Attribute(attributeName));
						continue;
					}else{
						//not support type
					}
				}
			}
		}
		return groups;
	}
	/**
	 * 转换成ehcache识别的排序
	 * @param queryContent
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static Map<Attribute<?>, Direction> convertToOrderBy(
			QueryContent queryContent) {
		Map<Attribute<?>, Direction> orders = null;
		if(queryContent!=null){
			ArrayList<QueryItem> ordersItem = queryContent.getOrders();
			if(!ordersItem.isEmpty()){
				orders = new HashMap<Attribute<?>, Direction>();
				for (Iterator<QueryItem> iterator = ordersItem.iterator(); iterator
						.hasNext();) {
					QueryItem queryItem = (QueryItem) iterator.next();
					QueryType queryType = queryItem.getQueryType();
					String attributeName = queryItem.getFieldName();
					//Class<?> fieldType = queryItem.getFiledType();
					if(queryType == QueryType.ORDERBY_ASC){
						orders.put(new Attribute(attributeName), Direction.ASCENDING);
						continue;
					}else if(queryType == QueryType.ORDERBY_DESC){
						orders.put(new Attribute(attributeName), Direction.DESCENDING);
						continue;
					}else{
						//not support type
					}
				}
			}
		}
		return orders;
	}

	/**
	 * 翻译成encache识别的条件
	 * @param queryContent
	 * @return
	 */
	public static List<Criteria> convertToCriteria(QueryContent queryContent){
		List<Criteria> criterias = null;
		if(queryContent!=null){
			ArrayList<QueryItem> criteriasItem = queryContent.getCriterias();
			if(!criteriasItem.isEmpty()){
				criterias = new ArrayList<Criteria>();
				for (Iterator<QueryItem> iterator = criteriasItem.iterator(); iterator
						.hasNext();) {
					QueryItem queryItem = (QueryItem) iterator.next();
					QueryType queryType = queryItem.getQueryType();
					String attributeName = queryItem.getFieldName();
					Object value = queryItem.getValue();
					if(queryType == QueryType.EQ){
						criterias.add(new EqualTo(attributeName, value));
						continue;
					}else if(queryType == QueryType.NE){
						criterias.add(new NotEqualTo(attributeName, value));
						continue;
					}else if(queryType == QueryType.LT){
						criterias.add(new LessThan(attributeName, value));
						continue;
					}else if(queryType == QueryType.LE){
						criterias.add(new LessThanOrEqual(attributeName, value));
						continue;
					}else if(queryType == QueryType.GT){
						criterias.add(new GreaterThan(attributeName, value));
						continue;
					}else if(queryType == QueryType.GE){
						criterias.add(new GreaterThanOrEqual(attributeName, value));
						continue;
					}else if(queryType == QueryType.ROUND_LIKE){
						criterias.add(new ILike(attributeName, "*"+value+"*"));
						continue;
					}else if(queryType == QueryType.AFTER_LIKE){
						criterias.add(new ILike(attributeName, "*"+value));
						continue;
					}else if(queryType == QueryType.BEFORE_LIKE){
						criterias.add(new ILike(attributeName, value+"*"));
						continue;
					}else if(queryType == QueryType.NULL){
						criterias.add(new IsNull(attributeName));
						continue;
					}else if(queryType == QueryType.NOT_NULL){
						criterias.add(new NotNull(attributeName));
						continue;
					}else if(queryType == QueryType.IN){
						Collection<?> values = queryItem.getValues();
						criterias.add(new InCollection(attributeName,values));
						continue;
					}else if(queryType == QueryType.BETWEEN){
						boolean includeBegin = queryItem.isIncludeBegin();
						boolean includeEnd = queryItem.isIncludeEnd();
						Object endValue = queryItem.getEndValue();
						criterias.add(new Between(attributeName,value,endValue,includeBegin,includeEnd));
						continue;
					}else{
						//not support type
					}
					
				}
			}
		}
		return criterias;
	}
}
