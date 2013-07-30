/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * @Description 存储过程内嵌回调类
 * @ClassName ProcedureCallBack
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:37:15
 */
public abstract class ProcedureCallBack {

    /** The db type. */
    public String dbType = "oracle";

    /** The ndb type. */
    public int ndbType = 1;

    /** The cstm. */
    public CallableStatement cstm;

    /** The start parameters idx. */
    public int startParametersIdx = 0;

    /** The parameters count. */
    public int parametersCount;

    /** The out parameters. */
    public Map<Integer, Integer> outParameters = new HashMap<Integer, Integer>();

    /**
     * Adds the parameters count.
     */
    public void addParametersCount() {
	parametersCount++;
    };

    /**
     * Gets the parameter index.
     *
     * @param parameterIndex
     *            the parameter index
     * @return the parameter index
     */
    public int getParameterIndex(int parameterIndex) {
	return parameterIndex + startParametersIdx;
    }

    /**
     * Register out parameter.
     *
     * @param parameterIndex
     *            the parameter index
     * @param sqlType
     *            the sql type
     * @throws SQLException
     *             the sQL exception
     */
    public void registerOutParameter(int parameterIndex, int sqlType)
	    throws SQLException {
	if (cstm != null) {
	    parameterIndex = getParameterIndex(parameterIndex);
	    cstm.registerOutParameter(parameterIndex, sqlType);
	    outParameters.put(parameterIndex, new Integer(sqlType));
	}
	addParametersCount();
    };

    /**
     * Adds the oracle cursor.
     *
     * @param parameterIndex
     *            the parameter index
     * @throws SQLException
     *             the sQL exception
     */
    public void addOracleCursor(int parameterIndex) throws SQLException {
	if (!dbType.equalsIgnoreCase("oracle"))
	    return;
	if (ndbType != DBOption.DB_ORACLE)
	    return;
	if (cstm != null) {
	    parameterIndex = getParameterIndex(parameterIndex);
	    cstm.registerOutParameter(parameterIndex,
		    oracle.jdbc.OracleTypes.CURSOR);
	    outParameters.put(parameterIndex, new Integer(
		    oracle.jdbc.OracleTypes.CURSOR));
	}
	addParametersCount();
    };

    /**
     * Register out parameter.
     *
     * @param parameterIndex
     *            the parameter index
     * @param sqlType
     *            the sql type
     * @param scale
     *            the scale
     * @throws SQLException
     *             the sQL exception
     */
    public void registerOutParameter(int parameterIndex, int sqlType, int scale)
	    throws SQLException {
	if (cstm != null)
	    cstm.registerOutParameter(getParameterIndex(parameterIndex),
		    sqlType, scale);
	outParameters.put(new Integer(parameterIndex), new Integer(sqlType));
	addParametersCount();
    };

    /**
     * Register parameter.
     *
     * @throws SQLException
     *             the sQL exception
     */
    public abstract void registerParameter() throws SQLException;

    /**
     * Map row.
     *
     * @param rs
     *            the rs
     * @param rsIndex
     *            the rs index
     * @return the object
     * @throws SQLException
     *             the sQL exception
     */
    public abstract Object mapRow(ResultSet rs, int rsIndex)
	    throws SQLException;

    /**
     * Gets the out parameters.
     *
     * @return the out parameters
     */
    public Map<Integer, Integer> getOutParameters() {
	return outParameters;
    }

