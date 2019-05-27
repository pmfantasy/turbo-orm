package com.fantasy.app.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
/**
 * xml<->bean
 * @author 公众号：18岁fantasy
 * @2014-11-28 @上午10:52:16
 */
public class XstreamUtil {
	private static XStream xstream = new XStream(new DomDriver("utf-8"));

	static {
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.autodetectAnnotations(true);
		xstream.ignoreUnknownElements();
	}

	/**
	 * 将Object转化成xml
	 *
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String toXml(Object fromObj) throws Exception {
		if (fromObj == null) {
			throw new IllegalArgumentException("obj cant not be null");
		}
		xstream.processAnnotations(fromObj.getClass());
		return xstream.toXML(fromObj);
	}
	/**
	 * 将xml转化为对象
	 * @param toObjectClass 目标对象
	 * @param fromXml
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toObject(Class<T> toObjectClass,String fromXml) throws Exception {
		if (StrUtil.isBlank(fromXml)) {
			throw new IllegalArgumentException("xml can not be null");
		}
		xstream.processAnnotations(toObjectClass);
		return (T)xstream.fromXML(fromXml,null);
	}

	/**
	 * 将xml文件转化为对象
	 * @param xmlfile
	 * @param toObjectClass目标对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toObject(Class<T> toObjectClass,File xmlfile) {
		xstream.processAnnotations(toObjectClass);
		return (T)xstream.fromXML(xmlfile);
	}

	/**
	 * 将对象转化为xml.并存储到xmlfile里面
	 * @param xmlfile
	 * @return
	 */
	public static void toXmlfile(Object fromObj, File xmlfile) throws IOException {
		OutputStream outputStream = new FileOutputStream(xmlfile);
		xstream.processAnnotations(fromObj.getClass());
		xstream.toXML(fromObj, outputStream);
		outputStream.flush();
		outputStream.close();
	}
	
	/**
	 * 将Object转化成xml，在不添加注解的时候去除包路径名
	 * @param obj    要转换的对象
	 * @param child  要去除包路径名的类
	 * @return
	 */
	public static String toShortXml(Object fromObj,Class<?>[] notAliasPackageClass) { 
		if (fromObj == null) {
			throw new IllegalArgumentException("obj cant not be null");
		}
		xstream.alias(fromObj.getClass().getSimpleName(), fromObj.getClass());
		xstream.processAnnotations(fromObj.getClass());
		for(int i=0;i < notAliasPackageClass.length; i++){
			Class<?> clazz =notAliasPackageClass[i];
			xstream.alias(clazz.getSimpleName(), clazz);
			xstream.processAnnotations(clazz);
		}
		return xstream.toXML(fromObj);
	} 
	
	/**
	 * ，在不添加注解的时，将没有包路径的xml转换为指定对象
	 * @param xml
	 * @param toClassNotAliasPackageClass 
	 * @return
	 */
	public static Object shortXmlToObj(String xml,Class<?>[] toClassNotAliasPackageClass) { 
		if (StrUtil.isBlank(xml)) {
			throw new IllegalArgumentException("xml can not be null");
		}
		for (int i = 0; i < toClassNotAliasPackageClass.length; i++) {
			Class<?> clazz = toClassNotAliasPackageClass[i];
			xstream.alias(clazz.getSimpleName(), clazz);
		}
		return xstream.fromXML(xml);
	} 
}
