/**
 * @Description:
 * @cta-new.com.mvc.cms.interceptors
 * @FileName:LogInterceptor.java
 * @Created By:wjz_home@163.com
 * @Created:2013-9-6 下午01:44:17
 */
package com.mvc.basemvc.interceptors;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @Description
 * @ClassName LogInterceptor
 * @author wjz_home@163.com
 * @Created 2013 2013-8-6 下午01:44:17
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

    // private static final Log logger =
    // LogFactory.getLog(LogInterceptor.class);

    private static final String ACCESS_LOG = "%s|%s|%s|%s|%s|%s|%s|";

    private static final Log accessLog = LogFactory.getLog("accessLogger");

    private Map<Object, Set<String>> handlerPatternMap = new HashMap<Object, Set<String>>();

    private PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request,
	    HttpServletResponse response, Object handler) throws Exception {
	String requestURI = request.getRequestURI();
	String method = request.getMethod();
	if ("/admin/".equals(requestURI)) {
	    return true;
	}
	String pattern = findPattern(handler, requestURI);
	if (pattern == null) {
	    pattern = requestURI;
	}
	String ip = request.getRemoteAddr();
	String currentTime = DateFormatUtils.format(new Date(),
		"yyyy-MM-dd HH:mm:ss");
	if (request.getQueryString() != null) {
	    pattern += "?" + request.getQueryString();
	}
	String logString = String.format(ACCESS_LOG, "/" + method + pattern,
		"111", currentTime, method, "", ip, requestURI);
	if (accessLog.isInfoEnabled()) {
	    accessLog.info(logString);
	}

	return true;
    }

    private String findPattern(Object handler, String path) {
	Set<String> patterns = getPatternSet(handler);
	if (patterns != null) {
	    for (String pattern : patterns) {
		if (pathMatcher.match(pattern, path)) {
		    return pattern;
		}
	    }
	}
	return null;
    }

    private Set<String> getPatternSet(Object handler) {
	Set<String> patternSet = handlerPatternMap.get(handler);
	if (patternSet == null) {
	    synchronized (handlerPatternMap) {
		patternSet = handlerPatternMap.get(handler);
		if (patternSet == null) {
		    patternSet = getPatterSetUseHandler(handler);
		    handlerPatternMap.put(handler, patternSet);
		}
	    }
	}
	return patternSet;
    }

    private Set<String> getPatterSetUseHandler(Object handler) {
	Set<String> patternSet = new HashSet<String>();
	RequestMapping requestMapping = AnnotationUtils.findAnnotation(
		handler.getClass(), RequestMapping.class);
	String[] classMappedPaths = null;
	if (requestMapping != null) {
	    classMappedPaths = requestMapping.value();
	}

	for (Method method : handler.getClass().getDeclaredMethods()) {
	    RequestMapping methodMapping = AnnotationUtils.getAnnotation(
		    method, RequestMapping.class);
	    if (methodMapping != null) {
		String[] methodMappedPaths = methodMapping.value();
		if (methodMappedPaths != null && methodMappedPaths.length > 0) {
		    for (String path : methodMappedPaths) {
			if (!path.startsWith("/")) {
			    path += "/";
			}
			if (classMappedPaths != null) {
			    for (String classMappedPath : methodMappedPaths) {
				String pattern = classMappedPath + path;
				if (!patternSet.contains(pattern)) {
				    patternSet.add(pattern);
				}
			    }
			} else {
			    String pattern = path;
			    if (!patternSet.contains(pattern)) {
				patternSet.add(pattern);
			    }
			}
		    }
		}
	    } else {
		if (classMappedPaths != null) {
		    for (String classMappedPath : classMappedPaths) {
			String pattern = classMappedPath;
			if (!patternSet.contains(pattern)) {
			    patternSet.add(pattern);
			}
		    }
		}
	    }
	}
	return patternSet;
    }
}
