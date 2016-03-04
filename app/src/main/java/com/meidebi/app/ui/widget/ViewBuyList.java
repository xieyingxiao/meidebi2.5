package com.meidebi.app.ui.widget;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.service.bean.detail.InventoryBean;
import com.meidebi.app.support.utils.Utility;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.ui.browser.BrowserWebActivity;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ViewBuyList extends PopupWindow {
	private List<InventoryBean> list_data = new ArrayList<InventoryBean>();
	private Activity activity;
	private LinearLayout linLayout;
	private Resources res = XApplication.getInstance().getResources();
	public ViewBuyList(List<InventoryBean> list_data, Activity activity) {
		this.list_data = list_data;
		this.activity = activity;
		onCreateView(activity);
	}

	public void onCreateView(Activity activity) {
		this.activity = activity;
		LayoutInflater _inflater = LayoutInflater.from(activity);
		View _view = _inflater.inflate(R.layout.common_fragment_activity, null);
		setContentView(_view);
//		linLayout = (LinearLayout) _view.findViewById(R.id.common_fragment);
		BuildBuyList();
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(false);
//		setAnimationStyle(R.style.PopupDownAnimation);
		int screenWidth = Utility.getScreenWidth(activity);
		setWidth(screenWidth);
		setHeight(LayoutParams.WRAP_CONTENT);
		// _dataBean = PublicDataBean.getInstance();
	}

	private void BuildBuyList() {
		linLayout.setBackgroundColor(Color.parseColor("#ffe9e9e9"));
		linLayout.setPadding(25, 25, 25, 0);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);  // , 1是可选写的
		lp.setMargins(0, 0, 0, 25); 
		TextView tv_title = new TextView(activity);
		tv_title.setText("购物清单");
		tv_title.setTextSize(15);
		tv_title.setGravity(Gravity.CENTER_HORIZONTAL);
		tv_title.setLayoutParams(lp);
		tv_title.setTextColor(res.getColor(R.color.share_plaform_text));
		linLayout.addView(tv_title);
		for (int i = 0; i < list_data.size(); i++) {
			TextView tv_buyitem = new TextView(activity);
			final InventoryBean buyitem = list_data.get(i);
			tv_buyitem.setText(buyitem.getTitle());
//			tv_buyitem.setBackground(res.getDrawable(R.drawable.btn_common_sel));
			tv_buyitem.setTextSize(18);
			tv_buyitem.setPadding(7, 7, 7, 7);
			tv_buyitem.setGravity(Gravity.CENTER_VERTICAL);
			tv_buyitem.setTextColor(res.getColor(R.color.share_plaform_text));
			tv_buyitem.setSingleLine(true);
			tv_buyitem.setLayoutParams(lp);
//			tv_buyitem.setClickable(true);
//			tv_buyitem.setFocusable(true);
//			tv_buyitem.setFocusableInTouchMode(true);
			tv_buyitem.setEllipsize(TextUtils.TruncateAt.END);
 			tv_buyitem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//					Bundle bundle = new Bundle();
//					MsgBaseBean bean = new MsgBaseBean();
//					bean.setId(buyitem.getId());
//					bundle.putSerializable("bean", bean);
					IntentUtil.start_activity(activity,
							BrowserWebActivity.class,new BasicNameValuePair("url", buyitem.getUrl()),new BasicNameValuePair("title", buyitem.getTitle()));
				}
			});
			linLayout.addView(tv_buyitem);
		}
	}

	
	public void showActionWindow(View parent) {
		// int screenHeight = Utility.getScreenHeight(activity);
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
	}
}
