package com.fantasy.app.core.component.db.orm.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
/**
 * 单个返回值 Integer处理
 * @author 公众号：18岁fantasy
 * 2017-5-3 下午5:22:39
 */
public class SingleColumnIntegerRowMapper implements RowMapper<Integer>{
	@Override
	public Integer mapRow(ResultSet rss, int rowNum) throws SQLException {
		BigDecimal decimal = rss.getBigDecimal(1);
		return decimal==null?null:decimal.intValue();
	}
}
