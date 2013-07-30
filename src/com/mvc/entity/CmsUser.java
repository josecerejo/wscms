package com.mvc.entity;

import java.util.Date;
import com.mvc.basemvc.persistence.meta.Column;
import com.mvc.basemvc.persistence.meta.Entity;
import com.mvc.basemvc.persistence.meta.Id;
import com.mvc.basemvc.persistence.meta.Table;

/**
 * CmsUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_CMS_USER")
public class CmsUser implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7640033814192072212L;
	
	public static final String admin = "admin";

	/**
	 * Fields
	 */
	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "ADDDATE")
	private Date addDate;

	@Column(name = "AUTHORIZED_PERSON")
	private int authorizedPerson;

	@Column(name = "STATE")
	private String state;

	@Column(name = "OWN_MENU")
	private String ownMenu;

	@Column(name = "OWN_COLUMN")
	private String ownColumn;

	@Column(name = "LAST_LOGIN_TIME")
	private Date lastLoginTime;

	@Column(name = "LAST_LOGIN_IP")
	private String lastLoginIp;

	@Column(name = "REALNAME")
	private String realName;

	/** default constructor */
	public CmsUser() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public int getAuthorizedPerson() {
		return authorizedPerson;
	}

	public void setAuthorizedPerson(int authorizedPerson) {
		this.authorizedPerson = authorizedPerson;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOwnMenu() {
		return ownMenu;
	}

	public void setOwnMenu(String ownMenu) {
		this.ownMenu = ownMenu;
	}

	public String getOwnColumn() {
		return ownColumn;
	}

	public void setOwnColumn(String ownColumn) {
		this.ownColumn = ownColumn;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

}