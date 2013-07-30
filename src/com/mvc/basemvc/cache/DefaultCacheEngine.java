/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @Description 默认缓存实现类
 * @ClassName DefaultCacheEngine
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午05:05:34
 */
public class DefaultCacheEngine implements CacheEngine {

    /** The cache. */
    @SuppressWarnings("rawtypes")
    private Map cache = new Hashtable();

    /**
     * (non-Javadoc).
     *
     * @param key
     *            the key
     * @param value
     *            the value
     * @see com.koolearn.util.cache.CacheEngine#add(java.io.Serializable,
     *      java.lang.Object)
     */
    @SuppressWarnings({ "unchecked" })
    public void add(Serializable key, Object value) {
	this.cache.put(key, value);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mvc.basemvc.cache.CacheEngine#add(java.io.Serializable,
     * java.lang.Object, int)
     */
    @Override
    public void add(Serializable key, Object value, int expire) {
	add(key, value);

    }

    /**
     * (non-Javadoc).
     *
     * @param fqn
     *            the fqn
     * @param key
     *            the key
     * @param value
     *            the value
     * @see com.koolearn.util.cache.CacheEngine#add(java.lang.String,
     *      java.io.Serializable, java.lang.Object)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void add(String fqn, Serializable key, Object value) {
	Map m = (Map) this.cache.get(fqn);
	if (m == null) {
	    m = new HashMap();
	}

	m.put(key, value);
	this.cache.put(fqn, m);
    }

    /**
     * (non-Javadoc).
     *
     * @param fqn
     *            the fqn
     * @param key
     *            the key
     * @return the object
     * @see com.koolearn.util.cache.CacheEngine#get(java.lang.String,
     *      java.io.Serializable)
     */
    @SuppressWarnings("rawtypes")
    public Object get(String fqn, Serializable key) {
	Map m = (Map) this.cache.get(fqn);
	if (m == null) {
	    return null;
	}

	return m.get(key);
    }

    /**
     * (non-Javadoc).
     *
     * @param fqn
     *            the fqn
     * @return the object
     * @see com.koolearn.util.cache.CacheEngine#get(java.lang.String)
     */
    public Object get(String fqn) {
	return this.cache.get(fqn);
    }

    /**
     * (non-Javadoc).
     *
     * @param fqn
     *            the fqn
     * @return the values
     * @see com.koolearn.util.cache.CacheEngine#getValues(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public Collection getValues(String fqn) {
	Map m = (Map) this.cache.get(fqn);
	if (m == null) {
	    return new ArrayList();
	}

	return m.values();
    }

    /**
     * (non-Javadoc).
     *
     * @see com.koolearn.util.cache.CacheEngine#init()
     */
    @SuppressWarnings("rawtypes")
    public void init() {
	this.cache = new HashMap();
    }

    /**
     * (non-Javadoc).
     *
     * @see com.koolearn.util.cache.CacheEngine#stop()
     */
    public void stop() {
    }

    /**
     * (non-Javadoc).
     *
     * @param fqn
     *            the fqn
     * @param key
     *            the key
     * @see com.koolearn.util.cache.CacheEngine#remove(java.lang.String,
     *      java.io.Serializable)
     */
    @SuppressWarnings("rawtypes")
    public void remove(String fqn, Serializable key) {
	Map m = (Map) this.cache.get(fqn);
	if (m != null) {
	    m.remove(key);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param fqn
     *            the fqn
     * @see com.koolearn.util.cache.CacheEngine#remove(java.lang.String)
     */
    public void remove(String fqn) {
	this.cache.remove(fqn);
    }

    /**
     * Gets the map.
     *
     * @param fqn
     *            the fqn
     * @return the map
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getMap(String fqn) {
	Map m = (Map) this.cache.get(fqn);
	return Collections.unmodifiableMap(m);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mvc.basemvc.cache.CacheEngine#getClientName()
     */
    @Override
    public String getClientName() {
	// TODO Auto-generated method stub
	return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mvc.basemvc.cache.CacheEngine#setClientName(java.lang.String)
     */
    @Override
    public void setClientName(String clientName) {
	// TODO Auto-generated method stub

    }
}
