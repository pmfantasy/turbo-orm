package com.fantasy.app.core.component.pager;

import java.util.LinkedHashMap;
/**
 * 内存分页
 * @author 公众号：18岁fantasy
 * 2017-5-25 下午3:40:23
 */
@SuppressWarnings("rawtypes")
public class MemoryPagerData  {
	
	
	private LinkedHashMap datas;
	private int total;
	
	
	public LinkedHashMap getDatas() {
		return datas;
	}
	public void setDatas(LinkedHashMap datas) {
		this.datas = datas;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}

}
