/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.meta;

/**
 * @Description 数据库关联类型
 * @ClassName RelationType
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:14:00
 */
public class RelationType {

    /** The Constant none. */
    public static final int none = 0;

    /** The Constant oneToOne. */
    public static final int oneToOne = 1;

    /** The Constant oneToMany. */
    public static final int oneToMany = 2;

    /** The Constant manyToMany. */
    public static final int manyToMany = 3;

}
