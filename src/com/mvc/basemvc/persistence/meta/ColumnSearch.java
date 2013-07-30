/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.meta;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * @Description 数据库字段的操作类
 * @ClassName ColumnSearch
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:23:01
 */
public class ColumnSearch implements Serializable, Cloneable {

    /**
     * @Description
     * @field long serialVersionUID
     * @Created 2013 2013-8-5 下午04:23:09
     */
    private static final long serialVersionUID = 683089535134424185L;

    /** The exclude. */
    private boolean exclude = false;

    /** The type. */
    private int type;

    /** The ignore value. */
    private Object ignoreValue;

    /** The fetch. */
    private boolean fetch;

    /**
     * Gets the default instance.
     *
     * @param clazz
     *            the clazz
     * @return the default instance
     */
    public static ColumnSearch getDefaultInstance(Class<?> clazz) {
	ColumnSearch searchInfo = new ColumnSearch();
	searchInfo.setType(SearchType.EQ);
	searchInfo.setFetch(true);
	searchInfo.setExclude(false);

	String className = clazz.getName();
	setDefaultIgnoreValue(searchInfo, className);
	return searchInfo;
    }

    /**
     * Sets the ignore value.
     *
     * @param className
     *            the class name
     * @param value
     *            the value
     */
    public void setIgnoreValue(String className, String value) {
	if (value == null)
	    return;

	if (StringUtils.isBlank(value)) {
	    if ("java.lang.String".equals(className))
		this.ignoreValue = value;
	    else
		return;
	}

	value = value.trim();

	try {
	    if ("int".equals(className)
		    || "java.lang.Integer".equals(className)) {
		this.ignoreValue = Integer.parseInt(value);
	    } else if ("byte".equals(className)
		    || "java.lang.Byte".equals(className)) {
		this.ignoreValue = Byte.parseByte(value);
	    } else if ("short".equals(className)
		    || "java.lang.Short".equals(className)) {
		this.ignoreValue = Short.parseShort(value);
	    } else if ("long".equals(className)
		    || "java.lang.Long".equals(className)) {
		this.ignoreValue = Long.parseLong(value);
	    } else if ("float".equals(className)
		    || "java.lang.Float".equals(className)) {
		this.ignoreValue = Float.parseFloat(value);
	    } else if ("double".equals(className)
		    || "java.lang.Double".equals(className)) {
		this.ignoreValue = Double.parseDouble(value);
	    } else if ("char".equals(className)
		    || "java.lang.Character".equals(className)) {
		this.ignoreValue = value.toCharArray()[0];
	    } else if ("java.util.Date".equals(className)) {
		SimpleDateFormat df = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
		try {
		    this.ignoreValue = df.parse(value);
		} catch (ParseException e) {
		    df = new SimpleDateFormat("yyyy-MM-dd");
		    this.ignoreValue = df.parse(value);
		}
	    } else if ("java.sql.Timestamp".equals(className)) {
		SimpleDateFormat df = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
		try {
		    this.ignoreValue = new Timestamp(df.parse(value).getTime());
		} catch (ParseException e) {
		    df = new SimpleDateFormat("yyyy-MM-dd");
		    this.ignoreValue = new Timestamp(df.parse(value).getTime());
		}
	    }
	} catch (Exception e) {
	    setDefaultIgnoreValue(this, className);
	}
    }

    /**
     * Sets the default ignore value.
     *
     * @param searchInfo
     *            the search info
     * @param className
     *            the class name
     */
    private static void setDefaultIgnoreValue(ColumnSearch searchInfo,
	    String className) {
	if ("int".equals(className) || "byte".equals(className)
		|| "short".equals(className) || "long".equals(className)
		|| "float".equals(className) || "double".equals(className)
		|| "char".equals(className)) {
	    searchInfo.setIgnoreValue(0);
	} else {
	    searchInfo.setIgnoreValue(null);
	}
    }

    /**
     * Checks if is equal ignore value.
     *
     * @param value
     *            the value
     * @return true, if is equal ignore value
     */
    public boolean isEqualIgnoreValue(Object value) {
	if (value == null)
	    return true;
	String className = value.getClass().getName();

	if ("java.lang.Integer".equals(className)
		|| "java.lang.Byte".equals(className)
		|| "java.lang.Short".equals(className)
		|| "java.lang.Long".equals(className)
		|| "java.lang.Float".equals(className)
		|| "java.lang.Double".equals(className)
		|| "java.lang.Character".equals(className)) {
	    return value.equals(this.ignoreValue);
	} else if ("java.util.Date".equals(className)) {
	    Date d = (Date) value;
	    return d.compareTo((Date) ignoreValue) == 0;
	} else if ("java.sql.Timestamp".equals(className)) {
	    Timestamp t = (Timestamp) value;
	    return t.compareTo((Timestamp) ignoreValue) == 0;
	} else {
	    return false;
	}
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
	ColumnSearch cs = (ColumnSearch) super.clone();
	return cs;
    }

    /**
     * Checks if is fetch.
     *
     * @return true, if is fetch
     */
    public boolean isFetch() {
	return fetch;
    }

    /**
     * Sets the fetch.
     *
     * @param fetch
     *            the new fetch
     */
    public void setFetch(boolean fetch) {
	this.fetch = fetch;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public int getType() {
	return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType(int type) {
	this.type = type;
    }

    /**
     * Gets the ignore value.
     *
     * @return the ignore value
     */
    public Object getIgnoreValue() {
	return ignoreValue;
    }

    /**
     * Sets the ignore value.
     *
     * @param ignoreValue
     *            the new ignore value
     */
    public void setIgnoreValue(Object ignoreValue) {
	this.ignoreValue = ignoreValue;
    }

    /**
     * Checks if is exclude.
     *
     * @return true, if is exclude
     */
    public boolean isExclude() {
	return exclude;
    }

    /**
     * Sets the exclude.
     *
     * @param exclude
     *            the new exclude
     */
    public void setExclude(boolean exclude) {
	this.exclude = exclude;
    }

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
	Integer i = 0;
	Integer i1 = 0;
	System.out.println(i.equals(i1));
    }

}
