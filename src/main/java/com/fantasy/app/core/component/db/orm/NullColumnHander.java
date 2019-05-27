package com.fantasy.app.core.component.db.orm;

/**
 * 当前属性的值为空时的处理，需根据数据库情况进行定义，
 *  例如null值是否需要最终存为“Null”字符串
 *  @see DefaultNullColumnHander
 * @日期：2012-12-14下午11:19:17
 * @作者：公众号：18岁fantasy
 */
public interface NullColumnHander {

	public Object warpNull(ColumnInfo nullColumn);
}
