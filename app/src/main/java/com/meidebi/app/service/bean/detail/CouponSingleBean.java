package com.meidebi.app.service.bean.detail;

import java.util.List;

public class CouponSingleBean   {
	private String siteid;
	private String sitename;
	private List<CouponBean> data;
	private String logo1;
	
	public String getLogo1() {
		return logo1;
	}
	public void setLogo1(String logo1) {
		this.logo1 = logo1;
	}
	public String getSiteid() {
		return siteid;
	}
	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}
	public String getSitename() {
		return sitename;
	}
	public void setSitename(String sitename) {
		this.sitename = sitename;
	}
	public List<CouponBean> getData() {
		return data;
	}
	public void setData(List<CouponBean> data) {
		this.data = data;
	}
	
}
