package com.fantasy.app.core.component.db.plsql;

import oracle.jdbc.internal.OracleTypes;
/**
 * 存储过程操作
 * @日期：2013-6-6下午7:37:40
 * @作者：公众号：18岁fantasy
 */
public class In {

	private String name;
	private InType type;
	private Object value;
	
	public In(String name, InType type, Object value) {
		super();
		this.name = name;
		this.type = type;
		this.value = value;
	}

	public static enum InType{
		
		NUMBER(OracleTypes.NUMBER),
		CHAR(OracleTypes.CHAR),
		DATE(OracleTypes.DATE),
		TIMESTAMP(OracleTypes.TIMESTAMP),
		VARCHAR2(OracleTypes.VARCHAR);
		private int type;
		
		private InType(int type){
			this.type = type;
		}
		public int getType() {
			return type;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public InType getType() {
		return type;
	}

	public void setType(InType type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
}
