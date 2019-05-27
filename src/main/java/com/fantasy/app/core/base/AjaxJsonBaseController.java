package com.fantasy.app.core.base;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;

import com.fantasy.app.core.para.CorePara.StaticPara;
import com.fantasy.app.core.util.JsonUtil;

/**
 * ajax 基础类
 * @author 公众号：18岁fantasy
 * 2017-5-25 下午3:23:28
 */
public class AjaxJsonBaseController {

	/*****************ajax请求常量  start*********************************/
	private static final String AJAX_STATUS_KEY = "status";//Ajax请求返回值状态key
	private static final String AJAX_MSG_KEY = "msg"; //Ajax请求返回消息key
	private static final String AJAX_DATA_KEY = "data"; //Ajax请求返回的数据
	//private static final String AJAX_ERROR_KEY = "error"; //Ajax请求错误key
	private static final String AJAX_SUCCESS = "1"; //Ajax请求status成功状态值
	private static final String AJAX_FAIL = "0"; //Ajax请求status失败状态值
	/*****************ajax请求常量  end*********************************/

	/*****************Form表单Ajax校验常量  start*******************/
	public static final String FORM_CHECK_SUCCESS = "success";
	public static final String FORM_CHECK_FAIL = "fail";

	/*****************Form表单Ajax校验常量  end********************/

	
	
	public static final String jsonpCallBackFunctionName = "jsonpCallBack";
	
	
	/** 
	 * ajax操作失败返回信息，包括状态信息，和用户提示信息
	 * @param errorMsgForDubug
	 * @param errorMsgForUser
	 * @return
	 */
	protected Map<String, Object> jsonFailResult(String errorMsgForUser) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(AJAX_STATUS_KEY, AJAX_FAIL);
		resultMap.put(AJAX_MSG_KEY, errorMsgForUser);
		return resultMap;
	}
	/** 
	 * ajax操作失败返回信息，包括状态信息，和用户提示信息
	 * @param errorMsgForDubug
	 * @param errorMsgForUser
	 * @return
	 */
	protected Map<String, Object> jsonFailResult(String errorMsgForUser,Object data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(AJAX_STATUS_KEY, AJAX_FAIL);
		resultMap.put(AJAX_MSG_KEY, errorMsgForUser);
		resultMap.put(AJAX_DATA_KEY, data);
		return resultMap;
	}
	/**
	 * 
	 * ajax操作失败返回信息，包括状态信息，和用户提示信息
	 * @param errorMsgForDubug
	 * @param errorMsgForUser
	 * @return
	 */
	protected String jsonpFailResult(String msg, Object data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(AJAX_STATUS_KEY, AJAX_FAIL);
		resultMap.put(AJAX_MSG_KEY, msg);
		resultMap.put(AJAX_DATA_KEY, data);
		return jsonpCallBackFunctionName + "(" + JsonUtil.toJson(resultMap) + ")";
	}

	
	/**
	 * 
	 * ajax操作成功返回信息，包括状态信息，和用户提示信息
	 * @param errorMsgForDubug
	 * @param errorMsgForUser
	 * @return
	 */
	protected Map<String, Object> jsonSuccessResult(String successMsgForUser) {
		return jsonSuccessDataResult(successMsgForUser, null);
	}
	/**
	 * 
	 * ajax操作失败返回信息，包括状态信息，和用户提示信息
	 * @param errorMsgForDubug
	 * @param errorMsgForUser
	 * @return
	 */
	protected String jsonpSuccessResult(String successMsgForUser, Object data) {
		Map<String, Object> resultMap = jsonSuccessDataResult(successMsgForUser, data);
		return jsonpCallBackFunctionName + "(" + JsonUtil.toJson(resultMap) + ")";
	}
	/**
	 * 
	 * 返回json类型的数据,带有提示信息和返回数据
	 * @param errorMsgForDubug
	 * @param errorMsgForUser
	 * @return
	 */
	protected Map<String, Object> jsonSuccessDataResult(String successMsgForUser, Object value) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(AJAX_STATUS_KEY, AJAX_SUCCESS);
		resultMap.put(AJAX_MSG_KEY, successMsgForUser);
		resultMap.put(AJAX_DATA_KEY, value);
		return resultMap;
	}

	/**
	 * 
	 * @param errorMsgForDubug
	 * @param errorMsgForUser
	 * @return
	 * @throws IOException 
	 */
	protected void writeString(HttpServletResponse response, String value) throws IOException {
		try {
			OutputStream outputStream = response.getOutputStream();
			response.setContentType("text/html;charset=UTF-8");
			FileCopyUtils.copy(value, new OutputStreamWriter(outputStream, StaticPara.HTTP_CHARSET));
			outputStream.flush();
			response.flushBuffer();
			if (outputStream != null)
				outputStream.close();
		} catch (UnsupportedEncodingException e) {
			//
		}
	}
}
