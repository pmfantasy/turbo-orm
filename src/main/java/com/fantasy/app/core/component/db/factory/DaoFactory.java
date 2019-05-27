package com.fantasy.app.core.component.db.factory;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import com.fantasy.app.core.base.Dao;
import com.fantasy.app.core.component.db.impl.DbcpPoolDao;
import com.fantasy.app.core.component.db.jdbcpool.DataSourceProperty;
import com.fantasy.app.core.util.StrUtil;

/**
 * 创建到的工厂方法
 * @author 公众号：18岁fantasy
 * 2017-5-26 上午10:44:37
 */
public class DaoFactory {

	public static final String DEFAULT_DAOSTR = "DEFAULT";
	private static  ConcurrentHashMap<String, Dao> daoPool = new ConcurrentHashMap<String, Dao>();
	/**
	 * 获取dao
	 * @param dateSourceName
	 * @return
	 */
	public static Dao getDao(String dateSourceName){
		return daoPool.get(dateSourceName.toUpperCase());
	}
	/**
	 * 获取默认的dao
	 * @param dateSourceName
	 * @return
	 */
	public static Dao getDao(){
		return daoPool.get(DEFAULT_DAOSTR);
	}
	/**
	 * 获取dao
	 * @param dateSourceName
	 * @param dataSourceProperty
	 * @return
	 */
	public static synchronized Dao createDao(String dateSourceName,DataSourceProperty dataSourceProperty){
		if(StrUtil.isBlank(dateSourceName)){
			dateSourceName = DEFAULT_DAOSTR;
		}
		dateSourceName = dateSourceName.trim().toUpperCase();
		if(!daoPool.contains(dateSourceName)){
			DbcpPoolDao dbcpPoolDao = new DbcpPoolDao();
			dbcpPoolDao.initDao(dataSourceProperty);
			daoPool.put(dateSourceName, dbcpPoolDao);
		}
		return daoPool.get(dateSourceName);
	}
	/**
	 * 关闭所有dao
	 */
	public static synchronized void closeAll(){
		for (Iterator<Dao> iterator = daoPool.values().iterator(); iterator.hasNext();) {
			Dao dao = (Dao) iterator.next();
			dao.closeDao();
		}
	}
}
