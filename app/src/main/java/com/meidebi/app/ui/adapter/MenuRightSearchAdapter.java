package com.meidebi.app.ui.adapter;

import java.util.List;

import com.meidebi.app.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meidebi.app.service.bean.HotKeyJson;
import com.meidebi.app.ui.adapter.base.BaseArrayAdapter;

public class MenuRightSearchAdapter extends BaseArrayAdapter<HotKeyJson>{
	public MenuRightSearchAdapter(Context context, List<HotKeyJson> mList) {
		super(context, mList);
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		convertView = getLayoutInflater()
				.inflate(R.layout.adapter_menu_right_hotkey, null);
		TextView icon = (TextView)convertView.findViewById(R.id.adapter_menu_right_icon);
		icon.setText(arg0+1+"");
		TextView keyword_tx =(TextView)convertView.findViewById(R.id.adapter_menu_right_hotkeyword);
		keyword_tx.setText(mData.get(arg0).getKeyword());
		return convertView;
	}
	
}
