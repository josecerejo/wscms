/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */

package com.mvc.basemvc.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;

import com.mvc.basemvc.cache.CacheEngineFactory;
import com.mvc.basemvc.util.SystemGlobals;

/**
 * @Description 初始化system参数和缓存
 * @ClassName ConfigInitializer
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午05:04:03
 */
public class ConfigInitializer implements ServletContextListener {

    /**
     * (non-Javadoc).
     *
     * @param event
     *            the event
     * @see javax.servlet.ServletContextListene
     *      r#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event) {
	ServletContext context = event.getServletContext();

	String configFile = context.getInitParameter("com.mvc.config.file");
	if (!StringUtils.isBlank(configFile)) {
	    configFile = configFile.trim();
	    SystemGlobals.loadConfig(configFile);
	}
	int useCache = SystemGlobals.getIntPreference("use_cache", 0);
	// 初始化默认缓存
	if (useCache != 0) {
	    CacheEngineFactory.getEngine();
	}
    }

    /**
     * (non-Javadoc).
     *
     * @param event
     *            the event
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event) {
	if (CacheEngineFactory.isEngineStarted()) {
	    CacheEngineFactory.getEngine().stop();
	}
    }

}
