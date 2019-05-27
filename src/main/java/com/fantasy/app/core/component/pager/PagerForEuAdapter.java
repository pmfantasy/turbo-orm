package com.fantasy.app.core.component.pager;
/**
 * 为了适配easyui所做的适配
 * @author 公众号：18岁fantasy
 * 2017年7月18日 下午4:09:44
 */
public class PagerForEuAdapter extends Pager{
  /**
   * rows 代表的easyui的当前页码。
   */
  private int page;
  /**
   * rows 代表的easyui的pagesize。
   */
  private int rows;
  
  public PagerForEuAdapter(){
    
  }
  public PagerForEuAdapter(int page,int pagesize){
    //顺序不能乱，要不后面计算有问题
    setRows(pagesize);
    setPage(page);
  }
  public int getPage() {
    return page;
  }
  public void setPage(int page) {
    //把当前页换算成数据库的offset
    int offset = (page-1)*rows;
    if(offset<0)offset = 0;
    getPg().setOffset((page-1)*rows);
    this.page = page;
  }
  public int getRows() {
    return rows;
  }
  public void setRows(int rows) {
    getPg().setPagesize(rows);
    this.rows = rows;
  }
  
}
