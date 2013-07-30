/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.sql.RowSet;

import oracle.jdbc.driver.OracleResultSet;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mvc.basemvc.persistence.db.DBOption;
import com.mvc.basemvc.persistence.db.NoCaseMap;

/**
 * @Description db帮助类
 * @ClassName DBHelper
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:03:55
 */
public class DBHelper {

    /** The Constant logger. */
    private static final Log logger = LogFactory.getLog(DBHelper.class);

    /**
     * Sets the prepared parameters.
     *
     * @param ps
     *            the ps
     * @param params
     *            the params
     * @throws SQLException
     *             the sQL exception
     */
    public static void setPreparedParameters(PreparedStatement ps,
	    Object[] params) throws SQLException {
	ParameterMetaData paramMetaData = ps.getParameterMetaData();
	if (paramMetaData.getParameterCount() > 0
		&& (params == null || params.length < paramMetaData
			.getParameterCount())) {
	    throw new SQLException("Not enough parameters");
	}

	if (params != null && params.length > 0) {
	    Object param = null;
	    // TODO: null/BLOB
	    for (int i = 1; i <= params.length; i++) {
		param = params[i - 1];
		if (param == null) {
		    ps.setObject(i, param);
		} else if (param instanceof String) {
		    ps.setString(i, (String) param);
		} else if (param instanceof Integer) {
		    ps.setInt(i, ((Integer) param).intValue());
		} else if (param instanceof java.util.Date) {
		    ps.setTimestamp(i,
			    new Timestamp(((java.util.Date) param).getTime()));
		} else if (param instanceof Timestamp) {
		    ps.setTimestamp(i, (Timestamp) param);
		} else if (param instanceof java.sql.Date) {
		    ps.setTimestamp(i,
			    new Timestamp(((java.sql.Date) param).getTime()));
		} else if (param instanceof Float) {
		    ps.setFloat(i, ((Float) param).floatValue());
		} else if (param instanceof Double) {
		    ps.setDouble(i, ((Double) param).doubleValue());
		} else if (param instanceof Long) {
		    ps.setLong(i, ((Long) param).longValue());
		} else if (param instanceof Short) {
		    ps.setShort(i, ((Short) param).shortValue());
		} else if (param instanceof Byte) {
		    ps.setShort(i, ((Byte) param).byteValue());
		} else if (param instanceof Boolean) {
		    ps.setBoolean(i, ((Boolean) param).booleanValue());
		} else if (param instanceof BigDecimal) {
		    ps.setBigDecimal(i, (BigDecimal) param);
		} else if (param instanceof ByteArrayInputStream) {
		    ps.setBinaryStream(i, (InputStream) param);
		} else if (param instanceof StringReader) {
		    ps.setCharacterStream(i, (Reader) param);
		}
	    }
	}
    }

    /**
     * Gets the resultset value.
     *
     * @param <T>
     *            the generic type
     * @param rs
     *            the rs
     * @param column
     *            the column
     * @param propertyClass
     *            the property class
     * @return the resultset value
     * @throws SQLException
     *             the sQL exception
     */
    public static <T> Object getResultsetValue(ResultSet rs, String column,
	    Class<T> propertyClass) throws SQLException {
	Object value = null;
	String className = propertyClass.getName();

	if (!BaseUtils.isBaseType(propertyClass))
	    throw new SQLException("Cannot Recognise the data Type, column="
		    + column + ", type=" + className);

	if ("java.lang.String".equals(className)) {
	    value = rs.getString(column);
	} else if ("int".equals(className)
		|| "java.lang.Integer".equals(className)) {
	    value = new Integer(rs.getInt(column));

	} else if ("float".equals(className)
		|| "java.lang.Float".equals(className)) {
	    value = new Float(rs.getFloat(column));
	} else if ("double".equals(className)
		|| "java.lang.Double".equals(className)) {
	    value = new Double(rs.getDouble(column));
	} else if ("long".equals(className)
		|| "java.lang.Long".equals(className)) {
	    value = new Long(rs.getLong(column));
	} else if ("short".equals(className)
		|| "java.lang.Short".equals(className)) {
	    value = new Short(rs.getShort(column));
	} else if ("byte".equals(className)
		|| "java.lang.Byte".equals(className)) {
	    value = new Byte(rs.getByte(column));
	} else if ("boolean".equals(className)
		|| "java.lang.Boolean".equals(className)) {
	    value = new Boolean(rs.getBoolean(column));
	} else if ("java.util.Date".equals(className)) {
	    Timestamp t = rs.getTimestamp(column);
	    if (t == null) {
		value = null;
	    } else {
		value = new java.util.Date(t.getTime());
	    }
	} else if ("java.sql.Timestamp".equals(className)) {
	    value = rs.getTimestamp(column);
	} else if ("java.sql.Date".equals(className)) {
	    Timestamp t = rs.getTimestamp(column);
	    if (t == null) {
		value = null;
	    } else {
		value = new java.sql.Date(t.getTime());
	    }
	} else if ("java.lang.BigDecimal".equals(className)) {
	    value = rs.getBigDecimal(column);
	} else {
	    throw new SQLException("Cannot Recognise the data Type, column="
		    + column + ", type=" + className);
	}

	return value;
    }

    /**
     * Gets the resultset value.
     *
     * @param rs
     *            the rs
     * @param columnName
     *            the column name
     * @return the resultset value
     * @throws SQLException
     *             the sQL exception
     */
    public static Object getResultsetValue(ResultSet rs, String columnName)
	    throws SQLException {
	ResultSetMetaData rsMeta = rs.getMetaData();

	int i = 1;
	for (; i <= rsMeta.getColumnCount(); i++) {
	    if (rsMeta.getColumnName(i).equalsIgnoreCase(columnName))
		break;
	}

	if (i > rsMeta.getColumnCount())
	    return null;
	return getResultsetValue(rs, i);
    }

