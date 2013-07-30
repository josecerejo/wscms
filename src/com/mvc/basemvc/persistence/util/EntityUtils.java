/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.mvc.basemvc.persistence.db.ColumnValue;
import com.mvc.basemvc.persistence.meta.Entity;
import com.mvc.basemvc.persistence.meta.EntityMapping;
import com.mvc.basemvc.persistence.meta.EntityMappingFactory;
import com.mvc.basemvc.persistence.meta.ObjectMapping;
import com.mvc.basemvc.persistence.meta.PropertyMapping;

/**
 * @Description 实体bean工具类
 * @ClassName EntityUtils
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:02:34
 */
public class EntityUtils {

    /**
     * Gets the id column map.
     *
     * @param entity
     *            the entity
     * @return the id column map
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
    public static Map<String, ColumnValue> getIdColumnMap(Object entity)
	    throws ClassNotFoundException, SQLException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException, NoSuchMethodException {
	ColumnValue cv = null;
	Map<String, ColumnValue> idColumnMap = new HashMap<String, ColumnValue>();

	EntityMapping entityMapping = EntityMappingFactory
		.getEntityMapping(entity.getClass());
	PropertyMapping idPm = entityMapping.getIdMapping();
	if (!idPm.isComplexType()) {
	    cv = new ColumnValue();
	    cv.setColumnName(idPm.getColumnName());
	    cv.setColumnvalue(PropertyUtils.getProperty(entity,
		    idPm.getPropertyName()));
	    cv.setPrimaryKey(true);
	    cv.setPropertyMapping(idPm);
	    idColumnMap.put(idPm.getColumnName(), cv);
	} else {
	    Object idValue = PropertyUtils.getProperty(entity,
		    idPm.getPropertyName());
	    ObjectMapping objectMapping = idPm.getObjectMapping();
	    for (PropertyMapping pm : objectMapping.getPropertyMap().values()) {
		if (pm.isTransientField() || pm.isRelationProperty()
			|| pm.isComplexType() || !pm.isBaseTypeProperty()) {
		    continue;
		}
		cv = new ColumnValue();
		cv.setColumnName(pm.getColumnName());
		cv.setColumnvalue(PropertyUtils.getProperty(idValue,
			pm.getPropertyName()));
		cv.setPrimaryKey(true);
		cv.setPropertyMapping(pm);
		idColumnMap.put(pm.getColumnName(), cv);
	    }
	}

	return idColumnMap;
    }

    // public static Map<String, ColumnValue> getValueMapRecursive( Object
    // entity, boolean isEntity, boolean ignoreTransient, boolean isPk )
    // throws ClassNotFoundException, SQLException, InstantiationException,
    // IllegalAccessException, InvocationTargetException, NoSuchMethodException
    // {
    // Map<String, ColumnValue> parentMap = getValueMap( entity, true,
    // ignoreTransient, isPk );
    // Map<String, ColumnValue> childMap = null;
    //
    // ObjectMapping objectMapping = null;
    // if ( isEntity ) {
    // objectMapping = EntityMappingFactory.getEntityMapping( entity.getClass()
    // );
    // }
    // else {
    // objectMapping = EntityMappingFactory.getObjectMapping( entity.getClass()
    // );
    // }
    //
    // List<PropertyMapping> complexPropertyList =
    // objectMapping.getComplexPropertyMappingList();
    //
    // for ( PropertyMapping pm : complexPropertyList ) {
    // Object object = PropertyUtils.getProperty( entity, pm.getPropertyName()
    // );
    //
    // if ( !isPk ) isPk = pm.isPrimaryKey();
    //
    // childMap = EntityUtils.getValueMapRecursive( object, false,
    // ignoreTransient, isPk );
    // for ( Entry<String, ColumnValue> entry : childMap.entrySet() ) {
    // if ( !parentMap.containsKey(entry.getKey()) ) {
    // parentMap.put( entry.getKey(), entry.getValue() );
    // }
    // }
    // }
    //
    // return parentMap;
    // }
    //
    //
    // private static Map<String, ColumnValue> getValueMap( Object entity,
    // boolean isEntity, boolean ignoreTransient, boolean isPk )
    // throws ClassNotFoundException, SQLException, InstantiationException,
    // IllegalAccessException, InvocationTargetException, NoSuchMethodException
    // {
    // ObjectMapping mapping = null;
    //
    // if ( isEntity ) mapping = EntityMappingFactory.getEntityMapping(
    // entity.getClass() );
    // else mapping = EntityMappingFactory.getObjectMapping( entity.getClass()
    // );
    //
    // Map<String, ColumnValue> valueMap = new HashMap<String, ColumnValue>();
    // String columnName = null;
    // Object value = null;
    // ColumnValue columnValue = null;
    //
    // Collection<PropertyMapping> c = mapping.getPropertyMap().values();
    // for ( PropertyMapping pm : c ) {
    // if ( pm.isComplexType() || pm.isRelationProperty() ) continue;
    //
    // columnName = pm.getColumnName();
    // if ( valueMap.containsKey(columnName) ) continue;
    // value = PropertyUtils.getProperty( entity, pm.getPropertyName() );
    //
    // columnValue = new ColumnValue();
    // columnValue.setColumnName( columnName );
    // columnValue.setColumnvalue( value );
    // columnValue.setPropertyMapping( pm );
    //
    // if ( pm.isPrimaryKey() ) columnValue.setPrimaryKey( true );
    // else columnValue.setPrimaryKey( isPk );
    //
    // valueMap.put( columnName, columnValue );
    // }
    //
    // return valueMap;
    // }

    /**
     * Clone entity from proxy.
     *
     * @param source
     *            the source
     * @return the object
     * @throws Exception
     *             the exception
     */
    public static Object cloneEntityFromProxy(Object source) throws Exception {
	try {
	    if (source == null) {
		return null;
	    }
	    if (!isCglibProxy(source)) {
		return source;
	    }

	    Class<?> sourceClass = source.getClass();
	    if (!isEntity(sourceClass)) {
		return source;
	    }

	    String className = getClassNameProxyedByCglib(sourceClass);
	    Class<?> entityClass = Class.forName(className);
	    Object entity = entityClass.newInstance();

	    copyProperties(source, entity);
	    return entity;
	} catch (Exception e) {
	    e.printStackTrace();
	    throw e;
	}
    }

