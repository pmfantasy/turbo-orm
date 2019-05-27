package com.fantasy.app.core.component.db.jdbcpool;
/**
 * 数据源配置参数对象
 * @author 公众号：18岁fantasy
 * 2017-5-26 上午10:14:23
 */
public class DataSourceProperty {

	private String driverClassName;
	private String jdbcUrl;
	private String username; 
	private String password;
	private int initialSize; 
	private int maxActive;
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getInitialSize() {
		return initialSize;
	}
	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}
	public int getMaxActive() {
		return maxActive;
	}
	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}
	@Override
	public String toString() {
		return "DataSourceProperty [driverClassName=" + driverClassName
				+ ", jdbcUrl=" + jdbcUrl + ", username=" + username
				+ ", password=******,initialSize=" + initialSize
				+ ", maxActive=" + maxActive + "]";
	}
	
	
}
