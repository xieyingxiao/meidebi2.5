package com.meidebi.app.service.init;

import android.content.res.Resources;
import com.meidebi.app.R;
import com.meidebi.app.XApplication;

import java.util.ArrayList;
import java.util.List;

public class ChanneInit {
	public static String jing = "jing";
	public static String guo = "guo";
	public static String hai = "hai";

	public static String tian = "tian";

	public static String shai = "shai";
	public static String quan = "quan";

	private Resources res = XApplication.getInstance().getResources();

	private static ChanneInit instance = new ChanneInit();
	private List<ChannelInitBean> list = new ArrayList<ChannelInitBean>();

	public static ChanneInit getInstance() {
		return instance;
	}

	public List<ChannelInitBean> get() {
		return list;
	}

	public ChanneInit() {
		BuildCategory();
	}

	private void BuildCategory() {
		// TODO Auto-generated method stub
		ChannelInitBean cat1 = new ChannelInitBean();
//		cat1.setImgres(R.drawable.ic_left_drawer_index_sel);
		cat1.setName(res.getString(R.string.cat_index));
		cat1.setParam("jing");
		list.add(cat1);
		ChannelInitBean cat2 = new ChannelInitBean();
//		cat2.setImgres(R.drawable.ic_left_drawer_inland_sel);
		cat2.setName(res.getString(R.string.cat_inland));
		cat2.setParam("guo");
		list.add(cat2);
		ChannelInitBean cat3 = new ChannelInitBean();
//		cat3.setImgres(R.drawable.ic_left_drawer_tmall_sel);
		cat3.setName(res.getString(R.string.cat_tmall));
		cat3.setParam("tian");
		list.add(cat3);
		ChannelInitBean cat4 = new ChannelInitBean();
//		cat4.setImgres(R.drawable.ic_left_drawer_haitao_sel);
		cat4.setName(res.getString(R.string.cat_haitao));
		cat4.setParam("hai");
		list.add(cat4);
//		ChannelInitBean cat5 = new ChannelInitBean();
////		cat5.setImgres(R.drawable.ic_left_drawer_showorder_sel);
//		cat5.setName(res.getString(R.string.cat_ordershow));
//		cat5.setParam("shai");
//		list.add(cat5);
//		ChannelInitBean cat6 = new ChannelInitBean();
////		cat6.setImgres(R.drawable.ic_left_drawer_coupon_sel);
//		cat6.setName(res.getString(R.string.cat_buycoupon));
//		cat6.setParam("quan");
//		list.add(cat6);
//		ChannelInitBean cat7 = new ChannelInitBean();
//		cat7.setImgres(R.drawable.ic_left_drawer_coupon_sel);
//		cat7.setName("云相机");
//		cat7.setParam("camera");
//		list.add(cat7);
	}

}
