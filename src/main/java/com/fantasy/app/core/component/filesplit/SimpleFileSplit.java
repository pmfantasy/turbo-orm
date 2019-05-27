package com.fantasy.app.core.component.filesplit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fantasy.app.core.exception.FileSplitException;
import com.fantasy.app.core.util.StrUtil;
import com.fantasy.app.core.util.UUIDGenerator;

/**
 * simple 拆包组实现
 * @author 公众号：18岁fantasy
 * @2015-2-6 @下午6:17:28
 */
public class SimpleFileSplit implements FileSplitService{

	/**
	 * 拆文件
	 * @param file
	 * @param dirTmp
	 * @param size
	 * @param deleteSrcFile
	 * @return
	 * @throws FileSplitException
	 */
	@SuppressWarnings("resource")
	public  Packet splitFile(File file,String dirTmp,long size,boolean deleteSrcFile) throws FileSplitException{
		List<String> packetfile = new ArrayList<String>();
		Packet packet = new Packet();
		packet.setPacketfile(packetfile);
		String tn = UUIDGenerator.getUUID();
		try{
			if(size<=0)size = FileSplitStatic.DEFAULT_SIZE;
			BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream os = null;
			//计算生成的文件的个数
			int num = file.length()>=((file.length()/size)*size)?(int) (file.length()/size)+1:(int) (file.length()/size);
			if(num==1)return null;
			
			File parent=new File(dirTmp);
			long beginIndex=0l;
			long endIndex=0l;
			byte[] buffer=new byte[FileSplitStatic.BUFFER_LENGTH];
			int readCount=0;
			for (int i = 0; i < num; i++) {
				File newFile=new File(parent,file.getName()+"_"+num+"_"+i+"_"+tn+"_"+FileSplitStatic.SPLIT_FILE_SUFFIX);
				os=new BufferedOutputStream(new FileOutputStream(newFile));
				endIndex=(endIndex+size)>=file.length()?file.length():endIndex+size;
				for (;beginIndex<endIndex;) {
					if (endIndex-beginIndex>=FileSplitStatic.BUFFER_LENGTH) {//够读到byte中
						readCount=is.read(buffer);
						beginIndex+=readCount;
						os.write(buffer, 0, readCount);
						os.flush();
					}else{
						 for(;beginIndex<endIndex;beginIndex++) {  
		                      os.write(is.read());
		                    }  
						continue;
					}
				}
				os.close();
				packetfile.add(newFile.getAbsolutePath());
			}
			is.close();
			if(deleteSrcFile){
				file.delete();
			}
			return packet;
		} catch (Exception e) {
			throw new FileSplitException("文件拆包失败...",e);
		}
	}
	/**
	 * 组文件
	 * @param deleteSplitFile
	 * @param files
	 * @return
	 * @throws FileSplitException
	 */
	public  String unionFile(boolean deleteSplitFile,List<String> files) throws FileSplitException{
		return unionFile(null,deleteSplitFile, files);
	}
	/**
	 * 组文件
	 * @param newFile 
	 * @param deleteSplitFile
	 * @param files
	 * @return
	 * @throws FileSplitException
	 */
	@SuppressWarnings("resource")
	public  String unionFile(String newFile,boolean deleteSplitFile,List<String> files) throws FileSplitException{
		if(files==null||files.size()==0)return null;
		String rawFilePath = checkFile(files);
		if(StrUtil.isBlank(newFile)){
			newFile = rawFilePath;
		}
		try {
			File nf=new File(newFile);
			BufferedOutputStream os=new BufferedOutputStream(new FileOutputStream(nf));
			for (Iterator<String> iterator = files.iterator(); iterator.hasNext();) {
				String filesplit = (String) iterator.next();
				BufferedInputStream is=new BufferedInputStream(new FileInputStream(filesplit));
				byte[] buf=new byte[FileSplitStatic.BUFFER_LENGTH];
				int count=0;
				while ((count=is.read(buf))>=0) {
					os.write(buf, 0, count);
				}
				is.close();
				os.flush();
			}
			if(deleteSplitFile){
				for (Iterator<String> iterator = files.iterator(); iterator.hasNext();) {
					String filesplit = (String) iterator.next();
					new File(filesplit).delete();
				}
			}
			return newFile;
		} catch (Exception e) {
			throw new FileSplitException("文件组包失败...",e);
		}
	}
	private String checkFile(List<String> files) throws FileSplitException {
		String tn = null;
		String rawFilePath = null;
		Integer fileSum = null;
		Integer currentFileSum = null;
		int count = 0;
		String currentFile = "";
		String currentRawFilePath = null;
		Integer currentFileNum = null;
		String currentTn = null;
		for (Iterator<String> iterator = files.iterator(); iterator.hasNext();) {
			try {
				currentFile = iterator.next();
				if(!currentFile.endsWith(FileSplitStatic.SPLIT_FILE_SUFFIX)){
					throw new FileSplitException("["+currentFile+"]不是合法的拆包文件,后缀必须是["+FileSplitStatic.SPLIT_FILE_SUFFIX+"],不能组包...");
				}
				String[] splitpath = currentFile.split("\\_");
				currentFileSum = Integer.parseInt(splitpath[splitpath.length-4]);
				currentFileNum = Integer.parseInt(splitpath[splitpath.length-3]);
				currentTn = splitpath[splitpath.length-2];
				if(tn == null){//验证批次号
					tn = currentTn;
				}else{
					if(!tn.equals(currentTn)){//不是同一组文件不能组包
						throw new FileSplitException("批次号不相同,发现["+tn+"]和["+currentTn+"],不能组包...");
					}
				}
				if(fileSum==null){//验证总数
					fileSum = currentFileSum;
				}else{
					if(!fileSum.equals(currentFileSum)){//不是同一组文件不能组包
						throw new FileSplitException("文件总数不相同，发现["+fileSum+"]和["+currentFileSum+"],不能组包...");
					}
					if(currentFileNum>=fileSum){//超过了总数
						throw new FileSplitException("文件总数错误，需要["+fileSum+"]个，发现["+currentFileNum+"].不能组包...");
					}
				}
				int indexTn = currentFile.lastIndexOf(currentTn);
				
				currentRawFilePath = currentFile.substring(0, indexTn);
				int rawFileStartIndex = currentRawFilePath.lastIndexOf(fileSum+"")-1;
				currentRawFilePath = currentRawFilePath.substring(0, rawFileStartIndex);
				if(rawFilePath==null){//验证源文件名是否一样
					rawFilePath = currentRawFilePath;
				}else{
					if(!rawFilePath.equalsIgnoreCase(currentRawFilePath)){
						throw new FileSplitException("不是同一组文件不能组包...");
					}
				}
				count++;
			} catch (Exception e) {
				throw new FileSplitException("文件组包失败，文件["+currentFile+"]不是合法的已拆包文件..",e);
			}
			}
			if(count!=fileSum){
				throw new FileSplitException("文件组包失败，缺失文件,需要["+fileSum+"]个,实际["+count+"]个");
			}
			return rawFilePath;
	}

