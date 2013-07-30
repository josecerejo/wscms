/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.mvc.basemvc.util.SystemGlobals;

/**
 * @Description 缓存引擎工厂类
 * @ClassName CacheEngineFactory
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午05:06:38
 */
public class CacheEngineFactory {

    /** The cache engine map. */
    private static Map<String, CacheEngine> cacheEngineMap = new ConcurrentHashMap<String, CacheEngine>();

    /**
     * Gets the engine.
     *
     * @return the engine
     */
    public static CacheEngine getEngine() {
	CacheEngine cacheEngine = cacheEngineMap.get("");
	if (cacheEngine != null)
	    return cacheEngine;

	String cacheEngineClass = SystemGlobals
		.getPreference("cache.implement");
	if (StringUtils.isBlank(cacheEngineClass))
	    cacheEngineClass = "com.mvc.basemvc.cache.DefaultCacheEngine";
	return getEngine("", cacheEngineClass);
    }

    /**
     * Gets the engine.
     *
     * @param name
     *            the name
     * @param cacheEngineClass
     *            the cache engine class
     * @return the engine
     */
    public static CacheEngine getEngine(String name, String cacheEngineClass) {
	CacheEngine cacheEngine = cacheEngineMap.get(name);
	if (cacheEngine != null)
	    return cacheEngine;

	try {
	    cacheEngine = (CacheEngine) Class.forName(cacheEngineClass)
		    .newInstance();
	    cacheEngine.init();
	    cacheEngineMap.put(name, cacheEngine);
	    return cacheEngine;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    /**
     * Gets the engine.
     *
     * @param name
     *            the name
     * @return the engine
     */
    public static CacheEngine getEngine(String name) {
	return cacheEngineMap.get(name);
    }

    /**
     * Checks if is engine started.
     *
     * @return true, if is engine started
     */
    public static boolean isEngineStarted() {
	CacheEngine cacheEngine = getEngine();
	return (cacheEngine != null);
    }

}
