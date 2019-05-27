package com.fantasy.app.core.component.db;
/**
 * 数据库类型
 * @author 公众号：18岁fantasy
 * 2017-5-26 上午11:10:18
 */
public enum DbTypeEnum {
	
	ORACLE("1","oracle","oracle.jdbc.driver.OracleDriver","select 1 from dual"),
	MYSQL("2","mySql","org.gjt.mm.mysql.Driver","select 1 "),
	SQLSERVER("3","sql server","com.microsoft.sqlserver.jdbc.SQLServerDriver","select 1 "),
    POSTGRESQL("4","postgresql","org.postgresql.Driver","select 1 ");
	private String code;
	private String name;
	private String driverClassName;
	private String validataSql;
	
	private DbTypeEnum(String code,String nameToDisPlay,String driverClassName,String validataSql){
		this.code = code;
		this.name = nameToDisPlay;
		this.driverClassName = driverClassName;
		this.validataSql = validataSql;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getValidataSql() {
		return validataSql;
	}

	public void setValidataSql(String validataSql) {
		this.validataSql = validataSql;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
