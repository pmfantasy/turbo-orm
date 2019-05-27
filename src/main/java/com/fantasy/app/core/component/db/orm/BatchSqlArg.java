package com.fantasy.app.core.component.db.orm;

import java.util.List;

public class BatchSqlArg{

	
	public BatchSqlArg(String sql, List<Object[]> batchArgs) {
		super();
		this.sql = sql;
		this.batchArgs = batchArgs;
	}

	private String sql ;
	private List<Object[]> batchArgs;

	public List<Object[]> getBatchArgs() {
		return batchArgs;
	}

	public void setBatchArgs(List<Object[]> batchArgs) {
		this.batchArgs = batchArgs;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	@Override
	public String toString() {
		return "BatchSqlArg [sql=" + sql + ", batchArgs=" + batchArgs + "]";
	}
	
}
