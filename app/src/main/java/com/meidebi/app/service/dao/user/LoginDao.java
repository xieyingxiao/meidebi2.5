package com.meidebi.app.service.dao.user;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.loopj.android.http.RequestParams;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.base.net.HttpMethod;
import com.meidebi.app.base.net.HttpUtility;
import com.meidebi.app.service.bean.user.LoginJson;
import com.meidebi.app.service.dao.BaseDao;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.RestHttpUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginDao extends BaseDao{
	private String username;
	private String password;
	public String getName() {
		return username;
	}
	public void setName(String username) {
		this.username = username;
	}
	public String getPw() {
		return password;
	}
	public void setPw(String password) {
		this.password = password;
	}
	
	public LoginJson mapperJson() {
		// TODO Auto-generated method stub
		LoginJson loginJson = new LoginJson();
		try {
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("username", username);
			param.put("password", password);
			param.put("my", "1");
			String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Post, HttpUrl.LOGIN_URL, param);
			AppLogger.e("result"+result);
			if(!TextUtils.isEmpty(result)){
			JSONObject jsonObject = new JSONObject(result);
			int status = jsonObject.getInt("status");
			if(status == 1||status == 2||status ==0){
				//String result = HttpUtils.postByHttpClient(mActivity, HttpUrl.LOGIN_URL,param);
 			loginJson = gson.fromJson(result, LoginJson.class);
 				if (loginJson == null) {
 				return null;
 				}
			}else{
				loginJson.setStatus(status);
			}
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

    public static void findPassWord(String email,RestHttpUtils.RestHttpHandler handler){
        RequestParams map = new RequestParams();
        map.put("email",email);
        postCommonResult(HttpUrl.LOGIN_FIND_PS_URL,map,handler, Object.class);
    }

}
