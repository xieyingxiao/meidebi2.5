package com.meidebi.app.service.dao.user;

import android.app.Activity;

import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.dao.BaseDao;

import java.util.HashMap;

public class RegDao extends BaseDao{
	public RegDao(Activity activity){
		super(activity);
	}
	public CommonJson<String> doReg( String username,String password,String email){
		HashMap<String, String> params=new HashMap<String, String>();
		params.put("username", username);
		params.put("password", password);
		params.put("email", email);
		return postMapperJson(HttpUrl.REGIST_URL, params);
	}
	
}
