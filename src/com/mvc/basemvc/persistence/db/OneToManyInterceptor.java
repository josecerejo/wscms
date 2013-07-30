/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.mvc.basemvc.persistence.meta.PropertyMapping;


/**
 * @Description 一对多关系代理类，框架内部使用,不要在框架外部调用
 * @ClassName OneToManyInterceptor
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:41:00
 */
public class OneToManyInterceptor extends RelationInterceptor {

    /**
     * @Description
     * @field long serialVersionUID
     * @Created 2013 2013-8-5 下午04:40:47
     */
    private static final long serialVersionUID = -4457470359915087758L;

    /**
     * (non-Javadoc).
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
     * @see com.koolearn.persistence.db.RelationInterceptor#load(java.sql.Connection,
     *      com.koolearn.persistence.meta.PropertyMapping, java.util.List)
     */
    @Override
    public Object load(Connection conn, PropertyMapping propertyMapping,
	    List<Object> foreignColumnValues) throws SQLException {
	return OneToManyLoader.load(conn, propertyMapping, foreignColumnValues);
    }

}
