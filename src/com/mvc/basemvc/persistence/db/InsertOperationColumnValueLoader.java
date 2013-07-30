/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;

import com.mvc.basemvc.persistence.meta.PropertyMapping;

/**
 * @Description 插入操作数据库字段操作类，框架内部使用,不要在框架外部调用
 * @ClassName InsertOperationColumnValueLoader
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:55:33
 */
public class InsertOperationColumnValueLoader extends ColumnValueLoader {

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
	if (columnValue == null) {
	    return false;
	}

	return true;
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
	if (propertyMapping.isTransientField()) {
	    return false;
	}

	if (propertyMapping.isComplexType()) {
	    return true;
	} else {
	    if (propertyMapping.isRelationProperty()) {
		return false;
	    }
	    if (!propertyMapping.isBaseTypeProperty()) {
		return false;
	    }
	    return true;
	}
    }

}
