package com.meidebi.app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.service.bean.user.FavBean;
import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyFavListAdapter extends BaseRecyclerAdapter<FavBean> {
	// List<MainListItemBean> mList =new ArrayList<MainListItemBean>();
	private int type = 0;

	public MyFavListAdapter(Context con, List<FavBean> mList) {
		super(con, mList);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}





	private void setDataView(ViewHolder holder, final int position) {// 显示数据
		final FavBean item = mData.get(position);
		if (type == 2) {
//			holder._des.setVisibility(View.VISIBLE);
//			holder._des.setText(item.getStrUp());
			holder._title.setText(item.getStrDown());
		}else if(type == 5){
 			holder._title.setText(item.getStrUp());
		} else {
			holder._title.setText(item.getStrUp());
		}
 		if (item.getState() == 1) {
			holder._tv_flag.setVisibility(View.VISIBLE);
		} else {
			holder._tv_flag.setVisibility(View.GONE);
		}
		imageLoader.displayImage(item.getStrImgUrl(), holder._img, options,
				animateFirstListener);
		// holder._img.setImageUrl(item.getImage(),
		// AppConfigs.Loading_List_Img_Bg_Small);
	}

    @Override
    public BasicItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(R.layout.adapter_my_fav,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        setDataView((ViewHolder)holder,position);
    }


    class ViewHolder extends BasicItemViewHolder {
        @InjectView(R.id.adapter_main_item_tx_title)
        TextView _title;
        @InjectView(R.id.adapter_main_item_tx_time)
        TextView _time;
        @InjectView(R.id.adapter_main_item_img)
        ImageView _img;
        @InjectView(R.id.tv_adapter_flag)
        TextView _tv_flag;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }


    }



}
