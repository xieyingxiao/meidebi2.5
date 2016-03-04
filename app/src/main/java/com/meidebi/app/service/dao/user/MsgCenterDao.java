package com.meidebi.app.service.dao.user;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.loopj.android.http.RequestParams;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.base.net.HttpMethod;
import com.meidebi.app.base.net.HttpUtility;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.user.MsgCenterBean;
import com.meidebi.app.service.dao.BaseDao;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.LoginUtil;

import java.util.HashMap;

public class MsgCenterDao extends BaseDao{

//	public ListJson<MsgCenterBean> mapperJson() {
//        ListJson<MsgCenterBean> commentjson;
//        HashMap<String,String> map =new HashMap<String, String>();
//        map.put("userkey", LoginUtil.getUid());
//        map.put("page", String.valueOf(page));
//
// 		try {
//			String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Post,HttpUrl.USER_MSGCENTER_URL, map);
//			//String result = HttpUtils.postByHttpClient(mActivity, HttpUrl.LOGIN_URL,param);
//			commentjson = gson.fromJson(result, ListJson.class);
//			if (commentjson == null) {
//				return null;
//			}
//			return commentjson;
//		} catch (JsonParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (XException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	public void setRead(String ids) {
		if(!TextUtils.isEmpty(ids)){
            RequestParams map =new RequestParams();
        map.put("userkey", LoginUtil.getUid());
        map.put("id", ids);
            AppLogger.e("ids"+ids);
            AppLogger.e("userkey"+LoginUtil.getUid());
         postCommonResult(HttpUrl.USER_READMSG_URL,map,null,Object.class);

 	}
	}


    public ListJson<MsgCenterBean> mapperJson() {
        ListJson<MsgCenterBean> commentjson;
        HashMap<String,String> map =new HashMap<String, String>();
        map.put("userkey", LoginUtil.getUid());
        map.put("page", String.valueOf(page));

        try {
            String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Post,HttpUrl.USER_MSGCENTER_URL, map);
            //String result = HttpUtils.postByHttpClient(mActivity, HttpUrl.LOGIN_URL,param);
            commentjson = gson.fromJson(result, ListJson.class);
            if (commentjson == null) {
                return null;
            }
            return commentjson;
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

    public void getResult(RestHttpUtils.RestHttpHandler<ListJson> handler){
        RequestParams map =new RequestParams();
        map.put("userkey", LoginUtil.getUid());
        map.put("p", String.valueOf(page));
        getListResult(HttpUrl.USER_MSGCENTER_URL,map,handler,MsgCenterBean.class);
    }


}
