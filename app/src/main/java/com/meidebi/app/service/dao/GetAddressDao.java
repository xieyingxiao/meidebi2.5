package com.meidebi.app.service.dao;

import com.google.gson.JsonParseException;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.base.net.HttpMethod;
import com.meidebi.app.base.net.HttpUtility;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.lbs.AddressBean;
import com.meidebi.app.service.bean.lbs.GeoJson;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.GsonUtils;

import java.util.HashMap;

public class GetAddressDao extends BaseDao{
	private int type = 0;
	
	
	
	
	public void setType(int type) {
		this.type = type;
	}


	public GeoJson coderAdd(String add) {
		// TODO Auto-generated method stub
		GeoJson msgdetailjson;
		HashMap<String ,String> map = new HashMap<String, String>();
		map.put("output", "json");
		map.put("address", add);

		try {
			String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Get,HttpUrl.BAIDU_GEOCODER_URL,map);
 			AppLogger.e("result"+result);
			//String result = HttpUtils.postByHttpClient(mActivity, HttpUrl.LOGIN_URL,param);
			msgdetailjson = gson.fromJson(result, GeoJson.class);
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

	public ListJson<AddressBean> MapperJson(String id) {
		// TODO Auto-generated method stub
        ListJson<AddressBean> msgdetailjson;
		HashMap<String ,String> map = new HashMap<String, String>();
		switch (type) {
		case 2:
			map.put("pid", id);
			break;
		case 3:
			map.put("cid", id);
			break;
		default:
			break;
		}
		try {
			String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Get,HttpUrl.PUSH_MANUAL_LOCATION_URL,map);
 			AppLogger.e("result"+result);
			//String result = HttpUtils.postByHttpClient(mActivity, HttpUrl.LOGIN_URL,param);
			msgdetailjson = GsonUtils.fromListJson(result,AddressBean.class);

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
}
