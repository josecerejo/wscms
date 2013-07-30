/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.mvc.basemvc.persistence.meta.ColumnMapping;
import com.mvc.basemvc.persistence.meta.EntityMapping;
import com.mvc.basemvc.persistence.meta.EntityMappingFactory;
import com.mvc.basemvc.persistence.meta.GenerationType;
import com.mvc.basemvc.persistence.meta.LobType;
import com.mvc.basemvc.persistence.meta.ObjectMapping;
import com.mvc.basemvc.persistence.meta.PropertyMapping;
import com.mvc.basemvc.persistence.meta.RelationType;
import com.mvc.basemvc.persistence.meta.TableHandler;
import com.mvc.basemvc.persistence.util.BaseUtils;
import com.mvc.basemvc.persistence.util.DBHelper;
import com.mvc.basemvc.persistence.util.EntityUtils;
import com.mvc.basemvc.util.ListPager;

/**
 * @Description 数据库操作的封装类，此类框架内部使用
 * @ClassName DBOperator
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:59:14
 */
public class DBOperator {

    /** The logger. */
    private static Logger logger = Logger.getLogger(DBOperator.class);

    /** The nest load. */
    private boolean nestLoad = true;

    /** The db type. */
    private int dbType = DBOption.DB_ORACLE;

    /** The BATCHSIZE. */
    private static int BATCHSIZE = 30;

    /**
     * Instantiates a new dB operator.
     *
     * @param nest
     *            the nest
     */
    public DBOperator(boolean nest) {
	this.nestLoad = nest;
    }

    /**
     * Instantiates a new dB operator.
     *
     * @param nest
     *            the nest
     * @param dbType
     *            the db type
     */
    public DBOperator(boolean nest, int dbType) {
	this.nestLoad = nest;
	this.dbType = dbType;
    }

    /**
     * Select one.
     *
     * @param <T>
     *            the generic type
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param clazz
     *            the clazz
     * @return the t
     * @throws SQLException
     *             the sQL exception
     */
    public <T> T selectOne(Connection conn, String sql, Class<T> clazz)
	    throws SQLException {
	return selectOne(conn, sql, new Object[0], clazz);
    }

    /**
     * Select one.
     *
     * @param <T>
     *            the generic type
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param clazz
     *            the clazz
     * @return the t
     * @throws SQLException
     *             the sQL exception
     */
    public <T> T selectOne(Connection conn, String sql, Object[] parameters,
	    Class<T> clazz) throws SQLException {
	List<T> list = select(conn, sql, parameters, clazz);
	if (list == null || list.isEmpty()) {
	    return null;
	}
	return list.get(0);
    }

    /**
     * Select.
     *
     * @param <T>
     *            the generic type
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param clazz
     *            the clazz
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    public <T> List<T> select(Connection conn, String sql, Class<T> clazz)
	    throws SQLException {
	return select(conn, sql, new Object[0], clazz);
    }

    /**
     * Select.
     *
     * @param <T>
     *            the generic type
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param clazz
     *            the clazz
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    public <T> List<T> select(Connection conn, String sql, Object[] parameters,
	    Class<T> clazz) throws SQLException {
	return selectPage(conn, sql, parameters, clazz, null);
    }

    /**
     * Select page.
     *
     * @param <T>
     *            the generic type
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param clazz
     *            the clazz
     * @param pager
     *            the pager
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    public <T> List<T> selectPage(Connection conn, String sql, Class<T> clazz,
	    ListPager pager) throws SQLException {
	return selectPage(conn, sql, new Object[0], clazz, pager);
    }

    /**
     * Select all.
     *
     * @param <T>
     *            the generic type
     * @param conn
     *            the conn
     * @param orderby
     *            the orderby
     * @param clazz
     *            the clazz
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    public <T> List<T> selectAll(Connection conn, String orderby, Class<T> clazz)
	    throws SQLException {
	String tableName = TableHandler.getTableName(clazz);
	String sql = "select * from " + tableName + " order by " + orderby;
	return select(conn, sql, clazz);
    }

    /**
     * Select all page.
     *
     * @param <T>
     *            the generic type
     * @param conn
     *            the conn
     * @param orderby
     *            the orderby
     * @param clazz
     *            the clazz
     * @param pager
     *            the pager
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    public <T> List<T> selectAllPage(Connection conn, String orderby,
	    Class<T> clazz, ListPager pager) throws SQLException {
	String tableName = TableHandler.getTableName(clazz);
	String sql = "select * from " + tableName + " order by " + orderby;
	return selectPage(conn, sql, clazz, pager);
    }

    /**
     * Select by id.
     *
     * @param <T>
     *            the generic type
     * @param conn
     *            the conn
     * @param id
     *            the id
     * @param clazz
     *            the clazz
     * @return the t
     * @throws SQLException
     *             the sQL exception
     */
    public <T> T selectById(Connection conn, Object id, Class<T> clazz)
	    throws SQLException {
	List<T> recordList = null;
	try {
	    EntityMapping mapping = EntityMappingFactory
		    .getEntityMapping(clazz);
	    String table = mapping.getTableName();

	    PropertyMapping idMapping = mapping.getIdMapping();
	    String where = "";
	    List<Object> whereParams = new ArrayList<Object>();

	    if (!idMapping.isComplexType()) {
		where = idMapping.getColumnName() + "=?";
		whereParams.add(id);
	    } else {
		ColumnValueLoader columnValueLoader = new UpdateOperationColumnValueLoader();
		ObjectMapping om = EntityMappingFactory.getObjectMapping(id
			.getClass());
		Map<String, ColumnValue> idColumnValues = columnValueLoader
			.getColumnValueMapRecursive(id, om);
		Object object = null;
		for (ColumnValue cv : idColumnValues.values()) {
		    where += " and " + cv.getColumnName() + " = ?";

		    object = cv.getColumnvalue();
		    if (object == null) {
			throw new SQLException(
				"primary key column's value cannot be null");
		    }
		    whereParams.add(cv.getColumnvalue());
		}
		where = where.substring(5);
	    }

	    String sql = "select * from " + table + " where " + where;
	    recordList = select(conn, sql, whereParams.toArray(), clazz);
	} catch (Exception e) {
	    logger.error("Exception", e);
	    throw new SQLException(e.getMessage());
	}

	if (recordList == null || recordList.isEmpty()) {
	    return null;
	} else {
	    return recordList.get(0);
	}
    }

