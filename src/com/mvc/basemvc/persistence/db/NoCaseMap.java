/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;

import java.util.HashMap;

/**
 * @Description 不区分key大小写的Map
 * @ClassName NoCaseMap
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:41:31
 */
public class NoCaseMap<K, V> extends HashMap<K, V> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * (non-Javadoc).
     *
     * @param key
     *            the key
     * @return the v
     * @see java.util.HashMap#get(java.lang.Object)
     */
    @Override
    public V get(Object key) {
	// TODO Auto-generated method stub
	if (key instanceof String) {
	    V o = super.get(((String) key).toLowerCase());
	    if (o != null)
		return o;
	    o = super.get(((String) key).toUpperCase());
	    return o;
	}
	return super.get(key);
    }

}
