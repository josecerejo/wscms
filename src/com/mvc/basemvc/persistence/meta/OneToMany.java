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
 * @Description
 * @ClassName OneToMany的annotation
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:16:28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
public @interface OneToMany {

    /**
     * Reference column.
     *
     * @return the string
     */
    String referenceColumn();

    /**
     * Order by.
     *
     * @return the string
     */
    String orderBy() default "";

    /**
     * Lazy.
     *
     * @return true, if successful
     */
    boolean lazy() default true;
}
