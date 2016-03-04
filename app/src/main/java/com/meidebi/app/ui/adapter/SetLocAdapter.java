package com.meidebi.app.ui.adapter;

import java.util.List;

import com.meidebi.app.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meidebi.app.service.bean.lbs.AddressBean;
import com.meidebi.app.ui.adapter.base.BaseArrayAdapter;

public class SetLocAdapter extends BaseArrayAdapter<AddressBean>{


    public SetLocAdapter(Context context, List<AddressBean> mList) {
		super(context, mList);
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = getLayoutInflater().inflate(R.layout.adapter_address,
					null);	
			holder.tv = (TextView)convertView.findViewById(R.id.tv_adapter_address);
			convertView.setTag(holder);
			} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(mData.get(position).getRegion_name());
		return convertView;
 	}
	
	static class ViewHolder {
	private TextView tv;
	}

}
