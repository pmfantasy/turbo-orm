package com.fantasy.app.core.component.encrypt;

import java.io.File;

import com.fantasy.app.core.exception.EncryptException;

/**
 * 加密服务
 * @author 公众号：18岁fantasy
 * @2015-2-6 @上午10:28:08
 */
public interface EncryptService {
	
	/**
	 * 生成秘钥
	 * @param seed
	 * @return
	 */
	public String randomSeed(String seed);
	
	public String getSeed(String seed);

	 /**  
     * 加密  字符串
     *   
     * @param seed  
     * @param content   明文
     * @return  密文
     * @throws Exception  
     */  
    public  String encryptString(String seed, String content) throws EncryptException;
    
    /**  
     * 加密  字符串
     *   
     * @param seed 秘钥字符串 
     * @param content   明文
    *  @param  encryptFilter 字符串滤器
     * @return  加密结果
     * @throws Exception  
     */  
    public  EncryptResult<String> encryptString(String seed, String content,EncryptFilter<String> encryptFilter) throws EncryptException ;
    /**
     * 加密文件
     * 文件 file 进行加密并保存目标文件 destFile 中
     * @param seed 加密秘钥随机串
     * @param file
     *             要加密的文件 如 c:/test/srcFile.txt
     * @param destFile
     *             加密后存放的文件名 如 c:/ 加密后文件 .txt
     * 
     * @param  encryptFilter 文件过滤器
     */
   public  EncryptResult<String> encryptFile(String seed,String filePath, String destFile,EncryptFilter<File> encryptFilter,boolean deleteSrcFile) throws EncryptException ;
   /**
    * 加密文件，删除原文件，返回和源文件路径相同的密文文件
    * 文件 file 进行加密并保存目标文件 destFile 中
    * @param seed 加密秘钥随机串
    * @param file
    *             要加密的文件 如 c:/test/srcFile.txt
    * @param destFile
    *             加密后存放的文件名 如 c:/ 加密后文件 .txt
    * 
    * @param  encryptFilter 文件过滤器
    */
  public  EncryptResult<String> encryptFileUseRawFilePath(String seed,String filePath,EncryptFilter<File> encryptFilter) throws EncryptException ;
  /**
   * 加密文件，删除原文件，返回带固定后缀的新文件
   * 文件 file 进行加密并保存目标文件 destFile 中
   * @param seed 加密秘钥随机串
   * @param file
   *             要加密的文件 如 c:/test/srcFile.txt
   * @param destFile
   *             加密后存放的文件名 如 c:/ 加密后文件 .txt
   * 
   * @param  encryptFilter 文件过滤器
   */
 public  EncryptResult<String> encryptFile(String seed,String filePath,EncryptFilter<File> encryptFilter,boolean deleteSrcFile) throws EncryptException ;
   /**
    * 文件 file 进行加密并保存目标文件 destFile 中
    *
    * @param file
    *             要加密的文件 如 c:/test/srcFile.txt
    * @param destFile
    *             加密后存放的文件名 如 c:/ 加密后文件 .txt
    */
   public  void encryptFile(String seed,String file, String destFile,boolean deleteSrcFile) throws EncryptException ;
   /**  
    * 解密  字符串
    *   
    * @param seed 秘钥  
    * @param content   密文
    * @return  明文
    * @throws Exception  
    */  
   public  String decryptString(String seed, String content) throws EncryptException ;
   /**
    * 文件采用 DES 算法解密文件
    *
    * @param file
    *             已加密的文件 如 c:/ 加密后文件 .txt *
    * @param destFile
    *             解密后存放的文件名 如 c:/ test/ 解密后文件 .txt
    */
  public  String decryptFile(String seed,String file, String dest,boolean deleteSrcFile) throws EncryptException ;
  /**
   * 文件采用 DES 算法解密文件
   *
   * @param file
   *             已加密的文件 如 c:/ 加密后文件 .txt *
   * @param destFile
   *             解密后存放的文件名 如 c:/ test/ 解密后文件 .txt
   */
  public  String decryptFile(String seed,String enfile,boolean deleteSrcFile) throws EncryptException ;
}
