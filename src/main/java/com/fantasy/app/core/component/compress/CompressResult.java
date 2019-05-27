package com.fantasy.app.core.component.compress;
/**
 * 压缩结果
 * @author 公众号：18岁fantasy
 * @2015-2-4 @下午3:08:09
 * @param <T>
 */
public class CompressResult<T> {

	/**
	 * 是否被压缩了
	 */
	private boolean commpressed;
	/**
	 * 压缩返回结果
	 */
	private T commpressResultObj;
	
	
	public CompressResult() {
		super();
	}
	public CompressResult(boolean commpressed) {
		this.commpressed = commpressed;
	}
	public CompressResult(boolean commpressed, T commpressResultObj) {
		this.commpressed = commpressed;
		this.commpressResultObj = commpressResultObj;
	}
	public boolean isCommpressed() {
		return commpressed;
	}
	public void setCommpressed(boolean commpressed) {
		this.commpressed = commpressed;
	}
	public T getCommpressResultObj() {
		return commpressResultObj;
	}
	public void setCommpressResultObj(T commpressResultObj) {
		this.commpressResultObj = commpressResultObj;
	}
	
	
	
	
}
