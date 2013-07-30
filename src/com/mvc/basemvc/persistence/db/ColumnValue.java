/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;

import com.mvc.basemvc.persistence.meta.PropertyMapping;

/**
 * @Description 数据库字段操作类，框架内部使用
 * @ClassName ColumnValue
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:59:57
 */
public class ColumnValue {

    /** The column name. */
    private String columnName;

    /** The columnvalue. */
    private Object columnvalue;

    /** The property mapping. */
    private PropertyMapping propertyMapping;

    /** The is primary key. */
    private boolean isPrimaryKey = false;

    /**
     * Gets the column name.
     *
     * @return the column name
     */
    public String getColumnName() {
	return columnName;
    }

    /**
     * Sets the column name.
     *
     * @param columnName
     *            the new column name
     */
    public void setColumnName(String columnName) {
	this.columnName = columnName;
    }

    /**
     * Gets the columnvalue.
     *
     * @return the columnvalue
     */
    public Object getColumnvalue() {
	return columnvalue;
    }

    /**
     * Sets the columnvalue.
     *
     * @param columnvalue
     *            the new columnvalue
     */
    public void setColumnvalue(Object columnvalue) {
	this.columnvalue = columnvalue;
    }

    /**
     * Gets the property mapping.
     *
     * @return the property mapping
     */
    public PropertyMapping getPropertyMapping() {
	return propertyMapping;
    }

    /**
     * Sets the property mapping.
     *
     * @param propertyMapping
     *            the new property mapping
     */
    public void setPropertyMapping(PropertyMapping propertyMapping) {
	this.propertyMapping = propertyMapping;
    }

    /**
     * Checks if is primary key.
     *
     * @return true, if is primary key
     */
    public boolean isPrimaryKey() {
	return isPrimaryKey;
    }

    /**
     * Sets the primary key.
     *
     * @param isPrimaryKey
     *            the new primary key
     */
    public void setPrimaryKey(boolean isPrimaryKey) {
	this.isPrimaryKey = isPrimaryKey;
    }

}
