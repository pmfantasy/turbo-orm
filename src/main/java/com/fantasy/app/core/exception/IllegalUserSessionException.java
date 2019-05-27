package com.fantasy.app.core.exception;


/**
 * 用户登录session过期异常类
 * @日期：2012-12-14下午11:17:32
 * @作者：SINE
 */
public class IllegalUserSessionException extends Exception implements MessageAlertable,Logable{
	private static final long serialVersionUID = 1L;
   
	public IllegalUserSessionException(Throwable e){
		super( e);
	}
	
	public IllegalUserSessionException(String msg,Throwable e){
		super(msg, e);
	}
	public IllegalUserSessionException(String msg){
		super(msg);
	}
}
