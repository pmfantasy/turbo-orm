package com.fantasy.app.core.component.db.plsql;

import oracle.jdbc.internal.OracleTypes;

import org.springframework.jdbc.core.RowMapper;
/**
 * 存储过程操作
 * @日期：2013-6-6下午7:37:40
 * @作者：公众号：18岁fantasy
 */
public class Out {
	private String name;
	private OutType type;
	private String plsTypeName;
	private Class<?> caseType;
	private  RowMapper<?> rowMapper;
	
	public static Out varchar2Out(String paraName){
		Out out = new Out();
		out.setName(paraName);
		out.setType(OutType.VARCHAR2);
		return out;
	}
	public static Out charOut(String paraName){
		Out out = new Out();
		out.setName(paraName);
		out.setType(OutType.CHAR);
		return out;
	}
	public static Out numberForIntegerOut(String paraName){
		Out out = numberOut(paraName);
		out.setCaseType(Integer.class);
		return out;
	}
	public static Out numberForLongOut(String paraName){
		Out out = numberOut(paraName);
		out.setCaseType(Long.class);
		return out;
	}
	public static Out numberForDoubleOut(String paraName){
		Out out = numberOut(paraName);
		out.setCaseType(Double.class);
		return out;
	}
	public static Out dateOut(String paraName){
		Out out = new Out();
		out.setName(paraName);
		out.setType(OutType.DATE);
		return out;
	}
	public static Out timestampOut(String paraName){
		Out out = new Out();
		out.setName(paraName);
		out.setType(OutType.TIMESTAMP);
		return out;
	}
	public static Out refCursorOut(String paraName,RowMapper<?> rowMapper){
		Out out = new Out();
		out.setName(paraName);
		out.setType(OutType.REF_CURSOR);
		out.setRowMapper(rowMapper);
		return out;
	}
	private static Out numberOut(String paraName){
		Out out = new Out();
		out.setName(paraName);
		out.setType(OutType.NUMBER);
		return out;
	}
	private Out(){}
	
	public static enum OutType {
		
		NUMBER(OracleTypes.NUMBER),
		CHAR(OracleTypes.CHAR),
		DATE(OracleTypes.DATE),
		TIMESTAMP(OracleTypes.TIMESTAMP),
		VARCHAR2(OracleTypes.VARCHAR),
		
		ARRAY(OracleTypes.ARRAY),
		INDEX_TABLE(OracleTypes.PLSQL_INDEX_TABLE),
		TABLE_RECORE(OracleTypes.STRUCT),
		TABLE(OracleTypes.CURSOR),
		REF_CURSOR(OracleTypes.CURSOR);
		private int type;
		
		private OutType(int type){
			this.type = type;
		}
		public int getType() {
			return type;
		}
		
	}
	//public class 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OutType getType() {
		return type;
	}

	public void setType(OutType type) {
		this.type = type;
	}
	public RowMapper<?> getRowMapper() {
		return rowMapper;
	}
	public void setRowMapper(RowMapper<?> rowMapper) {
		this.rowMapper = rowMapper;
	}
	
	public String getPlsTypeName() {
		return plsTypeName;
	}
	public void setPlsTypeName(String plsTypeName) {
		this.plsTypeName = plsTypeName;
	}
	public void setCaseType(Class<?> caseType) {
		this.caseType = caseType;
	}
	public Class<?> getCaseType() {
		return caseType;
	}

}
