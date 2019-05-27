package com.fantasy.app.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * 文件操作类
 * 
 * @author 公众号：18岁fantasy
 * 2018年1月26日16:35:36
 */
public class FileUtil extends FileUtils {

	public static String getFileSuffix(File file) {
		String fileName = file.getName();
		if (fileName.indexOf(".") != -1) {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		} else {
			return "";
		}
	}
	// public static void main(String[] args) {
	// File f = new File("C:\\Users\\Administrator\\Desktop\\1.s1.s");
	// System.out.println(FileUtil.getFileSuffix(f));
	// }

	/***
	 * 复制单个文件*
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt*
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt*@return boolean
	 */

	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public static void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}

	}
	
	
	/**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName
     *            要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir
     *            要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }
    
    
    public static List<String> getAllFileNames(File filePath,List<String> fileNames){
         File[] files = filePath.listFiles();
         if(files == null){
             return fileNames;    
         }    
         for(File f:files){
             if(f.isDirectory()){
            	 fileNames.add(f.getName());
                 getAllFileNames(f,fileNames);
             }else{
                // filePaths.add(f.getPath());
             }    
         }
         return fileNames;
    }
    
    public static List<File> getAllFileDirs(File filePath,List<File> fileDirs){
        File[] files = filePath.listFiles();
        if(files == null){
            return fileDirs;    
        }    
        for(File f:files){
            if(f.isDirectory()){
            	fileDirs.add(f);
           	    getAllFileDirs(f,fileDirs);
            }else{
               // filePaths.add(f.getPath());
            }    
        }
        return fileDirs;
   }

    public static void main(String[] args) {
	//  // 删除单个文件
	//  String file = "c:/test/test.txt";
	//  DeleteFileUtil.deleteFile(file);
	//  System.out.println();
	        // 删除一个目录
	        String dir = "E:\\zlworkdir";
	       // deleteDirectory(dir);
	//  System.out.println();
	//  // 删除文件
	//  dir = "c:/test/test0";
	//  DeleteFileUtil.delete(dir);
	        List<String> paths = new ArrayList<String>();
	                 paths = getAllFileNames(new File("E:\\\\zlworkdir"),paths);
	                 for(String path:paths){
	                   System.out.println(path);    
	                 }
    }
	
}