	@Override
	public List<String> getSplitFileListFromSplitFile(String splitFile)
			throws FileSplitException {
		try {
			if(!splitFile.endsWith(FileSplitStatic.SPLIT_FILE_SUFFIX)){
				throw new FileSplitException("["+splitFile+"]不是合法的拆包文件,后缀必须是["+FileSplitStatic.SPLIT_FILE_SUFFIX+"],不能组包...");
			}
			String[] splitpath = splitFile.split("\\_");
			Integer fileSum = Integer.parseInt(splitpath[splitpath.length-4]);
			String currentTn = splitpath[splitpath.length-2];
			List<String> list = new ArrayList<String>();
			if(fileSum!=0){
				int indexTn = splitFile.lastIndexOf(currentTn);
				String currentRawFilePath = splitFile.substring(0, indexTn);
				int rawFileStartIndex = currentRawFilePath.lastIndexOf(fileSum+"")-1;
				currentRawFilePath = currentRawFilePath.substring(0, rawFileStartIndex);
				for (int i = 0; i < fileSum; i++) {
					list.add(currentRawFilePath+"_"+fileSum+"_"+i+"_"+currentTn+"_"+FileSplitStatic.SPLIT_FILE_SUFFIX);
				}
			}else{
				throw new FileSplitException("["+splitFile+"]不是一个拆包文件...");
			}
			return list;
		}catch (Exception e) {
			throw new FileSplitException("["+splitFile+"]不是一个拆包文件...");
		}
		
	}
	
//	public static void main1(String[] args) throws Exception {
//		FileSplitService fileSplit = FileSplitServiceFactory.createFileSplitService(FILESPLIT_ALGORITHM.SIMPLE);
//    	long DEFAULT_SIZE=1024*1024*10;
//    	String srcFile = "C:\\Users\\Administrator\\Desktop\\test\\3.pdf";
//    	//String unFile = "C:\\Users\\Administrator\\Desktop\\test\\new.pdf";
//    	Date s = new Date();
//    	String md5src = Md5Util.encrypt(new File(srcFile));
//    	Packet packet = fileSplit.splitFile(new File(srcFile),"C:\\Users\\Administrator\\Desktop\\test\\tmp",DEFAULT_SIZE,true);
//        if(packet==null){
//        	System.out.println("没拆包..");
//        }else{
//    	    System.out.println("拆包时间:"+(new Date().getTime()-s.getTime())/1000+" s");
//    	    Date u = new Date();
//    	    List<String> splist = fileSplit.getSplitFileListFromSplitFile(packet.getPacketfile().get(0));
//    		System.out.println("需要组包的文件"+splist);
//    	    String newFile = fileSplit.unionFile(false,splist);
//    		System.out.println("组包时间:"+(new Date().getTime()-u.getTime())/1000+" s");
//    		System.out.println("md5是否一致："+md5src.equals(Md5Util.encrypt(new File(newFile))));
//        }
//	}
//	public static void main(String[] args) throws FileSplitException {
//		System.out.println(new SimpleFileSplit().getSplitFileListFromSplitFile("3.pdf_6_5_134123412_.split"));
//	}
	
	
}
