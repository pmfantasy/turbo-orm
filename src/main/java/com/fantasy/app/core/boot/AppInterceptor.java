package com.fantasy.app.core.boot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fantasy.app.core.base.BaseController;
import com.fantasy.app.core.base.CmCommon;
import com.fantasy.app.core.base.InterceptorBean;
import com.fantasy.app.core.base.InterceptorBean.SIGNAL;
import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.base.user.LoginUser;
import com.fantasy.app.core.component.log.Log;
import com.fantasy.app.core.interceptor.InterceptorArgs;


/**
 *  拦截器执行类,执行注册拦截器，注册方式 ：{@link cn.gov.yellowriver.em.common.registerInterceptor#registerInterceptor}
 * @日期：2012-12-14下午11:21:12
 * @作者：公众号：18岁fantasy
 */
@Component
public class AppInterceptor extends HandlerInterceptorAdapter {

	private static  Logger logger = Log.getLogger(LogType.SYSINNER);

	private static List<InterceptorInfo> registeredInterceptorBeans = new ArrayList<InterceptorInfo>();

	private static Map<String, InterceptorBean> interceptorBeansInSpring = null;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (interceptorBeansInSpring == null) {
			interceptorBeansInSpring = new HashMap<String, InterceptorBean>();
			Map<String, InterceptorBean> beansInSpring = CmCommon.getBeanByType(InterceptorBean.class);
			if (beansInSpring != null && !beansInSpring.isEmpty()) {
				for (Iterator<InterceptorBean> iterator = beansInSpring.values().iterator(); iterator.hasNext();) {
					InterceptorBean interceptorBean = (InterceptorBean) iterator.next();
					interceptorBeansInSpring.put(interceptorBean.getClass().getName(), interceptorBean);
				}
			} else
				return true;
		}

		InterceptorArgs interceptorArgs = new InterceptorArgs();
		interceptorArgs.setRequest(request);
		interceptorArgs.setResponse(response);
		interceptorArgs.setHandlerMethod((HandlerMethod) handler);
		interceptorArgs.setRequestURI(request.getRequestURI());
		interceptorArgs.setRequestURLWithQuery(request.getRequestURL() + "?" + request.getQueryString());
		injectCacheUrl(interceptorArgs);
		//注入session
		injectUserSessionVo(interceptorArgs);
		
