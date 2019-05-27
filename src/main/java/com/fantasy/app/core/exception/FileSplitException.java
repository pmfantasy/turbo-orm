package com.fantasy.app.core.exception;


/**
 * 文件拆包解包异常
 * @author 公众号：18岁fantasy
 * @2015年1月21日 @下午3:38:44
 */
public class FileSplitException extends Exception{

	public FileSplitException(Throwable e) {
		super();
	}
	public FileSplitException(String msg) {
		super(msg);
	}
	public FileSplitException(String msg,Throwable e) {
		super(msg,e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