    /**
     * Select page.
     *
     * @param <T>
     *            the generic type
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param clazz
     *            the clazz
     * @param pager
     *            the pager
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    public <T> List<T> selectPage(Connection conn, String sql,
	    Object[] parameters, Class<T> clazz, ListPager pager)
	    throws SQLException {

	if (pager != null) {
	    selectTotalRowcount(conn, sql, parameters, pager);
	    sql = pager.buildPagedSQL(dbType, sql);
	}

	List<T> recordList = new ArrayList<T>();
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    ps = conn.prepareStatement(sql);
	    DBHelper.setPreparedParameters(ps, parameters);
	    rs = ps.executeQuery();

	    ResultSetMetaData meta = rs.getMetaData();

	    while (rs.next()) {
		T record = createEntityFromRS(conn, rs, meta, clazz);
		recordList.add(record);
	    }
	} catch (SQLException e) {
	    logger.error("SQLException", e);
	    throw e;
	} catch (Exception e) {
	    logger.error("Exception", e);
	    throw new SQLException(e.getMessage());
	} finally {
	    DBHelper.close(ps, rs);
	}

	return recordList;
    }

    /**
     * Select page.
     *
     * @param <T>
     *            the generic type
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param clazz
     *            the clazz
     * @param from
     *            the from
     * @param to
     *            the to
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    public <T> List<T> selectPage(Connection conn, String sql,
	    Object[] parameters, Class<T> clazz, long from, long to)
	    throws SQLException {
	String newsql = ListPager.buildPagedSQLOracle(sql, from, to);
	return select(conn, newsql, parameters, clazz);
    }

    /**
     * Creates the entity from rs.
     *
     * @param <T>
     *            the generic type
     * @param conn
     *            the conn
     * @param rs
     *            the rs
     * @param meta
     *            the meta
     * @param clazz
     *            the clazz
     * @return the t
     * @throws SQLException
     *             the sQL exception
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     * @throws NoSuchMethodException
     *             the no such method exception
     */
    @SuppressWarnings("unchecked")
    private <T> T createEntityFromRS(Connection conn, ResultSet rs,
	    ResultSetMetaData meta, Class<T> clazz) throws SQLException,
	    ClassNotFoundException, InstantiationException,
	    IllegalAccessException, InvocationTargetException,
	    NoSuchMethodException {
	ObjectMapping mapping = EntityMappingFactory.getObjectMapping(clazz);
	List<PropertyMapping> propertyMappingList = null;

	int colCount = meta.getColumnCount();
	String colName = null;
	String alias = null;
	Object value = null;

	ColumnMapping columnMapping = null;
	T record = null;
	BeanMap beanMap = null;

	String propertyName = null;
	Method method = null;
	Class<?>[] methodParaTypes = null;

	int columnTypeName = 0;
	List<Object> joinValues = null;
	if (clazz.getName().equals("java.lang.Integer")) {
	    record = (T) (Integer) rs.getInt(1);
	    return record;

	} else if (clazz.getName().equals("java.lang.Double")) {
	    record = (T) (Double) rs.getDouble(1);
	    return record;

	} else if (clazz.getName().equals("java.lang.Long")) {
	    record = (T) (Long) rs.getLong(1);
	    return record;

	} else if (clazz.getName().equals("java.lang.Float")) {
	    record = (T) (Float) rs.getFloat(1);
	    return record;

	} else if (clazz.getName().equals("java.lang.Byte")) {
	    record = (T) (Byte) rs.getByte(1);
	    return record;

	} else if (clazz.getName().equals("java.lang.String")) {
	    record = (T) (String) rs.getString(1);
	    return record;

	} else if (clazz.getName().equals("java.lang.Short")) {
	    record = (T) (Short) rs.getShort(1);
	    return record;

	} else {
	    record = (T) EntityFactory.createEntity(mapping);

	}
	beanMap = new BeanMap(record);
	Set<String> aliasProperties = new HashSet<String>();

	for (int i = 1; i <= colCount; i++) {
	    colName = meta.getColumnName(i);
	    columnTypeName = meta.getColumnType(i);

	    propertyMappingList = mapping.getProperyMappingByColumnName(colName
		    .toUpperCase());
	    if (propertyMappingList == null || propertyMappingList.isEmpty()) {
		continue;
	    }

	    for (PropertyMapping propertyMapping : propertyMappingList) {
		columnMapping = propertyMapping.getColumnMapping();
		propertyName = propertyMapping.getPropertyName();

		if (aliasProperties.contains(propertyName)) {
		    continue;
		}

		method = null;
		if (propertyName != null) {
		    method = beanMap.getWriteMethod(propertyName);
		}
		if (propertyName == null || method == null) {
		    continue;
		}

		methodParaTypes = method.getParameterTypes();
		if (methodParaTypes == null || methodParaTypes.length != 1) {
		    continue;
		}

		if (columnMapping.getLobType() == LobType.CLOB) {
		    if (columnTypeName == Types.CLOB
			    && !columnMapping.isLobLazy()) {
			value = DBHelper.getClobValue(rs, colName);
		    } else {
			value = null;
		    }
		} else if (columnMapping.getLobType() == LobType.BLOB) {
		    if (columnTypeName == Types.BLOB
			    && !columnMapping.isLobLazy()) {
			value = DBHelper.getBlobValue(rs, colName);
		    } else {
			value = null;
		    }
		} else if (BaseUtils.isBaseType(methodParaTypes[0])) {
		    // 简单数据类型，或者基本对象类型
		    value = DBHelper.getResultsetValue(rs, colName,
			    methodParaTypes[0]);
		} else {
		    // if ( !nestLoad ) continue;

		    // 对象类型，检查是否是关联对象
		    if (propertyMapping.getReloationType() == RelationType.oneToOne) {
			// 延迟加载
			if (propertyMapping.isLoadLazy()) {
			    value = null;
			} else {
			    // TODO: foreign column的值从rs中必须都可以得到
			    joinValues = getJoinColumnValue(rs,
				    propertyMapping.getForeignColumn());
			    value = OneToOneLoader.load(conn, propertyMapping,
				    joinValues);
			}
		    } else if (propertyMapping.getReloationType() == RelationType.oneToMany) {
			if (propertyMapping.isLoadLazy()) {
			    value = null;
			} else {
			    // TODO: foreign column的值从rs中必须都可以得到
			    joinValues = getJoinColumnValue(rs,
				    propertyMapping.getForeignColumn());
			    value = OneToManyLoader.load(conn, propertyMapping,
				    joinValues);
			}
		    } else if (propertyMapping.getReloationType() == RelationType.manyToMany) {
			if (propertyMapping.isLoadLazy()) {
			    value = null;
			} else {
			    // TODO: foreign column的值从rs中必须都可以得到
			    joinValues = getJoinColumnValue(rs,
				    propertyMapping.getForeignColumn());
			    value = ManyToManyLoader.load(conn,
				    propertyMapping, joinValues);
			}
		    } else {
			continue;
		    }
		}

		PropertyUtils.setProperty(record, propertyName, value);

		alias = columnMapping.getAlias();
		if (alias != null && alias.length() > 0
			&& colName.equalsIgnoreCase(alias)) {
		    aliasProperties.add(propertyName);
		}
	    }
	}

	// create complex properties
	List<PropertyMapping> complexPropertyMappingList = mapping
		.getComplexPropertyMappingList();
	Object propertyObject = null;

	for (PropertyMapping pm : complexPropertyMappingList) {
	    propertyName = pm.getPropertyName();
	    propertyObject = createEntityFromRS(conn, rs, meta,
		    pm.getPropertyClass());
	    PropertyUtils.setProperty(record, propertyName, propertyObject);
	}

	return record;
    }

