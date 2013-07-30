/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;

import java.util.HashMap;
import java.util.Map;

import com.mvc.basemvc.persistence.meta.PropertyMapping;


/**
 * @Description 数据库字段Map表示方式操作类，框架内部使用,不要在框架外部调用
 * @ClassName MapColumnValueLoader
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:41:57
 */
public class MapColumnValueLoader extends ColumnValueLoader {

    /** The column name map. */
    Map<String, String> columnNameMap = new HashMap<String, String>();

    /**
     * (non-Javadoc).
     *
     * @param columnValue
     *            the column value
     * @return true, if successful
     * @see com.koolearn.persistence.db.ColumnValueLoader#filter(com.koolearn.persistence.db.ColumnValue)
     */
    @Override
    public boolean filter(ColumnValue columnValue) {
	String columnName = columnValue.getColumnName().toUpperCase();
	if (columnNameMap.containsKey(columnName)
		&& columnValue.getColumnvalue() != null)
	    return true;
	return false;
    }

    /**
     * (non-Javadoc).
     *
     * @param propertyMapping
     *            the property mapping
     * @return true, if successful
     * @see com.koolearn.persistence.db.ColumnValueLoader#filter(com.koolearn.persistence.meta.PropertyMapping)
     */
    @Override
    public boolean filter(PropertyMapping propertyMapping) {
	if (propertyMapping.isComplexType()) {
	    return true;
	} else {
	    if (propertyMapping.isTransientField()
		    || propertyMapping.isRelationProperty()
		    || !propertyMapping.isBaseTypeProperty())
		return false;
	    return true;
	}
    }

    /**
     * Adds the column name.
     *
     * @param columnName
     *            the column name
     */
    public void addColumnName(String columnName) {
	columnNameMap.put(columnName, "");
    }

    /**
     * Gets the column name map.
     *
     * @return the column name map
     */
    public Map<String, String> getColumnNameMap() {
	return columnNameMap;
    }

    /**
     * Sets the column name map.
     *
     * @param columnNameMap
     *            the column name map
     */
    public void setColumnNameMap(Map<String, String> columnNameMap) {
	this.columnNameMap = columnNameMap;
    }

}
