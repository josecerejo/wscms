/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.meta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.mvc.basemvc.persistence.util.BaseUtils;
import com.mvc.basemvc.persistence.util.EntityUtils;
import com.mvc.basemvc.util.BeanUtils;
import com.mvc.basemvc.util.FieldDescriptor;

/**
 * @Description 数据库Column的Mapping操作类
 * @ClassName ColumnHandler
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:24:27
 */
public class ColumnHandler {

    /**
     * Mapping property.
     *
     * @param clazz
     *            the clazz
     * @param simpleType
     *            the simple type
     * @return the list
     * @throws SQLException
     *             the sQL exception
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    public static List<PropertyMapping> mappingProperty(Class<?> clazz,
	    boolean simpleType) throws SQLException, ClassNotFoundException,
	    InstantiationException, IllegalAccessException {
	List<PropertyMapping> mappingList = new ArrayList<PropertyMapping>();

	Map<String, FieldDescriptor> fieldMap = BeanUtils
		.getFieldDescriptors(clazz);
	if (fieldMap == null || fieldMap.isEmpty())
	    return mappingList;

	Collection<FieldDescriptor> fields = fieldMap.values();

	// Field[] fields = clazz.getDeclaredFields();
	// if ( fields==null || fields.length==0 ) return mappingList;

	Class<?> fieldClass = null;
	int modifier = 0;
	boolean isSimpleType = true;

	PropertyMapping mapping = null;
	Field field = null;

	for (FieldDescriptor f : fields) {
	    field = f.getField();
	    fieldClass = field.getType();
	    modifier = field.getModifiers();

	    if ((modifier & Modifier.FINAL) > 0
		    || (modifier & Modifier.NATIVE) > 0
		    || (modifier & Modifier.STATIC) > 0
		    || (modifier & Modifier.TRANSIENT) > 0
		    || (modifier & Modifier.VOLATILE) > 0) {
		continue;
	    }

	    isSimpleType = BaseUtils.isBaseType(fieldClass);
	    if (simpleType && isSimpleType) {
		// 基本类型
		mapping = fillBaseTypePropertyMapping(field);
		if (mapping != null)
		    mappingList.add(mapping);
	    } else if (!simpleType && !isSimpleType) {
		// 关联对象类型
		mapping = fillComplexTypePropertyMapping(field);
		if (mapping != null)
		    mappingList.add(mapping);
	    }
	}

	return mappingList;
    }

    /**
     * Fill base type property mapping.
     *
     * @param field
     *            the field
     * @return the property mapping
     * @throws SQLException
     *             the sQL exception
     */
    private static PropertyMapping fillBaseTypePropertyMapping(Field field)
	    throws SQLException {
	String propertyName = field.getName();
	Class<?> fieldClass = field.getType();

	PropertyMapping mapping = new PropertyMapping();
	mapping.setPropertyName(propertyName);
	mapping.setPropertyClass(fieldClass);

	ColumnMapping columnMapping = new ColumnMapping();
	mapping.setColumnMapping(columnMapping);

	Annotation annotation = null;

	// process for Column Annotation
	annotation = field.getAnnotation(Column.class);
	if (annotation != null) {
	    String columnName = ((Column) annotation).name();
	    if (columnName == null || columnName.length() == 0) {
		columnName = getDefaultColumn(propertyName);
	    }
	    columnMapping.setColumnName(columnName);

	    String alias = ((Column) annotation).alias();
	    if (alias != null && alias.length() > 0) {
		columnMapping.setAlias(alias);
	    }
	} else {
	    columnMapping.setColumnName(getDefaultColumn(propertyName));
	}

	// process for Transient Annotation
	annotation = field.getAnnotation(Transient.class);
	if (annotation != null) {
	    mapping.setTransientField(true);
	    return mapping;
	}

	// process for Id Annotation
	annotation = field.getAnnotation(Id.class);
	if (annotation != null) {
	    mapping.setPrimaryKey(true);
	    int strategy = ((Id) annotation).strategy();
	    if (strategy == GenerationType.SEQUENCE) {
		String generator = ((Id) annotation).generator();
		mapping.setGenerateStrategy(strategy);
		if (!StringUtils.isBlank(generator)) {
		    mapping.setGenerator(generator);
		}
	    }
	}

	// process for Clob Annotation
	annotation = field.getAnnotation(Clob.class);
	if (annotation != null) {
	    if (!"java.lang.String".equals(fieldClass.getName()))
		throw new SQLException("Clob column must mapping to String. ");
	    columnMapping.setLobType(LobType.CLOB);
	    columnMapping.setLobLazy(((Clob) annotation).lazy());
	}

	// process for Blob Annotation
	annotation = field.getAnnotation(Blob.class);
	if (annotation != null) {
	    if (!"[B".equals(fieldClass.getName()))
		throw new SQLException("Blob column must mapping to byte[]");
	    columnMapping.setLobType(LobType.BLOB);
	    columnMapping.setLobLazy(((Blob) annotation).lazy());
	}

	annotation = field.getAnnotation(Search.class);
	if (annotation != null) {
	    if (!((Search) annotation).exclude()) {
		ColumnSearch searchInfo = ColumnSearch
			.getDefaultInstance(fieldClass);
		searchInfo.setType(((Search) annotation).type());
		searchInfo.setFetch(((Search) annotation).fetch());
		searchInfo.setIgnoreValue(fieldClass.getName(),
			((Search) annotation).ignoreValue());
		columnMapping.setSearchInfo(searchInfo);
	    }
	}

	if (columnMapping.getColumnName() == null) {
	    columnMapping.setColumnName(getDefaultColumn(propertyName));
	}

	return mapping;
    }

