/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.util;

import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.mvc.basemvc.persistence.db.ResultList;

/**
 * @Description db工具类
 * @ClassName DBUtils
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:03:30
 */
public class DBUtils {

    /** The init ctx. */
    private static Context initCtx = null;

    /** The env ctx. */
    private static Context envCtx = null;

    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(DBUtils.class);

    /**
     * Instantiates a new dB utils.
     */
    public DBUtils() {
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
	Connection conn;

	if (initCtx == null) {
	    initCtx = new InitialContext();
	    envCtx = (Context) initCtx.lookup("java:comp/env");
	}
	DataSource dataSource = (DataSource) envCtx.lookup(datasource);
	try {
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false);
	    return conn;
	} catch (Exception e) {
	    logger.error("Cannot get database connection from datasource. "
		    + e.getMessage());
	    throw e;
	}
    }

    /**
     * Close.
     *
     * @param conn
     *            the conn
     */
    public static void close(Connection conn) {
	if (conn != null) {
	    try {
		conn.close();
	    } catch (Exception exception) {
	    }
	}
    }

    /**
     * Close.
     *
     * @param stmt
     *            the stmt
     */
    public static void close(Statement stmt) {
	if (stmt != null) {
	    try {
		stmt.close();
	    } catch (Exception exception) {
	    }
	}
    }

    /**
     * Close.
     *
     * @param rs
     *            the rs
     */
    public static void close(ResultSet rs) {
	if (rs != null) {
	    try {
		rs.close();
	    } catch (Exception exception) {
	    }
	}
    }

    /**
     * Close.
     *
     * @param ps
     *            the ps
     */
    public static void close(PreparedStatement ps) {
	if (ps != null) {
	    try {
		ps.close();
	    } catch (Exception exception) {
	    }
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
	    if (conn != null) {
		conn.rollback();
	    }
	} catch (Exception exception) {
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
	if (s == null || s.length() == 0) {
	    return s;
	}
	StringBuffer buffer = new StringBuffer(200);
	for (int i = 0; i < s.length(); i++) {
	    char c = s.charAt(i);
	    if (c == '\'') {
		buffer.append(c);
	    }
	    buffer.append(c);
	}

	return buffer.toString();
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
	Clob clobField = null;
	String content = "";
	int count = 0;
	char buffer[] = new char[4086];
	clobField = rs.getClob(clobidx);
	if (clobField != null) {
	    Reader reader = clobField.getCharacterStream();
	    while ((count = reader.read(buffer)) > 0) {
		content = content + new String(buffer, 0, count);
	    }
	}
	return content;
    }

    /**
     * Execute query.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @return the result list
     * @throws SQLException
     *             the sQL exception
     */
    public static ResultList executeQuery(Connection conn, String sql)
	    throws SQLException {
	Statement st;
	ResultSet rs;
	ResultList result;
	st = null;
	rs = null;
	result = new ResultList();
	try {
	    st = conn.createStatement();
	    rs = st.executeQuery(sql);
	    ResultSetMetaData meta = rs.getMetaData();
	    int colCount = meta.getColumnCount();
	    String colName = null;
	    int colType = 0;
	    LinkedHashMap<String, Object> record = null;
	    Object value = null;
	    for (; rs.next(); result.addRecord(record)) {
		record = new LinkedHashMap<String, Object>(colCount);
		for (int i = 1; i <= colCount; i++) {
		    colName = meta.getColumnName(i);
		    colType = meta.getColumnType(i);
		    switch (colType) {
		    case 1: // '\001'
		    case 12: // '\f'
			value = rs.getString(colName);
			break;

		    case 91: // '['
		    case 92: // '\\'
		    case 93: // ']'
			value = rs.getTimestamp(colName);
			break;

		    case 2: // '\002'
		    case 3: // '\003'
		    case 7: // '\007'
		    case 8: // '\b'
			value = new Double(rs.getDouble(colName));
			break;

		    case 6: // '\006'
			value = new Float(rs.getFloat(colName));
			break;

		    case -6:
		    case 4: // '\004'
		    case 5: // '\005'
			value = new Integer(rs.getInt(colName));
			// fall through

		    default:
			value = rs.getObject(colName);
			break;
		    }
		    record.put(colName, value);
		}

	    }

	} catch (SQLException e) {
	    throw e;
	} finally {
	    close(st, rs);
	}
	return result;
    }

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
	PreparedStatement ps;
	ResultSet rs;
	Date date;
	String sql;
	ps = null;
	rs = null;
	date = null;
	sql = null;
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
	    close(conn, ps, rs);
	}
	return date;
    }

}
