package com.meidebi.app.service.bean.msg;

import java.util.List;

import com.meidebi.app.service.bean.base.BaseItemBean;
import com.meidebi.app.service.bean.base.MsgBaseBean;
import com.meidebi.app.service.bean.detail.InventoryBean;
import com.meidebi.app.service.bean.detail.RelateacBean;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.orm.dsl.Table;

@Table(name = "MsgDetailBean")
public class MsgDetailBean extends MsgBaseBean {
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
		if(postage=="0"){
			this.postage = "";
		}else{
		this.postage = postage;
		}
	}

	private String proprice;// 促销价格
	private List<RelateacBean> relateac;// 关联的活动（有时候有）
	private List<RelateacBean> relatevo;// 关联的优惠卷（有时候有）
	private int starttime;// 有效开始时间
	private int endtime;// 有效结束时间（有时候有）
	private List<String> whorsubsites;
	private String prodescription;// 推荐语
	// private String miane;//面额数组
	private int quanchang;// 1、全场通用；2、不全场通用
	private int limitnew;// 1、限制新用户；2、不现在新用户
	private int getstarttime;// 领卷开始时间
	private int getendtime;// 领卷结束时间
	private int dostarttime;// 使用开始时间
	private int doendtime;// 使用结束时间
	private String orginprice;
	private int isfav;
	private String outurl;
	private String content;
	private List<InventoryBean> inventorylist;
	private BaseItemBean transitlist;
	private BaseItemBean site;
	private String photo;
	private String webViewCache;
 	private String mContryName;
    private String freight;
     private transitcompany transitcompany;
    private String directtariff;
    public void setFreight(String freight) {
        this.freight = freight;
    }

    public void setTransitcompany(transitcompany transitcompany) {
        this.transitcompany = transitcompany;
    }

    public String getDirecttariff() {
        return directtariff;
    }

    public void setDirecttariff(String directtariff) {
        this.directtariff = directtariff;
    }

    public transitcompany getTransitcompany() {
        return transitcompany;
    }

    public String getFreight() {
        return freight;
    }
//
    public String getmContryName() {
		return mContryName;
	}

	public void setmContryName(String mContryName) {
		this.mContryName = mContryName;
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

	public int getQuanchang() {
		return quanchang;
	}

	public void setQuanchang(int quanchang) {
		this.quanchang = quanchang;
	}

	public int getLimitnew() {
		return limitnew;
	}

	public void setLimitnew(int limitnew) {
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
