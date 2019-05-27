package com.fantasy.app.core.base;

import com.fantasy.app.core.para.CorePara.CoreInitCtx;


/**
 * 给普通的bean添加module特性（注：单表维护使用）
 * 如果继承此类，在项目启动时将会被扫描到，并被解析， 
 * 之后可即可直接通过此bean进行进行数据库的插入和查询操作
 *  <br/>相关注解:
 *  @see {@link com.fantasy.app.core.annotation.WsdTable}
 *  @see {@link com.fantasy.app.core.annotation.WsdColumnNullAble}
 *  @see {@link com.fantasy.app.core.annotation.WsdColumn}
 *  @see {@link com.fantasy.app.core.annotation.WsdNotDbColumn}
 * @日期：2012-12-14下午11:07:27
 * @作者：公众号：18岁fantasy
 */
public abstract class ModuleFeatureBean {
	/**
	 * 如果@WsdTable(name)中name不是常量时，可通过重写此方法设置表名,优先级高于{@link com.fantasy.app.core.annotation.WsdTable}
	 * @return
	 */
	public String tableName(){
		return null;
	}
	/**
	 * 如果@WsdTable中schema不是常量时，可通过重写此方法设置schema,优先级高于{@link com.fantasy.app.core.annotation.WsdTable}
	 * @return
	 */
	public String schema(){
		return CoreInitCtx.DEFAULT_SCHEMA_NAME;
	}
}
