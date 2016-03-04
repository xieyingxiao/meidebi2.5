package com.meidebi.app.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.meidebi.app.R;
import com.meidebi.app.ui.adapter.base.BasePagerAdapter;
import com.meidebi.app.ui.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseVpWithTitleActivity extends BaseFragmentActivity{
	private ViewPager mViewPager;
	protected SlidingTabLayout mIndicator;
	protected PagerAdapter mBasePageAdapter;
	protected int layout_res = R.layout.common_vp;
	private String fragment_name;
	private boolean isFirstInit = true;
	private boolean toRefresh = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(layout_res);
		initViewPager();
	}

	// start vp
	protected void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.common_vp);
		mIndicator = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		mBasePageAdapter = new PagerAdapter(getSupportFragmentManager());
		mViewPager.setOffscreenPageLimit(0);
		mViewPager.setAdapter(mBasePageAdapter);
		mIndicator.setViewPager(mViewPager);
		isFirstInit = false;
		// mIndicator.setOnPageChangeListener(new MyPageChangeListener());
	}

	public class PagerAdapter extends BasePagerAdapter {

		private List<Fragment> Fragmentlist = new ArrayList<Fragment>();

		public PagerAdapter(FragmentManager fm) {
			super(fm);
			Fragmentlist = initFrogmentList();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return getTitle(position);
		}

		public Fragment getItem(int position) {
			return Fragmentlist.get(position);
		}

		@Override
		public int getCount() {
			return Fragmentlist.size();
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		protected String getTag(int position) {
			// TODO Auto-generated method stub
			List<String> tagList = new ArrayList<String>();
			for (int i = 0; i < Fragmentlist.size(); i++) {
				tagList.add(fragment_name + i);
			}
			return tagList.get(position);
		}
		
 
	}

	public Fragment getFragment(int num) {
		return this.getSupportFragmentManager().findFragmentByTag(
				fragment_name + num);
	}
	

	protected abstract List<Fragment> initFrogmentList();

	protected abstract CharSequence getTitle(int position);
	


	public void refresh() {
		if (!isFirstInit) {
			toRefresh = true;
			initFrogmentList();
			mBasePageAdapter.notifyDataSetChanged();
		}
	}

	
}
