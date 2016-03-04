package com.meidebi.app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.service.bean.detail.CouponBean;
import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/*
 * 优惠券Adapter
 */
public class CouponListAdapter extends BaseRecyclerAdapter<CouponBean> {
    private ListView lv;

    public CouponListAdapter( Context context, List<CouponBean> objects) {
        super(context, objects);
        setUseLoadMore(false);
        // TODO Auto-generated constructor stub
 //        this.lv = lv;
    }

    @Override
    public boolean useHeader() {
        return false;
    }


    private void setDataView(final ViewHolder holder, final int position) {// 显示数据
        final CouponBean item = getItem(position);
        imageLoader.displayImage(item.getImgUrl(), holder.iv, options, animateFirstListener);
// 		holder.tv_site.setText(item.getSitename());
        holder.tv_title.setText(item.getJian());
        holder.tv_content.setText("满" + item.getMan() + "使用");

//		imageLoader.displayImage(item.getImgUrl(), holder.iv, options, animateFirstListener);
//        boolean isChecked = lv.isItemChecked(position);
//        if (isChecked) {
//            holder.iv_cursor.setVisibility(View.VISIBLE);
//        } else {
//            holder.iv_cursor.setVisibility(View.GONE);
//        }

     }

    @Override
    public BasicItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(R.layout.adapter_couponlist,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(R.layout.listview_footer_layout_padding,parent,false);
        return new ViewFooter(v);
    }

    public class ViewFooter extends RecyclerView.ViewHolder {
        @InjectView(R.id.listview_footer) TextView tv;
        @InjectView(R.id.refresh)
        CircularProgressBar refreshView;

        public ViewFooter(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
            refreshView.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
        }
    }
    @Override
    public void onBindFooterView(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        setDataView((ViewHolder)holder,position);
    }

    class ViewHolder extends BasicItemViewHolder {
        @InjectView(R.id.iv_adapter_coupon_list)
        ImageView iv;
        @InjectView(R.id.tv_coupon_value)
        TextView tv_title;
        @InjectView(R.id.tv_coupon_des)
        TextView tv_content;

//        @InjectView(R.id.ll_adapter_coupon)
//        LinearLayout ll;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }


    }
}