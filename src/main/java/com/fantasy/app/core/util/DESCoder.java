package com.fantasy.app.core.util;
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
@SuppressWarnings("restriction")
public abstract class DESCoder{   
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
    public static final String ALGORITHM = "DES";   
  
    /**  
     * 转换密钥<br>  
     *   
     * @param key  
     * @return  
     * @throws Exception  
     */  
    private static Key toKey(byte[] key) throws Exception {   
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
    private static Key toKey(String seed) throws Exception {   
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
    public static byte[] decrypt(byte[] data, String key) throws Exception {   
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
    public static byte[] encrypt(byte[] data, String key) throws Exception {   
        Key k = toKey(decryptBASE64(key));   
        Cipher cipher = Cipher.getInstance(ALGORITHM);   
        cipher.init(Cipher.ENCRYPT_MODE, k);   
  
        return cipher.doFinal(data);   
    }  
    /**  
     * 解密  
     *   
     * @param data  
     * @param key  
     * @return  
     * @throws Exception  
     */  
    public static String decryptString(String seed, String content) throws Exception {   
    	String key = initKey(seed);
		byte[] outputData = DESCoder.decrypt(DESCoder.decryptBASE64(content), key);   
	    String outputStr = new String(outputData,"utf-8");   
		return outputStr;
    }   
    /**  
     * 加密  
     *   
     * @param data  
     * @param key  
     * @return  
     * @throws Exception  
     */  
    public static String encryptString(String seed, String content) throws Exception {   
    	String key = initKey(seed);
		byte[] inputData = content.getBytes("UTF-8"); 
	    inputData = DESCoder.encrypt(inputData, key);
	    String outputStr = DESCoder.encryptBASE64(inputData);
        return outputStr;   
    }  
    /**
     * 文件 file 进行加密并保存目标文件 destFile 中
     *
     * @param file
     *             要加密的文件 如 c:/test/srcFile.txt
     * @param destFile
     *             加密后存放的文件名 如 c:/ 加密后文件 .txt
     */
   public static void encryptFile(String file, String destFile,String seed) throws Exception {
      Cipher cipher = Cipher.getInstance ( "DES" );
      cipher.init(Cipher.ENCRYPT_MODE ,toKey(seed) );
      InputStream is = new FileInputStream(file);
      OutputStream out = new FileOutputStream(destFile);
      CipherInputStream cis = new CipherInputStream(is, cipher);
      byte [] buffer = new byte [1024];
      int r;
      while ((r = cis.read(buffer)) > 0) {
          out.write(buffer, 0, r);
      }
      cis.close();
      is.close();
      out.close();
   }

   /**
     * 文件采用 DES 算法解密文件
     *
     * @param file
     *             已加密的文件 如 c:/ 加密后文件 .txt *
     * @param destFile
     *             解密后存放的文件名 如 c:/ test/ 解密后文件 .txt
     */
   public static void decryptFile(String file, String dest,String seed) throws Exception {
      Cipher cipher = Cipher.getInstance ( "DES" );
      cipher.init(Cipher.DECRYPT_MODE , toKey(seed));
      InputStream is = new FileInputStream(file);
      OutputStream out = new FileOutputStream(dest);
      CipherOutputStream cos = new CipherOutputStream(out, cipher);
      byte [] buffer = new byte [1024];
      int r;
      while ((r = is.read(buffer)) >= 0) {
          cos.write(buffer, 0, r);
      }
      cos.close();
      out.close();
      is.close();
   }
    /**  
     * 生成密钥  
     *   
     * @return  
     * @throws Exception  
     */  
    public static String initKey() throws Exception {   
        return initKey(null);   
    }   
    /**  
     * BASE64解密  
     *   
     * @param key  
     * @return  
     * @throws Exception  
     */  
    public static byte[] decryptBASE64(String key) throws Exception {   
        return (new BASE64Decoder()).decodeBuffer(key);   
    }   
      
    /**  
     * BASE64加密  
     *   
     * @param key  
     * @return  
     * @throws Exception  
     */  
    public static String encryptBASE64(byte[] key) throws Exception {   
        return (new BASE64Encoder()).encodeBuffer(key);   
    }   
    /**  
     * 生成密钥  
     *   
     * @param seed  
     * @return  
     * @throws Exception  
     */  
    public static String initKey(String seed) throws Exception {   
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
//    	String inputStr = "公众号：18岁fantasy";   
//    	DESCoder.initKey();  
//        String key = "gDurgxoOl3k=";//DESCoder.initKey();   
//        System.err.println("原文:\t" + inputStr);   
//  
//        System.err.println("密钥:\t" + key);   
//  
//        byte[] inputData = inputStr.getBytes(); 
//        inputData = DESCoder.encrypt(inputData, key);
//        String after = DESCoder.encryptBASE64(inputData);
//       System.err.println("加密后:\t" + DESCoder.encryptBASE64(inputData));   
//      //String inputDataa = "8qO1ziROVRdgU6KN5HdUcaEd4vG6kHkd8qb409ycvXe+PLFbV1i4wg==";
//        byte[] outputData = DESCoder.decrypt(decryptBASE64(after), key);   
//        String outputStr = new String(outputData);   
//  
//        System.err.println("解密后:\t" + outputStr);   
//	}
//    public static void main(String[] args) throws Exception {
//    	String seed = "公众号：18岁fantasybing";
//    	String file = "E:\\book\\e.pdf";
//    	String desFile = "C:\\Users\\Administrator\\Desktop\\2.pdf";
//    	String decodeFile = "C:\\Users\\Administrator\\Desktop\\3.pdf";
//    	
//    	String md51 = Md5Util.encrypt(new File(file));
//    	System.out.println("加密前md5："+md51);
//    	System.out.println("加密前大小："+new File(file).length()/1024+" K");
//    	Date etime = new Date();
//    	encryptFile(file, desFile, seed);
//    	System.out.println("加密时间:"+(new Date().getTime()-etime.getTime())/1000+" s");
//    	System.out.println("加密后大小："+new File(desFile).length()/1024+" K");
//    	Date dtime = new Date();
//    	decryptFile(desFile, decodeFile, seed);
//    	System.out.println("解密时间:"+(new Date().getTime()-dtime.getTime())/1000+" s");
//    	System.out.println("解密后大小："+new File(decodeFile).length()/1024+" K");
//    	String md53 = Md5Util.encrypt(new File(decodeFile));
//    	System.out.println("解密后md5："+md53);
//    	System.out.println(md51.equals(md53));
//    	
//	}
//    public static void main2(String[] args) throws Exception {
//    	String seed = "123";
//    	String keyb = null;
//		for (int i = 0; i < 100; i++) {
//			Thread.sleep(1000l);
//			String kn = DESCoder.initKey();
//			System.out.println(kn);
//			if(keyb!=null){
//				System.out.println(keyb.equals(kn));
//			}else{
//				keyb = kn;
//			}
//		}
//	}
}  

