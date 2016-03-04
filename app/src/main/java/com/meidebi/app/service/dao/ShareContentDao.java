package com.meidebi.app.service.dao;

import android.app.Activity;

import com.google.gson.JsonParseException;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.base.net.HttpMethod;
import com.meidebi.app.base.net.HttpUtility;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.detail.ShareContentJson;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.component.LoginUtil;

import java.util.HashMap;

public class ShareContentDao extends BaseDao {
 public ShareContentDao(Activity activity){
	 super(activity);
 }
 
 public ShareContentJson getShareContent(String id){
	  HashMap<String,String> map =new HashMap<String, String>();
      map.put("id", id);
      ShareContentJson json = null;
      try {
			String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Get,HttpUrl.GET_SHAREWORDS_URL, map);
			//String result = HttpUtils.postByHttpClient(mActivity, HttpUrl.LOGIN_URL,param);
//			AppLogger.e(result);
			json = gson.fromJson(result, ShareContentJson.class);
			if (json == null) {
				return null;
			}
			return json;
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
 
 public CommonJson<Object> getUniShareUrl(String url){
	  HashMap<String,String> map =new HashMap<String, String>();
      map.put("url", url);
      map.put("userkey", LoginUtil.getUid());
      CommonJson<Object> json = null;
     try {
			String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Get,HttpUrl.GET_UNIONSHAREWORDS_URL, map);
			//String result = HttpUtils.postByHttpClient(mActivity, HttpUrl.LOGIN_URL,param);
			AppLogger.e(result);
			json = gson.fromJson(result, CommonJson.class);
			if (json == null) {
				return null;
			}
			return json;
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
