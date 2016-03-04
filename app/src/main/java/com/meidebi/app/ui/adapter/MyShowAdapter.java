package com.meidebi.app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.service.bean.msg.OrderShowBean;
import com.meidebi.app.support.utils.content.CommonUtil;
import com.meidebi.app.support.utils.content.TimeUtil;
import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by mdb-ii on 15-2-4.
 */
public class MyShowAdapter extends BaseRecyclerAdapter<OrderShowBean> {
    private final int NETERR = 404;
    private final int DATAERR = 400;
    private String userid = XApplication.getInstance().getAccountBean().getId();



    // List<MainListItemBean> mList =new ArrayList<MainListItemBean>();



    public MyShowAdapter(Context context, List<OrderShowBean> mList) {
        super(context, mList);
    }




    private void setDataView(ViewHolder holder, final int position) {// 显示数据
        final OrderShowBean item = getItem(position);
        if(item!=null) {
            holder._title.setText(item.getTitle());
            // holder._good.setText(String.valueOf(item.getVotesp()));


//            holder._site.setText(item.getSitename());
//            holder._comment.setText(String.valueOf(item.getCommentcount()));

//            if (!TextUtils.isEmpty(item.getHasVoteSp())
//                    && item.getHasVoteSp().equals(userid)) {
//                // holder._good.setSelected(true);
//                holder._good.setText((String.valueOf(item.getVotesp())));
//                holder._good.setEnabled(false);
//                holder._good.setSelected(true);
//            } else {
//                holder._good.setText(String.valueOf(item.getVotesp()));
//                holder._good.setSelected(false);
//
//                holder._good.setEnabled(true);
//            }
//            CommonUtil.formatTextView(holder._souce,item.getSitename());
            CommonUtil.formatTextView(holder._time,TimeUtil.getListTime(item.getCreatetime()),R.string.pre_my_show_time);


//            holder._time.setText(TimeUtil.getListTime(item.getCreatetime()));

//            holder._good.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    // TODO Auto-generated method stub
//                    doVote("1", item.getId(), item, position);
//                }
//            });
//
//            holder._comment.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    // TODO Auto-generated method stub
//                    IntentUtil.start_activity((Activity) context,
//                            CommentActivity.class, new BasicNameValuePair("id",
//                                    item.getId()), new BasicNameValuePair(
//                                    "linkType", String.valueOf(item.getLinktype())));
//                }
//            });
            if(item.getDevicetype()==0){
                CommonUtil.formatTextView(holder._souce,"网站",R.string.pre_my_show_source);
            }else{
                CommonUtil.formatTextView(holder._souce,"手机",R.string.pre_my_show_source);

            }

                if (item.getUnchecked() == 0 && item.getStatus() == 0) {//审核未通过
                    CommonUtil.formatTextView(holder._tv_status, "审核未通过", R.string.pre_my_show_status);

                } else if (item.getUnchecked() == 1) {//未审核 unchecked＝1
                    CommonUtil.formatTextView(holder._tv_status, "审核中", R.string.pre_my_show_status);

                } else if (item.getUnchecked() == 0 && item.getStatus() == 1) {//审核通过
                    CommonUtil.formatTextView(holder._tv_status, "审核通过", R.string.pre_my_show_status);
                }
                imageLoader.displayImage(item.getPic(), holder._img, options,
                        animateFirstListener);

        }
    }





    @Override
    public BasicItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(R.layout.adapter_my_ordershow,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        setDataView((ViewHolder)holder,position);
    }

    class ViewHolder extends BasicItemViewHolder {
        @InjectView(R.id.adapter_main_item_tx_title)
        TextView _title;
//        @InjectView(R.id.adapter_main_item_tx_price)
//        TextView _price;
        @InjectView(R.id.tv_adapter_my_show_source)
        TextView _souce;

        @InjectView(R.id.tv_adapter_main_item_time)
        TextView _time;
//        @InjectView(R.id.adapter_main_item_tx_good)
//        TextView _good;
//        @InjectView(R.id.adapter_main_item_tx_comment)
//        TextView _comment;
        @InjectView(R.id.adapter_main_item_img)
        ImageView _img;


        @InjectView(R.id.tv_adapter_my_show_status)
        TextView _tv_status;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }


    }



}
