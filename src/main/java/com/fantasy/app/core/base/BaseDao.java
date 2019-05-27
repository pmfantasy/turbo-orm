package com.fantasy.app.core.base;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fantasy.app.core.component.db.DbTypeEnum;
import com.fantasy.app.core.component.db.tablesharding.TableShardingParser;
import com.fantasy.app.core.para.CorePara.CoreInitCtx;
import com.fantasy.app.core.util.DbUtil;


/**
 * 数据库操作基类，如果有自定义的数据库操作类，请继承此方法
 * @日期：2012-12-14下午11:03:56
 * @作者：公众号：18岁fantasy
 */
@Component
@Lazy
public class BaseDao {

	
	public Dao getDao() {
		return CmCommon.getDao();
	}
	/**
	 * 
	 * @return 
	 */
	public static String WSD_TABLE(String tableName){
		return CoreInitCtx.DEFAULT_SCHEMA_NAME+"."+tableName;
	}
	/**
	 * 
	 * @return 
	 */
	public static String WSD_TABLE(String scheamName,String tableName){
		if(!StringUtils.hasText(scheamName)){
			return WSD_TABLE(tableName);
		}
		return scheamName+"."+tableName;
	}
	/**
	 * DML(insert update delete)表拆分的支持 目前只支持表后缀为_数字的，拆分
	 * @param rawTableName
	 * @param id
	 * @return
	 */
	public static String  SHARDING_TABLE_DML(String rawTableName,String id){
		if(TableShardingParser.tableIsSharding(rawTableName)){
			return TableShardingParser.SHARDING_TABLE_DML(rawTableName, id);
		}else{
			throw new RuntimeException("未对当前表"+rawTableName+"配置水平拆分，请检查先关配置文件");
		}
		
	}
	/**
	 * 查询，当没有给定id时，为全表查询
	 * @param rawTableName
	 * @param id
	 * @return
	 */
	public static String SHARDING_TABLE_SEL(String rawTableName,String id){
		if(TableShardingParser.tableIsSharding(rawTableName)){
			return TableShardingParser.SHARDING_TABLE_SEL(rawTableName, id);
		}else{
			throw new RuntimeException("未对当前表"+rawTableName+"配置水平拆分，请检查相关配置文件");
		}
	}
	
	public static DbTypeEnum getDbType(String  connectUrl) {
		if(DbUtil.isOracleFromConnectUrl(connectUrl)){
			return DbTypeEnum.ORACLE;
		}else if(DbUtil.isMysqlFromConnectUrl(connectUrl)){
			return DbTypeEnum.MYSQL;
		}else if(DbUtil.isSqlServerFromConnectUrl(connectUrl)){
			return DbTypeEnum.SQLSERVER;
		}else if(DbUtil.isPostgresqlFromConnectUrl(connectUrl)){
          return DbTypeEnum.POSTGRESQL;
      }else{
			throw new RuntimeException("不支持的数据库类型...");
		}
	}
	public static String getValidateUrlByConnectUrl(String connectUrl) {
		return getDbType(connectUrl).getValidataSql();
	}
}
