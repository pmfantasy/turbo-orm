package com.fantasy.app.core.component.db.orm;
/**
 * moudel所对应的数据库表名称
 * @日期：2012-12-14下午11:20:52
 * @作者：公众号：18岁fantasy
 */
public class TableInfo {
	

	

	public TableInfo(String schema,String table,  boolean isSharding) {
		super();
		this.table = table;
		this.schema = schema;
		this.isSharding = isSharding;
	}

	private String table;
	private String schema;
	private boolean isSharding;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public boolean isSharding() {
		return isSharding;
	}

	public void setSharding(boolean isSharding) {
		this.isSharding = isSharding;
	}

	
	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	@Override
	public String toString() {
		return "TableInfo [table=" + table + "]";
	}
	
	
}
