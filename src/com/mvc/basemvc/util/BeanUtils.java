/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * @Description 工具类
 * @ClassName BeanUtils
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午03:58:10
 */
public class BeanUtils {

    /** The Constant DATEFORMAT_10LENGTH. */
    private static final int DATEFORMAT_10LENGTH = 10;

    /** The bean map. */
    private static Map<String, Map<String, Field>> beanMap = Collections
	    .synchronizedMap(new HashMap<String, Map<String, Field>>());

    /** The field descriptor map. */
    private static Map<String, Map<String, FieldDescriptor>> fieldDescriptorMap = Collections
	    .synchronizedMap(new HashMap<String, Map<String, FieldDescriptor>>());

    /** The iscache. */
    private static String iscache = SystemGlobals
	    .getPreference("cache.bean.field");

    /**
     * Copy properties.
     *
     * @param target
     *            the target
     * @param source
     *            the source
     */
    public static void copyProperties(Object target, Object source) {
	Map<String, Field> sourceFields = getFields(source);
	Map<String, Field> targetFields = getFields(target);
	if (sourceFields != null && !sourceFields.isEmpty()
		&& targetFields != null && !targetFields.isEmpty()) {
	    Iterator<Map.Entry<String, Field>> it = sourceFields.entrySet()
		    .iterator();
	    while (it.hasNext()) {
		Map.Entry<String, Field> entry = it.next();
		String key = entry.getKey();
		Field fd = entry.getValue();
		Field targetField = targetFields.get(key);
		if (targetField != null) {
		    try {
			copyValue(target, targetField, source, fd);
		    } catch (ParseException e) {
			e.printStackTrace();
		    }
		}
	    }
	}
    }

    /**
     * Copy value.
     *
     * @param targetObj
     *            the target obj
     * @param targetField
     *            the target field
     * @param sourceObj
     *            the source obj
     * @param sourceField
     *            the source field
     * @throws ParseException
     *             the parse exception
     */
    public static void copyValue(Object targetObj, Field targetField,
	    Object sourceObj, Field sourceField) throws ParseException {
	Type targetType = targetField.getType();
	Type sourceType = sourceField.getType();
	String targetStr = targetType.toString();
	Object sourceValue = null;
	try {
	    sourceField.setAccessible(true);
	    sourceValue = sourceField.get(sourceObj);

	    targetField.setAccessible(true);

	    if (targetType.equals(sourceType)) {
		targetField.set(targetObj, sourceValue);
		return;
	    }

	    if (sourceValue == null) {
		if ("int".equals(targetStr) || "long".equals(targetStr)
			|| "byte".equals(targetStr)
			|| "short".equals(targetStr)
			|| "float".equals(targetStr)
			|| "double".equals(targetStr)) {
		    targetField.set(targetObj, 0);
		} else if ("boolean".equals(targetStr)) {
		    targetField.set(targetObj, false);
		} else {
		    targetField.set(targetObj, null);
		}
		return;
	    }

	    String sourceStrValue = null;
	    if (sourceType.toString().equals(Date.class.toString())) {
		sourceStrValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			.format(sourceValue);
	    } else {
		sourceStrValue = String.valueOf(sourceValue);
	    }

	    if (targetStr.equals(String.class.toString())) {
		targetField.set(targetObj, sourceStrValue);
	    } else if (targetStr.equals(int.class.toString())) {
		targetField.set(targetObj,
			Integer.parseInt(getPointPre(sourceStrValue)));
	    } else if (targetStr.equals(byte.class.toString())) {
		targetField.set(targetObj,
			Byte.parseByte(getPointPre(sourceStrValue)));
	    } else if (targetStr.equals(short.class.toString())) {
		targetField.set(targetObj,
			Short.parseShort(getPointPre(sourceStrValue)));
	    } else if (targetStr.equals(long.class.toString())) {
		targetField.set(targetObj,
			Long.parseLong(getPointPre(sourceStrValue)));
	    } else if (targetStr.equals(float.class.toString())) {
		targetField.set(targetObj, Float.parseFloat(sourceStrValue));
	    } else if (targetStr.equals(double.class.toString())) {
		targetField.set(targetObj, Double.parseDouble(sourceStrValue));
	    } else if (targetStr.equals(boolean.class.toString())) {
		targetField
			.set(targetObj, Boolean.parseBoolean(sourceStrValue));
	    } else if (targetStr.equals(Integer.class.toString())) {
		targetField.set(targetObj,
			Integer.valueOf(getPointPre(sourceStrValue)));
	    } else if (targetStr.equals(Byte.class.toString())) {
		targetField.set(targetObj,
			Byte.valueOf(getPointPre(sourceStrValue)));
	    } else if (targetStr.equals(Short.class.toString())) {
		targetField.set(targetObj,
			Short.valueOf(getPointPre(sourceStrValue)));
	    } else if (targetStr.equals(Long.class.toString())) {
		targetField.set(targetObj,
			Long.valueOf(getPointPre(sourceStrValue)));
	    } else if (targetStr.equals(Float.class.toString())) {
		targetField.set(targetObj, Float.valueOf(sourceStrValue));
	    } else if (targetStr.equals(Double.class.toString())) {
		targetField.set(targetObj, Double.valueOf(sourceStrValue));
	    } else if (targetStr.equals(Boolean.class.toString())) {
		targetField.set(targetObj, Boolean.valueOf(sourceStrValue));
	    } else if (targetStr.equals(Date.class.toString())) {
		if (sourceStrValue.length() == DATEFORMAT_10LENGTH) {
		    targetField.set(targetObj, new SimpleDateFormat(
			    "yyyy-MM-dd").parse(sourceStrValue));
		} else {
		    targetField.set(targetObj, new SimpleDateFormat(
			    "yyyy-MM-dd HH:mm:ss").parse(sourceStrValue));
		}
	    }
	} catch (Exception e) {
	    // e.printStackTrace();
	}
    }