    /**
     * Gets the join column value.
     *
     * @param rs
     *            the rs
     * @param foreignColumns
     *            the foreign columns
     * @return the join column value
     * @throws SQLException
     *             the sQL exception
     */
    private List<Object> getJoinColumnValue(ResultSet rs, String foreignColumns)
	    throws SQLException {
	if (StringUtils.isBlank(foreignColumns)) {
	    return null;
	}

	foreignColumns = foreignColumns.trim();
	List<Object> joinValues = new ArrayList<Object>();

	String[] ss = foreignColumns.split(",");
	Object value = null;
	for (String s : ss) {
	    if (StringUtils.isBlank(s)) {
		continue;
	    }
	    value = DBHelper.getResultsetValue(rs, s);
	    if (value == null) {
		throw new SQLException("join column value cannot be null");
	    }
	    joinValues.add(value);
	}

	if (joinValues.isEmpty()) {
	    throw new SQLException("join column value cannot be null");
	}
	return joinValues;
    }

    /**
     * Select total rowcount.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param pager
     *            the pager
     * @return the long
     * @throws SQLException
     *             the sQL exception
     */
    public static long selectTotalRowcount(Connection conn, String sql,
	    Object[] parameters, ListPager pager) throws SQLException {
	long rowcount = selectTotalRowCount(conn, sql, parameters);
	pager.setTotalRows(rowcount);
	return rowcount;
    }

    /**
     * Select total row count.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @return the long
     * @throws SQLException
     *             the sQL exception
     */
    public static long selectTotalRowCount(Connection conn, String sql,
	    Object[] parameters) throws SQLException {
	PreparedStatement ps = null;
	ResultSet rs = null;
	long rowcount = 0;

	try {
	    String rowCountSQL = ListPager.buildRowCountSQL(sql);
	    ps = conn.prepareStatement(rowCountSQL);
	    DBHelper.setPreparedParameters(ps, parameters);
	    rs = ps.executeQuery();

	    if (rs.next()) {
		rowcount = rs.getLong(1);
	    }
	} catch (Exception e) {
	    logger.error("Exception", e);
	    throw new SQLException(e.getMessage());
	} finally {
	    DBHelper.close(ps, rs);
	}

	return rowcount;
    }

    /**
     * Execute query.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @return the list
     */
    public List<Map<String, Object>> executeQuery(Connection conn, String sql,
	    Object[] parameters) {
	return executeQueryPage(conn, sql, parameters, null);
    }

    /**
     * Execute query page.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param from
     *            the from
     * @param to
     *            the to
     * @return the list
     */
    public List<Map<String, Object>> executeQueryPage(Connection conn,
	    String sql, Object[] parameters, int from, int to) {
	String newsql = new ListPager().buildPagedSQL(sql);
	return executeQuery(conn, newsql, parameters);
    }

    /**
     * Query page.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param pager
     *            the pager
     * @return the list
     */

    public List<Map<String, Object>> queryPage(Connection conn, String sql,
	    Object[] parameters, ListPager pager) {
	return executeQueryPage(conn, sql, parameters, pager, this.dbType);
    }

    /**
     * Execute query page.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param pager
     *            the pager
     * @return the list
     */
    public List<Map<String, Object>> executeQueryPage(Connection conn,
	    String sql, Object[] parameters, ListPager pager) {
	return executeQueryPage(conn, sql, parameters, pager,
		DBOption.DB_ORACLE);
    }

