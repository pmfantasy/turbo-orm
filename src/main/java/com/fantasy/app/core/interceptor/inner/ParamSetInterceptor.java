package com.fantasy.app.core.interceptor.inner;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fantasy.app.core.base.InterceptorBean;
import com.fantasy.app.core.interceptor.InterceptorArgs;
import com.fantasy.app.core.util.StrUtil;


/**
 * 分页查询参数设置，从request里面获取key为_urlpara的参数并放入request.setAttribute(_urlpara中
 * 在分页组件<jsp:param value="_urlpara" name="urlpara"/>参数进行获取
 * 
 * @日期：2012-12-14下午11:21:12
 * @作者：公众号：18岁fantasy
 */
@Component
public class ParamSetInterceptor extends InterceptorBean {

	public static final String URLPARA_KEY = "_urlpara";
	public static final String PAGER_KEY = "pg.offset";
	public static final String MENUCODE_KEY = "menucode_key";

	@Override
	public SIGNAL beforeControllerMethodExecute(InterceptorArgs interceptorArgs) {
		HttpServletRequest request = interceptorArgs.getRequest();
		if (request == null || request.getMethod() == null) {
			return SIGNAL.CONTINUE;
		}
		String urlpara = null;
		String menuKey = null;
		try {
			urlpara = request.getParameter(URLPARA_KEY);
			menuKey = request.getParameter(MENUCODE_KEY);
		} catch (Exception e) {
			return SIGNAL.CONTINUE;
		}
		if (StringUtils.hasText(menuKey)) {
			request.setAttribute(MENUCODE_KEY, menuKey);
		}
		if (StringUtils.hasText(urlpara)) {// in post
			request.setAttribute(URLPARA_KEY, urlpara);
		} else {// in get
			String queryString = request.getQueryString();
			String pagerValue = request.getParameter(PAGER_KEY);
			if (StringUtils.hasText(queryString) && (StrUtil.isNotBank(pagerValue)||StrUtil.isNotBank(pagerValue))) {
				if(StrUtil.isNotBank(pagerValue)){
					int offsetAfterPagerPara = queryString.indexOf(PAGER_KEY) + PAGER_KEY.length() + 1+ pagerValue.length();
					queryString = queryString.substring(offsetAfterPagerPara);
				}
				request.setAttribute(URLPARA_KEY, removePre7(queryString));
			}
		}
		return SIGNAL.CONTINUE;
	}

	// 去掉前面的&
	private String removePre7(String str) {
		if (!StringUtils.hasText(str)) {
			return "";
		}
		if (str.startsWith("&")) {
			str = str.substring(1);
			return removePre7(str);
		}
		return str;
	}

	@Override
	public SIGNAL interceptorExecuteErrorSignal(Exception beforeControllerMethodExecuteException) {
		return SIGNAL.CONTINUE;
	}
	
	
//	public static void main(String[] args) {
//		String queryString = "pg.offset=80";
//		int offsetAfterPagerPara = queryString.indexOf(PAGER_KEY) + PAGER_KEY.length() + 1 + 2;
//		String queryAfterPager = queryString.substring(offsetAfterPagerPara);
//		System.out.println(queryAfterPager);
//		System.out.println(new PagerInterceptor().removePre7(queryAfterPager));
//	}
}
