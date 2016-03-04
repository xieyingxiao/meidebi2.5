package com.meidebi.app.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.base.MsgBaseBean;
import com.meidebi.app.service.bean.msg.OrderShowBean;
import com.meidebi.app.service.dao.detail.MsgDetailDao;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;
import com.meidebi.app.ui.msgdetail.OrderShowDetailActivity;
import com.meidebi.app.ui.submit.CommentActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.orm.SugarRecord;

import org.apache.http.message.BasicNameValuePair;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OrderShowRecylerAdapter extends BaseRecyclerAdapter<OrderShowBean> {
    private final int NETERR = 404;
    private boolean isSearch = false;
    private String userid = XApplication.getInstance().getAccountBean().getId();
    private MsgDetailDao dao;
     int rowLayout;
    public MsgDetailDao getDao() {
        if (dao == null) {
            dao = new MsgDetailDao((Activity) context);
        }
        return dao;
    }

    public OrderShowRecylerAdapter(Context context, List<OrderShowBean> mList, int res) {
        super(context, mList);
        this.rowLayout = res;
        initImageOptions(R.drawable.iv_no_avantar);
        options =  new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.iv_order_show_no_img)
                .showImageOnLoading(AppConfigs.Loading_List_Img_Bg_Small)
                .showImageOnFail(AppConfigs.Loading_List_Img_Bg_Small)

                .cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)

                .build();
    }


    public boolean isSearch() {
        return isSearch;
    }

    public void setSearch(boolean isSearch) {
        this.isSearch = isSearch;
    }


    private void setDataView(final ViewHodler holder, final int position) {// 显示数据
        final OrderShowBean item = mData.get(position);
        holder._title.setText(item.getTitle());
        holder._comment.setText(String.valueOf(item.getCommentcount()));
        holder._good.setText(String.valueOf(item.getVotesp()));
        holder._tv_writer.setText(item.getName());
        // holder._week.setText(String.valueOf(item.getVotem()));
//        holder._time.setText(TimeUtil.getListTime(item.getCreatetime()));

            imageLoader.displayImage(item.getCover(), holder._img, options, animateFirstListener);
        AppLogger.e(item.getId()+item.getTitle()+item.getCover());
         holder._content.setText(item.getContent());
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
        holder._good.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                doVote("1", item.getId(), item, position);
            }
        });
        holder._comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                IntentUtil.start_activity((Activity) context,
                        CommentActivity.class, new BasicNameValuePair("id",
                                item.getId()), new BasicNameValuePair(
                                "linkType", String.valueOf(item.getLinktype())));
            }
        });
         holder.itemView.setTag(item);
    }


    /**
     * 赞
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
//                ((MainActivity) context).showErr(R.string.hasvote);
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
        }

        ;
    };

    @Override
    public BasicItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(rowLayout,parent,false);
        return new ViewHodler(v);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        setDataView((ViewHodler)holder,position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(R.layout.listview_footer_layout_padding,parent,false);
        return new ViewFooter(v);
    }
    FrameLayout.LayoutParams cover_params;

    class ViewHodler extends BasicItemViewHolder  {
        @InjectView(R.id.adapter_title)
        TextView _title;
        @InjectView(R.id.adapter_like)
        TextView _good;
//        @InjectView(R.id.adapter_post_time)
//        TextView _time;
        @InjectView(R.id.adapter_comment)
        TextView _comment;
        @InjectView(R.id.adapter_image)
        ImageView _img;
        @InjectView(R.id.tv_user_name)
        TextView _tv_writer;
        @InjectView(R.id.iv_user_avantar)
        ImageView _iv_writer;
        @InjectView(R.id.adapter_content)
        TextView _content;



        public ViewHodler(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            if(cover_params==null) {
                cover_params = (FrameLayout.LayoutParams) _img.getLayoutParams();
                cover_params.height = AppConfigs.getCoverHeight();
            }
            _img.setLayoutParams(cover_params);

        }


           @Override
           protected void setItemViewOnClick(int position) {
               Bundle bundle =  new Bundle();
               bundle.putSerializable("bean", getData().get(getPosition()));
               IntentUtil.start_activity((Activity)context,OrderShowDetailActivity.class,bundle);
            }
       }



}