    /**
     * Gets the resultset value.
     *
     * @param rs
     *            the rs
     * @param columnIndex
     *            the column index
     * @return the resultset value
     * @throws SQLException
     *             the sQL exception
     */
    public static Object getResultsetValue(ResultSet rs, int columnIndex)
	    throws SQLException {
	ResultSetMetaData rsMeta = rs.getMetaData();
	Object value = null;

	String className = rsMeta.getColumnClassName(columnIndex);
	int columnType = rsMeta.getColumnType(columnIndex);
	String column = rsMeta.getColumnName(columnIndex);

	int scale = rsMeta.getScale(columnIndex);

	if ("java.lang.String".equals(className)) {
	    value = rs.getString(column);
	} else if ("java.math.BigDecimal".equals(className)) {
	    BigDecimal decimal = rs.getBigDecimal(column);
	    int precision = rsMeta.getPrecision(columnIndex);
	    if (decimal == null) {
		value = null;
	    } else {
		if (scale == 0) {
		    try {
			long l = decimal.longValueExact();
			if (precision <= 10 && l <= Integer.MAX_VALUE
				&& l >= Integer.MIN_VALUE) {
			    value = (int) l;
			} else {
			    value = l;
			}
		    } catch (Exception e) {
			e.printStackTrace();
			value = decimal;
		    }
		} else {
		    // 判断是否sequence，或者count。返回long
		    String s = column.toLowerCase();
		    if (s.endsWith("currval") || s.endsWith("nextval")
			    || s.startsWith("count(")) {
			long l = decimal.longValueExact();
			if (l <= Integer.MAX_VALUE && l >= Integer.MIN_VALUE)
			    value = (int) l;
			else
			    value = l;
		    } else {
			double d = decimal.doubleValue();
			value = d;
		    }
		}
	    }
	} else if ("int".equals(className)
		|| "java.lang.Integer".equals(className)) {
	    value = new Integer(rs.getInt(column));
	} else if ("float".equals(className)
		|| "java.lang.Float".equals(className)) {
	    value = new Float(rs.getFloat(column));
	} else if ("double".equals(className)
		|| "java.lang.Double".equals(className)) {
	    value = new Double(rs.getDouble(column));
	} else if ("long".equals(className)
		|| "java.lang.Long".equals(className)) {
	    value = new Long(rs.getLong(column));
	} else if ("short".equals(className)
		|| "java.lang.Short".equals(className)) {
	    value = new Short(rs.getShort(column));
	} else if ("byte".equals(className)
		|| "java.lang.Byte".equals(className)) {
	    value = new Byte(rs.getByte(column));
	} else if ("boolean".equals(className)
		|| "java.lang.Boolean".equals(className)) {
	    value = new Boolean(rs.getBoolean(column));
	} else if ("java.util.Date".equals(className)
		|| "java.sql.Timestamp".equals(className)
		|| "java.sql.Date".equals(className)) {
	    Timestamp t = rs.getTimestamp(column);
	    if (t == null) {
		value = null;
	    } else {
		value = new java.util.Date(t.getTime());
	    }
	} else if ("byte[]".equals(className)) {
	    value = rs.getBytes(column);
	} else if ("oracle.sql.ROWID".equals(className)) {
	    value = rs.getString(column);
	} else if (columnType == Types.CLOB) {
	    value = DBHelper.getClobValue(rs, column);
	} else if (columnType == Types.BLOB) {
	    value = getBlobValue(rs, column);
	} else {
	    throw new SQLException("Cannot Recognise the data Type, column="
		    + column + ", type=" + className);
	}

	return value;
    }

    /**
     * Gets the resultset row.
     *
     * @param rs
     *            the rs
     * @return the resultset row
     * @throws SQLException
     *             the sQL exception
     */
    public static Map<String, Object> getResultsetRow(ResultSet rs)
	    throws SQLException {
	String column = null;
	Object value = null;
	ResultSetMetaData rsMeta = rs.getMetaData();
	Map<String, Object> record = new HashMap<String, Object>();

	for (int i = 1; i <= rsMeta.getColumnCount(); i++) {
	    column = rsMeta.getColumnName(i);
	    value = getResultsetValue(rs, i);
	    record.put(column, value);
	}

	return record;
    }

    /**
     * Gets the resultset row no case.
     *
     * @param rs
     *            the rs
     * @return the resultset row no case
     * @throws SQLException
     *             the sQL exception
     */
    public static NoCaseMap<String, Object> getResultsetRowNoCase(ResultSet rs)
	    throws SQLException {
	String column = null;
	Object value = null;
	ResultSetMetaData rsMeta = rs.getMetaData();
	NoCaseMap<String, Object> record = new NoCaseMap<String, Object>();

	for (int i = 1; i <= rsMeta.getColumnCount(); i++) {
	    column = rsMeta.getColumnName(i);
	    value = getResultsetValue(rs, i);
	    record.put(column, value);
	}

	return record;
    }

    /**
     * Gets the clob value.
     *
     * @param rs
     *            the rs
     * @param columnName
     *            the column name
     * @return the clob value
     * @throws SQLException
     *             the sQL exception
     */
    public static String getClobValue(ResultSet rs, String columnName)
	    throws SQLException {
	try {
	    Reader reader = null;
	    if (rs instanceof OracleResultSet) {
		CLOB clob = ((OracleResultSet) rs).getCLOB(columnName);
		if (clob == null)
		    return null;
		reader = clob.getCharacterStream();
	    }

	    else {
		Clob clob = rs.getClob(columnName);
		if (clob == null)
		    return null;
		reader = clob.getCharacterStream();
	    }

	    char[] buffer = new char[8096];
	    StringBuffer sb = new StringBuffer();
	    int count = 0;
	    while ((count = reader.read(buffer)) != -1) {
		sb.append(new String(buffer, 0, count));
	    }
	    return sb.toString();
	} catch (IOException e) {
	    e.printStackTrace();
	    throw new SQLException(e.getMessage());
	}
    }

    /**
     * Gets the clob value.
     *
     * @param rs
     *            the rs
     * @param clobidx
     *            the clobidx
     * @return the clob value
     * @throws Exception
     *             the exception
     */
    public static String getClobValue(ResultSet rs, int clobidx)
	    throws Exception {
	try {
	    Reader reader = null;
	    if (rs instanceof OracleResultSet) {
		CLOB clob = ((OracleResultSet) rs).getCLOB(clobidx);
		if (clob == null)
		    return null;
		reader = clob.getCharacterStream();
	    } else {
		Clob clob = rs.getClob(clobidx);
		if (clob == null)
		    return null;
		reader = clob.getCharacterStream();
	    }

	    char[] buffer = new char[8096];
	    StringBuffer sb = new StringBuffer();
	    int count = 0;
	    while ((count = reader.read(buffer)) != -1) {
		sb.append(new String(buffer, 0, count));
	    }
	    return sb.toString();
	} catch (IOException e) {
	    e.printStackTrace();
	    throw new SQLException(e.getMessage());
	}
    }

    /**
     * Gets the cLOB content.
     *
     * @param rs
     *            the rs
     * @param clobidx
     *            the clobidx
     * @return the cLOB content
     * @throws Exception
     *             the exception
     */
    public static String getCLOBContent(ResultSet rs, int clobidx)
	    throws Exception {
	return getClobValue(rs, clobidx);
    }

