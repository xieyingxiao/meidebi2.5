package com.meidebi.app.ui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.service.bean.BannerBean;
import com.meidebi.app.support.utils.Utility;
import com.meidebi.app.support.utils.anim.AnimateFirstDisplayListener;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.ui.browser.BrowserWebActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class VpBannerView implements OnPageChangeListener {
	private View view;
	private ViewPager activie_vp;
	private LinearLayout ll_vp;
	private ImageView iv_close_banner;
	private RelativeLayout ll_banner;
	private Activity activity;
	private int vp_height;
	private List<BannerBean> list_bannner;
	private ImageView[] points;
	private ViewPagerAdapter vp_adapter;

	public List<BannerBean> getList_bannner() {
		return list_bannner;
	}

	public void setList_bannner(List<BannerBean> list_bannner) {
		this.list_bannner = list_bannner;
	}

	public VpBannerView(Activity activity) {
		this.activity = activity;
		double size = (double) (Math
				.round(Utility.getScreenWidth(activity) * 100.0 / 640) / 100.0);
		vp_height = (int) (234 * size);
	}

	public View getView() {
		view = activity.getLayoutInflater().from(activity)
				.inflate(R.layout.layout_vp_banner, null);
		activie_vp = (ViewPager) view.findViewById(R.id.common_vp);
		ll_vp = (LinearLayout) view.findViewById(R.id.ll_vp);
//		iv_close_banner = (ImageView) view.findViewById(R.id.iv_close_banner);
//		ll_banner = (RelativeLayout) view.findViewById(R.id.ll_banner);
		if (vp_adapter == null) {
			vp_adapter = new ViewPagerAdapter();
		}
		activie_vp.setAdapter(vp_adapter);
		activie_vp.setLayoutParams(new LayoutParams(Utility
				.getScreenWidth(activity), vp_height));
		activie_vp.setOnPageChangeListener(this);
		initPoint();
		ll_vp.setVisibility(View.VISIBLE);
		ll_banner.setVisibility(View.VISIBLE);
		activie_vp.setVisibility(View.VISIBLE);
		iv_close_banner.setVisibility(View.VISIBLE);
		iv_close_banner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AppConfigs.OPENBANNER = false;
				list_bannner = null;
				ll_banner.setVisibility(View.GONE);
				ll_vp.setVisibility(View.GONE);
				activie_vp.setVisibility(View.GONE);
				iv_close_banner.setVisibility(View.GONE);
			}
		});
		return view;
	}

	class ViewPagerAdapter extends PagerAdapter {
		private DisplayImageOptions options;

		private ImageLoader imageLoader = ImageLoader.getInstance();
		protected ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		public ViewPagerAdapter() {
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(AppConfigs.Loading_List_Img_Bg_Small)
					// .showImageOnFail(R.drawable.ic_error)
					.cacheInMemory(true).cacheOnDisc(true)
					.considerExifParams(true).build();
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View view = LayoutInflater.from(XApplication.getInstance())
					.inflate(R.layout.hd_adapter_main_vp, null);
			final ImageView iv = (ImageView) view
					.findViewById(R.id.iv_adapter_main_vp);
			iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (LoginUtil.isAccountLogin(activity)) {
						Bundle bundle = new Bundle();
//						bundle.putSerializable(
//								"url",
//								list_bannner.get(position).getUrl()
//										+ "-devicetoken-"
//										+ Base64.encodeToString(Utility
//												.getUUID().getBytes(),
//												Base64.DEFAULT)
//										+ "-userkey-"
//										+ Base64.encodeToString(LoginUtil
//												.getUid().getBytes(),
//												Base64.DEFAULT));
//						bundle.putSerializable("title",
//								list_bannner.get(position).getTitle());
						IntentUtil.start_activity(activity,
								BrowserWebActivity.class, bundle);
					}
				}
			});
//			imageLoader.displayImage(list_bannner.get(position).getImage(), iv,
//					options, new AnimateFirstDisplayListener());
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

	/**
	 * 初始化底部小点
	 */

	private int currentIndex;

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
			iv.setPadding(10, 10, 10, 10);

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

}
