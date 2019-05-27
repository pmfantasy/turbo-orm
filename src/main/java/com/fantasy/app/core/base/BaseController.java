package com.fantasy.app.core.base;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fantasy.app.core.base.user.AuthUserType;
import com.fantasy.app.core.base.user.LoginUser;
import com.fantasy.app.core.component.updown.DefaulFileStorageStrategy;
import com.fantasy.app.core.component.updown.FileStorageStrategy;
import com.fantasy.app.core.component.updown.MultiPartHandlerSpring;
import com.fantasy.app.core.component.updown.UpLoadResult;
import com.fantasy.app.core.interceptor.inner.ParamSetInterceptor;
import com.fantasy.app.core.para.CorePara.StaticPara;
import com.fantasy.app.core.spring.IntEditor;
import com.fantasy.app.core.spring.MultiCustomDateEditor;
import com.fantasy.app.core.util.CollectionUtil;
import com.fantasy.app.core.util.SpringUtil;


/**
 * Contoller 基类，所有controll请继承此类 
 * @日期：2012-12-14下午11:05:48
 * @作者：公众号：18岁fantasy
 */
public class BaseController extends AjaxJsonBaseController implements RefererAble {
	public static final String COMMON_ALERT_KEY = "alert_key";
	public static final String VO_KEY = "vo";  //单个实体类型
	public static final String DATA_KEY = "_data"; //默认的查询结果返回数据key值，返回结果可能是一个VO对象，也可能是一个List<T>对象， 在JSP页面通过${_data}取值
	public static final String TOTAL_KEY = "_total"; //记录总条数
	public static final String LAYOUT_XML_FILENAME = "xml_filename";
	public static final String LAYOUT_INIT_URL = "inti_url";

	public String backUrl = "";

	public void setLayOutData(Model model, String xmlFileName, String initUrl) {
		model.addAttribute(LAYOUT_XML_FILENAME, xmlFileName);
		model.addAttribute(LAYOUT_INIT_URL, initUrl);
	}

	public void setReferer(String referer) {
		this.backUrl = referer;
	}
	/**
	 * @param model
	 * @param msg
	 * @return
	 */
	public String toAlertPage(Model model, String msg) {
		model.addAttribute(COMMON_ALERT_KEY, msg);
		return StaticPara.COMMON_ALERT_MODEL_PAGE;
	}
	/**
	 * @param alertPage
	 * @param model
	 * @param msg
	 * @return
	 */
	public String toAlertPage(String alertPage, Model model, String msg) {
		model.addAttribute(COMMON_ALERT_KEY, msg);
		return alertPage;
	}
	
