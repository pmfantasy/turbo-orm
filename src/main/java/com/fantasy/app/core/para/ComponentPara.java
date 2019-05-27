package com.fantasy.app.core.para;


public  class ComponentPara{
	/**
	 * 日志
	 * @author 公众号：18岁fantasy
	 * @2015-8-13 @下午4:52:05
	 */
	public static class LOG{
		//是否入库
		public  static  boolean LOG_TO_DB_ENABLE = ParaGetTool.getKernelPara(Boolean.class,"core.log.to_db_enable");
		//日志入库规则
		public  static  String LOG_TODB_APPENDER_LEVEL = ParaGetTool.getKernelPara("core.log.to_db_appender_level");
		//自定义日志入库类
		public static String CUSTOM_SAVETODB_CLASS = ParaGetTool.getKernelPara("core.log.to_db_custom_class");
		//入库表名
		public static final String LOG_TODB_TABLE = ParaGetTool.getKernelPara("core.log.to_db_table");
	}
	/**
	 * 下载相关
	 * @author 公众号：18岁fantasy
	 * 2016-8-16 上午10:44:16
	 */
	public static class MULTI {
		public static String UPLOAD_DEFAULT_BASEDIR = ParaGetTool.getKernelPara("core.multi.upload.default.basedir",System.getProperty("user.home"));
		public static long UPLOAD_DEFAULT_MAXSIZE = ParaGetTool.getPara(long.class, "core.multi.upload.default.maxsize",10485760l);
	}
	/**
	 * 邮件相关
	 * @author 公众号：18岁fantasy
	 * 2016-8-16 上午10:44:22
	 */
	public static class MAIL {
		public static String MAIL_CHARSET = "utf-8";
		public static String MAIL_SMTPSERVER = ParaGetTool.getPara(String.class,"core.mail.smtpserver");
		public static String MAIL_SENDER_EMAILUSERNAME = ParaGetTool.getPara(String.class,"core.mail.username");
		public static String MAIL_SENDER_EMAILPASSWORD = ParaGetTool.getPara(String.class,"core.mail.pwd");
		public static String MAIL_SENDER_EMAILADDR = ParaGetTool.getPara(String.class,"core.mail.email_addr",MAIL_SENDER_EMAILUSERNAME);
		public static String MAIL_SENDER_EMAILNEEDAUTH = ParaGetTool.getPara(String.class,"core.mail.need_auth","false");
	}
}
