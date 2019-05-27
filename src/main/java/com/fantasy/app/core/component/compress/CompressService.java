package com.fantasy.app.core.component.compress;

import java.io.File;

import com.fantasy.app.core.exception.CompressException;


public interface CompressService {

	/**
	 * 将一个字符串使用gzip的形式压缩
	 * @param str 原始字符串
	 * @return 压缩后的字符串
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(String str,CompressFilter<String> filter) throws CompressException;
	/**
	 * 将一个字符串使用gzip的形式压缩
	 * @param str 原始字符串
	 * @return 压缩后的字符串
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(String str) throws CompressException;
	
	/**
	 * 将一个压缩后的字符串使用gzip的形式解压缩
	 * @param str 压缩后的字符串
	 * @return 原始字符串
	 * @throws CompressException 
	 */
	public  String uncompress(String str) throws CompressException;
	
	
	/**
	 * 文件压缩
	 * @param srcFile 原始文件
	 * @return 压缩后的文件路径
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(File srcFile) throws CompressException;
	/**
	 * 文件压缩
	 * @param srcFile 原始文件
	 * @param gzipName 压缩后的文件名称
	 * @return 压缩后的文件路径
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(File srcFile,String gzipName) throws CompressException;
	/**
	 * 文件压缩
	 * @param srcFile 原始文件
	 * @param delete 是否删除原始文件
	 * @return 压缩后的文件路径
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(File srcFile,boolean delete) throws CompressException;
	/**
	 * 文件压缩
	 * @param srcFile 原始文件
	 * @return 压缩后的文件路径
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(File srcFile,CompressFilter<File> filter) throws CompressException;
	/**
	 * 文件压缩
	 * @param srcFile 原始文件
	 * @param gzipName 压缩后的文件名称
	 * @param delete 是否删除原始文件
	 * @return 压缩后的文件路径
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(File srcFile,String gzipName,boolean delete) throws CompressException;
	
	/**
	 * 文件压缩
	 * @param srcFile 原始文件
	 * @param gzipName 压缩后的文件名称
	 * @return 压缩后的文件路径
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(File srcFile,CompressFilter<File> filter,String gzipName) throws CompressException;
	
	
	/**
	 * 文件压缩
	 * @param srcFile 原始文件
	 * @param delete 是否删除原始文件
	 * @return 压缩后的文件路径
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(File srcFile,CompressFilter<File> filter,boolean delete) throws CompressException;
	
	/**
	 * 文件压缩
	 * @param srcFile 原始文件
	 * @param gzipName 压缩后的文件名称
	 * @param delete 是否删除原始文件
	 * @return 压缩后的文件路径
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(File srcFile,CompressFilter<File> filter,String gzipFilePath,boolean delete) throws CompressException;
	
	

	/**
	 * 文件解压缩
	 * @param gzipFile gzip文件
	 * @param filename 解压后的文件名
	 * @return 解压后的文件名
	 * @throws CompressException 
	 */
	public  String uncompress(File gzipFile) throws CompressException;


	/**
	 * 文件解压缩
	 * @param gzipFile gzip文件
	 * @param filename 解压后的文件名
	 * @param delete 是否删除gzip文件
	 * @return 解压后的文件名
	 * @throws CompressException 
	 */
	public  String uncompress(File gzipFile,boolean delete) throws CompressException;
	/**
	 * 文件解压缩
	 * @param gzipFile gzip文件
	 * @param filename 解压后的文件名
	 * @return 解压后的文件名
	 * @throws CompressException 
	 */
	public  String uncompress(File gzipFile,String filename) throws CompressException;
	
	/**
	 * 文件解压缩
	 * @param gzipFile gzip文件
	 * @param filename 解压后的文件名
	 * @param delete 是否删除gzip文件
	 * @return 解压后的文件名
	 * @throws CompressException 
	 */
	public  String uncompress(File gzipFile,String filename,boolean delete) throws CompressException;
	
}
