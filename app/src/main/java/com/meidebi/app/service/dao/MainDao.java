package com.meidebi.app.service.dao;


import com.google.gson.JsonParseException;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.base.net.HttpMethod;
import com.meidebi.app.base.net.HttpUtility;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.msg.MsgDetailBean;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;

import java.util.HashMap;
/**
 * 主页 分类筛选()
 * @author mdb-ii
 *
 */
class MainDao extends BaseDao {
 
	private int cat_id = 1;
 
	private Boolean isAll = true;
	private String product_type="0";
	//start get set 

	public String getProduct_type() {
		return product_type;
	}

	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}

	public int getCat_id() {
		return cat_id;
	}

	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}

	public Boolean getIsAll() {
		return isAll;
	}

	public void setIsAll(Boolean isAll) {
		this.isAll = isAll;
	}
	//end get set 

//	public MainDao(Activity activity) {
//		super(activity);
//	}

	private MainDao mainDao;

	

	public ListJson<MsgDetailBean> mapperJson(boolean useCache) {
		// TODO Auto-generated method stub
        ListJson<MsgDetailBean> mainJson;
        HashMap<String,String> map =new HashMap<String, String>();
        map.put("cats", product_type);
        map.put("pagecount", "20");
        map.put("p", String.valueOf(page));
	//	String url = HttpUrl.getCatListUrl(cat_id, isAll)+"-cats-"+product_type+"-p-"+page;
		try {
			String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Get,HttpUrl.getCatListUrl(cat_id, isAll), map);//分类网址判断

			mainJson = gson.fromJson(result, ListJson.class);
			if (mainJson == null) {
				return null;
			}else{
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
	
//	public MainBean getMore(String more_url) {
//		MainBean response;
//		try {
//			String result = RequestCacheUtil.getRequestContent(mActivity,
//					more_url + Utility.getScreenParams(mActivity),
//					Constants.WebSourceType.Json,
//					Constants.DBContentType.Content_list, true);
//			response = mObjectMapper.readValue(result,
//					new TypeReference<WikiMoreResponse>() {
//					});
//			return response;
//		} catch (JsonParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}


	public MainDao getMainDao() {
		return mainDao;
	}

	public void setMainDao(MainDao mainDao) {
		this.mainDao = mainDao;
	}

	private void saveCache(String json) {
		// TODO Auto-generated method stub
		if(page == 1){
			SharePrefUtility.setIndexCache(json);
		}
	}


}
