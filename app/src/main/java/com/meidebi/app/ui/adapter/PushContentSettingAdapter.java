package com.meidebi.app.ui.adapter;

import java.util.List;

import com.meidebi.app.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.meidebi.app.service.bean.lbs.PushSetBean;
import com.meidebi.app.ui.adapter.base.BaseArrayAdapter;

public class PushContentSettingAdapter extends BaseArrayAdapter<PushSetBean> {
	private LayoutInflater mInflater;

	public PushContentSettingAdapter(Context context, List<PushSetBean> objects) {
		super(context, objects);
		// TODO Auto-generated constructor stub
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = buildLayout(holder, convertView);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		setDataView(holder, position);
		return convertView;
	}

	private View buildLayout(ViewHolder holder, View convertView) {
		convertView = getLayoutInflater().inflate(
				R.layout.adapter_push_setting, null);
		holder.icon = (CheckBox) convertView.findViewById(R.id.cb_adapter_pushset_sel);
		holder.title = (TextView) convertView.findViewById(R.id.tv_adapter_pushset_title);
		// holder.summary = (TextView) convertView
		// .findViewById(com.meidebi.app.R.id.summary);
		convertView.setTag(holder);
		return convertView;// 绑定view

	}

	private void setDataView(final ViewHolder holder, final int position) {// 显示数据
		final PushSetBean item = mData.get(position);
		holder.title.setText(item.getTitle());
		holder.icon.setChecked(item.getChecked());
		holder.icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setIsClcik(true);
				// TODO Auto-generated method stub
				// if (position == 0) {
				// mData.get(0).getChecked()
				// isLocal = holder.icon.isChecked();
				//
				// } else {

				// if (holder.icon.isChecked()) {
				item.setChecked(holder.icon.isChecked());
				// chooseSet.add(item.getId());
				// } else {
				// chooseSet.remove(item.getId());
				// }
				// }
			}
		});
	}

//	public String getIsLocal() {
//		return mData.get(0).getChecked() ? "1" : "0";
//	}
//
//	public int getIsLocalInt() {
//		return mData.get(0).getChecked() ? 1 : 0;
//	}

	public String getChooseSet() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (int i = 0; i < mData.size(); i++) {
			if (mData.get(i).getChecked()) {
				if (mData.get(i).getId() != 0) {
					if (first) {
						first = false;
					} else {
						sb.append(",");
					}
					sb.append(mData.get(i).getId());
				}
			}
		}
		return sb.toString();
	}

	private class ViewHolder {
		CheckBox icon;
		TextView title;
		// TextView summary;
	}
	
	public Boolean isClcik = false;
 	public Boolean getIsClcik() {
		return isClcik;
	}

	public void setIsClcik(Boolean isClcik) {
		this.isClcik = isClcik;
	}
 
}
