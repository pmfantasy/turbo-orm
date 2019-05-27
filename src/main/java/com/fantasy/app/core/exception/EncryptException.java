package com.fantasy.app.core.exception;


/**
 * 加密相关异常
 * @author 公众号：18岁fantasy
 * @2015年1月21日 @下午3:38:44
 */
public class EncryptException extends Exception{

	public EncryptException(Throwable e) {
		super();
	}
	public EncryptException(String msg) {
		super(msg);
	}
	public EncryptException(String msg,Throwable e) {
		super(msg,e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
