package com.fantasy.app.core.component.db.orm;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

import com.fantasy.app.core.base.ModuleFeatureBean;
import com.fantasy.app.core.base.log.LogType;
import com.fantasy.app.core.component.log.Log;


/**
 * 对继承了ModuleFeatureBean的类查询结果的自动封装
 * @see cn.gov.mwr.si.core.base.ModuleFeatureBean
 * 如何使用eg: new ModuleFeatureBeanRowWapper<TeacherVO>(TeacherVO.class)
 * @日期：2012-12-14下午11:18:40
 * @作者：公众号：18岁fantasy
 */
public class ModuleFeatureBeanRowWapper<T extends ModuleFeatureBean> implements RowMapper<T>{

	private static Logger logger = Log.getLogger(LogType.DB);
	
	private Class<T> mappedClass;
	public   ModuleFeatureBeanRowWapper(Class<T> mappedClass){
		this.mappedClass = mappedClass;
	}
	@Override
	public T mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		Assert.state(this.mappedClass != null, "Mapped class was not specified");
		T featureBean = null;
		try {
			featureBean = BeanUtils.instantiate(this.mappedClass);
		} catch (Exception e) {
			throw new RuntimeException("实例化"+this.mappedClass+"出错，请确保有public并且无参数的构造方法，并且不能是abstract类型",e);
		} 
		Map<String,ColumnInfo> columnInfos = ModuleParser.getModuleColumn(mappedClass);
		
		
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(featureBean);

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();

		for (int index = 1; index <= columnCount; index++) {
			String column = JdbcUtils.lookupColumnName(rsmd, index);
			
			ColumnInfo pd = columnInfos.get(column.replaceAll(" ", "").toLowerCase());
			if (pd != null) {
				try {
					Object value = JdbcUtils.getResultSetValue(rs, index, pd.getField().getType());
						logger.debug("映射 column '" + column + "' 到 property '" +
								pd.getColumnName() + "' of type " + pd.getField().getType());
					try {
						bw.setPropertyValue(pd.getField().getName(), value);
					}
					catch (TypeMismatchException e) {
						if (value == null) {
							logger.debug("Intercepted TypeMismatchException for row " + rowNum +
									" and column '" + column + "' with value " + value +
									" when setting property '" + pd.getField().getName() + "' of type " + pd.getField().getType() +
									" on object: " + featureBean);
						}
						else {
							throw e;
						}
					}
				}
				catch (NotWritablePropertyException ex) {
					throw new DataRetrievalFailureException(
							"Unable to map column " + column + " to property " + pd.getField().getName(), ex);
				}
			}
		}
		return featureBean;
	}


}