    /**
     * Fill complex type property mapping.
     *
     * @param field
     *            the field
     * @return the property mapping
     * @throws SQLException
     *             the sQL exception
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    private static PropertyMapping fillComplexTypePropertyMapping(Field field)
	    throws SQLException, ClassNotFoundException,
	    InstantiationException, IllegalAccessException {
	String propertyName = field.getName();
	Class<?> fieldClass = field.getType();

	PropertyMapping mapping = new PropertyMapping();
	mapping.setPropertyName(propertyName);
	mapping.setPropertyClass(fieldClass);

	Annotation transientAnnotation = field.getAnnotation(Transient.class);
	Annotation oneToOneAnnotation = field.getAnnotation(OneToOne.class);
	Annotation oneToManyAnnotation = field.getAnnotation(OneToMany.class);
	Annotation manyToManyAnnotation = field.getAnnotation(ManyToMany.class);
	Annotation idAnnotation = field.getAnnotation(Id.class);
	Annotation columnAnnotation = field.getAnnotation(Column.class);

	if (oneToOneAnnotation != null || oneToManyAnnotation != null
		|| manyToManyAnnotation != null) {
	    if (columnAnnotation == null)
		throw new SQLException(
			"must assign column for relation mapping");

	    String foreignColumn = ((Column) columnAnnotation).name();
	    if (StringUtils.isBlank(foreignColumn))
		throw new SQLException(
			"Relation Type must asign column name for "
				+ mapping.getPropertyName());

	    mapping.setForeignColumn(foreignColumn);
	    mapping.setComplexType(false);

	    if (oneToOneAnnotation != null) {
		String refColumn = ((OneToOne) oneToOneAnnotation)
			.referenceColumn();
		boolean lazy = ((OneToOne) oneToOneAnnotation).lazy();

		PropertyMapping refPropertyMapping = null;
		if (refColumn == null) {
		    EntityMapping refEntityMapping = EntityMappingFactory
			    .getEntityMapping(fieldClass);
		    refPropertyMapping = refEntityMapping.getIdMapping();
		    refColumn = refPropertyMapping.getColumnName();
		}

		mapping.setReloationType(RelationType.oneToOne);
		mapping.setReferenceClass(fieldClass);
		mapping.setReferenceColumn(refColumn);
		mapping.setLoadLazy(lazy);
	    } else if (oneToManyAnnotation != null) {
		String refColumn = ((OneToMany) oneToManyAnnotation)
			.referenceColumn();
		String orderBy = ((OneToMany) oneToManyAnnotation).orderBy();
		boolean lazy = ((OneToMany) oneToManyAnnotation).lazy();

		Type gType = field.getGenericType();
		if (!(gType instanceof ParameterizedType)) {
		    throw new SQLException(
			    "OneToMany mapping must be a ParameterizedType. reference column="
				    + refColumn);
		}

		ParameterizedType pType = (ParameterizedType) gType;
		Class<?> rowClass = (Class<?>) pType.getRawType();

		if (!"java.util.List".equals(rowClass.getName())) {
		    throw new SQLException(
			    "OneToMany mapping must be a List. reference column="
				    + refColumn);
		}

		Type[] args = pType.getActualTypeArguments();
		if (args.length != 1) {
		    throw new SQLException(
			    "ParameterizedType must have a argument. OneToMany mapping reference column="
				    + refColumn);
		}

		Class<?> refClass = (Class<?>) args[0];

		mapping.setReloationType(RelationType.oneToMany);
		mapping.setReferenceClass(refClass);
		mapping.setReferenceColumn(refColumn);
		mapping.setReferenceOrder(orderBy);
		mapping.setLoadLazy(lazy);
	    } else {
		String refColumn = ((ManyToMany) manyToManyAnnotation)
			.referenceColumn();
		String orderBy = ((ManyToMany) manyToManyAnnotation).orderBy();
		String relationTable = ((ManyToMany) manyToManyAnnotation)
			.relationTable();
		String relationRefCol1 = ((ManyToMany) manyToManyAnnotation)
			.relationRefColumn1();
		String relationRefCol2 = ((ManyToMany) manyToManyAnnotation)
			.relationRefColumn2();
		boolean lazy = ((ManyToMany) manyToManyAnnotation).lazy();

		Type gType = field.getGenericType();
		if (!(gType instanceof ParameterizedType)) {
		    throw new SQLException(
			    "ManyToMany mapping must be a ParameterizedType. reference column="
				    + refColumn);
		}

		ParameterizedType pType = (ParameterizedType) gType;
		Class<?> rowClass = (Class<?>) pType.getRawType();

		if (!"java.util.List".equals(rowClass.getName())) {
		    throw new SQLException(
			    "MayToMany mapping must be a collection. reference column="
				    + refColumn);
		}

		Type[] args = pType.getActualTypeArguments();
		if (args.length != 1) {
		    throw new SQLException(
			    "ParameterizedType must have a argument. ManyToMany mapping reference column="
				    + refColumn);
		}

		Class<?> refClass = (Class<?>) args[0];

		mapping.setReloationType(RelationType.manyToMany);
		mapping.setReferenceClass(refClass);
		mapping.setReferenceColumn(refColumn);
		mapping.setReferenceOrder(orderBy);
		mapping.setRelationTable(relationTable);
		mapping.setRelationRefCol1(relationRefCol1);
		mapping.setRelationRefCol2(relationRefCol2);
		mapping.setLoadLazy(lazy);
	    }
	} else {
	    if (!EntityUtils.isEntity(fieldClass)) {
		return null;
	    }

	    mapping.setComplexType(true);

	    if (transientAnnotation != null) {
		mapping.setTransientField(true);
	    } else if (idAnnotation != null) {
		mapping.setPrimaryKey(true);
	    }

	    ObjectMapping objectMapping = EntityMappingFactory
		    .loadObjectMapping(fieldClass);
	    mapping.setObjectMapping(objectMapping);
	    for (PropertyMapping pm : objectMapping.getPropertyMap().values()) {
		pm.setPrimaryKey(mapping.isPrimaryKey());
		pm.setTransientField(mapping.isTransientField());
	    }
	}

	return mapping;
    }

    /**
     * Gets the default column.
     *
     * @param propertyName
     *            the property name
     * @return the default column
     */
    private static String getDefaultColumn(String propertyName) {
	return propertyName;
    }

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
	PropertyUtils.getPropertyDescriptors(ColumnHandler.class);
    }
}
