package com.fantasy.app.core.component.db.orm.wherebean;
/**
 * 直接append
 * @author 公众号：18岁fantasy
 * 2017-5-3 下午5:25:37
 */
public class JustAppend {

	private String content;

	
	public JustAppend(String content) {
		super();
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "JustAppend [content=" + content + "]";
	}
	
}
