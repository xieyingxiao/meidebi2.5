package com.meidebi.app.service.dao.detail;

import android.app.Activity;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.base.net.HttpMethod;
import com.meidebi.app.base.net.HttpUtility;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.detail.MsgDetailJson;
import com.meidebi.app.service.bean.detail.OrderShowDetailJson;
import com.meidebi.app.service.dao.BaseDao;
import com.meidebi.app.support.utils.AppLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * 详情页(单品,活动,优惠券) 点赞 弱
 * 
 * @author mdb-ii
 * 
 */
public class MsgDetailDao extends BaseDao {
	private int type = 2;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public MsgDetailDao(Activity activity) {
		super(activity);
	}

	public MsgDetailJson getDetail(String id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
        map.put("type",String.valueOf(getType()));
		if (!TextUtils.isEmpty(XApplication.getInstance().getAccountBean()
				.getUserKey())) {
			map.put("userkey", XApplication.getInstance().getAccountBean()
					.getUserKey());
		}
		return MapperJson(HttpUrl.MSG_DETAIL_URL, map);
	}
	
	public OrderShowDetailJson getOrdewShowDetail(String id) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", id);
        map.put("type",String.valueOf(getType()));
		if (!TextUtils.isEmpty(XApplication.getInstance().getAccountBean()
				.getUserKey())) {
			map.put("userkey", XApplication.getInstance().getAccountBean()
					.getUserKey());
		}
		OrderShowDetailJson msgdetailjson;
		try {
			String result = HttpUtility.getInstance().executeNormalTask(
					HttpMethod.Get, HttpUrl.MSG_DETAIL_URL, map);
			AppLogger.e("result" + result);
			// String result = HttpUtils.postByHttpClient(mActivity,
			// HttpUrl.LOGIN_URL,param);
			msgdetailjson = gson.fromJson(result, OrderShowDetailJson.class);
			if (msgdetailjson == null) {
				return null;
			}
			return msgdetailjson;
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
	

	public MsgDetailJson MapperJson(String url, Map param) {
		// TODO Auto-generated method stub
		MsgDetailJson msgdetailjson;
		try {
			String result = HttpUtility.getInstance().executeNormalTask(
					HttpMethod.Get, url, param);
			AppLogger.e("result" + result);
			// String result = HttpUtils.postByHttpClient(mActivity,
			// HttpUrl.LOGIN_URL,param);
			msgdetailjson = gson.fromJson(result, MsgDetailJson.class);
			if (msgdetailjson == null) {
				return null;
			}
			return msgdetailjson;
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

	public CommonJson<Object> doVote(String id, String vote,String type) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("type", type);
		map.put("votes", vote);
		map.put("userid", XApplication.getInstance().getAccountBean().getUserKey());
		return getMapperJson(HttpUrl.MSG_DETAIL_VOTE_URL, map);
	}

	public CommonJson<Object> doFav(String id, String fetype) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("userkey", XApplication.getInstance().getAccountBean().getUserKey());
		map.put("fetype", fetype);
		return getMapperJson(HttpUrl.DETAIL_FAV, map);
	}

}
