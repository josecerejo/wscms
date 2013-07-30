/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.meta;

import java.io.Serializable;

import com.mvc.basemvc.persistence.util.BaseUtils;

/**
 * @Description 属性Mapping类
 * @ClassName PropertyMapping
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:14:47
 */
public class PropertyMapping implements Serializable {

    /**
     * @Description
     * @field long serialVersionUID
     * @Created 2013 2013-8-5 下午04:10:12
     */
    private static final long serialVersionUID = 3662388353637817720L;

    /** The property class. */
    private Class<?> propertyClass;

    /** The property name. */
    private String propertyName;

    /** The primary key. */
    private boolean primaryKey;

    /** The generate strategy. */
    private int generateStrategy;

    /** The generator. */
    private String generator;

    /** The sequence name. */
    private String sequenceName;

    /** The reloation type. */
    private int reloationType;

    /** The foreign column. */
    private String foreignColumn;

    /** The reference class. */
    private Class<?> referenceClass;

    /** The reference column. */
    private String referenceColumn;

    /** The reference order. */
    private String referenceOrder;

    /** The relation table. */
    private String relationTable;

    /** The relation ref col1. */
    private String relationRefCol1;

    /** The relation ref col2. */
    private String relationRefCol2;

    /** The load lazy. */
    private boolean loadLazy;

    /** The transient field. */
    private boolean transientField = false;

    /** The complex type. */
    private boolean complexType = false;

    /** The column mapping. */
    private ColumnMapping columnMapping = null;

    /** The object mapping. */
    private ObjectMapping objectMapping = null;

    /**
     * Checks if is relation property.
     *
     * @return true, if is relation property
     */
    public boolean isRelationProperty() {
	return !(this.reloationType == RelationType.none);
    }

    /**
     * Checks if is base type property.
     *
     * @return true, if is base type property
     */
    public boolean isBaseTypeProperty() {
	return BaseUtils.isBaseType(propertyClass);
    }

    /**
     * Gets the column name.
     *
     * @return the column name
     */
    public String getColumnName() {
	if (complexType)
	    return objectMapping.getColumnNames();
	else if (this.isRelationProperty())
	    return this.foreignColumn;
	else
	    return columnMapping.getColumnName();
    }

    /**
     * (non-Javadoc).
     *
     * @return the object
     * @throws CloneNotSupportedException
     *             the clone not supported exception
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException {
	return super.clone();
    }

    /**
     * Gets the generate strategy.
     *
     * @return the generate strategy
     */
    public int getGenerateStrategy() {
	return generateStrategy;
    }

    /**
     * Sets the generate strategy.
     *
     * @param generateStrategy
     *            the new generate strategy
     */
    public void setGenerateStrategy(int generateStrategy) {
	this.generateStrategy = generateStrategy;
    }

    /**
     * Gets the generator.
     *
     * @return the generator
     */
    public String getGenerator() {
	return generator;
    }

    /**
     * Sets the generator.
     *
     * @param generator
     *            the new generator
     */
    public void setGenerator(String generator) {
	this.generator = generator;
    }

    /**
     * Checks if is load lazy.
     *
     * @return true, if is load lazy
     */
    public boolean isLoadLazy() {
	return loadLazy;
    }

    /**
     * Sets the load lazy.
     *
     * @param loadLazy
     *            the new load lazy
     */
    public void setLoadLazy(boolean loadLazy) {
	this.loadLazy = loadLazy;
    }

    /**
     * Checks if is primary key.
     *
     * @return true, if is primary key
     */
    public boolean isPrimaryKey() {
	return primaryKey;
    }

    /**
     * Sets the primary key.
     *
     * @param primaryKey
     *            the new primary key
     */
    public void setPrimaryKey(boolean primaryKey) {
	this.primaryKey = primaryKey;
    }

    /**
     * Gets the property class.
     *
     * @return the property class
     */
    public Class<?> getPropertyClass() {
	return propertyClass;
    }

    /**
     * Sets the property class.
     *
     * @param propertyClass
     *            the new property class
     */
    public void setPropertyClass(Class<?> propertyClass) {
	this.propertyClass = propertyClass;
    }

    /**
     * Gets the property name.
     *
     * @return the property name
     */
    public String getPropertyName() {
	return propertyName;
    }

    /**
     * Sets the property name.
     *
     * @param propertyName
     *            the new property name
     */
    public void setPropertyName(String propertyName) {
	this.propertyName = propertyName;
    }

    /**
     * Gets the reference class.
     *
     * @return the reference class
     */
    public Class<?> getReferenceClass() {
	return referenceClass;
    }

    /**
     * Sets the reference class.
     *
     * @param referenceClass
     *            the new reference class
     */
    public void setReferenceClass(Class<?> referenceClass) {
	this.referenceClass = referenceClass;
    }

