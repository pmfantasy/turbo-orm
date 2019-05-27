package com.fantasy.app.core.tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.component.log.Log;
import com.fantasy.app.core.exception.InitException;
import com.fantasy.app.core.util.ReflectionUtil;
import com.fantasy.app.core.util.StrUtil;


/**
 * 码表注册中心。
 * 注册后可使用tag来输出select下拉列表信息
 * @author 公众号：18岁fantasy
 * @2015-7-30 @下午3:53:54
 */
public class CT {

	/**
	 * 结构：<codeType,<code,<fieldName,FieldValue>>>：
	 * 其中：codeType可以为表的名称或者枚举变量类的名称
	 */
	private static ConcurrentHashMap<String, Map<String, Map<String, String>>> codeTablePool = new ConcurrentHashMap<String, Map<String, Map<String, String>>>();
	private static Logger logger = Log.getLogger(LogType.SYSINNER);
	
	public static final String CT_DEFAULT_CODE_KEY = "code";
	public static final String CT_DEFAULT_NAME_KEY = "name";
	
	public static void registCodeTableCodeName(String codeType,Map<String, String> codeTable){
		if(codeTable==null||codeTable.isEmpty())return;
		Map<String, Map<String, String>> codeAndFieldsTable = null;
		if((codeAndFieldsTable=codeTablePool.get(codeType.toLowerCase()))==null){
			codeAndFieldsTable =  new HashMap<String,Map<String, String>>();
			codeTablePool.put(codeType.toLowerCase(),codeAndFieldsTable);
		}else{
			logger.warn("码表注册：发现码表注册覆盖-->"+codeType);
		}
		for (Iterator<String> iterator = codeTable.keySet().iterator(); iterator.hasNext();) {
			String code = iterator.next();
			Map<String,String> fields = new HashMap<String, String>();
			fields.put(CT_DEFAULT_NAME_KEY, codeTable.get(code));
			codeAndFieldsTable.put(code, fields);
		}
	}
	/**
	 * 注册
	 * @param codeTableToAdd
	 */
	public static void registCodeTable(Map<String,Map<String, Map<String, String>>> codeTableToAdd){
		if(codeTableToAdd==null||codeTableToAdd.isEmpty())return;
		codeTablePool.putAll(codeTableToAdd);
	}
	/**
	 * 
	 * @param codeType 码表类型
	 * @param codeAndFieldsTableToAdd 码表信息<codeType,<code,<fieldName,FieldValue>>>
	 */
	public static void registCodeTableCodeAndFields(String codeType,Map<String, Map<String, String>> codeAndFieldsTableToAdd){
		if(codeAndFieldsTableToAdd==null||codeAndFieldsTableToAdd.isEmpty())return;
		Map<String, Map<String, String>> codeAndFieldsTable = null;
		if((codeAndFieldsTable=codeTablePool.get(codeType.toLowerCase()))==null){
			codeAndFieldsTable =  new HashMap<String,Map<String, String>>();
			codeTablePool.put(codeType.toLowerCase(),codeAndFieldsTable);
		}else{
			logger.warn("码表注册：发现码表注册覆盖-->"+codeType);
		}
		codeAndFieldsTable.putAll(codeAndFieldsTableToAdd);
	}
	public static void registCodeTableCodeAndFields(String codeType,String code, Map<String, String> fieldsToAdd){
		if(fieldsToAdd==null||fieldsToAdd.isEmpty())return;
		Map<String, Map<String, String>> codeAndFieldsTable = null;
		if((codeAndFieldsTable=codeTablePool.get(codeType.toLowerCase()))==null){
			codeAndFieldsTable =  new HashMap<String,Map<String, String>>();
			codeTablePool.put(codeType.toLowerCase(),codeAndFieldsTable);
		}else{
			logger.warn("码表注册：发现码表注册覆盖-->"+codeType);
		}
		Map<String, String> fields = new HashMap<String, String>();
		if(codeAndFieldsTable.get(code)==null){
			fields =  new HashMap<String,String>();
			codeAndFieldsTable.put(code,fields);
		}
		fields.putAll(fieldsToAdd);
	}
	public static void registCodeTable(String codeType,String code,String name){
		Map<String,String> ct = new HashMap<String, String>();
		ct.put(code.trim(), name);
		registCodeTableCodeAndFields(codeType, code,ct);
	}
	/**
	 * 获取码表
	 * @param codeType
	 * @return
	 */
	public static Map<String, Map<String, String>> getCodeTable(String codeType){
		return codeTablePool.get(codeType.toLowerCase());
	}
	/**
	 * 根据码表名称获取码表
	 * @param codeType
	 * @param code
	 * @return
	 */
	public static String getCodeTableNameByCode(String codeType,String code){
		Map<String, Map<String, String>> codeTable = codeTablePool.get(codeType.toLowerCase());
		if(codeTable!=null){
			Map<String, String> cn = codeTable.get(code);
			return cn.get(CT_DEFAULT_NAME_KEY);
		}
		return null;
	}
	
