package com.meidebi.app.ui.search;

import java.util.ArrayList;
import java.util.List;

import com.meidebi.app.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.meidebi.app.service.bean.HotKeyJson;
import com.meidebi.app.service.dao.SearchDao;
import com.meidebi.app.support.utils.Net;
import com.meidebi.app.ui.adapter.SearchGridAdapter;

public class SearchHotKeyFragment extends Fragment implements OnClickListener {
	private SearchGridAdapter lv_keyword_Adapter;
	private ListView lv_keyword;
	private List<HotKeyJson> list = new ArrayList<HotKeyJson>();
	private SearchDao dao;
	private Button btn_refresh;

	/**
	 * 数据回调处理
	 */
	private Handler mHandler = new Handler() {
		@SuppressLint("ValidFragment")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:// 刷新
				btn_refresh.setVisibility(View.GONE);
				lv_keyword_Adapter = new SearchGridAdapter(getActivity(), list);
				lv_keyword.setDivider(null);
				lv_keyword.setAdapter(lv_keyword_Adapter);
				break;
			case 2:
				if(!Net.isNetworkAvailable(getActivity()))
				btn_refresh.setVisibility(View.VISIBLE);
				break;
			}
		};

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.layout_search_gridview, null);
		lv_keyword = (ListView) view.findViewById(R.id.common_list_view);
//		lv_keyword.setSelector(new ColorDrawable(Color.TRANSPARENT));
		btn_refresh = (Button) view.findViewById(R.id.btn_search_refresh);
		btn_refresh.setOnClickListener(this);
		onRefresh();
		return view;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_search_refresh:
			onRefresh();
			break;

		default:
			break;
		}
	}

	public void onRefresh() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				getData();
			}
		}.start();
	}

	/**
	 * 登陆前的列表
	 */
	private void getData() {
		if (dao == null) {
			dao = new SearchDao();
		}
		list = dao.getHotKey();
		if (list != null) {
			mHandler.sendEmptyMessage(1);
		} else {
			mHandler.sendEmptyMessage(2);
		}
	}

}
