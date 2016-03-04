package com.meidebi.app.service.dao;

import android.app.Activity;

import com.google.gson.JsonParseException;
import com.loopj.android.http.RequestParams;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.base.net.HttpMethod;
import com.meidebi.app.base.net.HttpUtility;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.detail.CouponBean;
import com.meidebi.app.service.bean.detail.CouponData;
import com.meidebi.app.service.bean.detail.CouponListJson;
import com.meidebi.app.service.bean.detail.CouponSingleBean;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.RestHttpUtils;

import java.util.HashMap;

public class CouponDao extends BaseDao {
	private int type;
	private int limit = 10;
	
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public CouponDao(Activity activity) {
		super(activity);
	}

	public CouponListJson mapperJson() {
		// TODO Auto-generated method stub
		CouponListJson mainJson;
		HashMap<String, String> map = new HashMap<String, String>();
		// map.put("pagecount", "20");
		map.put("page", String.valueOf(page));
		map.put("limit", String.valueOf(limit));
		// String url = HttpUrl.getCatListUrl(cat_id,
		// isAll)+"-cats-"+product_type+"-p-"+page;
		try {
			String result = HttpUtility.getInstance().executeNormalTask(
					HttpMethod.Get, HttpUrl.COUPONLIST_URL, map);// 分类网址判断
			  AppLogger.e("Couponresult"+result);
			mainJson = gson.fromJson(result, CouponListJson.class);
			if (mainJson == null) {
				return null;
			}
			return mainJson;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void getDetail(String id,RestHttpUtils.RestHttpHandler<CommonJson> handler) {
        RequestParams params = new RequestParams();
        params.put("id",id);
         getCommonResult(HttpUrl.COUPON_DETAI_URL, params, handler, CouponData.class);
 	}


	
	public  static void doExchange(String id,RestHttpUtils.RestHttpHandler<CommonJson> handler) {
        RequestParams params  = new RequestParams();
        params.put("id", id);
        params.put("userkey", XApplication.getInstance().getAccountBean().getUserKey());
        getCommonResult(HttpUrl.COUPON_EXCHAGE_URL, params, handler, Object.class);

 	}


	public CommonJson<Object> doVote(String id, String type) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("type", 1 + "");
		map.put("votes", type);
		map.put("userid", XApplication.getInstance().getAccountBean().getUserKey());
		return getMapperJson(HttpUrl.MSG_DETAIL_VOTE_URL, map);
	}

	public CommonJson doFav(String id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("userkey", XApplication.getInstance().getAccountBean().getUserKey());
		map.put("fetype", "5");
		return getMapperJson(HttpUrl.DETAIL_FAV, map);
	}
	
	public ListJson<CouponBean> getMyCoupon(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userkey", XApplication.getInstance().getAccountBean().getUserKey());
		map.put("page", String.valueOf(page));
        ListJson<CouponBean> couponListJson;
		try {
			String result = HttpUtility.getInstance().executeNormalTask(
					HttpMethod.Get, HttpUrl.GET_MY_COUPON_URL, map);
			AppLogger.e("result" + result);
			// String result = HttpUtils.postByHttpClient(mActivity,
			// HttpUrl.LOGIN_URL,param);
			couponListJson = gson.fromJson(result, ListJson.class);
			if (couponListJson == null) {
				return null;
			}
			return couponListJson;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public CouponListJson getSites() {
		CouponListJson commonjson;
			try {
				String result = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpUrl.COUPONSITE_URL, null);
				commonjson = gson.fromJson(result, CouponListJson.class);
				if (commonjson == null) {
					return null;
				}
				return commonjson;
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;

	}

    public void getMyCoupon(boolean isTimeout ,RestHttpUtils.RestHttpHandler<ListJson> handler){
        RequestParams map = new RequestParams();
        map.put("userkey", XApplication.getInstance().getAccountBean().getUserKey());
        map.put("page", String.valueOf(page));
        map.put("limit", "20");
        map.put("istimeout", isTimeout?1:0);
        postListResult(HttpUrl.GET_MY_COUPON_URL, map, handler, CouponBean.class);
    }


    public static void getResult(RestHttpUtils.RestHttpHandler<ListJson> handler) {
        getListResult(HttpUrl.COUPONSITE_URL, null, handler, CouponSingleBean.class);
    }
}