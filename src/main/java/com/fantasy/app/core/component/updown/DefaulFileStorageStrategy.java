package com.fantasy.app.core.component.updown;



/**
 * 默认的文件路径策略
 * @日期：2012-12-16上午5:50:40
 * @作者：公众号：18岁fantasy
 */
public class DefaulFileStorageStrategy extends FileStorageStrategy {

	
	@Override
	public String generalUploadFilePath(String rawFileName) {
		return super.getBaseDir()+rawFileName;
	}

}
