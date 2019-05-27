package com.fantasy.app.core.component.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.fantasy.app.core.exception.CompressException;
import com.fantasy.app.core.util.StrUtil;


/**
 * gzip 压缩
 * @author 公众号：18岁fantasy
 * @2015年1月21日 @下午3:25:08
 */
public class JavaGzipCompress implements  CompressService{

	public static final String EXT = ".gz";
	/**
	 * 将一个字符串使用gzip的形式压缩
	 * @param str 原始字符串
	 * @return 压缩后的字符串
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(String str,CompressFilter<String> filter) throws CompressException{
		if (StrUtil.isBlank(str)) {  
		    return new CompressResult<String>(false, str);   
		} 
		if(filter!=null&&filter.exclude(str)){
			return new CompressResult<String>(false, str);
		}
		
		ByteArrayInputStream is = null;
		ByteArrayOutputStream  os = null;
		try {
			is = new ByteArrayInputStream(str.getBytes("UTF-8"));
			os = new ByteArrayOutputStream();
		
			//压缩
			compress(is, os);
			return  new CompressResult<String>(true,os.toString(CompressStatic.ENCODING));//将输出流中的数据转换成字符串
		} catch (IOException e) {
			throw new CompressException(e);
		}finally{
			try {
				if(is!=null)is.close();
				if(os!=null)os.close();
			} catch (IOException e) {
				//
			}
		} 
	}
	/**
	 * 将一个字符串使用gzip的形式压缩
	 * @param str 原始字符串
	 * @return 压缩后的字符串
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(String str) throws CompressException{
		return compress(str, null);
	}
	
	/**
	 * 将一个压缩后的字符串使用gzip的形式解压缩
	 * @param str 压缩后的字符串
	 * @return 原始字符串
	 * @throws CompressException 
	 */
	public  String uncompress(String str) throws CompressException{
		if (StrUtil.isBlank(str)) {   
		     return str;   
		 } 
		 ByteArrayOutputStream os = new ByteArrayOutputStream();   
		 ByteArrayInputStream is=null;
		   try {
			 is= new ByteArrayInputStream(str.getBytes(CompressStatic.ENCODING));
			//解压缩
			uncompress(is, os);
			return os.toString("UTF-8");//将输出流中的数据转换成字符串
		} catch (UnsupportedEncodingException e) {
			//
		} catch (Exception e) {
			throw new CompressException(e);
		}finally{
			try {
				if(is!=null)is.close();
				if(os!=null)os.close();
			} catch (IOException e) {
				//
			}
		} 
		return null;
	}
	
	
	/**
	 * 文件压缩
	 * @param srcFile 原始文件
	 * @return 压缩后的文件路径
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(File srcFile) throws CompressException{
		
		return compress(srcFile,null, srcFile.getAbsolutePath()+EXT, false);
	}
	/**
	 * 文件压缩
	 * @param srcFile 原始文件
	 * @param gzipName 压缩后的文件名称
	 * @return 压缩后的文件路径
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(File srcFile,String gzipName) throws CompressException{
		return compress(srcFile, null,gzipName, false);
	}
	/**
	 * 文件压缩
	 * @param srcFile 原始文件
	 * @param delete 是否删除原始文件
	 * @return 压缩后的文件路径
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(File srcFile,boolean delete) throws CompressException{
		return compress(srcFile,null, srcFile.getAbsolutePath()+EXT, delete);
	}
	/**
	 * 文件压缩
	 * @param srcFile 原始文件
	 * @return 压缩后的文件路径
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(File srcFile,CompressFilter<File> filter) throws CompressException{
		
		return compress(srcFile,filter, srcFile.getAbsolutePath()+EXT, false);
	}
	/**
	 * 文件压缩
	 * @param srcFile 原始文件
	 * @param gzipName 压缩后的文件名称
	 * @param delete 是否删除原始文件
	 * @return 压缩后的文件路径
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(File srcFile,String gzipName,boolean delete) throws CompressException{
		return compress(srcFile, null, gzipName, delete);
	}
	
	/**
	 * 文件压缩
	 * @param srcFile 原始文件
	 * @param gzipName 压缩后的文件名称
	 * @return 压缩后的文件路径
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(File srcFile,CompressFilter<File> filter,String gzipName) throws CompressException{
		return compress(srcFile, filter,gzipName, false);
	}
	
	
	/**
	 * 文件压缩
	 * @param srcFile 原始文件
	 * @param delete 是否删除原始文件
	 * @return 压缩后的文件路径
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(File srcFile,CompressFilter<File> filter,boolean delete) throws CompressException{
		return compress(srcFile,filter, srcFile.getAbsolutePath()+EXT, delete);
	}
	
	/**
	 * 文件压缩
	 * @param srcFile 原始文件
	 * @param gzipName 压缩后的文件名称
	 * @param delete 是否删除原始文件
	 * @return 压缩后的文件路径
	 * @throws CompressException 
	 */
	public  CompressResult<String> compress(File srcFile,CompressFilter<File> filter,String gzipFilePath,boolean delete) throws CompressException{
		
		if(filter!=null&&filter.exclude(srcFile)){
			return new CompressResult<String>(false, srcFile.getAbsolutePath());
		}
		FileInputStream is=null;
		FileOutputStream os=null;
		try {
			is=new FileInputStream(srcFile);
			os=new FileOutputStream(gzipFilePath);
			compress(is, os);
			is.close();
			os.close();
			if (delete) {
				srcFile.delete();//删除原文件
			}
			return new CompressResult<String>(true, gzipFilePath);
		} catch (FileNotFoundException e) {
			throw new CompressException(e);
		} catch (IOException e) {
			throw new CompressException(e);
		}finally{
			try {
				if(is!=null)is.close();
				if(os!=null)os.close();
			} catch (IOException e) {
				//
			}
		} 
	}
	
	

