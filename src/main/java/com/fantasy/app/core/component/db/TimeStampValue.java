package com.fantasy.app.core.component.db;

import java.sql.Types;

import org.springframework.jdbc.core.SqlParameterValue;

/**
 * 时间戳类型处理
 * @author bing
 *
 */
public class TimeStampValue extends SqlParameterValue{

	public TimeStampValue(java.util.Date value) {
		super(Types.TIMESTAMP, value);
	}
}