    /**
     * Sets the null.
     *
     * @param parameterIndex
     *            the parameter index
     * @param sqlType
     *            the sql type
     * @throws SQLException
     *             the sQL exception
     */
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
	if (cstm != null)
	    cstm.setNull(getParameterIndex(parameterIndex), sqlType);
	addParametersCount();
    }

    /**
     * Sets the boolean.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
	if (cstm != null)
	    cstm.setBoolean(getParameterIndex(parameterIndex), x);
	addParametersCount();
    };

    /**
     * Sets the byte.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setByte(int parameterIndex, byte x) throws SQLException {
	if (cstm != null)
	    cstm.setByte(getParameterIndex(parameterIndex), x);
	addParametersCount();
    };

    /**
     * Sets the short.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setShort(int parameterIndex, short x) throws SQLException {
	if (cstm != null)
	    cstm.setShort(getParameterIndex(parameterIndex), x);
	addParametersCount();
    };

    /**
     * Sets the int.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setInt(int parameterIndex, int x) throws SQLException {
	if (cstm != null)
	    cstm.setInt(getParameterIndex(parameterIndex), x);
	addParametersCount();
    };

    /**
     * Sets the long.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setLong(int parameterIndex, long x) throws SQLException {
	if (cstm != null)
	    cstm.setLong(getParameterIndex(parameterIndex), x);
	addParametersCount();
    };

    /**
     * Sets the float.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setFloat(int parameterIndex, float x) throws SQLException {
	if (cstm != null)
	    cstm.setFloat(getParameterIndex(parameterIndex), x);
	addParametersCount();
    };

    /**
     * Sets the double.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setDouble(int parameterIndex, double x) throws SQLException {
	if (cstm != null)
	    cstm.setDouble(getParameterIndex(parameterIndex), x);
	addParametersCount();
    };

    /**
     * Sets the big decimal.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setBigDecimal(int parameterIndex, BigDecimal x)
	    throws SQLException {
	if (cstm != null)
	    cstm.setBigDecimal(getParameterIndex(parameterIndex), x);
	addParametersCount();
    };

    /**
     * Sets the string.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setString(int parameterIndex, String x) throws SQLException {
	if (cstm != null)
	    cstm.setString(getParameterIndex(parameterIndex), x);
	addParametersCount();
    };

    /**
     * Sets the bytes.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setBytes(int parameterIndex, byte x[]) throws SQLException {
	if (cstm != null)
	    cstm.setBytes(getParameterIndex(parameterIndex), x);
	addParametersCount();
    };

    /**
     * Sets the date.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setDate(int parameterIndex, java.sql.Date x)
	    throws SQLException {
	if (cstm != null)
	    cstm.setDate(getParameterIndex(parameterIndex), x);
	addParametersCount();
    };

    /**
     * Sets the time.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setTime(int parameterIndex, java.sql.Time x)
	    throws SQLException {
	if (cstm != null)
	    cstm.setTime(getParameterIndex(parameterIndex), x);
	addParametersCount();
    };

    /**
     * Sets the timestamp.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setTimestamp(int parameterIndex, java.sql.Timestamp x)
	    throws SQLException {
	if (cstm != null)
	    cstm.setTimestamp(getParameterIndex(parameterIndex), x);
	addParametersCount();
    };

    /**
     * Sets the ascii stream.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @param length
     *            the length
     * @throws SQLException
     *             the sQL exception
     */
    public void setAsciiStream(int parameterIndex, java.io.InputStream x,
	    int length) throws SQLException {
	if (cstm != null)
	    cstm.setAsciiStream(getParameterIndex(parameterIndex), x, length);
	addParametersCount();
    };

    /**
     * Sets the binary stream.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @param length
     *            the length
     * @throws SQLException
     *             the sQL exception
     */
    public void setBinaryStream(int parameterIndex, java.io.InputStream x,
	    int length) throws SQLException {
	if (cstm != null)
	    cstm.setAsciiStream(getParameterIndex(parameterIndex), x, length);
	addParametersCount();
    };

    // ----------------------------------------------------------------------
    // Advanced features:

    /**
     * Sets the object.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @param targetSqlType
     *            the target sql type
     * @param scale
     *            the scale
     * @throws SQLException
     *             the sQL exception
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType,
	    int scale) throws SQLException {
	if (cstm != null)
	    cstm.setObject(getParameterIndex(parameterIndex), x, targetSqlType,
		    scale);
	addParametersCount();
    };

    /**
     * Sets the object.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @param targetSqlType
     *            the target sql type
     * @throws SQLException
     *             the sQL exception
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType)
	    throws SQLException {
	if (cstm != null)
	    cstm.setObject(getParameterIndex(parameterIndex), x, targetSqlType);
	addParametersCount();
    };

    /**
     * Sets the object.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setObject(int parameterIndex, Object x) throws SQLException {
	if (cstm != null)
	    cstm.setObject(getParameterIndex(parameterIndex), x);
	addParametersCount();
    };

    // --------------------------JDBC 2.0-----------------------------

    /**
     * Sets the character stream.
     *
     * @param parameterIndex
     *            the parameter index
     * @param reader
     *            the reader
     * @param length
     *            the length
     * @throws SQLException
     *             the sQL exception
     */
    public void setCharacterStream(int parameterIndex, java.io.Reader reader,
	    int length) throws SQLException {
	if (cstm != null)
	    cstm.setCharacterStream(getParameterIndex(parameterIndex), reader,
		    length);
	addParametersCount();
    };

    /**
     * Sets the ref.
     *
     * @param i
     *            the i
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setRef(int i, Ref x) throws SQLException {
	if (cstm != null)
	    cstm.setRef(getParameterIndex(i), x);
	addParametersCount();
    };

    /**
     * Sets the blob.
     *
     * @param i
     *            the i
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setBlob(int i, Blob x) throws SQLException {
	if (cstm != null)
	    cstm.setBlob(getParameterIndex(i), x);
	addParametersCount();
    };

    /**
     * Sets the clob.
     *
     * @param i
     *            the i
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setClob(int i, Clob x) throws SQLException {
	if (cstm != null)
	    cstm.setClob(getParameterIndex(i), x);
	addParametersCount();
    };

    /**
     * Sets the array.
     *
     * @param i
     *            the i
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setArray(int i, Array x) throws SQLException {
	if (cstm != null)
	    cstm.setArray(getParameterIndex(i), x);
	addParametersCount();
    };

    /**
     * Sets the date.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @param cal
     *            the cal
     * @throws SQLException
     *             the sQL exception
     */
    public void setDate(int parameterIndex, java.sql.Date x, Calendar cal)
	    throws SQLException {
	if (cstm != null)
	    cstm.setDate(getParameterIndex(parameterIndex), x, cal);
	addParametersCount();
    };

    /**
     * Sets the time.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @param cal
     *            the cal
     * @throws SQLException
     *             the sQL exception
     */
    public void setTime(int parameterIndex, java.sql.Time x, Calendar cal)
	    throws SQLException {
	if (cstm != null)
	    cstm.setTime(getParameterIndex(parameterIndex), x, cal);
	addParametersCount();
    };

    /**
     * Sets the timestamp.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @param cal
     *            the cal
     * @throws SQLException
     *             the sQL exception
     */
    public void setTimestamp(int parameterIndex, java.sql.Timestamp x,
	    Calendar cal) throws SQLException {
	if (cstm != null)
	    cstm.setTimestamp(getParameterIndex(parameterIndex), x, cal);
	addParametersCount();
    };

    /**
     * Sets the null.
     *
     * @param paramIndex
     *            the param index
     * @param sqlType
     *            the sql type
     * @param typeName
     *            the type name
     * @throws SQLException
     *             the sQL exception
     */
    public void setNull(int paramIndex, int sqlType, String typeName)
	    throws SQLException {
	if (cstm != null)
	    cstm.setNull(getParameterIndex(paramIndex), sqlType, typeName);
	addParametersCount();
    };

    // ------------------------- JDBC 3.0 -----------------------------------

    /**
     * Sets the url.
     *
     * @param parameterIndex
     *            the parameter index
     * @param x
     *            the x
     * @throws SQLException
     *             the sQL exception
     */
    public void setURL(int parameterIndex, java.net.URL x) throws SQLException {
	if (cstm != null)
	    cstm.setURL(getParameterIndex(parameterIndex), x);
	addParametersCount();
    }
}
