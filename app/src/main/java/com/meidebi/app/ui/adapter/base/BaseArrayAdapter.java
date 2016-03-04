package com.meidebi.app.ui.adapter.base;

import java.util.ArrayList;
import java.util.List;

import com.meidebi.app.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.support.utils.anim.AnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public abstract class BaseArrayAdapter<T> extends BaseAdapter {

	protected List<T> mData;
	protected Context context;
	private LayoutInflater mLayoutInflater; // 解析配置文件工具
	protected ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options;
	protected DisplayImageOptions options2;

	public BaseArrayAdapter(Context context, List<T> objects) {
		super();
		this.mData = objects;
		this.context = context;
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(AppConfigs.Loading_List_Img_Bg_Small)
 		.showImageForEmptyUri(R.drawable.iv_no_img)
//		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.build();
	}
	
	
	public void initImageOptions(int resid){
		options2 = new DisplayImageOptions.Builder()
		.showImageOnLoading(resid)
		.showImageForEmptyUri(resid)
// 		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisc(true)
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

			super.notifyDataSetInvalidated();
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

	@Override
	public int getCount() {
		return mData != null ? mData.size() : 0;
	}

	@Override
	public T getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public Context getContext() {
		return context;
	}

//	/**
//	 * 获取一个图片下载器
//	 * 
//	 * @return
//	 */
//	public WebImageBuilder getWebImageBuilder() {
//		if (this.webImageBuilder == null || this.webImageBuilder.get() == null) {
//			this.webImageBuilder = new SoftReference<WebImageBuilder>(
//					ClientFactory.getImageBuilder(this.getContext()));
//		}
//		return this.webImageBuilder.get();
//	}

//	/**
//	 * 将图片设置到界面
//	 */
//	public void onImageReceived(ImageDisplayer displayer) {
//		if (displayer.getBmp() != null) {
//			Activity activity = (Activity) getContext();
//			activity.runOnUiThread(displayer);
//		}
//	}


	public List<T> getData() {
		return mData;
	}

	public void setData(List<T> mData) {
		this.mData = mData;
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
	
	public void appendToList(List<T> lists) {
		if (lists == null) {
			return;
		}
		mData.addAll(lists);
		notifyDataSetChanged();
	}


}
