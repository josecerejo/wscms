/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.cglib.proxy.CallbackFilter;


/**
 * @Description 延迟加载类的回调Filter，框架内部使用,不要在框架外部调用
 * @ClassName LazyLoadCallbackFilter
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:55:14
 */
public class LazyLoadCallbackFilter implements CallbackFilter {

    /** The Constant OP_NO. */
    private final static int OP_NO = 0;

    /** The Constant OP_ONE_TO_ONE. */
    private final static int OP_ONE_TO_ONE = 1;

    /** The Constant OP_ONE_TO_MANY. */
    private final static int OP_ONE_TO_MANY = 2;

    /** The Constant OP_MANY_TO_MANY. */
    private final static int OP_MANY_TO_MANY = 3;

    /** The Constant OP_LOB. */
    private final static int OP_LOB = 4;

    /** The oo read methods. */
    private Map<Method, String> ooReadMethods = new LinkedHashMap<Method, String>();

    /** The om read methods. */
    private Map<Method, String> omReadMethods = new LinkedHashMap<Method, String>();

    /** The mm read methods. */
    private Map<Method, String> mmReadMethods = new LinkedHashMap<Method, String>();

    /** The lob read methods. */
    private Map<Method, String> lobReadMethods = new LinkedHashMap<Method, String>();

    /**
     * (non-Javadoc).
     *
     * @param method
     *            the method
     * @return the int
     * @see net.sf.cglib.proxy.CallbackFilter#accept(java.lang.reflect.Method)
     */
    public int accept(Method method) {
	if (ooReadMethods.containsKey(method)) {
	    return OP_ONE_TO_ONE;
	} else if (omReadMethods.containsKey(method)) {
	    return OP_ONE_TO_MANY;
	} else if (mmReadMethods.containsKey(method)) {
	    return OP_MANY_TO_MANY;
	} else if (lobReadMethods.containsKey(method)) {
	    return OP_LOB;
	} else {
	    return OP_NO;
	}
    }

    /**
     * Adds the one to one method.
     *
     * @param readMethod
     *            the read method
     */
    public void addOneToOneMethod(Method readMethod) {
	ooReadMethods.put(readMethod, "");
    }

    /**
     * Adds the one to many method.
     *
     * @param readMethod
     *            the read method
     */
    public void addOneToManyMethod(Method readMethod) {
	omReadMethods.put(readMethod, "");
    }

    /**
     * Adds the many to many method.
     *
     * @param readMethod
     *            the read method
     */
    public void addManyToManyMethod(Method readMethod) {
	mmReadMethods.put(readMethod, "");
    }

    /**
     * Adds the lob method.
     *
     * @param readMethod
     *            the read method
     */
    public void addLobMethod(Method readMethod) {
	lobReadMethods.put(readMethod, "");
    }

    /**
     * (non-Javadoc).
     *
     * @param o
     *            the o
     * @return true, if successful
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
	if (!(o instanceof LazyLoadCallbackFilter)) {
	    return false;
	}

	LazyLoadCallbackFilter filter = (LazyLoadCallbackFilter) o;

	if (equalsMapkey(this.ooReadMethods, filter.ooReadMethods)
		&& equalsMapkey(this.omReadMethods, filter.omReadMethods)
		&& equalsMapkey(this.mmReadMethods, filter.mmReadMethods)
		&& equalsMapkey(this.lobReadMethods, filter.lobReadMethods)) {
	    return true;
	}

	return false;
    }

    /**
     * Equals mapkey.
     *
     * @param m1
     *            the m1
     * @param m2
     *            the m2
     * @return true, if successful
     */
    @SuppressWarnings("rawtypes")
    private static boolean equalsMapkey(Map m1, Map m2) {
	Object k = null;
	for (Iterator iter = m1.keySet().iterator(); iter.hasNext();) {
	    k = iter.next();
	    if (!m2.containsKey(k)) {
		return false;
	    }
	}

	for (Iterator iter = m2.keySet().iterator(); iter.hasNext();) {
	    k = iter.next();
	    if (!m1.containsKey(k)) {
		return false;
	    }
	}

	return true;
    }

    /**
     * Hash code mapkey.
     *
     * @param m
     *            the m
     * @return the int
     */
    @SuppressWarnings("rawtypes")
    private static int hashCodeMapkey(Map m) {
	int h = 0;
	Method k = null;
	for (Iterator iter = m.keySet().iterator(); iter.hasNext();) {
	    k = (Method) iter.next();
	    h += k.hashCode();
	}
	return h;
    }

    /**
     * (non-Javadoc).
     *
     * @return the int
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result;
	result += hashCodeMapkey(this.ooReadMethods);
	result += hashCodeMapkey(this.omReadMethods);
	result += hashCodeMapkey(this.mmReadMethods);
	result += hashCodeMapkey(this.lobReadMethods);
	return result;
    }
}
