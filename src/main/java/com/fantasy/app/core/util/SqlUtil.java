package com.fantasy.app.core.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import com.fantasy.app.core.annotation.WsdTable;
import com.fantasy.app.core.base.ModuleFeatureBean;
import com.fantasy.app.core.component.db.orm.SqlArg;
import com.fantasy.app.core.component.db.orm.SqlCreator;


/**
 * 根据给定的参数封装sql语句
 * @日期：2012-12-18下午5:26:48
 * @作者：公众号：18岁fantasy
 */
public class SqlUtil extends SqlCreator{
	
	
	/**
	 * 将数据库表字段转化为实体类属性
	 * e.g. user_info --> userInfo
	 * @param columnName
	 * @return
	 */
	public static String toCamelStyle(String columnName) {
		StringBuffer sb = new StringBuffer();
		boolean match = false;
		for (int i = 0; i < columnName.length(); i++) {
			char ch = columnName.charAt(i);
			if (match && ch >= 97 && ch <= 122)
				ch -= 32;
			if (ch != '_') {
				match = false;
				sb.append(ch);
			} else {
				match = true;
			}
		}
		return sb.toString();
	}

	/**
	 * 将驼峰命名法转化为下划线的形式
	 * e.g.  UserInfo --> user_info     loginId --> login_id
	 * @param name
	 * @return
	 */
	public static String addUnderscores(String name) {
		StringBuffer buf = new StringBuffer(name.replace('.', '_'));
		for (int i = 1; i < buf.length() - 1; i++) {
			if (Character.isLowerCase(buf.charAt(i - 1)) && Character.isUpperCase(buf.charAt(i))
					&& Character.isLowerCase(buf.charAt(i + 1))) {
				buf.insert(i++, '_');
			}
		}
		return buf.toString().toLowerCase();
	}

	/**
	 * 根据注解获取表名
	 * @param clazz
	 * @return
	 */
	private static String parseTable(Class<? extends ModuleFeatureBean> clazz) {
		WsdTable tableAnnotation = AnnotationUtils.findAnnotation(clazz, WsdTable.class);
		String tableName = clazz.getSimpleName().toUpperCase();
		if (null != tableAnnotation) {
			String annotationTableName = null;
			annotationTableName = (String) AnnotationUtils.getValue(tableAnnotation, "name");
			tableName = StringUtils.hasText(annotationTableName) ? annotationTableName.toUpperCase() : tableName;
		}
		return addUnderscores(tableName).toLowerCase();
	}