    /**
     * Gets the blob value.
     *
     * @param rs
     *            the rs
     * @param columnName
     *            the column name
     * @return the blob value
     * @throws SQLException
     *             the sQL exception
     */
    public static byte[] getBlobValue(ResultSet rs, String columnName)
	    throws SQLException {
	try {
	    InputStream is = null;
	    if (rs instanceof OracleResultSet) {
		BLOB blob = ((OracleResultSet) rs).getBLOB(columnName);
		if (blob == null)
		    return new byte[0];
		is = blob.getBinaryStream();
	    } else {
		Blob blob = rs.getBlob(columnName);
		if (blob == null)
		    return new byte[0];
		is = blob.getBinaryStream();
	    }

	    if (is == null)
		return new byte[0];

	    ByteArrayOutputStream bos = new ByteArrayOutputStream();

	    byte[] buffer = new byte[8096];
	    int count = 0;
	    while ((count = is.read(buffer)) != -1) {
		bos.write(buffer, 0, count);
	    }

	    return bos.toByteArray();
	} catch (IOException e) {
	    e.printStackTrace();
	    throw new SQLException(e.getMessage());
	}
    }

    /**
     * Update clob value.
     *
     * @param conn
     *            the conn
     * @param tableName
     *            the table name
     * @param clobColumn
     *            the clob column
     * @param clobValue
     *            the clob value
     * @param idColumn
     *            the id column
     * @param idValue
     *            the id value
     * @throws SQLException
     *             the sQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static void updateClobValue(Connection conn, String tableName,
	    String clobColumn, String clobValue, String idColumn, Object idValue)
	    throws SQLException, IOException {
	updateClobValue(DBOption.DB_ORACLE, conn, tableName, clobColumn,
		clobValue, idColumn, idValue);
    }

    /**
     * Update clob value.
     *
     * @param dbType
     *            the db type
     * @param conn
     *            the conn
     * @param tableName
     *            the table name
     * @param clobColumn
     *            the clob column
     * @param clobValue
     *            the clob value
     * @param idColumn
     *            the id column
     * @param idValue
     *            the id value
     * @throws SQLException
     *             the sQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static void updateClobValue(int dbType, Connection conn,
	    String tableName, String clobColumn, String clobValue,
	    String idColumn, Object idValue) throws SQLException, IOException {
	String where = idColumn + " = ?";
	List<Object> whereParams = new ArrayList<Object>(1);
	whereParams.add(idValue);
	updateClobValue(dbType, conn, tableName, clobColumn, clobValue, where,
		whereParams);
	if (clobValue == null)
	    clobValue = "";
    }

    /**
     * Update clob value.
     *
     * @param dbType
     *            the db type
     * @param conn
     *            the conn
     * @param tableName
     *            the table name
     * @param clobColumn
     *            the clob column
     * @param clobValue
     *            the clob value
     * @param where
     *            the where
     * @param whereParams
     *            the where params
     * @throws SQLException
     *             the sQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static void updateClobValue(int dbType, Connection conn,
	    String tableName, String clobColumn, String clobValue,
	    String where, List<Object> whereParams) throws SQLException,
	    IOException {
	if (clobValue == null)
	    clobValue = "";
	if (whereParams == null)
	    whereParams = new ArrayList<Object>(0);

	if (dbType == DBOption.DB_ORACLE) {
	    updateClobValueOracle(conn, tableName, clobColumn, clobValue,
		    where, whereParams);
	} else if (dbType == DBOption.DB_SQLSERVER) {
	    updateClobValueSQLServer(conn, tableName, clobColumn, clobValue,
		    where, whereParams);
	} else if (dbType == DBOption.DB_MYSQL) {
	    updateClobValueMySQL(conn, tableName, clobColumn, clobValue, where,
		    whereParams);
	} else {
	    throw new SQLException("not support this DBMS");
	}
    }

    /**
     * Update clob value sql server.
     *
     * @param conn
     *            the conn
     * @param tableName
     *            the table name
     * @param clobColumn
     *            the clob column
     * @param clobValue
     *            the clob value
     * @param where
     *            the where
     * @param whereParams
     *            the where params
     * @throws SQLException
     *             the sQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static void updateClobValueSQLServer(Connection conn,
	    String tableName, String clobColumn, String clobValue,
	    String where, List<Object> whereParams) throws SQLException,
	    IOException {

	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    String sql = "select " + clobColumn + " from " + tableName
		    + " where " + where;
	    ps = conn.prepareStatement(sql, RowSet.TYPE_FORWARD_ONLY,
		    RowSet.CONCUR_UPDATABLE);

	    Object[] params = whereParams.toArray();
	    DBHelper.setPreparedParameters(ps, params);

	    rs = ps.executeQuery();
	    if (rs.next()) {
		Clob clob = rs.getClob(clobColumn);
		clob.setString(1, clobValue);
		rs.updateClob(clobColumn, clob);
		rs.updateRow();
	    }
	} finally {
	    DBHelper.close(ps, rs);
	}
    }

    /**
     * Update clob value oracle.
     *
     * @param conn
     *            the conn
     * @param tableName
     *            the table name
     * @param clobColumn
     *            the clob column
     * @param clobValue
     *            the clob value
     * @param where
     *            the where
     * @param whereParams
     *            the where params
     * @throws SQLException
     *             the sQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static void updateClobValueOracle(Connection conn, String tableName,
	    String clobColumn, String clobValue, String where,
	    List<Object> whereParams) throws SQLException, IOException {
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    String sql = "select " + clobColumn + " from " + tableName
		    + " where " + where + " for update";
	    ps = conn.prepareStatement(sql);

	    Object[] params = whereParams.toArray();
	    DBHelper.setPreparedParameters(ps, params);

	    rs = ps.executeQuery();
	    if (rs.next()) {
		Writer writer = null;
		if (rs instanceof OracleResultSet) {
		    CLOB clob = ((OracleResultSet) rs).getCLOB(clobColumn);
		    writer = clob.setCharacterStream(0);
		} else {
		    Clob clob = rs.getClob(clobColumn);
		    writer = clob.setCharacterStream(0);
		}
		writer.write(clobValue);
		writer.flush();
		writer.close();
	    }
	} finally {
	    DBHelper.close(ps, rs);
	}
    }

    /**
     * Update clob value my sql.
     *
     * @param conn
     *            the conn
     * @param tableName
     *            the table name
     * @param clobColumn
     *            the clob column
     * @param clobValue
     *            the clob value
     * @param where
     *            the where
     * @param whereParams
     *            the where params
     * @throws SQLException
     *             the sQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static void updateClobValueMySQL(Connection conn, String tableName,
	    String clobColumn, String clobValue, String where,
	    List<Object> whereParams) throws SQLException, IOException {
	String sql = "update " + tableName + " set " + clobColumn
		+ " = ? where " + where;
	PreparedStatement ps = null;

	try {
	    ps = conn.prepareStatement(sql);
	    whereParams.add(0, new StringReader(clobValue));
	    DBHelper.setPreparedParameters(ps, whereParams.toArray());
	    ps.executeUpdate();
	} finally {
	    DBHelper.close(ps);
	}
    }

    /**
     * Update blob value.
     *
     * @param conn
     *            the conn
     * @param tableName
     *            the table name
     * @param blobColumn
     *            the blob column
     * @param blobValue
     *            the blob value
     * @param idColumn
     *            the id column
     * @param idValue
     *            the id value
     * @throws SQLException
     *             the sQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static void updateBlobValue(Connection conn, String tableName,
	    String blobColumn, byte[] blobValue, String idColumn, Object idValue)
	    throws SQLException, IOException {
	updateBlobValue(DBOption.DB_ORACLE, conn, tableName, blobColumn,
		blobValue, idColumn, idValue);
    }

    /**
     * Update blob value.
     *
     * @param dbType
     *            the db type
     * @param conn
     *            the conn
     * @param tableName
     *            the table name
     * @param blobColumn
     *            the blob column
     * @param blobValue
     *            the blob value
     * @param idColumn
     *            the id column
     * @param idValue
     *            the id value
     * @throws SQLException
     *             the sQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static void updateBlobValue(int dbType, Connection conn,
	    String tableName, String blobColumn, byte[] blobValue,
	    String idColumn, Object idValue) throws SQLException, IOException {
	String where = "id = ?";
	List<Object> whereParams = new ArrayList<Object>(1);
	whereParams.add(idValue);
	updateBlobValue(dbType, conn, tableName, blobColumn, blobValue, where,
		whereParams);
    }

    /**
     * Update blob value.
     *
     * @param dbType
     *            the db type
     * @param conn
     *            the conn
     * @param tableName
     *            the table name
     * @param blobColumn
     *            the blob column
     * @param blobValue
     *            the blob value
     * @param where
     *            the where
     * @param whereParams
     *            the where params
     * @throws SQLException
     *             the sQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static void updateBlobValue(int dbType, Connection conn,
	    String tableName, String blobColumn, byte[] blobValue,
	    String where, List<Object> whereParams) throws SQLException,
	    IOException {
	if (blobValue == null)
	    blobValue = new byte[0];
	if (whereParams == null)
	    whereParams = new ArrayList<Object>(0);

	if (dbType == DBOption.DB_ORACLE) {
	    updateBlobValueOracle(conn, tableName, blobColumn, blobValue,
		    where, whereParams);
	} else if (dbType == DBOption.DB_SQLSERVER) {
	    updateBlobValueSQLServer(conn, tableName, blobColumn, blobValue,
		    where, whereParams);
	} else if (dbType == DBOption.DB_MYSQL) {
	    updateBlobValueMySQL(conn, tableName, blobColumn, blobValue, where,
		    whereParams);
	} else {
	    throw new SQLException("not support for DBMS");
	}
    }

    /**
     * Update blob value my sql.
     *
     * @param conn
     *            the conn
     * @param tableName
     *            the table name
     * @param blobColumn
     *            the blob column
     * @param blobValue
     *            the blob value
     * @param where
     *            the where
     * @param whereParams
     *            the where params
     * @throws SQLException
     *             the sQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static void updateBlobValueMySQL(Connection conn, String tableName,
	    String blobColumn, byte[] blobValue, String where,
	    List<Object> whereParams) throws SQLException, IOException {
	String sql = "update " + tableName + " set " + blobColumn
		+ " = ? where " + where;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    InputStream is = new ByteArrayInputStream(blobValue);
	    whereParams.add(0, is);

	    ps = conn.prepareStatement(sql);
	    DBHelper.setPreparedParameters(ps, whereParams.toArray());
	    ps.executeUpdate();
	} finally {
	    DBHelper.close(ps, rs);
	}
    }

    /**
     * Update blob value oracle.
     *
     * @param conn
     *            the conn
     * @param tableName
     *            the table name
     * @param blobColumn
     *            the blob column
     * @param blobValue
     *            the blob value
     * @param where
     *            the where
     * @param whereParams
     *            the where params
     * @throws SQLException
     *             the sQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static void updateBlobValueOracle(Connection conn, String tableName,
	    String blobColumn, byte[] blobValue, String where,
	    List<Object> whereParams) throws SQLException, IOException {
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    String sql = "select " + blobColumn + " from " + tableName
		    + " where " + where + " for update";
	    ps = conn.prepareStatement(sql);

	    Object[] params = whereParams.toArray();
	    DBHelper.setPreparedParameters(ps, params);
	    rs = ps.executeQuery();

	    if (rs.next()) {
		OutputStream os = null;
		if (rs instanceof OracleResultSet) {
		    BLOB blob = ((OracleResultSet) rs).getBLOB(blobColumn);
		    os = blob.setBinaryStream(0);
		} else {
		    Blob blob = rs.getBlob(blobColumn);
		    os = blob.setBinaryStream(0);
		}
		os.write(blobValue);
		os.flush();
		os.close();
	    }
	} finally {
	    DBHelper.close(ps, rs);
	}
    }

    /**
     * Update blob value sql server.
     *
     * @param conn
     *            the conn
     * @param tableName
     *            the table name
     * @param blobColumn
     *            the blob column
     * @param blobValue
     *            the blob value
     * @param where
     *            the where
     * @param whereParams
     *            the where params
     * @throws SQLException
     *             the sQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static void updateBlobValueSQLServer(Connection conn,
	    String tableName, String blobColumn, byte[] blobValue,
	    String where, List<Object> whereParams) throws SQLException,
	    IOException {
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    String sql = "select " + blobColumn + " from " + tableName
		    + " where " + where;
	    ps = conn.prepareStatement(sql, RowSet.TYPE_FORWARD_ONLY,
		    RowSet.CONCUR_UPDATABLE);

	    Object[] params = whereParams.toArray();

	    DBHelper.setPreparedParameters(ps, params);
	    rs = ps.executeQuery();

	    if (rs.next()) {
		Blob blob = rs.getBlob(blobColumn);
		blob.setBytes(1, blobValue);
		rs.updateBlob(blobColumn, blob);
		rs.updateRow();
	    }
	} finally {
	    DBHelper.close(ps, rs);
	}
    }

    /**
     * Gets the seq next value.
     *
     * @param conn
     *            the conn
     * @param seqname
     *            the seqname
     * @return the seq next value
     * @throws SQLException
     *             the sQL exception
     */
    public static int getSeqNextValue(Connection conn, String seqname)
	    throws SQLException {
	String sql = "select " + seqname + ".nextval from dual";
	PreparedStatement ps = null;
	ResultSet rs = null;
	int id = 0;

	try {
	    ps = conn.prepareStatement(sql);
	    rs = ps.executeQuery();
	    if (rs.next()) {
		id = rs.getInt(1);
	    }
	} finally {
	    close(ps, rs);
	}

	return id;
    }

