/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.spring;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * @Description spring容器，提供connection
 * @ClassName SpringContext
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:01:28
 */
public class SpringContext implements ApplicationContextAware {

    /** The log. */
    private static Log log = LogFactory.getLog(SpringContext.class);

    /** The context. */
    protected static ApplicationContext context;

    /** The data source. */
    private static DataSource dataSource;

    /**
     * Gets the data source.
     * @return the data source
     */
    public DataSource getDataSource() {
    	return dataSource;
    }

    /**
     * Sets the data source.
     * @param dataSource
     *            the new data source
     */
    @SuppressWarnings("static-access")
    public void setDataSource(DataSource dataSource) {
    	this.dataSource = dataSource;
    }

    /**
     * Gets the context.
     * @return the context
     */
    public static ApplicationContext getContext() {
    	return context;
    }

    /**
     * (non-Javadoc).
     * @param arg0
     *            the new application context
     * @throws BeansException
     *             the beans exception
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    public void setApplicationContext(ApplicationContext arg0)
	    throws BeansException {
    	context = arg0;
    }

    /**
     * Gets the connection.
     * @return the connection
     */
    public static Connection getConnection() {
		Connection conn = null;
		try {
		    if (dataSource != null) {
			conn = DataSourceUtils.doGetConnection(dataSource);
			if (conn != null)
			    conn.setAutoCommit(false);
		    }
		} catch (Exception ex) {
		    conn = null;
		}
		return conn;
    }

    /**
     * Close connection.
     * @param conn
     *            the conn
     */
    public static void closeConnection(Connection conn) {
		try {
		    if (dataSource != null)
			DataSourceUtils.releaseConnection(conn, dataSource);
		} catch (Exception ex) {
		    log.error(ex);
		}
    }

    /**
     * Find bean.
     * @param beanid
     *            the beanid
     * @return the object
     */
    public static Object findBean(String beanid) {
		if (context == null)
		    return null;
		if (beanid == null || "".equals(beanid))
		    return null;
		Object ref = null;
		try {
		    ref = context.getBean(beanid);
		} catch (Exception ex) {
		    ref = null;
		}
		return ref;
    }

}
