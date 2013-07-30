/**
 * @Description:
 * @cta-new.com.mvc.cms.annotation
 * @FileName:WithoutAuthorize.java
 * @Created By:wjz_home@163.com
 * @Created:2013-9-7 下午01:51:55
 */
package com.mvc.basemvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Description 不需要验证用户信息
 * @ClassName WithoutAuthorize
 * @author wjz_home@163.com
 * @Created 2013 2013-8-7 下午01:51:55
 */
@Target(value={ElementType.TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
@Documented
public  @interface WithoutAuthorize{

}
