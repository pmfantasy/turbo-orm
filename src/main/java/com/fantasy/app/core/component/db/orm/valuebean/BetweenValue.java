package com.fantasy.app.core.component.db.orm.valuebean;
/**
 * 区间值 
 * @日期：2013-6-6下午7:38:15
 * @作者：公众号：18岁fantasy
 */
public class BetweenValue{
	private Object  begin;
	private boolean includeBegin = true;//默认包含起始值
	private Object  end;
	private boolean includeEnd = true;//默认包含结束值
	public BetweenValue(Object begin, Object end) {
		super();
		this.begin = begin;
		this.end = end;
	}
	/**
	 * 
	 * @param begin 开始值
	 * @param end 结束值
	 * @param includeBegin 包含起始值
	 * @param includeEnd 包含结束值
	 */
	public BetweenValue(Object begin, Object end,boolean includeBegin,boolean includeEnd) {
		super();
		this.begin = begin;
		this.end = end;
		this.includeBegin = includeBegin;
		this.includeEnd = includeEnd;
	}
	public Object getBegin() {
		return begin;
	}
	public void setBegin(Object begin) {
		this.begin = begin;
	}
	public Object getEnd() {
		return end;
	}
	public void setEnd(Object end) {
		this.end = end;
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
	
} 
