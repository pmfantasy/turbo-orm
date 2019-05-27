package com.fantasy.app.core.component.pager;

import com.fantasy.app.core.para.CorePara.CoreInitCtx;
/**
 * see pg.jsp: id="pg"
 * @日期：2012-12-14下午11:23:57
 * @作者：公众号：18岁fantasy
 */
public class Pg {

	
	private int offset;
	private int pagesize;
	private int currentPage = 1;
	
	public Pg() {
		super();
	}
	public Pg(int offset, int pagesize) {
		super();
		this.offset = offset;
		this.pagesize = pagesize;
	}
	public int getPagesize() {
		
		if(pagesize<=0)pagesize=CoreInitCtx.PAGESIZE;
		
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getOffset() {
		if(offset<=0){
			if(currentPage>=1){
				offset = (currentPage-1)*pagesize;
			}
		}
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	@Override
	public String toString() {
		return "Pager [offset=" + offset + ", pagesize=" + getPagesize() + "]";
	}
	
}
