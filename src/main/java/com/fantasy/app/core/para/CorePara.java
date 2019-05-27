package com.fantasy.app.core.para;


/**
 * 系统环境变量设置和获取类
 * @日期：2012-12-15上午10:15:20
 * @作者：公众号：18岁fantasy
 */
public class CorePara {
	/**
	 * 不变的参数
	 * @author 公众号：18岁fantasy
	 * 2017-5-25 下午2:52:31
	 */
	public static class StaticPara{
		public static final String DEFAULT_FILE_TMP_SUFIX = ".dhtmp";
		public static String USER_SESSION_KEY = "_user_session";
		public static String USER_SESSION_TOKEN_KEY = "_user_session_token";
		public static String SSO_TOKEN_KEY = "ssotoken";
		public static String SUPER_USER = "_suer_user";
		public static String HTTP_CHARSET = "UTF-8";
		public static int _NO_SESSION_ERROR_CODE = 488;
		public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		public static final String DB_DATE_FORMAT = "yyyy-MM-dd HH24:mi:ss";
		public static final String COMMON_ALERT_MODEL_PAGE = "pub/alert/alert_model"; //处理结果成功界面，不带top
		public static final String COMMON_ALERT_PAGE = "pub/alert/alert"; //处理结果返回页面，带top
		public static final String USER_SESSION_URLS_KEY = "_user_session_urls";
		public static final String USER_SESSION_MENUS_KEY = "_user_session_menus";
		public static final String USER_SESSION_MENUS_TOP_KEY = "_user_session_top_menus";
	}
	/**
	 * 初始化参数
	 * @author 公众号：18岁fantasy
	 * @2013-12-9 @下午2:35:26
	 */
	public static class CoreInitCtx {
	  
		private static final String PRODUCT_MODE = ParaGetTool.getKernelPara(String.class,"app.software.mode","product");
		/**
		 * 内核版本号
		 */
		public static final String core_version = "2.1.0(20170511)";
		/**
		 * 版本号
		 */
		public static final String version = ParaGetTool.getKernelPara("app.software.version");
		/**
		 * 软件名称
		 */
		public static final String appName = ParaGetTool.getKernelPara("app.displayname");
		
		public static final String ZLWORKDIR_KEY = "app_work_dir";
		/**
		 * 项目临时目录或数据目录
		 */
		public static final String ZLWORKDIR = System.getProperty("app_work_dir")==null?ParaGetTool.getPara(ZLWORKDIR_KEY, System.getProperty("user.dir")):System.getProperty("app_work_dir");
		
		public static String DEFAULT_SCHEMA_NAME = "";
		
		public static int PAGESIZE = ParaGetTool.getKernelPara(int.class, "app.page.pagersize", 10);;
		
		public static boolean PRINTSQL = ParaGetTool.getKernelPara(Boolean.class, "app.db.printsql", false);
		
		public static String APP_ROOT = null;
		/**
		 * 默认schema
		 * @return
		 */
		public static String defaultSchema() {
			return ParaGetTool.getPara(String.class, "db.default.schema");
		}

		public static boolean isProductMode() {
			return "product".equalsIgnoreCase(PRODUCT_MODE);
		}
		
	}

}
