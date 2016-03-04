package com.meidebi.app.service.bean.user;

import java.io.Serializable;

import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;

public class FavBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String strUp;
	private String strDown;
	private String strImgUrl;
	private String iZanNum;
	private String title;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	private int state;//state 0 未过期 1已过期

	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStrUp() {
		return strUp;
	}
	public void setStrUp(String strUp) {
		this.strUp = strUp;
	}
	public String getStrDown() {
		return strDown;
	}
	public void setStrDown(String strDown) {
		this.strDown = strDown;
	}
	public String getStrImgUrl() {
		if(SharePrefUtility.getEnablePic()){
			return strImgUrl;
		}
		return null;
	}
	public void setStrImgUrl(String strImgUrl) {
		this.strImgUrl = strImgUrl;
	}
	public String getiZanNum() {
		return iZanNum;
	}
	public void setiZanNum(String iZanNum) {
		this.iZanNum = iZanNum;
	}
	
}
