/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;

/**
 * @Description 数据库行记录回调接口
 * @ClassName ParameterizedRowMapper
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:37:40
 */
public interface ParameterizedRowMapper<T> {

    /**
     * Map row.
     *
     * @param rs
     *            the rs
     * @param rowNum
     *            the row num
     * @return the t
     */
    T mapRow(NoCaseMap<String, Object> rs, int rowNum);
}
