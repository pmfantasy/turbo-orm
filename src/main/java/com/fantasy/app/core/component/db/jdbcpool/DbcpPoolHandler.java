package com.fantasy.app.core.component.db.jdbcpool;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import com.fantasy.app.core.base.BaseDao;
import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.component.log.Log;


/**
 * dbcp 连接池
 * 
 * @author 公众号：18岁fantasy 2017-5-26 上午10:01:21
 */
public class DbcpPoolHandler{

	private static Logger logger = Log.getLogger(LogType.DB);
	/**
	 * 硬编码创建数据源
	 * @param driverClassName 驱动名
	 * @param jdbcUrl  连接名
	 * @param username 用户名
	 * @param password 密码
	 * @param initialSize 初始化连接数
	 * @param maxActive 最大连接数
	 * @return
	 */
	public static  DataSource createDataSource(DataSourceProperty  dataSourceProperty) {
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName(dataSourceProperty.getDriverClassName());
		basicDataSource.setUrl(dataSourceProperty.getJdbcUrl());
		basicDataSource.setUsername(dataSourceProperty.getUsername());
		basicDataSource.setPassword(dataSourceProperty.getPassword());
		basicDataSource.setInitialSize(dataSourceProperty.getInitialSize());
		basicDataSource.setMaxIdle(dataSourceProperty.getInitialSize());
		basicDataSource.setMaxActive(dataSourceProperty.getMaxActive());
		basicDataSource.setValidationQuery(BaseDao.getValidateUrlByConnectUrl(dataSourceProperty.getJdbcUrl()));
		return basicDataSource;
	}
	
	/**
	 * 关闭数据库
	 * @param dataSource
	 */
	public static void closeDataSource(DataSource dataSource) {
		if(dataSource!=null){
			BasicDataSource basicDataSource = (BasicDataSource)dataSource;
			if(!basicDataSource.isClosed()){
				try {
					basicDataSource.close();
				} catch (SQLException e) {
					logger.error("关闭连接池失败...",e);
				}
			}
		}
	}
	/**
	 * 创建lob处理类
	 * @param dataSourceProperty
	 * @return
	 */
	public static LobHandler  createLobHandler(DataSourceProperty  dataSourceProperty){
		if(dataSourceProperty!=null){
			DefaultLobHandler defaultLobHandler = new DefaultLobHandler();
			handlerLobArgs(defaultLobHandler);
			return defaultLobHandler;
		}
		return null;
	}
	/**
	 * 设置lob参数
	 * @param defaultLobHandler
	 */
	public static void handlerLobArgs(DefaultLobHandler defaultLobHandler){
		//must be javaSE 6 + for jdbc 4.0
		defaultLobHandler.setCreateTemporaryLob(true);
		defaultLobHandler.setWrapAsLob(true);
		defaultLobHandler.setStreamAsLob(true);
	}
}
