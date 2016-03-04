package com.meidebi.app.service.dao;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.loopj.android.http.RequestParams;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.base.net.HttpMethod;
import com.meidebi.app.base.net.HttpUtility;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.lbs.PushCatBean;
import com.meidebi.app.service.bean.lbs.PushHelpBean;
import com.meidebi.app.service.bean.lbs.PushJson;
import com.meidebi.app.support.component.jpush.PushUtity;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.Utility;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.support.utils.shareprefelper.SharePrefHelper;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.setting.SettingActivity;
import com.umeng.message.PushAgent;

import java.util.HashMap;

/**
 * 推送dao
 *
 * @author mdb-ii
 */
public class PushDao extends BaseDao {
    private String userid;
    private String channelid;

    public PushDao() {
        PushUtity.getIds();
    }

    public String getUserid() {
        if (TextUtils.isEmpty(userid))
            userid = PushUtity.clientId;
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getChannelid() {
        if (TextUtils.isEmpty(channelid))
            channelid = PushUtity.channelId;
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public CommonJson<Object> initPush(String userid, String channelid, String devicetoken) {
        setUserid(userid);
        setChannelid(channelid);
        HashMap<String, String> map = new HashMap<String, String>();
//		map.put("userid", getUserid());
//		map.put("channelid", getChannelid());
        map.put("devicetype", "2");
        map.put("umengtoken", SharePrefUtility.getUMENGID());
        return postMapperJson(HttpUrl.PUSH_INIT_URL, map);
    }

    public CommonJson<Object> submitLocation(String longitude, String latitude) {
        HashMap<String, String> map = new HashMap<String, String>();
//		map.put("userid", getUserid());
//		map.put("channelid", getChannelid());
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        map.put("umengtoken", SharePrefUtility.getUMENGID());
        map.put("devicetype", "2");
        return postMapperJson(HttpUrl.PUSH_UMENG_BINDADRESS, map);
    }

    public CommonJson<Object> submitSetting(String localstock, String cates) {
        HashMap<String, String> map = new HashMap<String, String>();
//		map.put("userid", getUserid());
//		map.put("channelid", getChannelid());
        map.put("userkey", LoginUtil.getUid());
        map.put("localstock", localstock);
        map.put("cates", cates);
        map.put("devicetype", "2");
        map.put("devicetoken", Utility.getImeiCode());
        map.put("issound", SharePrefHelper.getSharedPreferencesBoolean(XApplication.getInstance(), SettingActivity.PUSH_SOUND, false) + "");
        map.put("isvibrate", SharePrefHelper.getSharedPreferencesBoolean(XApplication.getInstance(), SettingActivity.PUSH_VIBRATE, true) + "");
        map.put("isnear", SharePrefHelper.getSharedPreferencesBoolean(XApplication.getInstance(), SettingActivity.PUSH_RECENT_PRICE, false) + "");
        map.put("islowest", SharePrefHelper.getSharedPreferencesBoolean(XApplication.getInstance(), SettingActivity.PUSH_HISTORY_PRICE, false) + "");
        return postMapperJson(HttpUrl.PUSH_UMENG_CONFIG, map);


//        return postMapperJson(HttpUrl.PUSH_SETCONFIG_URL, map);
    }

    public PushJson getSetting() {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("devicetoken", Utility.getImeiCode());
        map.put("devicetype", "2");
        map.put("userkey", LoginUtil.getUid());
        PushJson json;
        try {
            String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Post, HttpUrl.PUSH_UMENG_GETCONFIG, map);

//            String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Post,HttpUrl.PUSH_GETCONFIG_URL, map);
            json = gson.fromJson(result, PushJson.class);
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


    public ListJson<PushHelpBean> getSettings() {

        HashMap<String, String> map = new HashMap<String, String>();
//		map.put("userid", getUserid());
//		map.put("channelid", getChannelid());
        map.put("devicetoken", Utility.getImeiCode());
        map.put("devicetype", "2");
        ListJson<PushHelpBean> json;
        try {
            String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Post, HttpUrl.PUSH_GETCONFIG_URL, map);
            json = gson.fromJson(result, ListJson.class);
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


    public CommonJson submitFinalTagToServer(String localstock, String cates, String tag) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userkey", LoginUtil.getUid());
//		map.put("channelid", getChannelid());
        map.put("umengtoken", SharePrefUtility.getUMENGID());
        map.put("devicetype", "2");
        map.put("tagsstr", tag);
        map.put("localstock", localstock);
        map.put("cates", cates);


        return super.postMapperJson(HttpUrl.PUSH_SUBMITTAG_URL, map);
    }

    public static void submitNewSetting(String cates,RestHttpUtils.RestHttpHandler<CommonJson> handler) {
        PushAgent mPushAgent = PushAgent.getInstance(XApplication.getInstance());
        RequestParams map = new RequestParams();
        map.put("userkey", LoginUtil.getUid());
        map.put("localstock", SharePrefHelper.getSharedPreferences(XApplication.getInstance(), SettingActivity.PUSH_LOC, false));
        map.put("cates", cates);
        map.put("devicetype", "2");
        map.put("umengtoken", mPushAgent.getRegistrationId());
        AppLogger.e(mPushAgent.getRegistrationId());
        //isvibrate
        map.put("isvibrate", SharePrefHelper.getSharedPreferencesBoolean(XApplication.getInstance(), SettingActivity.PUSH_VIBRATE, false) + "");
        map.put("issound", SharePrefHelper.getSharedPreferencesBoolean(XApplication.getInstance(), SettingActivity.PUSH_SOUND, false) + "");
        map.put("isnear", SharePrefHelper.getSharedPreferencesBoolean(XApplication.getInstance(), SettingActivity.PUSH_RECENT_PRICE, false) + "");
        map.put("islowest", SharePrefHelper.getSharedPreferencesBoolean(XApplication.getInstance(), SettingActivity.PUSH_HISTORY_PRICE, false) + "");

        postCommonResult(HttpUrl.PUSH_UMENG_CONFIG, map, handler, Object.class);
    }

    public static void getNewSetting(RestHttpUtils.RestHttpHandler<CommonJson> handler) {
        PushAgent mPushAgent = PushAgent.getInstance(XApplication.getInstance());

        RequestParams map = new RequestParams();
        map.put("umengtoken", mPushAgent.getRegistrationId());
        AppLogger.e("umengtoken" + mPushAgent.getRegistrationId());
        map.put("devicetype", "2");
        getCommonResult(HttpUrl.PUSH_UMENG_GETCONFIG, map, handler, PushCatBean.class);
    }
}