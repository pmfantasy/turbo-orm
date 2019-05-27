package com.fantasy.app.core.component.db.tablesharding;

/**
 * 表拆分对象。表的后缀需要充0开始。如拆分为2张表，对应为：原表_0,原表_1
 * @author 公众号：18岁fantasy
 * @2015-4-20 @下午4:39:22
 */
public class TableShardingVo {

	public String tableName;//未拆分表前缀
	public int shardingNum;//拆分个数  
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getShardingNum() {
		return shardingNum;
	}
	public void setShardingNum(int shardingNum) {
		this.shardingNum = shardingNum;
	}
	
	
}
