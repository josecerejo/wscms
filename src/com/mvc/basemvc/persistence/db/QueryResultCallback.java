/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @Description 查询回调接口
 * @ClassName QueryResultCallback
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:32:53
 */
public interface QueryResultCallback<T> {

    /**
     * Gets the row.
     *
     * @param rs
     *            the rs
     * @return the row
     * @throws SQLException
     *             the sQL exception
     */
    public T getRow(ResultSet rs) throws SQLException;

}
