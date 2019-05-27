package com.fantasy.app.core.component.db.impl;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.fantasy.app.core.base.BaseDao;
import com.fantasy.app.core.base.Dao;
import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.component.db.DbTypeEnum;
import com.fantasy.app.core.component.db.jdbcpool.DataSourceProperty;
import com.fantasy.app.core.component.db.jdbcpool.DbcpPoolHandler;
import com.fantasy.app.core.component.db.orm.DefaultDmlCreateor;
import com.fantasy.app.core.component.log.Log;


/**
 * dbcp 连接池类型的数据操作类   非线程安全的
 * @日期：2012-12-14下午11:21:26
 * @作者：公众号：18岁fantasy
 *  缺少部分功能：存储过程调用等，按后期需求添加
 */
public  class DbcpPoolDao extends Dao{
	
	private Logger logger = Log.getLogger(LogType.DB);
	
	public void initDao(DataSourceProperty dataSourceProperty){
		DataSource dataSource = DbcpPoolHandler.createDataSource(dataSourceProperty);
		setJdbcTemplate(new JdbcTemplate(dataSource));
		setLobHandler(DbcpPoolHandler.createLobHandler(dataSourceProperty));
		setTransactionManager(new DataSourceTransactionManager(dataSource));
		setModuleDmlCreator(new DefaultDmlCreateor());
		
		logger.info("init :" + getJdbcTemplate() + "//" + getLobHandler() + "//" + getTransactionManager() + "//"
				+ getModuleDmlCreator());
	}
    
	@Override
	public void closeDao() {
		DataSource dataSource = getJdbcTemplate().getDataSource();
		DbcpPoolHandler.closeDataSource(dataSource);
	}

	@Override
	public DbTypeEnum getDbType() {
		DataSource dataSource = getJdbcTemplate().getDataSource();
		BasicDataSource basicDataSource = (BasicDataSource)dataSource; 
		String connectUrl = basicDataSource.getUrl();
		return BaseDao.getDbType(connectUrl);
	}
}
