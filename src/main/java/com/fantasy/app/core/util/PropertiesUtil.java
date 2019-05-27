package com.fantasy.app.core.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.Set;

import com.fantasy.app.core.spring.CommentedProperties;


/**
 * 参数文件操作类
 * @日期：2012-12-15上午11:22:56
 * @作者：公众号：18岁fantasy
 */
public class PropertiesUtil {

	public static <T> T getPara(Properties properties, Class<T> type, String key, T defaultV) {
		String value = properties.getProperty(key, String.valueOf(defaultV));
		return parseValue(type, value);
	}

	
	public static <T> T getPara(CommentedProperties properties, Class<T> type, String key, T defaultV) {
		String value = properties.getProperty(key, String.valueOf(defaultV));
		return parseValue(type, value);
	}
	/**
	 * 获取所有的参数key
	 * @param properties
	 * @return
	 */
	public static Set<String> getAllParaKey(CommentedProperties properties) {
      return properties.propertyNames();
    }
	public static <T> T getPara(CommentedProperties properties, Class<T> type, String key) {
		String value = properties.getProperty(key);
		return parseValue(type, value);
	}
	public static void setPara(CommentedProperties properties, String key, String value) {
		properties.setProperty(key, value);
	}
	public static void save(CommentedProperties properties,String propertyFileName) {
		OutputStream outputStream = null;
		try {
			String filePath = PropertiesUtil.class.getClassLoader().getResource(propertyFileName).getFile();
			outputStream = new FileOutputStream(filePath);
			properties.store(outputStream);
		} catch (IOException e) {
		}finally {
			try {
				outputStream.close();
			} catch (IOException e) {
			}
		}
	}
	public static String getFormatPara(CommentedProperties properties, String key, Object[] args) {
		String value = properties.getProperty(key);
		return MessageFormat.format(value, args);
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
			return "";
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T parseValue(Class<T> type, String value) {
		if (Integer.class == type || int.class == type) {
			return (T) Integer.valueOf(value);
		}
		if (Double.class == type || double.class == type) {
			return (T) Double.valueOf(value);
		}
		if (Long.class == type || long.class == type) {
			return (T) Long.valueOf(value);
		}
		if (Boolean.class == type || boolean.class == type) {
			return (T) Boolean.valueOf(value);
		}
		return (T) value;
	}

	public static CommentedProperties loadProperties(String... propertiesList) throws Exception {
		CommentedProperties prop = new CommentedProperties();
		for( String properties : propertiesList ) {
			InputStream propertiesStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(properties);
			if( propertiesStream == null ) {
				System.err.println( "配置文件：" + properties + "不存在！");
				continue;
			}
			
			try {
				prop.load(propertiesStream);
			} finally {
				propertiesStream.close();
			}
		}
		return prop;
	}
}
