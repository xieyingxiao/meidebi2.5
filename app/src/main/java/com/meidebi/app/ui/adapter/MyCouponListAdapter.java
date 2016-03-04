package com.meidebi.app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.service.bean.detail.CouponBean;
import com.meidebi.app.support.utils.content.CommonUtil;
import com.meidebi.app.support.utils.content.ContentUtils;
import com.meidebi.app.support.utils.content.TimeUtil;
import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyCouponListAdapter extends BaseRecyclerAdapter<CouponBean> {
	public MyCouponListAdapter(Context context, List<CouponBean> objects) {
		super(context, objects);
		// TODO Auto-generated constructor stub
	}
	

     private void setDataView(final ViewHolder holder, final int position) {// 显示数据
         CouponBean item = mData.get(position);
		imageLoader.displayImage(item.getImgUrl(), holder.iv, options, animateFirstListener);
		holder.tv_title.setText(item.getTitle());
        if(!TextUtils.isEmpty(ContentUtils.replaceBlank(item.getPass()))){
			holder.tv_pass.setVisibility(View.VISIBLE);
            CommonUtil.formatTextView(holder.tv_pass,item.getPass(),"密码:%s");
 		}else{
			holder.tv_pass.setVisibility(View.GONE);
		}
         if(item.getTimeout()==1){
             holder.tv_go.setBackgroundResource(R.color.text_dark_gery_color);
             holder.tv_go.setText("已过期");

         }else{
             holder.tv_go.setBackgroundResource(R.color.app_main_color);
             holder.tv_go.setText("去使用");
         }
         CommonUtil.formatTextView(holder.tv_content,item.getCard(),"券号:%s");
         CommonUtil.formatTextView(holder.tv_start, TimeUtil.getDate(item.getUsestart()),R.string.msg_coupon_use_time);
         CommonUtil.formatTextView(holder.tv_end,TimeUtil.getDate(item.getUseend()),R.string.msg_coupon_use_end_time);
     }

    @Override
    public BasicItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(R.layout.adapter_my_coupon,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        setDataView((ViewHolder)holder,position);
    }

    class ViewHolder extends BasicItemViewHolder {
        @InjectView (R.id.iv_adapter_mycoupon_item_img) ImageView iv;
        @InjectView (R.id.tv_adapter_mycoupon_tx_title)TextView tv_title;
        @InjectView (R.id.tv_adapter_coupon_card)TextView tv_content;
        @InjectView (R.id.tv_adapter_coupon_pass)TextView tv_pass;
        @InjectView (R.id.tv_adapter_coupon_start)TextView tv_start;
        @InjectView (R.id.tv_adapter_coupon_end)TextView tv_end;
        @InjectView (R.id.text_goto)TextView tv_go;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

//        @Override
//        protected void setItemViewOnClick(int position) {
////            Bundle bundle = new Bundle();
////            bundle.putSerializable("bean", getData().get(getPosition()));
////            IntentUtil.AnimationJump(bundle, (Activity) context, MsgDetailActivity.class, Pair.create((View) _img, "img"));
//
//        }
    }
}
