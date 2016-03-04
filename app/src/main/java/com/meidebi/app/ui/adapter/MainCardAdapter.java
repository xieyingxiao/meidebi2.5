package com.meidebi.app.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.service.bean.BannerBean;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.base.MsgBaseBean;
import com.meidebi.app.service.bean.msg.MsgDetailBean;
import com.meidebi.app.service.dao.detail.MsgDetailDao;
import com.meidebi.app.support.utils.Utility;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.support.utils.content.CommonUtil;
import com.meidebi.app.support.utils.content.TimeUtil;
import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;
import com.meidebi.app.ui.browser.BrowserWebActivity;
import com.meidebi.app.ui.main.SingleChanelListFragment;
import com.meidebi.app.ui.submit.CommentActivity;
import com.meidebi.app.ui.view.LoopViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.orm.SugarRecord;

import org.apache.http.message.BasicNameValuePair;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainCardAdapter extends BaseRecyclerAdapter<MsgDetailBean>  {
    private final int NETERR = 404;
    private final int DATAERR = 400;
    private String userid = XApplication.getInstance().getAccountBean().getId();

    public List<BannerBean> getList_bannner() {
        return list_bannner;
    }

    public void setList_bannner(List<BannerBean> list_bannner) {
        this.list_bannner = list_bannner;
    }

    private List<BannerBean> list_bannner;

    private Fragment fragment;
    // List<MainListItemBean> mList =new ArrayList<MainListItemBean>();
    private MsgDetailDao dao;

    public MsgDetailDao getDao() {
        if (dao == null) {
            dao = new MsgDetailDao((Activity) context);
        }
        return dao;
    }

     private boolean isSearch = false;

    public MainCardAdapter(Fragment fragment, List<MsgDetailBean> mList) {
         super(fragment.getActivity(), mList);
        this.fragment = fragment;
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
                holder._comment.setText(String.valueOf(item.getCommentcount()));
                // holder._good.setText(String.valueOf(item.getVotesp()));
                if (!TextUtils.isEmpty(item.getPrice())) {
                    holder._price.setVisibility(View.VISIBLE);

                    CommonUtil.formatTextView(holder._price,String.valueOf(item.getPrice()),R.string.pre_price_cn_sy);
                 } else {
                    holder._price.setVisibility(View.GONE);
                 }
                if (item.getTimeout() == 2) {
                    holder._tv_flag.setVisibility(View.VISIBLE);
                } else {
                    holder._tv_flag.setVisibility(View.GONE);
                }
             if (holder._img.getTag() == null ||
                !holder._img.getTag().equals(item.getImage())) {
                imageLoader.displayImage(item.getImage(), holder._img, options,
                    animateFirstListener);
                 holder._img.setTag(item.getImage());
            }



                holder._site.setText(item.getSitename());

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
                holder._time.setText(TimeUtil.getListTime(item.getCreatetime()));

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
        View v =getLayoutInflater().inflate(R.layout.adapter_main_item,parent,false);
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
        @InjectView(R.id.adapter_main_item_tx_good)
        TextView _good;
        @InjectView(R.id.tv_adapter_main_item_time)
        TextView _time;
        @InjectView(R.id.adapter_main_item_tx_comment)
        TextView _comment;
        @InjectView(R.id.adapter_main_item_img)
        ImageView _img;

        @InjectView(R.id.tv_adapter_flag)
        TextView _tv_flag;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(R.layout.layout_vp_banner,parent,false);
        return new ViewHeader(v);
    }

    @Override
    public void onBindHeaderView(RecyclerView.ViewHolder holder, int position) {
        setBannerView((ViewHeader) holder);
    }

        private void setBannerView(ViewHeader header) {
     }


      class ViewHeader extends RecyclerView.ViewHolder implements ViewPager.OnPageChangeListener{
        @InjectView(R.id.common_vp)
        LoopViewPager activie_vp;
        @InjectView(R.id.ll_vp) LinearLayout ll_vp;
         @InjectView(R.id.ll_banner)
         FrameLayout ll_banner;
          private BannerAdapter adapter = new BannerAdapter();
        private int vp_height;
          private ImageView[] points;
          private int currentIndex;


        public ViewHeader(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
            double size = (double) (Math
                    .round(Utility.getScreenWidth(context) * 100.0 / 640) / 100.0);
            vp_height = (int) (234 * size);
            activie_vp.setLayoutParams(new FrameLayout.LayoutParams(Utility
                    .getScreenWidth(context), vp_height));
            activie_vp.setOnPageChangeListener(this);
            ll_vp.setVisibility(View.VISIBLE);
            ll_banner.setVisibility(View.VISIBLE);
            activie_vp.setVisibility(View.VISIBLE);
             activie_vp.setAdapter(adapter);
            activie_vp.setAdapter(adapter);
             activie_vp.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ((SingleChanelListFragment)fragment).mPullToRefreshLayout.setEnabled(false);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                            ((SingleChanelListFragment)fragment).mPullToRefreshLayout.setEnabled(true);
                            break;
                    }
                    return false;
                }
            });
            initPoint();

        }

          @Override
          public void onPageScrollStateChanged(int arg0) {
              // TODO Auto-generated method stub

          }

          @Override
          public void onPageScrolled(int arg0, float arg1, int arg2) {
              // TODO Auto-generated method stub

          }

          @Override
          public void onPageSelected(int positon) {
              // TODO Auto-generated method stub
              setCurDot(positon);
          }

          /**
           * 设置当前的小点的位置
           */
          private void setCurDot(int positon) {
              if (positon < 0 || positon > list_bannner.size() - 1
                      || currentIndex == positon) {
                  return;
              }
              points[positon].setEnabled(false);
              points[currentIndex].setEnabled(true);
              currentIndex = positon;
          }



          @SuppressLint("NewApi")
          private void initPoint() {
              points = new ImageView[list_bannner.size()];
              ll_vp.setGravity(Gravity.CENTER_VERTICAL);
              ll_vp.removeAllViews();
              // 循环取得小点图片
              for (int i = 0; i < list_bannner.size(); i++) {
                  // 得到一个LinearLayout下面的每一个子元素
                  ImageView iv = new ImageView(XApplication.getInstance());
                  iv.setTag(i);
                  iv.setImageResource(R.drawable.iv_guide_point_sel);
                  iv.setPadding(10, 10, 10, 0);

                  ll_vp.addView(iv);
                  points[i] = iv;
                  // 默认都设为灰色
                  points[i].setEnabled(true);
                  // 给每个小点设置监听
                  // points[i].setOnClickListener(this);
                  // 设置位置tag，方便取出与当前位置对应
                  points[i].setTag(i);
              }

              // 设置当面默认的位置
              currentIndex = 0;
              // 设置为白色，即选中状态
              points[currentIndex].setEnabled(false);
          }

    }

    public class BannerAdapter extends PagerAdapter {
        private DisplayImageOptions options;


        public BannerAdapter() {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(AppConfigs.Loading_List_Img_Bg_Small)
                            // .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true).cacheOnDisc(true)
                    .considerExifParams(true).build();
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            View view = LayoutInflater.from(XApplication.getInstance())
                    .inflate(R.layout.hd_adapter_main_vp, null);
            final ImageView iv = (ImageView) view
                    .findViewById(R.id.iv_adapter_main_vp);
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                         Bundle bundle = new Bundle();
                        bundle.putSerializable(
                                "url",
                                list_bannner.get(position).getLink());


                        bundle.putSerializable("title",
                                list_bannner.get(position).getTitle());
                        IntentUtil.start_activity((Activity)context,
                                BrowserWebActivity.class, bundle);
                    }
                }
            );


            imageLoader.displayImage(list_bannner.get(position).getImgUrl(), iv,
                    options, animateFirstListener);
            container.addView(view);
            return view;

        }

        @Override
        public int getCount() {
            return list_bannner.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }


}
