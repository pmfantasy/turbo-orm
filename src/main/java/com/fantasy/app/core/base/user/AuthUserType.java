package com.fantasy.app.core.base.user;


/**
 * 人员分类
 * @author 公众号：18岁fantasy
 * @2015-7-20 @下午4:15:40
 */
public enum AuthUserType {
	/**
	 * 业务操作员
	 */
	BUSINESS_OFFICER("1","业务员"),
	/**
	 * 系统管理员，用于系统基本对象，参数的管理
	 */
	SYSTEM_MANAGER_OFFICER("2","系统管理员"),
	/**
	 * 安全员，用户权限配置
	 */
	SAFETY_OFFICER("4","安全保密管理员"),
	/**
	 * 审计员，可以检查包括安全员和管理员的行为
	 */
	AUDIT_OFFICER("8","安全审计员"),
	
	/**
	 * 系统后台，包含后台进程，接口
	 */
	SYSTEM_BACK_OFFICER("15","系统后台"),
	
	/**
	 * 默认为系统管理员
	 */
	DEFAULT_OFFICER("99","其他");
	private String code;
	private String name;
	private AuthUserType(String code, String name) {
		this.code = code;
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static AuthUserType getAuthUserType(String code){
		AuthUserType[]  ats = AuthUserType.values();
		for (AuthUserType at:ats) {
			if(at.getCode().equals(code)){
				return at;
			}
		}
		return null;
	}
}
