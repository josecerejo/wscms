/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.util;

import java.lang.reflect.Method;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang.ArrayUtils;

/**
 * @Description bean操作工具类
 * @ClassName BaseUtils
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:09:44
 */
public class BaseUtils {

    /** The Constant baseClassNames. */
    private static final String[] baseClassNames = new String[] {
	    "java.lang.String", "int", "java.lang.Integer", "float",
	    "java.lang.Float", "double", "java.lang.Double", "long",
	    "java.lang.Long", "short", "java.lang.Short", "byte",
	    "java.lang.Byte", "boolean", "java.lang.Boolean", "java.util.Date",
	    "java.util.Timestamp", "java.sql.Timestamp", "java.sql.Date",
	    "java.lang.BigDecimal", "[B" // byte[]
    };

    /**
     * Checks if is base type.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return true, if is base type
     */
    public static <T> boolean isBaseType(Class<T> clazz) {
	String className = clazz.getName();
	return ArrayUtils.contains(baseClassNames, className);
    }

    /**
     * Gets the read method.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @param propertyName
     *            the property name
     * @return the read method
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    public static <T> Method getReadMethod(Class<T> clazz, String propertyName)
	    throws InstantiationException, IllegalAccessException {
	Object bean = clazz.newInstance();
	BeanMap beanMap = new BeanMap(bean);
	return beanMap.getReadMethod(propertyName);
    }

    /**
     * Gets the write method.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @param propertyname
     *            the propertyname
     * @return the write method
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    public static <T> Method getWriteMethod(Class<T> clazz, String propertyname)
	    throws InstantiationException, IllegalAccessException {
	Object bean = clazz.newInstance();
	BeanMap beanMap = new BeanMap(bean);
	return beanMap.getWriteMethod(propertyname);
    }

}
