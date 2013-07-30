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

import com.mvc.basemvc.persistence.meta.EntityMapping;
import com.mvc.basemvc.persistence.meta.EntityMappingFactory;
import com.mvc.basemvc.persistence.meta.PropertyMapping;

/**
 * @Description 多对多关系延时加载类，框架内部使用,不要在框架外部调用
 * @ClassName ManyToManyLoader
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:42:16
 */
public class ManyToManyLoader {

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
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    public static List<?> load(Connection conn,
	    PropertyMapping propertyMapping, List<Object> foreignColumnValues)
	    throws SQLException, ClassNotFoundException,
	    InstantiationException, IllegalAccessException {
	Class<?> refClass = propertyMapping.getReferenceClass();
	EntityMapping refEntityMapping = EntityMappingFactory
		.getEntityMapping(refClass);
	String refTable = refEntityMapping.getTableName();
	String relationTable = propertyMapping.getRelationTable();
	if (StringUtils.isBlank(refTable) || StringUtils.isBlank(relationTable))
	    return null;

	String refColumns = propertyMapping.getReferenceColumn();
	String relationColumns1 = propertyMapping.getRelationRefCol1();
	String relationColumns2 = propertyMapping.getRelationRefCol2();
	if (StringUtils.isBlank(refColumns)
		|| StringUtils.isBlank(relationColumns1)
		|| StringUtils.isBlank(relationColumns2))
	    return null;

	List<String> refColumnArr = split(refColumns);
	List<String> relationColumn1Arr = split(relationColumns1);
	List<String> relationColumn2Arr = split(relationColumns2);
	if (relationColumn1Arr.size() != foreignColumnValues.size()
		|| relationColumn2Arr.size() != refColumnArr.size()) {
	    return null;
	}

	String conditionWhere = "";
	for (int i = 0; i < relationColumn1Arr.size(); i++) {
	    conditionWhere += " and " + relationTable + "."
		    + relationColumn1Arr.get(i) + " = ?";
	}

	String relationWhere = "";
	for (int i = 0; i < refColumnArr.size(); i++) {
	    relationWhere += " and " + refTable + "." + refColumnArr.get(i)
		    + " = " + relationTable + "." + relationColumn2Arr.get(i);
	}

	String sql = "select " + refTable + ".* from " + refTable + ", "
		+ relationTable + " where " + relationWhere.substring(5)
		+ conditionWhere;
	if (!StringUtils.isBlank(propertyMapping.getReferenceOrder())) {
	    sql += " order by " + propertyMapping.getReferenceOrder();
	}

	DBOperator operator = new DBOperator(false);
	List<?> list = operator.select(conn, sql,
		foreignColumnValues.toArray(), refClass);
	if (list == null)
	    list = new ArrayList<Object>();
	return list;

    }

    /**
     * Split.
     *
     * @param str
     *            the str
     * @return the list
     */
    private static List<String> split(String str) {
	if (StringUtils.isBlank(str))
	    return new ArrayList<String>(0);

	str = str.trim();
	String[] ss = str.split(",");
	List<String> list = new ArrayList<String>(ss.length);
	for (String s : ss) {
	    s = s.trim();
	    if (s.length() > 0)
		list.add(s);
	}

	return list;
    }
}
