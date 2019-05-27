package com.fantasy.app.core.component.compress;

import com.fantasy.app.core.component.compress.CompressStatic.COMPRESS_ALGORITHM;

/**
 * 压缩算法
 * @author 公众号：18岁fantasy
 * @2015-2-4 @下午2:36:41
 */
public class CompressServiceFactory {

	public static  CompressService createCompressService(COMPRESS_ALGORITHM algorithm){
		if(algorithm==COMPRESS_ALGORITHM.JAVA_GZIP){
			return new JavaGzipCompress();
		}else{
			throw new RuntimeException("不支持的压缩算法");
		}
	}
}
