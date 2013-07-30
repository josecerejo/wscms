/**
 * Copyright (c ) 2013 wjz
 *
 * All rights reserved.
 *
 */
package com.mvc.basemvc.util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mvc.basemvc.persistence.db.ResultList;
import com.mvc.basemvc.persistence.util.DBUtils;

/**
 * @Description 分页bean
 * @ClassName ListPager
 * @author wjz_home@163.com
 * @Created 2013 2013-8-5 下午03:55:58
 */
public class ListPager implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 0xf7141555988635bL;

    /** The rows per page. */
    protected int rowsPerPage;

    /** The page no. */
    protected int pageNo;

    /** The total rows. */
    protected Long totalRows;

    /** The page data. */
    @SuppressWarnings("rawtypes")
    protected List pageData;

    /**
     * Instantiates a new list pager.
     */
    public ListPager() {
	rowsPerPage = 10;
	pageNo = 0;
	totalRows = null;
	pageData = null;
    }

    /**
     * Retrieve total rows.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @param refresh
     *            the refresh
     * @return the long
     */
    public long retrieveTotalRows(Connection conn, String sql, boolean refresh) {
	Statement st;
	ResultSet rs;
	if (totalRows != null) {
	}
	sql = buildRowCountSQL(sql);
	st = null;
	rs = null;
	try {
	    st = conn.createStatement();
	    rs = st.executeQuery(sql);
	    if (rs.next()) {
		totalRows = new Long(rs.getLong(1));
	    }
	} catch (Exception e) {
	    e.printStackTrace();

	} finally {
	    DBUtils.close(st, rs);
	}

	if (totalRows == null) {
	    totalRows = new Long(0L);
	}
	return totalRows.longValue();
    }

    /**
     * Retrieve paged rows.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @return the result list
     * @throws SQLException
     *             the sQL exception
     */
    public ResultList retrievePagedRows(Connection conn, String sql)
	    throws SQLException {
	sql = buildPagedSQL(sql);
	ResultList result = DBUtils.executeQuery(conn, sql);
	if (result != null) {
	    pageData = result.getRecords();
	}
	return result;
    }

    /**
     * Retrieve paged row list.
     *
     * @param conn
     *            the conn
     * @param sql
     *            the sql
     * @return the list
     * @throws SQLException
     *             the sQL exception
     */
    @SuppressWarnings("rawtypes")
    public List retrievePagedRowList(Connection conn, String sql)
	    throws SQLException {
	ResultList result = retrievePagedRows(conn, sql);
	return result.getRecords();
    }

    /**
     * Builds the row count sql.
     *
     * @param sql
     *            the sql
     * @return the string
     */
    @SuppressWarnings("unused")
    public static String buildRowCountSQL(String sql) {
	String orderby = null;
	String regex = "\\s(order\\s+by\\s+[\\w|\\d|\\.|\\-|_|\\s]+)$";
	Pattern p = Pattern.compile(regex, 2);
	Matcher m = p.matcher(sql);
	int start = 0;
	int end = 0;
	if (m.find()) {
	    orderby = m.group(1);
	    start = m.start(1);
	    end = m.end(1);
	}
	String newSQL = sql;
	if (orderby != null) {
	    newSQL = sql.substring(0, start);
	}
	StringBuffer buf = new StringBuffer(sql.length()
		+ "select count(*)  from (".length() + ") tmp ".length());
	buf.append("select count(*)  from (");
	buf.append(newSQL);
	buf.append(") tmp ");
	return buf.toString();
    }

    /**
     * Builds the paged sql.
     *
     * @param dbType
     *            the db type
     * @param sql
     *            the sql
     * @return the string
     * @throws SQLException
     *             the sQL exception
     */
    public String buildPagedSQL(int dbType, String sql) throws SQLException {
	if (dbType == 1) {
	    return buildPagedSQLOracle(sql);
	}
	if (dbType == 2) {
	    return buildPagedSQLSqlserver(sql);
	}
	if (dbType == 3) {
	    return buildPagedSQLMySql(sql);
	} else {
	    throw new SQLException("Not support dabase type:" + dbType);
	}
    }

    /**
     * Builds the paged sql oracle.
     *
     * @param sql
     *            the sql
     * @return the string
     */
    public String buildPagedSQLOracle(String sql) {
	StringBuffer sqlBuffer = new StringBuffer(500);
	sqlBuffer
		.append("select * from ( select row_.*, rownum rownum_ from ( ");
	sqlBuffer.append(sql);
	sqlBuffer.append(" ) row_ where rownum <= " + (pageNo + 1)
		* rowsPerPage + ")");
	sqlBuffer.append(" where rownum_ > " + pageNo * rowsPerPage);
	return sqlBuffer.toString();
    }

    /**
     * Builds the paged sql oracle.
     *
     * @param sql
     *            the sql
     * @param from
     *            the from
     * @param to
     *            the to
     * @return the string
     */
    public static String buildPagedSQLOracle(String sql, long from, long to) {
	StringBuffer sqlBuffer = new StringBuffer(500);
	sqlBuffer
		.append("select * from ( select row_.*, rownum rownum_ from ( ");
	sqlBuffer.append(sql);
	sqlBuffer.append(" ) row_ where rownum <= " + to + ")");
	sqlBuffer.append(" where rownum_ > " + from);
	return sqlBuffer.toString();
    }

    /**
     * Builds the paged sql sqlserver_2000.
     *
     * @param sql
     *            the sql
     * @return the string
     */
    public String buildPagedSQLSqlserver_2000(String sql) {
	StringBuffer sqlBuffer = new StringBuffer(500);
	sql = sql.trim().substring(6);
	sqlBuffer.append("select * from (").append("select top ")
		.append(rowsPerPage).append(" * from (").append("select top ")
		.append((pageNo + 1) * rowsPerPage).append(" ").append(sql)
		.append(") as b order by id desc").append(") as a order by id");
	return sqlBuffer.toString();
    }

    /**
     * Builds the paged sql sqlserver.
     *
     * @param sql
     *            the sql
     * @return the string
     */
    @SuppressWarnings("unused")
    public String buildPagedSQLSqlserver(String sql) {
	if (sql == null) {
	    return sql;
	}
	if (!sql.toLowerCase().startsWith("select")) {
	    throw new RuntimeException(
		    "Must start with 'select' clause for paged sql");
	}
	String orderby = null;
	String regex = "\\s(order\\s+by\\s+[\\w|\\d|\\.|\\-|_|\\s]+)$";
	Pattern p = Pattern.compile(regex, 2);
	Matcher m = p.matcher(sql);
	int start = -1;
	int end = -1;
	if (m.find()) {
	    orderby = m.group(1);
	    start = m.start(1);
	    end = m.end(1);
	}
	if (orderby == null) {
	    throw new RuntimeException(
		    "Must have 'order by' clause for paged sql");
	} else {
	    String newSQL = sql.substring(6, start);
	    newSQL = "select * from (select row_number() over(" + orderby
		    + ") as RowNum, " + newSQL + ") t where t.RowNum between "
		    + (pageNo * rowsPerPage + 1) + " and " + (pageNo + 1)
		    * rowsPerPage;
	    return newSQL;
	}
    }

    /**
     * Builds the paged sql my sql.
     *
     * @param sql
     *            the sql
     * @return the string
     */
    public String buildPagedSQLMySql(String sql) {
	if (sql == null) {
	    return sql;
	} else {
	    int startRow = pageNo + rowsPerPage;
	    int endRow = (pageNo + 1) * rowsPerPage;
	    String pagedSQL = sql + " limit " + startRow + "," + endRow;
	    return pagedSQL;
	}
    }

    /**
     * Builds the paged sql.
     *
     * @param oriNativeSql
     *            the ori native sql
     * @return the string
     */
    public String buildPagedSQL(String oriNativeSql) {

	try {
	    return buildPagedSQL(1, oriNativeSql);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    /**
     * Gets the total page.
     *
     * @return the total page
     */
    public int getTotalPage() {
	if (totalRows == null) {
	    return 0;
	} else {
	    long rows = totalRows.longValue();
	    return (int) Math.ceil((double) rows / (double) rowsPerPage);
	}
    }

    /**
     * Builds the pager bean.
     *
     * @param pageUrl
     *            the page url
     * @param showPageNum
     *            the show page num
     * @return the pager bean
     */
    public PagerBean<Page> buildPagerBean(String pageUrl, int showPageNum) {
	PagerBean<Page> tagBean = new PagerBean<Page>();
	if (getTotalPage() == 0) {
	    return tagBean;
	}
	tagBean.setCurrentPage(String.valueOf(getPageNo() + 1));
	tagBean.setTotalPage(String.valueOf(getTotalPage()));
	if (getPageNo() > 0) {
	    tagBean.setFirstPage(composePageURL(pageUrl, 0));
	    tagBean.setPrevPage(composePageURL(pageUrl, getPageNo() - 1));
	}
	if (getPageNo() < getTotalPage() - 1) {
	    tagBean.setLastPage(composePageURL(pageUrl, getTotalPage() - 1));
	    tagBean.setNextPage(composePageURL(pageUrl, getPageNo() + 1));
	}
	int firstPage = getPageNo() - showPageNum / 2;
	if (firstPage < 0) {
	    firstPage = 0;
	}
	int lastPage = (firstPage + showPageNum) - 1;
	if (lastPage >= getTotalPage()) {
	    lastPage = getTotalPage() - 1;
	}
	int k = (lastPage - showPageNum) + 1;
	if (k < 0) {
	    firstPage = 0;
	} else {
	    firstPage = k;
	}
	String pages[] = new String[(lastPage - firstPage) + 1];
	List<Page> pageList = new ArrayList<Page>((lastPage - firstPage) + 1);
	String url = null;
	Page page = null;
	for (int i = firstPage; i <= lastPage; i++) {
	    url = composePageURL(pageUrl, i);
	    page = new Page();
	    page.setPageNo(i);
	    page.setUrl(url);
	    page.setSelected(i == pageNo);
	    pageList.add(page);
	    if (url.startsWith("javascript:")) {
		pages[i - firstPage] = "<a href=\"#\" onclick=\"" + url + "\">";
	    } else {
		pages[i - firstPage] = "<a href=\"" + url + "\">";
	    }
	    if (i == getPageNo()) {
		pages[i - firstPage] += "<b>" + (i + 1) + "</b></a>";
	    } else {
		pages[i - firstPage] += (i + 1) + "</a>";
	    }
	}

	tagBean.setPages(pages);
	tagBean.setPageList(pageList);
	String inputName = generateInputName();
	tagBean.setInputName(inputName);
	tagBean.setGotoScript(composeGotoPageScript(pageUrl, inputName));
	return tagBean;
    }

    /**
     * Compose page url.
     *
     * @param url
     *            the url
     * @param pageNo
     *            the page no
     * @return the string
     */
    private String composePageURL(String url, int pageNo) {
	String pageURL = null;
	if (url.indexOf("{p}") >= 0) {
	    pageURL = url.replaceAll("\\{p\\}", String.valueOf(pageNo));
	} else if (url.indexOf("?") >= 0) {
	    pageURL = url + "&pageNo=" + pageNo;
	} else {
	    pageURL = url + "?pageNo=" + pageNo;
	}
	return pageURL;
    }

    /**
     * Compose goto page script.
     *
     * @param url
     *            the url
     * @param inputName
     *            the input name
     * @return the string
     */
    private String composeGotoPageScript(String url, String inputName) {
	StringBuffer sb;
	if (url.startsWith("javascript:")) {
	    url = url.substring(11, url.length());
	    if (url.indexOf("'{p}'") >= 0) {
		url = url.replaceAll("'\\{p\\}'", "p");
	    } else {
		url = url.replaceAll("\\{p\\}", "p");
	    }
	    sb = new StringBuffer();
	    sb.append("javascript:");
	    sb.append("var p=document.getElementById('" + inputName
		    + "').value;");
	    sb.append("if ( isNaN(p) || p<1 || p>" + getTotalPage()
		    + ") alert( '\u9519\u8BEF\u7684\u9875\u7801!' );");
	    sb.append("else {");
	    sb.append("p = parseInt(p);p = p-1;");
	    sb.append(url);
	    sb.append("}");
	    return sb.toString();
	}
	sb = new StringBuffer();
	sb.append("javascript:");
	sb.append("var p=document.getElementById('" + inputName + "').value;");
	sb.append("if ( isNaN(p) || p<1 || p>" + getTotalPage()
		+ ") alert( '\u9519\u8BEF\u7684\u9875\u7801!' );");
	sb.append("else {");
	sb.append("p = parseInt(p);p = p-1;");
	int pos = 0;
	if (url.indexOf("{p}") > 0) {
	    while (pos >= 0) {
		pos = url.indexOf("{p}");
		if (pos >= 0) {
		    if (pos < url.length() - 3) {
			url = url.replace("{p}", "'+p+'");
		    } else {
			url = url.replace("{p}", "'+p");
		    }
		}
	    }
	    sb.append("window.location='" + url);
	    if (!url.endsWith("'+p")) {
		sb.append("'");
	    }
	} else if (url.indexOf("?") > 0) {
	    sb.append("window.location='" + url + "' + '&pageNo='+p");
	} else {
	    sb.append("window.location='" + url + "' + '?pageNo='+p");
	}
	sb.append(";}");
	return sb.toString();
    }

    /**
     * Generate input name.
     *
     * @return the string
     */
    private String generateInputName() {
	int r = (int) (Math.random() * 10000D);
	return "gotoPageNo" + r;
    }

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String args[]) {
	ListPager pager = new ListPager();
	String s = pager.composeGotoPageScript(
		"javascript:gotoPage{'hell',{p}}", "goPage12");
	System.out.println(s);
    }

    /**
     * Builds the pager string.
     *
     * @param baseURL
     *            the base url
     * @param pageNum
     *            the page num
     * @return the string
     */
    public String buildPagerString(String baseURL, int pageNum) {
	return buildPagerString(baseURL, pageNum, true, true, true, true, true);
    }

    /**
     * Builds the pager string.
     *
     * @param baseURL
     *            the base url
     * @param pageNum
     *            the page num
     * @param showFirstPage
     *            the show first page
     * @param showPrevPage
     *            the show prev page
     * @param showPageList
     *            the show page list
     * @param showTotalPage
     *            the show total page
     * @param showCurrentPage
     *            the show current page
     * @return the string
     */
    public String buildPagerString(String baseURL, int pageNum,
	    boolean showFirstPage, boolean showPrevPage, boolean showPageList,
	    boolean showTotalPage, boolean showCurrentPage) {
	StringBuffer sb = new StringBuffer(1000);
	PagerBean<Page> bean = buildPagerBean(baseURL, pageNum);
	if (showFirstPage) {
	    if (bean.getFirstPage() == null) {
		sb.append("\u9996\u9875");
	    } else {
		sb.append("<a href=\"" + bean.getFirstPage()
			+ "\">\u9996\u9875</a>");
	    }
	}
	if (showPrevPage) {
	    if (bean.getPrevPage() == null) {
		sb.append("\u524D\u4E00\u9875");
	    } else {
		sb.append(" <a href=\"" + bean.getPrevPage()
			+ "\">\u524D\u4E00\u9875</a>");
	    }
	}
	if (showPageList && bean.getPages() != null) {
	    for (int i = 0; i < bean.getPages().length; i++) {
		sb.append(" " + bean.getPages()[i]);
	    }

	}
	if (showPrevPage) {
	    if (bean.getNextPage() == null) {
		sb.append("\u4E0B\u4E00\u9875");
	    } else {
		sb.append(" <a href=\"" + bean.getNextPage()
			+ "\">\u4E0B\u4E00\u9875</a>");
	    }
	}
	if (showFirstPage) {
	    if (bean.getLastPage() == null) {
		sb.append("\u672B\u9875");
	    } else {
		sb.append(" <a href=\"" + bean.getLastPage()
			+ "\">\u672B\u9875</a>");
	    }
	}
	if (showTotalPage) {
	    sb.append(" \u5171" + bean.getTotalPage() + "\u9875");
	}
	if (showCurrentPage) {
	    if (showTotalPage) {
		sb.append("\uFF0C\u5F53\u524D\u7B2C" + bean.getCurrentPage()
			+ "\u9875");
	    } else {
		sb.append(" \u5F53\u524D\u7B2C" + bean.getCurrentPage()
			+ "\u9875");
	    }
	}
	return sb.toString();
    }

    /**
     * Gets the first row.
     *
     * @return the first row
     */
    public long getFirstRow() {
	return (long) (rowsPerPage * pageNo);
    }

    /**
     * Gets the last row.
     *
     * @return the last row
     */
    public long getLastRow() {
	return Math.min((long) (rowsPerPage + 1) * (long) pageNo - 1L,
		getTotalRows());
    }

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
     * Gets the rows per page.
     *
     * @return the rows per page
     */
    public int getRowsPerPage() {
	return rowsPerPage;
    }

    /**
     * Sets the rows per page.
     *
     * @param rowsPerPage
     *            the new rows per page
     */
    public void setRowsPerPage(int rowsPerPage) {
	this.rowsPerPage = rowsPerPage;
    }

    /**
     * Gets the total rows.
     *
     * @return the total rows
     */
    public long getTotalRows() {
	return totalRows != null ? totalRows.longValue() : 0L;
    }

    /**
     * Sets the total rows.
     *
     * @param totalRows
     *            the new total rows
     */
    public void setTotalRows(long totalRows) {
	this.totalRows = new Long(totalRows);
    }

    /**
     * Gets the page data.
     *
     * @return the page data
     */
    @SuppressWarnings("rawtypes")
    public List getPageData() {
	return pageData;
    }

    /**
     * Sets the page data.
     *
     * @param pageData
     *            the new page data
     */
    @SuppressWarnings("rawtypes")
    public void setPageData(List pageData) {
	this.pageData = pageData;
    }
}
