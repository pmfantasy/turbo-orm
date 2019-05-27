package com.fantasy.app.core.component.filesplit;

import java.io.File;
import java.util.List;

import com.fantasy.app.core.exception.FileSplitException;


/**
 * 拆包解包接口
 * @author 公众号：18岁fantasy
 * @2015-2-6 @下午6:17:12
 */
public interface FileSplitService {

	/**
	 * 拆文件
	 * @param file
	 * @param dirTmp
	 * @param size
	 * @param deleteSrcFile
	 * @return
	 * @throws FileSplitException
	 */
	public  Packet splitFile(File file,String dirTmp,long size,boolean deleteSrcFile) throws FileSplitException;
	/**
	 * 组文件
	 * @param deleteSplitFile
	 * @param files
	 * @return
	 * @throws FileSplitException
	 */
	public  String unionFile(boolean deleteSplitFile,List<String> files) throws FileSplitException;
	/**
	 * 组文件
	 * @param newFile 
	 * @param deleteSplitFile
	 * @param files
	 * @return
	 * @throws FileSplitException
	 */
	public  String unionFile(String newFile,boolean deleteSplitFile,List<String> files) throws FileSplitException;
	/**
	 * 通过一个分割文件获取，同一组的分割文件列表
	 * @param splitFile
	 * @return
	 * @throws FileSplitException
	 */
	public List<String> getSplitFileListFromSplitFile(String splitFile)throws FileSplitException;
	
}
