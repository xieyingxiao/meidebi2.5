package com.meidebi.app.ui.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.support.utils.anim.AnimateFirstDisplayListener;
import com.meidebi.app.ui.widget.LoadStatus;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Created by mdb-ii on 14-12-2.
 */
public abstract class BaseRecyclerAdapter<T> extends HeaderRecyclerViewAdapter {
    protected List<T> mData;
    private LayoutInflater mLayoutInflater; // 解析配置文件工具
    protected ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    protected Context context;
    protected DisplayImageOptions options;
    protected DisplayImageOptions options2;
    private boolean userFooter = true;
    private boolean useLoadMore = true;

    public boolean isUseLoadMore() {
        return useLoadMore;
    }

    public void setUseLoadMore(boolean useLoadMore) {
        this.useLoadMore = useLoadMore;
    }

    public void setUserFooter(boolean userFooter) {
        this.userFooter = userFooter;
    }

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

    public BaseRecyclerAdapter(Context context, List<T> objects) {
        super();
        this.mData = objects;
        this.context = context;
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(AppConfigs.unload_bg)
                .showImageOnLoading(AppConfigs.Loading_List_Img_Bg_Small)
                .showImageOnFail(AppConfigs.Loading_List_Img_Bg_Small)
                .cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)
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
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }


    public void addItem(T object) {
        if (mData != null) {
            mData.add(object);
        } else {
            mData = new ArrayList<T>();
            mData.add(object);
        }
        super.notifyDataSetChanged();
    }

    public void addAllItem(List<T> obs) {
        if (mData != null) {
            mData.addAll(obs);
        } else {
            mData = new ArrayList<T>(obs);
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


    public void resetData(List<T> objs) {
        if (mData != null) {
            mData.clear();
        } else {
            mData = new ArrayList<T>();
        }
        mData.addAll(objs);
        super.notifyDataSetChanged();
    }


    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> mData) {
        this.mData = mData;
    }


    @Override
    public boolean useHeader() {
        return headView != null ? true : false;
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
        return userFooter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View v = getLayoutInflater().inflate(R.layout.listview_footer_layout, parent, false);
        return new ViewFooter(v);
    }

    @Override
    public void onBindFooterView(RecyclerView.ViewHolder holder, int position) {
        setFooterView((ViewFooter) holder);
    }

    private void setFooterView(final ViewFooter holder) {
        switch (mState) {
            case Loading:
                holder.refreshView.setVisibility(View.VISIBLE);
                holder.tv.setVisibility(View.GONE);
                break;
            case err:
                holder.refreshView.setVisibility(View.GONE);
                holder.tv.setVisibility(View.VISIBLE);
                holder.tv.setText(context.getString(R.string.pull_listview_err));
                holder.tv.setOnClickListener(new View.OnClickListener() {

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


    public class ViewFooter extends RecyclerView.ViewHolder {
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
        if (mData != null && mData.size() > 0 && position < mData.size()) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public int getBasicItemType(int position) {
        return 0;
    }
}