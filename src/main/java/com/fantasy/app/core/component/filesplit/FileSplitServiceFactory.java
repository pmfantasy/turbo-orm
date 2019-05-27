package com.fantasy.app.core.component.filesplit;

import com.fantasy.app.core.component.filesplit.FileSplitStatic.FILESPLIT_ALGORITHM;

/**
 * 获取拆包解包的工厂
 * @author 公众号：18岁fantasy
 * @2015-2-4 @下午2:36:41
 */
public class FileSplitServiceFactory {

	public static  FileSplitService createFileSplitService(FILESPLIT_ALGORITHM algorithm){
		if(algorithm==FILESPLIT_ALGORITHM.SIMPLE){
			return new SimpleFileSplit();
		}else{
			throw new RuntimeException("不支持的拆包解包算法");
		}
	}
}
