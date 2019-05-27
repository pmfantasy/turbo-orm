package com.fantasy.app.core.exception;


/**
 * service 相关类异常
 * @日期：2012-12-14下午11:17:12
 * @作者：公众号：18岁fantasy
 */
public class InitException extends Exception implements MessageAlertable,Logable{
	private static final long serialVersionUID = 1L;
   
	public InitException(Throwable e){
		super( e);
	}
	public InitException(String msg,Throwable e){
		super(msg, e);
	}
	public InitException(String msg){
		super(msg);
	}
}
