/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;

import com.mvc.basemvc.persistence.meta.EntityMappingFactory;
import com.mvc.basemvc.persistence.meta.ObjectMapping;
import com.mvc.basemvc.persistence.meta.PropertyMapping;


/**
 * @Description 数据库字段操作类，框架内部使用
 * @ClassName ColumnValueLoader
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:59:36
 */
public abstract class ColumnValueLoader {

    /**
     * Gets the column value map recursive.
     *
     * @param entity
     *            the entity
     * @param mapping
     *            the mapping
     * @return the column value map recursive
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws SQLException
     *             the sQL exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     * @throws NoSuchMethodException
     *             the no such method exception
     */
    public Map<String, ColumnValue> getColumnValueMapRecursive(Object entity,
	    ObjectMapping mapping) throws ClassNotFoundException, SQLException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException, NoSuchMethodException {
	Map<String, ColumnValue> parentMap = getColumnValueMap(entity, mapping);
	Map<String, ColumnValue> childMap = null;

	List<PropertyMapping> complexPropertyList = mapping
		.getComplexPropertyMappingList();
	ObjectMapping childMapping = null;
	Object object = null;

	for (PropertyMapping pm : complexPropertyList) {
	    if (!filter(pm)) {
		continue;
	    }

	    if (entity == null) {
		object = null;
	    } else {
		object = PropertyUtils
			.getProperty(entity, pm.getPropertyName());
	    }

	    childMapping = EntityMappingFactory.getObjectMapping(pm
		    .getPropertyClass());
	    childMap = getColumnValueMapRecursive(object, childMapping);

	    for (Entry<String, ColumnValue> entry : childMap.entrySet()) {
		if (pm.isPrimaryKey()) {
		    entry.getValue().setPrimaryKey(true);
		}
		if (!parentMap.containsKey(entry.getKey())) {
		    parentMap.put(entry.getKey(), entry.getValue());
		}
	    }
	}

	return parentMap;
    }

    /**
     * Gets the column value map.
     *
     * @param entity
     *            the entity
     * @param mapping
     *            the mapping
     * @return the column value map
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws SQLException
     *             the sQL exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     * @throws NoSuchMethodException
     *             the no such method exception
     */
    private Map<String, ColumnValue> getColumnValueMap(Object entity,
	    ObjectMapping mapping) throws ClassNotFoundException, SQLException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException, NoSuchMethodException {
	Map<String, ColumnValue> valueMap = new HashMap<String, ColumnValue>();
	String columnName = null;
	Object value = null;
	ColumnValue columnValue = null;

	Collection<PropertyMapping> c = mapping.getPropertyMap().values();
	for (PropertyMapping pm : c) {
	    if (pm.isComplexType()) {
		continue;
	    }

	    columnName = pm.getColumnName();
	    if (valueMap.containsKey(columnName)) {
		continue;
	    }

	    if (!filter(pm)) {
		continue;
	    }

	    if (entity == null) {
		value = null;
	    } else {
		value = PropertyUtils.getProperty(entity, pm.getPropertyName());
	    }

	    columnValue = new ColumnValue();
	    columnValue.setColumnName(columnName);
	    columnValue.setColumnvalue(value);
	    columnValue.setPropertyMapping(pm);

	    if (pm.isPrimaryKey()) {
		columnValue.setPrimaryKey(true);
	    }

	    if (filter(columnValue)) {
		valueMap.put(columnName, columnValue);
	    }
	}

	return valueMap;
    }

    /**
     * Filter.
     *
     * @param columnValue
     *            the column value
     * @return true, if successful
     */
    public abstract boolean filter(ColumnValue columnValue);

    /**
     * Filter.
     *
     * @param propertyMapping
     *            the property mapping
     * @return true, if successful
     */
    public abstract boolean filter(PropertyMapping propertyMapping);
}