	/**
	 * 通过Class注解和反射获取UpdateSql语句
	 * @param clazz
	 * @param prefix  表名的前缀 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getUpdateSql(@SuppressWarnings("rawtypes") Class clazz, String prefix) {
		StringBuffer sb = new StringBuffer("");
		String tableName = parseTable(clazz);
		if (StringUtils.hasText(prefix) && !tableName.startsWith(prefix)) {
			tableName = prefix.toUpperCase() + "_" + tableName;
		}
		sb.append("UPDATE "+tableName+" SET ");
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			String clumnName = addUnderscores(f.getName());
			if(!"id".equals(clumnName) && !"cuid".equals(clumnName) && !"created".equals(clumnName)){
				sb.append(clumnName.toLowerCase() +"=?,");
			}
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(" WHERE id=?");
		return sb.toString();
	}
	
	
	/**
	 * 通过Class注解和反射获取getUpdateParamString语句
	 * @param clazz
	 * @return
	 */
	public static String getUpdateParamString(@SuppressWarnings("rawtypes") Class clazz) {
		StringBuffer sb = new StringBuffer("");
		sb.append("new Object[] { ");
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			String fieldName = f.getName();
			if(!"id".equals(fieldName) && !"cuid".equals(fieldName) && !"created".equals(fieldName)){
				sb.append("vo.get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1) +"(),");
			}
		}
		sb.append("vo.getId()");
		sb.append(" }");
		return sb.toString();
	}
	
	
	/**
	 * 通过Class注解和反射获取QuerySql语句
	 * @param clazz
	 * @param prefix  表名的前缀 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getQuerySql(@SuppressWarnings("rawtypes") Class clazz, String prefix) {
		StringBuffer sb = new StringBuffer("");
		String tableName = parseTable(clazz);
		if (StringUtils.hasText(prefix) && !tableName.startsWith(prefix)) {
			tableName = prefix.toUpperCase() + "_" + tableName;
		}
		sb.append("SELECT ");
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			String clumnName = addUnderscores(f.getName());
			sb.append("t." + clumnName.toLowerCase() + ", ");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(" FROM " + tableName + " t");
		return sb.toString();
	}
	
	
	
	/**
	 * 创建InsertSql
	 * @param tableName
	 * @param data
	 * @return
	 * e.g.
	 * tableName:user    
	 * data: {id=1,name="sinba",age=25}
	 * 
	 * SqlArg.sql:  INSERT INTO user (id,name,age) VALUES (?,?,?)
	 * SqlArg.args:  Object[]{1,"sinba",25}
	 */
	public static SqlArg getInsertSql(String tableName , Map<String,Object> data){
		StringBuffer clumnNames = new StringBuffer("");
		StringBuffer placeholder = new StringBuffer("");
		List<Object> paramList = new ArrayList<Object>();
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			String columNams = entry.getKey();
			Object columValue = entry.getValue();
			clumnNames.append(columNams + ",");
			placeholder.append("?,");
			paramList.add(columValue);
		}
		String sql = "INSERT INTO " + tableName + " ("
				+ StrUtil.substringBeforeLast(clumnNames.toString(), ",") + ") " + 
				" VALUES ("+ StrUtil.substringBeforeLast(placeholder.toString(), ",") + ")";
		SqlArg sqlArg = new SqlArg();
		sqlArg.setSql(sql);
		sqlArg.setArgs(paramList.toArray());
		return sqlArg;
	}
	
	
	/**
	 * 创建更新语句
	 * @param tableName
	 * @param pkNames  联合主键用英文逗号隔开
	 * @param data
	 * @return
	 * e.g.
	 * tableName:user    
	 * pkNames: id,name
	 * data: {id=1,name="sinba",age=25}
	 * 
	 * SqlArg.sql:  UPDATE user SET name=?,age=? where id=?
	 * SqlArg.args: Object[]{"sinba",25,1}
	 */
	public static SqlArg getUpdateSql(String tableName , String pkNames , Map<String,Object> data){
		StringBuffer clumnNames = new StringBuffer("");
		List<Object> paramList = new ArrayList<Object>();
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			String columNams = entry.getKey();
			Object columValue = entry.getValue();
			if( !StrUtil.containsIgnoreCase(pkNames, columNams)){
				clumnNames.append( columNams + "=?,");
				paramList.add(columValue);
			}
		}
		String[] pkNameArr = pkNames.split(",");
		String pkConditon = "";
		for(String pkName:pkNameArr){
			pkConditon += pkName + "=? and ";
			paramList.add(data.get(pkName));
		}
		
		pkConditon = StrUtil.substringBeforeLast(pkConditon, "and");
		
		String sql = "UPDATE "+tableName+" SET "+StrUtil.substringBeforeLast(clumnNames.toString(), ",")+" WHERE "+ pkConditon;
		SqlArg sqlArg = new SqlArg();
		sqlArg.setSql(sql);
		sqlArg.setArgs(paramList.toArray());
		return sqlArg;
	}
	
	

	/**
	 * 返回简单的key=？
	 * @param args
	 * @param excludeNullAndBlank
	 * @return
	 */
	/*
	public static String createWhere(Map<String, Object> args,boolean excludeNullAndBlank){
	if(args==null||args.isEmpty())return "";
	StringBuilder where = new StringBuilder();
	where.append(" WHERE ");
	int count = 0;
	for (Iterator<String> iterator = args.keySet().iterator(); iterator.hasNext();) {
		String clumnName = iterator.next();
		Object value = args.get(clumnName);
		
		if(value==null&&excludeNullAndBlank) continue;
		if((value instanceof String&&!StringUtils.hasText((String)value))&&excludeNullAndBlank) continue;
		if(count>0)where.append(" and ");
		if(value==null){
			where.append(clumnName).append(" =? ");
		}else{
			if(value instanceof String&&isLikeValue((String)value)) {
				where.append(clumnName).append(" like ? ");
			}else if(value instanceof Date){
				where.append(clumnName).append(" = ?");
			}else{
				where.append(clumnName).append(" =? ");
			}
		}
		count ++;
	}
	if(count==0)return "";
	return where.toString();
	}*/
	public static void main(String[] args1) {
	
	}
}
