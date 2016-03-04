package com.meidebi.app.service.bean.detail;

import com.meidebi.app.service.bean.msg.MsgDetailBean;

public class MsgDetailJson {
	private MsgDetailBean data;
	private String info;
	private int status;
	public MsgDetailBean getData() {
		return data;
	}
	public void setData(MsgDetailBean data) {
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
