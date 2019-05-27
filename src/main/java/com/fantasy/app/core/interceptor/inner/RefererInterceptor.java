package com.fantasy.app.core.interceptor.inner;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fantasy.app.core.base.BaseController;
import com.fantasy.app.core.base.InterceptorBean;
import com.fantasy.app.core.base.RefererAble;
import com.fantasy.app.core.interceptor.InterceptorArgs;


/**
 * 将request头中的Referer参数 赋值给RefererAble类 eg: {@link BaseController}
 * @日期：2012-12-14下午11:21:12
 * @作者：公众号：18岁fantasy
 */
@Component
public class RefererInterceptor extends InterceptorBean {

	public static final String BACKURL_KEY = "backurl";

	@Override
	public SIGNAL beforeControllerMethodExecute(InterceptorArgs interceptorArgs) {
		HandlerMethod handlerMethod = interceptorArgs.getHandlerMethod();
		HttpServletRequest request = interceptorArgs.getRequest();
		if (request == null || request.getMethod() == null) {
			return SIGNAL.CONTINUE;
		}
		//如果请求里面有backurl值就不设置
		String backurl = null;
		try {
			backurl = request.getParameter(BACKURL_KEY);
		} catch (Exception e) {
			return SIGNAL.CONTINUE;
		}
		if (!StringUtils.hasText(backurl)) {
			backurl = request.getHeader("Referer");
		}
		if (backurl != null && StringUtils.hasText(backurl)) {
			Object object = handlerMethod.getBean();
			if (object instanceof RefererAble) {
				RefererAble refererAble = ((RefererAble) object);
				refererAble.setReferer(backurl);
			}
		}
		return SIGNAL.CONTINUE;
	}

	@Override
	public void afterControllerMethodExecute(InterceptorArgs interceptorArgs, ModelAndView modelAndView) {
		HandlerMethod handlerMethod = interceptorArgs.getHandlerMethod();
		HttpServletRequest request = interceptorArgs.getRequest();
		if (modelAndView != null && modelAndView.getModel().containsKey(BACKURL_KEY)) {
			return;
		}

		if (request.getAttribute(BACKURL_KEY) != null) {
			return;
		}

		Object object = handlerMethod.getBean();
		//???
		/*if (object != null && object instanceof LoginController) {
			return;
		}*/
		if (object instanceof RefererAble) {
			RefererAble refererAble = ((RefererAble) object);
			if (StringUtils.hasText(refererAble.getReferer())) {
				if (null != modelAndView) {
					modelAndView.addObject(BACKURL_KEY, refererAble.getReferer());
				} else {
					request.setAttribute(BACKURL_KEY, refererAble.getReferer());
				}
			}
		}
	}

	@Override
	public SIGNAL interceptorExecuteErrorSignal(Exception beforeControllerMethodExecuteException) {
		return SIGNAL.CONTINUE;
	}
}
