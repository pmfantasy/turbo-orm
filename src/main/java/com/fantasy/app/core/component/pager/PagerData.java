package com.fantasy.app.core.component.pager;

import java.util.List;

/**
 * 返回分页数据，包含数据列表和当前条件下的总数
 * 
 * @日期：2012-12-14下午11:23:51
 * @作者：公众号：18岁fantasy
 */
public class PagerData<T> {
  /**
   * 查询结果集
   */
  private List<T> rows;

  private Pg pg;

  /**
   * 查询结果总数
   */
  private int total;



  public List<T> getRows() {
    return rows;
  }

  public void setRows(List<T> rows) {
    this.rows = rows;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public Pg getPg() {
    return pg;
  }

  public void setPg(Pg pg) {
    this.pg = pg;
  }

  @Override
  public String toString() {
    return "PagerData [datas=" + rows + ", total=" + total + "]";
  }
}
