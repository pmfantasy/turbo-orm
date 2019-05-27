package com.fantasy.app.core.component.pager;


/**
 * 分页处理封装
 * @日期：2012-12-14下午11:23:42
 * @作者：公众号：18岁fantasy
 */
public class Pager {

    
	public Pager(){
		
	}
	public Pager(int offset,int pagesize){
		this.pg.setOffset(offset);
		this.pg.setPagesize(pagesize);
	}
	private Pg pg = new Pg();

	public Pg getPg() {
		return pg;
	}

	public void setPg(Pg pg) {
		this.pg = pg;
	}

	@Override
	public String toString() {
		return "Pager [pg=" + pg + "]";
	}
}