	/**
	 * 注册码表
	 * @param ctClazz 码表class
	 * @throws InitException 
	 */
	/**
	 * 注册枚举变量的码表
	 * @param ctClazz 枚举变量类型
	 * @param extFieldsStr 扩展项 逗号隔开
	 * @param excludeCodeStr 不注册的项 逗号隔开
	 */
	@SuppressWarnings({"rawtypes" })
	public static void registEnumCodeTableWithCodeNameField(Class ctClazz,String extFieldsStr,String excludeCodeStr){
		registEnumCodeTableWithCustomCodeNameField(ctClazz, CT_DEFAULT_CODE_KEY, CT_DEFAULT_NAME_KEY, extFieldsStr, excludeCodeStr);
	}
	/**
	 * 注册自定义码表
	 * @param ctClazz  码表class
	 * @param codeFileName code 字段映射名
	 * @param nameFileName name字段映射名
	 * @param extFieldsStr  扩展属性 逗号隔开
	 * @param excludeCodeStr 不包含的属性 逗号隔开
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registEnumCodeTableWithCustomCodeNameField(Class ctClazz,String codeFileName,String nameFileName,String extFieldsStr,String excludeCodeStr){
		check(ctClazz,codeFileName,nameFileName);
		EnumSet cts = EnumSet.allOf(ctClazz);
		//exclude
		List<String> excludeCodes = new ArrayList<String>();
        if(StringUtils.hasText(excludeCodeStr)){
        	excludeCodes.addAll(Arrays.asList(excludeCodeStr.split(",")));
        }
        //ext
        List<String> extFileds = new ArrayList<String>();
        if(StringUtils.hasText(extFieldsStr)){
        	extFileds.addAll(Arrays.asList(extFieldsStr.split(",")));
        }
        if (cts != null) {
        	Map<String, Map<String, String>> ct = new HashMap<String, Map<String, String>>();
            for (Iterator<Enum> iterator = cts.iterator(); iterator.hasNext(); ) {
                Enum em = (Enum) iterator.next();
                String code = (String) ReflectionUtil.getFieldValue(em, codeFileName);
                if(excludeCodes.contains(code))continue;
                String name = (String) ReflectionUtil.getFieldValue(em, nameFileName);
                Map<String, String> cn = new HashMap<String, String>();
                cn.put(CT_DEFAULT_NAME_KEY, name);
                for (Iterator iterator2 = extFileds.iterator(); iterator2
						.hasNext();) {
					String f = (String) iterator2.next();
					String v = (String) ReflectionUtil.getFieldValue(em, f);
					cn.put(f, v);
				}
                ct.put(code, cn);
            }
            registCodeTableCodeAndFields(ctClazz.getSimpleName(), ct);
        }
	}
	/**
	 * 验证不能相同，必须有code和name两个属性
	 * @param ctClazz
	 * @throws InitException 
	 */
	private static void check(Class<?> ctClazz,String codeFileName,String nameFileName){
		String key = ctClazz.getSimpleName();
		String codeKeyName = CT_DEFAULT_CODE_KEY;
		String nameKeyName = CT_DEFAULT_NAME_KEY;
		if(StrUtil.isNotBank(codeFileName)){
			codeKeyName = codeFileName;
		}
		if(StrUtil.isNotBank(nameKeyName)){
			nameKeyName = nameFileName;
		}
		if(!(ReflectionUtil.hasField(ctClazz, codeKeyName)&&ReflectionUtil.hasField(ctClazz, nameKeyName))){
			logger.error("码表["+ctClazz+"]注册失败，enum里面必须包含code和name属性");
		}
		if(codeTablePool.containsKey(key)){
			logger.error("码表["+ctClazz+"]注册失败，键值已被注册");
		}
	}
}
