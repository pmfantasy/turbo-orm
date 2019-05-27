package com.fantasy.app.core.exception;
/**
 * Jmx异常
 * @author Administrator
 *
 */
public class JmxException  extends Exception {
	private static final long serialVersionUID = 1L;
   
	public JmxException(String msg,Throwable e){
		super(msg, e);
	}
	public JmxException(Throwable e){
		super(e);
	}
	public JmxException(String string) {
		super(string);
	}
}
