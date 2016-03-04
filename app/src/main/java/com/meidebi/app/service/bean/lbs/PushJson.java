package com.meidebi.app.service.bean.lbs;


public class PushJson {
	private PushCatBean data;
	private String info;
	private int status;
	public PushCatBean getData() {
		return data;
	}
	public void setData(PushCatBean data) {
		this.data = data;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
