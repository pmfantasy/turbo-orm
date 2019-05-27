package com.fantasy.app.core.base.log;
/**
 * 日志记录类型
 * @author 公众号：18岁fantasy
 * 2017-5-11 下午3:21:21
 */
public class LogType {

	/**
	 * 安全保密员的日志
	 */
	public static final String SAFELOG = "SAFELOG";
	/**
	 * 系统管理员产生的日志
	 */
	public static final String SYSLOG = "SYSLOG";
	/**
	 * 审计员产生的日志
	 */
	public static final String AUDITLOG = "AUDITLOG";
	
	/**
	 * 业务员产生的日志
	 */
	public static final String BUSILOG = "BUSILOG";
	
	/**
	 * 系统内部产生的日志
	 */
	public static final String SYSINNER = "SYSINNER";
	
	/**
	 * 发邮件的日志
	 */
	public static final String MAIL = "MAIL";
	
	/**
	 * 数据库处理的日志
	 */
	public static final String DB = "DB";
	/**
	 * 缓存的日志
	 */
	public static final String CACHE = "CACHE";
	/**
	 * QUARTZ的日志
	 */
	public static final String QUARTZ = "QUARTZ";
	
	/**
	 * SOCKET的日志
	 */
	public static final String SOCKET = "SOCKET";
	/**
	 * 线程池的日志
	 */
	public static final String THREAD = "THREAD";
	/**
	 * 权限管理的日志
	 */
	public static final String AUTH_MGR = "AUTH_MGR";
}
