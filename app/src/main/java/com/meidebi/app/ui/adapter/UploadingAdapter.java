package com.meidebi.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.meidebi.app.R;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.service.bean.show.Draft;
import com.meidebi.app.support.utils.content.CommonUtil;
import com.meidebi.app.support.utils.content.TimeUtil;
import com.meidebi.app.ui.adapter.base.InterRecyclerOnItemClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by mdb-ii on 15-2-26.
 */
public class UploadingAdapter  extends RecyclerView.Adapter {

    private Context context;
    private SparseArray<Draft> mList;
    public InterRecyclerOnItemClick OnItemClick;
    protected DisplayImageOptions options;
    private ImageLoader imageLoader=ImageLoader.getInstance();

    public void setOnItemClick(InterRecyclerOnItemClick onItemClick) {
        OnItemClick = onItemClick;
    }

    public UploadingAdapter(Context context, SparseArray<Draft> mList) {
        super();
        this.context = context;
        this.mList = mList;
        options =  new DisplayImageOptions.Builder()
                .showImageForEmptyUri(AppConfigs.Loading_List_Img_Bg_Small)
                .showImageOnLoading(AppConfigs.Loading_List_Img_Bg_Small)
                .showImageOnFail(AppConfigs.Loading_List_Img_Bg_Small)
                .cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .build();
     }






    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(R.layout.adapter_uploading,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setDataView((ViewHolder)holder,position);

    }

    private void setDataView(ViewHolder holder, int position) {
        Draft item =  mList.valueAt(position);
        holder._title.setText(item.getContent());
        holder.progressBar.setProgress((int)item.getProgress());
         imageLoader.displayImage("file://"+item.getCover(), holder._img,
                options);
        CommonUtil.formatTextView(holder._time, TimeUtil.getListTime(item.getCreatTime()), R.string.pre_my_show_time);
        if(item.getProgress()==0){
            holder.tv_upload_status.setText("等待上传");
            holder.progressBar.setVisibility(View.GONE);
        }else if(item.getProgress()==100){
            holder.tv_upload_status.setText("上传完成");
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.progressBar.setProgress((int) item.getProgress());

        }  else{
            holder.tv_upload_status.setText("上传中");
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.progressBar.setProgress((int) item.getProgress());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder   extends RecyclerView.ViewHolder {
        @InjectView(R.id.adapter_main_item_tx_title)
        TextView _title;
        //        @InjectView(R.id.adapter_main_item_tx_price)

        @InjectView(R.id.tv_adapter_main_item_time)
        TextView _time;
        //        @InjectView(R.id.adapter_main_item_tx_good)
//        TextView _good;
//        @InjectView(R.id.adapter_main_item_tx_comment)
//        TextView _comment;
        @InjectView(R.id.adapter_main_item_img)
        ImageView _img;
        @InjectView(R.id.upload_progress)
        NumberProgressBar progressBar;
        @InjectView(R.id.tv_adapter_my_upload_status)
        TextView tv_upload_status;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.inject(this, itemView);
             if(OnItemClick!=null){
            OnItemClick.OnItemClick(getPosition());
            }
        }


    }
    private LayoutInflater mLayoutInflater; // 解析配置文件工具

    public LayoutInflater getLayoutInflater() {
        return this.getLayoutInflater(context);
    }

    public LayoutInflater getLayoutInflater(Context context) {
        if (this.mLayoutInflater == null) {
            this.mLayoutInflater = LayoutInflater.from(context);
        }
        return this.mLayoutInflater;
    }

}
