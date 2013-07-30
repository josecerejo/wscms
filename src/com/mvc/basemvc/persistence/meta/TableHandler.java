/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.meta;

import org.apache.commons.lang.StringUtils;

/**
 * @Description 获取表名辅助类
 * @ClassName TableHandler
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:11:39
 */
public class TableHandler {

    /**
     * Gets the table name.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return the table name
     */
    public static <T> String getTableName(Class<T> clazz) {
	String tableName = null;
	if (clazz.isAnnotationPresent(Table.class)) {
	    Table table = (Table) clazz.getAnnotation(Table.class);
	    tableName = table.name();
	    if (StringUtils.isBlank(tableName)) {
		tableName = getDefaultTableName(clazz);
	    } else {
		tableName = tableName.toUpperCase();
	    }
	} else {
	    tableName = getDefaultTableName(clazz);
	}

	return tableName;
    }

    /**
     * Gets the default table name.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return the default table name
     */
    private static <T> String getDefaultTableName(Class<T> clazz) {
	String className = clazz.getName();
	if (className.indexOf(".") > 0)
	    className = className.substring(className.lastIndexOf(".") + 1,
		    className.length());
	className = className.toUpperCase();
	return className;
    }

    // public static void main( String[] args )
    // {
    // String tableName = TableHandler.getTableName(IndexLog.class);
    // System.out.println( tableName );
    // }
}