	/**
	 * @param alertPage
	 * @param request
	 * @param msg
	 * @return
	 */
	public String toAlertPage(String alertPage, HttpServletRequest request, String msg) {
		request.setAttribute(COMMON_ALERT_KEY, msg);
		return alertPage;
	}

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(int.class, new IntEditor());
		binder.registerCustomEditor(Date.class, new MultiCustomDateEditor(true));
	}

	public void setTotoal(Model model, int total) {
		model.addAttribute(TOTAL_KEY, total);
	}

	public List<UpLoadResult> upLoadFile(MultipartHttpServletRequest request, FileStorageStrategy fileStorageStrategy)
			throws IOException {
		return MultiPartHandlerSpring.uploadFile(request, fileStorageStrategy);
	}

	public List<UpLoadResult> upLoadFile(MultipartHttpServletRequest request) throws IOException {
		return upLoadFile(request, new DefaulFileStorageStrategy());
	}

	@Override
	public String getReferer() {
		return backUrl;
	}

	/**
	 * 
	 * @param model
	 * @param paraStr 参数数字符串，eg: name=joe&sex=1
	 */
	public void setUrlpara(HttpServletRequest request, String urlpara) {
		String urlparaInReq = (String) request.getAttribute(ParamSetInterceptor.URLPARA_KEY);
    	if(StringUtils.hasText(urlpara)&&!StringUtils.hasText(urlparaInReq)){
    		request.setAttribute(ParamSetInterceptor.URLPARA_KEY, urlpara);
    	}
	}

	/**
	 * 下载文件，异常请拦截 下载成功后请返回null 或者void
	 * @param response
	 * @param file 要下载的文件
	 * @param nameToShow 显示在界面下载框上的文件名称 eg:文件.doc 
	 * @throws IOException 下载中出现的异常，包括@see FileNotFoundException 
	 */
	public void downLoadFile(HttpServletResponse response, File file, String nameToShow) throws IOException {
		MultiPartHandlerSpring.downLoadFile(response, file, nameToShow);
	}


	public static String WSD_TABLE(String tableName) {
		return BaseDao.WSD_TABLE(tableName);
	}


	/**
	 * 获取当前登录信息
	 * @param httpSession
	 * @return
	 */
	public static LoginUser  getCurrentUserInfo(HttpSession httpSession) {
		if (null == httpSession){
			return null;
		}
		LoginUser sessionVo = (LoginUser)httpSession.getAttribute(StaticPara.USER_SESSION_KEY);
		return sessionVo;
	}
	/**
     * 获取当前登录用户真实姓名信息
     * @param httpSession
     * @return
     */
    public static String  getLoginUserRealName(HttpSession httpSession) {
        LoginUser sessionVo = getCurrentUserInfo(httpSession);
        return sessionVo==null?"":sessionVo.getRealName();
    }
	/**
	 * 获取当前登录信息
	 * @param httpSession
	 * @return
	 */
	public static void  putCurrentUserInfoToSession(LoginUser loginUser,HttpSession httpSession) {
		if (null == httpSession||null==loginUser){
			return;
		}
		httpSession.setAttribute(StaticPara.USER_SESSION_KEY, loginUser);
	}
	public static boolean isSuperAdmin(HttpSession httpSession){
		LoginUser loginUser = getCurrentUserInfo(httpSession);
		if(loginUser!=null){
			return loginUser.isSuperAdmin();
		}
		return false;
	}
	public String getAppRoot(HttpServletRequest request)
	  {
	    return request.getSession().getServletContext().getRealPath("/");
	  }
	public static String getUid(HttpSession httpSession){
		LoginUser loginUser = getCurrentUserInfo(httpSession);
		if(loginUser!=null){
			return loginUser.getId();
		}
		return null;
	}
	public static String getUid(){
		HttpSession httpSession = SpringUtil.getSession();
		if(httpSession!=null){
			return getUid(httpSession);
		}
		return null;
	}
	/**
	 * 获取当前登陆用户的角色类型列表
	 * @param httpSession
	 * @return
	 */
	public static Set<AuthUserType> getLoginUserType(HttpSession httpSession){
		LoginUser loginUser = getCurrentUserInfo(httpSession);
		if(loginUser!=null){
			return loginUser.getAuthUserTypes();
		}
		return null;
	}
	/**
	 * 获取登陆用户角色字符串
	 * @param httpSession
	 * @return
	 */
	public static String  getRoleTypeStr(HttpSession httpSession){
		Set<AuthUserType> authUserTypes = getLoginUserType(httpSession);
		return getRoleTypeStr(authUserTypes);
	}
	public static String  getRoleTypeStr(Set<AuthUserType> authUserTypes){
		if(CollectionUtil.hasElement(authUserTypes)){
			StringBuffer roleTypeStr = new StringBuffer();
			roleTypeStr.append("[");
			for (Iterator<AuthUserType> iterator = authUserTypes.iterator(); iterator
					.hasNext();) {
				AuthUserType authUserType2 = (AuthUserType) iterator
						.next();
				roleTypeStr.append(authUserType2.getName());
				if(iterator.hasNext()){
					roleTypeStr.append(",");
				}else{
					roleTypeStr.append("]");
				}
			}
			return roleTypeStr.toString();
		}
		return "";
	}
	/**
	 * 登陆用户是否包含安全员角色
	 * @param httpSession
	 * @return
	 */
	public static boolean hasSafeRole(HttpSession httpSession){
		Set<AuthUserType> authUserTypes = getLoginUserType(httpSession);
		if(authUserTypes!=null){
			return authUserTypes.contains(AuthUserType.SAFETY_OFFICER);
		}
		return false;
	}
	/**
	 * 登陆用户是否包含审计角色
	 * @param httpSession
	 * @return
	 */
	public static boolean hasAuditRole(HttpSession httpSession){
		Set<AuthUserType> authUserTypes = getLoginUserType(httpSession);
		if(authUserTypes!=null){
			return authUserTypes.contains(AuthUserType.AUDIT_OFFICER);
		}
		return false;
	}
	/**
	 * 登陆用户是否包含系统管理员角色
	 * @param httpSession
	 * @return
	 */
	public static boolean hasSysMgrRole(HttpSession httpSession){
		Set<AuthUserType> authUserTypes = getLoginUserType(httpSession);
		if(authUserTypes!=null){
			return authUserTypes.contains(AuthUserType.SYSTEM_MANAGER_OFFICER);
		}
		return false;
	}
	/**
	 * 登陆用户是否包含业务员角色
	 * @param httpSession
	 * @return
	 */
	public static boolean hasBusiRole(HttpSession httpSession){
		Set<AuthUserType> authUserTypes = getLoginUserType(httpSession);
		if(authUserTypes!=null){
			return authUserTypes.contains(AuthUserType.BUSINESS_OFFICER);
		}
		return false;
	}
}
