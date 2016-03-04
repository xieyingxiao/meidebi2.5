package com.meidebi.app.ui.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.meidebi.app.R;
import com.meidebi.app.ui.adapter.base.BasePagerAdapter;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;
import com.meidebi.app.ui.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

public class UserFavActivity extends BasePullToRefreshActivity {
	private static final String[] CONTENT = new String[] { "单品", "活动", "优惠券",
			"晒单" };

	private ViewPager mViewPager;
	private SlidingTabLayout mIndicator;
	private PagerAdapter mBasePageAdapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        setUseSwipe(false);
        super.onCreate(savedInstanceState);
 		setActionBar("我的收藏");
		initViewPager();
	}

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_my_fav;
    }


    // start vp
	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.common_vp);
		mIndicator = (SlidingTabLayout)findViewById(R.id.sliding_tabs);
		mBasePageAdapter = new PagerAdapter(getSupportFragmentManager());
		mViewPager.setOffscreenPageLimit(0);
		mViewPager.setAdapter(mBasePageAdapter);
		mIndicator.setViewPager(mViewPager);
	}

	private class PagerAdapter extends BasePagerAdapter {

		public List<Fragment> Fragmentlist = new ArrayList<Fragment>();

		public PagerAdapter(FragmentManager fm) {
			super(fm);
			if (getSingleFragment() == null) {
				Fragmentlist.add(new UserFavFragment(1));
			} else {
				Fragmentlist.add(getSingleFragment());
			}
			if (getActFragment() == null) {
				Fragmentlist.add(new UserFavFragment(2));
			} else {
				Fragmentlist.add(getActFragment());
			}
			if (getCouponFragment() == null) {
				Fragmentlist.add(new UserFavFragment(3));
			} else {
				Fragmentlist.add(getCouponFragment());
			}
			if (getCouponFragment() == null) {
				Fragmentlist.add(new UserFavFragment(4));
			} else {
				Fragmentlist.add(getShowOrderFragment());
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		public Fragment getItem(int position) {
			return Fragmentlist.get(position);
		}

		@Override
		public int getCount() {
			return Fragmentlist.size();
		}

		@Override
		protected String getTag(int position) {
			// TODO Auto-generated method stub
			List<String> tagList = new ArrayList<String>();
			tagList.add(UserFavFragment.class.getName() + 0);
			tagList.add(UserFavFragment.class.getName() + 1);
			tagList.add(UserFavFragment.class.getName() + 2);
			tagList.add(UserFavFragment.class.getName() + 3);
			return tagList.get(position);
		}
	}

	private Fragment getSingleFragment() {
		return this.getSupportFragmentManager().findFragmentByTag(
				UserFavFragment.class.getName() + 0);
	}

	private Fragment getActFragment() {
		return this.getSupportFragmentManager().findFragmentByTag(
				UserFavFragment.class.getName() + 1);
	}

	private Fragment getCouponFragment() {
		return this.getSupportFragmentManager().findFragmentByTag(
				UserFavFragment.class.getName() + 2);
	}

	private Fragment getShowOrderFragment() {
		return this.getSupportFragmentManager().findFragmentByTag(
				UserFavFragment.class.getName() + 3);
	}
	
}
