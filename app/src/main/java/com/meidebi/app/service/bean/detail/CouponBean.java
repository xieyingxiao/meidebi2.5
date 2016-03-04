package com.meidebi.app.service.bean.detail;

import com.meidebi.app.service.bean.base.MsgBaseBean;

public class CouponBean extends MsgBaseBean {
	private String man;
	private String jian;
	private String copper;
	private int getend;
	private int usestart;
	private int useend;
	private boolean isbindaccount;
	private boolean limitnewuser;
	private boolean allgoods;// 全场
	// private int zhuanchang;// 专场
	private boolean daixiadan;
	private int couponcount;
	private int saledcount;
	private String tags;
	private String jubao;
	private int buylevel;
	private boolean isFav;
	private String card;
	private String pass;
	private String siteurl;
	private String host;
	
	
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSiteurl() {
		return siteurl;
	}

	public void setSiteurl(String siteurl) {
		this.siteurl = siteurl;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getMan() {
		return man;
	}

	public void setMan(String man) {
		this.man = man;
	}

	public String getJian() {
		return jian;
	}

	public void setJian(String jian) {
		this.jian = jian;
	}

	public String getCopper() {
		return copper;
	}

	public void setCopper(String copper) {
		this.copper = copper;
	}

	public int getGetend() {
		return getend;
	}

	public void setGetend(int getend) {
		this.getend = getend;
	}

	public int getUsestart() {
		return usestart;
	}

	public void setUsestart(int usestart) {
		this.usestart = usestart;
	}

	public int getUseend() {
		return useend;
	}

	public void setUseend(int useend) {
		this.useend = useend;
	}

	public Boolean getIsbindaccount() {
		return isbindaccount;
	}

	public void setIsbindaccount(Boolean isbindaccount) {
		this.isbindaccount = isbindaccount;
	}

	public boolean getLimitnewuser() {
		return limitnewuser;
	}

	public void setLimitnewuser(boolean limitnewuser) {
		this.limitnewuser = limitnewuser;
	}

	public boolean getAllgoods() {
		return allgoods;
	}

	public void setAllgoods(boolean allgoods) {
		this.allgoods = allgoods;
	}

	// public int getZhuanchang() {
	// return zhuanchang;
	// }
	// public void setZhuanchang(int zhuanchang) {
	// this.zhuanchang = zhuanchang;
	// }
	public Boolean getDaixiadan() {
		return daixiadan;
	}

	public void setDaixiadan(Boolean daixiadan) {
		this.daixiadan = daixiadan;
	}

	public int getCouponcount() {
		return couponcount;
	}

	public void setCouponcount(int couponcount) {
		this.couponcount = couponcount;
	}

	public int getSaledcount() {
		return saledcount;
	}

	public void setSaledcount(int saledcount) {
		this.saledcount = saledcount;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getJubao() {
		return jubao;
	}

	public void setJubao(String jubao) {
		this.jubao = jubao;
	}

	public int getBuylevel() {
		return buylevel;
	}

	public void setBuylevel(int buylevel) {
		this.buylevel = buylevel;
	}


	public boolean isFav() {
		return isFav;
	}

	public void setFav(boolean isFav) {
		this.isFav = isFav;
	}

}
