/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.mvc.basemvc.persistence.meta.EntityMapping;
import com.mvc.basemvc.persistence.meta.EntityMappingFactory;
import com.mvc.basemvc.persistence.meta.PropertyMapping;
import com.mvc.basemvc.persistence.util.DBHelper;
import com.mvc.basemvc.persistence.util.EntityUtils;
import com.mvc.basemvc.spring.SpringContext;

/**
 * @Description 延时加载类的代理接口记录
 * @ClassName RelationInterceptor
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:00:06
 */
public abstract class RelationInterceptor implements MethodInterceptor,
	Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * (non-Javadoc).
     *
     * @param entity
     *            the entity
     * @param method
     *            the method
     * @param parameters
     *            the parameters
     * @param proxy
     *            the proxy
     * @return the object
     * @throws Throwable
     *             the throwable
     * @see net.sf.cglib.proxy.MethodInterceptor#intercept(java.lang.Object,
     *      java.lang.reflect.Method, java.lang.Object[],
     *      net.sf.cglib.proxy.MethodProxy)
     */
    public Object intercept(Object entity, Method method, Object[] parameters,
	    MethodProxy proxy) throws Throwable {
	Connection conn = null;
	Object value = null;
	boolean bspring = true;
	try {
	    value = proxy.invokeSuper(entity, parameters);
	    if (value != null)
		return value;

	    Class<?> entityClass = entity.getClass().getSuperclass();
	    EntityMapping entityMapping = EntityMappingFactory
		    .getEntityMapping(entityClass);

	    if (!method.getName().startsWith("get"))
		return null;

	    String propertyName = method.getName().substring(3);
	    propertyName = propertyName.substring(0, 1).toLowerCase()
		    + propertyName.substring(1);

	    PropertyMapping propertyMapping = entityMapping
		    .getPropertyMapping(propertyName);
	    if (propertyMapping == null)
		return null;
	    conn = SpringContext.getConnection();
	    if (conn == null) {
		bspring = false;
		conn = DBHelper.getConnection();
	    }
	    List<Object> joinValues = getJoinColumnValue(conn, entity,
		    entityMapping, propertyMapping.getForeignColumn());
	    if (joinValues == null || joinValues.isEmpty())
		return null;

	    value = load(conn, propertyMapping, joinValues);
	    PropertyUtils.setProperty(entity, propertyName, value);
	} catch (Throwable e) {
	    e.printStackTrace();
	    throw e;
	} finally {
	    if (bspring)
		SpringContext.closeConnection(conn);
	    else
		DBHelper.close(conn);
	}

	return value;
    }

    /**
     * Gets the join column value.
     *
     * @param conn
     *            the conn
     * @param entity
     *            the entity
     * @param mapping
     *            the mapping
     * @param foreignColumns
     *            the foreign columns
     * @return the join column value
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws SQLException
     *             the sQL exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     * @throws NoSuchMethodException
     *             the no such method exception
     */
    private List<Object> getJoinColumnValue(Connection conn, Object entity,
	    EntityMapping mapping, String foreignColumns)
	    throws ClassNotFoundException, SQLException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException, NoSuchMethodException {
	Map<String, Object> result = new HashMap<String, Object>();
	if (foreignColumns == null)
	    throw new SQLException("foreign column not set in relation");

	foreignColumns = foreignColumns.toUpperCase();
	List<String> columns = split(foreignColumns);

	Map<String, String> cm = new HashMap<String, String>();
	for (String c : columns) {
	    c = c.trim();
	    if (!StringUtils.isBlank(c)) {
		cm.put(c, "");
	    }
	}

	MapColumnValueLoader columnValueLoader = new MapColumnValueLoader();
	columnValueLoader.setColumnNameMap(cm);
	Map<String, ColumnValue> columnValueMap = columnValueLoader
		.getColumnValueMapRecursive(entity, mapping);

	ColumnValue columnValue = null;
	List<String> noValueColumns = new ArrayList<String>();

	for (String columnName : cm.keySet()) {
	    columnValue = columnValueMap.get(columnName);
	    if (columnValue == null) {
		noValueColumns.add(columnName);
	    } else {
		if (columnValue.getColumnvalue() == null)
		    throw new SQLException(
			    "foreign key column value is null in relation");
		result.put(columnName, columnValue.getColumnvalue());
	    }
	}

	if (!noValueColumns.isEmpty()) {
	    // get value
	    Map<String, ColumnValue> idColumnValues = EntityUtils
		    .getIdColumnMap(entity);
	    String where = "";
	    List<Object> whereParams = new ArrayList<Object>();
	    for (ColumnValue cv : idColumnValues.values()) {
		where += " and " + cv.getColumnName() + " = ?";
		whereParams.add(cv.getColumnvalue());
	    }

	    String sql = "";
	    for (String columnName : noValueColumns) {
		sql += "," + columnName;
	    }

	    sql = "select " + sql.substring(1) + " from "
		    + mapping.getTableName() + " where " + where.substring(5);

	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    ps = conn.prepareStatement(sql);
	    DBHelper.setPreparedParameters(ps, whereParams.toArray());
	    rs = ps.executeQuery();

	    if (rs.next()) {
		Map<String, Object> row = DBHelper.getResultsetRow(rs);
		result.putAll(row);
	    }
	}

	List<Object> columnValueList = new ArrayList<Object>();
	Object value = null;
	for (String c : columns) {
	    value = result.get(c);
	    if (value == null)
		throw new SQLException(
			"foreign key column value is null in relation");
	    columnValueList.add(value);
	}

	return columnValueList;

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

    /**
     * Load.
     *
     * @param conn
     *            the conn
     * @param propertyMapping
     *            the property mapping
     * @param foreignColumnValues
     *            the foreign column values
     * @return the object
     * @throws SQLException
     *             the sQL exception
     */
    public abstract Object load(Connection conn,
	    PropertyMapping propertyMapping, List<Object> foreignColumnValues)
	    throws SQLException;
}
