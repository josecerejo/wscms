/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mvc.basemvc.persistence.db.DBOperator;
import com.mvc.basemvc.persistence.db.DBOption;
import com.mvc.basemvc.persistence.db.ParameterizedRowMapper;
import com.mvc.basemvc.persistence.db.ProcedureCallBack;
import com.mvc.basemvc.persistence.db.ProcedureResult;
import com.mvc.basemvc.persistence.db.QueryResultCallback;
import com.mvc.basemvc.persistence.util.DBHelper;
import com.mvc.basemvc.util.ListPager;

/**
 * @Description 获取持久层的静态方法类，框架内部使用,不要在框架外部调用
 * @ClassName DBAccessor
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午05:03:41
 */
public class DBAccessor {

    /**
     * Gets the dB operator.
     *
     * @return the dB operator
     */
    private static DBOperator getDBOperator() {
	int dbType = DBOption.DB_ORACLE;
	dbType = DBHelper.getDBType();
	return new DBOperator(true, dbType);
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
    public static <T> List<T> select(Connection conn, String sql,
	    Object[] parameters, Class<T> clazz) throws SQLException {
	return getDBOperator().select(conn, sql, parameters, clazz);
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
    public static <T> List<T> selectPage(Connection conn, String sql,
	    Object[] parameters, Class<T> clazz, ListPager pager)
	    throws SQLException {
	return getDBOperator().selectPage(conn, sql, parameters, clazz, pager);
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
    public static <T> List<T> select(Connection conn, String sql, Class<T> clazz)
	    throws SQLException {
	return getDBOperator().select(conn, sql, clazz);
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
    public static <T> List<T> selectPage(Connection conn, String sql,
	    Class<T> clazz, ListPager pager) throws SQLException {
	return getDBOperator().selectPage(conn, sql, clazz, pager);
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
    public static <T> T selectOne(Connection conn, String sql,
	    Object[] parameters, Class<T> clazz) throws SQLException {
	return getDBOperator().selectOne(conn, sql, parameters, clazz);
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
    public static <T> List<T> selectPage(Connection conn, String sql,
	    Object[] parameters, Class<T> clazz, long from, long to)
	    throws SQLException {
	return getDBOperator().selectPage(conn, sql, parameters, clazz, from,
		to);
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
    public static <T> T selectOne(Connection conn, String sql, Class<T> clazz)
	    throws SQLException {
	return getDBOperator().selectOne(conn, sql, clazz);
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
	return DBOperator.selectTotalRowCount(conn, sql, parameters);
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
    public static List<Map<String, Object>> executeQuery(Connection conn,
	    String sql, Object[] parameters) {
	return getDBOperator().queryPage(conn, sql, parameters, null);
    }

    /**
     * Execute query one.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @return the map
     */
    public static Map<String, Object> executeQueryOne(Connection conn,
	    String sql, Object[] parameters) {
	List<Map<String, Object>> list = executeQuery(conn, sql, parameters);
	if (list != null && list.size() > 0)
	    return list.get(0);
	return null;
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
    public static List<Map<String, Object>> executeQueryPage(Connection conn,
	    String sql, Object[] parameters, ListPager pager) {
	return getDBOperator().queryPage(conn, sql, parameters, pager);
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
    public static List<Map<String, Object>> executeQueryPage(Connection conn,
	    String sql, Object[] parameters, int from, int to) {
	return getDBOperator()
		.executeQueryPage(conn, sql, parameters, from, to);
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
    public static <T> List<T> executeQueryPage(Connection conn, String sql,
	    Object[] parameters, ListPager pager,
	    QueryResultCallback<T> callback) throws SQLException {
	return getDBOperator().executeQueryPage(conn, sql, parameters, pager,
		callback);
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
    public static <T> List<T> selectAll(Connection conn, String orderby,
	    Class<T> clazz) throws SQLException {
	return getDBOperator().selectAll(conn, orderby, clazz);
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
    public static <T> List<T> selectAllPage(Connection conn, String orderby,
	    Class<T> clazz, ListPager pager) throws SQLException {
	return getDBOperator().selectAllPage(conn, orderby, clazz, pager);
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
    public static <T> T selectById(Connection conn, Object id, Class<T> clazz)
	    throws SQLException {
	return getDBOperator().selectById(conn, id, clazz);
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
    public static void executeUpdate(Connection conn, String sql,
	    Object[] parameters) throws SQLException {
	getDBOperator().executeUpdate(conn, sql, parameters);
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
    public static void executeUpdateBatch(Connection conn, String sql,
	    ArrayList<Object[]> parametersal) throws SQLException {
	getDBOperator().executeUpdateBatch(conn, sql, parametersal);
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
    public static void executeUpdateBatch(Connection conn, String sql,
	    ArrayList<Object[]> parametersal, int batchSize)
	    throws SQLException {
	getDBOperator().executeUpdateBatch(conn, sql, parametersal, batchSize);
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
    public static void executeUpdate(Connection conn, String sql)
	    throws SQLException {
	getDBOperator().executeUpdate(conn, sql);
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
    public static void delete(Connection conn, Object entity)
	    throws SQLException {
	getDBOperator().delete(conn, entity);
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
    public static <T> void deleteById(Connection conn, Object id, Class<T> clazz)
	    throws SQLException {
	getDBOperator().deleteById(conn, id, clazz);
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
    public static void insert(Connection conn, Object entity)
	    throws SQLException {
	getDBOperator().insert(conn, entity);
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
    public static void update(Connection conn, Object entity)
	    throws SQLException {
	getDBOperator().update(conn, entity);
    }

    /**
     * Execute procedure.
     *
     * @param conn
     *            the conn
     * @param procedure
     *            the procedure
     * @param procedureCallBack
     *            the procedure call back
     * @return the procedure result
     * @throws SQLException
     *             the sQL exception
     */
    /*
     * public static List search(Connection conn, Object searchFilter, String
     * orderBy, ListPager pager) throws SQLException { // TODO // return
     * getDBOperator().search(conn, searchFilter, orderBy, pager); return null;
     * }
     */

    /**
     * 执行查询语句.
     *
     * @param conn
     *            the conn 数据库连接
     * @param searchFilter
     *            the search filter 查询条件封装类
     * @param orderBy
     *            the order by 排序条件
     * @param pager
     *            the pager 分页数据封装类
     * @return the List 记录集
     * @throws SQLException
     *             the SQL exception
     * @deprecated
     */
    /*
     * public static List search(Connection conn, Object searchFilter, String
     * sql, String orderBy, ListPager pager) throws SQLException { // TODO //
     * return getDBOperator().search(conn, searchFilter, sql, orderBy, //
     * pager); return null; }
     */

    /**
     * 执行存储过程.
     *
     * @param conn
     *            the conn 数据库连接
     * @param procedure
     *            the procedure 存储过程名称
     * @param procedureCallBack
     *            内嵌回调类
     * @return the ProcedureResult 返回值封装类
     * @throws SQLException
     *             the SQL exception
     */
    public static ProcedureResult executeProcedure(Connection conn,
	    String procedure, ProcedureCallBack procedureCallBack)
	    throws SQLException {
	return getDBOperator().execProc(conn, procedure, null,
		procedureCallBack);

    }

    /**
     * Execute procedure.
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
    public static ProcedureResult executeProcedure(Connection conn,
	    String procedure, int sqlType, ProcedureCallBack procedureCallBack)
	    throws SQLException {
	return getDBOperator().execProc(conn, procedure, sqlType,
		procedureCallBack);

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
    public static <T> List<T> executeQueryPage(Connection conn, String sql,
	    Object[] parameters, ListPager pager, ParameterizedRowMapper<T> rm,
	    int dbType) throws SQLException {
	return getDBOperator().executeQueryPage(conn, sql, parameters, pager,
		rm, dbType);
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
    public static <T> List<T> executeQueryPage(Connection conn, String sql,
	    Object[] parameters, ListPager pager, ParameterizedRowMapper<T> rm)
	    throws SQLException {
	return getDBOperator().executeQueryPage(conn, sql, parameters, pager,
		rm);
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
    public static <T> List<T> selectTop(Connection conn, String sql,
	    Object[] parameters, Class<T> clazz, int nTop) throws SQLException {
	return getDBOperator().selectTop(conn, sql, parameters, clazz, nTop);
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
    public static <T> List<T> selectTop(Connection conn, String sql,
	    Object[] parameters, int nTop, ParameterizedRowMapper<T> rm)
	    throws SQLException {
	return getDBOperator().selectTop(conn, sql, parameters, nTop, rm);
    }

}