		List<InterceptorInfo> registeredInterceptorBeans = new ArrayList<>();
        registeredInterceptorBeans.addAll( this.registeredInterceptorBeans );
		for (Iterator<InterceptorInfo> iterator = registeredInterceptorBeans.iterator(); iterator.hasNext();) {
			InterceptorInfo beanInfo = iterator.next();
			String beanName = beanInfo.getClazz();
			if (null != interceptorBeansInSpring.get(beanName)) {
				//logger.debug("[" + beanName + "] 执行 拦截，操作：beforeControllerMethodExecute");
				InterceptorBean interceptorBean = interceptorBeansInSpring.get(beanName);
				SIGNAL signal = null;
				try {
					signal = interceptorBean.beforeControllerMethodExecute(interceptorArgs);
				} catch (Exception e) {
					if(e instanceof IllegalStateException){
						
					} else {
						signal = interceptorBean.errorSignal(e);
						logger.error("[" + beanName + "] 执行 拦截异常，signal：" + signal.name(), e);
					}
				}
				//logger.debug("[" + beanName + "] 执行 拦截结束，signal：" + signal.name());
				if (SIGNAL.STOP == signal)
					return false;
				else if (SIGNAL.CONTINUE == signal)
					continue;
				else {/* no this signal */
					continue;
				}
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		if (interceptorBeansInSpring == null || interceptorBeansInSpring.isEmpty()) {
			return;
		}
		InterceptorArgs interceptorArgs = new InterceptorArgs();
		//reset 3 args
		interceptorArgs.setRequest(request);
		interceptorArgs.setResponse(response);
		interceptorArgs.setHandlerMethod((HandlerMethod) handler);

		List<InterceptorInfo> registeredInterceptorBeans = new ArrayList<>();
		registeredInterceptorBeans.addAll( this.registeredInterceptorBeans );
		for (Iterator<InterceptorInfo> iterator = registeredInterceptorBeans.iterator(); iterator.hasNext();) {
			InterceptorInfo beanInfo = iterator.next();

			String beanName = beanInfo.getClazz();

			if (null != interceptorBeansInSpring.get(beanName)) {
				//logger.debug("[" + beanName + "] 执行 处理，操作：afterControllerMethodExecute");
				interceptorBeansInSpring.get(beanName).afterControllerMethodExecute(interceptorArgs, modelAndView);
				//logger.debug("[" + beanName + "] 执行 处理成功");
			}
		}
	}

	public void injectCacheUrl(InterceptorArgs interceptorArgs) {
		HandlerMethod handlerMethod = interceptorArgs.getHandlerMethod();
		RequestMapping classmapping = handlerMethod.getBean().getClass().getAnnotation(RequestMapping.class);
		String pre = "";
		if (classmapping != null) {
			pre = classmapping.value()[0] == null ? "" : classmapping.value()[0];
		}
		String action = "";
		RequestMapping methodmapping = handlerMethod.getMethodAnnotation(RequestMapping.class);
		if (methodmapping != null) {
			if (methodmapping.value() != null && (methodmapping.value().length != 0)) {
				action = methodmapping.value()[0] == null ? "" : methodmapping.value()[0];
			}
		}
		interceptorArgs.setCacheUrl(convertCacheUrl(pre, action));
	}

	/**
	 * 注入用户信息seesion数据
	 * @param interceptorArgs
	 */
	public void injectUserSessionVo(InterceptorArgs interceptorArgs) {
      HttpServletRequest request = interceptorArgs.getRequest();
      if (request != null) {
          HttpSession httpSession = null;
          try {
              httpSession = request.getSession(true);
          } catch (Exception e) {
              logger.error("获取Session发生异常！" + request.getRequestURI());
              return;
          }
          LoginUser loginUser = BaseController.getCurrentUserInfo(httpSession);
          interceptorArgs.setLoginUser(loginUser);
      }
      
    }
	public static String convertCacheUrl(String pre, String action) {
		if (StringUtils.hasText(pre)) {
			if (pre.startsWith("/"))
				pre = pre.substring(1);
			if (pre.endsWith("/"))
				pre = pre.substring(0, pre.length() - 1);
		}
		if (StringUtils.hasText(action)) {
			if (action.startsWith("/"))
				action = action.substring(1);
		}
		String end = action.toLowerCase();
		if (StringUtils.hasText(pre)) {
			end = (pre + "/" + action).toLowerCase();
		}
		if (!end.endsWith(".do"))
			end += ".do";
		return end;
	}
	
	/**
	 * 注册拦截器
	 * @param clazz 拦截器类
	 * @param order 拦截器执行排序，越小越先执行
	 */
	public static void register(Class<? extends InterceptorBean> clazz, int order) {
		registeredInterceptorBeans.add(new InterceptorInfo(clazz.getName(), order));
	}
	
	public static void sort() {
	  Collections.sort(registeredInterceptorBeans);
	}
}

class InterceptorInfo implements Comparable<InterceptorInfo> {

	public InterceptorInfo(String clazz, int order) {
		super();
		this.clazz = clazz;
		this.order = order;
	}

	private String clazz;
	private int order;

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InterceptorInfo other = (InterceptorInfo) obj;
		if (clazz == null) {
			if (other.clazz != null)
				return false;
		} else if (!clazz.equals(other.clazz))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InterceptorInfo [clazz=" + clazz + ", order=" + order + "]";
	}

	@Override
	public int compareTo(InterceptorInfo o) {
		return this.order - o.order;
	}

}
