package com.fantasy.app.core.component.db.orm;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.fantasy.app.core.annotation.WsdColumn;
import com.fantasy.app.core.annotation.WsdColumnNullAble;
import com.fantasy.app.core.annotation.WsdNotDbColumn;
import com.fantasy.app.core.annotation.WsdTable;
import com.fantasy.app.core.base.ModuleFeatureBean;
import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.component.db.tablesharding.TableShardingParser;
import com.fantasy.app.core.component.log.Log;
import com.fantasy.app.core.para.CorePara.CoreInitCtx;
import com.fantasy.app.core.util.StrUtil;


/**
 * module类的扫描和解析类
 * @日期：2012-12-14下午11:18:56
 * @作者：公众号：18岁fantasy
 */
public class ModuleParser {

	public static ModuleParser INSTANCE = new ModuleParser();

	public static String MODULE_PACKAGE = "*.*";

	private ModuleParser() {
	}

	public static ModuleParser getInstance() {
		return INSTANCE;
	}

	private static Logger logger = Log.getLogger(LogType.DB);

	private ModuleHandlerFilter moduleHanderFilter = new DefaultModuleHandlerFilter();

	//module的table信息
	private static Map<Class<? extends ModuleFeatureBean>, TableInfo> moduleTable = new HashMap<Class<? extends ModuleFeatureBean>, TableInfo>();
	private static Map<Class<? extends ModuleFeatureBean>, Map<String, ColumnInfo>> moduleColumn = new HashMap<Class<? extends ModuleFeatureBean>, Map<String, ColumnInfo>>();
	private static Map<Class<? extends ModuleFeatureBean>, Map<String, ColumnInfo>> moduleIdColumn = new HashMap<Class<? extends ModuleFeatureBean>, Map<String, ColumnInfo>>();

	private List<Class<? extends ModuleFeatureBean>> getModule(String basePackage) {
		final List<Class<? extends ModuleFeatureBean>> dbModules = new ArrayList<Class<? extends ModuleFeatureBean>>();
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new TypeFilter() {
			@SuppressWarnings("unchecked")
			public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
					throws IOException {
				if (ModuleFeatureBean.class.getName().equals(metadataReader.getClassMetadata().getSuperClassName())) {
					String className = metadataReader.getClassMetadata().getClassName();
					logger.debug("找到  module >>>>" + className);
					try {
						dbModules.add((Class<? extends ModuleFeatureBean>) ClassUtils.forName(className,
								ClassUtils.getDefaultClassLoader()));
					} catch (Exception e) {
						logger.error("注册module[" + className + "]出错，查看是否有无参数public的构造函数", e);
					}
					return true;
				}
				return false;
			}
		});
		String[] ps = basePackage.split("\\|");
		for (int i = 0; i < ps.length; i++) {
			provider.findCandidateComponents(ps[i]);
		}

