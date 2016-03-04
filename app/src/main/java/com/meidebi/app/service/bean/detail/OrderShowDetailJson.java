package com.meidebi.app.service.bean.detail;

import com.meidebi.app.service.bean.msg.OrderShowBean;

public class OrderShowDetailJson {
	private OrderShowBean data;
	private String info;
	private int status;
	public OrderShowBean getData() {
		return data;
	}
	public void setData(OrderShowBean data) {
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
