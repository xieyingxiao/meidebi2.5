package com.meidebi.app.service.init;

import android.content.res.Resources;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;

import java.util.ArrayList;
import java.util.List;

public class CatgoryInit {


	private Resources res = XApplication.getInstance().getResources();

	private static CatgoryInit instance = new CatgoryInit();
	private List<ChannelInitBean> list = new ArrayList<ChannelInitBean>();

	public static CatgoryInit getInstance() {
		return instance;
	}

	public List<ChannelInitBean> get() {
		return list;
	}

	public CatgoryInit() {
		BuildCategory();
	}

	private void BuildCategory() {
		// TODO Auto-generated method stub


		ChannelInitBean cat1 = new ChannelInitBean();
 		cat1.setImgres(R.drawable.ic_search_camera);
		cat1.setName(res.getString(R.string.pref_push_cat_1));
		cat1.setParam("1");
		list.add(cat1);
		ChannelInitBean cat2 = new ChannelInitBean();
 		cat2.setImgres(R.drawable.ic_search_shoe    );
		cat2.setName(res.getString(R.string.pref_push_cat_2));
		cat2.setParam("2");
		list.add(cat2);
		ChannelInitBean cat3 = new ChannelInitBean();
 		cat3.setImgres(R.drawable.ic_search_computer);
		cat3.setName(res.getString(R.string.pref_push_cat_3));
		cat3.setParam("3");
		list.add(cat3);
		ChannelInitBean cat4 = new ChannelInitBean();
    	cat4.setImgres(R.drawable.ic_search_house);
		cat4.setName(res.getString(R.string.pref_push_cat_4));
		cat4.setParam("4");
		list.add(cat4);
		ChannelInitBean cat5 = new ChannelInitBean();
    	cat5.setImgres(R.drawable.ic_search_child);
		cat5.setName(res.getString(R.string.pref_push_cat_5));
		cat5.setParam("6");
		list.add(cat5);
		ChannelInitBean cat6 = new ChannelInitBean();
 		cat6.setImgres(R.drawable.ic_search_makeup);
		cat6.setName(res.getString(R.string.pref_push_cat_6));
		cat6.setParam("48");
		list.add(cat6);
		ChannelInitBean cat7 = new ChannelInitBean();
        cat7.setImgres(R.drawable.ic_search_food);
        cat7.setName(res.getString(R.string.pref_push_cat_7));
		cat7.setParam("52");
		list.add(cat7);
        ChannelInitBean cat8 = new ChannelInitBean();
        cat8.setName(res.getString(R.string.pref_push_cat_8));
        cat8.setImgres(R.drawable.ic_search_book);
        cat8.setParam("54");
        list.add(cat8);
        ChannelInitBean cat9 = new ChannelInitBean();
        cat9.setImgres(R.drawable.ic_search_watch);
        cat9.setName(res.getString(R.string.pref_push_cat_9));
        cat9.setParam("55");
        list.add(cat9);
        ChannelInitBean cat10 = new ChannelInitBean();
        cat10.setImgres(R.drawable.ic_search_car);
        cat10.setName(res.getString(R.string.pref_push_cat_10));
        cat10.setParam("56");
        list.add(cat10);
        ChannelInitBean cat11 = new ChannelInitBean();
        cat11.setImgres(R.drawable.ic_search_other);
        cat11.setName(res.getString(R.string.pref_push_cat_11));
        cat11.setParam("57");
        list.add(cat11);

	}

}