    /**
     * Execute query page.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param pager
     *            the pager
     * @param dbType
     *            the db type
     * @return the list
     */
    public List<Map<String, Object>> executeQueryPage(Connection conn,
	    String sql, Object[] parameters, ListPager pager, int dbType) {
	PreparedStatement ps = null;
	ResultSet rs = null;
	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

	try {
	    if (pager != null) {
		selectTotalRowcount(conn, sql, parameters, pager);
		//pager.retrieveTotalRows(conn, sql, false);
		sql = pager.buildPagedSQL(dbType, sql);
	    }

	    ps = conn.prepareStatement(sql);
	    DBHelper.setPreparedParameters(ps, parameters);
	    rs = ps.executeQuery();

	    Map<String, Object> record = null;
	    while (rs.next()) {
		record = DBHelper.getResultsetRow(rs);
		result.add(record);
	    }
	} catch (SQLException e) {
	    logger.error("SQLException", e);
	} finally {
	    DBHelper.close(ps, rs);
	}

	return result;
    }

    /**
     * Execute query page.
     *
     * @param <T>
     *            the generic type
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param pager
     *            the pager
     * @param callback
     *            the callback
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    public <T> List<T> executeQueryPage(Connection conn, String sql,
	    Object[] parameters, ListPager pager,
	    QueryResultCallback<T> callback) throws SQLException {
	PreparedStatement ps = null;
	ResultSet rs = null;
	List<T> result = new ArrayList<T>();

	try {
	    if (pager != null) {
		selectTotalRowcount(conn, sql, parameters, pager);
		//pager.retrieveTotalRows(conn, sql, false);
		sql = pager.buildPagedSQL(dbType, sql);
	    }

	    ps = conn.prepareStatement(sql);
	    DBHelper.setPreparedParameters(ps, parameters);
	    rs = ps.executeQuery();

	    while (rs.next()) {
		T t = callback.getRow(rs);
		result.add(t);
	    }
	} catch (SQLException e) {
	    logger.error("SQLException", e);
	    throw e;
	} finally {
	    DBHelper.close(ps, rs);
	}

	return result;
    }

    /**
     * Execute query page no case map.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param pager
     *            the pager
     * @param dbType
     *            the db type
     * @return the list
     */
    private List<NoCaseMap<String, Object>> executeQueryPageNoCaseMap(
	    Connection conn, String sql, Object[] parameters, ListPager pager,
	    int dbType) {
	PreparedStatement ps = null;
	ResultSet rs = null;
	List<NoCaseMap<String, Object>> result = new ArrayList<NoCaseMap<String, Object>>();

	try {
	    if (pager != null) {
		selectTotalRowcount(conn, sql, parameters, pager);
		//pager.retrieveTotalRows(conn, sql, false);
		sql = pager.buildPagedSQL(dbType, sql);
	    }

	    ps = conn.prepareStatement(sql);
	    DBHelper.setPreparedParameters(ps, parameters);
	    rs = ps.executeQuery();

	    NoCaseMap<String, Object> record = null;
	    while (rs.next()) {
		record = DBHelper.getResultsetRowNoCase(rs);
		result.add(record);
	    }
	} catch (SQLException e) {
	    logger.error("SQLException", e);
	} finally {
	    DBHelper.close(ps, rs);
	}

	return result;
    }

    /**
     * Execute update batch.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parametersal
     *            the parametersal
     * @param batchSize
     *            the batch size
     * @throws SQLException
     *             the sQL exception
     */
    public void executeUpdateBatch(Connection conn, String sql,
	    ArrayList<Object[]> parametersal, int batchSize)
	    throws SQLException {

	if (batchSize > BATCHSIZE || batchSize < 0)
	    batchSize = BATCHSIZE;
	PreparedStatement ps = null;
	int batch = 0;
	try {
	    ps = conn.prepareStatement(sql);
	    for (Object[] parameters : parametersal) {
		DBHelper.setPreparedParameters(ps, parameters);
		ps.addBatch();

		batch++;
		if (batch == batchSize) {
		    ps.executeBatch();
		    ps.clearBatch();
		    batch = 0;
		}
	    }
	    if (batch > 0) {
		ps.executeBatch();
		ps.clearBatch();
	    }
	} finally {
	    DBHelper.close(ps);
	}
    }

    /**
     * Execute update batch.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parametersal
     *            the parametersal
     * @throws SQLException
     *             the sQL exception
     */
    public void executeUpdateBatch(Connection conn, String sql,
	    ArrayList<Object[]> parametersal) throws SQLException {

	PreparedStatement ps = null;
	int batch = 0;
	try {
	    ps = conn.prepareStatement(sql);
	    for (Object[] parameters : parametersal) {
		DBHelper.setPreparedParameters(ps, parameters);
		ps.addBatch();

		batch++;
		if (batch == BATCHSIZE) {
		    ps.executeBatch();
		    ps.clearBatch();
		    batch = 0;
		}
	    }
	    if (batch > 0) {
		ps.executeBatch();
		ps.clearBatch();
	    }
	} finally {
	    DBHelper.close(ps);
	}
    }

    /**
     * Execute update.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @throws SQLException
     *             the sQL exception
     */
    public void executeUpdate(Connection conn, String sql, Object[] parameters)
	    throws SQLException {

	PreparedStatement ps = null;
	try {
	    ps = conn.prepareStatement(sql);
	    DBHelper.setPreparedParameters(ps, parameters);
	    ps.executeUpdate();
	} finally {
	    DBHelper.close(ps);
	}
    }

    /**
     * Execute update.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @throws SQLException
     *             the sQL exception
     */
    public void executeUpdate(Connection conn, String sql) throws SQLException {
	executeUpdate(conn, sql, new Object[0]);
    }

