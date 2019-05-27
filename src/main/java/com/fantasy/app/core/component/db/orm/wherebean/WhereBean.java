package com.fantasy.app.core.component.db.orm.wherebean;
/**
 * 条件对象
 * @author 公众号：18岁fantasy
 * 2017-5-3 下午5:25:54
 */
public class WhereBean {

	private ConditionType conditionType;
	private String column;
	private Object value;
	
	
	public WhereBean(ConditionType conditionType, String clumnName, Object value) {
		super();
		this.conditionType = conditionType;
		this.column = clumnName;
		this.value = value;
	}
	

	/**
	 * =, !=, <, >, <=, >=,comparison
	IS [NOT] NULL, LIKE, [NOT] BETWEEN, [NOT] IN, EXISTS, IS OF type comparison
	NOT exponentiation, logical negation
	AND conjunction
	OR disjunction
	( left bracket
	)right  bracket
	 * @author 公众号：18岁fantasy
	 * @2014-4-17 @下午1:48:00
	 */
	public static enum ConditionType{
		EQ,
		NOT_EQ,
		GREATER_THAN,
		LESS_THAN,
		GREATER_THAN_OR_EQUAL,
		LESS_THAN_OR_EQUAL,
		LIKE,
		NOT_LIKE,
		BETWEEN,
		NOT_BETWEEN,
		IN,
		NOT_IN;
	}
	public ConditionType getConditionType() {
		return conditionType;
	}
	public void setConditionType(ConditionType conditionType) {
		this.conditionType = conditionType;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}