		return dbModules;
	}

	public void parseModule(ModuleHandlerFilter moduleHanderFilter) {

		if (null == moduleHanderFilter)
			moduleHanderFilter = new DefaultModuleHandlerFilter();
		this.moduleHanderFilter = moduleHanderFilter;

		List<Class<? extends ModuleFeatureBean>> classes = getModule(MODULE_PACKAGE);

		for (Iterator<Class<? extends ModuleFeatureBean>> iterator = classes.iterator(); iterator.hasNext();) {
			Class<? extends ModuleFeatureBean> clazz = (Class<? extends ModuleFeatureBean>) iterator.next();
			parseModule(clazz);
		}
	}

	private void parseModule(Class<? extends ModuleFeatureBean> clazz) {
		logger.debug("解析 module >>>>" + clazz.getName());
		parseSchemaTable(clazz);
		parseColumn(clazz);
		logger.debug("解析结束 module >>>>" + clazz.getName() + " ");

	}

	private void parseSchemaTable(Class<? extends ModuleFeatureBean> clazz) {
		WsdTable tableAnnotation = AnnotationUtils.findAnnotation(clazz, WsdTable.class);
		String tableName = clazz.getSimpleName().toLowerCase();
		String schemaname = CoreInitCtx.DEFAULT_SCHEMA_NAME;
		
		String cusTableName = null;
		String cusSchema = null;
		try {
			 cusTableName = clazz.newInstance().tableName();
			 cusSchema = clazz.newInstance().schema();
		} catch (Exception e) {
			logger.error("解析"+clazz+" 出错!",e);
		}
		if(StrUtil.isNotBank(cusTableName)){
			tableName = cusTableName;
		}
		if(StrUtil.isNotBank(cusSchema)){
			schemaname = cusSchema;
		}
		if (!StrUtil.isNotBank(cusTableName)&&null != tableAnnotation) {
			String annotationTableName = null;
			annotationTableName = (String) AnnotationUtils.getValue(tableAnnotation, "name");
			tableName = StringUtils.hasText(annotationTableName) ? annotationTableName : tableName;
		}
		if (!StrUtil.isNotBank(schemaname)&&null != tableAnnotation) {
			String annotationScheamName = null;
			annotationScheamName = (String) AnnotationUtils.getValue(tableAnnotation, "schema");
			schemaname = StringUtils.hasText(annotationScheamName) ? annotationScheamName : schemaname;
		}
		
        boolean isSharding = TableShardingParser.tableIsSharding(tableName);
		moduleTable.put(clazz, new TableInfo(schemaname,tableName.toLowerCase(),isSharding));

		logger.debug("表映射关系：class[" + clazz.getName() + "] --> table  [" + schemaname+"."+tableName + "]");

	}

	private void parseColumn(Class<? extends ModuleFeatureBean> clazz) {
		Field[] moduleFs = clazz.getDeclaredFields();
		moduleColumn.put(clazz, new LinkedHashMap<String, ColumnInfo>());

		for (Field field : moduleFs) {
			if (moduleHanderFilter.include(field) && !moduleHanderFilter.exclude(field)) {
				moduleColumn.get(clazz).putAll(parseColumn(clazz, field));
			}
		}

	}

	private Map<String, ColumnInfo> parseColumn(Class<? extends ModuleFeatureBean> clazz, Field field) {
		String fieldName = field.getName();
		Class<?> rawtype = field.getType();
		Map<String, ColumnInfo> ccmap = new HashMap<String, ColumnInfo>();
		WsdColumn columnAnnotation = field.getAnnotation(WsdColumn.class);
		WsdColumnNullAble nullAbleAnnotation = field.getAnnotation(WsdColumnNullAble.class);
		boolean nullable = true;

		ColumnInfo columnInfo = null;
		if (null != columnAnnotation) {
			String aname = columnAnnotation.name();
			aname = StringUtils.hasText(aname) ? aname : fieldName;
			aname = aname.toLowerCase();
			WsdColumn.TYPE atype = columnAnnotation.type();
			if (WsdColumn.TYPE._DEFAULT == columnAnnotation.type()) {
				atype = warpType(rawtype);
			}
			if (nullAbleAnnotation != null) {
				nullable = nullAbleAnnotation.value();
			}
			columnInfo = new ColumnInfo(field, atype, aname, nullable, columnAnnotation.isId(),columnAnnotation.UUID());
			columnInfo.setDbColumn(true);
			if (columnAnnotation.isId()) {
				Map<String, ColumnInfo> cc = moduleIdColumn.get(clazz);
				if (cc == null) {
					cc = new LinkedHashMap<String, ColumnInfo>();
					moduleIdColumn.put(clazz, cc);
				}
				moduleIdColumn.get(clazz).put(aname, columnInfo);
			}
			
		} else {
			WsdNotDbColumn notDbColumn = field.getAnnotation(WsdNotDbColumn.class);
			WsdColumn.TYPE moduleType = warpType(rawtype);
			columnInfo = new ColumnInfo(field, moduleType, fieldName.toLowerCase(), nullable, false,false);
			if(null!=notDbColumn){
				columnInfo.setDbColumn(false);
		    }else{
		    	columnInfo.setDbColumn(true);
		    }
		}
		logger.debug("字段[" + field + "]映射结果：" + columnInfo);
		ccmap.put(columnInfo.getColumnName().toLowerCase(), columnInfo);
		return ccmap;

	}

	private WsdColumn.TYPE warpType(Class<?> type) {

		//由于clob也是接受string，如果没有标明使用clob的话返回VARCHAR2
		if (type == String.class)
			return WsdColumn.TYPE.VARCHAR2;

		WsdColumn.TYPE[] ts = WsdColumn.TYPE.values();
		for (WsdColumn.TYPE t : ts) {
			Class<?>[] equalClazz = t.getequalClazz();
			for (Class<?> c : equalClazz) {
				if (c == type)
					return t;
			}
		}
		return null;
	}

	public void parase(ServletContext servletContext) {

		try {
			parseModule(moduleHanderFilter);
		} catch (Exception e) {
			if (servletContext != null)
				servletContext.log("解析ModuleFeatureBean 成功！package>>" + MODULE_PACKAGE, e);
		}
		if (servletContext != null)
			servletContext.log("解析ModuleFeatureBean 成功！package>>" + MODULE_PACKAGE);
		print();
	}

	private void print() {

	}

	public static TableInfo getModuleTable(Class<? extends ModuleFeatureBean> clazz) {
		return moduleTable.get(clazz);
	}

	public static Map<String, ColumnInfo> getModuleColumn(Class<? extends ModuleFeatureBean> clazz) {
		return moduleColumn.get(clazz);
	}

	public static Map<String, ColumnInfo> getModuleIdColumn(Class<? extends ModuleFeatureBean> classToCoder) {
		return moduleIdColumn.get(classToCoder);
	}

	public ModuleHandlerFilter getModuleHanderFilter() {
		return moduleHanderFilter;
	}

	public void setModuleHanderFilter(ModuleHandlerFilter moduleHanderFilter) {
		this.moduleHanderFilter = moduleHanderFilter;
	}

}
