/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

import com.mvc.basemvc.persistence.db.DBOperator;
import com.mvc.basemvc.persistence.db.ParameterizedRowMapper;
import com.mvc.basemvc.persistence.db.ProcedureCallBack;
import com.mvc.basemvc.persistence.db.ProcedureResult;
import com.mvc.basemvc.util.ListPager;

/**
 * @Description DAO接口实现类，此接口是Spring框架中DAO的接口实现类的基类，继承此的DAO不需要代码控制事务
 * @ClassName BaseDAOImpl
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午05:02:02
 */
public class BaseDAOImpl implements BaseDAO {

    /** The data source. */
    // private Log log = LogFactory.getLog(BaseDAOImpl.class);

    /** 域 dataSource. */
    protected DataSource dataSource = null;

    /** The Constant DB_ORACLE. */
    private static final int DB_ORACLE = 1;

    /** The Constant DB_SQLSERVER. */
    private static final int DB_SQLSERVER = 2;

    /** The Constant DB_MYSQL. */
    private static final int DB_MYSQL = 3;

    /** The Constant DB_NAME_ORACLE. */
    private static final String DB_NAME_ORACLE = "oracle";

    /** The Constant DB_NAME_SQLSERVER. */
    private static final String DB_NAME_SQLSERVER = "sqlserver";

    /** The Constant DB_NAME_MYSQL. */
    private static final String DB_NAME_MYSQL = "mysql";

    /** The db type. */
    private String dbType = "oracle";

    /** The dboperator. */
    protected DBOperator dboperator = new DBOperator(true, getDBType(dbType));

    /**
     * Gets the dB type.
     *
     * @param name
     *            the name
     * @return the dB type
     */
    private int getDBType(String name) {
	if (DB_NAME_ORACLE.equalsIgnoreCase(name)) {
	    return DB_ORACLE;
	} else if (DB_NAME_SQLSERVER.equalsIgnoreCase(name)) {
	    return DB_SQLSERVER;
	} else if (DB_NAME_MYSQL.equalsIgnoreCase(name)) {
	    return DB_MYSQL;
	}
	return DB_ORACLE;
    }

    /**
     * Checks if is oracle.
     *
     * @return true, if is oracle
     */
    public boolean isOracle() {
	return dbType.equalsIgnoreCase(DB_NAME_ORACLE);
    }

    /**
     * Gets the db type.
     *
     * @return the db type
     */
    public String getDbType() {
	return dbType;
    }

    /**
     * Sets the db type.
     *
     * @param dbType
     *            the new db type
     */
    public void setDbType(String dbType) {
	this.dbType = dbType;
    }

    /**
     * Gets the data source.
     *
     * @return the data source
     */
    public DataSource getDataSource() {
	return dataSource;
    }

