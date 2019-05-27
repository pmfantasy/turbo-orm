package com.fantasy.app.core.listener.inner;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.fantasy.app.core.base.BaseDao;
import com.fantasy.app.core.base.InterceptorBean.SIGNAL;
import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.boot.BootParam;
import com.fantasy.app.core.component.db.DbTypeEnum;
import com.fantasy.app.core.component.db.factory.DaoFactory;
import com.fantasy.app.core.component.db.jdbcpool.DataSourceProperty;
import com.fantasy.app.core.component.db.orm.ModuleParser;
import com.fantasy.app.core.component.db.tablesharding.TableShardingParser;
import com.fantasy.app.core.component.log.Log;
import com.fantasy.app.core.exception.InitException;
import com.fantasy.app.core.listener.AppInitListener;
import com.fantasy.app.core.para.ParaGetTool;
import com.fantasy.app.core.para.CorePara.CoreInitCtx;
import com.fantasy.app.core.util.StrUtil;


/**
 * dao linstener
 * @author 公众号：18岁fantasy
 * 2017-4-19 下午4:52:06
 */
public class DaoInitListener implements AppInitListener {
	static Logger logger = Log.getLogger(LogType.DB);
	@Override
	public void init(ServletContextEvent contextEvent, BootParam bootParam) throws InitException {
        try {
        	ServletContext servletContext = contextEvent==null?null:contextEvent.getServletContext();
        	//初始化orm
        	parseModules(servletContext,bootParam);
        	//初始化数据库
        	//从配置文件中读取多个数据源的参数并设置,目前只实现单个数据源的情况
        	initDao(servletContext);
        	//设置默认的schema
        	setDefaultSchema();
        	//解析拆分表信息。必须在解析实体之前
			//parseTableSharding(servletContext);
			if(contextEvent!=null){
				contextEvent.getServletContext().log("初始化数据源监听[DaoInitListener]成功.");
			}
		} catch (Exception e) {
			if(contextEvent!=null){
				contextEvent.getServletContext().log("初始化数据源监听[DaoInitListener]失败.",e);
			}
			logger.error("初始化数据源监听[DaoInitListener]失败",e);
			throw new InitException(e);
		}
	}

	private void setDefaultSchema() {
		String defalutSchemaName = ParaGetTool.getPara("db.default.schema");
		if (!StringUtils.hasText(defalutSchemaName)) {
			throw new RuntimeException("请设置默认表空间名称!!!");
		}
		CoreInitCtx.DEFAULT_SCHEMA_NAME = defalutSchemaName;
	}
	/**
     * 初始化所有的数据源
     * 配置参数格式必须是db.xxx.url db.xxx.username 
     * @param servletContext
     */
	private void initDao(ServletContext servletContext) {
	  //初始化所有的dao
	  Set<String> keys =  ParaGetTool.getAllKey("^db\\..+\\.url$");
	  if(keys!=null) {
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
           String key = (String) iterator.next();
           String dbName = key.split("\\.")[1];
           DataSourceProperty dataSourceProperty = getDataSourceProperty(dbName);
           dbName = dbName.equalsIgnoreCase(DaoFactory.DEFAULT_DAOSTR)?DaoFactory.DEFAULT_DAOSTR:dbName.toUpperCase();
           dbName = dbName.toUpperCase();
           DaoFactory.createDao(dbName, dataSourceProperty);
           if(servletContext!=null) {
             servletContext.log("初始化  dao-->"+dbName+" success...");
           }else {
             logger.info("初始化 dao-->"+dbName+" success...");
           }
        }
	  }else {
	    if(servletContext!=null) {
          servletContext.log("未发现数据源配置,请确保参数格式为db.xxx.开头");
        }else {
          logger.warn("未发现数据源配置,请确保参数格式为db.xxx.开头");
        }
	  }
	}
    /**
     * 解析module
     * @param servletContext
     * @param bootParam
     */
	private void parseModules(ServletContext servletContext,BootParam bootParam) {
		List<String> pkgList = bootParam.getModulePackage();
		String modulePackage = "";
		for (String pkg : pkgList) {
			modulePackage += pkg + "|";
		}
		modulePackage = StrUtil.substringBeforeLast(modulePackage, "|");
		ModuleParser.MODULE_PACKAGE = modulePackage;
		ModuleParser.getInstance().parase(servletContext);
	}

	/**
	 * 获取数据源
	 * @return
	 */
	public DataSourceProperty getDataSourceProperty(String dbName){
	    dbName = dbName.trim();
		DataSourceProperty dataSourceProperty = new DataSourceProperty();
		dataSourceProperty.setJdbcUrl(ParaGetTool.getPara("db."+dbName+".url"));
		dataSourceProperty.setDriverClassName(getDriverClassNameFromUrl(dataSourceProperty.getJdbcUrl()));
		dataSourceProperty.setUsername(ParaGetTool.getPara("db."+dbName+".username"));
		dataSourceProperty.setPassword(ParaGetTool.getPara("db."+dbName+".password"));
		dataSourceProperty.setInitialSize(ParaGetTool.getPara(Integer.class,"db."+dbName+".initPoolSize",5));
		dataSourceProperty.setMaxActive(ParaGetTool.getPara(Integer.class,"db."+dbName+".maxPoolSize",5));
		return dataSourceProperty;
	}
	
    public static String getDriverClassNameFromUrl(String jdbcUrl){
    	DbTypeEnum dbTypeEnum = BaseDao.getDbType(jdbcUrl);
		return dbTypeEnum.getDriverClassName();
    }

	@Override
	public void destroyed(ServletContextEvent contextEvent, BootParam bootParam) throws InitException {
		DaoFactory.closeAll();
	}
	@SuppressWarnings("unused")
    private void parseTableSharding(ServletContext servletContext) {
		TableShardingParser.getInstance().parase(servletContext);
	}

	@Override
	public SIGNAL onError(ServletContextEvent contextEvent, BootParam bootParam)
			throws InitException {
		return SIGNAL.STOP;
	}
}
