package com.meidebi.app.service.bean.msg;

import java.util.List;

public class SingleBean {
 
	private int count;
	private List<MsgDetailBean> linklist;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<MsgDetailBean> getLinklist() {
		return linklist;
	}
	public void setLinklist(List<MsgDetailBean> linklist) {
		this.linklist = linklist;
	}
	
	
}