    /**
     * Sets the data source.
     *
     * @param dataSource
     *            the new data source
     */
    public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource;
    }

    /**
     * (non-Javadoc).
     *
     * @param entity
     *            the entity
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#delete(java.lang.Object)
     */
    public void delete(Object entity) throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    dboperator.delete(conn, entity);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param <T>
     *            the generic type
     * @param id
     *            the id
     * @param clazz
     *            the clazz
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#deleteById(java.lang.Object,
     *      java.lang.Class)
     */
    public <T> void deleteById(Object id, Class<T> clazz) throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    dboperator.deleteById(conn, id, clazz);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @return the List
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#executeQuery(java.lang.String,
     *      java.lang.Object[])
     */
    public List<Map<String, Object>> executeQuery(String sql,
	    Object[] parameters) throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator.executeQuery(conn, sql, parameters);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @return the Map
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#executeQueryOne(java.lang.String,
     *      java.lang.Object[])
     */
    public Map<String, Object> executeQueryOne(String sql, Object[] parameters)
	    throws SQLException {
	List<Map<String, Object>> list = executeQuery(sql, parameters);
	if (list != null && list.size() > 0) {
	    return list.get(0);
	} else
	    return null;
    }

    /**
     * (non-Javadoc).
     *
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param pager
     *            the pager
     * @return the List
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#executeQueryPage(java.lang.String,
     *      java.lang.Object[], com.koolearn.basemvc2.util.pager.ListPager)
     */
    public List<Map<String, Object>> executeQueryPage(String sql,
	    Object[] parameters, ListPager pager) throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator.executeQueryPage(conn, sql, parameters, pager);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#executeUpdate(java.lang.String,
     *      java.lang.Object[])
     */
    public void executeUpdate(String sql, Object[] parameters)
	    throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    dboperator.executeUpdate(conn, sql, parameters);

	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param sql
     *            the sql
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#executeUpdate(java.lang.String)
     */
    public void executeUpdate(String sql) throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    dboperator.executeUpdate(conn, sql);

	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param sql
     *            the sql
     * @param parametersal
     *            the parametersal
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#executeUpdateBatch(java.lang.String,
     *      java.util.ArrayList)
     */
    public void executeUpdateBatch(String sql, ArrayList<Object[]> parametersal)
	    throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    dboperator.executeUpdateBatch(conn, sql, parametersal);

	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param entity
     *            the entity
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#insert(java.lang.Object)
     */
    public void insert(Object entity) throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    dboperator.insert(conn, entity);

	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param <T>
     *            the generic type
     * @param sql
     *            the sql
     * @param clazz
     *            the clazz
     * @return the List
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#select(java.lang.String,
     *      java.lang.Class)
     */
    public <T> List<T> select(String sql, Class<T> clazz) throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator.select(conn, sql, clazz);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param <T>
     *            the generic type
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param clazz
     *            the clazz
     * @return the List
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#select(java.lang.String,
     *      java.lang.Object[], java.lang.Class)
     */
    public <T> List<T> select(String sql, Object[] parameters, Class<T> clazz)
	    throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator.select(conn, sql, parameters, clazz);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param <T>
     *            the generic type
     * @param orderby
     *            the orderby
     * @param clazz
     *            the clazz
     * @return the List
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#selectAll(java.lang.String,
     *      java.lang.Class)
     */
    public <T> List<T> selectAll(String orderby, Class<T> clazz)
	    throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator.selectAll(conn, orderby, clazz);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param <T>
     *            the generic type
     * @param orderby
     *            the orderby
     * @param clazz
     *            the clazz
     * @param pager
     *            the pager
     * @return the List
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#selectAllPage(java.lang.String,
     *      java.lang.Class, com.koolearn.basemvc2.util.pager.ListPager)
     */
    public <T> List<T> selectAllPage(String orderby, Class<T> clazz,
	    ListPager pager) throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator.selectAllPage(conn, orderby, clazz, pager);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param <T>
     *            the generic type
     * @param id
     *            the id
     * @param clazz
     *            the clazz
     * @return the Object
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#selectById(java.lang.Object,
     *      java.lang.Class)
     */
    public <T> T selectById(Object id, Class<T> clazz) throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator.selectById(conn, id, clazz);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param <T>
     *            the generic type
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param clazz
     *            the clazz
     * @return the Object
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#selectOne(java.lang.String,
     *      java.lang.Object[], java.lang.Class)
     */
    public <T> T selectOne(String sql, Object[] parameters, Class<T> clazz)
	    throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator.selectOne(conn, sql, parameters, clazz);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param <T>
     *            the generic type
     * @param sql
     *            the sql
     * @param clazz
     *            the clazz
     * @return the Object
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#selectOne(java.lang.String,
     *      java.lang.Class)
     */
    public <T> T selectOne(String sql, Class<T> clazz) throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator.selectOne(conn, sql, clazz);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param <T>
     *            the generic type
     * @param sql
     *            the sql
     * @param clazz
     *            the clazz
     * @param pager
     *            the pager
     * @return the List
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#selectPage(java.lang.String,
     *      java.lang.Class, com.koolearn.basemvc2.util.pager.ListPager)
     */
    public <T> List<T> selectPage(String sql, Class<T> clazz, ListPager pager)
	    throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator.selectPage(conn, sql, clazz, pager);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param <T>
     *            the generic type
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param clazz
     *            the clazz
     * @param pager
     *            the pager
     * @return the List
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#selectPage(java.lang.String,
     *      java.lang.Object[], java.lang.Class,
     *      com.koolearn.basemvc2.util.pager.ListPager)
     */
    public <T> List<T> selectPage(String sql, Object[] parameters,
	    Class<T> clazz, ListPager pager) throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator.selectPage(conn, sql, parameters, clazz, pager);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param <T>
     *            the generic type
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
     * @return the List
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#selectPage(java.lang.String,
     *      java.lang.Object[], java.lang.Class, long, long)
     */
    public <T> List<T> selectPage(String sql, Object[] parameters,
	    Class<T> clazz, long from, long to) throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator
		    .selectPage(conn, sql, parameters, clazz, from, to);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param <T>
     *            the generic type
     * @param entity
     *            the entity
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#update(java.lang.Object)
     */
    public <T> void update(T entity) throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    dboperator.update(conn, entity);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @return the long
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#selectTotalRowCount(java.lang.String,
     *      java.lang.Object[])
     */
    public long selectTotalRowCount(String sql, Object[] parameters)
	    throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return DBOperator.selectTotalRowCount(conn, sql, parameters);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param sql
     *            the sql
     * @param parametersal
     *            the parametersal
     * @param batchSize
     *            the batch size
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#executeUpdateBatch(java.lang.String,
     *      java.util.ArrayList, int)
     */
    public void executeUpdateBatch(String sql,
	    ArrayList<Object[]> parametersal, int batchSize)
	    throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    dboperator.executeUpdateBatch(conn, sql, parametersal, batchSize);

	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param procedure
     *            the procedure
     * @param procedureCallBack
     *            the procedure call back
     * @return the ProcedureResult
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#executeProcedure(java.lang.String,
     *      com.koolearn.persistence.db.ProcedureCallBack)
     */
    public ProcedureResult executeProcedure(String procedure,
	    ProcedureCallBack procedureCallBack) throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator
		    .execProc(conn, procedure, null, procedureCallBack);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param procedure
     *            the procedure
     * @param sqlType
     *            the sql type
     * @param procedureCallBack
     *            the procedure call back
     * @return the ProcedureResult
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#executeProcedure(java.lang.String,
     *      int, com.koolearn.persistence.db.ProcedureCallBack)
     */
    public ProcedureResult executeProcedure(String procedure, int sqlType,
	    ProcedureCallBack procedureCallBack) throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator.execProc(conn, procedure, isOracle() ? null
		    : new Integer(sqlType), procedureCallBack);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}

    }

    /**
     * (non-Javadoc).
     *
     * @param <T>
     *            the generic type
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
     * @return the List
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#executeQueryPage(java.lang.String,
     *      java.lang.Object[], com.koolearn.basemvc2.util.pager.ListPager,
     *      com.koolearn.persistence.db.ParameterizedRowMapper, int)
     */
    public <T> List<T> executeQueryPage(String sql, Object[] parameters,
	    ListPager pager, ParameterizedRowMapper<T> rm, int dbType)
	    throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator.executeQueryPage(conn, sql, parameters, pager,
		    rm, dbType);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param <T>
     *            the generic type
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param pager
     *            the pager
     * @param rm
     *            the rm
     * @return the List
     * @throws SQLException
     *             the SQL exception
     * @see com.koolearn.rest.spring.dao.BaseDAO#executeQueryPage(java.lang.String,
     *      java.lang.Object[], com.koolearn.basemvc2.util.pager.ListPager,
     *      com.koolearn.persistence.db.ParameterizedRowMapper)
     */
    public <T> List<T> executeQueryPage(String sql, Object[] parameters,
	    ListPager pager, ParameterizedRowMapper<T> rm) throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator
		    .executeQueryPage(conn, sql, parameters, pager, rm);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param <T>
     *            the generic type
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
     * @see com.koolearn.rest.spring.dao.BaseDAO#selectTop(java.lang.String,
     *      java.lang.Object[], java.lang.Class, int)
     */
    public <T> List<T> selectTop(String sql, Object[] parameters,
	    Class<T> clazz, int nTop) throws SQLException {

	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator.selectTop(conn, sql, parameters, clazz, nTop);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param <T>
     *            the generic type
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
     * @see com.koolearn.rest.spring.dao.BaseDAO#selectTop(java.lang.String,
     *      java.lang.Object[], int,
     *      com.koolearn.persistence.db.ParameterizedRowMapper)
     */
    public <T> List<T> selectTop(String sql, Object[] parameters, int nTop,
	    ParameterizedRowMapper<T> rm) throws SQLException {
	Connection conn = null;
	try {
	    conn = DataSourceUtils.doGetConnection(dataSource);
	    return dboperator.selectTop(conn, sql, parameters, nTop, rm);
	} finally {
	    DataSourceUtils.releaseConnection(conn, dataSource);
	}
    }

}
