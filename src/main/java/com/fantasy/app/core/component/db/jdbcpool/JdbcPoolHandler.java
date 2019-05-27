package com.fantasy.app.core.component.db.jdbcpool;

/**
 * 连接池处理
 * @author 公众号：18岁fantasy
 * 2017-5-26 上午10:11:23
 */
public interface JdbcPoolHandler {

	/**
	 * 初始化数据源
	 * @param dataSourceProperty
	 */
	public void initDao(DataSourceProperty dataSourceProperty);
	/**
	 * 关闭数据源
	 * @param dataSource
	 */
	public void closeDao();
}
