/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @Description 对象mapping类
 * @ClassName ObjectMapping
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:18:23
 */
public class ObjectMapping implements Serializable, Cloneable {

    /**
     * @Description
     * @field long serialVersionUID
     * @Created 2013 2013-8-5 下午04:17:26
     */
    private static final long serialVersionUID = 144778838343829016L;

    /** The entity class. */
    private Class<?> entityClass;

    /** The property map. */
    private Map<String, PropertyMapping> propertyMap = new HashMap<String, PropertyMapping>();

    /** The column2 property map. */
    private Map<String, List<PropertyMapping>> column2PropertyMap = new Hashtable<String, List<PropertyMapping>>();

    /** The alias2 property map. */
    // private Map<String, List<PropertyMapping>> alias2PropertyMap = new
    // Hashtable<String, List<PropertyMapping>>();

    /** The complex property mapping list. */
    private List<PropertyMapping> complexPropertyMappingList = null;

    /** The column names. */
    private String columnNames;

    /** The lazy lob property mapping list. */
    private List<PropertyMapping> lazyLobPropertyMappingList = null;

    /** The lazy relation property mapping group map. */
    private Map<String, List<PropertyMapping>> lazyRelationPropertyMappingGroupMap = null;

    /**
     * Gets the property mapping.
     *
     * @param propertyName
     *            the property name
     * @return the property mapping
     */
    public PropertyMapping getPropertyMapping(String propertyName) {
	if (StringUtils.isBlank(propertyName))
	    return null;
	PropertyMapping propertyMapping = propertyMap.get(propertyName);
	return propertyMapping;
    }

    /**
     * Gets the propery mapping by column name.
     *
     * @param columnName
     *            the column name
     * @return the propery mapping by column name
     */
    @SuppressWarnings("unchecked")
    public List<PropertyMapping> getProperyMappingByColumnName(String columnName) {
	if (StringUtils.isBlank(columnName)) {
	    return Collections.EMPTY_LIST;
	}

	columnName = columnName.toUpperCase().trim();

	List<PropertyMapping> pmList = column2PropertyMap.get(columnName);
	if (pmList != null)
	    return pmList;

	Collection<PropertyMapping> c = propertyMap.values();
	pmList = new ArrayList<PropertyMapping>();

	for (PropertyMapping pm : c) {
	    if (!pm.isComplexType() && !pm.isRelationProperty()) {
		if (columnName.equalsIgnoreCase(pm.getColumnMapping()
			.getAlias())) {
		    pmList.add(pm);
		} else if (columnName.equalsIgnoreCase(pm.getColumnMapping()
			.getColumnName())) {
		    pmList.add(pm);
		}
	    }
	}

	column2PropertyMap.put(columnName, pmList);
	return pmList;
    }

    /**
     * Gets the complex property mapping list.
     *
     * @return the complex property mapping list
     */
    public List<PropertyMapping> getComplexPropertyMappingList() {
	if (complexPropertyMappingList != null)
	    return complexPropertyMappingList;

	Collection<PropertyMapping> c = propertyMap.values();
	List<PropertyMapping> pmList = new ArrayList<PropertyMapping>();
	for (PropertyMapping pm : c) {
	    if (pm.isComplexType())
		pmList.add(pm);
	}

	complexPropertyMappingList = pmList;
	return pmList;
    }

    /**
     * Gets the column names.
     *
     * @return the column names
     */
    public String getColumnNames() {
	if (columnNames != null)
	    return columnNames;

	String names = "";
	for (PropertyMapping pm : propertyMap.values()) {
	    if (pm.isTransientField() || pm.isComplexType()
		    || pm.isRelationProperty() || !pm.isBaseTypeProperty())
		continue;
	    names += "," + pm.getColumnName();
	}

	if (names.length() > 0)
	    names = names.substring(1);
	columnNames = names;
	return names;
    }

    /**
     * Checks for lazy relation.
     *
     * @return true, if successful
     */
    public boolean hasLazyRelation() {
	if (propertyMap == null || propertyMap.isEmpty())
	    return false;

	for (PropertyMapping pm : propertyMap.values()) {
	    if (pm.isRelationProperty())
		return true;
	}

	return false;
    }

    /**
     * Checks for lazy lob.
     *
     * @return true, if successful
     */
    public boolean hasLazyLob() {
	List<PropertyMapping> list = getLazyLob();
	return list != null && !list.isEmpty();
    }

    /**
     * Gets the lazy lob.
     *
     * @return the lazy lob
     */
    public List<PropertyMapping> getLazyLob() {
	if (lazyLobPropertyMappingList != null)
	    return lazyLobPropertyMappingList;
	List<PropertyMapping> result = new ArrayList<PropertyMapping>();
	if (propertyMap == null || propertyMap.isEmpty())
	    return result;

	ColumnMapping cm = null;
	for (PropertyMapping pm : propertyMap.values()) {
	    if (pm.isComplexType() || pm.isRelationProperty())
		continue;
	    cm = pm.getColumnMapping();
	    if (cm.getLobType() == LobType.BLOB
		    || cm.getLobType() == LobType.CLOB) {
		if (cm.isLobLazy())
		    result.add(pm);
	    }
	}

	lazyLobPropertyMappingList = result;
	return result;
    }

    /**
     * Gets the lazy relation.
     *
     * @return the lazy relation
     */
    public Map<String, List<PropertyMapping>> getLazyRelation() {
	if (lazyRelationPropertyMappingGroupMap != null)
	    return lazyRelationPropertyMappingGroupMap;

	Map<String, List<PropertyMapping>> result = new HashMap<String, List<PropertyMapping>>();

	List<PropertyMapping> ooList = new ArrayList<PropertyMapping>();
	List<PropertyMapping> omList = new ArrayList<PropertyMapping>();
	List<PropertyMapping> mmList = new ArrayList<PropertyMapping>();

	result.put("oo", ooList);
	result.put("om", omList);
	result.put("mm", mmList);

	if (propertyMap == null || propertyMap.isEmpty())
	    return result;

	for (PropertyMapping pm : propertyMap.values()) {
	    if (pm.getReloationType() == RelationType.oneToOne) {
		if (pm.isLoadLazy())
		    ooList.add(pm);
	    } else if (pm.getReloationType() == RelationType.oneToMany) {
		if (pm.isLoadLazy())
		    omList.add(pm);
	    } else if (pm.getReloationType() == RelationType.manyToMany) {
		if (pm.isLoadLazy())
		    mmList.add(pm);
	    }
	}

	lazyRelationPropertyMappingGroupMap = result;
	return result;
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
     * Gets the entity class.
     *
     * @return the entity class
     */
    public Class<?> getEntityClass() {
	return entityClass;
    }

    /**
     * Sets the entity class.
     *
     * @param entityClass
     *            the new entity class
     */
    public void setEntityClass(Class<?> entityClass) {
	this.entityClass = entityClass;
    }

    /**
     * Gets the property map.
     *
     * @return the property map
     */
    public Map<String, PropertyMapping> getPropertyMap() {
	return propertyMap;
    }

    /**
     * Sets the property map.
     *
     * @param propertyMap
     *            the property map
     */
    public void setPropertyMap(Map<String, PropertyMapping> propertyMap) {
	this.propertyMap = propertyMap;
    }
}
