/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.meta;

import java.util.Map;

/**
 * @Description 类描述
 * @ClassName ObjectMappingTraveller
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:16:45
 * @deprecated
 */
public abstract class ObjectMappingTraveller {

    /**
     * Travel.
     *
     * @param mapping
     *            the mapping
     */
    public void travel(ObjectMapping mapping) {
	if (mapping == null)
	    return;

	if (!processObjectMapping(mapping))
	    return;

	Map<String, PropertyMapping> propertyMap = mapping.getPropertyMap();
	if (propertyMap == null || propertyMap.isEmpty())
	    return;

	ObjectMapping childMapping = null;
	for (PropertyMapping pm : propertyMap.values()) {
	    if (!processPropertyMapping(pm))
		return;
	    if (pm.isComplexType()) {
		childMapping = pm.getObjectMapping();
		travel(childMapping);
	    }
	}
    }

    /**
     * Process object mapping.
     *
     * @param mapping
     *            the mapping
     * @return true, if successful
     */
    public abstract boolean processObjectMapping(ObjectMapping mapping);

    /**
     * Process property mapping.
     *
     * @param mapping
     *            the mapping
     * @return true, if successful
     */
    public abstract boolean processPropertyMapping(PropertyMapping mapping);

}
