/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.meta;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mvc.basemvc.cache.CacheEngine;
import com.mvc.basemvc.cache.CacheEngineFactory;
import com.mvc.basemvc.persistence.util.EntityUtils;

/**
 * @Description 实体类的工厂类
 * @ClassName EntityMappingFactory
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:21:30
 */
public class EntityMappingFactory {

    /** The Constant CACHE_ENTITYMAPPING. */
    private static final String CACHE_ENTITYMAPPING = "_entityMapping";

    /** The Constant CACHE_OBJECTMAPPING. */
    private static final String CACHE_OBJECTMAPPING = "_objectMapping";

    /** The Constant CACHE_ENGINE. */
    private static final String CACHE_ENGINE = "com.mvc.basemvc.cache.DefaultCacheEngine";

    /**
     * Gets the entity mapping.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return the entity mapping
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws SQLException
     *             the sQL exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    public static <T> EntityMapping getEntityMapping(Class<T> clazz)
	    throws ClassNotFoundException, SQLException,
	    InstantiationException, IllegalAccessException {
	String className = clazz.getName();
	CacheEngine cacheEngine = CacheEngineFactory.getEngine(
		"entityMetaCache", CACHE_ENGINE);
	EntityMapping mapping = (EntityMapping) cacheEngine.get(
		CACHE_ENTITYMAPPING, className);
	if (mapping != null)
	    return mapping;

	mapping = loadEntityMapping(clazz);
	return mapping;
    }

    /**
     * Load entity mapping.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return the entity mapping
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws SQLException
     *             the sQL exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    private static <T> EntityMapping loadEntityMapping(Class<T> clazz)
	    throws ClassNotFoundException, SQLException,
	    InstantiationException, IllegalAccessException {
	Class<?> entitySuperClass = EntityUtils.getEntitySuperClass(clazz);
	if (entitySuperClass == null)
	    throw new ClassNotFoundException(clazz.getName()
		    + " or its super class must has Entity Annotation");

	String tableName = TableHandler.getTableName(entitySuperClass);
	EntityMapping mapping = new EntityMapping();
	mapping.setMappingSuperClass(entitySuperClass);
	mapping.setTableName(tableName);

	loadObjectMapping(entitySuperClass, mapping);

	// 设置主键
	Collection<PropertyMapping> c = mapping.getPropertyMap().values();
	PropertyMapping idPropertyMapping = null;
	for (PropertyMapping cm : c) {
	    if (cm.isPrimaryKey()) {
		idPropertyMapping = cm;
		break;
	    }

	    if (!cm.isComplexType() && !cm.isTransientField()
		    && "id".equals(cm.getPropertyName())) {
		idPropertyMapping = cm;
	    }
	}

	if (idPropertyMapping == null)
	    throw new SQLException("entity mapping must assign primary key");

	if (!idPropertyMapping.isPrimaryKey()) {
	    idPropertyMapping.setPrimaryKey(true);
	    idPropertyMapping.setGenerateStrategy(GenerationType.SEQUENCE);
	    idPropertyMapping.setGenerator(getDefaultSequence(tableName));
	}

	if (mapping.getIdMapping() == null) {
	    mapping.setIdMapping(idPropertyMapping);
	}

	if (idPropertyMapping.isComplexType()) {
	    ObjectMapping objectMapping = idPropertyMapping.getObjectMapping();
	    for (PropertyMapping pm : objectMapping.getPropertyMap().values()) {
		if (!pm.isTransientField())
		    pm.setPrimaryKey(true);
	    }
	} else {
	    if (idPropertyMapping.getGenerateStrategy() == GenerationType.SEQUENCE
		    && StringUtils.isBlank(idPropertyMapping.getGenerator())) {
		String generator = getDefaultSequence(tableName);
		idPropertyMapping.setGenerator(generator);
	    }
	}

	return mapping;
    }

    /**
     * Gets the object mapping.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return the object mapping
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws SQLException
     *             the sQL exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    public static <T> ObjectMapping getObjectMapping(Class<T> clazz)
	    throws ClassNotFoundException, SQLException,
	    InstantiationException, IllegalAccessException {
	String className = clazz.getName();
	CacheEngine cacheEngine = CacheEngineFactory.getEngine(
		"entityMetaCache", CACHE_ENGINE);
	ObjectMapping mapping = (ObjectMapping) cacheEngine.get(
		CACHE_OBJECTMAPPING, className);
	if (mapping != null)
	    return mapping;

	mapping = loadObjectMapping(clazz);
	return mapping;
    }

    /**
     * Load object mapping.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return the object mapping
     * @throws SQLException
     *             the sQL exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws ClassNotFoundException
     *             the class not found exception
     */
    public static <T> ObjectMapping loadObjectMapping(Class<T> clazz)
	    throws SQLException, InstantiationException,
	    IllegalAccessException, ClassNotFoundException {
	ObjectMapping mapping = new ObjectMapping();
	loadObjectMapping(clazz, mapping);
	return mapping;
    }

    /**
     * Load object mapping.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @param mapping
     *            the mapping
     * @throws SQLException
     *             the sQL exception
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    private static <T> void loadObjectMapping(Class<T> clazz,
	    ObjectMapping mapping) throws SQLException, ClassNotFoundException,
	    InstantiationException, IllegalAccessException {
	mapping.setEntityClass(clazz);

	// 简单数据类型
	loadSimpleProperties(mapping, clazz);

	// 关联数据类型
	loadComplexProperties(mapping, clazz);
    }

    /**
     * Load simple properties.
     *
     * @param <T>
     *            the generic type
     * @param objectMapping
     *            the object mapping
     * @param clazz
     *            the clazz
     * @throws SQLException
     *             the sQL exception
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    private static <T> void loadSimpleProperties(ObjectMapping objectMapping,
	    Class<T> clazz) throws SQLException, ClassNotFoundException,
	    InstantiationException, IllegalAccessException {
	List<PropertyMapping> propertyList = ColumnHandler.mappingProperty(
		clazz, true);
	if (propertyList == null || propertyList.isEmpty())
	    return;

	Map<String, PropertyMapping> propertyMap = objectMapping
		.getPropertyMap();
	for (PropertyMapping property : propertyList) {
	    propertyMap.put(property.getPropertyName(), property);
	}
    }

    /**
     * Load complex properties.
     *
     * @param <T>
     *            the generic type
     * @param objectMapping
     *            the object mapping
     * @param clazz
     *            the clazz
     * @throws SQLException
     *             the sQL exception
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    private static <T> void loadComplexProperties(ObjectMapping objectMapping,
	    Class<T> clazz) throws SQLException, ClassNotFoundException,
	    InstantiationException, IllegalAccessException {
	List<PropertyMapping> propertyList = ColumnHandler.mappingProperty(
		clazz, false);
	if (propertyList == null || propertyList.isEmpty())
	    return;

	Map<String, PropertyMapping> propertyMap = objectMapping
		.getPropertyMap();
	for (PropertyMapping property : propertyList) {
	    propertyMap.put(property.getPropertyName(), property);
	}
    }

    /**
     * Gets the default sequence.
     *
     * @param tableName
     *            the table name
     * @return the default sequence
     */
    private static String getDefaultSequence(String tableName) {
	return "seq_" + tableName;
    }

}
