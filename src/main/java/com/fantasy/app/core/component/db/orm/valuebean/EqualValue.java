package com.fantasy.app.core.component.db.orm.valuebean;

/**
 * 相等值 
 * @author 公众号：18岁fantasy
 * 2017-5-3 下午5:23:33
 */
public class EqualValue {

	private Object value;
	public EqualValue(Object value){
		this.value = value;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
} 
