package com.fantasy.app.core.exception;

/**
 * 数据库操作异常
 * @日期：2012-12-14下午11:16:59
 * @作者：公众号：18岁fantasy
 */
public class DaoException extends Exception {
	private static final long serialVersionUID = 1L;
   
	public DaoException(String msg,Throwable e){
		super(msg, e);
	}
	public DaoException(Throwable e){
		super(e);
	}
}
