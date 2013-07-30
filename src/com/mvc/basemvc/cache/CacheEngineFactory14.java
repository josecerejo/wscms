/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.cache;

import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mvc.basemvc.util.SystemGlobals;


/**
 * @Description 缓存引擎工厂类
 * @ClassName CacheEngineFactory14
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午05:06:22
 */
public class CacheEngineFactory14 {

    /** The cache engine map. */
    @SuppressWarnings("rawtypes")
    private static Map cacheEngineMap = new Hashtable();

    /**
     * Gets the engine.
     *
     * @return the engine
     */
    public static CacheEngine getEngine() {
	CacheEngine cacheEngine = (CacheEngine) cacheEngineMap.get("");
	if (cacheEngine != null)
	    return cacheEngine;

	String cacheEngineClass = SystemGlobals
		.getPreference("cache.implement");
	if (StringUtils.isBlank(cacheEngineClass))
	    cacheEngineClass = "com.koolearn.util.cache.DefaultCacheEngine";
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
    @SuppressWarnings("unchecked")
    public static CacheEngine getEngine(String name, String cacheEngineClass) {
	CacheEngine cacheEngine = (CacheEngine) cacheEngineMap.get(name);
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
	return (CacheEngine) cacheEngineMap.get(name);
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
