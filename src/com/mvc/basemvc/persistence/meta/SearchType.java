/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.meta;

/**
 * @Description 查询类型
 * @ClassName SearchType
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:12:34
 */
public class SearchType {

    /** The Constant EQ. */
    public static final int EQ = 0;

    /** The Constant GE. */
    public static final int GE = 1;

    /** The Constant LE. */
    public static final int LE = 2;

    /** The Constant GT. */
    public static final int GT = 3;

    /** The Constant LT. */
    public static final int LT = 4;

    /** The Constant LIKE. */
    public static final int LIKE = 5;

    /** The Constant LIKE_PRE. */
    public static final int LIKE_PRE = 6;

    /** The Constant LIKE_POST. */
    public static final int LIKE_POST = 7;

    /**
     * Gets the operator.
     *
     * @param type
     *            the type
     * @return the operator
     */
    public static String getOperator(int type) {
	if (type == SearchType.GE) {
	    return ">=";
	} else if (type == SearchType.LE) {
	    return "<=";
	} else if (type == SearchType.GT) {
	    return ">";
	} else if (type == SearchType.LT) {
	    return "<";
	} else if (type == SearchType.LIKE) {
	    return "like";
	} else if (type == SearchType.LIKE_PRE) {
	    return "like";
	} else if (type == SearchType.LIKE_POST) {
	    return "like";
	} else {
	    return "=";
	}
    }

}
