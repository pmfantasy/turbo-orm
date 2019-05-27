package com.fantasy.app.core.component.db.orm;

import java.util.Arrays;

/**
 * 根据传入modeul值分析出执行dml操作必须的sql语句和参数值
 * @日期：2012-12-14下午11:20:44
 * @作者：公众号：18岁fantasy
 */
public class SqlArg {
	private String sql ;
	private Object[] args;
	
	public SqlArg(){}
	
	public SqlArg(String sql, Object[] args) {
		super();
		this.sql = sql;
		this.args = args;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	public void addArgBefore(Object... value){
		if(value!=null&&value.length!=0){
			Object[] args  = getArgs();
			Object[] newArgs = new Object[args.length+value.length];
			for (int i=0; i < value.length; i++) {
				newArgs[i] = value[i];
			}
			for (int j = 0; j < args.length; j++) {
				newArgs[value.length+j] = args[j];
			}
			setArgs(newArgs);
		}
	}
	/*public void addArgAfter(Object value){
		Object[] args  = getArgs();
		Object[] newArgs = new Object[args.length+1];
		newArgs[newArgs.length] = value;
		for (int i = 0; i < args.length; i++) {
			newArgs[i] = args[i];
		}
		setArgs(newArgs);
	}*/
	@Override
	public String toString() {
		return "SqlArg [sql=" + sql + ", args=" + Arrays.toString(args) + "]";
	}

}
