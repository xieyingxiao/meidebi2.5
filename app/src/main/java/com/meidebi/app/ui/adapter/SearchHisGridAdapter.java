package com.meidebi.app.ui.adapter;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.meidebi.app.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meidebi.app.service.bean.HotKeyJson;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.adapter.base.BaseArrayAdapter;
import com.meidebi.app.ui.search.SearchHistoryFragment;
import com.meidebi.app.ui.search.SearchResultActivity;

public class SearchHisGridAdapter extends BaseArrayAdapter<HotKeyJson> {
	private SearchHistoryFragment frament;
	private Animation anim_del;

	public SearchHisGridAdapter(SearchHistoryFragment frament,
			List<HotKeyJson> mList) {
		super(frament.getActivity(), mList);
		this.frament = frament;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = getLayoutInflater().inflate(R.layout.adapter_search_his,
				null);
		// TextView icon =
		// (TextView)convertView.findViewById(R.id.adapter_menu_right_icon);
		// icon.setText(arg0+1+"");
		final RelativeLayout layout = (RelativeLayout) convertView
				.findViewById(R.id.layout_adapter_search_item);
		TextView keyword_tx = (TextView) convertView
				.findViewById(R.id.tv_adapter_search_text);
		keyword_tx.setText(mData.get(position).getKeyword());
		keyword_tx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IntentUtil.start_activity(frament.getActivity(),
						SearchResultActivity.class, new BasicNameValuePair(
								"keyword", mData.get(position).getKeyword()));
			}
		});
		ImageButton iv_del = (ImageButton) convertView
				.findViewById(R.id.iv_adapter_search_del);
		iv_del.setVisibility(View.VISIBLE);
		iv_del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				layout.startAnimation(getAnim());
				getAnim().setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						String remove_str = mData.get(position).getKeyword();
						if (position == 0) {
							remove_str = remove_str + ",";
						} else {
							remove_str = "," + remove_str;
						}
						if (mData.size() == 1) {
							SharePrefUtility.RemoveHistoyOneItem(mData.get(
									position).getKeyword());
						} else {
							SharePrefUtility.RemoveHistoyOneItem(remove_str);
						}
						frament.refreshData();
						// mData.remove(position);
						// notifyDataSetChanged();
						// frament.ClearBtnIsVisibile();
					}
				});
			}
		});
		return convertView;
	}

	private Animation getAnim() {
		if (anim_del == null) {
			anim_del = (Animation) AnimationUtils.loadAnimation(context,
					R.anim.zoom_exit);

		}
		return anim_del;
	}

}
