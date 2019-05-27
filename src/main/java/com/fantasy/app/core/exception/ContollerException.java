package com.fantasy.app.core.exception;


/**
 * Contoller 相关类异常
 * @日期：2012-12-14下午11:16:49
 * @作者：公众号：18岁fantasy
 */
public class ContollerException extends Exception implements MessageAlertable,Logable{
	private static final long serialVersionUID = 1L;
   
	public ContollerException(String msg,Throwable e){
		super(msg, e);
	}
	public ContollerException(String msg){
		super(msg);
	}
}
