package com.fantasy.app.core.base;

import org.springframework.web.servlet.ModelAndView;

import com.fantasy.app.core.interceptor.InterceptorArgs;

/**
 * mvc拦截器基础类
 * @author 公众号：18岁fantasy
 * 2017-5-25 下午3:24:45
 */
public abstract class InterceptorBean {

	/**
	 * 当前连接器拦截处理完成后是继续后续操作
	 * @日期：2012-12-15上午6:45:45
	 * @作者：公众号：18岁fantasy
	 */
	public enum SIGNAL {
		STOP, CONTINUE
	}

	/**在controller方法执行之前进行拦截
	 * @param request
	 * @param response
	 * @param handlerMethod
	 * @return
	 */
	public SIGNAL beforeControllerMethodExecute(InterceptorArgs interceptorArgs) {
		return SIGNAL.CONTINUE;
	}

	/**
	 * 在controller方法执行之后进行拦截
	 * @param request
	 * @param response
	 * @param handlerMethod
	 * @param modelAndView
	 */
	public void afterControllerMethodExecute(InterceptorArgs interceptorArgs, ModelAndView modelAndView) {
	}

	/**
	 * 拦截器执行异常之后的动作
	 * @return
	 */
	public abstract SIGNAL interceptorExecuteErrorSignal(Exception beforeControllerMethodExecuteException);

	/**
	 * 拦截器执行异常之后的动作,有默认处理
	 * @return
	 */
	public SIGNAL errorSignal(Exception beforeControllerMethodExecuteException) {
		SIGNAL signal = interceptorExecuteErrorSignal(beforeControllerMethodExecuteException);
		return signal == null ? SIGNAL.CONTINUE : signal;
	}
}
