package com.meidebi.app.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.base.MsgBaseBean;
import com.meidebi.app.service.bean.msg.MsgDetailBean;
import com.meidebi.app.service.dao.detail.MsgDetailDao;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.support.utils.content.CommonUtil;
import com.meidebi.app.support.utils.content.TimeUtil;
import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;
import com.orm.SugarRecord;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by mdb-ii on 15-2-4.
 */
public class MyNewsAdapter extends BaseRecyclerAdapter<MsgDetailBean> {
    private final int NETERR = 404;
    private final int DATAERR = 400;
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

    public MyNewsAdapter(Context context, List<MsgDetailBean> mList) {
        super(context, mList);
    }

    public boolean isSearch() {
        return isSearch;
    }

    public void setSearch(boolean isSearch) {
        this.isSearch = isSearch;
    }



    private void setDataView(ViewHolder holder, final int position) {// 显示数据
        final MsgDetailBean item = getItem(position);
             holder._title.setText(item.getTitle());
            // holder._good.setText(String.valueOf(item.getVotesp()));
            if (!TextUtils.isEmpty(item.getPrice())) {
                CommonUtil.formatTextView(holder._price,String.valueOf(item.getPrice()),R.string.pre_price_cn_sy);
            } else {
            }
            if (item.getTimeout() == 2) {
                holder._tv_flag.setVisibility(View.VISIBLE);
            } else {
                holder._tv_flag.setVisibility(View.GONE);
            }
            imageLoader.displayImage(item.getImage(), holder._img, options,
                    animateFirstListener);
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
//        if(item.getDevicetype()==1){
//            CommonUtil.formatTextView(holder._souce,"网站",R.string.pre_my_show_source);
//            holder._tv_mobile.setVisibility(View.GONE);
//            holder._img.setVisibility(View.VISIBLE);
//        }else{
//            CommonUtil.formatTextView(holder._souce,"手机",R.string.pre_my_show_source);
//            holder._tv_mobile.setVisibility(View.VISIBLE);
//            holder._img.setVisibility(View.GONE);
//        }

        CommonUtil.formatTextView(holder._site,item.getSitename(),R.string.pre_site);
            CommonUtil.formatTextView(holder._time,TimeUtil.getListTime(item.getCreatetime()),R.string.pre_my_new_time);
            CommonUtil.formatTextView(holder._tv_status,item.getStatus()==1?"正常":"待审核",R.string.pre_status);

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
        }

        ;
    };

    @Override
    public BasicItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(R.layout.adapter_my_news,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        setDataView((ViewHolder)holder,position);
    }

    class ViewHolder extends BasicItemViewHolder {
        @InjectView(R.id.adapter_main_item_tx_title)
        TextView _title;
        @InjectView(R.id.adapter_main_item_tx_price)
        TextView _price;
        @InjectView(R.id.tv_adapter_main_item_site)
        TextView _site;

        @InjectView(R.id.tv_adapter_main_item_time)
        TextView _time;
//        @InjectView(R.id.adapter_main_item_tx_good)
//        TextView _good;
//        @InjectView(R.id.adapter_main_item_tx_comment)
//        TextView _comment;
        @InjectView(R.id.adapter_main_item_img)
        ImageView _img;

        @InjectView(R.id.tv_adapter_flag)
        TextView _tv_flag;
        @InjectView(R.id.tv_adapter_my_news_status)
        TextView _tv_status;
        @InjectView(R.id.adapter_news_from_mobile)
        TextView _tv_mobile;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }


    }

}
