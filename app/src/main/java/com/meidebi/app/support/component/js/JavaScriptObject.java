package com.meidebi.app.support.component.js;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;

import com.meidebi.app.service.dao.BaseDao;
import com.meidebi.app.support.utils.component.LoginUtil;

public class JavaScriptObject {
    private Context mContxt;
    private BaseDao baseDao;
    
    
    public BaseDao getBaseDao() {
    	if(baseDao==null){
    		baseDao = new BaseDao();
    	}
		return baseDao;
	}
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	public JavaScriptObject(Context mContxt) {
        this.mContxt = mContxt;
    }
    @JavascriptInterface
    public void runFromAndroid1(String url) {
    	if(LoginUtil.isAccountLogin((Activity)mContxt)){
//    		doInferface(url);
    	}
    }
    @JavascriptInterface
    public void runFromAndroid2(String name) {
    }
    
    @JavascriptInterface
    public void doInferface(final String url,final String key,final String value) {
		// showLoading();
		new Thread() {
			@Override
			public void run() {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(key, value);
				map.put("userkey", LoginUtil.getUid());
				getBaseDao().postMapperJson(url, map);
			}
		}.start();
	}


}