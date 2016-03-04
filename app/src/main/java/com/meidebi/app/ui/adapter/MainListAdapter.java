package com.meidebi.app.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.base.MsgBaseBean;
import com.meidebi.app.service.bean.msg.MsgDetailBean;
import com.meidebi.app.service.dao.detail.MsgDetailDao;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.support.utils.content.TimeUtil;
import com.meidebi.app.ui.adapter.base.BaseArrayAdapter;
 import com.meidebi.app.ui.msgdetail.MsgDetailActivity;
import com.meidebi.app.ui.submit.CommentActivity;
import com.orm.SugarRecord;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;

public class MainListAdapter extends BaseArrayAdapter<MsgDetailBean>  {
	private final int NETERR = 404;
	public final int DATAERR = 400;
  	private String userid = XApplication.getInstance().getAccountBean().getId();
 

	// List<MainListItemBean> mList =new ArrayList<MainListItemBean>();
	private MsgDetailDao dao;

	public MsgDetailDao getDao() {
		if (dao == null) {
			dao = new MsgDetailDao((Activity) context);
		}
		return dao;
	}

	private boolean isSearch = false;

	public MainListAdapter(Context context, List<MsgDetailBean> mList) {
		super(context, mList);
	}

	public boolean isSearch() {
		return isSearch;
	}

	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
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

	private View buildLayout(ViewHolder holder, View convertView) {// 绑定view
		convertView = getLayoutInflater().inflate(R.layout.adapter_main_item,
				null);
		holder.activie_vp = (ViewPager) convertView
				.findViewById(R.id.common_vp);
		holder.ll_vp = (LinearLayout) convertView.findViewById(R.id.ll_vp);

//		holder.ll_banner = (RelativeLayout) convertView
//				.findViewById(R.id.ll_banner);
		holder._title = (TextView) convertView
				.findViewById(R.id.adapter_main_item_tx_title);// 标题
		holder._price = (TextView) convertView
				.findViewById(R.id.adapter_main_item_tx_price);// 价钱
		holder._good = (TextView) convertView
				.findViewById(R.id.adapter_main_item_tx_good);// 赞
		// holder._week = (TextView) convertView
		// .findViewById(R.id.adapter_main_item_tx_weak);// 弱
		holder._comment = (TextView) convertView
				.findViewById(R.id.adapter_main_item_tx_comment);// 评论
		holder._img = (ImageView) convertView
				.findViewById(R.id.adapter_main_item_img);// 评论

		holder._site = (TextView) convertView
				.findViewById(R.id.tv_adapter_main_item_site);
		holder._tv_flag = (TextView) convertView
				.findViewById(R.id.tv_adapter_flag);
		holder._ll_adapter = (RelativeLayout) convertView
				.findViewById(R.id.ll_adapter_main_item);
		holder._time = (TextView)convertView
				.findViewById(R.id.tv_adapter_main_item_time);
		convertView.setTag(holder);
		return convertView;
	}

	private void setDataView(ViewHolder holder, final int position) {// 显示数据
		final MsgDetailBean item = mData.get(position);
		holder._title.setText(item.getTitle());
		holder._comment.setText(String.valueOf(item.getCommentcount()));
		// holder._good.setText(String.valueOf(item.getVotesp()));
		if (!TextUtils.isEmpty(item.getPrice())) {
 			holder._price.setText(String.valueOf(item.getPrice()));
		}
		if (item.getTimeout() == 2) {
			holder._tv_flag.setVisibility(View.VISIBLE);
		} else {
			holder._tv_flag.setVisibility(View.GONE);
		}
		imageLoader.displayImage(item.getImage(), holder._img, options,
				animateFirstListener);
		holder._site.setText(item.getSitename());
		holder._ll_adapter.setOnClickListener(new OnClickListener() {// 解决edittext冲突
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Bundle bundle = new Bundle();
						bundle.putSerializable("bean", item);
						IntentUtil.start_activity((Activity) context,
								MsgDetailActivity.class, bundle);
					}
				});
		if (!TextUtils.isEmpty(item.getHasVoteSp())
				&& item.getHasVoteSp().equals(userid)) {
			// holder._good.setSelected(true);
			holder._good.setText((String.valueOf(item.getVotesp())));
			holder._good.setEnabled(false);
            holder._good.setSelected(true);
		} else {
			holder._good.setText(String.valueOf(item.getVotesp()));
            holder._good.setSelected(false);

            holder._good.setEnabled(true);
		}
		holder._good.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				doVote("1", item.getId(), item, position);
			}
		});

		holder._comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				IntentUtil.start_activity((Activity) context,
						CommentActivity.class, new BasicNameValuePair("id",
								item.getId()), new BasicNameValuePair(
								"linkType", String.valueOf(item.getLinktype())));
			}
		});
		holder._time.setText(TimeUtil.getListTime(item.getCreatetime()));
	}

	static class ViewHolder {
		private TextView _title;
		private TextView _price;
		private TextView _site;
		private TextView _good;
		private TextView _time;
		// public TextView _week;
		private TextView _comment;
		private ImageView _img;
 		public View _ll_search;
		private RelativeLayout _ll_adapter;
		private TextView _tv_flag;
		private ViewPager activie_vp;
		private LinearLayout ll_vp;
		private ImageView iv_close_banner;
		private RelativeLayout ll_banner;
	}

	/**
	 * 赞
	 * 
	 */
	private void doVote(final String type, final String detail_id,
			MsgBaseBean bean, final int position) {
		if (LoginUtil.isAccountLogin((Activity) context)) {
			if (!bean.getHasVoteSp().equals(userid)) {
				new Thread() {
					@Override
					public void run() {
						CommonJson<Object> bean = null;
						bean = getDao().doVote(detail_id, type, "1");
						Message message = new Message();
						if (bean != null) {
							message.arg2 = Integer.parseInt(type);
							message.arg1 = position;
							if (bean.getStatus() == 1) {
								message.what = 2;
							} else if (bean.getStatus() == 0) {
								message.what = 3;
							}
							mHandler.sendMessage(message);
						} else {
							message.what = NETERR;
							mHandler.sendMessage(message);
						}
					}
				}.start();
			} else {
//				((MainActivity) context).showErr(R.string.hasvote);
			}
		}
	}

	/**
	 * 数据回调处理
	 */
	private Handler mHandler = new Handler() {
		@SuppressLint("ValidFragment")
		public void handleMessage(Message msg) {
			MsgDetailBean bean = getData().get(msg.arg1);
			switch (msg.what) {
			case 2:// 赞 弱 成功
					// dissmissDialog();
				if (msg.arg2 == 1) {
					bean.setHasVoteSp(userid);
					bean.setVotesp(bean.getVotesp() + msg.arg2);
				} else {

				}
				notifyDataSetChanged();
				SugarRecord.save(bean);
				break;
			case 3:// 赞 弱 失败
				bean.setHasVoteSp(userid);
				notifyDataSetChanged();
//				((MainActivity) context).showErr(R.string.hasvote);
				// Toast.makeText(MsgDetailActivity.this,
				// getString(R.string.hasvote), Toast.LENGTH_SHORT).show();
				SugarRecord.save(bean);
				break;

			case NETERR:
//				((MainActivity) context).showErr(R.string.timeout);
				break;
			}
		};
	};

 

 
}
