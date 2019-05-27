package com.fantasy.app.core.component.updown;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 文件上传处理类
 * @日期：2013-01-08上午09:02:50
 * @作者：公众号：18岁fantasy
 */
@Component("multipartResolver")
//name 必须是multipartResolver
public class MultiPartHandlerSpring extends CommonsMultipartResolver implements MultipartResolver, ServletContextAware,InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		FileStorageStrategy fileStorageStrategy = new DefaulFileStorageStrategy();
		super.setMaxUploadSize(fileStorageStrategy.getMaxSize());
		super.setUploadTempDir(new FileSystemResource(fileStorageStrategy.getTmpDir()));
	}

	public static List<UpLoadResult> uploadFile(MultipartHttpServletRequest request,FileStorageStrategy fileStorageStrategy) {
		if (fileStorageStrategy == null) {
			fileStorageStrategy = new DefaulFileStorageStrategy();
		}
		List<UpLoadResult> upLoadResults = new ArrayList<UpLoadResult>();
		Map<String, MultipartFile> multipartFiles = request.getFileMap();
		for (Iterator<Map.Entry<String, MultipartFile>> iterator = multipartFiles.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, MultipartFile> upLoadResult = iterator.next();
			MultipartFile file = upLoadResult.getValue();
			if (file.getOriginalFilename() != null && StringUtils.hasText(file.getOriginalFilename())) {
				UpLoadResult loadResult = uploadFile(file, fileStorageStrategy);
				upLoadResults.add(loadResult);
			}
		}
		return upLoadResults;
	}

	private static UpLoadResult uploadFile(MultipartFile file, FileStorageStrategy fileStorageStrategy) {
		/*
		Spring上传空文件需要处理的地方
		CommonsMultipartFile cFile = (CommonsMultipartFile) file;
		if(cFile.getFileItem().getSize() == -1){
			return new UpLoadResult(false, UpLoadResult.UPLOAD_ERROR_TYPE.Other, new IllegalArgumentException("uploadfile size is -1"));
		}
		*/
		String savedFilePaht = fileStorageStrategy.generalUploadFilePath(file.getOriginalFilename());
		if (!StringUtils.hasText(savedFilePaht)) {
			savedFilePaht = new DefaulFileStorageStrategy().generalUploadFilePath(file.getOriginalFilename());
		}
		File fileResult = new File(savedFilePaht);
		try {
			FileUtils.writeByteArrayToFile(fileResult, file.getBytes());
		} catch (IOException e) {
			return new UpLoadResult(false, UpLoadResult.UPLOAD_ERROR_TYPE.Other, e);
		}
		return new UpLoadResult(true, file.getOriginalFilename(), savedFilePaht, fileStorageStrategy.getMaxSize(),file.getSize());
	}

	public static void downLoadFile(HttpServletResponse response, File file, String nameToShow) throws IOException {
		FileInputStream fileReader = null;
		try {
			fileReader = new FileInputStream(file);//此处将会产生FileNotFoundException
			byte[] buf = new byte[1024];
			int len = -1;
			if (!StringUtils.hasText(nameToShow))
				nameToShow = file.getName();
			nameToShow = new String(nameToShow.getBytes("gb2312"), "iso8859-1");
			response.addHeader("Content-disposition", "attachment;filename=" + nameToShow + "");
			//response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			OutputStream outputStream = response.getOutputStream();
			while ((len = fileReader.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}
			//需要flush，否则spring将进行内部处理 
			outputStream.flush();
			response.flushBuffer();
			//如果没有异常，关闭outputStream ,有异常则将response交给spring处理,response暂不关闭
			if (outputStream != null)
				outputStream.close();
		} finally {
			if (fileReader != null)
				fileReader.close();
		}
	}
}
