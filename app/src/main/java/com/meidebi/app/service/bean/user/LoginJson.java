package com.meidebi.app.service.bean.user;


public class LoginJson {
	private String data;
	private int status;
	private String info;
	private UserinfoBean user;
	public UserinfoBean getUser() {
		return user;
	}
	public void setUser(UserinfoBean user) {
		this.user = user;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
}
