package com.fantasy.app.core.component.compress;

/**
 * 压缩用常量
 * @author 公众号：18岁fantasy
 * @2015年1月21日 @下午3:36:09
 */
public class CompressStatic {

	public enum COMPRESS_ALGORITHM{
		JAVA_GZIP
	}
	/**
	 * 缓存大小
	 */
	public static int BUFFER_SIZE = 1024;
	/**
	 * 默认编码
	 */
	public static String ENCODING = "ISO-8859-1";
}
