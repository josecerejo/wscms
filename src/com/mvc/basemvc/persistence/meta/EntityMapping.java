/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.meta;

/**
 * @Description 实体类的Mapping
 * @ClassName EntityMapping
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:22:05
 */
public class EntityMapping extends ObjectMapping {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8085891783581226684L;

    /** The mapping super class. */
    private Class<?> mappingSuperClass;

    /** The table name. */
    private String tableName;

    /** The id mapping. */
    private PropertyMapping idMapping = null;

    /**
     * Gets the mapping super class.
     *
     * @return the mapping super class
     */
    public Class<?> getMappingSuperClass() {
	return mappingSuperClass;
    }

    /**
     * Sets the mapping super class.
     *
     * @param mappingSuperClass
     *            the new mapping super class
     */
    public void setMappingSuperClass(Class<?> mappingSuperClass) {
	this.mappingSuperClass = mappingSuperClass;
    }

    /**
     * Gets the id mapping.
     *
     * @return the id mapping
     */
    public PropertyMapping getIdMapping() {
	return idMapping;
    }

    /**
     * Sets the id mapping.
     *
     * @param idMapping
     *            the new id mapping
     */
    public void setIdMapping(PropertyMapping idMapping) {
	this.idMapping = idMapping;
    }

    /**
     * Gets the table name.
     *
     * @return the table name
     */
    public String getTableName() {
	return tableName;
    }

    /**
     * Sets the table name.
     *
     * @param tableName
     *            the new table name
     */
    public void setTableName(String tableName) {
	this.tableName = tableName;
    }

    /**
     * Clone.
     *
     * @return the object
     * @throws CloneNotSupportedException
     *             the clone not supported exception
     */
    public Object Clone() throws CloneNotSupportedException {
	return super.clone();
    }
}
