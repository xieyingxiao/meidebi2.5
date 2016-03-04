package com.meidebi.app.service.init;

public class ChannelInitBean {
	private String name;
	private int imgres;
	private String param;
	private boolean isHot = true;

	public boolean isHot() {
		return isHot;
	}

	public void setHot(boolean isHot) {
		this.isHot = isHot;
	}
	
	public void changeHot() {
		this.isHot = !this.isHot;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getImgres() {
		return imgres;
	}

	public void setImgres(int imgres) {
		this.imgres = imgres;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

}