    /**
     * Gets the identity value.
     *
     * @param conn
     *            the conn
     * @return the identity value
     * @throws SQLException
     *             the sQL exception
     */
    public static int getIdentityValue(Connection conn) throws SQLException {
	String sql = "select @@IDENTITY as id";
	PreparedStatement ps = null;
	ResultSet rs = null;
	int id = 0;

	try {
	    ps = conn.prepareStatement(sql);
	    rs = ps.executeQuery();
	    if (rs.next()) {
		id = rs.getInt(1);
	    }
	} finally {
	    close(ps, rs);
	}

	return id;
    }

    /**
     * Gets the auto increment value.
     *
     * @param conn
     *            the conn
     * @return the auto increment value
     * @throws SQLException
     *             the sQL exception
     */
    public static int getAutoIncrementValue(Connection conn)
	    throws SQLException {
	String sql = "select LAST_INSERT_ID()";
	PreparedStatement ps = null;
	ResultSet rs = null;
	int id = 0;

	try {
	    ps = conn.prepareStatement(sql);
	    rs = ps.executeQuery();
	    if (rs.next()) {
		id = rs.getInt(1);
	    }
	} finally {
	    DBHelper.close(ps, rs);
	}

	return id;
    }

    /** The Constant CONFIG_PROP_DATASOURCE. */
    private static final String CONFIG_PROP_DATASOURCE = "jdbc.datasource";

