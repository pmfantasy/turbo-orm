package com.fantasy.app.core.component.encrypt;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.fantasy.app.core.component.encrypt.EncryptStatic.ENCRYPT_ALGORITHM;
import com.fantasy.app.core.exception.EncryptException;
import com.fantasy.app.core.para.CorePara.StaticPara;
import com.fantasy.app.core.util.StrUtil;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
  
/**  
 * DES安全编码组件  
 *   
 * <pre>  
 * 支持 DES、DESede(TripleDES,就是3DES)、AES、Blowfish、RC2、RC4(ARCFOUR)  
 * DES                  key size must be equal to 56  
 * DESede(TripleDES)    key size must be equal to 112 or 168  
 * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available  
 * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive)  
 * RC2                  key size must be between 40 and 1024 bits  
 * RC4(ARCFOUR)         key size must be between 40 and 1024 bits  
 * 具体内容 需要关注 JDK Document http://.../docs/technotes/guides/security/SunProviders.html  
 * </pre> 
 * @日期：2013-2-23下午4:18:50
 * @作者：java加密与解密的艺术 
 * @版本：1.0
 */
public  class DesEncrypt implements EncryptService{ 
	public String randomSeed(String seed) {
			return seed;
	}
	
	public String getSeed(String seed) {
		return seed;
}
	
	
	
    /**  
     * ALGORITHM 算法 <br>  
     * 可替换为以下任意一种算法，同时key值的size相应改变。  
     *   
     * <pre>  
     * DES                  key size must be equal to 56  
     * DESede(TripleDES)    key size must be equal to 112 or 168  
     * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available  
     * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive)  
     * RC2                  key size must be between 40 and 1024 bits  
     * RC4(ARCFOUR)         key size must be between 40 and 1024 bits  
     * </pre>  
     *   
     * 在Key toKey(byte[] key)方法中使用下述代码  
     * <code>SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);</code> 替换  
     * <code>  
     * DESKeySpec dks = new DESKeySpec(key);  
     * SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);  
     * SecretKey secretKey = keyFactory.generateSecret(dks);  
     * </code>  
     */  
    public static final String ALGORITHM = ENCRYPT_ALGORITHM.DES.name();   
  
    /**  
     * 转换密钥<br>  
     *   
     * @param key  
     * @return  
     * @throws Exception  
     */  
    private  Key toKey(byte[] key) throws Exception {   
        DESKeySpec dks = new DESKeySpec(key);   
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);   
        SecretKey secretKey = keyFactory.generateSecret(dks);   
  
        // 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码   
        // SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);   
  