    /**
     * Delete.
     *
     * @param conn
     *            the conn
     * @param entity
     *            the entity
     * @throws SQLException
     *             the sQL exception
     */
    public void delete(Connection conn, Object entity) throws SQLException {
	try {
	    EntityMapping mapping = EntityMappingFactory
		    .getEntityMapping(entity.getClass());
	    String tableName = mapping.getTableName();

	    Map<String, ColumnValue> idColumnMap = EntityUtils
		    .getIdColumnMap(entity);

	    String where = "";
	    List<Object> whereParams = new ArrayList<Object>(idColumnMap.size());
	    for (ColumnValue cv : idColumnMap.values()) {
		where += " and " + cv.getColumnName() + " = ?";
		whereParams.add(cv.getColumnvalue());
	    }

	    String sql = "delete from " + tableName + " where "
		    + where.substring(5);
	    executeUpdate(conn, sql, whereParams.toArray());
	} catch (SQLException e) {
	    throw e;
	} catch (Exception e) {
	    throw new SQLException(e.getMessage());
	}
    }

    /**
     * Delete by id.
     *
     * @param <T>
     *            the generic type
     * @param conn
     *            the conn
     * @param id
     *            the id
     * @param clazz
     *            the clazz
     * @throws SQLException
     *             the sQL exception
     */
    public <T> void deleteById(Connection conn, Object id, Class<T> clazz)
	    throws SQLException {
	try {
	    if (id == null) {
		throw new SQLException(
			"primary key value is null in deleteById");
	    }

	    EntityMapping mapping = EntityMappingFactory
		    .getEntityMapping(clazz);
	    String tableName = mapping.getTableName();

	    PropertyMapping idMapping = mapping.getIdMapping();
	    if (!idMapping.isComplexType()) {
		String sql = "delete from " + tableName + " where "
			+ idMapping.getColumnName() + " = ?";
		executeUpdate(conn, sql, new Object[] { id });
	    } else {
		if (!id.getClass().getName()
			.equals(idMapping.getPropertyClass().getName())) {
		    throw new SQLException(
			    "cast class exception for primary key");
		}

		ColumnValueLoader columnValueLoader = new UpdateOperationColumnValueLoader();
		ObjectMapping om = idMapping.getObjectMapping();
		Map<String, ColumnValue> idColumnValues = columnValueLoader
			.getColumnValueMapRecursive(id, om);
		String where = "";
		List<Object> whereParams = new ArrayList<Object>(
			idColumnValues.size());
		Object object = null;
		for (ColumnValue cv : idColumnValues.values()) {
		    where += " and " + cv.getColumnName() + " = ?";

		    object = cv.getColumnvalue();
		    if (object == null) {
			throw new SQLException(
				"primary key column's value cannot be null");
		    }
		    whereParams.add(cv.getColumnvalue());
		}

		String sql = "delete from " + tableName + " where "
			+ where.substring(5);
		executeUpdate(conn, sql, whereParams.toArray());
	    }

	} catch (SQLException e) {
	    throw e;
	} catch (Exception e) {
	    throw new SQLException(e.getMessage());
	}
    }

    /**
     * Insert.
     *
     * @param conn
     *            the conn
     * @param entity
     *            the entity
     * @throws SQLException
     *             the sQL exception
     */
    public void insert(Connection conn, Object entity) throws SQLException {
	checkDbType();

	try {
	    EntityMapping mapping = EntityMappingFactory
		    .getEntityMapping(entity.getClass());
	    String tableName = mapping.getTableName();

	    PropertyMapping idPm = mapping.getIdMapping();

	    List<ColumnValue> idColumns = new ArrayList<ColumnValue>();
	    List<ColumnValue> clobColumns = new ArrayList<ColumnValue>();
	    List<ColumnValue> blobColumns = new ArrayList<ColumnValue>();

	    ColumnValueLoader columnValueLoader = new InsertOperationColumnValueLoader();
	    Map<String, ColumnValue> columnValueMap = columnValueLoader
		    .getColumnValueMapRecursive(entity, mapping);

	    Collection<ColumnValue> columnValues = columnValueMap.values();
	    List<Object> paramList = new ArrayList<Object>();

	    String columns = "";
	    String values = "";
	    Object id = null;

	    String columnName = null;
	    PropertyMapping pm = null;
	    ColumnMapping cm = null;

	    for (ColumnValue cv : columnValues) {
		columnName = cv.getColumnName();
		pm = cv.getPropertyMapping();
		cm = pm.getColumnMapping();

		if (pm.isTransientField() || pm.isRelationProperty()) {
		    continue;
		}

		if (cm.getLobType() == LobType.BLOB) {
		    columns += "," + columnName;
		    if (dbType == DBOption.DB_ORACLE) {
			values += ",empty_blob()";
			blobColumns.add(cv);
		    } else if (dbType == DBOption.DB_SQLSERVER) {
			values += ", 0";
			blobColumns.add(cv);
		    } else if (dbType == DBOption.DB_MYSQL) {
			values += ", ?";
			Object o = cv.getColumnvalue();
			if (!(o instanceof byte[])) {
			    throw new Exception(
				    "Entity must use byte[] type property for blob field");
			}
			paramList.add(new ByteArrayInputStream((byte[]) o));
		    }
		    continue;
		} else if (cm.getLobType() == LobType.CLOB) {
		    columns += "," + columnName;
		    if (dbType == DBOption.DB_ORACLE) {
			values += ",empty_clob()";
			clobColumns.add(cv);
		    } else if (dbType == DBOption.DB_SQLSERVER) {
			values += ", ' '";
			clobColumns.add(cv);
		    } else if (dbType == DBOption.DB_MYSQL) {
			values += ", ?";
			Object o = cv.getColumnvalue();
			if (!(o instanceof String)) {
			    throw new Exception(
				    "Entity must use String type property for clob field");
			}
			paramList.add(new StringReader((String) o));
		    }
		    continue;
		} else if (cv.isPrimaryKey()) {
		    idColumns.add(cv);

		    if (!idPm.isComplexType()) {
			if (pm.getGenerateStrategy() == GenerationType.SEQUENCE) {
			    if (dbType == DBOption.DB_ORACLE) {
				String sequenceName = pm.getGenerator();
				id = DBHelper.getSeqNextValue(conn,
					sequenceName);

				columns += "," + columnName;
				values += ",?";
				paramList.add(id);

				cv.setColumnvalue(id);
				PropertyUtils.setProperty(entity,
					idPm.getPropertyName(), id);
			    } else {
				throw new Exception(
					"cannot use sequence for primary key in database type :"
						+ dbType);
			    }
			} else if (pm.getGenerateStrategy() == GenerationType.IDENTITY) {
			    if (dbType != DBOption.DB_SQLSERVER
				    && dbType != DBOption.DB_MYSQL) {
				throw new Exception(
					"cannot use identity for primary key in database type : "
						+ dbType);
			    }
			} else if (pm.getGenerateStrategy() == GenerationType.TABLE) {
			    columns += "," + columnName;
			    values += ",?";
			    paramList.add(cv.getColumnvalue());
			}
		    } else {
			// 复合主键
			columns += "," + columnName;
			values += ",?";
			paramList.add(cv.getColumnvalue());
		    }
		} else {
		    columns += "," + columnName;
		    values += ",?";
		    paramList.add(cv.getColumnvalue());
		}
	    }

	    columns = columns.substring(1);
	    values = values.substring(1);
	    String sql = "insert into " + tableName + " (" + columns
		    + ") values (" + values + ")";
	    executeUpdate(conn, sql, paramList.toArray());

	    // 处理sqlserver identity
	    if (!idPm.isComplexType()
		    && idPm.getGenerateStrategy() == GenerationType.IDENTITY) {
		if (dbType == DBOption.DB_SQLSERVER) {
		    id = DBHelper.getIdentityValue(conn);
		} else if (dbType == DBOption.DB_MYSQL) {
		    id = DBHelper.getAutoIncrementValue(conn);
		}
		idColumns.get(0).setColumnvalue(id);
		PropertyUtils.setProperty(entity, idPm.getPropertyName(), id);
	    }

	    // 处理blob，clob
	    if (dbType == DBOption.DB_SQLSERVER || dbType == DBOption.DB_ORACLE) {
		updateLob(conn, tableName, blobColumns, clobColumns, idColumns);
	    }
	} catch (SQLException e) {
	    logger.error("SQLException", e);
	    throw e;
	} catch (Exception e) {
	    logger.error("Exception", e);
	    throw new SQLException();
	}
    }

