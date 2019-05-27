package com.fantasy.app.core.component.db.orm;

import java.util.List;

import com.fantasy.app.core.base.ModuleFeatureBean;

/**
 * 用来对module类进行dml操作
 * @日期：2012-12-14下午11:18:33
 * @作者：公众号：18岁fantasy
 */
public interface ModuleDmlCreator {

	public SqlArg createInsert(ModuleFeatureBean modulefeatureBean,String[] executeFields) throws Exception ;

	public SqlArg createDelete(Class<? extends ModuleFeatureBean> classToQuery,WhereCondition deleteParameter)throws Exception;
	
	public SqlArg createShardingDelete(ModuleFeatureBean moduleFeatureBean,WhereCondition deleteParameter)throws Exception;
	
	public SqlArg createUpdate(ModuleFeatureBean modulefeatureBean,WhereCondition searchParameter,boolean executeAllNull, String[] executeFields)throws Exception;
	
	public SqlArg createQuery(Class<? extends ModuleFeatureBean> classToQuery,WhereCondition searchParameter)throws Exception;
	
	public BatchSqlArg createBatchInsert(List<? extends ModuleFeatureBean> modulefeatureBeans,String[] executeFields);
	
	public WhereCondition createByIdCondition(ModuleFeatureBean modulefeatureBean,String[] executeFields,boolean withWhere) throws Exception;
	
	public Object[] createByIdConditionAndShardingTable(ModuleFeatureBean modulefeatureBean,String[] executeFields,boolean withWhere) throws Exception;
	
	public WhereCondition createByIdCondition(Class<? extends ModuleFeatureBean> classToQuery,Object[] idValues, boolean withWhere) throws Exception;
	
}
