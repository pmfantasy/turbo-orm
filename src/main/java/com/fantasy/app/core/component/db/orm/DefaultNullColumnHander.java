package com.fantasy.app.core.component.db.orm;

/**
 * 默认的空值处理
 * @日期：2012-12-14下午11:18:03
 * @作者：公众号：18岁fantasy
 */

public class DefaultNullColumnHander implements NullColumnHander {

	@Override
	public Object warpNull(ColumnInfo nullColumn) {
		
		if(nullColumn.isNullable()){
			//@TODO 如果数据库可以为空，根据数据库不同字段类型返回数据库能保存的空值
			return null;
		}
		return null;
	}

}