    /**
     * Copy properties.
     *
     * @param source
     *            the source
     * @param target
     *            the target
     * @throws Exception
     *             the exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void copyProperties(Object source, Object target)
	    throws Exception {
	BeanUtils.copyProperties(target, source);

	PropertyDescriptor[] sProperties = PropertyUtils
		.getPropertyDescriptors(source.getClass());
	String propertyName = null;
	Object propertyValue = null;
	Object newValue = null;

	for (PropertyDescriptor property : sProperties) {
	    propertyName = property.getName();
	    propertyValue = PropertyUtils.getProperty(source, propertyName);

	    if (propertyValue == null) {
		continue;
	    }

	    if (propertyValue instanceof List) {
		List currList = (List) propertyValue;
		List newList = new ArrayList(currList.size());
		for (Object e : currList) {
		    newList.add(cloneEntityFromProxy(e));
		}
		PropertyUtils.setProperty(target, propertyName, newList);
	    } else if (isCglibProxy(propertyValue.getClass())) {
		newValue = cloneEntityFromProxy(propertyValue);
		PropertyUtils.setProperty(target, propertyName, newValue);
	    }
	}
    }

    // private static void copyProperties( Object source, Object target ) throws
    // Exception
    // {
    // try {
    // PropertyDescriptor[] sProperties = PropertyUtils.getPropertyDescriptors(
    // source.getClass() );
    // PropertyDescriptor[] tProperties = PropertyUtils.getPropertyDescriptors(
    // target.getClass() );
    // EntityMapping entityMapping = EntityMappingFactory.getEntityMapping(
    // target.getClass() );
    //
    // Map tPropertiesMap = new HashMap(tProperties.length);
    // for ( PropertyDescriptor property : tProperties ) {
    // tPropertiesMap.put( property.getName(), property );
    // }
    //
    // String propertyName = null;
    // ColumnMapping columnMapping = null;
    // PropertyDescriptor tProperty = null;
    // Object value = null;
    //
    //
    // for ( PropertyDescriptor property : sProperties ) {
    // try {
    // propertyName = property.getName();
    // columnMapping = entityMapping.getPropertyMap().get( propertyName );
    //
    // if ( property.getReadMethod()==null ) continue;
    //
    // tProperty = (PropertyDescriptor) tPropertiesMap.get( propertyName );
    // if ( tProperty==null || tProperty.getWriteMethod()==null ) continue;
    //
    // if ( columnMapping!=null && columnMapping.isRelationColumn() ) {
    // Field field = target.getClass().getDeclaredField(propertyName);
    // field.setAccessible(true);
    // value = field.get(source);
    //
    // if ( value instanceof List ) {
    // List currList = (List) value;
    // List newList = new ArrayList(currList.size());
    // for ( Object e : currList ) {
    // newList.add( EntityUtils.cloneEntityFromProxy(e) );
    // }
    // PropertyUtils.setProperty( target, propertyName, newList );
    // }
    // else {
    // value = EntityUtils.cloneEntityFromProxy( value );
    // PropertyUtils.setProperty( target, propertyName, value);
    // }
    // }
    // else {
    // value = PropertyUtils.getProperty( source, propertyName );
    // PropertyUtils.setProperty( target, propertyName, value);
    // }
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // throw e;
    // }
    // }

    /**
     * Checks if is entity.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return true, if is entity
     */
    public static <T> boolean isEntity(Class<T> clazz) {
	Class<?> entityClass = getEntitySuperClass(clazz);
	return entityClass != null;
    }

    /**
     * Gets the entity super class.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return the entity super class
     */
    public static <T> Class<?> getEntitySuperClass(Class<? extends T> clazz) {
	Class<?> superClass = clazz;
	while (superClass != null
		&& !"java.lang.Object".equals(superClass.getName())) {
	    if (superClass.isAnnotationPresent(Entity.class)) {
		return superClass;
	    }
	    superClass = superClass.getSuperclass();
	}

	return null;
    }

    /**
     * Checks if is cglib proxy.
     *
     * @param entity
     *            the entity
     * @return true, if is cglib proxy
     */
    public static boolean isCglibProxy(Object entity) {
	return (entity instanceof net.sf.cglib.proxy.Factory);
    }

    /**
     * Gets the class name proxyed by cglib.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return the class name proxyed by cglib
     * @throws Exception
     *             the exception
     */
    public static <T> String getClassNameProxyedByCglib(Class<T> clazz)
	    throws Exception {
	String classname = clazz.getName();
	int p = classname.indexOf("$$EnhancerByCGLIB$$");
	if (p > 0) {
	    classname = classname.substring(0, p);
	    return classname;
	}

	throw new Exception("not a proxy class");
    }

}
