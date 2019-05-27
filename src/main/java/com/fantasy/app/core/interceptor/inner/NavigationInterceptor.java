package com.fantasy.app.core.interceptor.inner;

import org.springframework.stereotype.Component;

import com.fantasy.app.core.base.InterceptorBean;
import com.fantasy.app.core.interceptor.InterceptorArgs;

@Component
public class NavigationInterceptor  extends InterceptorBean{

	public static final String NAVIGATION_KEY = "navigation";

	@Override
	public SIGNAL beforeControllerMethodExecute(InterceptorArgs interceptorArgs) {
		
		return SIGNAL.CONTINUE;
	}

	@Override
	public SIGNAL interceptorExecuteErrorSignal(Exception beforeControllerMethodExecuteException) {
		return SIGNAL.CONTINUE;
	}
}
