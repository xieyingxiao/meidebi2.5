package com.meidebi.app.service.dao;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.base.net.HttpMethod;
import com.meidebi.app.base.net.HttpUtility;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.user.postBean;
import com.meidebi.app.support.utils.Utility;
import com.meidebi.app.support.utils.component.LoginUtil;

import org.json.JSONObject;

import java.util.HashMap;

public class PostDao extends BaseDao {

	public CommonJson<postBean> post(String url, String description) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userkey", LoginUtil.getUid());
		map.put("url", url);
		map.put("description", description);
		map.put("devicetoken", Utility.getUUID());
        CommonJson<postBean> json = null;
		try {
			String result = HttpUtility.getInstance().executeNormalTask(
					HttpMethod.Post, HttpUrl.POST_NEWS, map);
			if (!TextUtils.isEmpty(result)) {
				JSONObject jsonObject = new JSONObject(result);
				int status = jsonObject.getInt("status");
				if (status == 0) {
					json = new CommonJson<postBean>();
					json.setData(null);
					json.setInfo(jsonObject.getString("info"));
					json.setStatus(status);		
					return json;
				} else {
					json = gson.fromJson(result, CommonJson.class);
					if (json == null) {
						return null;
					}
					return json;
				}
			}
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
