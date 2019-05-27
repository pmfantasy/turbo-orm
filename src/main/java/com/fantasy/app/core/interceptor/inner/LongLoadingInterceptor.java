package com.fantasy.app.core.interceptor.inner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fantasy.app.core.base.InterceptorBean;
import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.component.log.Log;
import com.fantasy.app.core.interceptor.InterceptorArgs;


/**
 * 加载时间过长的请求，首先会被跳转到loading界面,当前不支持form表单提交
 * @日期：2012-12-14下午11:21:12
 * @作者：公众号：18岁fantasy
 */
@Component
public class LongLoadingInterceptor extends InterceptorBean {

	private Logger logger = Log.getLogger(LogType.SYSINNER);

	public static final String LOADING_PAGE = "/static/pages/loading.jsp";
	public static final String FORWORD_URL_KEY = "forword";
	public static final String TO_LOADING_PARAMETER = "_ldps";
	public static final String FULL_TO_LOADING_PARAMETER = TO_LOADING_PARAMETER + "=1";

	@Override
	public SIGNAL beforeControllerMethodExecute(InterceptorArgs interceptorArgs) {
		HttpServletRequest request = interceptorArgs.getRequest();
		if (request == null || request.getMethod() == null) {
			return SIGNAL.CONTINUE;
		}
		HttpServletResponse response = interceptorArgs.getResponse();
		String urlpara = request.getParameter(TO_LOADING_PARAMETER);
		if (StringUtils.hasText(urlpara)) {
			String requestURLWithQuery = interceptorArgs.getRequestURLWithQuery();
			requestURLWithQuery = requestURLWithQuery.replaceAll(FULL_TO_LOADING_PARAMETER, "");
			request.setAttribute(FORWORD_URL_KEY, requestURLWithQuery);
			try {
				interceptorArgs.getRequest().getRequestDispatcher(LOADING_PAGE).forward(request, response);
			} catch (Exception e) {
				logger.error(e);
				return SIGNAL.CONTINUE;
			}
			return SIGNAL.STOP;
		}
		return SIGNAL.CONTINUE;
	}

	@Override
	public SIGNAL interceptorExecuteErrorSignal(Exception beforeControllerMethodExecuteException) {
		return SIGNAL.CONTINUE;
	}
}
