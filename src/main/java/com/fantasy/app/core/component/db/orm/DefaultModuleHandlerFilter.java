package com.fantasy.app.core.component.db.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
/**
 * 默认的moudle解析过滤类
 * @日期：2012-12-14下午11:17:56
 * @作者：公众号：18岁fantasy
 */
public class DefaultModuleHandlerFilter implements ModuleHandlerFilter{

	/**
	 * 对有 @see SiNotDbColumn 标签标注的属性和serialVersionUID（序列化标示）,static 统一不解析
	 */
	@Override
	public boolean exclude(Field moduleField) {
		boolean bingo = 
				"serialVersionUID".equalsIgnoreCase(moduleField.getName())||
						Modifier.isStatic(moduleField.getModifiers());
				
		return bingo;
	}
	/**
	 * 直解析所有的私有属性
	 */
	@Override
	public boolean include(Field moduleField) {
		boolean bingo = 
				"private".equalsIgnoreCase(Modifier.toString(moduleField.getModifiers()));
		
		return bingo;
	}

}
