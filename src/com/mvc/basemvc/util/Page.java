/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.util;

/**
 * @Description 分页bean
 * @ClassName Page
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午03:55:20
 */
public class Page {

    /** The page no. */
    private int pageNo;

    /** The url. */
    private String url;

    /** The selected. */
    private boolean selected;

    /**
     * Gets the page no.
     *
     * @return the page no
     */
    public int getPageNo() {
	return pageNo;
    }

    /**
     * Sets the page no.
     *
     * @param pageNo
     *            the new page no
     */
    public void setPageNo(int pageNo) {
	this.pageNo = pageNo;
    }

    /**
     * Checks if is selected.
     *
     * @return true, if is selected
     */
    public boolean isSelected() {
	return selected;
    }

    /**
     * Sets the selected.
     *
     * @param selected
     *            the new selected
     */
    public void setSelected(boolean selected) {
	this.selected = selected;
    }

    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
	return url;
    }

    /**
     * Sets the url.
     *
     * @param url
     *            the new url
     */
    public void setUrl(String url) {
	this.url = url;
    }
}
