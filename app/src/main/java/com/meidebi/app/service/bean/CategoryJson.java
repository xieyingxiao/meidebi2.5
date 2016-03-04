package com.meidebi.app.service.bean;

import java.util.List;

import com.meidebi.app.service.bean.base.BaseItemBean;

public class CategoryJson {
 private List<BaseItemBean> data;

public List<BaseItemBean> getData() {
	return data;
}

public void setData(List<BaseItemBean> data) {
	this.data = data;
}
}
