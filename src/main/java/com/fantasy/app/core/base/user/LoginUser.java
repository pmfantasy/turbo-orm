package com.fantasy.app.core.base.user;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;


/**
 * 登录用户信息
 * @author 公众号：18岁fantasy
 * @2015-8-11 @下午3:20:15
 */
public class LoginUser implements Serializable {
	
	private static final long serialVersionUID = 1L;
	//通用属性
	/**
	 * 用户id
	 */
	private String id;
	/**
	 * 用户登录名
	 */
	private String userName;
	/**
	 * 用户编码
	 */
	private String userCode;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 是否超级管理员
	 */
	private boolean superAdmin;
	/**
	 * 是否从单点登录登录
	 */
	private boolean fromSso;
	/**
	 * 上次登录时间
	 */
	private Date lastLoginTime;
	/**
	 * 本次登录时间
	 */
	private Date loginTime;
	/**
	 * 登录IP
	 */
	private String ip;
	/**
	 * 用户类型
	 */
	private Set<AuthUserType> authUserTypes;
	/**
	 * 是否登录成功
	 */
	private boolean loginSuccess;
	//通用属性-end
	/**
	 * 应用自定义属性
	 */
	private Object customData;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public boolean isSuperAdmin() {
		return superAdmin;
	}
	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}
	public boolean isFromSso() {
		return fromSso;
	}
	public void setFromSso(boolean fromSso) {
		this.fromSso = fromSso;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void ip(Date loginTime) {
		this.loginTime = loginTime;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Object getCustomData() {
		return customData;
	}
	public void setCustomData(Object customData) {
		this.customData = customData;
	}
	public boolean isLoginSuccess() {
		return loginSuccess;
	}
	public void setLoginSuccess(boolean loginSuccess) {
		this.loginSuccess = loginSuccess;
	}
	public Set<AuthUserType> getAuthUserTypes() {
		return authUserTypes;
	}
	public void setAuthUserTypes(Set<AuthUserType> authUserTypes) {
		this.authUserTypes = authUserTypes;
	}
	
}
