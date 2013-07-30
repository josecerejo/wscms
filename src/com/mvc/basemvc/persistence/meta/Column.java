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
 * @Description Column的annotation
 * @ClassName Column
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:25:57
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
public @interface Column {

    /**
     * Name.
     *
     * @return the string
     */
    String name() default "";

    /**
     * Alias.
     *
     * @return the string
     */
    String alias() default "";

    /**
     * Type.
     *
     * @return the string
     */
    String type() default "";

}
