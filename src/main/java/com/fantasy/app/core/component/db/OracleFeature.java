package com.fantasy.app.core.component.db;
/**
 * oracle 特性定制类
 * @author 公众号：18岁fantasy
 * @date 2017年9月16日 下午6:08:02
 */
public class OracleFeature {

	public static final String ROW_NUM_FLAG = "row_num_";
	/**
	 * 返回oracle下的分页语句
	 * @param sql
	 * @param offset 
	 * @param pagerSize
	 * @return
	 */
	public  static String pagerWrapSql(String sql,int offset,int pagerSize) {
		return "SELECT * FROM (select tinner.*,rownum "+ROW_NUM_FLAG+"  FROM ("+sql+") tinner " +
				"WHERE rownum <"+(offset+pagerSize+1)+") pg WHERE "+ROW_NUM_FLAG+">"+offset;
	}
}
