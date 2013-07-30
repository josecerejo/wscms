/**
 * @Description:
 * @cta-new.com.mvc.cms.utils
 * @FileName:RequestTypeUtils.java
 * @Created By:wjz_home@163.com
 * @Created:2013-9-7 下午06:09:10
 */
package com.mvc.basemvc.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @ClassName RequestTypeUtils
 * @author wjz_home@163.com
 * @Created 2013 2013-8-7 下午06:09:10
 */
public class RequestTypeUtils {
    public static final String JSON_REQUEST_PARAM = "_json";

    @SuppressWarnings("unchecked")
	public static boolean isJsonRequest(HttpServletRequest request) {
	Enumeration<String> enumeration = request.getParameterNames();
	while (enumeration.hasMoreElements()) {
	    String parameterName = enumeration.nextElement();
	    if (JSON_REQUEST_PARAM.equals(parameterName)) {
		return true;
	    }
	}
	return false;
    }

}
