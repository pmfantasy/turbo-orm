package com.fantasy.app.core.para;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.fantasy.app.core.spring.CommentedProperties;
import com.fantasy.app.core.util.PropertiesUtil;


/**
 * 参数获取
 * @author 公众号：18岁fantasy
 * 2017-5-11 下午4:12:33
 */
public class ParaGetTool {
  static  Logger logger = Logger.getLogger(ParaGetTool.class);
	static{
		try {
			loadPropertiesFiles();
		} catch (Exception e) {
			logger.error("加载配置文件失败",e);
		}
	}
	public static void loadPropertiesFiles() throws Exception {
		appProps = PropertiesUtil.loadProperties(APP_PROPERTIES);
		kernelProps = PropertiesUtil.loadProperties(KERNEL_PROPERTIES);
	}
	public static void reloadPropertiesFiles() throws Exception {
		appProps = kernelProps  = null;
		loadPropertiesFiles();
	}
	//用户配置文件
	public static final String APP_PROPERTIES = "app.properties";
	//内核配置文件 一般用户不会改，比如默认的参数
	private static final String KERNEL_PROPERTIES = "kernel.properties";
	//数据库
	private static CommentedProperties appProps = null;
	private static CommentedProperties kernelProps = null;
	
	static {
		try {
			if (null == appProps){
				appProps = PropertiesUtil.loadProperties(APP_PROPERTIES,  APP_PROPERTIES);
			}
			if (null == kernelProps){
				kernelProps = PropertiesUtil.loadProperties(KERNEL_PROPERTIES);
			}
		} catch (Exception e) {
			logger.error("参数初始化错误.", e);
		}
	}
	public static void setAppPara(String key, String value) {
		 appProps.setProperty(key, value);
	}
	public static void saveAppPara() {
		PropertiesUtil.save(appProps, APP_PROPERTIES);
	}
	/**
	* 获取所有的key
	* @param key
	* @return
	*/
	public static Set<String> getAllKey() {
	      return PropertiesUtil.getAllParaKey(appProps);
	}
    /**
    * 获取所有的key
    * @param key
    * @return
    */
    public static Set<String> getAllKey(String reg) {
      Set<String> keys = getAllKey();
      Set<String> key_match = new HashSet<>();
      if(keys!=null) {
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
          String key = iterator.next();
          if(key.matches(reg)) {
            key_match.add(key);
          }
        }
      }
      return key_match;
    }	
	/**
	* 获取string类型变量 调用方式SiEnv.getPara
	* @param key
	* @return
	*/
	public static String getPara(String key) {
		return PropertiesUtil.getPara(appProps, String.class, key);
	}
	/**
	* 获取string类型变量 调用方式SiEnv.getPara
	* @param key
	* @return
	*/
	public static String getPara(String key,String defaultV) {
		return PropertiesUtil.getPara(appProps, String.class, key,defaultV);
	}
	/**
	 * 获取不同的类型变量
	 * @param type
	 * @param key
	 * @return
	 */
	public static <T> T getPara(Class<T> type, String key) {
		return PropertiesUtil.getPara(appProps, type, key);
	}

	public static <T> T getPara(Class<T> type, String key, T defaultV) {
		return PropertiesUtil.getPara(appProps, type, key, defaultV);
	}
	/**
	* 获取string类型变量 调用方式SiEnv.getPara
	* @param key
	* @return
	*/
	public static String getKernelPara(String key) {
		return PropertiesUtil.getPara(kernelProps, String.class, key);
	}
	/**
	* 获取string类型变量 调用方式SiEnv.getPara
	* @param key
	* @return
	*/
	public static String getKernelPara(String key,String defaultV) {
		return PropertiesUtil.getPara(kernelProps, String.class, key,defaultV);
	}
	/**
	 * 获取不同的类型变量
	 * @param type
	 * @param key
	 * @return
	 */
	public static <T> T getKernelPara(Class<T> type, String key) {
		return PropertiesUtil.getPara(kernelProps, type, key);
	}

	public static <T> T getKernelPara(Class<T> type, String key, T defaultV) {
		return PropertiesUtil.getPara(kernelProps, type, key, defaultV);
	}
	/**
	 * 从指定的配置文件获取参数
	 * @param propertiesName
	 * @param key
	 * @return
	 */
	public static String getParaFromProperties(String propertiesName,String key){
		try {
			CommentedProperties properties = PropertiesUtil.loadProperties(propertiesName);
			return PropertiesUtil.getPara(properties, String.class, key);
		} catch (Exception e) {
			logger.error(e);
			return "";
		}
	}
	/**
	 * 从指定的配置文件获取参数
	 * @param propertiesName
	 * @param key
	 * @return
	 */
	public static String getParaFromProperties(Properties properties,String key,String defaultV){
		try {
			return PropertiesUtil.getPara(properties, String.class, key,defaultV);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}
	/**
	 * 从指定的配置文件获取参数
	 * @param propertiesName
	 * @param key
	 * @return
	 */
	public static <T> T getParaFromProperties(Properties properties,Class<T> type,String key,T defaultV){
		try {
			return PropertiesUtil.getPara(properties, type, key,defaultV);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}
}
