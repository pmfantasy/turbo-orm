package com.fantasy.app.core.spring;

import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;
/**
 * 整形处理
 * @日期：2013-6-6下午7:39:14
 * @作者：公众号：18岁fantasy
 */
public class IntEditor extends PropertyEditorSupport {

	@Override
	public String getAsText() {
		return super.getAsText();
	}
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (!StringUtils.hasText(text)) {
			setValue(0);
		} else {
			setValue(Integer.parseInt(text));
		}
	}
}
