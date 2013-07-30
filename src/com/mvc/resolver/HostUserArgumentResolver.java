/**
 * @Description:
 * @cta-new.com.mvc.cms.resolver
 * @FileName:HostUserArgumentResolver.java
 * @Created By:wjz_home@163.com
 * @Created:2013-9-7 下午01:26:45
 */
package com.mvc.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

import com.mvc.entity.CmsUser;

/**
 * @Description
 * @ClassName HostUserArgumentResolver
 * @author 
 * @Created 2013 2013-8-7 下午01:26:45
 */
public class HostUserArgumentResolver implements WebArgumentResolver {

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.web.bind.support.WebArgumentResolver#resolveArgument
     * (org.springframework.core.MethodParameter,
     * org.springframework.web.context.request.NativeWebRequest)
     */
    public Object resolveArgument(MethodParameter arg0, NativeWebRequest arg1)
	    throws Exception {
    	if (arg0.getParameterType().equals(CmsUser.class) && arg0.getParameterName().equals("user")) {
    		CmsUser member = (CmsUser) arg1.getAttribute("host",RequestAttributes.SCOPE_SESSION);
    		if (member != null) {
    			return member;
    		} else {
    			return UNRESOLVED;
    		}
    	}
    	return UNRESOLVED;
   }

}
