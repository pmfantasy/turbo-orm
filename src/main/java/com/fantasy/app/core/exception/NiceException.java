package com.fantasy.app.core.exception;


/**
 * 当抛出此类异常的时候，系统认为异常的message是友好的（可以被显示在界面上的） yeah it's nice
 * @日期：2012-12-14下午11:17:24
 * @作者：公众号：18岁fantasy
 */
public class NiceException extends Exception implements  MessageAlertable,Logable{
	private static final long serialVersionUID = 1L;
   
	public NiceException(String niceMsg,Throwable e){
		super(niceMsg, e);
	}
	
	public NiceException(String niceMsg){
		super(niceMsg);
	}
	public NiceException(Throwable e){
		super(e);
	}
}