    /** The Constant CONFIG_PROP_DBUSER. */
    private static final String CONFIG_PROP_DBUSER = "jdbc.user";

    /** The Constant CONFIG_PROP_DBPASSWD. */
    private static final String CONFIG_PROP_DBPASSWD = "jdbc.password";

    /** The Constant CONFIG_PROP_DBURL. */
    private static final String CONFIG_PROP_DBURL = "jdbc.url";

    /** The Constant CONFIG_PROP_DBDRIVER. */
    private static final String CONFIG_PROP_DBDRIVER = "jdbc.driver";

    /** The Constant CONFIG_PROP_CONNECT_TYPE. */
    private static final String CONFIG_PROP_CONNECT_TYPE = "jdbc.connect_type";

    /** The Constant CONFIG_PROP_DBTYPE. */
    private static final String CONFIG_PROP_DBTYPE = "jdbc.dbType";

    /** The Constant CONNECT_TYPE_DS. */
    private static final String CONNECT_TYPE_DS = "datasource";

    /** The database properties. */
    private static Properties databaseProperties = null;

    /** The init ctx. */
    private static Context initCtx = null;

    /** The Constant NAME_HEAD. */
    private final static String NAME_HEAD = "java:/comp/env";

    /**
     * Load database properties.
     *
     * @throws Exception
     *             the exception
     */
    private static synchronized void loadDatabaseProperties() throws Exception {
	if (databaseProperties == null) {
	    databaseProperties = new Properties();
	    InputStream in = DBHelper.class.getClassLoader()
		    .getResourceAsStream("database.properties");
	    databaseProperties.load(in);
	}
    }

    /**
     * Gets the dB type.
     *
     * @return the dB type
     */
    public static int getDBType() {
	try {
	    if (databaseProperties == null)
		loadDatabaseProperties();
	    String dbType = databaseProperties.getProperty(CONFIG_PROP_DBTYPE);
	    if ("sqlserver".equals(dbType))
		return DBOption.DB_SQLSERVER;
	    else if ("mysql".equals(dbType))
		return DBOption.DB_MYSQL;
	    else
		return DBOption.DB_ORACLE;
	} catch (Exception e) {
	    e.printStackTrace();
	    return DBOption.DB_ORACLE;
	}
    }

    /**
     * Gets the connection.
     *
     * @return the connection
     * @throws SQLException
     *             the sQL exception
     */
	public static Connection getConnection() throws SQLException {
	try {
	    if (databaseProperties == null)
		loadDatabaseProperties();

	    String connectType = databaseProperties
		    .getProperty(CONFIG_PROP_CONNECT_TYPE);
	    if (CONNECT_TYPE_DS.equalsIgnoreCase(connectType)) {
		String datasource = databaseProperties
			.getProperty(CONFIG_PROP_DATASOURCE);
		if (datasource.startsWith(NAME_HEAD + "/")) {
		    datasource = datasource.substring(NAME_HEAD.length() + 1);
		}
		return getConnection(datasource);
	    } else {
		String user = databaseProperties
			.getProperty(CONFIG_PROP_DBUSER);
		String passwd = databaseProperties
			.getProperty(CONFIG_PROP_DBPASSWD);
		String url = databaseProperties.getProperty(CONFIG_PROP_DBURL);
		String driver = databaseProperties
			.getProperty(CONFIG_PROP_DBDRIVER);
		return getDirectConnection(driver, url, user, passwd);
	    }
	} catch (Exception e) {
	    throw new SQLException("can not get a connection");
	}
    }

    /**
     * Gets the connection.
     *
     * @param datasource
     *            the datasource
     * @return the connection
     * @throws Exception
     *             the exception
     */
    public static Connection getConnection(String datasource) throws Exception {
	try {
	    if (initCtx == null) {
		initCtx = new InitialContext();

	    }


	    javax.sql.DataSource dataSource = (DataSource) initCtx
		    .lookup(NAME_HEAD + "/" + datasource);
	    Connection conn = dataSource.getConnection();
	    conn.setAutoCommit(false);

	    return conn;
	} catch (Exception e) {
	    e.printStackTrace();
	    throw e;
	}
    }

    /**
     * Gets the direct connection.
     *
     * @return the direct connection
     * @throws Exception
     *             the exception
     */
    public static Connection getDirectConnection() throws Exception {
	if (databaseProperties == null)
	    loadDatabaseProperties();
	String user = databaseProperties.getProperty(CONFIG_PROP_DBUSER);
	String passwd = databaseProperties.getProperty(CONFIG_PROP_DBPASSWD);
	String url = databaseProperties.getProperty(CONFIG_PROP_DBURL);
	String driver = databaseProperties.getProperty(CONFIG_PROP_DBDRIVER);
	return getDirectConnection(driver, url, user, passwd);
    }

