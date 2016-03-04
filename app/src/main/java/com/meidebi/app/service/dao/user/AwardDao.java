package com.meidebi.app.service.dao.user;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;

import com.google.gson.JsonParseException;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.base.net.HttpMethod;
import com.meidebi.app.base.net.HttpUtility;
import com.meidebi.app.service.bean.user.AwardJson;
import com.meidebi.app.service.dao.BaseDao;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.component.LoginUtil;

public class AwardDao extends BaseDao{
	public AwardDao(Activity activity) {
		super(activity);
	}
	
	
	public AwardJson getAwardJson(){
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("userkey", LoginUtil.getUid());
		AwardJson loginJson = new AwardJson();
		try {
  			String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Post, HttpUrl.SHARE_AWARD, param);
			AppLogger.e("result"+result);
			JSONObject jsonObject = new JSONObject(result);
			int status = jsonObject.getInt("status");
			if(status == 1){
				//String result = HttpUtils.postByHttpClient(mActivity, HttpUrl.LOGIN_URL,param);
 			loginJson = gson.fromJson(result, AwardJson.class);
 				if (loginJson == null) {
 				return null;
 				}
			}else{
				loginJson.setStatus(status);
			}

 			return loginJson;
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