    /**
     * Gets the point pre.
     *
     * @param str
     *            the str
     * @return the point pre
     */
    private static String getPointPre(String str) {
	int index = str.indexOf(".");
	if (index != -1) {
	    str = str.substring(0, index);
	}
	return str;
    }

    /**
     * Gets the fields.
     *
     * @param obj
     *            the obj
     * @return the fields
     */
    public static Map<String, Field> getFields(Object obj) {
	Class<?> c = obj.getClass();
	Map<String, Field> fs = null;
	String className = c.getName();
	fs = beanMap.get(className);
	if (fs == null) {
	    fs = new HashMap<String, Field>();
	    Field[] fields = null;

	    while (!c.getName().equals(Object.class.getName())) {
		fields = c.getDeclaredFields();
		for (Field field : fields) {
		    if (!fs.containsKey(field.getName())) {
			fs.put(field.getName(), field);
		    }
		}

		c = c.getSuperclass();
	    }

	    if ("true".equals(iscache)) {
		beanMap.put(className, fs);
	    }
	}
	return fs;
    }

    /**
     * Gets the field descriptors.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return the field descriptors
     */
    public static <T> Map<String, FieldDescriptor> getFieldDescriptors(
	    Class<T> clazz) {
	Map<String, FieldDescriptor> map = fieldDescriptorMap.get(clazz
		.getName());
	if (map != null) {
	    return map;
	}

	map = new HashMap<String, FieldDescriptor>();
	Class<?> c = clazz;
	Field f = null;
	FieldDescriptor fd = null;
	String propertyName = null;

	while (c != null && !c.getName().equals(Object.class.getName())) {
	    PropertyDescriptor[] properties = PropertyUtils
		    .getPropertyDescriptors(c);
	    for (PropertyDescriptor p : properties) {
		try {
		    propertyName = p.getName();
		    f = c.getDeclaredField(propertyName);
		    if (f != null) {
			fd = new FieldDescriptor();
			fd.setField(f);
			fd.setReadable(p.getReadMethod() != null);
			fd.setWriteable(p.getWriteMethod() != null);
		    }
		    if (!map.containsKey(propertyName)) {
			map.put(propertyName, fd);
		    }
		} catch (Exception e) {
		}
	    }
	    c = c.getSuperclass();
	}

	return map;
    }

    /**
     * Checks if is list.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return true, if is list
     */
    public static <T> boolean isList(Class<T> clazz) {
	if ("java.util.List".equals(clazz.getName())) {
	    return true;
	}

	Class<?>[] interfaces = clazz.getInterfaces();
	if (interfaces == null || interfaces.length == 0)
	    return false;

	for (Class<?> c : interfaces) {
	    if ("java.util.List".equals(c.getName())) {
		return true;
	    }
	}

	return false;
    }

    /**
     * Checks if is map.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return true, if is map
     */
    public static <T> boolean isMap(Class<T> clazz) {
	if ("java.util.Map".equals(clazz.getName())) {
	    return true;
	}

	Class<?>[] interfaces = clazz.getInterfaces();
	if (interfaces == null || interfaces.length == 0)
	    return false;

	for (Class<?> c : interfaces) {
	    if ("java.util.Map".equals(c.getName())) {
		return true;
	    }
	}

	return false;
    }

    /**
     * Checks if is array.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return true, if is array
     */
    public static <T> boolean isArray(Class<T> clazz) {
	String className = clazz.getName();
	if (className.startsWith("["))
	    return true;
	else
	    return false;
    }

    /**
     * Checks if is primitive.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return true, if is primitive
     */
    public static <T> boolean isPrimitive(Class<T> clazz) {
	String className = clazz.getName();
	if ("int".equals(className) || "java.lang.Integer".equals(className)) {
	    return true;
	} else if ("java.lang.String".equals(className)) {
	    return true;
	} else if ("long".equals(className)
		|| "java.lang.Long".equals(className)) {
	    return true;
	} else if ("float".equals(className)
		|| "java.lang.Float".equals(className)) {
	    return true;
	} else if ("java.util.Date".equals(className)) {
	    return true;
	} else if ("byte".equals(className)
		|| "java.lang.Byte".equals(className)) {
	    return true;
	} else if ("short".equals(className)
		|| "java.lang.Short".equals(className)) {
	    return true;
	} else if ("double".equals(className)
		|| "java.lang.Double".equals(className)) {
	    return true;
	} else if ("char".equals(className)
		|| "java.lang.Character".equals(className)) {
	    return true;
	} else if ("boolean".equals(className)
		|| "java.lang.Boolean".equals(className)) {
	    return true;
	} else {
	    return false;
	}
    }
}
