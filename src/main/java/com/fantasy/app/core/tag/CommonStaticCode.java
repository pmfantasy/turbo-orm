package com.fantasy.app.core.tag;

/**
 * 通用的码表
 * @author 公众号：18岁fantasy
 * 2017-5-19 上午10:31:53
 */
public class CommonStaticCode {
	
	public static enum OPENCLOSE {
		OPEN("1", "开启"), COLSE("0", "暂停");

		private String status;
		private String desc;
        private String name;
        private String code;
		public String getStatus() {
			return status;
		}

		public String getDesc() {
			return desc;
		}
		private OPENCLOSE(String status, String desc) {
			this.status = status;
			this.desc = desc;
			this.code = status;
			this.name=desc;
		}

		public String getName() {
			return name;
		}

		public String getCode() {
			return code;
		}
	}
	/**
	 * 通用实体状态标志
	 * @author 公众号：18岁fantasy
	 * @2015-8-13 @下午5:10:42
	 */
	public static enum ENTITY_STATUS {
		ENABLE("1", "启用"), 
		DISABLE("0", "停用"),
		DELETE("9", "删除"),
		
		REL_DELETE("99", "物理删除");

		private String code;
		private String name;
		private ENTITY_STATUS(String code, String name) {
			this.code = code;
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public String getCode() {
			return code;
		}
	}
	/**
	 * 是或者否
	 * @author 公众号：18岁fantasy
	 * 2017-5-19 上午9:42:14
	 */
	public static enum YESNO {
		 NO("0", "否"),YES("1", "是");

		private String status;
		private String desc;

		public String getStatus() {
			return status;
		}
		public String getDesc() {
			return desc;
		}
		private YESNO(String status, String desc) {
			this.status = status;
			this.desc = desc;
		}
	}
	/**
	 * 成功失败
	 * @author 公众号：18岁fantasy
	 * 2017-5-19 上午9:42:27
	 */
	public static enum SUCCESSFAIL {
		SUCCESS("1", "成功"), FAIL("0", "失败");
		
		private String status;
		private String desc;
		
		public String getStatus() {
			return status;
		}
		public String getDesc() {
			return desc;
		}
		
		private SUCCESSFAIL(String status, String desc) {
			this.status = status;
			this.desc = desc;
		}
	}
	/**
	 * 男或者女
	 * @author 公众号：18岁fantasy
	 * 2017-5-19 上午9:42:34
	 */
	public static enum SEX {
		MALE("1", "男"), FMALE("2", "女");
		
		private String code;
		private String name;
		public String getCode() {
			return code;
		}
		public String getName() {
			return name;
		}


		private SEX(String code, String name) {
			this.code = code;
			this.name = name;
		}
	}
	/**
	 * 增删改查
	 * @author 公众号：18岁fantasy
	 * 2017-5-19 上午9:42:45
	 */
	public static enum OPERATION_TYPE {
		ADD("1", "添加"), 
		DELETE("2", "删除"),
		UPDATE("3", "修改"), 
		GET("4", "查看");
		private String code;
		private String name;
		public String getCode() {
			return code;
		}
		public String getName() {
			return name;
		}
		private OPERATION_TYPE(String code, String name) {
			this.code = code;
			this.name = name;
		}
	}
	
}