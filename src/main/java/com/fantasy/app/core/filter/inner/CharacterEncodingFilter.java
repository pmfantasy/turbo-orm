package com.fantasy.app.core.filter.inner;

import java.io.UnsupportedEncodingException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.fantasy.app.core.base.InterceptorBean.SIGNAL;
import com.fantasy.app.core.boot.BootParam;
import com.fantasy.app.core.exception.InitException;
import com.fantasy.app.core.filter.AppInitFilter;
import com.fantasy.app.core.para.CorePara.StaticPara;

/**
 * http请求相应的编码filter
 * @author 公众号：18岁fantasy
 * 2017-5-27 下午5:10:54
 */
public class CharacterEncodingFilter implements AppInitFilter{

	public static  String ENCODING = StaticPara.HTTP_CHARSET;
	@Override
	public void init(FilterConfig filterConfig,BootParam bootParam) throws InitException {
		
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response
			,BootParam bootParam) throws  InitException {
			try {
				request.setCharacterEncoding(ENCODING);
				response.setCharacterEncoding(ENCODING);
			} catch (UnsupportedEncodingException e) {
				//不会报错
			}
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public SIGNAL onSuccess(ServletContextEvent contextEvent,
			BootParam bootParam) throws InitException {
		return SIGNAL.CONTINUE;
	}

	@Override
	public SIGNAL onError(ServletContextEvent contextEvent, BootParam bootParam)
			throws InitException {
		return SIGNAL.CONTINUE;
	}

}
