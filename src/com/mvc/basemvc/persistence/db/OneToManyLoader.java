/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mvc.basemvc.persistence.meta.PropertyMapping;
import com.mvc.basemvc.persistence.meta.TableHandler;


/**
 * @Description 一对多关系延时加载类，框架内部使用,不要在框架外部调用
 * @ClassName OneToManyLoader
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:40:26
 */
public class OneToManyLoader {

    /**
     * Load.
     *
     * @param conn
     *            the conn
     * @param propertyMapping
     *            the property mapping
     * @param foreignColumnValues
     *            the foreign column values
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    public static List<?> load(Connection conn,
	    PropertyMapping propertyMapping, List<Object> foreignColumnValues)
	    throws SQLException {
	Class<?> refClass = propertyMapping.getReferenceClass();
	String refColumn = propertyMapping.getReferenceColumn();

	String sql = "";
	String[] ss = refColumn.split(",");
	for (String s : ss) {
	    s = s.trim();
	    if (!StringUtils.isBlank(s))
		sql += " and " + s + " = ?";
	}

	DBOperator operator = new DBOperator(false);
	String refTable = TableHandler.getTableName(refClass);
	sql = "select * from " + refTable + " where " + sql.substring(5);
	List<?> list = operator.select(conn, sql,
		foreignColumnValues.toArray(), refClass);
	if (list == null)
	    list = new ArrayList<Object>();
	return list;
    }
}
