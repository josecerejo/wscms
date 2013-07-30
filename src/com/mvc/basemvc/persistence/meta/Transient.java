/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description Transient的annotation
 * @ClassName Transient
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:11:12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
public @interface Transient {

}
