package com.fantasy.app.core.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.cxf.tools.common.toolspec.ToolSpec;
import org.apache.cxf.tools.validator.WSDLValidator;
import org.apache.log4j.Logger;

import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.component.log.Log;


/**
 * WSDL校验工具类
 * 依赖于org.apache.cxf
 * @author SINE
 *  usage：
 		
	File wsdlFile = new File("D:/demos/wsdl/userservice.wsdl");
	boolean ok = WSDLValidatorUtil.validate(wsdlFile);
	
	String wsdlUrl = "http://www.domain.com/service/userwebservice?wsdl";
	boolean ok = WSDLValidatorUtil.validate(wsdlUrl);
 */
public class WSDLValidatorUtil {

	private static Logger logger = Log.getLogger(LogType.SYSINNER);
	
	private static WSDLValidator validator;
	static {
		try {
			InputStream toolspecStream = WSDLValidator.class.getResourceAsStream("wsdlvalidator.xml");
			ToolSpec spec = new ToolSpec(toolspecStream, false);
			toolspecStream.close();
			validator = new WSDLValidator(spec);
		} catch (IOException e) {
			logger.error("WSDLValidatorUtil --> IOException");
		} catch (Exception e) {
			logger.error("WSDLValidatorUtil --> Exception");
		}
	}
	/**
	 * 验证远端的WSDL是否合法
	 * @param wsdlUrl
	 * @return
	 */
	public static boolean validate(String wsdlUrl) {
		boolean flag = false;
		if (StrUtil.isBlank(wsdlUrl) || !wsdlUrl.endsWith("wsdl")) {
			return false;
		}
		try {
			String[] pargs = new String[] { wsdlUrl };
			validator.setArguments(pargs);
			flag = validator.executeForMaven();
		} catch (Exception e) {
			return false;
		}
		return flag;
	}

	/**
	 * 校验本地的WSDL是否合法
	 * @param wsdlFile
	 * @return
	 */
	public static boolean validate(File wsdlFile) {
		if (wsdlFile == null || !wsdlFile.exists() || !wsdlFile.isFile()) {
			return false;
		}
		return validate(wsdlFile.getAbsolutePath());
	}

}