    /**
     * Gets the reference column.
     *
     * @return the reference column
     */
    public String getReferenceColumn() {
	return referenceColumn;
    }

    /**
     * Sets the reference column.
     *
     * @param referenceColumn
     *            the new reference column
     */
    public void setReferenceColumn(String referenceColumn) {
	if (referenceColumn != null)
	    referenceColumn = referenceColumn.toUpperCase();
	this.referenceColumn = referenceColumn;
    }

    /**
     * Gets the reference order.
     *
     * @return the reference order
     */
    public String getReferenceOrder() {
	return referenceOrder;
    }

    /**
     * Sets the reference order.
     *
     * @param referenceOrder
     *            the new reference order
     */
    public void setReferenceOrder(String referenceOrder) {
	this.referenceOrder = referenceOrder;
    }

    /**
     * Gets the relation ref col1.
     *
     * @return the relation ref col1
     */
    public String getRelationRefCol1() {
	return relationRefCol1;
    }

    /**
     * Sets the relation ref col1.
     *
     * @param relationRefCol1
     *            the new relation ref col1
     */
    public void setRelationRefCol1(String relationRefCol1) {
	if (relationRefCol1 != null)
	    relationRefCol1 = relationRefCol1.toUpperCase();
	this.relationRefCol1 = relationRefCol1;
    }

    /**
     * Gets the relation ref col2.
     *
     * @return the relation ref col2
     */
    public String getRelationRefCol2() {
	return relationRefCol2;
    }

    /**
     * Sets the relation ref col2.
     *
     * @param relationRefCol2
     *            the new relation ref col2
     */
    public void setRelationRefCol2(String relationRefCol2) {
	if (relationRefCol2 != null)
	    relationRefCol2 = relationRefCol2.toUpperCase();
	this.relationRefCol2 = relationRefCol2;
    }

    /**
     * Gets the relation table.
     *
     * @return the relation table
     */
    public String getRelationTable() {
	return relationTable;
    }

    /**
     * Sets the relation table.
     *
     * @param relationTable
     *            the new relation table
     */
    public void setRelationTable(String relationTable) {
	this.relationTable = relationTable;
    }

    /**
     * Gets the reloation type.
     *
     * @return the reloation type
     */
    public int getReloationType() {
	return reloationType;
    }

    /**
     * Sets the reloation type.
     *
     * @param reloationType
     *            the new reloation type
     */
    public void setReloationType(int reloationType) {
	this.reloationType = reloationType;
    }

    /**
     * Gets the sequence name.
     *
     * @return the sequence name
     */
    public String getSequenceName() {
	return sequenceName;
    }

    /**
     * Sets the sequence name.
     *
     * @param sequenceName
     *            the new sequence name
     */
    public void setSequenceName(String sequenceName) {
	this.sequenceName = sequenceName;
    }

    /**
     * Checks if is transient field.
     *
     * @return true, if is transient field
     */
    public boolean isTransientField() {
	return transientField;
    }

    /**
     * Sets the transient field.
     *
     * @param transientField
     *            the new transient field
     */
    public void setTransientField(boolean transientField) {
	this.transientField = transientField;
    }

    /**
     * Gets the column mapping.
     *
     * @return the column mapping
     */
    public ColumnMapping getColumnMapping() {
	return columnMapping;
    }

    /**
     * Sets the column mapping.
     *
     * @param columnMapping
     *            the new column mapping
     */
    public void setColumnMapping(ColumnMapping columnMapping) {
	this.columnMapping = columnMapping;
    }

    /**
     * Checks if is complex type.
     *
     * @return true, if is complex type
     */
    public boolean isComplexType() {
	return complexType;
    }

    /**
     * Sets the complex type.
     *
     * @param complexType
     *            the new complex type
     */
    public void setComplexType(boolean complexType) {
	this.complexType = complexType;
    }

    /**
     * Gets the object mapping.
     *
     * @return the object mapping
     */
    public ObjectMapping getObjectMapping() {
	return objectMapping;
    }

    /**
     * Sets the object mapping.
     *
     * @param objectMapping
     *            the new object mapping
     */
    public void setObjectMapping(ObjectMapping objectMapping) {
	this.objectMapping = objectMapping;
    }

    /**
     * Gets the foreign column.
     *
     * @return the foreign column
     */
    public String getForeignColumn() {
	return foreignColumn;
    }

    /**
     * Sets the foreign column.
     *
     * @param foreignColumn
     *            the new foreign column
     */
    public void setForeignColumn(String foreignColumn) {
	if (foreignColumn != null)
	    foreignColumn = foreignColumn.toUpperCase();
	this.foreignColumn = foreignColumn;
    }

}
