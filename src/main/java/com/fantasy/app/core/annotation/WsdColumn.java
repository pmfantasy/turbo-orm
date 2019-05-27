package com.fantasy.app.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;

/**
 * 标示数据库字段
 * @日期：2012-12-14下午11:04:47
 * @作者：公众号：18岁fantasy
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WsdColumn {
	/**
	 * 字段名
	 * @return
	 */
	String name() default "";
	/**
	 * 如果使用@see _DEFAULT值，将会使用Field的类型
	 * @return
	 */
	TYPE type() default TYPE._DEFAULT;	
	boolean isId() default false;
	boolean UUID() default false;
    enum TYPE{
    	_DEFAULT,
    	NUMBER(Integer.class,Long.class,Double.class,int.class,double.class,long.class),
    	TIMESTEMP(Date.class),
    	VARCHAR2(String.class),
    	BLOB(byte[].class),
    	CLOB(String.class);
    	
    	private TYPE(){
    		this.equalClazz = new Class<?>[]{};
    	}
    	private Class<?>[] equalClazz;
    	private TYPE(Class<?>... equalClazz){
    		this.equalClazz = equalClazz;
    	}
    	public Class<?>[] getequalClazz(){
    		return equalClazz;
    	}
    }
    /**
     * 数据库字段分组
     * @author 公众号：18岁fantasy
     * @2014-11-21 @下午7:53:24
     */
    public static enum DB_TYPE_GROUP{
    	NUMBER("number","int","tinyint","decimal","float","double","bigint","numeric"),
    	TIMESTAMP("timestamp","timestamp with local time zone","timestamp with time zone"),
    	DATE("date","datetime","time"),
    	STR("varchar2","varchar","char","nvarchar2","nvarchar"),
    	BLOB("blob"),
    	CLOB("clob","ntext","text","longtext");
    	private String[] dbType;
    	DB_TYPE_GROUP(String... dbType){
    		this.dbType = dbType;
    	}
		public String[] getDbType() {
			return dbType;
		}
		/*
		 * 是否可以是时间戳字段
		 */
		public static boolean isDateColumn(String columnType){
			DB_TYPE_GROUP[] groups = {TIMESTAMP,DATE};
			for (int i = 0; i < groups.length; i++) {
				String[] dbTypes =  groups[i].dbType;
		    	for (int j = 0; j < dbTypes.length; j++) {
					if(columnType.toLowerCase().startsWith(dbTypes[j])){
						return true;
					}
				}
			}
			return false;
		}
		public static boolean isBigColumn(String columnType){
			DB_TYPE_GROUP[] groups = {BLOB,CLOB};
			for (int i = 0; i < groups.length; i++) {
				String[] dbTypes =  groups[i].dbType;
		    	for (int j = 0; j < dbTypes.length; j++) {
					if(columnType.toLowerCase().startsWith(dbTypes[j])){
						return true;
					}
				}
			}
			return false;
		}
		public static DB_TYPE_GROUP getTypeGroup(String dbTypeArg){
			DB_TYPE_GROUP[] groups = DB_TYPE_GROUP.values();
		    for (int i = 0; i < groups.length; i++) {
		    	 String[] dbTypes =  groups[i].dbType;
		    	 for (int j = 0; j < dbTypes.length; j++) {
					if(dbTypeArg.toLowerCase().startsWith(dbTypes[j])){
						return groups[i];
					}
				}
			}
		    return null;
		}
    }
}