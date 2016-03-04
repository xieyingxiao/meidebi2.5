package com.meidebi.app.ui.setting;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.meidebi.app.R;
import com.meidebi.app.service.bean.lbs.AddressBean;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.ui.adapter.SetLocAdapter;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")public class AddressFragment extends Fragment {
	private List<AddressBean> list_address = new ArrayList<AddressBean>();
	private SetLocAdapter daAdapter;
	private int type;

	public AddressFragment(int type) {
		this.type = type;
	}

	public AddressFragment() {
	}

	public List<AddressBean> getList_address() {
		return list_address;
	}

	public void setList_address(List<AddressBean> list_address) {
		this.list_address = list_address;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.layout_cards_manual_location,
				container, false);
		initListView(view);
		return view;
	}

	private void initListView(View view) {
		ListView lv = (ListView) view.findViewById(R.id.common_list_view);
		daAdapter = new SetLocAdapter(getActivity(), list_address);
		daAdapter.setData(getList_address());
		lv.setAdapter(daAdapter);
		lv.setDivider(getResources().getDrawable(R.drawable.list_divider));
		lv.setDividerHeight(1);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int postion, long arg3) {
				// TODO Auto-generated method stub
				AddressBean bean = list_address.get(postion);
				AppLogger.e("type" + type);
				if (type < 4) {
					((PushManualLocationActivity) getActivity()).refreshView(
							bean.getRegion_id(), type + 1,
							bean.getRegion_name());
				}
			}
		});
	}

	public void refresh() {
		if (daAdapter != null) {
			daAdapter.setData(getList_address());
			daAdapter.notifyDataSetChanged();
		}
	}

}
