package com.fantasy.app.core.util;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 存储资源先关util
 * @author 公众号：18岁fantasy
 * @2014-9-12 @下午4:56:15
 */
public class StorageResourcesUtil {

	/**
	 * 获取文件系统使用率
	 * @param dir
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static int getFileSystemUsedRate(String dir) throws FileNotFoundException {
	   File win = new File(dir);
	   if (win.exists()) {
	    long total = (long) win.getTotalSpace();
	    long free = (long) win.getFreeSpace();
	    Double compare = (Double) (1 - free * 1.0 / total) * 100;
	    return compare.intValue() ;
	   }else{
		   throw new FileNotFoundException("文件"+dir+"不存在！");
	   }
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println(getFileSystemUsedRate("d:\\sr1_stat_b.dmp"));
	}
}
