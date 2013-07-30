/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.persistence.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description
 * @ClassName ResultList
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午04:28:54
 */
public class ResultList {

    /** The records. */
    private List<LinkedHashMap<String, Object>> records;

    /**
     * Instantiates a new result list.
     */
    public ResultList() {

	records = new ArrayList<LinkedHashMap<String, Object>>();
    }

    /**
     * Gets the cell.
     *
     * @param row
     *            the row
     * @param colName
     *            the col name
     * @return the cell
     */
    public Object getCell(int row, String colName) {
	if (row < 0) {
	    return null;
	}
	if (colName == null || (colName = colName.trim()).length() == 0) {
	    return null;
	}
	Map<String, Object> record = getRecord(row);
	if (record == null) {
	    return null;
	} else {
	    return record.get(colName);
	}
    }

    /**
     * Gets the cell.
     *
     * @param row
     *            the row
     * @param column
     *            the column
     * @return the cell
     */
    public Object getCell(int row, int column) {
	if (row < 0 || column < 0) {
	    return null;
	}
	Map<String, Object> record = getRecord(row);
	if (record == null) {
	    return null;
	}
	if (column >= record.size()) {
	    return null;
	}
	int i = 0;
	for (Iterator<Object> iter = record.values().iterator(); iter.hasNext(); iter
		.next()) {
	    if (i == column) {
		return iter.next();
	    }
	    i++;
	}

	return null;
    }

    /**
     * Gets the record.
     *
     * @param row
     *            the row
     * @return the record
     */
    public LinkedHashMap<String, Object> getRecord(int row) {
	if (row < 0 || row >= size()) {
	    return null;
	} else {
	    return (LinkedHashMap<String, Object>) records.get(row);
	}
    }

    /**
     * Adds the record.
     *
     * @param record
     *            the record
     */
    public void addRecord(LinkedHashMap<String, Object> record) {
	records.add(record);
    }

    /**
     * Adds the record.
     *
     * @param index
     *            the index
     * @param record
     *            the record
     */
    public void addRecord(int index, LinkedHashMap<String, Object> record) {
	records.add(index, record);
    }

    /**
     * Size.
     *
     * @return the int
     */
    public int size() {
	return records.size();
    }

    /**
     * Checks if is empty.
     *
     * @return true, if is empty
     */
    public boolean isEmpty() {
	return records.isEmpty();
    }

    /**
     * Gets the records.
     *
     * @return the records
     */
    public List<LinkedHashMap<String, Object>> getRecords() {
	return records;
    }
}
