package com.fantasy.app.core.component.db;

/**
 * 单事务操作类，有回值
 * @日期：2012-12-14下午11:22:37
 * @作者：公众号：18岁fantasy
 */
public interface SingleTransationCircleWithResult<T> {

	public   T actionInCircle() throws RuntimeException;
}
