package com.meidebi.app.service.dao;

import android.app.Activity;

import com.google.gson.JsonParseException;
import com.loopj.android.http.RequestParams;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.base.net.HttpMethod;
import com.meidebi.app.base.net.HttpUtility;
import com.meidebi.app.service.bean.BannerBean;
import com.meidebi.app.service.bean.base.Chanel;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.base.LinkListJson;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.msg.MsgDetailBean;
import com.meidebi.app.service.bean.msg.SingleBean;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.Utility;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.orm.SugarRecord;

import java.util.HashMap;
import java.util.List;

public class SingleDao extends BaseDao {
	private String product_type;
	private int limit = 10;
	private int order = 0;
	private boolean isAll;
	private String chanel;

	// start get set

	public boolean isAll() {
		return isAll;
	}

	public void setAll(boolean isAll) {
		this.isAll = isAll;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getProduct_type() {
		return product_type;
	}

	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}

	// end get set

	public String getChanel() {
		return chanel;
	}

	public void setChanel(String chanel) {
		this.chanel = chanel;
	}

	public SingleDao(Activity activity) {
		super(activity);
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	private MainDao mainDao;

	public CommonJson<SingleBean> mapperJson(boolean isHot) {
		// TODO Auto-generated method stub
		String url = isAll ? HttpUrl.CAT_HOT_LIST_All_URL
				: HttpUrl.CAT_LIST_All_URL;
        CommonJson<SingleBean> mainJson;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("type", chanel);
		map.put("cats", product_type);
		map.put("limit", "20");
		map.put("p", String.valueOf(page));
		try {
			String result = HttpUtility.getInstance().executeNormalTask(
					HttpMethod.Get, url, map);// 分类网址判断
			// AppLogger.e("result"+result);
			saveCache(result);
			mainJson = gson.fromJson(result, CommonJson.class);
			if (mainJson == null) {
				return null;
			} else {
				List<MsgDetailBean> _list = ((SingleBean)mainJson.getData()).getLinklist();
				if (page == 1) {
					String[] args = { product_type, chanel,
							String.valueOf(page), isAll ? "1" : "0" };
					SugarRecord
							.deleteAll(
									Chanel.class,
									"CATSTR= ? and M_CHANEL=? and M_PAGE=? and IS_ALL=?",
									args);
				}
					for (int i = 0; i < _list.size(); i++) {
						MsgDetailBean bean = _list.get(i);
						if (page == 1) {
						Chanel chanelbean = new Chanel();
						chanelbean.setD_id(bean.getId());
						chanelbean.setCat_str(product_type);
						chanelbean.setmPage(String.valueOf(page));
						chanelbean.setmChanel(chanel);
						chanelbean.setIsAll(isAll);
						SugarRecord.save(chanelbean);
						}
						MsgDetailBean dbbean = SugarRecord.findById(MsgDetailBean.class, bean.getId());
						if(dbbean!=null){
						bean.setHasVoteSp(dbbean.getHasVoteSp());
						}
						dbbean = null;
				}
				SugarRecord.saveInTx(_list);
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

	public MainDao getMainDao() {
		return mainDao;
	}

	public void setMainDao(MainDao mainDao) {
		this.mainDao = mainDao;
	}

	private void saveCache(String json) {
		// TODO Auto-generated method stub
		if (page == 1) {
			SharePrefUtility.setIndexCache(json);
		}
	}
	
	public void indexBanner(RestHttpUtils.RestHttpHandler<ListJson> handle){
        getListResult(HttpUrl.INDEX_BANNER, null, handle, BannerBean.class);

	}

    public void getResult(boolean isHot,RestHttpUtils.RestHttpHandler<LinkListJson> handler){
         String url = isAll ? HttpUrl.CAT_HOT_LIST_All_URL
                : HttpUrl.CAT_LIST_All_URL;
        RequestParams  map = new RequestParams();
        map.put("type", chanel);
        map.put("cats", product_type);
        AppLogger.e("cats"+ product_type);
        map.put("limit", "20");
        map.put("p", String.valueOf(page));
        map.put("version", Utility.getVersionCode(XApplication.getInstance()));
        getLinkListResult(url, map, handler, MsgDetailBean.class);

    }
}
