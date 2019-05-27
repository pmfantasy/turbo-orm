package com.fantasy.app.core.component.encrypt;

/**
 * 加密用常量
 * @author 公众号：18岁fantasy
 * @2015年1月21日 @下午3:36:09
 */
public class EncryptStatic {

	public enum ENCRYPT_ALGORITHM{
		DES,
		DESede,
		AES,
		Blowfish,
		RC2,
		RC4
	}
	/**
	 * 缓存大小
	 */
	public static int BUFFER_SIZE = 1024;
	/**
	 * 默认编码
	 */
	public static String ENCODING = "UTF-8";
	/**
	 * 加密文件后缀
	 */
	public static final String ENCRYPT_FILE_SUFIX = ".encrypt";
}
