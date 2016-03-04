package com.meidebi.app.service.bean.base;


public class LinkListJson<T> {
	private LinkListBean<T> data;
	private String info;
	private int status;
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
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
	public LinkListBean<T> getData() {
		return data;
	}

	public void setData(LinkListBean<T> data) {
		this.data = data;
	}
	
}
