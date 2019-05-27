package com.fantasy.app.core.interceptor.inner;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import com.fantasy.app.core.annotation.WsdNoNeedAuth;
import com.fantasy.app.core.annotation.WsdNoNeedLogin;
import com.fantasy.app.core.base.InterceptorBean;
import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.component.log.Log;
import com.fantasy.app.core.interceptor.InterceptorArgs;
import com.fantasy.app.core.para.CorePara.StaticPara;


/**
 * 系统权限的控制
 * 
 * @日期：2012-12-14下午11:21:12
 * @作者：公众号：18岁fantasy
 */
@Component
public class SimpleAuthInterceptor extends InterceptorBean {

	private static Logger logger = Log.getLogger(LogType.AUTH_MGR);

	@Override
	public SIGNAL beforeControllerMethodExecute(InterceptorArgs interceptorArgs) {
		HandlerMethod handlerMethod = interceptorArgs.getHandlerMethod();
		HttpServletRequest request = interceptorArgs.getRequest();
		if (request == null || request.getMethod() == null) {
			return SIGNAL.CONTINUE;
		}
		HttpServletResponse response = interceptorArgs.getResponse();
		//无需登录权限认证
		if (null != handlerMethod.getMethodAnnotation(WsdNoNeedLogin.class)) {
			logger.info("佚名资源：" + request.getRequestURI());
			return SIGNAL.CONTINUE;
		} else {
			Object user = getSessionVo(interceptorArgs);
			if (user == null) {
				try {
					response.sendError(StaticPara._NO_SESSION_ERROR_CODE, "请登录！");
				} catch (IOException e) {
				}
				return SIGNAL.STOP;
			} else {
				if (null != handlerMethod.getMethodAnnotation(WsdNoNeedAuth.class)) {
					logger.info("公共资源：" + request.getRequestURI());
					return SIGNAL.CONTINUE;
				}
			}
			return SIGNAL.CONTINUE;
		}
	}

	@Override
	public SIGNAL interceptorExecuteErrorSignal(Exception beforeControllerMethodExecuteException) {
		return SIGNAL.STOP;
	}

	private Object getSessionVo(InterceptorArgs interceptorArgs) {
		Object sessionVo = interceptorArgs.getLoginUser();
		return sessionVo;
	}
}
