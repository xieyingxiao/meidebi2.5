package com.meidebi.app.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.service.bean.detail.CouponSingleBean;
import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CouponSiteListAdapter extends BaseRecyclerAdapter<CouponSingleBean> {
//	private ListView lv;
    private Fragment fragment;
    private int select_position;

    public void setSelect_position(int select_position) {
        this.select_position = select_position;
        notifyDataSetChanged();
    }

    public CouponSiteListAdapter(Fragment fragment,
			List<CouponSingleBean> objects) {
		super(fragment.getActivity(), objects);
        this.fragment = fragment;
		// TODO Auto-generated constructor stub
//		this.lv = lv;
	}

    @Override
    public boolean useHeader() {
        return false;
    }

    @Override
    public boolean useFooter() {
        return false;
    }

    @Override
    public BasicItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(R.layout.adapter_coupon_site,parent,false);
        return new ViewHodler(v);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        setDataView((ViewHodler)holder,position);
    }

    private void setDataView(final ViewHodler holder, final int position) {// 显示数据
        final CouponSingleBean item = mData.get(position);
//        boolean isChecked = lv.isItemChecked(position);
//        holder.tv_site.setSelected(isChecked);
        if (this.select_position == position) {
            holder.tv_site.setTextColor(XApplication.getInstance()
                    .getResources().getColor(R.color.titlebar_bg));
        } else {
            holder.tv_site.setTextColor(XApplication.getInstance()
                    .getResources().getColor(R.color.text_color));
        }
        holder.tv_site.setText(item.getSitename());
    }


    class ViewHodler extends BasicItemViewHolder  {
        @InjectView(R.id.tv_adapter_common)
        TextView tv_site;



        public ViewHodler(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }



    }

}
