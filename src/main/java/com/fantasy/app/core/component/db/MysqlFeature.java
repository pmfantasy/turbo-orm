package com.fantasy.app.core.component.db;
/**
 * mysql 特性定制类
 * @author 公众号：18岁fantasy
 * @date 2017年9月16日 下午6:08:21
 */
public class MysqlFeature {

	/**
	 * 返回mysql下的分页语句
	 * @param sql
	 * @param offset 
	 * @param pagerSize
	 * @return
	 */
	public  static String pagerWrapSql(String sql,int offset,int pagerSize) {
		return "select * from  ("+sql+") pager " +
				"LIMIT "+offset+","+ pagerSize;
	}
}
