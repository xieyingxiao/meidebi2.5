package com.meidebi.app.service.dao.user;

import com.google.gson.JsonParseException;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.base.net.HttpMethod;
import com.meidebi.app.base.net.HttpUtility;
import com.meidebi.app.service.bean.user.LoginJson;
import com.meidebi.app.service.dao.BaseDao;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.Net;
import com.meidebi.app.support.utils.Utility;

import java.util.HashMap;

public class SocailLoginDao extends BaseDao {

    private String sid;
    private String token;
    private String nickname;
    private String headerImage;

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LoginJson OauthLogin(SocailPlatform socailMethod) {
        String url = null;
        HashMap<String, String> map = new HashMap<String, String>();
        switch (socailMethod) {
            case QQ:
                url = HttpUrl.QQ_OAUTHLOGIN;
                map.put("openid", getSid());
                map.put("qqnickname", getNickname());
                map.put("access_token", getToken());
                map.put("type", 1 + "");
                break;
            case Weibo:
                url = HttpUrl.WB_OAUTHLOGIN;
                map.put("uid", getSid());
                map.put("weibo_nickname", getNickname());
                map.put("weibo_access_token", getToken());
                map.put("type", 2 + "");
                break;
            case Taobao:
                url = HttpUrl.TB_OAUTHLOGIN;
                map.put("tbuser_id", getSid());
                map.put("type", 3 + "");
                break;
        }
        AppLogger.e("url" + url);
        return mapperJson(url, map);
    }

    /**
     * 传参  QQ: openid,access_token,qqnickname
     * 微博: uid,weibo_access_token,weibo_nickname
     * 淘宝: tbuser_id,tbaccess_token,tbnickname
     *
     * @param socailMethod
     * @return
     */
    public LoginJson AutoReg(SocailPlatform socailMethod) {
        String url = null;
        HashMap<String, String> map = new HashMap<String, String>();
        switch (socailMethod) {
            case QQ:
                map.put("openid", getSid());
                map.put("access_token", getToken());
                map.put("qqnickname", getNickname());
                map.put("type", 1 + "");
                break;
            case Weibo:
                map.put("uid", getSid());
                map.put("weibo_access_token", getToken());
                map.put("weibo_nickname", getNickname());
                map.put("type", 2 + "");
                break;
            case Taobao:
                map.put("tbuser_id", getSid());
                map.put("tbaccess_token", getToken());
                map.put("tbnickname", getNickname());
                map.put("type", 3 + "");
                break;
        }

        return mapperJson(HttpUrl.USER_AOTUREG, map);
    }

    public LoginJson mapperJson(String url, HashMap<String, String> map) {
        // TODO Auto-generated method stub
        LoginJson loginJson;
        try {
            String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Post, url, map);
            if (!Net.isWifi(XApplication.getInstance())) {
                if (!Net.isWifi(XApplication.getInstance())) {
                    Utility.string2File(result, AppConfigs.LOGIN_LOG);
                }
            }
            AppLogger.e("result" + result);
            //String result = HttpUtils.postByHttpClient(mActivity, HttpUrl.LOGIN_URL,param);
            loginJson = gson.fromJson(result, LoginJson.class);
            if (loginJson == null) {
                return null;
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