    /**
     * Gets the direct connection.
     *
     * @param url
     *            the url
     * @param userName
     *            the user name
     * @param passwd
     *            the passwd
     * @return the direct connection
     * @throws Exception
     *             the exception
     */
    public static Connection getDirectConnection(String url, String userName,
	    String passwd) throws Exception {
	String driverName = "oracle.jdbc.driver.OracleDriver";
	Connection connection = getDirectConnection(driverName, url, userName,
		passwd);
	return connection;
    }

    /**
     * Gets the direct connection.
     *
     * @param driver
     *            the driver
     * @param url
     *            the url
     * @param userName
     *            the user name
     * @param passwd
     *            the passwd
     * @return the direct connection
     * @throws Exception
     *             the exception
     */
    public static Connection getDirectConnection(String driver, String url,
	    String userName, String passwd) throws Exception {
	Connection connection = null;
	try {
	    if (driver == null || driver.length() == 0)
		driver = "oracle.jdbc.driver.OracleDriver";
	    Class.forName(driver);
	    connection = DriverManager.getConnection(url, userName, passwd);
	    connection.setAutoCommit(false);
	    // if ( checkOn ) {
	    // DBChecker.addConnection(connection);
	    // }
	    return connection;
	} catch (Exception e) {
	    throw e;
	}
    }

    /**
     * Gets the direct connection.
     *
     * @param dbType
     *            the db type
     * @param url
     *            the url
     * @param userName
     *            the user name
     * @param passwd
     *            the passwd
     * @return the direct connection
     * @throws Exception
     *             the exception
     */
    public static Connection getDirectConnection(int dbType, String url,
	    String userName, String passwd) throws Exception {
	if (dbType == DBOption.DB_ORACLE) {
	    return getDirectConnection(DBOption.DB_DRIVER_ORACLE, url,
		    userName, passwd);
	} else if (dbType == DBOption.DB_SQLSERVER) {
	    return getDirectConnection(DBOption.DB_DRIVER_SQLSERVER, url,
		    userName, passwd);
	} else if (dbType == DBOption.DB_MYSQL) {
	    return getDirectConnection(DBOption.DB_DRIVER_MYSQL, url, userName,
		    passwd);
	} else {
	    throw new SQLException("Not support the database type: " + dbType);
	}
    }

    /**
     * Close.
     *
     * @param conn
     *            the conn
     */
    public static void close(Connection conn) {
	if (conn != null)
	    try {
		conn.close();
	    } catch (Exception e) {
	    }
    }

    /**
     * Close.
     *
     * @param stmt
     *            the stmt
     */
    public static void close(Statement stmt) {
	if (stmt != null)
	    try {
		stmt.close();
	    } catch (Exception e) {
	    }
    }

    /**
     * Close.
     *
     * @param rs
     *            the rs
     */
    public static void close(ResultSet rs) {
	if (rs != null)
	    try {
		rs.close();
	    } catch (Exception e) {
	    }
    }

    /**
     * Close.
     *
     * @param ps
     *            the ps
     */
    public static void close(PreparedStatement ps) {
	if (ps != null)
	    try {
		ps.close();
	    } catch (Exception e) {
	    }
    }

    /**
     * Close.
     *
     * @param conn
     *            the conn
     * @param ps
     *            the ps
     */
    public static void close(Connection conn, PreparedStatement ps) {
	close(ps);
	close(conn);
    }

    /**
     * Close.
     *
     * @param conn
     *            the conn
     * @param stmt
     *            the stmt
     */
    public static void close(Connection conn, Statement stmt) {
	close(stmt);
	close(conn);
    }

