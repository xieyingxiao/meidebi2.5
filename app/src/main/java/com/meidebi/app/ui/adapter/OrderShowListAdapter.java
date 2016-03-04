package com.meidebi.app.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.base.MsgBaseBean;
import com.meidebi.app.service.bean.msg.OrderShowBean;
import com.meidebi.app.service.dao.detail.MsgDetailDao;
import com.meidebi.app.support.utils.Utility;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.support.utils.content.ContentUtils;
import com.meidebi.app.support.utils.content.TimeUtil;
import com.meidebi.app.ui.adapter.base.BaseArrayAdapter;
 import com.meidebi.app.ui.submit.CommentActivity;
import com.orm.SugarRecord;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OrderShowListAdapter extends BaseArrayAdapter<OrderShowBean> {
	// List<MainListItemBean> mList =new ArrayList<MainListItemBean>();
	private final int NETERR = 404;
	private boolean isSearch = false;
	private String userid = XApplication.getInstance().getAccountBean().getId();
	private MsgDetailDao dao;
 	private int img_height;

	public MsgDetailDao getDao() {
		if (dao == null) {
			dao = new MsgDetailDao((Activity) context);
		}
		return dao;
	}

	public OrderShowListAdapter(Context context, List<OrderShowBean> mList) {
		super(context, mList);
		initImageOptions(R.drawable.iv_no_avantar);
		double size = (double) (Math
				.round(Utility.getScreenWidth(context) * 100.0 / 640) / 100.0);
		img_height = (int) (350 * size);
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
            convertView = getLayoutInflater().inflate(
                R.layout.card_showorder, parent,false);
			holder = new ViewHolder(convertView);
            holder._img.setLayoutParams(new LayoutParams(Utility
                    .getScreenWidth(context), img_height));
            convertView.setTag(holder);
 		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		setDataView(holder, position);
		return convertView;
	}


	private void setDataView(final ViewHolder holder, final int position) {// 显示数据
		final OrderShowBean item = mData.get(position);
		holder._title.setText(item.getTitle());
		holder._comment.setText(String.valueOf(item.getCommentcount()));
		holder._good.setText(String.valueOf(item.getVotesp()));
		holder._tv_writer.setText(item.getName());
		// holder._week.setText(String.valueOf(item.getVotem()));
		holder._time.setText(TimeUtil.getListTime(item.getCreatetime()));
		imageLoader.displayImage(item.getCover(), holder._img, options,
				animateFirstListener);
		holder._content.setText(ContentUtils.getTextFromHtml(item.getContent()));
		imageLoader.displayImage(item.getHeadphoto(), holder._iv_writer,
				options2, animateFirstListener);
		if (!TextUtils.isEmpty(item.getHasVoteSp())
				&& item.getHasVoteSp().equals(userid)) {
            holder._good.setText((String.valueOf(item.getVotesp())));

            holder._good.setEnabled(false);
            holder._good.setSelected(true);

        } else {
            holder._good.setText((String.valueOf(item.getVotesp())));

            holder._good.setEnabled(true);
            holder._good.setSelected(false);

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
	}

	static class ViewHolder {
        @InjectView(R.id.adapter_title)
        TextView _title;
        @InjectView(R.id.adapter_like)
        TextView _good;
        @InjectView(R.id.adapter_post_time)
        TextView _time;
        @InjectView(R.id.adapter_comment)
        TextView _comment;
        @InjectView(R.id.adapter_image)
        ImageView _img;
        @InjectView(R.id.adapter_user_name)
        TextView _tv_writer;
        @InjectView(R.id.adapter_user_avatar)
        ImageView _iv_writer;
        @InjectView(R.id.adapter_content)
        TextView _content;
        @InjectView(R.id.ll_cover)
        RelativeLayout _ll_cover;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);

        }
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
						bean = getDao().doVote(detail_id, type, "3");
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
			OrderShowBean bean = getData().get(msg.arg1);
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
				bean.setVotesp(bean.getVotesp() + msg.arg2);
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
