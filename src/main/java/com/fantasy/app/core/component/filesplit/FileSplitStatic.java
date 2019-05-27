package com.fantasy.app.core.component.filesplit;

/**
 * 文件分割参数
 * @author 公众号：18岁fantasy
 * 2017-5-25 下午3:36:58
 */
public class FileSplitStatic {

	public static long DEFAULT_SIZE = 1024*1024*10;//默认分隔成的文件的大小
	
	public static int BUFFER_LENGTH = 1024;//默认一次读取的大小
	
	public static String SPLIT_FILE_SUFFIX=".split";
	
	public enum FILESPLIT_ALGORITHM{
		SIMPLE
	}
}
