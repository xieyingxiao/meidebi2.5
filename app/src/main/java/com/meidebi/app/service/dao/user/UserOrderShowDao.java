package com.meidebi.app.service.dao.user;

import com.loopj.android.http.RequestParams;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.msg.OrderShowBean;
import com.meidebi.app.service.dao.BaseDao;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.LoginUtil;

/**
 * Created by mdb-ii on 15-1-9.
 */
public class UserOrderShowDao extends BaseDao {



    public void getResult(RestHttpUtils.RestHttpHandler<ListJson> handler) {
        // TODO Auto-generated method stub
        RequestParams params = new RequestParams();
        AppLogger.e("userkey"+ LoginUtil.getUid());

        params.put("userkey", LoginUtil.getUid());
         params.put("pagecount", "20");
        params.put("page", String.valueOf(page));
        getListResult(HttpUrl.USER_MY_ORDERSHOW, params, handler,OrderShowBean.class);

    }
}
