package com.fantasy.app.core.util.excelutil;


/**
 * 
* @Description: excel导出工具类
* @author 公众号：18岁fantasy
* @date 2017年8月21日 下午2:53:15
 */
public class ExcelColumn {
	private Object value;  //列的值    
	private int mergedRight = 0;//向右合并多少单元格   负数表示向左合并
	private int mergedDown = 0;//向下合并多少单元格   负数表示向上合并
	
	
	
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public int getMergedRight() {
		return mergedRight;
	}
	public void setMergedRight(int mergedRight) {
		this.mergedRight = mergedRight;
	}
	public int getMergedDown() {
		return mergedDown;
	}
	public void setMergedDown(int mergedDown) {
		this.mergedDown = mergedDown;
	}
	
	
	
	
}
