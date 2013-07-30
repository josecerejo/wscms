/**
 * @Description:
 * @cta-new.com.mvc.cms.interceptors
 * @FileName:AuthorizeInterceptor.java
 * @Created By:wjz_home@163.com
 * @Created:2013-9-7 下午01:53:52
 */
package com.mvc.interceptor;

import java.io.Writer;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mvc.entity.CmsUser;
import com.mvc.basemvc.annotation.WithoutAuthorize;
import com.mvc.basemvc.util.RequestTypeUtils;

/**
 * @Description
 * @ClassName AuthorizeInterceptor
 * @author
 * @Created 2013-7-4 下午01:53:52
 */
public class AuthorizeInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
	    HttpServletResponse response, Object handler) throws Exception {
	if (handler.getClass().isAnnotationPresent(WithoutAuthorize.class)) {
	    return true;
	}

	CmsUser host = (CmsUser) request.getSession().getAttribute("host");
	if (host != null) {
	    request.setAttribute("host", host);
	    return true;
	}
	if (RequestTypeUtils.isJsonRequest(request)) {
	    Writer writer = null;
	    try {
		writer = response.getWriter();
		Map<String, String> map = new HashMap<String, String>();
		map.put("error_msg", "请登录");
		JSONObject jsonpObject = JSONObject.fromObject(map);
		writer.write(jsonpObject.toString());
	    } catch (Exception e) {

	    } finally {
		if (writer != null) {
		    writer.close();
		}
	    }
	} else {
	    String nextUrl = request.getRequestURL().toString();
	    String queryString = request.getQueryString();
	    if (StringUtils.isNotBlank(queryString)) {
		nextUrl = request.getRequestURL() + "?" + queryString;
	    }

	    response.sendRedirect(request.getContextPath() + "/admin?nexturl="
		    + URLEncoder.encode(nextUrl, "utf-8"));
	}
	return false;
    }

}
