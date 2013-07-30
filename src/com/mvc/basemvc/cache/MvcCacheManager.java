/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.cache;

import org.apache.log4j.Logger;

import com.mvc.basemvc.util.SystemGlobals;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description mvc缓存管理类，使用memcached
 * @ClassName MvcCacheManager
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午05:05:54
 */
public class MvcCacheManager {

    /** The Constant logger. */
    private static final Logger logger = Logger
	    .getLogger(MvcCacheManager.class);

    /** The Constant factory. */
    private static final Map<String, MvcCacheManager> factory = new ConcurrentHashMap<String, MvcCacheManager>();

    /** The namespace. */
    private String namespace = "mvc";

    /**
     * Instantiates a new mvc cache manager.
     *
     * @param namespace
     *            the namespace
     */
    private MvcCacheManager(String namespace) {
	this.namespace = namespace;
    }

    /**
     * Gets the cache manager.
     *
     * @param namespace
     *            the namespace
     * @return the cache manager
     */
    public static MvcCacheManager getCacheManager(String namespace) {
	MvcCacheManager cacheManager = factory.get("namespace");
	if (cacheManager == null) {
	    cacheManager = new MvcCacheManager(namespace);
	    factory.put(namespace, cacheManager);
	}
	return cacheManager;
    }

    /**
     * Gets the.
     *
     * @param <T>
     *            the generic type
     * @param key
     *            the key
     * @param clazz
     *            the clazz
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public <T extends Object> T get(String key, Class<T> clazz) {
	if (SystemGlobals.getIntPreference("use_cache", 0) == 0) {
	    return null;
	}
	String localKey = buildKey(key, clazz);
	CacheEngine cacheEngine = CacheEngineFactory.getEngine();
	T obj = null;
	if (cacheEngine == null) {
	    return obj;
	}

	try {
	    obj = (T) cacheEngine.get(localKey);
	} catch (RuntimeException e) {
	    logger.error("get key:" + key + " for[" + clazz.getName()
		    + "] error" + e.getMessage());
	}
	if (logger.isDebugEnabled()) {
	    logger.debug("get key:" + key + " obj:" + obj);
	}
	return obj;
    }

    /**
     * Save.
     *
     * @param <T>
     *            the generic type
     * @param key
     *            the key
     * @param obj
     *            the obj
     * @param clazz
     *            the clazz
     */
    public <T extends Object> void save(String key, T obj, Class<T> clazz) {
	String localKey = buildKey(key, clazz);
	if (SystemGlobals.getIntPreference("use_cache", 0) == 0) {
	    return;
	}
	CacheEngine cacheEngine = CacheEngineFactory.getEngine();
	try {
	    cacheEngine.add(localKey, obj);
	    if (logger.isDebugEnabled()) {
		logger.debug("save key:" + key + " obj:" + obj);
	    }
	} catch (RuntimeException e) {
	    logger.error("save key:" + key + " for[" + clazz.getName()
		    + "] error" + e.getMessage());
	}

    }

    /**
     * Delete.
     *
     * @param <T>
     *            the generic type
     * @param key
     *            the key
     * @param clazz
     *            the clazz
     */
    public <T extends Object> void delete(String key, Class<T> clazz) {
	if (SystemGlobals.getIntPreference("use_cache", 0) == 0) {
	    return;
	}
	if (key == null) {
	    return;
	}
	String localKey = buildKey(key, clazz);
	CacheEngine cacheEngine = CacheEngineFactory.getEngine();
	if (cacheEngine == null) {
	    return;
	}
	cacheEngine.remove(localKey);
    }

    /**
     * Save.
     *
     * @param <T>
     *            the generic type
     * @param key
     *            the key
     * @param obj
     *            the obj
     * @param clazz
     *            the clazz
     * @param expire
     *            the expire
     */
    public <T extends Object> void save(String key, T obj, Class<T> clazz,
	    int expire) {
	if (SystemGlobals.getIntPreference("use_cache", 0) == 0) {
	    return;
	}
	String localKey = buildKey(key, clazz);
	CacheEngine cacheEngine = CacheEngineFactory.getEngine();
	cacheEngine.add(localKey, obj, expire);
    }

    /**
     * Builds the key.
     *
     * @param <T>
     *            the generic type
     * @param key
     *            the key
     * @param clazz
     *            the clazz
     * @return the string
     */
    private <T extends Object> String buildKey(String key, Class<T> clazz) {
	return getNamespace() + ":" + clazz.getName() + ":" + key;
    }

    /**
     * Gets the namespace.
     *
     * @return the namespace
     */
    public String getNamespace() {
	return namespace;
    }

    /**
     * Sets the namespace.
     *
     * @param namespace
     *            the new namespace
     */
    public void setNamespace(String namespace) {
	this.namespace = namespace;
    }
}
