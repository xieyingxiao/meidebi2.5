package com.meidebi.app.service.bean.base;


public class CommonJson<T> {
	private T data;
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
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