    /**
     * Update.
     *
     * @param conn
     *            the conn
     * @param entity
     *            the entity
     * @throws SQLException
     *             the sQL exception
     */
    public void update(Connection conn, Object entity) throws SQLException {
	try {
	    EntityMapping mapping = EntityMappingFactory
		    .getEntityMapping(entity.getClass());
	    String tableName = mapping.getTableName();

	    ColumnValueLoader columnValueLoader = new UpdateOperationColumnValueLoader();
	    Map<String, ColumnValue> columnValueMap = columnValueLoader
		    .getColumnValueMapRecursive(entity, mapping);

	    PropertyMapping pm = null;
	    ColumnMapping cm = null;
	    String columnName = null;
	    Object value = null;

	    String columns = "";
	    List<Object> paramList = new ArrayList<Object>();

	    List<ColumnValue> idColumns = new ArrayList<ColumnValue>();
	    List<ColumnValue> blobColumns = new ArrayList<ColumnValue>();
	    List<ColumnValue> clobColumns = new ArrayList<ColumnValue>();

	    for (ColumnValue cv : columnValueMap.values()) {
		columnName = cv.getColumnName();
		value = cv.getColumnvalue();
		pm = cv.getPropertyMapping();
		cm = pm.getColumnMapping();

		if (value == null) {
		    continue;
		}

		if (cv.isPrimaryKey()) {
		    idColumns.add(cv);
		} else if (cm.getLobType() == LobType.BLOB) {
		    if (dbType == DBOption.DB_ORACLE) {
			columns += "," + columnName + "=empty_blob()";
			blobColumns.add(cv);
		    } else if (dbType == DBOption.DB_SQLSERVER) {
			columns += "," + columnName + "=0";
			blobColumns.add(cv);
		    } else if (dbType == DBOption.DB_MYSQL) {
			columns += "," + columnName + "=?";
			if (!(value instanceof byte[])) {
			    throw new Exception(
				    "must use byte[] type property for blob field");
			}
			paramList.add(new ByteArrayInputStream((byte[]) value));
		    }
		} else if (cm.getLobType() == LobType.CLOB) {
		    if (dbType == DBOption.DB_ORACLE) {
			columns += "," + columnName + "=empty_clob()";
			clobColumns.add(cv);
		    } else if (dbType == DBOption.DB_SQLSERVER) {
			columns += "," + columnName + "=''";
			clobColumns.add(cv);
		    } else if (dbType == DBOption.DB_MYSQL) {
			columns += "," + columnName + "=?";
			if (!(value instanceof String)) {
			    throw new Exception(
				    "must use String type property for clob field");
			}
			paramList.add(new StringReader((String) value));
		    }
		} else {
		    columns += "," + columnName + "=?";
		    paramList.add(value);
		}
	    }

	    if (idColumns == null || idColumns.isEmpty()) {
		throw new SQLException(
			"update operation need primary key for table");
	    }

	    String where = "";
	    for (ColumnValue cv : idColumns) {
		value = cv.getColumnvalue();
		if (value == null) {
		    throw new SQLException("primary key value cannot be null");
		}

		where += " and " + cv.getColumnName() + " = ?";
		paramList.add(cv.getColumnvalue());
	    }

	    columns = columns.substring(1);
	    where = where.substring(5);

	    String sql = "update " + tableName + " set " + columns + " where "
		    + where;
	    executeUpdate(conn, sql, paramList.toArray());

	    if (dbType == DBOption.DB_ORACLE || dbType == DBOption.DB_SQLSERVER) {
		updateLob(conn, tableName, blobColumns, clobColumns, idColumns);
	    }
	} catch (SQLException e) {
	    logger.error("SQLException", e);
	    throw e;
	} catch (Exception e) {
	    logger.error("Exception", e);
	    throw new SQLException();
	}
    }

