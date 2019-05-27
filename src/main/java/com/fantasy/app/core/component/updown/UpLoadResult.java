package com.fantasy.app.core.component.updown;

/**
 * 上传结果
 * @日期：2013-1-8上午11:46:29
 * @作者：公众号：18岁fantasy
 */
public class UpLoadResult {

	//上传成功与否 true：成功
	private boolean success;
	//原始文件名 注：当success为true时才有此值
	private String rawFileName;
	//新的文件全路径 注：当success为true时才有此值
	private String savedFilePath;
	//最大大小（bytes）
	private long maxSize;
	//失败信息：实际大小（bytes）
	private long actualSize;
	//失败信息：已知的错误类型
	private UPLOAD_ERROR_TYPE error_TYPE;
	//失败信息：原始异常
	private Exception exception;
	
	
	public UpLoadResult(boolean success) {
		super();
		this.success = success;
	}

	public UpLoadResult(boolean success, String rawFileName,
			String savedFilePath) {
		super();
		this.success = success;
		this.rawFileName = rawFileName;
		this.savedFilePath = savedFilePath;
	}
	public UpLoadResult(boolean success, UPLOAD_ERROR_TYPE error_TYPE,
			Exception exception) {
		super();
		this.success = success;
		this.error_TYPE = error_TYPE;
		this.exception = exception;
	}

	public UpLoadResult(boolean success, long maxSize, long actualSize,
			UPLOAD_ERROR_TYPE error_TYPE, Exception exception) {
		super();
		this.success = success;
		this.maxSize = maxSize;
		this.actualSize = actualSize;
		this.error_TYPE = error_TYPE;
		this.exception = exception;
	}

	
	public UpLoadResult(boolean success, String rawFileName, String savedFilePath, long maxSize, long actualSize) {
		super();
		this.success = success;
		this.rawFileName = rawFileName;
		this.savedFilePath = savedFilePath;
		this.maxSize = maxSize;
		this.actualSize = actualSize;
	}


	public static enum UPLOAD_ERROR_TYPE{
		//操作最大限制异常
		SizeExceeded,
		//其他
		Other
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public long getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}

	public long getActualSize() {
		return actualSize;
	}

	public void setActualSize(long actualSize) {
		this.actualSize = actualSize;
	}

	public UPLOAD_ERROR_TYPE getError_TYPE() {
		return error_TYPE;
	}

	public void setError_TYPE(UPLOAD_ERROR_TYPE error_TYPE) {
		this.error_TYPE = error_TYPE;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public String getRawFileName() {
		return rawFileName;
	}


	public void setRawFileName(String rawFileName) {
		this.rawFileName = rawFileName;
	}

	public String getSavedFilePath() {
		return savedFilePath;
	}

	public void setSavedFilePath(String savedFilePath) {
		this.savedFilePath = savedFilePath;
	}

	@Override
	public String toString() {
		return "UpLoadResult [success=" + success + ", rawFileName=" + rawFileName + ", savedFilePath=" + savedFilePath
				+ ", maxSize=" + maxSize + ", actualSize=" + actualSize + ", error_TYPE=" + error_TYPE + ", exception="
				+ exception + "]";
	}
}
