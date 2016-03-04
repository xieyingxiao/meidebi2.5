package com.meidebi.app.ui.adapter;

import com.meidebi.app.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.meidebi.app.XApplication;
import com.meidebi.app.service.init.ChanneInit;
import com.meidebi.app.service.init.ChannelInitBean;

public class LeftDrawerAdapter extends BaseAdapter {
	private ListView mListView;

	public LeftDrawerAdapter(ListView listView) {
		mListView = listView;
	}

	@Override
	public int getCount() {
		return ChanneInit.getInstance().get().size();
	}

	@Override
	public ChannelInitBean getItem(int position) {
		return ChanneInit.getInstance().get().get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(XApplication.getInstance())
					.inflate(R.layout.adapter_left_drawer, null);
		}
		LinearLayout ll = (LinearLayout) convertView
				.findViewById(R.id.ll_adapter_left_drawer_bg);
		ImageView iv = (ImageView) convertView
				.findViewById(R.id.iv_adapter_left_drawer_icon);
		TextView textView = (TextView) convertView.findViewById(R.id.textView);
		textView.setText(getItem(position).getName());
		boolean isChecked = mListView.isItemChecked(position);
		textView.setSelected(isChecked);
		iv.setImageResource(getItem(position).getImgres());
		iv.setSelected(isChecked);
		 if(isChecked){
			 ll.setBackgroundResource(R.color.titlebar_bg);
		 }else{
			 ll.setBackgroundResource(R.color.transparent);
		 }
		return convertView;
	};
}
