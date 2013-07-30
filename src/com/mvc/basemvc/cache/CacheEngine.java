/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.cache;

import java.io.Serializable;
import java.util.Collection;

/**
 * @Description 缓存框架接口类
 * @ClassName CacheEngine
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午05:07:05
 */
public interface CacheEngine {

    /** The Constant DUMMY_FQN. */
    public static final String DUMMY_FQN = "";

    /** The Constant NOTIFICATION. */
    public static final String NOTIFICATION = "notification";

    /** The Constant EXPIRE_SECOND. */
    public static final int EXPIRE_SECOND = 1;

    /** The Constant EXPIRE_MINUTE. */
    public static final int EXPIRE_MINUTE = 60;

    /** The Constant EXPIRE_HOUR. */
    public static final int EXPIRE_HOUR = 60 * EXPIRE_MINUTE;

    /** The Constant EXPIRE_DAY. */
    public static final int EXPIRE_DAY = 24 * EXPIRE_HOUR;

    /** The Constant EXPIRE_WEEK. */
    public static final int EXPIRE_WEEK = 7 * EXPIRE_DAY;

    /**
     * Inits the.
     */
    public void init();

    /**
     * Stop.
     */
    public void stop();

    /**
     * Adds the.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    public void add(Serializable key, Object value);

    /**
     * Adds the.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     * @param expire
     *            the expire
     */
    public void add(Serializable key, Object value, int expire);

    /**
     * Adds the.
     *
     * @param fqn
     *            the fqn
     * @param key
     *            the key
     * @param value
     *            the value
     */
    public void add(String fqn, Serializable key, Object value);

    /**
     * Gets the.
     *
     * @param fqn
     *            the fqn
     * @param key
     *            the key
     * @return the object
     */
    public Object get(String fqn, Serializable key);

    /**
     * Gets the.
     *
     * @param fqn
     *            the fqn
     * @return the object
     */
    public Object get(String fqn);

    /**
     * Gets the values.
     *
     * @param fqn
     *            the fqn
     * @return the values
     */
    @SuppressWarnings("rawtypes")
    public Collection getValues(String fqn);

    /**
     * Removes the.
     *
     * @param fqn
     *            the fqn
     * @param key
     *            the key
     */
    public void remove(String fqn, Serializable key);

    /**
     * Removes the.
     *
     * @param fqn
     *            the fqn
     */
    public void remove(String fqn);

    /**
     * Gets the client name.
     *
     * @return the client name
     */
    public String getClientName();

    /**
     * Sets the client name.
     *
     * @param clientName
     *            the new client name
     */
    public void setClientName(String clientName);
}
