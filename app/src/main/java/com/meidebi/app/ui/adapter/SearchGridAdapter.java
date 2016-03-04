package com.meidebi.app.ui.adapter;

import java.util.List;

import com.meidebi.app.R;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meidebi.app.service.bean.HotKeyJson;
import com.meidebi.app.ui.adapter.base.BaseArrayAdapter;

public class SearchGridAdapter extends BaseArrayAdapter<HotKeyJson> {

	public SearchGridAdapter(Context context, List<HotKeyJson> mList) {
		super(context, mList);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = getLayoutInflater().inflate(R.layout.adapter_search_his,
				null);
		TextView keyword_tx = (TextView) convertView
				.findViewById(R.id.tv_adapter_search_text);
		keyword_tx.setText(mData.get(position).getKeyword());
		keyword_tx.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				IntentUtil.start_activity((Activity) context,
//						SearchResultActivity.class, new BasicNameValuePair(
//								"keyword", mData.get(position).getKeyword()));
			}
		});
		return convertView;
	}
}
