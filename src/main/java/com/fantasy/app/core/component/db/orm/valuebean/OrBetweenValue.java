package com.fantasy.app.core.component.db.orm.valuebean;
/**
 * 区间值 
 * @日期：2013-6-6下午7:38:15
 * @作者：公众号：18岁fantasy
 */
public class OrBetweenValue extends BetweenValue implements OrValue{

	public OrBetweenValue(Object begin, Object end) {
		super(begin, end);
	}
	public OrBetweenValue(Object begin, Object end,boolean includeBegin,boolean includeEnd) {
		super(begin, end,includeBegin,includeEnd);
	}
} 
