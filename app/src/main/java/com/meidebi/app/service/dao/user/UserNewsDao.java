package com.meidebi.app.service.dao.user;

import com.google.gson.JsonParseException;
import com.loopj.android.http.RequestParams;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.base.net.HttpMethod;
import com.meidebi.app.base.net.HttpUtility;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.msg.MsgDetailBean;
import com.meidebi.app.service.dao.BaseDao;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.LoginUtil;

import java.util.HashMap;

/**
 * Created by mdb-ii on 15-1-9.
 */
public class UserNewsDao extends BaseDao {

    public ListJson<MsgDetailBean> getResult() {
        // TODO Auto-generated method stub
        ListJson<MsgDetailBean> loginJson = null;
        try {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("userkey", LoginUtil.getUid());
            param.put("pagecount", "20");
            param.put("page", String.valueOf(page));
            String result = HttpUtility.getInstance().executeNormalTask(
                    HttpMethod.Post, HttpUrl.USER_MY_LINK, param);
            loginJson = toListJson(result,MsgDetailBean.class);
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

    public void getResult(RestHttpUtils.RestHttpHandler<ListJson> handler){
        RequestParams map = new RequestParams();
        map.put("userkey", LoginUtil.getUid());
        map.put("pagecount", "20");
        map.put("page", String.valueOf(page));
        getListResult(HttpUrl.USER_MY_LINK, map, handler, MsgDetailBean.class);
    }
}
