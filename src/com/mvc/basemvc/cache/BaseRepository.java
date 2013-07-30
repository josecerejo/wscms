/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mvc.basemvc.util.SystemGlobals;


/**
 * @Description 应用层缓存框架抽象类，应用层需要操作缓存功能的类需要继承此类
 * @ClassName BaseRepository
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午05:07:47
 */
public abstract class BaseRepository {

	/** The cache engine. */
	protected CacheEngine cacheEngine = CacheEngineFactory.getEngine();


	/**
     * Gets the cached key list.
     *
     * @return the cached key list
     */
	@SuppressWarnings("unchecked")
	protected List<Serializable> getCachedKeyList() {
		return (List<Serializable>) cacheEngine.get( getCacheFullName(), "keys" );
	}


	/**
     * Sets the cached key list.
     *
     * @param keys
     *            the new cached key list
     */
	private void setCachedKeyList( List<Serializable> keys ) {
		cacheEngine.add( getCacheFullName(), "keys", keys );
	}


	/**
     * Gets the cache value.
     *
     * @param key
     *            the key
     * @return the cache value
     */
	protected Object getCacheValue( Serializable key ) {
		return cacheEngine.get( getCacheFullName(), key );
	}


	/**
     * Sets the cache value.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
	protected void setCacheValue( Serializable key, Object value ) {
		//cacheEngine.remove( getCacheName(), key );
		cacheEngine.add(getCacheFullName(), key, value);
		if(isKeyList()) {
			List<Serializable> keyList = getCachedKeyList();
			if ( keyList==null ) {
				keyList = new ArrayList<Serializable>();
				keyList.add( key );
				setCachedKeyList( keyList );
			}
			else {
				if ( !keyList.contains(key) ) {
					keyList.add( key );
					setCachedKeyList( keyList );
				}
			}
		}
	}


	/**
     * Gets the cache value map.
     *
     * @return the cache value map
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Map getCacheValueMap() {
		List<Serializable> keys = getCachedKeyList();
		Map valueMap = new LinkedHashMap();
		if ( keys==null || keys.isEmpty() ) return valueMap;

		Serializable key = null;
		for ( int i=0; i<keys.size(); i++ ) {
			key = keys.get(i);
			valueMap.put( key, getCacheValue(key) );
		}

		return valueMap;
	}


	/**
     * Removes the cached value.
     *
     * @param key
     *            the key
     */
	protected void removeCachedValue( Serializable key ) {
		cacheEngine.remove( getCacheFullName(), key );
		List<Serializable> keyList = getCachedKeyList();
		if ( keyList!=null && keyList.contains(key)) {
			keyList.remove(key);
			setCachedKeyList( keyList );
		}
	}


	/**
     * Clear.
     */
	public synchronized void clear() {
		if ( !isKeyList() ) return;
		List<Serializable> keyList = getCachedKeyList();
		if ( keyList == null || keyList.isEmpty() ) return;

		String cacheName = getCacheFullName();
		for ( Serializable key : keyList ) {
			cacheEngine.remove( cacheName, key );
		}

		cacheEngine.remove( cacheName, "keys" );
	}


	/**
     * Contains.
     *
     * @param key
     *            the key
     * @return true, if successful
     */
	protected boolean contains( Serializable key ) {
		Object o = getCacheValue(key);
		return (o!=null);
	}


	/**
     * Gets the cache full name.
     *
     * @return the cache full name
     */
	public String getCacheFullName() {
		String prefix = SystemGlobals.getPreference("cache.prefixName");
		if ( prefix != null ) prefix = prefix.trim();
		else prefix = "";
		if ( prefix.length() > 0 ) {
			return prefix + "_" + getCacheName();
		}
		else {
			return getCacheName();
		}
	}


	/**
     * Gets the cache name.
     *
     * @return the cache name
     */
	public abstract String getCacheName();


	/**
     * Checks if is key list.
     *
     * @return true, if is key list
     */
	public boolean isKeyList() {
		return false;
	}



}
