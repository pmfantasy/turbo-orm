package com.fantasy.app.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * md5 util
 * @author 公众号：18岁fantasy
 * @2014-11-22 @下午6:14:33
 */
public class Md5Util {

	/**
     * Hashes the password using MD5
     */
    public static String encrypt(String str) {
        return encryptMD5(str);
    }
    /**
     * Hashes the password using MD5
     */
    public static String encrypt(File file) {
        return encryptMD5(file);
    }

    /**
     * {@inheritDoc}
     */
    public static boolean matches(String passwordToCheck, String storedPassword) {
        if(storedPassword == null) {
            throw new NullPointerException("storedPassword can not be null");
        }
        if(passwordToCheck == null) {
            throw new NullPointerException("passwordToCheck can not be null");
        }
        
        return encrypt(passwordToCheck).equalsIgnoreCase(storedPassword);
    }
    /**
     * Encrypt string using MD5 algorithm
     */
    public final static String encryptMD5(String source) {
        if (source == null) {
            source = "";
        }

        String result = "";
        try {
            result = encrypt(source, "MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }
    /**
     * Encrypt string using MD5 algorithm
     */
    public final static String encryptMD5(File source) {
    	try {
    		 byte[] resByteArray = encrypt(source, "MD5");
             return toHexString(resByteArray);
		} catch (Exception e) {
			 throw new RuntimeException(e);
		}
    }

    /**
     * Get hex string from byte array
     */
    public final static String toHexString(byte[] res) {
        StringBuilder sb = new StringBuilder(res.length << 1);
        for (int i = 0; i < res.length; i++) {
            String digit = Integer.toHexString(0xFF & res[i]);
            if (digit.length() == 1) {
                sb.append('0');
            }
            sb.append(digit);
        }
        return sb.toString().toUpperCase();
    }
    /**
     * Encrypt string
     */
    public final static String encrypt(String source, String algorithm)
            throws NoSuchAlgorithmException {
        byte[] resByteArray = encrypt(source.getBytes(), algorithm);
        return toHexString(resByteArray);
    }
    /**
     * Encrypt byte array.
     */
    public final static byte[] encrypt(byte[] source, String algorithm)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.reset();
        md.update(source);
        return md.digest();
    }
    /**
     * Encrypt file
     */
    public final static byte[] encrypt(File file, String algorithm)
            throws NoSuchAlgorithmException {
	  MessageDigest md = MessageDigest.getInstance(algorithm);
	  FileInputStream fis = null;
      try {
          fis = new FileInputStream(file);
          byte[] buffer = new byte[102400];
          int length;
          while ((length = fis.read(buffer)) != -1) {
              md.update(buffer, 0, length);
          }
          return md.digest();
      } catch (FileNotFoundException e) {
          return null;
      } catch (IOException e) {
          return null;
      } finally {
          try {
              if (fis != null) fis.close();
          } catch (IOException e) {
        	  return null;
          }
      }
    }
}