    /**
     * Close.
     *
     * @param conn
     *            the conn
     * @param ps
     *            the ps
     * @param rs
     *            the rs
     */
    public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
	close(rs);
	close(ps);
	close(conn);
    }

    /**
     * Close.
     *
     * @param conn
     *            the conn
     * @param stmt
     *            the stmt
     * @param rs
     *            the rs
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
	close(rs);
	close(stmt);
	close(conn);
    }

    /**
     * Close.
     *
     * @param ps
     *            the ps
     * @param rs
     *            the rs
     */
    public static void close(PreparedStatement ps, ResultSet rs) {
	close(rs);
	close(ps);
    }

    /**
     * Close.
     *
     * @param stmt
     *            the stmt
     * @param rs
     *            the rs
     */
    public static void close(Statement stmt, ResultSet rs) {
	close(rs);
	close(stmt);
    }

    /**
     * Rollback.
     *
     * @param conn
     *            the conn
     */
    protected static void rollback(Connection conn) {
	try {
	    if (conn != null)
		conn.rollback();
	} catch (Exception e) {
	}
    }

    /**
     * Escape string.
     *
     * @param s
     *            the s
     * @return the string
     */
    public static String escapeString(String s) {
	if (s == null || s.length() == 0)
	    return s;

	char c;
	StringBuffer buffer = new StringBuffer(200);
	for (int i = 0; i < s.length(); i++) {
	    c = s.charAt(i);
	    if (c == '\'') {
		buffer.append(c);
	    }
	    buffer.append(c);
	}

	return buffer.toString();
    }

    // public static ResultList executeQuery( Connection conn, String sql )
    // throws SQLException {
    // Statement st = null;
    // ResultSet rs = null;
    //
    // ResultList result = new ResultList();
    //
    // try {
    // st = conn.createStatement();
    // rs = st.executeQuery(sql);
    //
    // ResultSetMetaData meta = rs.getMetaData();
    // int colCount = meta.getColumnCount();
    // String colName = null;
    // int colType = 0;
    //
    // LinkedHashMap record = null;
    // Object value = null;
    //
    //
    // while ( rs.next() ) {
    // record = new LinkedHashMap(colCount);
    // for ( int i=1; i<=colCount; i++ ) {
    // colName = meta.getColumnName(i);
    // colType = meta.getColumnType(i);
    // switch( colType ) {
    // case Types.CHAR:
    // case Types.VARCHAR:
    // value = rs.getString(colName);
    // break;
    // case Types.DATE:
    // case Types.TIME:
    // case Types.TIMESTAMP:
    // value = rs.getTimestamp(colName);
    // break;
    // case Types.DECIMAL:
    // case Types.DOUBLE:
    // case Types.REAL:
    // case Types.NUMERIC:
    // value = new Double(rs.getDouble(colName));
    // break;
    // case Types.FLOAT:
    // value = new Float(rs.getFloat(colName));
    // break;
    // case Types.INTEGER:
    // case Types.SMALLINT:
    // case Types.TINYINT:
    // value = new Integer(rs.getInt(colName));
    // default:
    // value = rs.getObject(colName);
    // }
    // record.put( colName, value );
    // }
    //
    // result.addRecord(record);
    // }
    // } catch (SQLException e) {
    // throw e;
    // } finally {
    // DBUtils.close(st, rs);
    // }
    //
    // return result;
    //
    // }

    /**
     * Gets the system datatime.
     *
     * @param conn
     *            the conn
     * @param isMicroSecond
     *            the is micro second
     * @return the system datatime
     */
    public static Date getSystemDatatime(Connection conn, boolean isMicroSecond) {
	PreparedStatement ps = null;
	ResultSet rs = null;
	Date date = null;

	String sql = null;
	if (isMicroSecond) {
	    sql = "select systemstamp from dual";
	} else {
	    sql = "select sysdate from dual";
	}

	try {
	    ps = conn.prepareStatement(sql);
	    rs = ps.executeQuery();
	    if (rs.next()) {
		date = new Date(rs.getTimestamp(1).getTime());
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    DBHelper.close(conn, ps, rs);
	}

	return date;
    }

    /**
     * Close connection.
     *
     * @param con
     *            the con
     */
    /*
     * public static void main( String[] args ) throws Exception { Connection
     * conn = null; PreparedStatement ps = null; ResultSet rs = null; try { conn
     * =
     * DBHelper.getDirectConnection("jdbc:oracle:thin:@192.168.122.26:1521:tol24"
     * , "newclass", "nc123"); String sql =
     * "select id, postfix from t_type order by id"; List<Map> result =
     * DBAccessor.executeQuery(conn, sql, null); for ( Map r : result ) { int id
     * = (Integer) r.get( "ID" ); String postfix = (String) r.get("POSTFIX");
     * boolean isOld = false;
     *
     * if ( postfix==null || postfix.length()==0 ) { System.out.println( id +
     * "	" + postfix ); continue; }
     *
     * postfix = postfix.trim(); byte[] bb = postfix.getBytes(); for ( byte b :
     * bb ) { if ( b < '0' || b>'9' ) { isOld = true; } }
     *
     * if ( isOld ) { System.out.println( id + "	" + postfix ); } else {
     * System.out.println( id + "	" + postfix + "	NEW!"); } }
     *
     *
     * // String driverName = "net.sourceforge.jtds.jdbc.Driver"; // String url
     * = "jdbc:jtds:sqlserver://localhost:2518/HuaxinSite"; // String user =
     * "sa"; // String passwd = "zhaoqi1234"; // // String s = "0123456789"; //
     * StringBuffer sb = new StringBuffer(400000); // for ( int i=0; i<40000;
     * i++ ) { // sb.append(s); // } // conn = DBHelper.getDirectConnection(
     * driverName, url, user, passwd ); // DBHelper.updateClobValue(
     * DBOption.DB_SQLSERVER, conn, "society_info", "society_record",
     * sb.toString(), "id", 9 ); // System.out.println( conn ); //
     * conn.commit(); // // ps = conn.prepareStatement(
     * "select society_record from society_info where id = 9" ); // rs =
     * ps.executeQuery(); // if ( rs.next() ) { // s = DBHelper.getClobValue(rs,
     * "society_record"); // System.out.println( s.length() ); // }
     *
     * } catch (Exception e) { conn.rollback(); e.printStackTrace(); } finally {
     * DBHelper.close( conn, ps, rs ); } }
     */

    /**
     * Close the given JDBC Connection and ignore any thrown exception. This is
     * useful for typical finally blocks in manual JDBC code.
     *
     * @param con
     *            the JDBC Connection to close (may be <code>null</code>)
     */
    public static void closeConnection(Connection con) {
	if (con != null) {
	    try {
		con.close();
	    } catch (SQLException ex) {
		logger.debug("Could not close JDBC Connection", ex);
	    } catch (Throwable ex) {
		// We don't trust the JDBC driver: It might throw
		// RuntimeException or Error.
		logger.debug("Unexpected exception on closing JDBC Connection",
			ex);
	    }
	}
    }

    /**
     * Close statement.
     *
     * @param stmt
     *            the stmt
     */
    public static void closeStatement(Statement stmt) {
	if (stmt != null) {
	    try {
		stmt.close();
	    } catch (SQLException ex) {
		logger.debug("Could not close JDBC Statement", ex);
	    } catch (Throwable ex) {
		// We don't trust the JDBC driver: It might throw
		// RuntimeException or Error.
		logger.debug("Unexpected exception on closing JDBC Statement",
			ex);
	    }
	}
    }

    /**
     * Close result set.
     *
     * @param rs
     *            the rs
     */
    public static void closeResultSet(ResultSet rs) {
	if (rs != null) {
	    try {
		rs.close();
	    } catch (SQLException ex) {
		logger.debug("Could not close JDBC ResultSet", ex);
	    } catch (Throwable ex) {
		// We don't trust the JDBC driver: It might throw
		// RuntimeException or Error.
		logger.debug("Unexpected exception on closing JDBC ResultSet",
			ex);
	    }
	}
    }

    /**
     * Gets the result set value.
     *
     * @param rs
     *            the rs
     * @param index
     *            the index
     * @return the result set value
     * @throws SQLException
     *             the sQL exception
     */
    public static Object getResultSetValue(ResultSet rs, int index)
	    throws SQLException {
	Object obj = rs.getObject(index);
	if (obj instanceof Blob) {
	    obj = rs.getBytes(index);
	} else if (obj instanceof Clob) {
	    obj = rs.getString(index);
	} else if (obj != null
		&& obj.getClass().getName().startsWith("oracle.sql.TIMESTAMP")) {
	    obj = rs.getTimestamp(index);
	} else if (obj != null
		&& obj.getClass().getName().startsWith("oracle.sql.DATE")) {
	    String metaDataClassName = rs.getMetaData().getColumnClassName(
		    index);
	    if ("java.sql.Timestamp".equals(metaDataClassName)
		    || "oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
		obj = rs.getTimestamp(index);
	    } else {
		obj = rs.getDate(index);
	    }
	} else if (obj != null && obj instanceof java.sql.Date) {
	    if ("java.sql.Timestamp".equals(rs.getMetaData()
		    .getColumnClassName(index))) {
		obj = rs.getTimestamp(index);
	    }
	}
	return obj;
    }

    /**
     * Gets the callable statement value.
     *
     * @param cstm
     *            the cstm
     * @param index
     *            the index
     * @return the callable statement value
     * @throws SQLException
     *             the sQL exception
     */
    public static Object getCallableStatementValue(CallableStatement cstm,
	    int index) throws SQLException {
	Object obj = cstm.getObject(index);
	if (obj instanceof Blob) {
	    obj = cstm.getBytes(index);
	} else if (obj instanceof Clob) {
	    obj = cstm.getString(index);
	} else if (obj != null
		&& obj.getClass().getName().startsWith("oracle.sql.TIMESTAMP")) {
	    obj = cstm.getTimestamp(index);
	} else if (obj != null
		&& obj.getClass().getName().startsWith("oracle.sql.DATE")) {
	    String metaDataClassName = cstm.getMetaData().getColumnClassName(
		    index);
	    if ("java.sql.Timestamp".equals(metaDataClassName)
		    || "oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
		obj = cstm.getTimestamp(index);
	    } else {
		obj = cstm.getDate(index);
	    }
	} else if (obj != null && obj instanceof java.sql.Date) {
	    if ("java.sql.Timestamp".equals(cstm.getMetaData()
		    .getColumnClassName(index))) {
		obj = cstm.getTimestamp(index);
	    }
	}
	return obj;
    }

    /**
     * Gets the callable statement value.
     *
     * @param cstm
     *            the cstm
     * @param name
     *            the name
     * @return the callable statement value
     * @throws SQLException
     *             the sQL exception
     */
    public static Object getCallableStatementValue(CallableStatement cstm,
	    String name) throws SQLException {
	Object obj = cstm.getObject(name);
	if (obj instanceof Blob) {
	    obj = cstm.getBytes(name);
	} else if (obj instanceof Clob) {
	    obj = cstm.getString(name);
	} else if (obj != null) {
	    if (obj instanceof java.sql.Timestamp
		    || "java.sql.Timestamp".equals(obj.getClass().getName())) {
		obj = cstm.getTimestamp(name);
	    } else if (obj.getClass().getName()
		    .startsWith("oracle.sql.TIMESTAMP")) {
		obj = cstm.getTimestamp(name);
	    } else if (obj instanceof java.sql.Date
		    || "java.sql.Date".equals(obj.getClass().getName())) {
		obj = cstm.getDate(name);
	    }
	}
	return obj;
    }

    /**
     * Gets the result set value.
     *
     * @param rs
     *            the rs
     * @param name
     *            the name
     * @return the result set value
     * @throws SQLException
     *             the sQL exception
     */
    public static Object getResultSetValue(ResultSet rs, String name)
	    throws SQLException {
	Object obj = rs.getObject(name);
	if (obj instanceof Blob) {
	    obj = rs.getBytes(name);
	} else if (obj instanceof Clob) {
	    obj = rs.getString(name);
	} else if (obj != null) {
	    if (obj instanceof java.sql.Timestamp
		    || "java.sql.Timestamp".equals(obj.getClass().getName())) {
		obj = rs.getTimestamp(name);
	    } else if (obj.getClass().getName()
		    .startsWith("oracle.sql.TIMESTAMP")) {
		obj = rs.getTimestamp(name);
	    } else if (obj instanceof java.sql.Date
		    || "java.sql.Date".equals(obj.getClass().getName())) {
		obj = rs.getDate(name);
	    }
	}
	return obj;
    }

    /**
     * Sets the parameters.
     *
     * @param pstmt
     *            the pstmt
     * @param parameters
     *            the parameters
     * @throws SQLException
     *             the sQL exception
     */
    public static void setParameters(PreparedStatement pstmt,
	    List<Object> parameters) throws SQLException {
	for (int i = 1, size = parameters.size(); i <= size; i++) {
	    Object value = parameters.get(i - 1);
	    if (value instanceof String) {
		pstmt.setString(i, (String) value);
	    } else if (value instanceof Integer) {
		pstmt.setInt(i, ((Integer) value).intValue());
	    } else if (value instanceof Long) {
		pstmt.setLong(i, ((Long) value).longValue());
	    } else if (value instanceof Double) {
		pstmt.setDouble(i, ((Double) value).doubleValue());
	    } else if (value instanceof Float) {
		pstmt.setFloat(i, ((Float) value).floatValue());
	    } else if (value instanceof Short) {
		pstmt.setShort(i, ((Short) value).shortValue());
	    } else if (value instanceof Byte) {
		pstmt.setByte(i, ((Byte) value).byteValue());
	    } else if (value instanceof BigDecimal) {
		pstmt.setBigDecimal(i, (BigDecimal) value);
	    } else if (value instanceof Boolean) {
		pstmt.setBoolean(i, ((Boolean) value).booleanValue());
	    } else if (value instanceof Timestamp) {
		pstmt.setTimestamp(i, (Timestamp) value);
	    } else if (value instanceof java.util.Date) {
		pstmt.setDate(i,
			new java.sql.Date(((java.util.Date) value).getTime()));
	    } else if (value instanceof java.sql.Date) {
		pstmt.setDate(i, (java.sql.Date) value);
	    } else if (value instanceof Time) {
		pstmt.setTime(i, (Time) value);
	    } else if (value instanceof Blob) {
		pstmt.setBlob(i, (Blob) value);
	    } else if (value instanceof Clob) {
		pstmt.setClob(i, (Clob) value);
	    } else {
		pstmt.setObject(i, value);
	    }
	}
    }

    /**
     * Supports batch updates.
     *
     * @param con
     *            the con
     * @return true, if successful
     */
    public static boolean supportsBatchUpdates(Connection con) {
	try {
	    DatabaseMetaData dbmd = con.getMetaData();
	    if (dbmd != null) {
		if (dbmd.supportsBatchUpdates()) {
		    logger.debug("JDBC driver supports batch updates");
		    return true;
		} else {
		    logger.debug("JDBC driver does not support batch updates");
		}
	    }
	} catch (SQLException ex) {
	    logger.debug(
		    "JDBC driver 'supportsBatchUpdates' method threw exception",
		    ex);
	} catch (AbstractMethodError err) {
	    logger.debug(
		    "JDBC driver does not support JDBC 2.0 'supportsBatchUpdates' method",
		    err);
	}
	return false;
    }

    /**
     * Checks if is numeric.
     *
     * @param sqlType
     *            the sql type
     * @return true, if is numeric
     */
    public static boolean isNumeric(int sqlType) {
	return Types.BIT == sqlType || Types.BIGINT == sqlType
		|| Types.DECIMAL == sqlType || Types.DOUBLE == sqlType
		|| Types.FLOAT == sqlType || Types.INTEGER == sqlType
		|| Types.NUMERIC == sqlType || Types.REAL == sqlType
		|| Types.SMALLINT == sqlType || Types.TINYINT == sqlType;
    }

    /**
     * Gets the proc parameters.
     *
     * @param count
     *            the count
     * @param str
     *            the str
     * @return the proc parameters
     */
    public static String getProcParameters(int count, String str) {
	String ret = "";
	while (count-- > 0)
	    ret += "," + str;
	return "(" + ret.substring(1) + ")";
    }
}
