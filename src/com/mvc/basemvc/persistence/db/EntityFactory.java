/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import org.apache.commons.beanutils.BeanMap;

import com.mvc.basemvc.persistence.meta.ObjectMapping;
import com.mvc.basemvc.persistence.meta.PropertyMapping;

/**
 * @Description 创建实体类工厂类，框架内部使用,不要在框架外部调用
 * @ClassName EntityFactory
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:57:52
 */
public class EntityFactory {

    /**
     * Creates a new Entity object.
     *
     * @param mapping
     *            the mapping
     * @return the object
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    public static Object createEntity(ObjectMapping mapping)
	    throws InstantiationException, IllegalAccessException {
	Object entity = null;
	if (mapping.hasLazyRelation() || mapping.hasLazyLob()) {
	    entity = createEntityProxy(mapping);
	} else {
	    Class<?> entityClass = mapping.getEntityClass();
	    entity = entityClass.newInstance();
	}
	return entity;

    }

    /**
     * Creates a new Entity object.
     *
     * @param mapping
     *            the mapping
     * @return the object
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    private static Object createEntityProxy(ObjectMapping mapping)
	    throws InstantiationException, IllegalAccessException {
	Enhancer enhancer = new Enhancer();
	enhancer.setSuperclass(mapping.getEntityClass());
	enhancer.setUseCache(true);
	Callback ooCallback = new OneToOneInterceptor();
	Callback omCallback = new OneToManyInterceptor();
	Callback mmCallback = new ManyToManyInterceptor();

	LobInterceptor lobCallback = new LobInterceptor();

	enhancer.setCallbacks(new Callback[] { NoOp.INSTANCE, ooCallback,
		omCallback, mmCallback, lobCallback });

	LazyLoadCallbackFilter filter = new LazyLoadCallbackFilter();

	Map<String, List<PropertyMapping>> lazyMap = mapping.getLazyRelation();
	List<PropertyMapping> ooList = lazyMap.get("oo");
	List<PropertyMapping> omList = lazyMap.get("om");
	List<PropertyMapping> mmList = lazyMap.get("mm");

	List<PropertyMapping> lobList = mapping.getLazyLob();

	Object entity = mapping.getEntityClass().newInstance();
	BeanMap beanMap = new BeanMap(entity);

	PropertyMapping propertyMapping = null;
	Method readMethod = null;

	for (int i = 0; i < ooList.size(); i++) {
	    propertyMapping = ooList.get(i);
	    readMethod = beanMap.getReadMethod(propertyMapping
		    .getPropertyName());
	    if (readMethod != null) {
		filter.addOneToOneMethod(readMethod);
	    }
	}

	for (int i = 0; i < omList.size(); i++) {
	    propertyMapping = omList.get(i);
	    readMethod = beanMap.getReadMethod(propertyMapping
		    .getPropertyName());
	    if (readMethod != null) {
		filter.addOneToManyMethod(readMethod);
	    }
	}

	for (int i = 0; i < mmList.size(); i++) {
	    propertyMapping = mmList.get(i);
	    readMethod = beanMap.getReadMethod(propertyMapping
		    .getPropertyName());
	    if (readMethod != null) {
		filter.addManyToManyMethod(readMethod);
	    }
	}

	for (int i = 0; i < lobList.size(); i++) {
	    propertyMapping = lobList.get(i);
	    readMethod = beanMap.getReadMethod(propertyMapping
		    .getPropertyName());
	    if (readMethod != null) {
		filter.addLobMethod(readMethod);
	    }
	}

	enhancer.setCallbackFilter(filter);

	Object proxy = enhancer.create();

	return proxy;
    }

}
