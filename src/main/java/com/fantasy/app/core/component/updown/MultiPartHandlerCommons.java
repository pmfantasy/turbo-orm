package com.fantasy.app.core.component.updown;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
/**
 * 文件上传处理类 无spring，此种环境无法使用spring来获取request里面的其他参数，需要自行获取(eg:request.getParameter)
 * @日期：2013-01-08上午09:02:50
 * @作者：公众号：18岁fantasy
 */
public class MultiPartHandlerCommons extends CommonsMultipartResolver implements
		MultipartResolver, ServletContextAware {
	
	private FileStorageStrategy fileStorageStrategy;


	/**
	 * 
	 * @param fileStorageStrategy 存储策略
	 * @throws IOException
	 */
	public MultiPartHandlerCommons(FileStorageStrategy fileStorageStrategy) throws IOException{
		if(fileStorageStrategy==null)
			this.fileStorageStrategy = new DefaulFileStorageStrategy();
		this.fileStorageStrategy = fileStorageStrategy;
		if(fileStorageStrategy.getMaxSize()<1l)super.setMaxUploadSize(FileStorageStrategy.DEFAULT_UPLOADSIZE);
		if(StringUtils.hasText(fileStorageStrategy.getTmpDir()))
			super.setUploadTempDir(new FileSystemResource(fileStorageStrategy.getTmpDir()));
	}
	@SuppressWarnings("unchecked")
	protected MultipartParsingResult parseRequest(HttpServletRequest request)
			throws MultipartException {
		String encoding = determineEncoding(request);
		FileUpload fileUpload = prepareFileUpload(encoding);
		try {
			List<FileItem> fileItems = ((ServletFileUpload) fileUpload)
					.parseRequest(request);
			return parseFileItems(fileItems, encoding);
		} catch (FileUploadBase.SizeLimitExceededException ex) {
			throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
		} catch (FileUploadException ex) {
			throw new MultipartException(
					"Could not parse multipart servlet request", ex);
		}
	}
    public List<UpLoadResult> uploadFile(HttpServletRequest request) throws MaxUploadSizeExceededException{
    	MultiValueMap<String, MultipartFile> multiFiles = null;
    	List<UpLoadResult> upLoadResults = new ArrayList<UpLoadResult>();
    	try {
    		MultipartParsingResult multipartParsingResult = parseRequest(request);
    		multiFiles = multipartParsingResult.getMultipartFiles();
    		for (Iterator<String> iterator = multiFiles.keySet().iterator(); iterator.hasNext();) {
    			String multipartFormKey =  iterator.next();
    			List<MultipartFile> files = multiFiles.get(multipartFormKey);
    			for (Iterator<MultipartFile> iterator2 = files.iterator(); iterator2.hasNext();) {
					MultipartFile multipartFile =  iterator2.next();
					String savedFilePaht = this.fileStorageStrategy.generalUploadFilePath(multipartFile.getOriginalFilename());
					File fileResult = new File(savedFilePaht);
					FileUtils.writeByteArrayToFile(fileResult, multipartFile.getBytes());
					upLoadResults.add(new UpLoadResult(true, multipartFile.getOriginalFilename(), savedFilePaht));
				}
			}
    		
		} catch (MaxUploadSizeExceededException e) {
			FileUploadBase.SizeLimitExceededException ex = (FileUploadBase.SizeLimitExceededException)e.getCause();
			UpLoadResult loadResultError = new UpLoadResult(false, ex.getPermittedSize(), ex.getActualSize(), UpLoadResult.UPLOAD_ERROR_TYPE.SizeExceeded,e);
			upLoadResults.add(loadResultError);
		}catch (Exception e) {
			UpLoadResult loadResultError = new UpLoadResult(false,UpLoadResult.UPLOAD_ERROR_TYPE.Other,e);
			upLoadResults.add(loadResultError);
		}finally{
			if(multiFiles!=null)
			cleanupFileItems(multiFiles);
		}
    	return upLoadResults;
    }
}

