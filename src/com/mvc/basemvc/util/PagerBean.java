/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 分页bean
 * @ClassName PagerBean
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午03:54:23
 */
public class PagerBean<T extends Page> {

    /** The first page. */
    private String firstPage;

    /** The last page. */
    private String lastPage;

    /** The prev page. */
    private String prevPage;

    /** The next page. */
    private String nextPage;

    /** The pages. */
    private String pages[];

    /** The page list. */
    private List<T> pageList;

    /** The total page. */
    private String totalPage;

    /** The current page. */
    private String currentPage;

    /** The goto script. */
    private String gotoScript;

    /** The input name. */
    private String inputName;

    /**
     * Instantiates a new pager bean.
     */
    public PagerBean() {
	firstPage = null;
	lastPage = null;
	prevPage = null;
	nextPage = null;
	pages = new String[0];
	pageList = new ArrayList<T>();
	totalPage = "0";
	currentPage = "0";
	gotoScript = null;
	inputName = null;
    }

    /**
     * Gets the current page.
     *
     * @return the current page
     */
    public String getCurrentPage() {
	return currentPage;
    }

    /**
     * Sets the current page.
     *
     * @param currentPage
     *            the new current page
     */
    public void setCurrentPage(String currentPage) {
	this.currentPage = currentPage;
    }

    /**
     * Gets the first page.
     *
     * @return the first page
     */
    public String getFirstPage() {
	return firstPage;
    }

    /**
     * Sets the first page.
     *
     * @param firstPage
     *            the new first page
     */
    public void setFirstPage(String firstPage) {
	this.firstPage = firstPage;
    }

    /**
     * Gets the last page.
     *
     * @return the last page
     */
    public String getLastPage() {
	return lastPage;
    }

    /**
     * Sets the last page.
     *
     * @param lastPage
     *            the new last page
     */
    public void setLastPage(String lastPage) {
	this.lastPage = lastPage;
    }

    /**
     * Gets the next page.
     *
     * @return the next page
     */
    public String getNextPage() {
	return nextPage;
    }

    /**
     * Sets the next page.
     *
     * @param nextPage
     *            the new next page
     */
    public void setNextPage(String nextPage) {
	this.nextPage = nextPage;
    }

    /**
     * Gets the pages.
     *
     * @return the pages
     */
    public String[] getPages() {
	return pages;
    }

    /**
     * Sets the pages.
     *
     * @param pages
     *            the new pages
     */
    public void setPages(String pages[]) {
	this.pages = pages;
    }

    /**
     * Gets the prev page.
     *
     * @return the prev page
     */
    public String getPrevPage() {
	return prevPage;
    }

    /**
     * Sets the prev page.
     *
     * @param prevPage
     *            the new prev page
     */
    public void setPrevPage(String prevPage) {
	this.prevPage = prevPage;
    }

    /**
     * Gets the total page.
     *
     * @return the total page
     */
    public String getTotalPage() {
	return totalPage;
    }

    /**
     * Sets the total page.
     *
     * @param totalPage
     *            the new total page
     */
    public void setTotalPage(String totalPage) {
	this.totalPage = totalPage;
    }

    /**
     * Gets the page list.
     *
     * @return the page list
     */
    public List<T> getPageList() {
	return pageList;
    }

    /**
     * Sets the page list.
     *
     * @param pageList
     *            the new page list
     */
    public void setPageList(List<T> pageList) {
	this.pageList = pageList;
    }

    /**
     * Gets the goto script.
     *
     * @return the goto script
     */
    public String getGotoScript() {
	return gotoScript;
    }

    /**
     * Sets the goto script.
     *
     * @param gotoScript
     *            the new goto script
     */
    public void setGotoScript(String gotoScript) {
	this.gotoScript = gotoScript;
    }

    /**
     * Gets the input name.
     *
     * @return the input name
     */
    public String getInputName() {
	return inputName;
    }

    /**
     * Sets the input name.
     *
     * @param inputName
     *            the new input name
     */
    public void setInputName(String inputName) {
	this.inputName = inputName;
    }
}
