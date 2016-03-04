package com.meidebi.app.ui.search;

import java.util.ArrayList;
import java.util.List;

import com.meidebi.app.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.meidebi.app.service.bean.HotKeyJson;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.adapter.SearchHisGridAdapter;

public class SearchHistoryFragment extends Fragment implements OnClickListener {
	private ListView gv_his;
	private List<HotKeyJson> list = new ArrayList<HotKeyJson>();
	private SearchHisGridAdapter lv_keyword_Adapter;
	private Button btn_clear;

	public SearchHistoryFragment() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.layout_search_gridview, null);
		btn_clear = (Button) view.findViewById(R.id.btn_search_clear_all);
		btn_clear.setOnClickListener(this);
		gv_his = (ListView) view.findViewById(R.id.common_list_view);
		gv_his.setDivider(null);
//		gv_his.setSelector(new ColorDrawable(Color.TRANSPARENT));
		onGetData();
		return view;
	}

	public void onGetData() {
		// TODO Auto-generated method stub
		String[] his_array = SharePrefUtility.getSearchHistory();// 从sp取历史
		for (int i = 0; i < his_array.length; i++) {
			if (!TextUtils.isEmpty(his_array[i])) {
				HotKeyJson json = new HotKeyJson();
				json.setKeyword(his_array[i]);
				list.add(json);
			}
		}
		lv_keyword_Adapter = new SearchHisGridAdapter(this, list);
		gv_his.setAdapter(lv_keyword_Adapter);
		ClearBtnIsVisibile();
	}

	public void ClearBtnIsVisibile() {//刷新清除按钮
		if (list.size() != 0) {
			btn_clear.setVisibility(View.VISIBLE);
		} else {
			btn_clear.setVisibility(View.GONE);

		}
	}

	public void refreshData() {
		list.clear();
		String[] his_array = SharePrefUtility.getSearchHistory();// 从sp取历史
		for (int i = 0; i < his_array.length; i++) {
			if (!TextUtils.isEmpty(his_array[i])) {
				HotKeyJson json = new HotKeyJson();
				json.setKeyword(his_array[i]);
				list.add(json);
			}
		}
		lv_keyword_Adapter.notifyDataSetChanged();
		ClearBtnIsVisibile();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_search_clear_all:
			SharePrefUtility.ClearAllHistoy();
			list.clear();
			lv_keyword_Adapter.notifyDataSetChanged();
			ClearBtnIsVisibile();
			break;

		default:
			break;
		}
	}
	
	
}
