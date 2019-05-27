package com.fantasy.app.core.component.updown;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fantasy.app.core.para.ComponentPara.MULTI;


/**
 * 文件目录和文件名存放策略
 * @日期：2012-12-16上午5:51:16
 * @作者：公众号：18岁fantasy
 */
public abstract class FileStorageStrategy {
	//10m
	public static final long DEFAULT_UPLOADSIZE = 10485760l;
	
	public  String getTmpDir() {
		return getBaseDir()+File.separator+"tmp";
	}
    public String getBaseDir(){
    	return  MULTI.UPLOAD_DEFAULT_BASEDIR;
    }
	public long getMaxSize() {
		Long maxSize = MULTI.UPLOAD_DEFAULT_MAXSIZE;
		if(maxSize==0||maxSize.longValue()==0l){
			maxSize = DEFAULT_UPLOADSIZE;
		}
		return maxSize;
	}
	/**
	 * 文件存储的全路径eg:E:/temp/soft/file.txt
	 * @param rawFileName
	 * @return
	 */
	public abstract String generalUploadFilePath(String rawFileName);
	
	/**
	 * 判断是否上传有有效文件
	 * @return
	 */
	public static boolean hasUploadFile(MultipartHttpServletRequest request) {
		boolean flag = false;
		Map<String, MultipartFile> multipartFiles = request.getFileMap();
		for (Iterator<Map.Entry<String, MultipartFile>> iterator = multipartFiles.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, MultipartFile> upLoadResult = iterator.next();
			CommonsMultipartFile file = (CommonsMultipartFile) upLoadResult.getValue();
			if (file.getFileItem().getSize() > 0 && file.getSize() > 0  ) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
}
