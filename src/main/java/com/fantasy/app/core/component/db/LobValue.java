package com.fantasy.app.core.component.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.DisposableSqlTypeValue;
import org.springframework.jdbc.support.lob.LobCreator;

import com.fantasy.app.core.base.CmCommon;

/**
 * clob blob 封装类型
 * @日期：2012-12-14下午11:21:56
 * @作者：公众号：18岁fantasy
 */
public class LobValue implements DisposableSqlTypeValue {

	private final Object content;

	private final LobCreator lobCreator = CmCommon.getDao().getLobHandler().getLobCreator();

	/**
	 * 创建一个blob对象 
	 * @param bytes 
	 */
	public LobValue(byte[] bytes) {
		this.content = bytes;
	}
	/**
	 * 创建一个clob对象
	 * @param content
	 */
	public LobValue(String content) {
		this.content = content;
	}
	/**
	 * Set the specified content via the LobCreator.
	 * be carefull,the null value will be case to string null
	 */
	public void setTypeValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName)
			throws SQLException {
		//blob
		if (this.content instanceof byte[] || this.content == null) {
			this.lobCreator.setBlobAsBytes(ps, paramIndex, (byte[]) this.content);
		}
		//clob
		else if (this.content instanceof String || this.content == null) {
			this.lobCreator.setClobAsString(ps, paramIndex, (String) this.content);
		}
		else {
			throw new IllegalArgumentException(
					"类型  [" + this.content.getClass().getName() + "] 不支持 CLOB 和 blob" +
							" ,CLOB 对应String，blob对应byte[]");
		}
	}
	/**
	 * Close the LobCreator, if any.
	 */
	public void cleanup() {
		this.lobCreator.close();
	}
}
