package com.fantasy.app.core.base.cache;

import java.util.ArrayList;

import com.fantasy.app.core.base.cache.QueryItem.QueryType;
import com.fantasy.app.core.util.StrUtil;

/**
 * 缓存查询条件容器
 * @author 公众号：18岁fantasy
 * 2017-5-25 下午3:20:39
 */
public class QueryContent {

	private ArrayList<QueryItem> criterias = new ArrayList<QueryItem>();
	
	private ArrayList<QueryItem> aggregators = new ArrayList<QueryItem>();
	
	private ArrayList<QueryItem> orders = new ArrayList<QueryItem>();
	
	private ArrayList<QueryItem> groups = new ArrayList<QueryItem>();
	
	public static QueryContent newInstance(){
		return new QueryContent();
	}
	/**
	 * 是否为空
	 * @param strToCheck
	 * @return
	 */
	private boolean isNotBlank(String strToCheck){
		return StrUtil.isNotBlank(strToCheck);
	}
	/**
	 * add "is null"   
	 * @return
	 */
	public QueryContent isNull(String clumnName){
		if(isNotBlank(clumnName)){
			criterias.add(new QueryItem(QueryType.NULL, clumnName));
		}
		return this;
	}
	/**
	 * add "is not null"   
	 * @return
	 */
	public QueryContent isNotNull(String clumnName){
		if(isNotBlank(clumnName)){
			criterias.add(new QueryItem(QueryType.NOT_NULL, clumnName));
		}
		return this;
	}
	/**
	 * add "="   
	 * @return
	 */
	public QueryContent eq(String clumnName,Object value){
		if(isNotBlank(clumnName)&&value!=null){
			criterias.add(new QueryItem(QueryType.EQ, clumnName,value));
		}
		return this;
	}
	/**
	 * add "<>"   
	 * @return
	 */
	public QueryContent notEq(String clumnName,Object value){
		if(isNotBlank(clumnName)&&value!=null){
			criterias.add(new QueryItem(QueryType.NE, clumnName,value));
		}
		return this;
	}
	/**
	 * add ">"   
	 * @return
	 */
	public QueryContent greaterThan(String clumnName,Object value){
		if(isNotBlank(clumnName)&&value!=null){
			criterias.add(new QueryItem(QueryType.GT, clumnName,value));
		}
		return this;
	}
	/**
	 * add ">="   
	 * @return
	 */
	public QueryContent greaterThanOrEq(String clumnName,Object value){
		if(isNotBlank(clumnName)&&value!=null){
			criterias.add(new QueryItem(QueryType.GE, clumnName, value));
		}
		return this;
	}
	/**
	 * add "<"   
	 * @return
	 */
	public QueryContent lessThan(String clumnName,Object value){
		if(isNotBlank(clumnName)&&value!=null){
			criterias.add(new QueryItem(QueryType.LT, clumnName, value));
		}
		return this;
	}
	/**
	 * add "<="   
	 * @return
	 */
	public QueryContent lessThanOrEq(String clumnName,Object value){
		if(isNotBlank(clumnName)&&value!=null){
			criterias.add(new QueryItem(QueryType.LE, clumnName, value));
		}
		return this;
	}
	/**
	 * add " like"   
	 * @return
	 */
	public QueryContent afterLike(String clumnName,String value){
		if(isNotBlank(clumnName)&&isNotBlank(value)){
			criterias.add(new QueryItem(QueryType.AFTER_LIKE, clumnName, value));
		}
		return this;
	}
	/**
	 * add " like"   
	 * @return
	 */
	public QueryContent beforeLike(String clumnName,String value){
		if(isNotBlank(clumnName)&&isNotBlank(value)){
			criterias.add(new QueryItem(QueryType.BEFORE_LIKE, clumnName, value));
		}
		return this;
	}
	/**
	 * add " like"   
	 * @return
	 */
	public QueryContent roundLike(String clumnName,String value){
		if(isNotBlank(clumnName)&&isNotBlank(value)){
			criterias.add(new QueryItem(QueryType.ROUND_LIKE, clumnName, value));
		}
		return this;
	}
	/**
	 * add " between"   
	 * @return
	 */
	public QueryContent between(String clumnName,Object value,Object endValue){
		if(isNotBlank(clumnName)&&value!=null){
			criterias.add(new QueryItem(QueryType.BETWEEN, clumnName, value,endValue));
		}
		return this;
	}
	/**
	 * add "in"   
	 * @return
	 */
	public QueryContent in(String clumnName,Object value){
		if(isNotBlank(clumnName)&&value!=null){
			criterias.add(new QueryItem(QueryType.IN, clumnName, value));
		}
		return this;
	}
	/**
	 * 求最小值
	 * @param clumnName 
	 * @return
	 */
	public QueryContent min(String clumnName){
		if(isNotBlank(clumnName)){
			aggregators.add(new QueryItem(QueryType.MIN, clumnName));
		}
		return this;
	}
	/**
	 * 求最大值
	 * @param clumnName 
	 * @return
	 */
	public QueryContent max(String clumnName){
		if(isNotBlank(clumnName)){
			aggregators.add(new QueryItem(QueryType.MAX, clumnName));
		}
		return this;
	}
	/**
	 * 求总数
	 * @param clumnName 
	 * @return
	 */
	public QueryContent count(String clumnName){
		if(isNotBlank(clumnName)){
			aggregators.add(new QueryItem(QueryType.COUNT, clumnName));
		}
		return this;
	}
	/**
	 * 求和,注意:只能对数值型元素求和
	 * @param clumnName 
	 * @return
	 */
	public QueryContent sum(String clumnName){
		if(isNotBlank(clumnName)){
			aggregators.add(new QueryItem(QueryType.SUM, clumnName));
		}
		return this;
	}
	/**
	 * 求平均 注意:只能对数值型元素求平均
	 * @param clumnName 
	 * @return
	 */
	public QueryContent average(String clumnName){
		if(isNotBlank(clumnName)){
			aggregators.add(new QueryItem(QueryType.AVERAGE, clumnName));
		}
		return this;
	}
	/**
	 * add "groupby"
	 * @param clumnNames  such as "a,b,c"
	 * @return
	 */
	public QueryContent groupBy(String clumnName){
		if(isNotBlank(clumnName)){
			groups.add(new QueryItem(QueryType.GROUPBY, clumnName));
		}
		return this;
	}
	/**
	 * 升序排序
	 * @param clumnName 
	 * @return
	 */
	public QueryContent orderByDesc(String clumnName){
		if(isNotBlank(clumnName)){
			orders.add(new QueryItem(QueryType.ORDERBY_DESC, clumnName));
		}
		return this;
	}
	/**
	 * 降序排序
	 * @param clumnName 
	 * @return
	 */
	public QueryContent orderByAsc(String clumnName){
		if(isNotBlank(clumnName)){
			orders.add(new QueryItem(QueryType.ORDERBY_ASC, clumnName));
		}
		return this;
	}
	
	//---------gets
	public ArrayList<QueryItem> getCriterias() {
		return criterias;
	}
	public ArrayList<QueryItem> getAggregators() {
		return aggregators;
	}
	public ArrayList<QueryItem> getOrders() {
		return orders;
	}
	public ArrayList<QueryItem> getGroups() {
		return groups;
	}
	
}
