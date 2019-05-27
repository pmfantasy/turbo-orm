package com.fantasy.app.core.spring;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.component.log.Log;

/**
 * 日期处理
 * @日期：2013-6-6下午7:39:38
 * @作者：公众号：18岁fantasy
 */
public class MultiCustomDateEditor extends PropertyEditorSupport {

	private static Logger logger = Log.getLogger(LogType.SYSINNER);
	private static final String[] dateFormat = new String[]{"yyyy","yyyy-MM","yyyy-MM-dd","yyyy-MM-dd HH","yyyy-MM-dd HH:mm","yyyy-MM-dd HH:mm:ss"};

	private final boolean allowEmpty;

	private final int exactDateLength;

	public MultiCustomDateEditor(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
		this.exactDateLength = -1;
	}




	/**
	 * Parse the Date from the given text, using the specified DateFormat.
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			// Treat empty String as null value.
			setValue(null);
		}
		else if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
			
			throw new IllegalArgumentException(
					"Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
		}
		else {
			try {
				setValue(DateUtils.parseDate(text, dateFormat));
			}
			catch (ParseException ex) {
				logger.error("解析日期出错",ex);
				throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
			}
		}
	}

}