        return secretKey;   
    }
    /**  
     * 转换密钥<br>  
     *   
     * @param seed  
     * @return  
     * @throws Exception  
     */  
    private  Key toKey(String seed) throws Exception {   
    	String key = initKey(seed);
    	Key k = toKey(decryptBASE64(key));   
        return k;   
    }  
  
    /**  
     * 解密  
     *   
     * @param data  
     * @param key  
     * @return  
     * @throws Exception  
     */  
    private  byte[] decrypt(byte[] data, String key) throws Exception {   
        Key k = toKey(decryptBASE64(key));   
  
        Cipher cipher = Cipher.getInstance(ALGORITHM);   
        cipher.init(Cipher.DECRYPT_MODE, k);   
  
        return cipher.doFinal(data);   
    }   
  
    /**  
     * 加密  
     *   
     * @param data  
     * @param key  
     * @return  
     * @throws Exception  
     */  
    private  byte[] encrypt(byte[] data, String key) throws Exception {   
        Key k = toKey(decryptBASE64(key));   
        Cipher cipher = Cipher.getInstance(ALGORITHM);   
        cipher.init(Cipher.ENCRYPT_MODE, k);   
  
        return cipher.doFinal(data);   
    }  
    /**  
     * 解密  字符串
     *   
     * @param seed 秘钥  
     * @param content   密文
     * @return  明文
     * @throws Exception  
     */  
    public  String decryptString(String seed, String content) throws EncryptException {   
    	try {
    		String key = initKey(seed);
    		byte[] outputData = decrypt(DesEncrypt.decryptBASE64(content), key);   
    	    String outputStr = new String(outputData,EncryptStatic.ENCODING); 
    	    return outputStr;
		} catch (Exception e) {
			throw new EncryptException("加密字符串出错",e);
		}
    }   
    /**  
     * 加密  字符串
     *   
     * @param seed 秘钥字符串 
     * @param content   明文
     * @return  密文
     * @throws Exception  
     */  
    public  String encryptString(String seed, String content) throws EncryptException {   
    	EncryptResult<String> encryptResult = encryptString(seed, content, null);
    	return encryptResult.getEncryptResultObj(); 
    }
    /**  
     * 加密  字符串
     *   
     * @param seed 秘钥字符串 
     * @param content   明文
    *  @param  encryptFilter 字符串滤器
     * @return  加密结果
     * @throws Exception  
     */  
    public  EncryptResult<String> encryptString(String seed, String content,EncryptFilter<String> encryptFilter) throws EncryptException { 
    	
    	if(StrUtil.isBlank(content)){
    		return new EncryptResult<String>(false,content);
    	}
    	if(encryptFilter!=null&&encryptFilter.exclude(content)){
    		return new EncryptResult<String>(false,content);
    	}
    	try {
    		String key = initKey(seed);
    		byte[] inputData = content.getBytes(EncryptStatic.ENCODING); 
    	    inputData = encrypt(inputData, key);
    	    String outputStr = encryptBASE64(inputData);
            return new EncryptResult<String>(true,outputStr);
		} catch (Exception e) {
			throw new EncryptException("加密字符串出错",e);
		}
    	
    } 
   
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
	  public  EncryptResult<String> encryptFile(String seed,String filePath, String destFile,EncryptFilter<File> encryptFilter,boolean deleteSrcFile) throws EncryptException {
	     File file = new File(filePath);
	     if(!file.exists()){
	    	return new EncryptResult<String>(false,filePath);
	     }
	     if(encryptFilter!=null&&encryptFilter.exclude(file)){
	 		return new EncryptResult<String>(false,filePath);
	 	 }
	     try {
	    	 String tempFile = destFile+StaticPara.DEFAULT_FILE_TMP_SUFIX;
	    	 Cipher cipher = Cipher.getInstance (ALGORITHM);
	         cipher.init(Cipher.ENCRYPT_MODE ,toKey(seed) );
	         InputStream is = new FileInputStream(file);
	         OutputStream out = new FileOutputStream(tempFile);
	         CipherInputStream cis = new CipherInputStream(is, cipher);
	         byte [] buffer = new byte [1024];
	         int r;
	         while ((r = cis.read(buffer)) > 0) {
	             out.write(buffer, 0, r);
	         }
	         cis.close();
	         is.close();
	         out.close();
	         if(deleteSrcFile){
	        	 file.delete(); 
	         }
	         new File(tempFile).renameTo(new File(destFile));
	         return new EncryptResult<String>(true,destFile);
			} catch (Exception e) {
				throw new EncryptException("加密文件出错",e);
			}
		 
	  }
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
	 public  EncryptResult<String> encryptFileUseRawFilePath(String seed,String filePath,EncryptFilter<File> encryptFilter) throws EncryptException{
		 return encryptFile(seed, filePath, filePath, encryptFilter, true);
	 }
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
	public  EncryptResult<String> encryptFile(String seed,String filePath,EncryptFilter<File> encryptFilter,boolean deleteSrcFile) throws EncryptException{
	     String newFilePath = filePath+EncryptStatic.ENCRYPT_FILE_SUFIX;
	     return encryptFile(seed, filePath, newFilePath, encryptFilter, deleteSrcFile);
	}
	   /**
	    * 文件 file 进行加密并保存目标文件 destFile 中
	    *
	    * @param file
	    *             要加密的文件 如 c:/test/srcFile.txt
	    * @param destFile
	    *             加密后存放的文件名 如 c:/ 加密后文件 .txt
	    */
	  public  void encryptFile(String seed,String filePath, String destFile,boolean deleteSrcFile) throws EncryptException {
		 encryptFile(seed, filePath,destFile, null, deleteSrcFile);
	  }

   /**
     * 文件采用 DES 算法解密文件
     *
     * @param file
     *             已加密的文件 如 c:/ 加密后文件 .txt *
     * @param destFile
     *             解密后存放的文件名 如 c:/ test/ 解密后文件 .txt
     */
   public  String  decryptFile(String seed,String enfile, String dest,boolean deleteSrcFile) throws EncryptException {
	   try {
		  File file = new File(enfile);
		  File tempFile = new File(enfile+StaticPara.DEFAULT_FILE_TMP_SUFIX);
	   	  Cipher cipher = Cipher.getInstance (ALGORITHM);
	      cipher.init(Cipher.DECRYPT_MODE , toKey(seed));
	      InputStream is = new FileInputStream(file);
	      OutputStream out = new FileOutputStream(tempFile);
	      CipherOutputStream cos = new CipherOutputStream(out, cipher);
	      byte [] buffer = new byte [1024];
	      int r;
	      while ((r = is.read(buffer)) >= 0) {
	          cos.write(buffer, 0, r);
	      }
	      cos.close();
	      out.close();
	      is.close();
	      if(deleteSrcFile){
	    	  file.delete();
	      }
	      tempFile.renameTo(new File(dest));
	      return dest;
		} catch (Exception e) {
			throw new EncryptException("解密文件出错",e);
		}
   }
   /**
    * 文件采用 DES 算法解密文件
    *
    * @param file
    *             已加密的文件 如 c:/ 加密后文件 .txt *
    * @param destFile
    *             解密后存放的文件名 如 c:/ test/ 解密后文件 .txt
    */
  public  String decryptFile(String seed,String enfile,boolean deleteSrcFile) throws EncryptException {
		  String destFilename = enfile.replace(EncryptStatic.ENCRYPT_FILE_SUFIX, "");
		  return decryptFile(seed, enfile, destFilename, deleteSrcFile);
  }
    /**  
     * 生成密钥  
     *   
     * @return  
     * @throws Exception  
     */  
    public  String initKey() throws Exception {   
        return initKey(null);   
    }   
    /**  
     * BASE64解密  
     *   
     * @param key  
     * @return  
     * @throws Exception  
     */  
	private static byte[] decryptBASE64(String key) throws Exception {   
        return (new BASE64Decoder()).decodeBuffer(key);   
    }   
      
    /**  
     * BASE64加密  
     *   
     * @param key  
     * @return  
     * @throws Exception  
     */  
	private  String encryptBASE64(byte[] key) throws Exception {   
        return (new BASE64Encoder()).encodeBuffer(key);   
    }   
    /**  
     * 生成密钥  
     *   
     * @param seed  
     * @return  
     * @throws Exception  
     */  
    private  String initKey(String seed) throws Exception {   
        SecureRandom secureRandom = null;   
  
        if (seed != null) {   
            secureRandom = new SecureRandom(decryptBASE64(seed));   
        } else {   
            secureRandom = new SecureRandom();   
        }   
  
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);   
        kg.init(secureRandom);   
  
        SecretKey secretKey = kg.generateKey();   
  
        return encryptBASE64(secretKey.getEncoded());   
    } 
    public String convertBase64(String base64Str){
    	return null;
    }
