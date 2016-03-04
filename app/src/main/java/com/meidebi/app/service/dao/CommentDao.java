package com.meidebi.app.service.dao;

import android.app.Activity;

import com.google.gson.JsonParseException;
import com.loopj.android.http.RequestParams;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.base.net.HttpMethod;
import com.meidebi.app.base.net.HttpUtility;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.comment.CommentBean;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.Utility;
import com.meidebi.app.support.utils.component.LoginUtil;

import java.util.HashMap;

public class CommentDao extends BaseDao {
	public CommentDao(Activity activity) {
		super(activity);
	}

	public ListJson<CommentBean> getCommentList(String linkid, int page, int type) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("linkid", linkid);
		map.put("type", String.valueOf(type));
		map.put("p", String.valueOf(page));
		return mapperJson(HttpUrl.MSG_DETAIL_COMMENTLIST_URL, map);
	}

	public ListJson<CommentBean> mapperJson(String url, HashMap param) {
        ListJson<CommentBean> commentjson;
		try {
			String result = HttpUtility.getInstance().executeNormalTask(
					HttpMethod.Post, url, param);
			// String result = HttpUtils.postByHttpClient(mActivity,
			// HttpUrl.LOGIN_URL,param);
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

	public CommonJson<Object> submitComment(String comment, String id, String type,
			String referid) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userid", LoginUtil.getUid());
		map.put("content", comment);
		map.put("type", type);
		map.put("fromid", id);
		map.put("referid", referid);
		map.put("brand", Utility.getMobileBrand());
		map.put("model", Utility.getMobileMoldel());
		return postMapperJson(HttpUrl.MSG_DETAIL_SUBMIT_COMMENT_URL, map);
	}


    public static void postComment(String comment, String id, String type,
                                             String referid,String toUserid,RestHttpUtils.RestHttpHandler<CommonJson> handler) {
        RequestParams map = new RequestParams();
        map.put("userid", LoginUtil.getUid());
        map.put("content", comment);
        map.put("type", type);
        map.put("fromid", id);
        map.put("referid", referid);
        map.put("touserid", toUserid);
        map.put("brand", Utility.getMobileBrand());
        map.put("model", Utility.getMobileMoldel());
          postCommonResult(HttpUrl.MSG_DETAIL_SUBMIT_COMMENT_URL, map,handler,String.class);
    }



    public static void  getResult(String linkid, int page, int type, RestHttpUtils.RestHttpHandler<ListJson> handler) {
        RequestParams params = new RequestParams();
        params.put("linkid", linkid);
        params.put("type", String.valueOf(type));
        params.put("p", String.valueOf(page));
        getListResult(HttpUrl.MSG_DETAIL_COMMENTLIST_URL,params,handler,CommentBean.class);
     }
}
