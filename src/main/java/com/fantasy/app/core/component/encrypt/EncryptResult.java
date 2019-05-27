package com.fantasy.app.core.component.encrypt;
/**
 * 加密结果
 * @author 公众号：18岁fantasy
 * @2015-2-4 @下午3:08:09
 */
public class EncryptResult<T> {
	
	
	public EncryptResult() {
		super();
	}

	public EncryptResult(boolean encrypted, T encryptResultObj) {
		super();
		this.encrypted = encrypted;
		this.encryptResultObj = encryptResultObj;
	}


	/**
	 * 是否被加密了
	 */
	private boolean encrypted;
	/**
	 * 加密返回结果
	 */
	private T encryptResultObj;
	

	public boolean isEncrypted() {
		return encrypted;
	}


	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}


	public T getEncryptResultObj() {
		return encryptResultObj;
	}


	public void setEncryptResultObj(T encryptResultObj) {
		this.encryptResultObj = encryptResultObj;
	}
	
	
}
