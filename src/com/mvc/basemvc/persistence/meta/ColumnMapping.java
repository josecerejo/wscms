/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.meta;

import java.io.Serializable;

/**
 * @Description 数据库字段的Mapping
 * @ClassName ColumnMapping
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:24:02
 */
public class ColumnMapping implements Serializable, Cloneable {

    /**
     * @Description
     * @field long serialVersionUID
     * @Created 2013 2013-8-5 下午04:23:52
     */
    private static final long serialVersionUID = 4302636365754111922L;

    /** The column name. */
    private String columnName;

    /** The alias. */
    private String alias;

    /** The lob type. */
    private int lobType = LobType.NO;

    /** The lob lazy. */
    private boolean lobLazy = true;

    /** The anti xss. */
    private boolean antiXss = false;

    /** The search info. */
    private ColumnSearch searchInfo = null;

    /**
     * (non-Javadoc).
     *
     * @return the object
     * @throws CloneNotSupportedException
     *             the clone not supported exception
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException {
	ColumnMapping cm = (ColumnMapping) super.clone();
	return cm;
    }

    /**
     * Gets the column name.
     *
     * @return the column name
     */
    public String getColumnName() {
	return columnName;
    }

    /**
     * Sets the column name.
     *
     * @param columnName
     *            the new column name
     */
    public void setColumnName(String columnName) {
	if (columnName != null)
	    columnName = columnName.toUpperCase();
	this.columnName = columnName;
    }

    /**
     * Gets the lob type.
     *
     * @return the lob type
     */
    public int getLobType() {
	return lobType;
    }

    /**
     * Sets the lob type.
     *
     * @param lobType
     *            the new lob type
     */
    public void setLobType(int lobType) {
	this.lobType = lobType;
    }

    /**
     * Gets the search info.
     *
     * @return the search info
     */
    public ColumnSearch getSearchInfo() {
	return searchInfo;
    }

    /**
     * Sets the search info.
     *
     * @param searchInfo
     *            the new search info
     */
    public void setSearchInfo(ColumnSearch searchInfo) {
	this.searchInfo = searchInfo;
    }

    /**
     * Checks if is lob lazy.
     *
     * @return true, if is lob lazy
     */
    public boolean isLobLazy() {
	return lobLazy;
    }

    /**
     * Sets the lob lazy.
     *
     * @param lobLazy
     *            the new lob lazy
     */
    public void setLobLazy(boolean lobLazy) {
	this.lobLazy = lobLazy;
    }

    /**
     * Checks if is anti xss.
     *
     * @return true, if is anti xss
     */
    public boolean isAntiXss() {
	return antiXss;
    }

    /**
     * Sets the anti xss.
     *
     * @param antiXss
     *            the new anti xss
     */
    public void setAntiXss(boolean antiXss) {
	this.antiXss = antiXss;
    }

    /**
     * Gets the alias.
     *
     * @return the alias
     */
    public String getAlias() {
	return alias;
    }

    /**
     * Sets the alias.
     *
     * @param alias
     *            the new alias
     */
    public void setAlias(String alias) {
	this.alias = alias;
    }

}
