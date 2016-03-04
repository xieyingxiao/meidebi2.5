package com.meidebi.app.service.dao;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.base.net.HttpMethod;
import com.meidebi.app.base.net.HttpUtility;
import com.meidebi.app.service.bean.HotKeyJson;
import com.meidebi.app.service.bean.base.LinkListBean;
import com.meidebi.app.service.bean.base.LinkListJson;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.msg.MsgDetailBean;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 搜索dao 包括关键字获取和关键字搜索
 * @author mdb-ii
 *
 */
public class SearchDao extends BaseDao {
	private String keyword;
	private boolean hasSearched = false;
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public boolean isHasSearched() {
		return hasSearched;
	}
	public void setHasSearched(boolean hasSearched) {
		this.hasSearched = hasSearched;
	}
	public List<HotKeyJson> getHotKey() {
		List<HotKeyJson> list = new ArrayList<HotKeyJson>();
		try {
			String result = HttpUtility.getInstance().executeNormalTask(
					HttpMethod.Get, HttpUrl.SEARCH_GET_HOTKEY_URL, null);
			JSONObject jsonObject = new JSONObject(result);
			String data = jsonObject.getString("data");
			LinkedHashMap<String, Object> retMap2 = gson.fromJson(data.toString(),
					new TypeToken<LinkedHashMap<String, Object>>() {
					}.getType());
			for (String key : retMap2.keySet()) {
				HotKeyJson json = new HotKeyJson();
				json.setKeyword(key);
		        list.add(json);
			}
			return list;
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
	
	public ListJson<LinkListBean<MsgDetailBean>> mapperJson() {
		// TODO Auto-generated method stub

        ListJson<LinkListBean<MsgDetailBean>> mainJson;
		try {
	        HashMap<String,String> map =new HashMap<String, String>();
	        map.put("noshowdan", "1");
	        map.put("keywords", keyword);
	        map.put("p",String.valueOf(page));
	        setHasSearched(true);
			String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Get, HttpUrl.SEARCH_URL, map);
//			AppLogger.e("result"+result);

//			String result = RequestCacheUtil.getRequestContent(mActivity,
//					Urls.WIKI_LIST + Utility.getScreenParams(mActivity),
//					Constants.WebSourceType.Json,
//					Constants.DBContentType.Content_list, useCache);
//			mainJson = mObjectMapper.readValue(result,
//					new TypeReference<MainJson>() {
//					});
			mainJson = gson.fromJson(result, ListJson.class);
			if (mainJson == null) {
				return null;
			}
			return mainJson;
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

    public void getResult(RestHttpUtils.RestHttpHandler<LinkListJson> handler){

        RequestParams map = new RequestParams();
        map.put("noshowdan", "1");
        map.put("keywords", keyword);
        map.put("p",String.valueOf(page));
        map.put("version", Utility.getVersionCode(XApplication.getInstance()));
        postLinkListResult(HttpUrl.SEARCH_URL, map, handler, MsgDetailBean.class);
     }
}
