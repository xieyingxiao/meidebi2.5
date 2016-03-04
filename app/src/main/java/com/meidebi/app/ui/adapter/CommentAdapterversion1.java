package com.meidebi.app.ui.adapter;

import java.util.List;

import com.meidebi.app.R;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meidebi.app.service.bean.comment.CommentBean;
import com.meidebi.app.support.utils.content.ContentUtils;
import com.meidebi.app.support.utils.content.TimeUtil;
import com.meidebi.app.ui.adapter.base.BaseArrayAdapter;

public class CommentAdapterversion1 extends BaseArrayAdapter<CommentBean> {

	public CommentAdapterversion1(Context context, List<CommentBean> mList) {

		super(context, mList);
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
		convertView = getLayoutInflater().inflate(
				R.layout.adapter_common_comment, null);
		// 评论
		holder._comment = (TextView) convertView
				.findViewById(R.id.tv_adapter_comment_content);// 评论内容
		holder._writename = (TextView) convertView
				.findViewById(R.id.tv_adapter_comment_writer_name);// 评论内容
		holder._commenttime = (TextView) convertView
				.findViewById(R.id.tv_adapter_comment_time_ago);// 评论内容
		holder._commentToname = (TextView) convertView
				.findViewById(R.id.tv_adapter_comment_time_ago);
		holder._ll_comment = (LinearLayout) convertView
				.findViewById(R.id.ll_adapter_comment_content);

		// 评论原话题
		holder._head_content = (TextView) convertView
				.findViewById(R.id.tv_adapter_comment_head_content);// 回复内容
		holder._headtime = (TextView) convertView
				.findViewById(R.id.tv_adapter_comment_head_time_ago);// 回复时间
		holder._headname = (TextView) convertView
				.findViewById(R.id.tv_adapter_comment_head_writer_name);// 回复内容
		holder._headToname = (TextView) convertView
				.findViewById(R.id.tv_adapter_comment_head_replyto);// 回复对象
		holder._ll_head = (RelativeLayout) convertView
				.findViewById(R.id.ll_tv_adapter_comment_head);// 回复layout
		holder._listDivder = (View)convertView
				.findViewById(R.id.adapter_comment_listdvider);// 回复对象
//		holder._commentcount = (TextView)convertView
//				.findViewById(R.id.tv_adapter_comment_head_count);// 回复条数
		convertView.setTag(holder);

		// //评论2
		// holder._ll_comment_content2 = (LinearLayout) convertView
		// .findViewById(R.id.ll_adapter_content2);// 评论内容
		// holder._comment2 = (TextView) convertView
		// .findViewById(R.id.tv_adapter_comment_content2);// 评论内容
		// holder._writename2= (TextView) convertView
		// .findViewById(R.id.tv_adapter_comment_writer_name2);// 评论内容
		// holder._commenttime2 = (TextView) convertView
		// .findViewById(R.id.tv_adapter_comment_time_ago2);// 评论内容
		// holder._commentToname2 =(TextView) convertView
		// .findViewById(R.id.tv_adapter_comment_headto2);
		return convertView;
	}

	private void setDataView(ViewHolder holder, int position) {
		CommentBean bean = mData.get(position);
		if(bean.getIsParent()){
			setHeadView(holder, bean);
			holder._ll_comment.setVisibility(View.GONE);
		}else{
			holder._ll_head.setVisibility(View.GONE);
			setCommentView(holder, bean);
		}
		if(bean.getIsLast()){
			holder._listDivder.setVisibility(View.VISIBLE);
		}else{
			holder._listDivder.setVisibility(View.GONE);
		}
	}

	private void setCommentView(ViewHolder holder, CommentBean item) {// 显示二级回复
		holder._ll_comment.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(item.getListViewSpannableString())) {
			holder._comment.setText(item.getListViewSpannableString());
		} else {
			ContentUtils.addJustHighLightLinks(item);
			holder._comment.setText(item.getListViewSpannableString());
		}
		if (!TextUtils.isEmpty(item.getTonickname())) {
			holder._commentToname.setVisibility(View.VISIBLE);
			holder._commentToname.setText("@" + item.getTonickname());
		}

		holder._writename.setText(item.getNickname());
		holder._commenttime.setText(TimeUtil.getListTime(item.getCreatetime()));
	}

	private void setHeadView(ViewHolder holder, CommentBean item) {// 显示一级回复
		holder._ll_head.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(item.getListViewSpannableString())) {
			holder._head_content.setText(item.getListViewSpannableString());
		} else {
			ContentUtils.addJustHighLightLinks(item);
			holder._head_content.setText(item.getListViewSpannableString());
		}
		holder._headname.setText(item.getNickname());
		if (item.getReferto()!=0) {
			holder._headToname.setVisibility(View.VISIBLE);
			holder._headToname.setText("@" + item.getRefernickname());
		}
		holder._commentcount.setText(String.valueOf(item.getReplaycount()));
 
		holder._headtime.setText(TimeUtil.getListTime(item.getCreatetime()));
	}

	// private void setCommentView2(ViewHolder holder, CommentBean item) {//
	// 显示数据
	// holder._ll_comment_content2.setVisibility(View.VISIBLE);
	// if (!TextUtils.isEmpty(item.getListViewSpannableString())) {
	// holder._comment2.setText(item.getListViewSpannableString());
	// } else {
	// Emoticons.addJustHighLightLinks(item);
	// holder._comment2.setText(item.getListViewSpannableString());
	// }
	// if (!TextUtils.isEmpty(item.getTonickname())) {
	// holder._commentToname2.setVisibility(View.VISIBLE);
	// holder._commentToname2.setText("@"+item.getTonickname());
	// }
	// holder._writename2.setText(item.getNickname());
	// holder._commenttime2.setText(TimeUtil.getListTime(item.getCreatetime()));
	// }

	// private void setReplyView(ViewHolder holder, CommentBean item) {// 显示数据
	// holder._ll_head.setVisibility(View.VISIBLE);
	// if (!TextUtils.isEmpty(item.getListViewSpannableString())) {
	// holder._head_content.setText(item.getListViewSpannableString());
	// } else {
	// Emoticons.addJustHighLightLinks(item);
	// holder._head_content.setText(item.getListViewSpannableString());
	// }
	// holder._headname.setText(item.getNickname());
	// if (!TextUtils.isEmpty(item.getTonickname())) {
	// holder._headToname.setVisibility(View.VISIBLE);
	// holder._headToname.setText("@"+item.getTonickname());
	// }
	// holder._headtime.setText(TimeUtil.getListTime(item.getCreatetime()));
	// }

	static class ViewHolder {
		// 评论原内容
		private TextView _comment;
		private TextView _writename;
		private TextView _commenttime;
		private TextView _commentToname;
		private TextView _commentcount;

		private LinearLayout _ll_comment;
		// 回复内容
		private RelativeLayout _ll_head;
		private TextView _head_content;
		private TextView _headtime;
		private TextView _headname;
		private TextView _headToname;
		private View _listDivder;

		// //评论原内容2
		// public LinearLayout _ll_comment_content2;
		// public TextView _comment2;
		// public TextView _writename2;
		// public TextView _commenttime2;
		// public TextView _commentToname2;
	}

}
