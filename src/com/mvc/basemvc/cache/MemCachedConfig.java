/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */

package com.mvc.basemvc.cache;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @Description memcache缓存配置类
 * @ClassName MemCachedConfig
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午05:04:57
 */
public class MemCachedConfig {

    /** The name. */
    private String name;

    /** The expire time. */
    private long expireTime = 0;

    /** The local expire time. */
    private long localExpireTime = 0;

    /** The length. */
    private int length = 10;

    /**
     * Gets the expire time.
     * 
     * @return the expire time
     */
    public long getExpireTime() {
	return expireTime;
    }

    /**
     * Sets the expire time.
     * 
     * @param expireTime
     *            the new expire time
     */
    public void setExpireTime(long expireTime) {
	this.expireTime = expireTime;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            the new name
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Gets the length.
     * 
     * @return the length
     */
    public int getLength() {
	return length;
    }

    /**
     * Sets the length.
     * 
     * @param length
     *            the new length
     */
    public void setLength(int length) {
	this.length = length;
    }

    /**
     * Gets the local expire time.
     * 
     * @return the local expire time
     */
    public long getLocalExpireTime() {
	return localExpireTime;
    }

    /**
     * Sets the local expire time.
     * 
     * @param localExpireTime
     *            the new local expire time
     */
    public void setLocalExpireTime(long localExpireTime) {
	this.localExpireTime = localExpireTime;
    }

    /** The repos map. */
    private static Map<String, MemCachedConfig> reposMap = null;

    /**
     * Inits the.
     */
    private static void init() {
	reposMap = new HashMap<String, MemCachedConfig>();
	String configInfo = com.mvc.basemvc.util.SystemGlobals
		.getPreference("cache.repos");
	if (StringUtils.isBlank(configInfo))
	    return;

	String[] reposArray = configInfo.split("\\|");

	MemCachedConfig repos = null;
	for (int i = 0; i < reposArray.length; i++) {
	    repos = parseRepos(reposArray[i]);
	    if (repos != null) {
		reposMap.put(repos.getName(), repos);
	    }
	}
    }

    /**
     * Parses the repos.
     * 
     * @param info
     *            the info
     * @return the mem cached config
     */
    private static MemCachedConfig parseRepos(String info) {
	if (StringUtils.isBlank(info))
	    return null;
	info = info.trim();
	String[] config = info.split(",");
	if (config.length < 2)
	    return null;
	MemCachedConfig repos = new MemCachedConfig();
	repos.setName(config[0].trim());
	repos.setExpireTime(Long.parseLong(config[1].trim()));

	if (config.length >= 3) {
	    repos.setLocalExpireTime(Long.parseLong(config[2].trim()));
	}

	if (config.length >= 4) {
	    repos.setLength(Integer.parseInt(config[3].trim()));
	}

	return repos;
    }

    /**
     * Gets the repos.
     * 
     * @param name
     *            the name
     * @return the repos
     */
    public static MemCachedConfig getRepos(String name) {
	if (reposMap == null) {
	    init();
	}

	return reposMap.get(name);
    }

}
