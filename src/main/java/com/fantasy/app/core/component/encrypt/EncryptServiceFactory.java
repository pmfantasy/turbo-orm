package com.fantasy.app.core.component.encrypt;

import com.fantasy.app.core.component.encrypt.EncryptStatic.ENCRYPT_ALGORITHM;

/**
 * 获取加密服务的工厂
 * @author 公众号：18岁fantasy
 * @2015-2-4 @下午2:36:41
 */
public class EncryptServiceFactory {

	public static  EncryptService createEncryptService(ENCRYPT_ALGORITHM algorithm){
		if(algorithm==ENCRYPT_ALGORITHM.DES){
			return new DesEncrypt();
		}else{
			throw new RuntimeException("不支持的加密算法");
		}
	}
}
