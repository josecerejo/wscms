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
 * @Description ManyToMany的annotation
 * @ClassName ManyToMany
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:18:44
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
public @interface ManyToMany {

    /**
     * Reference column.
     *
     * @return the string
     */
    String referenceColumn() default "";

    /**
     * Order by.
     *
     * @return the string
     */
    String orderBy() default "";

    /**
     * Relation table.
     *
     * @return the string
     */
    String relationTable();

    /**
     * Relation ref column1.
     *
     * @return the string
     */
    String relationRefColumn1();

    /**
     * Relation ref column2.
     *
     * @return the string
     */
    String relationRefColumn2();

    /**
     * Lazy.
     *
     * @return true, if successful
     */
    boolean lazy() default true;
}
