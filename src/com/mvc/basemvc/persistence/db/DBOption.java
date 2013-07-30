/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;


/**
 * @Description db类型
 * @ClassName DBOption
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:58:42
 */
public class DBOption {

    /** The Constant DB_ORACLE. */
    public static final int DB_ORACLE = 1;

    /** The Constant DB_SQLSERVER. */
    public static final int DB_SQLSERVER = 2;

    /** The Constant DB_MYSQL. */
    public static final int DB_MYSQL = 3;

    /** The Constant DB_NAME_ORACLE. */
    public static final String DB_NAME_ORACLE = "oracle";

    /** The Constant DB_NAME_SQLSERVER. */
    public static final String DB_NAME_SQLSERVER = "sqlserver";

    /** The Constant DB_NAME_MYSQL. */
    public static final String DB_NAME_MYSQL = "mysql";

    /** The Constant DB_DRIVER_ORACLE. */
    public static final String DB_DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";

    /** The Constant DB_DRIVER_SQLSERVER. */
    public static final String DB_DRIVER_SQLSERVER = "net.sourceforge.jtds.jdbc.Driver";

    /** The Constant DB_DRIVER_MYSQL. */
    public static final String DB_DRIVER_MYSQL = "com.mysql.jdbc.Driver";

    /**
     * Instantiates a new dB option.
     */
    public DBOption() {
    }

    /**
     * Gets the dB type.
     *
     * @param name
     *            the name
     * @return the dB type
     */
    public static int getDBType(String name) {
	if ("oracle".equalsIgnoreCase(name)) {
	    return 1;
	}
	if ("sqlserver".equalsIgnoreCase(name)) {
	    return 2;
	}
	return !"mysql".equalsIgnoreCase(name) ? 1 : 3;
    }

    /**
     * Gets the dB type name.
     *
     * @param type
     *            the type
     * @return the dB type name
     */
    public static String getDBTypeName(int type) {
	if (type == 1) {
	    return "oracle";
	}
	if (type == 2) {
	    return "sqlserver";
	}
	if (type == 3) {
	    return "mysql";
	} else {
	    return "oracle";
	}
    }
}
