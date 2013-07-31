package com.mvc.constant;

public enum Status {

	STATUS_ALL(-1, "全部"),
	STATUS_INVALID(0, "无效"),
	STATUS_NEW(1, "待审核"),
	STATUS_VALID(2, "有效");

	private  int code;
	private  String lable;

	Status(int code, String lable) {
		this.code = code;
		this.lable = lable;
	}

	public Integer getCode() {
		return code;
	}

	public String getLable() {
		return lable;
	}

}