    /**
     * Update lob.
     *
     * @param conn
     *            the conn
     * @param tableName
     *            the table name
     * @param blobColumns
     *            the blob columns
     * @param clobColumns
     *            the clob columns
     * @param idColumns
     *            the id columns
     * @throws SQLException
     *             the sQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void updateLob(Connection conn, String tableName,
	    List<ColumnValue> blobColumns, List<ColumnValue> clobColumns,
	    List<ColumnValue> idColumns) throws SQLException, IOException {
	if (idColumns == null || idColumns.isEmpty()) {
	    throw new SQLException("primary key not set");
	}

	String where = "";
	List<Object> whereParams = new ArrayList<Object>(idColumns.size());
	Object object = null;

	for (ColumnValue cv : idColumns) {
	    where += " and " + cv.getColumnName() + " = ?";
	    object = cv.getColumnvalue();
	    if (object == null) {
		throw new SQLException("primary key cannot be null");
	    }
	    whereParams.add(object);
	}

	where = where.substring(5);

	if (blobColumns != null && !blobColumns.isEmpty()) {
	    for (ColumnValue cv : blobColumns) {
		DBHelper.updateBlobValue(dbType, conn, tableName,
			cv.getColumnName(), (byte[]) cv.getColumnvalue(),
			where, whereParams);
	    }
	}

	if (clobColumns != null && !clobColumns.isEmpty()) {
	    for (ColumnValue cv : clobColumns) {
		DBHelper.updateClobValue(dbType, conn, tableName,
			cv.getColumnName(), (String) cv.getColumnvalue(),
			where, whereParams);
	    }
	}
    }

    /**
     * Check db type.
     *
     * @throws SQLException
     *             the sQL exception
     */
    private void checkDbType() throws SQLException {
	if (dbType != DBOption.DB_ORACLE && dbType != DBOption.DB_SQLSERVER
		&& dbType != DBOption.DB_MYSQL) {
	    throw new SQLException("Not support database type: " + dbType);
	}
    }

    /**
     * Checks if is nest load.
     *
     * @return true, if is nest load
     */
    public boolean isNestLoad() {
	return nestLoad;
    }

    /**
     * Sets the nest load.
     *
     * @param nestLoad
     *            the new nest load
     */
    public void setNestLoad(boolean nestLoad) {
	this.nestLoad = nestLoad;
    }

    /**
     * Gets the db type.
     *
     * @return the db type
     */
    public int getDbType() {
	return dbType;
    }

    /**
     * Sets the db type.
     *
     * @param dbType
     *            the new db type
     */
    public void setDbType(int dbType) {
	this.dbType = dbType;
    }

