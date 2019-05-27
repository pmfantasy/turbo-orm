package com.fantasy.app.core.exception;


/**
 * service 相关类异常
 * @日期：2012-12-14下午11:17:32
 * @作者：公众号：18岁fantasy
 */
public class ServiceException extends Exception implements MessageAlertable,Logable{
	private static final long serialVersionUID = 1L;
   
	public ServiceException(Throwable e){
		super( e);
	}
	
	public ServiceException(String msg,Throwable e){
		super(msg, e);
	}
	public ServiceException(String msg){
		super(msg);
	}
}
