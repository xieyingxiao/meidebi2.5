package com.meidebi.app.ui.picker;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.support.utils.anim.AnimateFirstDisplayListener;
import com.meidebi.app.ui.adapter.base.HeaderRecyclerViewAdapter;
import com.meidebi.app.ui.widget.LoadStatus;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Created by mdb-ii on 15-1-27.
 */
public abstract  class BasePickAdapter<T> extends HeaderRecyclerViewAdapter {

    protected SparseArray<T> mData;
    private LayoutInflater mLayoutInflater; // 解析配置文件工具
    protected ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    protected Context context;
    protected DisplayImageOptions options;
    protected DisplayImageOptions options2;
    private LoadStatus mState = LoadStatus.Idle;
    public LoadStatus getmState() {
        return mState;
    }
    private View headView;
    public void setState(final LoadStatus state) {
        this.mState = state;

    }

    public void setHeadView(View headView) {
        this.headView = headView;
    }

    public BasePickAdapter(Context context, SparseArray<T> objects) {
        super();
        this.mData = objects;
        this.context = context;
        options =  new DisplayImageOptions.Builder()
                .cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .build();
    }

    public LayoutInflater getLayoutInflater() {
        return this.getLayoutInflater(context);
    }

    public LayoutInflater getLayoutInflater(Context context) {
        if (this.mLayoutInflater == null) {
            this.mLayoutInflater = LayoutInflater.from(context);
        }
        return this.mLayoutInflater;
    }


    public void initImageOptions(int resid) {
        options2 = new DisplayImageOptions.Builder()
                .showImageOnLoading(resid)
                .showImageForEmptyUri(resid)
// 		.showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .build();
    }


    public void addItem(int key,T object) {
        if (mData != null) {
            mData.put(key, object);
        } else {
            mData = new SparseArray<T>();
            mData.put(key, object);
        }
        super.notifyDataSetChanged();
    }



    public void clear() {
        synchronized (mData) {
            if (mData != null) {
                mData.clear();
            }

            super.notifyDataSetChanged();
        }
    }





    public SparseArray<T> getData() {
        return mData;
    }

    public void setData(
            SparseArray<T> mData) {
        this.mData = mData;
    }


    @Override
    public boolean useHeader() {
        return headView!=null?true:false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return new HeaderViewHolder(headView);
    }

    @Override
    public void onBindHeaderView(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public boolean useFooter() {
        return true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(R.layout.listview_footer_layout,parent,false);
        return new ViewFooter(v);
    }

    @Override
    public void onBindFooterView(RecyclerView.ViewHolder holder, int position) {
        setFooterView((ViewFooter)holder);
    }

    private void setFooterView(final ViewFooter holder){
        switch (mState) {
            case Loading:
                holder.refreshView.setVisibility(View.VISIBLE);
                holder.tv.setVisibility(View.GONE);
                break;
            case err:
                holder.refreshView.setVisibility(View.GONE);
                holder.tv.setVisibility(View.VISIBLE);
                holder.tv.setText(context.getString(R.string.pull_listview_err));
                holder.tv.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        holder.refreshView.setVisibility(View.VISIBLE);
                        holder.tv.setVisibility(View.GONE);
                        setState(LoadStatus.Loading);
                        OnItemClickListener.OnFoooterClick(mData.size());
                    }
                });
                break;
            case TheEnd:
                holder.refreshView.setVisibility(View.GONE);
                holder.tv.setVisibility(View.VISIBLE);
                holder.tv.setText(context.getString(R.string.pull_listview_empty));
                break;
            default:
                holder.refreshView.setVisibility(View.VISIBLE);
                holder.tv.setVisibility(View.GONE);
                break;
        }

    }


    @Override
    public int getBasicItemCount() {
        return mData != null ? mData.size() : 0;
    }



    class ViewFooter extends RecyclerView.ViewHolder {
        @InjectView(R.id.listview_footer)
        TextView tv;
        @InjectView(R.id.refresh)
        CircularProgressBar refreshView;

        public ViewFooter(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }


    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    public T getItem(int position) {
        if ( mData!= null && mData.size() > 0 && position < mData.size()) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public int getBasicItemType(int position) {
        return 0;
    }
}
