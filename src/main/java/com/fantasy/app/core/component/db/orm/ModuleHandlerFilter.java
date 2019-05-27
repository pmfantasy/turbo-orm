package com.fantasy.app.core.component.db.orm;

import java.lang.reflect.Field;

/**
 * module 解析过滤接口
 * @日期：2012-12-14下午11:18:49
 * @作者：公众号：18岁fantasy
 */
public interface ModuleHandlerFilter {

	/**
	 * 自定义规则来决定当前的field是否不解析
	 * @param moduleField
	 * @return
	 */
	public boolean exclude(Field moduleField);
	
	/**
	 * 自定义规则来决定当前的field是否解析
	 * @param moduleField
	 * @return
	 */
	public boolean include(Field moduleField);
}
