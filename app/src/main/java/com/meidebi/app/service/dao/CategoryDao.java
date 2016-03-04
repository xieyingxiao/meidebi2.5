package com.meidebi.app.service.dao;

import android.text.TextUtils;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.service.bean.CatagerogyBean;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.support.component.upush.UpushUtity;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.GsonUtils;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;

public class CategoryDao extends BaseDao {


    public CategoryDao() {
    }


    public static void getCategory(final RestHttpUtils.RestHttpHandler<ListJson> handler) {
        String result = SharePrefUtility.getCatList();
        if (TextUtils.isEmpty(result)) {
            RequestParams params = new RequestParams();
            params.put("ismain", "1");
            RestHttpUtils.get(HttpUrl.CAT_GET_CAT_URL, params, new TextHttpResponseHandler() {
                public void onStart() {
                    if(handler!=null) {
                        handler.onStart();
                    }
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                    String cache =  XApplication.getInstance().getResources().getString(R.string.json_cat_init);
                    SharePrefUtility.setCatList(cache);
                    if(handler!=null) {
                        handler.onSuccess(GsonUtils.fromListJson(cache, CatagerogyBean.class));
                    }
                    UpushUtity.OnStart(XApplication.getInstance());
                }

                @Override
                public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                    AppLogger.e("response" + responseString);
                    if(handler!=null) {
                        handler.onSuccess(GsonUtils.fromListJson(responseString, CatagerogyBean.class));
                    }
                    SharePrefUtility.setCatList(responseString);
                    UpushUtity.OnStart(XApplication.getInstance());
                }
            });

        } else {
            AppLogger.e("cache" + result);
            if(handler!=null) {
                handler.onSuccess(GsonUtils.fromListJson(result, CatagerogyBean.class));
            }
            UpushUtity.OnStart(XApplication.getInstance());
        }

    }

}