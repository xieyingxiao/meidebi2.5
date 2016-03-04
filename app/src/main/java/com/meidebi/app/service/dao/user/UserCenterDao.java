package com.meidebi.app.service.dao.user;

import android.app.Activity;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.user.AwardBean;
import com.meidebi.app.service.bean.user.UserinfoBean;
import com.meidebi.app.service.dao.BaseDao;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;

import java.io.FileNotFoundException;

public class UserCenterDao extends BaseDao {
	public UserCenterDao(Activity activity) {
		super(activity);
	}

	public static void getUserInfo(RestHttpUtils.RestHttpHandler<CommonJson> handler) {
        RequestParams params = new RequestParams();
        params.put("userkey", LoginUtil.getUid());
        postCommonResult(HttpUrl.USER_CENTER_URL, params, handler, UserinfoBean.class);
	}

	public static void SignIn(RestHttpUtils.RestHttpHandler<CommonJson> handler) {
        RequestParams params = new RequestParams();
        params.put("userkey", LoginUtil.getUid());
        postCommonResult(HttpUrl.USER_SIGNIN,params,handler,AwardBean.class);
	}
	
	public static void uploadAvantar( TextHttpResponseHandler hanlder){
        RequestParams params = new RequestParams();
        params.put("userkey", LoginUtil.getUid());

        try {
            params.put("avatar",DiskCacheUtils.findInCache("avantar.jpg", ImageLoader.getInstance().getDiskCache()), "image/jpg", "avatar.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        RestHttpUtils.uploadImage(HttpUrl.USER_UPLOADAVATAR_URL,params,hanlder);
	}


}
