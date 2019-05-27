package com.fantasy.app.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fantasy.app.core.base.user.LoginUser;


public class InterceptorArgs {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private HandlerMethod handlerMethod;
	private String requestURI;
	private String requestURLWithQuery;
	private LoginUser loginUser;
	private String cacheUrl;
	private ModelAndView modelAndView;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HandlerMethod getHandlerMethod() {
		return handlerMethod;
	}

	public void setHandlerMethod(HandlerMethod handlerMethod) {
		this.handlerMethod = handlerMethod;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	public String getCacheUrl() {
		return cacheUrl;
	}

	public void setCacheUrl(String cacheUrl) {
		this.cacheUrl = cacheUrl;
	}

	public ModelAndView getModelAndView() {
		return modelAndView;
	}

	public void setModelAndView(ModelAndView modelAndView) {
		this.modelAndView = modelAndView;
	}

	public String getRequestURLWithQuery() {
		return requestURLWithQuery;
	}

	public void setRequestURLWithQuery(String requestURLWithQuery) {
		this.requestURLWithQuery = requestURLWithQuery;
	}

	public LoginUser getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(LoginUser loginUser) {
		this.loginUser = loginUser;
	}
}
