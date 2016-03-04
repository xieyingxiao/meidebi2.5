package com.meidebi.app.service.bean.base;

import com.orm.dsl.Table;

@Table(name = "Chanel")
public class Chanel {
	private String cat_str;
	private String mPage;
	private String mChanel;
	private String d_id;
	private Boolean isAll;
	private long INSERTTIME = System.currentTimeMillis();
	public long getINSERTTIME() {
		return INSERTTIME;
	}

	public void setINSERTTIME(long iNSERTTIME) {
		INSERTTIME = iNSERTTIME;
	}
 	
	public Boolean getIsAll() {
		return isAll;
	}

	public void setIsAll(Boolean isAll) {
		this.isAll = isAll;
	}

	public String getmChanel() {
		return mChanel;
	}

	public void setmChanel(String mChanel) {
		this.mChanel = mChanel;
	}

	public String getCat_str() {
		return cat_str;
	}

	public void setCat_str(String cat_str) {
		this.cat_str = cat_str;
	}
	
	public String getmPage() {
		return mPage;
	}

	public void setmPage(String mPage) {
		this.mPage = mPage;
	}

	public String getD_id() {
		return d_id;
	}

	public void setD_id(String d_id) {
		this.d_id = d_id;
	}
	
	
}
