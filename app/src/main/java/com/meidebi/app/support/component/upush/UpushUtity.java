package com.meidebi.app.support.component.upush;

import android.app.Activity;
import android.content.Context;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.service.bean.CatagerogyBean;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.GsonUtils;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.support.utils.shareprefelper.SharePrefHelper;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.setting.SettingActivity;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import org.apache.http.Header;

import java.util.List;

/**
 * Created by mdb-ii on 14-12-30.
 */
public class UpushUtity {

    public static void OnStart(Context context) {
        PushAgent mPushAgent = PushAgent.getInstance(context);
        mPushAgent.onAppStart();
        if (SharePrefUtility.getPushOn()) {
            mPushAgent.enable(mRegisterCallback);
//             mPushAgent.setMergeNotificaiton(false);
        } else {
            mPushAgent.disable();
        }
    }

    public static void setSlientTime(int startime, int endtime) {
        PushAgent mPushAgent = PushAgent.getInstance(XApplication.getInstance());
        mPushAgent.setNoDisturbMode(startime, 0, endtime, 0);
    }

    public static void SwitchPush(Activity activity) {
        PushAgent mPushAgent = PushAgent.getInstance(activity);
        String info = String.format("enabled:%s  isRegistered:%s",
                mPushAgent.isEnabled(), mPushAgent.isRegistered());
        if (!SharePrefUtility.getPushOn() && (mPushAgent.isEnabled() || UmengRegistrar.isRegistered(activity))) {
            mPushAgent.disable();
        } else {
            mPushAgent.enable();
        }
    }

    public static IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {

        @Override
        public void onRegistered(String registrationId) {
            // TODO Auto-generated method stub
            AppLogger.e("registrationId" + registrationId);
            SharePrefUtility.setUMENGID(registrationId);
            setCat();

        }
    };

    public static void setCat() {
        ListJson<CatagerogyBean> beans = GsonUtils.fromListJson(SharePrefUtility.getCatList(), CatagerogyBean.class);
        submitNewSetting(getChooseSet(beans.getData()));
    }

    public static void setCat(String cates) {
        submitNewSetting(cates);
    }


    public static void submitNewSetting(final String cates) {
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


        AppLogger.e("umengtoken" + mPushAgent.getRegistrationId());

        AppLogger.e("cats" + cates);
        RestHttpUtils.post(HttpUrl.PUSH_UMENG_CONFIG, map, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                AppLogger.e("responseString" + responseString);
            }
        });
    }


    public static String getChooseSet(List<CatagerogyBean> cats) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < cats.size(); i++) {
            if (cats.get(i).getSetPush() == 1) {
                // if (contentList.get(i).getKey() != 0) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(cats.get(i).getId());
                // }
            }
        }
        return sb.toString();

    }


}
