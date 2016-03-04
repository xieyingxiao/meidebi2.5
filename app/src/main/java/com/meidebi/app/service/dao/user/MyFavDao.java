package com.meidebi.app.service.dao.user;

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
import com.meidebi.app.service.bean.user.FavBean;
import com.meidebi.app.service.dao.BaseDao;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.LoginUtil;

import org.json.JSONObject;

import java.util.HashMap;

public class MyFavDao extends BaseDao {

	public MyFavDao(Activity activity) {
		super(activity);
	}

	public ListJson<FavBean> MapperJson(String type) {
		// TODO Auto-generated method stub
        ListJson<FavBean> msgdetailjson = null;
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("userkey", LoginUtil.getUid());
			map.put("ftype", type);
			map.put("page", String.valueOf(page));
			map.put("limit", "15");
			String result = HttpUtility.getInstance().executeNormalTask(
					HttpMethod.Get, HttpUrl.GET_MY_FAV, map);
			JSONObject jsonObject = new JSONObject(result);
			AppLogger.e("result" + result);
			// String result = HttpUtils.postByHttpClient(mActivity,
			// HttpUrl.LOGIN_URL,param);
			int status = jsonObject.getInt("status");
			if (status == 1) {
				msgdetailjson = gson.fromJson(result, ListJson.class);
				if (msgdetailjson == null) {
					return null;
				}
//			}
//            else if (!type.equals("2")) {
//				msgdetailjson = new ListJson<FavBean>();
//				msgdetailjson.setStatus(status);
//			}
//            else {
//				if (type.equals("4")) {
//					msgdetailjson = new ListJson<FavBean>;
//					msgdetailjson.setStatus(4);
//				}
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

	public CommonJson<Object> doFav(String id, String fetype) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("userkey", XApplication.getInstance().getAccountBean().getUserKey());
		map.put("fetype", fetype);
		return getMapperJson(HttpUrl.DETAIL_FAV, map);
	}

    public void getResult(String type,RestHttpUtils.RestHttpHandler httpHandler){
        RequestParams map = new RequestParams();
        map.put("userkey", LoginUtil.getUid());
        map.put("ftype", type);
        map.put("page", String.valueOf(page));
        map.put("limit", "15");
        getListResult(HttpUrl.GET_MY_FAV,map,httpHandler, FavBean.class);
     }
}
