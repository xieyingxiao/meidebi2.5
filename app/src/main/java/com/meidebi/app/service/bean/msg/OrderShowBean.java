package com.meidebi.app.service.bean.msg;

import com.meidebi.app.service.bean.base.BaseItemBean;
import com.meidebi.app.service.bean.base.MsgBaseBean;
import com.meidebi.app.service.bean.detail.InventoryBean;
import com.meidebi.app.service.bean.detail.RelateacBean;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.orm.dsl.Table;

import java.util.List;
@Table(name = "OrderShowBean")
public class OrderShowBean extends MsgBaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String activeprice;// （单品的活动价格，在单品中可能出现，出现后实际商品价格以这个为准）
	private String postage;// 邮费


	public String getPostage() {
		return postage;
	}

	public void setPostage(String postage) {
		this.postage = postage;
	}

	private String proprice;// 促销价格
	private List<RelateacBean> relateac;// 关联的活动（有时候有）
	private List<RelateacBean> relatevo;// 关联的优惠卷（有时候有）
	private int starttime;// 有效开始时间
	private int endtime;// 有效结束时间（有时候有）
	private List<String> whorsubsites;
	private String prodescription;// 推荐语
	// private String miane;//面额数组
	private Boolean quanchang;// 1、全场通用；2、不全场通用
	private Boolean limitnew;// 1、限制新用户；2、不现在新用户
	private int getstarttime;// 领卷开始时间
	private int getendtime;// 领卷结束时间
	private int dostarttime;// 使用开始时间
	private int doendtime;// 使用结束时间
	private String orginprice;
	private int isfav = 0;
	private String outurl;
	private String content;
	private List<InventoryBean> inventorylist;
	private BaseItemBean transitlist;
	private BaseItemBean site;
	private String photo;
	private String webViewCache;
	private String buylist;//数据库
	private String d_siteList;//数据库
	private String d_transList;//数据库
    private int unchecked;
    private int devicetype;
    private int isSending;
    private String draft_id;


    public String getDraft_id() {
        return draft_id;
    }


    public void setDraft_id(String draft_id) {
        this.draft_id = draft_id;
    }

    public void setIsSending(int isSending) {
        this.isSending = isSending;
    }


    public int getIsSending() {
        return isSending;
    }

    public int getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(int devicetype) {
        this.devicetype = devicetype;
    }

    public int getUnchecked() {
        return unchecked;
    }

    public void setUnchecked(int unchecked) {
        this.unchecked = unchecked;
    }

    public String getD_siteList() {
		return d_siteList;
	}

	public void setD_siteList(String d_siteList) {
		this.d_siteList = d_siteList;
	}

	public String getD_transList() {
		return d_transList;
	}

	public void setD_transList(String d_transList) {
		this.d_transList = d_transList;
	}

	public String getBuylist() {
		return buylist;
	}

	public void setBuylist(String buylist) {
		this.buylist = buylist;
	}

	public String getWebViewCache() {
		return webViewCache;
	}

	public void setWebViewCache(String webViewCache) {
		this.webViewCache = webViewCache;
	}

	
	
	public String getPhoto() {
		if(SharePrefUtility.getEnablePic()){
			return photo;
		}
		return null;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public BaseItemBean getTransitlist() {
		return transitlist;
	}

	public void setTransitlist(BaseItemBean transitlist) {
		this.transitlist = transitlist;
	}

	public BaseItemBean getSite() {
		return site;
	}

	public void setSite(BaseItemBean site) {
		this.site = site;
	}

	public List<InventoryBean> getInventorylist() {
		return inventorylist;
	}

	public void setInventorylist(List<InventoryBean> inventorylist) {
		this.inventorylist = inventorylist;
	}

	public String getActiveprice() {
		return activeprice;
	}

	public void setActiveprice(String activeprice) {
		this.activeprice = activeprice;
	}

	public String getProprice() {
		return proprice;
	}

	public void setProprice(String proprice) {
		this.proprice = proprice;
	}

	public List<RelateacBean> getRelateac() {
		return relateac;
	}

	public void setRelateac(List<RelateacBean> relateac) {
		this.relateac = relateac;
	}

	public List<RelateacBean> getRelatevo() {
		return relatevo;
	}

	public void setRelatevo(List<RelateacBean> relatevo) {
		this.relatevo = relatevo;
	}

	public int getStarttime() {
		return starttime;
	}

	public void setStarttime(int starttime) {
		this.starttime = starttime;
	}

	public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}

	public List<String> getWhorsubsites() {
		return whorsubsites;
	}

	public void setWhorsubsites(List<String> whorsubsites) {
		this.whorsubsites = whorsubsites;
	}

	public String getProdescription() {
		return prodescription;
	}

	public void setProdescription(String prodescription) {
		this.prodescription = prodescription;
	}

	public Boolean getQuanchang() {
		return quanchang;
	}

	public void setQuanchang(Boolean quanchang) {
		this.quanchang = quanchang;
	}

	public Boolean getLimitnew() {
		return limitnew;
	}

	public void setLimitnew(Boolean limitnew) {
		this.limitnew = limitnew;
	}

	public int getGetstarttime() {
		return getstarttime;
	}

	public void setGetstarttime(int getstarttime) {
		this.getstarttime = getstarttime;
	}

	public int getGetendtime() {
		return getendtime;
	}

	public void setGetendtime(int getendtime) {
		this.getendtime = getendtime;
	}

	public int getDostarttime() {
		return dostarttime;
	}

	public void setDostarttime(int dostarttime) {
		this.dostarttime = dostarttime;
	}

	public int getDoendtime() {
		return doendtime;
	}

	public void setDoendtime(int doendtime) {
		this.doendtime = doendtime;
	}

	public String getOrginprice() {
		return orginprice;
	}

	public void setOrginprice(String orginprice) {
		this.orginprice = orginprice;
	}

	public int getIsfav() {
		return isfav;
	}

	public void setIsfav(int isfav) {
		this.isfav = isfav;
	}

	public String getOuturl() {
		return outurl;
	}

	public void setOuturl(String outurl) {
		this.outurl = outurl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
