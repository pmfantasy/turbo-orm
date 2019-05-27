package com.fantasy.app.core.util;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
/**
 * Json工具类
 * @author 公众号：18岁fantasy
 * @date 2019年5月5日 下午1:18:48
 */
public class JsonUtil {
	private static ObjectMapper mapper = new ObjectMapper();
	private static ObjectMapper mapperWithDateFormat = new ObjectMapper();
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final String STATUS_KEY = "status";//请求返回值状态key
	private static final String MSG_KEY = "msg"; //请求返回消息key
	private static final String DATA_KEY = "data"; //请求返回的数据
	private static final String ERROR_KEY = "error"; //请求错误key
	private static final String SUCCESS = "1"; //请求status成功状态值
	private static final String FAIL = "0"; //请求status失败状态值

	/**
	 * 将实体类转化为json字符串
	 * @param o
	 * @return
	 */
	public static String toJson(Object obj) {
		String jsonStr = "";
		try {
			jsonStr = mapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}
	
	/**
	 * 将对象序列化到指定的文件中
	 * @param file
	 * @param obj
	 * @return
	 */
	public static void toJsonFile(File jsonFile, Object data) {
		try {
			mapper.writeValue(jsonFile, data);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将json字符串转换为对应的实体
	 * @param jsonString  --json字符串
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map toMap(String jsonString) {
		if (StrUtil.isBlank(jsonString)) {
			throw new IllegalArgumentException("jsonString can not be null");
		}
		Map o = new HashMap();
		try {
			o = mapper.readValue(jsonString, Map.class);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return o;
	}
	
	
	/**
	 * 将json字符串转换为对应的实体
	 * @param content  --json字符串
	 * @param valueType  --实体类型
	 * @return
	 */
	public static Object toObject(String jsonString) {
		if (StrUtil.isBlank(jsonString)) {
			throw new IllegalArgumentException("jsonString can not be null");
		}
		Object o = null;
		try {
			mapper.setDeserializationConfig(mapper.getDeserializationConfig().without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));
			o = mapper.readValue(jsonString, Object.class);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return o;
	}
	

	/**
	 * 将json字符串转换为对应的实体
	 * @param content  --json字符串
	 * @param valueType  --实体类型
	 * @return
	 */

	public static <T> T toObject(String jsonString, Class<T> requiredType) {
		if (StrUtil.isBlank(jsonString)) {
			throw new IllegalArgumentException("jsonString can not be null");
		}
		if (requiredType == null) {
			throw new IllegalArgumentException("requiredType can not be null");
		}
		T o = null;
		try {
			mapper.setDeserializationConfig(mapper.getDeserializationConfig().without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));
			o = mapper.readValue(jsonString, requiredType);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return o;
	}
	
	/**
	 * 将Json字符串转发为对应的List<T>
	 * @param jsonString
	 * @param requiredType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> toObjectList(String jsonString, Class<T> requiredType) {
		if (StrUtil.isBlank(jsonString)) {
			throw new IllegalArgumentException("jsonString can not be null");
		}
		if (requiredType == null) {
			throw new IllegalArgumentException("requiredType can not be null");
		}
		
		List<T> list = null;
		try {
			JavaType javaType = getCollectionType(ArrayList.class, requiredType);
			list = (List<T>) mapper.readValue(jsonString, javaType);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 将Json字符串转发为对应的List<T>
	 * @param jsonString
	 * @param requiredType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> toObjectList(File jsonFile, Class<T> requiredType) {
		if (jsonFile == null) {
			throw new IllegalArgumentException("jsonFile can not be null");
		}
		if (!jsonFile.exists()) {
			throw new IllegalArgumentException("jsonFile is NOT exist");
		}
		if (!jsonFile.isFile()) {
			throw new IllegalArgumentException("jsonFile is NOT a FILE");
		}
		if (requiredType == null) {
			throw new IllegalArgumentException("requiredType can not be null");
		}
		
		List<T> list = null;
		try {
			JavaType javaType = getCollectionType(ArrayList.class, requiredType);
			list = (List<T>) mapper.readValue(jsonFile, javaType);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 将实体类转化为json字符串
	 * @param o  日期将被格式化为 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String toJsonWithDateFormat(Object obj) {
		String jsonStr = "";
		try {
			mapperWithDateFormat.setSerializationConfig(mapperWithDateFormat.getSerializationConfig().withDateFormat(df));
			jsonStr = mapperWithDateFormat.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}
	
	/**
	 * 将实体类转化为json文件
	 * @param o  日期将被格式化为 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static void toJsonWithDateFormat(File jsonFile, Object obj) {
		try {
			mapperWithDateFormat.setSerializationConfig(mapperWithDateFormat.getSerializationConfig().withDateFormat(df));
			mapperWithDateFormat.writeValue(jsonFile, obj);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将json字符串转换为对应的实体
	 * @param jsonString  --json字符串
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map toMapWithDateFormat(String jsonString) {
		if (StrUtil.isBlank(jsonString)) {
			throw new IllegalArgumentException("jsonString can not be null");
		}
		Map o = new HashMap();
		try {
			mapperWithDateFormat.setDeserializationConfig(mapperWithDateFormat.getDeserializationConfig().withDateFormat(df));
			o = mapperWithDateFormat.readValue(jsonString, Map.class);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return o;
	}
	
	
	/**
	 * 将json字符串转换为对应的实体
	 * @param jsonString  --json字符串
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map toMapWithDateFormat(File jsonFile) {
		if (jsonFile == null) {
			throw new IllegalArgumentException("jsonFile can not be null");
		}
		if (!jsonFile.exists()) {
			throw new IllegalArgumentException("jsonFile is NOT exist");
		}
		if (!jsonFile.isFile()) {
			throw new IllegalArgumentException("jsonFile is NOT a FILE");
		}
		Map o = new HashMap();
		try {
			mapperWithDateFormat.setDeserializationConfig(mapperWithDateFormat.getDeserializationConfig().withDateFormat(df));
			o = mapperWithDateFormat.readValue(jsonFile, Map.class);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return o;
	}

	/**
	 * 将json字符串转换为对应的实体
	 * @param content  --json字符串
	 * @param valueType  --实体类型
	 * @return
	 */
	public static Object toObjectWithDateFormat(String jsonString) {
		Object o = null;
		try {
			mapperWithDateFormat.setDeserializationConfig(mapperWithDateFormat.getDeserializationConfig().withDateFormat(df));
			o = mapperWithDateFormat.readValue(jsonString, Object.class);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return o;
	}

	/**
	 * 将json字符串转换为对应的实体
	 * @param content  --json字符串
	 * @param valueType  --实体类型
	 * @return
	 */

	public static <T> T toObjectWithDateFormat(String jsonString, Class<T> requiredType) {
		T o = null;
		try {
			mapperWithDateFormat.setDeserializationConfig(mapperWithDateFormat.getDeserializationConfig().withDateFormat(df));
			o = mapperWithDateFormat.readValue(jsonString, requiredType);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return o;
	}

	/**
	 * 将Json字符串转发为对应的List<T>
	 * @param jsonString
	 * @param requiredType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> toObjectListWithDateFormat(String jsonString, Class<T> requiredType) {
		if (StrUtil.isBlank(jsonString)) {
			throw new IllegalArgumentException("jsonString can not be null");
		}
		if (requiredType == null) {
			throw new IllegalArgumentException("requiredType can not be null");
		}
		
		List<T> list = null;
		try {
			mapperWithDateFormat.setDeserializationConfig(mapperWithDateFormat.getDeserializationConfig().withDateFormat(df));
			JavaType javaType = getCollectionType(ArrayList.class, requiredType);
			list = (List<T>) mapperWithDateFormat.readValue(jsonString, javaType);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 将Json字符串转发为对应的List<T>
	 * @param jsonString
	 * @param requiredType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> toObjectListWithDateFormat(File jsonFile, Class<T> requiredType) {
		if (jsonFile == null) {
			throw new IllegalArgumentException("jsonFile can not be null");
		}
		if (!jsonFile.exists()) {
			throw new IllegalArgumentException("jsonFile is NOT exist");
		}
		if (!jsonFile.isFile()) {
			throw new IllegalArgumentException("jsonFile is NOT a FILE");
		}
		if (requiredType == null) {
			throw new IllegalArgumentException("requiredType can not be null");
		}
		
		List<T> list = null;
		try {
			mapperWithDateFormat.setDeserializationConfig(mapperWithDateFormat.getDeserializationConfig().withDateFormat(df));
			JavaType javaType = getCollectionType(ArrayList.class, requiredType);
			list = (List<T>) mapperWithDateFormat.readValue(jsonFile, javaType);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 操作成功返回信息
	 * 返回值:{"error":"","status":"1","data":"json字符串","msg":"操作成功提示信息"}
	 * @return
	 */
	public static String jsonSuccessResult(String message, Object data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(STATUS_KEY, SUCCESS);
		resultMap.put(MSG_KEY, message);
		resultMap.put(DATA_KEY, data);
		resultMap.put(ERROR_KEY, "");
		return toJson(resultMap);
	}

	/**
	 * 操作失败返回信息
	 * 返回值:{"error":"异常堆栈信息","status":"0","data":"","msg":"操作成功提示信息"}
	 * @return
	 */
	public static String jsonFailResult(String message, String error, Object data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(STATUS_KEY, FAIL);
		resultMap.put(MSG_KEY, message);
		resultMap.put(DATA_KEY, data);
		resultMap.put(ERROR_KEY, error);
		return toJson(resultMap);
	}

	private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}
	
	/**
	 * 将json文件转化为JSON对象
	 * @param jsonFile
	 * @return
	 */
	public static Object toObject(File jsonFile) {
		Object obj = null;
		if (jsonFile == null) {
			throw new IllegalArgumentException("jsonFile can not be null");
		}
		if (!jsonFile.exists()) {
			throw new IllegalArgumentException("jsonFile is NOT exist");
		}
		if (!jsonFile.isFile()) {
			throw new IllegalArgumentException("jsonFile is NOT a FILE");
		}
		try {
			obj = mapper.readValue(jsonFile, Object.class);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	
	/**
	 * 将json文件转化为JSON对象
	 * @param jsonFile
	 * @return
	 */
	public static Object toMap(File jsonFile) {
		Object obj = null;
		if (jsonFile == null) {
			throw new IllegalArgumentException("jsonFile can not be null");
		}
		if (!jsonFile.exists()) {
			throw new IllegalArgumentException("jsonFile is NOT exist");
		}
		if (!jsonFile.isFile()) {
			throw new IllegalArgumentException("jsonFile is NOT a FILE");
		}
		try {
			obj = mapper.readValue(jsonFile, Map.class);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 将json文件转化为指定的对象
	 * @param jsonFile
	 * @param requiredType
	 * @return
	 */
	public static <T> T toObject(File jsonFile, Class<T> requiredType) {
		if (jsonFile == null) {
			throw new IllegalArgumentException("jsonFile can not be null");
		}
		if (!jsonFile.exists()) {
			throw new IllegalArgumentException("jsonFile is NOT exist");
		}
		if (!jsonFile.isFile()) {
			throw new IllegalArgumentException("jsonFile is NOT a FILE");
		}
		if (requiredType == null) {
			throw new IllegalArgumentException("requiredType can not be null");
		}
		T o = null;
		try {
			mapper.setDeserializationConfig(mapper.getDeserializationConfig().without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));
			o = mapper.readValue(jsonFile, requiredType);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return o;
	}
	
}
