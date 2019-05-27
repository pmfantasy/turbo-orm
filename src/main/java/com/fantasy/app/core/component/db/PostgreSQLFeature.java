package com.fantasy.app.core.component.db;
/**
 * PostgreSQL特性定制类
 * @author 公众号：18岁fantasy
 * @date 2017年9月16日 下午6:08:33
 */
public class PostgreSQLFeature {

	/**
	 * 返回mysql下的分页语句
	 * @param sql
	 * @param offset 
	 * @param pagerSize
	 * @return
	 */
	public  static String pagerWrapSql(String sql,int offset,int pagerSize) {
		return "select * from  ("+sql+") pager " +
				"LIMIT "+pagerSize+" offset  "+ offset;
	}
}
