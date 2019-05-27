package com.fantasy.app.core.component.db.orm;

import java.lang.reflect.Field;

import com.fantasy.app.core.annotation.WsdColumn;

/**
 * 解析module后的信息
 * @日期：2012-12-14下午11:17:40
 * @作者：公众号：18岁fantasy
 */
public class ColumnInfo {

	
	public ColumnInfo(Field field, WsdColumn.TYPE type, String columnName,
			boolean nullable,boolean isId,boolean uuid) {
		super();
		this.field = field;
		this.type = type;
		this.columnName = columnName;
		this.nullable = nullable;
		this.isId = isId;
		this.uuid = uuid;
	}
	private Field field;
	private WsdColumn.TYPE type;
	private String columnName;
	private boolean nullable;
	private boolean uuid;
	private boolean isId;
	
	private boolean isDbColumn = true;
	
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	public WsdColumn.TYPE getType() {
		return type;
	}
	public void setType(WsdColumn.TYPE type) {
		this.type = type;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public boolean isNullable() {
		return nullable;
	}
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	
	public boolean isId() {
		return isId;
	}
	public void setId(boolean isId) {
		this.isId = isId;
	}
	
	public boolean isDbColumn() {
		return isDbColumn;
	}
	public void setDbColumn(boolean isDbColumn) {
		this.isDbColumn = isDbColumn;
	}

	public boolean isUuid() {
		return uuid;
	}

	public void setUuid(boolean uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "ColumnInfo{" +
				"field=" + field +
				", type=" + type +
				", columnName='" + columnName + '\'' +
				", nullable=" + nullable +
				", uuid=" + uuid +
				", isId=" + isId +
				", isDbColumn=" + isDbColumn +
				'}';
	}
}
