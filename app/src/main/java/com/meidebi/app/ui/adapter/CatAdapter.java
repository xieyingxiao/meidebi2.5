package com.meidebi.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.service.bean.CatagerogyBean;
import com.meidebi.app.support.utils.Utility;
import com.meidebi.app.support.utils.anim.AnimateFirstDisplayListener;
import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by mdb-ii on 15-1-16.
 */
public class CatAdapter extends BaseRecyclerAdapter<CatagerogyBean> {
    LinearLayout.LayoutParams layoutParams;

    public CatAdapter(Context context, List<CatagerogyBean> objects) {
        super(context, objects);

        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .cacheInMemory(false)
                .build();
    }

    @Override
    public boolean useFooter() {
        return false;
    }

    @Override
    public BasicItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View v = getLayoutInflater().inflate(R.layout.adapter_cat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        setDataView((ViewHolder) holder, position);
    }

    private void setDataView(ViewHolder holder, final int position) {// 显示数据
        final CatagerogyBean item = getItem(position);
        imageLoader.displayImage(item.getAndroidicon(), holder._ic, options, new AnimateFirstDisplayListener());
//        holder._ic.setImageResource(item.getAndroidicon());
        holder._tv.setText(item.getName());

    }

    class ViewHolder extends BasicItemViewHolder {
        @InjectView(R.id.iv_adapter_cat)
        ImageView _ic;
        @InjectView(R.id.tv_adapter_cat)
        TextView _tv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            if (layoutParams == null) {
                layoutParams = (LinearLayout.LayoutParams) _ic.getLayoutParams();
                int icon_width = Utility.getScreenWidth(context) / 8;
                layoutParams.width = icon_width;
                layoutParams.height = icon_width;
            }
            _ic.setLayoutParams(layoutParams);
        }


    }
}
