package com.fantasy.app.core.exception;


/**
 * 压缩相关异常
 * @author 公众号：18岁fantasy
 * @2015年1月21日 @下午3:38:44
 */
public class CompressException extends Exception{

	public CompressException(Throwable e) {
		super();
	}
	public CompressException(String msg) {
		super(msg);
	}
	public CompressException(String msg,Throwable e) {
		super(msg,e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
