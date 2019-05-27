package com.fantasy.app.core.component.db;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
/**
 * @see ResultSet 的包装，当查询结果没有此字段的时候，给初始值null
 * @日期：2013-6-6下午7:09:55
 * @作者：公众号：18岁fantasy
 */
public class WappedResultSet{
	
	private ResultSet _rs;
	
	private  String[] lables = new String[]{};
	
	public WappedResultSet(ResultSet rs){
		this._rs = rs;
	}
	/**
	 * 指定当前sql会返回哪些字段，数据量大时，将会提到效率
	 * @param rs
	 * @param lables 制定的返回字段名(或别名)
	 */
	public WappedResultSet(ResultSet rs,String[] lables){
		this._rs = rs;
		this.lables = lables;
	}
	public  boolean hasColumn(ResultSet rs,String clumn) throws SQLException{
		   int index = -1;
		   if(lables!=null && lables.length != 0){
			   for(int i = 1; i < lables.length; i++) {
				   lables[i] = lables[i].toUpperCase();
			   }
			   return Arrays.asList(lables).contains(clumn.toUpperCase());
		   }else{
			   try {
				   index = rs.findColumn(clumn);
				} catch (Exception e) {
					return false;
				}
			   return index==-1?false:true;
		   }
		   
	}
	private  boolean hasColumn(String clumn) throws SQLException{
		 return hasColumn(_rs, clumn);
	}
	public String getString(String columnLabel) throws SQLException {
		boolean hasColumn = hasColumn(columnLabel);
		return hasColumn?_rs.getString(columnLabel):null;
	}
	public Date getDate(String columnLabel) throws SQLException {
		boolean hasColumn = hasColumn(columnLabel);
		return hasColumn?_rs.getDate(columnLabel):null;
	}
	public Date getTimestamp(String columnLabel) throws SQLException {
		boolean hasColumn = hasColumn(columnLabel);
		return hasColumn?_rs.getTimestamp(columnLabel):null;
	}
	@Deprecated
	/**
	 * use {@link WappedResultSet#getInteger} instead.
	 */
	public Integer getInt(String columnLabel) throws SQLException {
		BigDecimal decimal = getBigDecimal(columnLabel);
		return decimal!=null?decimal.intValue():null;
	}
	public Integer getInteger(String columnLabel) throws SQLException {
		BigDecimal decimal = getBigDecimal(columnLabel);
		return decimal!=null?decimal.intValue():null;
	}
	public Double getDouble(String columnLabel) throws SQLException {
		BigDecimal decimal = getBigDecimal(columnLabel);
		return decimal!=null?decimal.doubleValue():null;
	}
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		boolean hasColumn = hasColumn(columnLabel);
		return hasColumn?_rs.getBigDecimal(columnLabel):null;
	}
	
}