	/**
	 * 文件解压缩
	 * @param gzipFile gzip文件
	 * @param filename 解压后的文件名
	 * @return 解压后的文件名
	 * @throws CompressException 
	 */
	public  String uncompress(File gzipFile) throws CompressException{
		String filename=gzipFile.getAbsolutePath().replace(EXT, "");
		return uncompress(gzipFile,filename);
	}


	/**
	 * 文件解压缩
	 * @param gzipFile gzip文件
	 * @param filename 解压后的文件名
	 * @param delete 是否删除gzip文件
	 * @return 解压后的文件名
	 * @throws CompressException 
	 */
	public  String uncompress(File gzipFile,boolean delete) throws CompressException{
		String filename=gzipFile.getAbsolutePath().replace(EXT, "");
		return uncompress(gzipFile,filename,delete);
	}
	/**
	 * 文件解压缩
	 * @param gzipFile gzip文件
	 * @param filename 解压后的文件名
	 * @return 解压后的文件名
	 * @throws CompressException 
	 */
	public  String uncompress(File gzipFile,String filename) throws CompressException{
		return uncompress(gzipFile, filename, false);
	}
	
	/**
	 * 文件解压缩
	 * @param gzipFile gzip文件
	 * @param filename 解压后的文件名
	 * @param delete 是否删除gzip文件
	 * @return 解压后的文件名
	 * @throws CompressException 
	 */
	public  String uncompress(File gzipFile,String filename,boolean delete) throws CompressException{
		FileInputStream is=null;
		FileOutputStream os=null;
		try {
			is=new FileInputStream(gzipFile);
			os=new FileOutputStream(filename);
			uncompress(is, os);
			is.close();
			os.close();
			if (delete) {
				gzipFile.delete();
			}
			return filename;
		} catch (FileNotFoundException e) {
			throw new CompressException(e);
		} catch (IOException e) {
			throw new CompressException(e);
		}finally{
			try {
				if(is!=null)is.close();
				if(os!=null)os.close();
			} catch (IOException e) {
				//
			}
		} 
	}
	/**
	 * 数据压缩
	 * @param is 输入流
	 * @param os 输出流
	 * @throws IOException 
	 */
	private  void compress(InputStream is ,OutputStream os) throws CompressException{
		GZIPOutputStream gos = null;
		try {
			gos = new GZIPOutputStream(os);
			byte[] buff=new byte[CompressStatic.BUFFER_SIZE];
			int len=0;
			while ((len=is.read(buff))>=0) {
				gos.write(buff,0,len);
			}
		} catch (IOException e) {
			throw new CompressException(e);
		}finally{
			try {
				gos.finish();
				gos.flush();
				gos.close();
			} catch (IOException e) {
				//
			}
			
		}
		
		
	}
	
	/**
	 * 数据解压缩
	 * @param is 输入流
	 * @param os 输出流
	 * @throws IOException 
	 */
	private   void uncompress(InputStream is ,OutputStream os) throws CompressException{
		GZIPInputStream gis = null;
		try {
			gis = new GZIPInputStream(is);
			byte[] buff=new byte[CompressStatic.BUFFER_SIZE];
			int len=0;
			while ((len=gis.read(buff))>=0) {
				os.write(buff, 0, len);
			}
		} catch (Exception e) {
			throw new CompressException(e);
		}finally{
			try {
				gis.close();
			} catch (IOException e) {
				//
			}
		}
		
	}
}
