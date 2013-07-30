/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.beanutils.PropertyUtils;

import com.mvc.basemvc.persistence.meta.EntityMapping;
import com.mvc.basemvc.persistence.meta.EntityMappingFactory;
import com.mvc.basemvc.persistence.meta.LobType;
import com.mvc.basemvc.persistence.meta.PropertyMapping;
import com.mvc.basemvc.persistence.util.DBHelper;
import com.mvc.basemvc.persistence.util.EntityUtils;
import com.mvc.basemvc.spring.SpringContext;


/**
 * @Description 延迟加载字段的代理类 ，框架内部使用,不要在框架外部调用
 * @ClassName LobInterceptor
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:43:32
 */
public class LobInterceptor implements MethodInterceptor, Serializable {

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
	Object value = proxy.invokeSuper(entity, parameters);
	if (value != null) {
	    return value;
	}

	if (!method.getName().startsWith("get")) {
	    return null;
	}

	String propertyName = method.getName().substring(3);
	propertyName = propertyName.substring(0, 1).toLowerCase()
		+ propertyName.substring(1);

	EntityMapping entityMapping = EntityMappingFactory
		.getEntityMapping(entity.getClass());
	String tableName = entityMapping.getTableName();

	PropertyMapping propertyMapping = entityMapping
		.getPropertyMapping(propertyName);
	if (propertyMapping == null) {
	    return null;
	}

	Map<String, ColumnValue> idColumnMap = EntityUtils
		.getIdColumnMap(entity);

	String where = "";
	List<Object> whereParams = new ArrayList<Object>(idColumnMap.size());
	for (ColumnValue cv : idColumnMap.values()) {
	    where += " and " + cv.getColumnName() + " = ?";
	    whereParams.add(cv.getColumnvalue());
	}

	Connection conn = null;
	boolean bspring = true;
	Object lobValue = null;
	try {
	    conn = SpringContext.getConnection();
	    if (conn == null) {
		bspring = false;
		conn = DBHelper.getConnection();
	    }
	    lobValue = getLobValue(conn, tableName,
		    propertyMapping.getColumnName(), propertyMapping
			    .getColumnMapping().getLobType(),
		    where.substring(5), whereParams);
	    if (lobValue != null) {
		PropertyUtils.setProperty(entity, propertyName, lobValue);
	    }
	} finally {
	    if (bspring)
		SpringContext.closeConnection(conn);
	    else
		DBHelper.close(conn);
	}

	return lobValue;
    }

    /**
     * Gets the lob value.
     *
     * @param conn
     *            the conn
     * @param tableName
     *            the table name
     * @param columnName
     *            the column name
     * @param lobType
     *            the lob type
     * @param where
     *            the where
     * @param whereParams
     *            the where params
     * @return the lob value
     * @throws SQLException
     *             the sQL exception
     */
    private Object getLobValue(Connection conn, String tableName,
	    String columnName, int lobType, String where,
	    List<Object> whereParams) throws SQLException {
	Object value = null;

	PreparedStatement ps = null;
	ResultSet rs = null;

	String sql = "select " + columnName + " from " + tableName + " where "
		+ where;
	ps = conn.prepareStatement(sql);
	DBHelper.setPreparedParameters(ps, whereParams.toArray());
	rs = ps.executeQuery();

	if (rs.next()) {
	    if (lobType == LobType.BLOB) {
		value = DBHelper.getBlobValue(rs, columnName);
		if (value == null) {
		    value = new Byte[0];
		}
	    } else if (lobType == LobType.CLOB) {
		value = DBHelper.getClobValue(rs, columnName);
		if (value == null) {
		    value = "";
		}
	    }
	}

	return value;
    }
}
