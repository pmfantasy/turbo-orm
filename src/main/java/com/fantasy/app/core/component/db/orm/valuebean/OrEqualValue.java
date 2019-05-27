package com.fantasy.app.core.component.db.orm.valuebean;
/**
 * OrEqual
 * @author 公众号：18岁fantasy
 * 2017-5-3 下午5:25:05
 */
public class OrEqualValue implements OrValue{
	private Object value;

	public OrEqualValue(Object value){
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
}
