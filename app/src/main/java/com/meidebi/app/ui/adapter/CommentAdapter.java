package com.meidebi.app.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.service.bean.comment.CommentBean;
import com.meidebi.app.support.utils.content.ContentUtils;
import com.meidebi.app.support.utils.content.TimeUtil;
import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;
import com.meidebi.app.ui.submit.CommentActivity;
import com.meidebi.app.ui.widget.PointerPopupWindow;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CommentAdapter extends BaseRecyclerAdapter<CommentBean> {
    private boolean onClickable = false;
	public CommentAdapter(Context context, List<CommentBean> mList) {

		super(context, mList);
		initImageOptions(AppConfigs.AVANTAR_DEFAULT);
	}


	private void setDataView(ViewHolder holder, int position) {
		CommentBean bean = mData.get(position);
		if (bean.getReferto() != 0) {
            setFloorView(holder, bean);
 			setCommentView(holder, bean);
		} else {
             setFloorView(holder, bean);
		}

	}
    //引用层
	private void setCommentView(ViewHolder holder, CommentBean item) {// 显示二级回复
		holder._ll_quote.setVisibility(View.VISIBLE);

		if (!TextUtils.isEmpty(item.getListViewSpannableStringReferTo())) {
			holder._quote_content.setText(item.getListViewSpannableStringReferTo());
		} else {
			ContentUtils.addJustHighLightLinks(item);
			holder._quote_content.setText(item.getListViewSpannableStringReferTo());
		}
		// if (!TextUtils.isEmpty(item.getTonickname())) {
		// holder._commentToname.setVisibility(View.VISIBLE);
		// holder._commentToname.setText("@" + item.getRefernickname());
		// }
		// holder._commentfromtime.setText(TimeUtil.getListTime(item.getCreatetime()));
		holder._quote_name.setText("@" + item.getRefernickname());
		holder._quote_time.setText(TimeUtil.getListTime(item.getCreatetime()));
	}
    //主楼
	private void setFloorView(ViewHolder holder, CommentBean item) {// 显示一级回复
        holder._ll_quote.setVisibility(View.GONE);
        if(item.getStatus()==2){
            holder._floor_content.setText("该评论已屏蔽");
            holder._floor_content.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        }else if (!TextUtils.isEmpty(item.getListViewSpannableString())) {
			holder._floor_content.setText(item.getListViewSpannableString());
            holder._floor_content.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        } else {
			ContentUtils.addJustHighLightLinks(item);
			holder._floor_content.setText(item.getListViewSpannableString());
            holder._floor_content.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        }
		if (!TextUtils.isEmpty(item.getNickname())) {
			holder._floorname.setText(item.getNickname());
		} else {
			holder._floorname.setText("匿名");
		}
		if ( !item.getTouserid().equals("0")) {
			holder._ll_replay.setVisibility(View.VISIBLE);
 			holder._replayToname.setText("@" + item.getTonickname());
		}else{
			holder._ll_replay.setVisibility(View.GONE);
		}
		// holder._commentcount.setText(String.valueOf(item.getReplaycount()));
		holder._floortime.setText(TimeUtil.getListTime(item.getCreatetime()));
		imageLoader.displayImage(item.getPhoto(), holder._floorAvantar,
				options2, animateFirstListener);
	}


    @Override
    public BasicItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(R.layout.adapter_common_comment,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        setDataView((ViewHolder)holder,position);
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

    private View view;
    private TextView tv_replay,tv_use,tv_good;



    class ViewHolder extends  BasicItemViewHolder implements View.OnClickListener{
         //主楼
        @InjectView(R.id.iv_user_avantar) ImageView _floorAvantar;
        @InjectView(R.id.tv_user_name) TextView _floorname;
        @InjectView(R.id.tv_post_time) TextView _floortime;
        @InjectView(R.id.tv_adapter_comment_floor_content) TextView _floor_content;
        //回复
        @InjectView(R.id.ll_replay_to) LinearLayout _ll_replay;
        @InjectView(R.id.tv_adapter_comment_head_replyto) TextView _replayToname;

        //引用
        @InjectView(R.id.tv_adapter_comment_quote_name) TextView _quote_name;
        @InjectView(R.id.tv_adapter_comment_quote_time_ago) TextView _quote_time;
        @InjectView(R.id.ll_adapter_quoto) View _ll_quote;
        @InjectView(R.id.tv_adapter_quote_content) TextView _quote_content;
        @InjectView(R.id.sub_view) View subView;



        PointerPopupWindow p;

        public ViewHolder(View itemView) {
            super(itemView);
             ButterKnife.inject(this, itemView);
            _floortime.setVisibility(View.VISIBLE);
        }

        protected void setItemViewOnClick(int position){
            if(!onClickable) {
                if (p == null) {
                    p = create();
                    p.setAlignMode(PointerPopupWindow.AlignMode.CENTER_FIX);
                }

                p.showAsPointer(subView, -30, -30);
            }else{
                onClickable =false;
                ((CommentActivity)context).onClickListItem(null,"","");

            }
        }


        private PointerPopupWindow create() {
            //warning: you must specify the window width explicitly(do not use WRAP_CONTENT or MATCH_PARENT)
             p = new PointerPopupWindow(context, context.getResources().getDimensionPixelSize(R.dimen.popup_width));
            view = getLayoutInflater().inflate(R.layout.popup_comment, null);
            tv_replay = (TextView) view.findViewById(R.id.tv_pop_comment_reply);
            tv_use = (TextView) view.findViewById(R.id.tv_pop_comment_use);
            tv_good = (TextView) view.findViewById(R.id.tv_pop_comment_good);
             tv_replay.setOnClickListener(this);
            tv_use.setOnClickListener(this);
            tv_good.setOnClickListener(this);
             p.setContentView(view);
            p.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_comment_bg));
             p.setPointerImageRes(R.drawable.popup_comment_pointer);
            return p;
        }

        @Override
        public void onClick(View view) {
             switch (view.getId()){
                case R.id.tv_pop_comment_reply:
                    onClickable=true;
                    CommentBean bean = mData.get(getPosition());
                    ((CommentActivity)context).onClickListItem(bean,"",bean.getUserid());
                    break;
                case R.id.tv_pop_comment_use:
                    onClickable=true;
                    CommentBean bean2 = mData.get(getPosition());
                    ((CommentActivity)context).onClickListItem(bean2,bean2.getId(),"");

                    break;
                case R.id.tv_pop_comment_good:
                    break;

                 default:

                     break;
            }
            p.dismiss();
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(R.layout.listview_footer_layout_padding,parent,false);
        return new ViewFooter(v);
    }
}
