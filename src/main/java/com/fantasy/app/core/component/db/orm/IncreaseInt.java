package com.fantasy.app.core.component.db.orm;
/**
 * 自增长的int
 * @author 公众号：18岁fantasy
 * 2017-5-3 下午5:27:44
 */
public class IncreaseInt {

	private int value;
	public IncreaseInt(int init){
		value = init;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public void increase(){
		value ++;
	}
	
}
