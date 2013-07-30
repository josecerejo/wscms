/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mvc.basemvc.persistence.db.ParameterizedRowMapper;
import com.mvc.basemvc.persistence.db.ProcedureCallBack;
import com.mvc.basemvc.persistence.db.ProcedureResult;
import com.mvc.basemvc.util.ListPager;

/**
 * @Description DAO接口实现类，此接口是Spring框架中DAO的接口实现类的基类，继承此的DAO不需要代码控制事务
 * @ClassName BaseDAO
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午05:02:25
 */
public interface BaseDAO {

    /**
     * Select by id.
     *
     * @param <T>
     *            the generic type
     * @param id
     *            the id
     * @param clazz
     *            the clazz
     * @return the t
     * @throws SQLException
     *             the sQL exception
     */
    <T> T selectById(Object id, Class<T> clazz) throws SQLException;

    /**
     * Select all.
     *
     * @param <T>
     *            the generic type
     * @param orderby
     *            the orderby
     * @param clazz
     *            the clazz
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    <T> List<T> selectAll(String orderby, Class<T> clazz) throws SQLException;

    /**
     * Insert.
     *
     * @param <T>
     *            the generic type
     * @param entity
     *            the entity
     * @throws SQLException
     *             the sQL exception
     */
    <T> void insert(T entity) throws SQLException;

    /**
     * Delete.
     *
     * @param <T>
     *            the generic type
     * @param entity
     *            the entity
     * @throws SQLException
     *             the sQL exception
     */
    <T> void delete(T entity) throws SQLException;

    /**
     * Delete by id.
     *
     * @param <T>
     *            the generic type
     * @param id
     *            the id
     * @param clazz
     *            the clazz
     * @throws SQLException
     *             the sQL exception
     */
    <T> void deleteById(Object id, Class<T> clazz) throws SQLException;

    /**
     * Update.
     *
     * @param <T>
     *            the generic type
     * @param entity
     *            the entity
     * @throws SQLException
     *             the sQL exception
     */
    <T> void update(T entity) throws SQLException;

    /**
     * Select.
     *
     * @param <T>
     *            the generic type
     * @param sql
     *            the sql
     * @param clazz
     *            the clazz
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    <T> List<T> select(String sql, Class<T> clazz) throws SQLException;

    /**
     * Select page.
     *
     * @param <T>
     *            the generic type
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
    <T> List<T> selectPage(String sql, Class<T> clazz, ListPager pager)
	    throws SQLException;

    /**
     * Select.
     *
     * @param <T>
     *            the generic type
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
    <T> List<T> select(String sql, Object[] parameters, Class<T> clazz)
	    throws SQLException;

    /**
     * Select all page.
     *
     * @param <T>
     *            the generic type
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
    <T> List<T> selectAllPage(String orderby, Class<T> clazz, ListPager pager)
	    throws SQLException;

    /**
     * Select page.
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
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    <T> List<T> selectPage(String sql, Object[] parameters, Class<T> clazz,
	    ListPager pager) throws SQLException;

    /**
     * Select page.
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
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    <T> List<T> selectPage(String sql, Object[] parameters, Class<T> clazz,
	    long from, long to) throws SQLException;

    /**
     * Select one.
     *
     * @param <T>
     *            the generic type
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
    <T> T selectOne(String sql, Object[] parameters, Class<T> clazz)
	    throws SQLException;

    /**
     * Select one.
     *
     * @param <T>
     *            the generic type
     * @param sql
     *            the sql
     * @param clazz
     *            the clazz
     * @return the t
     * @throws SQLException
     *             the sQL exception
     */
    <T> T selectOne(String sql, Class<T> clazz) throws SQLException;

    /**
     * Execute query.
     *
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    List<Map<String, Object>> executeQuery(String sql, Object[] parameters)
	    throws SQLException;

    /**
     * Execute query one.
     *
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @return the map
     * @throws SQLException
     *             the sQL exception
     */
    Map<String, Object> executeQueryOne(String sql, Object[] parameters)
	    throws SQLException;

    /**
     * Execute query page.
     *
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @param pager
     *            the pager
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    List<Map<String, Object>> executeQueryPage(String sql, Object[] parameters,
	    ListPager pager) throws SQLException;

    /**
     * Execute update.
     *
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @throws SQLException
     *             the sQL exception
     */
    void executeUpdate(String sql, Object[] parameters) throws SQLException;

    /**
     * Execute update batch.
     *
     * @param sql
     *            the sql
     * @param parametersal
     *            the parametersal
     * @throws SQLException
     *             the sQL exception
     */
    void executeUpdateBatch(String sql, ArrayList<Object[]> parametersal)
	    throws SQLException;

    /**
     * Execute update batch.
     *
     * @param sql
     *            the sql
     * @param parametersal
     *            the parametersal
     * @param batchSize
     *            the batch size
     * @throws SQLException
     *             the sQL exception
     */
    void executeUpdateBatch(String sql, ArrayList<Object[]> parametersal,
	    int batchSize) throws SQLException;

    /**
     * Execute update.
     *
     * @param sql
     *            the sql
     * @throws SQLException
     *             the sQL exception
     */
    void executeUpdate(String sql) throws SQLException;

    /**
     * Select total row count.
     *
     * @param sql
     *            the sql
     * @param parameters
     *            the parameters
     * @return the long
     * @throws SQLException
     *             the sQL exception
     */
    long selectTotalRowCount(String sql, Object[] parameters)
	    throws SQLException;

    /**
     * Execute procedure.
     *
     * @param procedure
     *            the procedure
     * @param procedureCallBack
     *            the procedure call back
     * @return the procedure result
     * @throws SQLException
     *             the sQL exception
     */
    ProcedureResult executeProcedure(String procedure,
	    ProcedureCallBack procedureCallBack) throws SQLException;

    /**
     * Execute procedure.
     *
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
    ProcedureResult executeProcedure(String procedure, int sqlType,
	    ProcedureCallBack procedureCallBack) throws SQLException;

    /**
     * Execute query page.
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
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    <T> List<T> executeQueryPage(String sql, Object[] parameters,
	    ListPager pager, ParameterizedRowMapper<T> rm, int dbType)
	    throws SQLException;

    /**
     * Execute query page.
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
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    <T> List<T> executeQueryPage(String sql, Object[] parameters,
	    ListPager pager, ParameterizedRowMapper<T> rm) throws SQLException;

    /**
     * Select top.
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
     */
    <T> List<T> selectTop(String sql, Object[] parameters, Class<T> clazz,
	    int nTop) throws SQLException;

    /**
     * Select top.
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
     */
    <T> List<T> selectTop(String sql, Object[] parameters, int nTop,
	    ParameterizedRowMapper<T> rm) throws SQLException;
}
