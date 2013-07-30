/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.cache;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.mvc.basemvc.util.SystemGlobals;
import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

/**
 * @Description memcache缓存实现类
 * @ClassName MemCacheEngine
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午05:04:34
 */
public class MemCacheEngine implements CacheEngine {

    /** The SEPERATOR. */
    private static String SEPERATOR = "__";

    /**
     * (non-Javadoc).
     *
     * @see com.koolearn.util.cache.CacheEngine#init()
     */
    public void init() {
	String s = SystemGlobals.getPreference("cache.mem.servers");
	String[] a = s.split(",");

	String[] serverList = new String[a.length];
	Integer[] weights = new Integer[a.length];
	for (int i = 0; i < a.length; i++) {
	    a[i] = a[i].trim();
	    serverList[i] = a[i];
	    weights[i] = new Integer(a[i].substring(a[i].lastIndexOf(":") + 1));
	}

	SockIOPool pool = SockIOPool.getInstance();
	pool.setServers(serverList);
	pool.setWeights(weights);

	s = SystemGlobals.getPreference("cache.mem.initConn");
	if (!StringUtils.isBlank(s))
	    pool.setInitConn(Integer.parseInt(s));

	s = SystemGlobals.getPreference("cache.mem.maxConn");
	if (!StringUtils.isBlank(s))
	    pool.setMaxConn(Integer.parseInt(s));

	s = SystemGlobals.getPreference("cache.mem.maxIdle");
	if (!StringUtils.isBlank(s))
	    pool.setMaxIdle(Long.parseLong(s));

	s = SystemGlobals.getPreference("cache.mem.maintSleep");
	if (!StringUtils.isBlank(s))
	    pool.setMaintSleep(Long.parseLong(s));

	s = SystemGlobals.getPreference("cache.mem.socketTO");
	if (!StringUtils.isBlank(s))
	    pool.setSocketTO(Integer.parseInt(s));

	s = SystemGlobals.getPreference("cache.mem.socketConnectTO");
	if (!StringUtils.isBlank(s))
	    pool.setSocketConnectTO(Integer.parseInt(s));

	s = SystemGlobals.getPreference("cache.mem.nagle");
	if (!StringUtils.isBlank(s))
	    pool.setNagle(Boolean.parseBoolean(s));
	pool.setHashingAlg(SockIOPool.NEW_COMPAT_HASH);
	pool.initialize();
    }

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
    public void add(Serializable key, Object value) {
	if (value == null)
	    return;

	MemCachedClient client = new MemCachedClient();

	long expireTime = getExpireTime(null);
	if (expireTime <= 0) {
	    client.set(key.toString(), value);
	} else {
	    client.set(key.toString(), value, new Date(expireTime));
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mvc.basemvc.cache.CacheEngine#add(java.io.Serializable,
     * java.lang.Object, int)
     */
    @Override
    public void add(Serializable key, Object value, int expire) {
	MemCachedClient client = new MemCachedClient();

	long expireTime = expire * 1000;
	if (expireTime <= 0) {
	    client.set(key.toString(), value);
	} else {
	    client.set(key.toString(), value, new Date(expireTime));
	}

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
    public void add(String fqn, Serializable key, Object value) {
	if (value == null)
	    return;

	MemCachedClient client = new MemCachedClient();

	String cacheKey = key.toString();
	if (!StringUtils.isBlank(fqn)) {
	    cacheKey = fqn + SEPERATOR + cacheKey;
	}

	long expireTime = getExpireTime(null);
	if (expireTime <= 0) {
	    client.set(cacheKey, value);
	} else {
	    client.set(cacheKey, value, new Date(expireTime));
	}
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
    public Object get(String fqn, Serializable key) {
	String cacheKey = key.toString();
	if (!StringUtils.isBlank(fqn)) {
	    cacheKey = fqn + SEPERATOR + cacheKey;
	}
	MemCachedClient client = new MemCachedClient();
	return client.get(cacheKey);
    }

    /**
     * Gets the.
     *
     * @param key
     *            the key
     * @return the object
     */
    public Object get(Serializable key) {
	MemCachedClient client = new MemCachedClient();
	return client.get(key.toString());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mvc.basemvc.cache.CacheEngine#get(java.lang.String)
     */
    public Object get(String fqn) {
	MemCachedClient client = new MemCachedClient();
	return client.get(fqn);
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
	return null;
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
    public void remove(String fqn, Serializable key) {
	MemCachedClient client = new MemCachedClient();
	String cacheKey = key.toString();
	if (!StringUtils.isBlank(fqn))
	    cacheKey = fqn + SEPERATOR + cacheKey;
	client.delete(cacheKey);
    }

    /**
     * (non-Javadoc).
     *
     * @param fqn
     *            the fqn
     * @see com.koolearn.util.cache.CacheEngine#remove(java.lang.String)
     */
    public void remove(String fqn) {
	MemCachedClient client = new MemCachedClient();

	if (!StringUtils.isBlank(fqn)) {
	    String cacheKey = fqn.trim();
	    client.delete(cacheKey);
	}
    }

    /**
     * (non-Javadoc).
     *
     * @see com.koolearn.util.cache.CacheEngine#stop()
     */
    public void stop() {
    }

    /**
     * Gets the expire time.
     *
     * @param fqn
     *            the fqn
     * @return the expire time
     */
    private long getExpireTime(String fqn) {
	MemCachedConfig config = MemCachedConfig.getRepos(fqn);
	if (config != null) {
	    long time = config.getExpireTime();
	    return time;
	}

	return 2 * 3600 * 1000;
    }

    /**
     * Test cache.
     */
    public void testCache() {
	CacheEngine engine = CacheEngineFactory.getEngine();

	for (int i = 0; i < 10; i++) {
	    engine.add("testCache", String.valueOf(i), "value-" + i);
	}

	for (int i = 0; i < 20; i++) {
	    Object v = engine.get("testCache", String.valueOf(i));
	    System.out.println(v);
	}
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
