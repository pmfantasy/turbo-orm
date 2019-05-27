package com.fantasy.app.core.component.encrypt;

/**
 * 加密过滤
 * @author 公众号：18岁fantasy
 * @2015-2-6 @上午10:00:46
 */
public interface EncryptFilter<T> {

	/**
	 * 验证当前对象是否不需要加密
	 * @param toCompressObj 要验证的待价密对象
	 * @return
	 */
	public  boolean exclude(T toCompressObj);
}
