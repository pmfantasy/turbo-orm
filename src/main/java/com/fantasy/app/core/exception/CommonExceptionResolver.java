package com.fantasy.app.core.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fantasy.app.core.base.BaseController;
import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.component.log.Log;
import com.fantasy.app.core.para.CorePara.StaticPara;

/**
 * 通用的异常处理类 
 * @日期：2012-12-14下午11:16:36
 * @作者：公众号：18岁fantasy
 */
@Component("exceptionResolver")
public class CommonExceptionResolver implements HandlerExceptionResolver {

	private static Logger logger = Log.getLogger(LogType.SYSINNER);

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object arg2, Exception exception) {
		ModelAndView failModelAndView = new ModelAndView(
				StaticPara.COMMON_ALERT_PAGE);
		String msg = "操作失败";
		if (exception instanceof MessageAlertable&&StringUtils.hasText(exception.getMessage())) {
			msg = exception.getMessage();
		} 
		failModelAndView.addObject(BaseController.COMMON_ALERT_KEY,
					msg);
		//if (exception instanceof Logable) {
			logger.error(exception.getMessage(), exception);
		//}
		if(exception instanceof MultipartException){
			handleMultiPartException(failModelAndView,(MultipartException)exception);
		}
		return failModelAndView;
	}
    public void handleMultiPartException(ModelAndView failModelAndView,MultipartException exception){
    	if(exception==null)return;
    	if(exception instanceof MaxUploadSizeExceededException){
    		MaxUploadSizeExceededException exceededException = (MaxUploadSizeExceededException)exception;
    		long maxsize = exceededException.getMaxUploadSize();
    		failModelAndView.addObject(BaseController.COMMON_ALERT_KEY, 
    				"上传文件失败，最大限制为："+maxsize+" bytes");
    	}else{
    		failModelAndView.addObject(BaseController.COMMON_ALERT_KEY, 
    				"上传文件异常");
    	}
    	
    }
}
