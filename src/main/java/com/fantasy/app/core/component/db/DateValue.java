package com.fantasy.app.core.component.db;

import java.sql.Types;

import org.springframework.jdbc.core.SqlParameterValue;

import com.fantasy.app.core.base.Dao;


/**
 * 到
 * @author bing
 *
 */
public class DateValue extends SqlParameterValue{

	public DateValue(Object value,Dao dao) {
		//mysql 用date 会截断 oracle不会
		//super(HookPara.isMysql()?Types.TIMESTAMP:Types.TIME, value);
		super(dao.isOracle()?Types.TIME:Types.TIMESTAMP, value);
	}
}