    /**
     * Exec proc.
     *
     * @param conn
     *            the conn
     * @param procedure
     *            the procedure
     * @param sqlType
     *            the sql type
     * @param procedureCallBack
     *            the procedure call back
     * @return the procedure result
     * @throws SQLException
     *             the sQL exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ProcedureResult execProc(Connection conn, String procedure,
	    Integer sqlType, ProcedureCallBack procedureCallBack)
	    throws SQLException {
	Statement stmt = null;
	ResultSet rs = null;
	ProcedureResult procResult = new ProcedureResult();
	Object type = null;
	if (sqlType != null)
	    type = (dbType == DBOption.DB_ORACLE) ? null : new Integer(sqlType);
	try {
	    procedureCallBack.ndbType = dbType;
	    procedureCallBack.registerParameter();// 计算参数的个数
	    int parameterSize = procedureCallBack.parametersCount;// 取参数的个数
	    procedureCallBack.parametersCount = 0;
	    if (parameterSize > 0)
		procedure = procedure
			+ DBHelper.getProcParameters(parameterSize, "?");// 根据参数的个数拼装sql
	    String sql = "{" + (type != null ? "?=" : "") + "call " + procedure
		    + "}";
	    CallableStatement cstm = conn.prepareCall(sql);// 预编译过程
	    procedureCallBack.cstm = cstm;
	    procedureCallBack.startParametersIdx = type != null ? 1 : 0;// 对外界忽略带返回值过程的参数下标
	    if (type != null)
		procedureCallBack.registerOutParameter(0,
			(((Integer) type).intValue()));// 注册带返回值过程的返回类型oracle不支持mssql支持
	    procedureCallBack.registerParameter();// 注册参数
	    // logger.error(sql);
	    boolean results = cstm.execute();// 执行过程
	    // logger.error("results="+results);
	    int rsIndex = 1;// 结果集个数计数器
	    for (; results; rsIndex++) {
		rs = cstm.getResultSet();// 取当前结果集
		List<Object> list = new ArrayList<Object>();// 为当前结果集创建一个容器
		while (rs.next())
		    list.add(procedureCallBack.mapRow(rs, rsIndex));
		procResult.addRs(list);
		results = cstm.getMoreResults();// 取下一个结果集
	    }
	    Map<Integer, Integer> map = procedureCallBack.getOutParameters();// 取声明的out参数,
	    Iterator<Integer> iterator = map.keySet().iterator();// 遍历声明的out参数
	    while (iterator.hasNext()) {
		int key = iterator.next();// out参数类型
		if (new Integer(-10).equals(map.get(key))) {// oracle结果集列表是通过游标的形式利用out参数输出的oracle.jdbc.OracleTypes.CURSOR的值是-10
		    DBHelper.closeResultSet(rs);// 关闭上次使用的结果集
		    rs = (ResultSet) cstm.getObject(key);// 从out参数中取结果集
		    List list = new ArrayList();// 为当前结果集创建一个容器
		    while (rs.next())
			list.add(procedureCallBack.mapRow(rs, rsIndex));
		    rsIndex++;// 结果集计数器累加
		    procResult.addRs(list);
		} else if (key == 1 && sqlType != null)// 如果过程有返回值,从第一个out参数中取得这个返回值
		    procResult.setValue(DBHelper.getCallableStatementValue(
			    cstm, key));
		else
		    procResult.getOutput().add(
			    DBHelper.getCallableStatementValue(cstm, key));// 取的普通的out参数输出到out输出容器
	    }
	    if (sqlType == null && procResult.getOutput().size() > 0)
		// 如果过程没用返回值,例如oracle过程根本不支持返回值,将第一个非游标的out参数返回值当作过程返回值
		procResult.setValue(procResult.getOutput().get(0));
	} catch (SQLException e) {
	    logger.error("SQLException", e);
	    throw e;
	} finally {
	    DBHelper.closeResultSet(rs);
	    DBHelper.closeStatement(stmt);

	}
	return procResult;
    }

    /**
     * Execute query page.
     *
     * @param <T>
     *            the generic type
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param pager
     *            the pager
     * @param rm
     *            the rm
     * @param dbType
     *            the db type
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    public <T> List<T> executeQueryPage(Connection conn, String sql,
	    Object[] parameters, ListPager pager, ParameterizedRowMapper<T> rm,
	    int dbType) throws SQLException {
	List<NoCaseMap<String, Object>> rs = executeQueryPageNoCaseMap(conn,
		sql, parameters, pager, dbType);
	List<T> result = new ArrayList<T>();
	if (rs == null || (rs != null && rs.size() <= 0))
	    return result;
	for (int i = 0; i < rs.size(); i++) {
	    result.add(rm.mapRow(rs.get(i), i));
	}
	return result;
    }

    /**
     * Execute query page.
     *
     * @param <T>
     *            the generic type
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param pager
     *            the pager
     * @param rm
     *            the rm
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    public <T> List<T> executeQueryPage(Connection conn, String sql,
	    Object[] parameters, ListPager pager, ParameterizedRowMapper<T> rm)
	    throws SQLException {
	return executeQueryPage(conn, sql, parameters, pager, rm,
		DBOption.DB_ORACLE);
    }

    /**
     * Builds the paged sql oracle.
     *
     * @param sql
     *            the sql
     * @param nTop
     *            the n top
     * @return the string
     */
    private String buildPagedSQLOracle(String sql, int nTop) {
	StringBuffer sqlBuffer = new StringBuffer(500);
	sqlBuffer.append("select * from ( ");
	sqlBuffer.append(sql);
	sqlBuffer.append(" ) where rownum <= " + nTop);
	return sqlBuffer.toString();
    }

    /**
     * Select top no case.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param nTop
     *            the n top
     * @return the list
     */
    @SuppressWarnings("rawtypes")
    private List<NoCaseMap> selectTopNoCase(Connection conn, String sql,
	    Object[] parameters, int nTop) {
	PreparedStatement ps = null;
	ResultSet rs = null;
	List<NoCaseMap> result = new ArrayList<NoCaseMap>();

	try {

	    ps = conn.prepareStatement(buildPagedSQLOracle(sql, nTop));
	    DBHelper.setPreparedParameters(ps, parameters);
	    rs = ps.executeQuery();

	    NoCaseMap<String, Object> record = null;
	    while (rs.next()) {
		record = DBHelper.getResultsetRowNoCase(rs);
		result.add(record);
	    }
	} catch (SQLException e) {
	    logger.error("SQLException", e);
	} finally {
	    DBHelper.close(ps, rs);
	}

	return result;
    }

    /**
     * Select top.
     *
     * @param <T>
     *            the generic type
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param nTop
     *            the n top
     * @param rm
     *            the rm
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <T> List<T> selectTop(Connection conn, String sql,
	    Object[] parameters, int nTop, ParameterizedRowMapper<T> rm)
	    throws SQLException {
	List<NoCaseMap> rs = selectTopNoCase(conn, sql, parameters, nTop);
	List<T> result = new ArrayList<T>();
	if (rs == null || (rs != null && rs.size() <= 0))
	    return result;
	for (int i = 0; i < rs.size(); i++) {
	    result.add(rm.mapRow(rs.get(i), i));
	}
	return result;
    }

    /**
     * Select top.
     *
     * @param <T>
     *            the generic type
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param clazz
     *            the clazz
     * @param nTop
     *            the n top
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    public <T> List<T> selectTop(Connection conn, String sql,
	    Object[] parameters, Class<T> clazz, int nTop) throws SQLException {

	List<T> recordList = new ArrayList<T>(nTop);
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {

	    ps = conn.prepareStatement(buildPagedSQLOracle(sql, nTop));
	    DBHelper.setPreparedParameters(ps, parameters);
	    rs = ps.executeQuery();

	    ResultSetMetaData meta = rs.getMetaData();

	    while (rs.next()) {
		T record = createEntityFromRS(conn, rs, meta, clazz);
		recordList.add(record);
	    }
	} catch (SQLException e) {
	    logger.error("SQLException", e);
	    throw e;
	} catch (Exception e) {
	    logger.error("Exception", e);
	    throw new SQLException(e.getMessage());
	} finally {
	    DBHelper.close(ps, rs);
	}

	return recordList;
    }

    /**
     * Ret t.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return the t
     * @throws Exception
     *             the exception
     */
    public static <T> T retT(Class<T> clazz) throws Exception {
	return clazz.newInstance();
    }

}
