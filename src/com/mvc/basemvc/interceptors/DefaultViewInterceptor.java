/**
 * @Description:
 * @cta-new.com.mvc.cms.interceptors
 * @FileName:DefaultViewInterceptor.java
 * @Created By:wjz_home@163.com
 * @Created:2013-9-6 下午05:32:16
 */
package com.mvc.basemvc.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @Description
 * @ClassName DefaultViewInterceptor
 * @author wjz_home@163.com
 * @Created 2013 2013-8-6 下午05:32:16
 */
public class DefaultViewInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request,
	    HttpServletResponse response, Object handler,
	    ModelAndView modelAndView) throws Exception {
	if (modelAndView == null) {
	    modelAndView = new ModelAndView("jsonView");
	}
	if (!modelAndView.hasView()) {
	    modelAndView.setViewName("jsonView");
	}
    }

}
