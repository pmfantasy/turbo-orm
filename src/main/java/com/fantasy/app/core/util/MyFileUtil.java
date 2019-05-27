package com.fantasy.app.core.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MyFileUtil {
	
	/**  
	 * 往文件里追加内容：使用RandomAccessFile  
	 *   
	 * @param fileName 文件名  
	 * @param content 追加的内容  
	 * @param backward 往后插入几个字符 
	 */
	public static void appendContentToFile(File file, String content, int backward) {
		RandomAccessFile randomFile = null;
		try {
			// 打开一个随机访问文件流，按读写方式   
			randomFile = new RandomAccessFile(file, "rw");
			// 文件长度，字节数   
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。   
			randomFile.seek(fileLength - backward);
			randomFile.writeBytes(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
