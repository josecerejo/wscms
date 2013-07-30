/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;

import java.util.ArrayList;
import java.util.List;


/**
 * @Description 存储过程调用结果封装类
 * @ClassName ProcedureResult
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:36:48
 */
@SuppressWarnings("unchecked")
public class ProcedureResult {

    /** The rs. */
    @SuppressWarnings("rawtypes")
    private List rs = new ArrayList();

    /** The output. */
    @SuppressWarnings("rawtypes")
    private List output = new ArrayList();

    /** The value. */
    private Object value;

    /**
     * Adds the rs.
     *
     * @param list
     *            the list
     */
    @SuppressWarnings("rawtypes")
    public void addRs(List list) {
	if (list.size() > 0)
	    rs.add(list);
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Object getValue() {
	return value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *            the new value
     */
    public void setValue(Object value) {
	this.value = value;
    }

    /**
     * Gets the rs.
     *
     * @return the rs
     */
    @SuppressWarnings("rawtypes")
    public List getRs() {
	if (rs.size() == 1)
	    return (List) rs.get(0);
	return rs;
    }

    /**
     * Gets the output.
     *
     * @return the output
     */
    @SuppressWarnings("rawtypes")
    public List getOutput() {
	return output;
    }
}
