package com.meidebi.app.ui.adapter;

import java.util.HashMap;
import java.util.List;

import com.meidebi.app.R;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.service.bean.user.MsgCenterBean;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.content.TimeUtil;
import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MsgCenterAdapter extends BaseRecyclerAdapter<MsgCenterBean>{
	private int unReadColour;
	private int ReadedColour;
	private HashMap<String, Boolean> unReadList;

	public HashMap<String, Boolean> getUnReadList() {
		return unReadList;
	}

	public void setUnReadList(HashMap<String, Boolean> unReadList) {
		this.unReadList = unReadList;
	}

	public MsgCenterAdapter(Context context, List<MsgCenterBean> mList) {
		super(context, mList);
		unReadColour = context.getResources().getColor(R.drawable.link_colour);
		ReadedColour = context.getResources().getColor(R.color.text_gery_color);
		initImageOptions(AppConfigs.AVANTAR_DEFAULT);
	}


	private void setDataView(ViewHolder holder, int position) {
		MsgCenterBean bean = mData.get(position);
		// if (bean.getReferto() != 0) {
		// setHeadView(holder, bean);
		// holder._ll_comment.setVisibility(View.VISIBLE);
		setCommentView(holder, bean);
		// } else {
		// holder._ll_comment.setVisibility(View.GONE);
		setHeadView(holder, bean);
		// }
		// if(bean.getIsLast()){
		// holder._listDivder.setVisibility(View.VISIBLE);
		// }else{
		// holder._listDivder.setVisibility(View.GONE);
		// }
	}

	private void setCommentView(ViewHolder holder, MsgCenterBean item) {// 显示二级回复
		if (!TextUtils.isEmpty(item.getListViewSpannableString())) {
			holder._comment.setText(item.getListViewSpannableString());
			if (item.isIsread() == 0) {
				if (unReadList == null) {
					unReadList = new HashMap<String, Boolean>();
				}
				holder._comment.setTextColor(unReadColour);
				if (unReadList.containsKey(item.getId())) {
					item.setIsread(1);
                    unReadList.put(item.getId(), true);
                    holder._comment.setTextColor(ReadedColour);
				} else {
					item.setIsread(1);
					unReadList.put(item.getId(), true);
 					holder._comment.setTextColor(unReadColour);
				}
			} else {
				holder._comment.setTextColor(ReadedColour);
			}
		}
        if(item.getRelatenickname()!=null&&item.getRelatenickname().equals("admin")){
            holder._comment_type.setText("[系统消息]");
        }else{
            holder._comment_type.setText("[互动提醒]");
        }
 	}

	private void setHeadView(ViewHolder holder, MsgCenterBean item) {// 显示一级回复


		holder._headtime.setText(TimeUtil.getListTime(item.getCreatetime()));
 
	}

    @Override
    public BasicItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(R.layout.adapter_user_msg_center,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        setDataView((ViewHolder)holder,position);
    }



    class ViewHolder extends BasicItemViewHolder  {
        @InjectView(R.id.tv_adapter_comment_content) TextView _comment;
        // 回复内容

        @InjectView (R.id.tv_adapter_comment_head_time_ago)TextView _headtime;

        @InjectView (R.id.tv_adapter_comment_type)TextView _comment_type;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }


    }



}
