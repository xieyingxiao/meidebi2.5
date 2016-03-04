package com.meidebi.app.service.bean.user;

public class AwardJson {
	private AwardBean data;
	private int status;
	private String info;
	private String content;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public AwardBean getData() {
		return data;
	}
	public void setData(AwardBean data) {
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
