package com.fantasy.app.core.base.cache;

import java.util.Collection;

/**
 * 查询条件
 * @author 公众号：18岁fantasy
 * 2017-4-18 下午2:44:02
 */
public class QueryItem {

	private QueryType queryType;
	private String fieldName;
	private Class<?> filedType;
	private Object value;
	private Object endValue;
	private Collection<?> values;
	private boolean includeBegin ;
	private boolean includeEnd ;
	
	public QueryItem(QueryType queryType,String filedName){
		this.queryType = queryType;
		this.fieldName = filedName;
	}
	public QueryItem(QueryType queryType,String filedName,Class<?> filedType){
		this.queryType = queryType;
		this.fieldName = filedName;
		this.filedType = filedType;
	}
	public QueryItem(QueryType queryType,String filedName,Object value){
		this(queryType, filedName);
		this.value = value;
	}
	public QueryItem(QueryType queryType,String filedName,Collection<?> values){
		this(queryType, filedName);
		this.values = values;
	}
	public QueryItem(QueryType queryType,String filedName,Object value,Object endValue){
		this(queryType, filedName,value);
		this.endValue = endValue;
	}
	public QueryItem(QueryType queryType,String filedName,Object value,Object endValue,boolean includeBegin,boolean includeEnd){
		this(queryType, filedName,value,endValue);
		this.includeBegin = includeBegin;
		this.includeEnd = includeEnd;
	}
	public QueryType getQueryType() {
		return queryType;
	}
	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Object getEndValue() {
		return endValue;
	}
	public void setEndValue(Object endValue) {
		this.endValue = endValue;
	}

	
	public Collection<?> getValues() {
		return values;
	}
	public void setValues(Collection<?> values) {
		this.values = values;
	}


	public boolean isIncludeBegin() {
		return includeBegin;
	}
	public void setIncludeBegin(boolean includeBegin) {
		this.includeBegin = includeBegin;
	}
	public boolean isIncludeEnd() {
		return includeEnd;
	}
	public void setIncludeEnd(boolean includeEnd) {
		this.includeEnd = includeEnd;
	}


	public Class<?> getFiledType() {
		return filedType;
	}
	public void setFiledType(Class<?> filedType) {
		this.filedType = filedType;
	}


	/**
	 * 条件类型
	 * @author 公众号：18岁fantasy
	 * 2017-4-18 下午2:44:13
	 */
	public enum  QueryType{
		/**
		 * 匹配给定的表达式  向后匹配 such as ：%abc
		 */
		AFTER_LIKE,
		/**
		 * 匹配给定的表达式  向前匹配 such as ：abc%
		 */
		BEFORE_LIKE,
		/**
		 * 匹配给定的表达式  区间匹配 such as ：%abc%
		 */
		ROUND_LIKE,
		/**
		 * 等于给定的值
		 */
		EQ,
		/**
		 * 不等于给定的值
		 */
		NE,
		/**
		 * 属性值在给定的范围之间
		 */
		BETWEEN,
		/**
		 * 在给定的集合之中
		 */
		IN,
		/**
		 * 小于给定的值
		 */
		LT,
		/**
		 * 小于或等于给定的值
		 */
		LE,
		/**
		 * 大于给定的值
		 */
		GT,
		/**
		 * 大于或等于给定的值
		 */
		GE,
		/**
		 * 等于 null
		 */
		NULL,
		/**
		 * 等于 null
		 */
		NOT_NULL,
		
		/**
		 * Aggregator 聚合函数
		 */
		MIN, MAX,SUM,COUNT,AVERAGE,
		/**
		 * 分组
		 */
		GROUPBY,
		/**
		 * 排序
		 */
		ORDERBY_DESC,
		ORDERBY_ASC;
	}
}