//    public static void main3(String[] args) throws Exception {
//    	DesEncrypt desEncrypt = new DesEncrypt();
//    	String inputStr = "公众号：18岁fantasy";   
//    	desEncrypt.initKey();  
//        String key = "gDurgxoOl3k=";//DESCoder.initKey();   
//        System.err.println("原文:\t" + inputStr);   
  
//        System.err.println("密钥:\t" + key);   
  
//        byte[] inputData = inputStr.getBytes(); 
//        inputData = desEncrypt.encrypt(inputData, key);
//        String after = desEncrypt.encryptBASE64(inputData);
//       System.err.println("加密后:\t" + desEncrypt.encryptBASE64(inputData));   
      //String inputDataa = "8qO1ziROVRdgU6KN5HdUcaEd4vG6kHkd8qb409ycvXe+PLFbV1i4wg==";
//        byte[] outputData = desEncrypt.decrypt(decryptBASE64(after), key);   
//        String outputStr = new String(outputData);   
  
//        System.err.println("解密后:\t" + outputStr);   
//	}
//    public static void main(String[] args) throws Exception {
//    	DesEncrypt desEncrypt = new DesEncrypt();
//    	String seed = "公众号：18岁fantasybing";
//    	String file = "E:\\book\\e.pdf";
//    	String desFile = "C:\\Users\\Administrator\\Desktop\\2.pdf";
//    	String decodeFile = "C:\\Users\\Administrator\\Desktop\\3.pdf";
//    	
//    	String md51 = Md5Util.encrypt(new File(file));
//    	System.out.println("加密前md5："+md51);
//    	System.out.println("加密前大小："+new File(file).length()/1024+" K");
//    	Date etime = new Date();
//    	desEncrypt.encryptFile(seed,file, desFile, false);
//    	System.out.println("加密时间:"+(new Date().getTime()-etime.getTime())/1000+" s");
//    	System.out.println("加密后大小："+new File(desFile).length()/1024+" K");
//    	Date dtime = new Date();
//    	desEncrypt.decryptFile(seed,desFile, decodeFile,false);
//    	System.out.println("解密时间:"+(new Date().getTime()-dtime.getTime())/1000+" s");
//    	System.out.println("解密后大小："+new File(decodeFile).length()/1024+" K");
//    	String md53 = Md5Util.encrypt(new File(decodeFile));
//    	System.out.println("解密后md5："+md53);
//    	System.out.println(md51.equals(md53));
    	
//	}
}  

