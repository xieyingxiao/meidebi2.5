package com.meidebi.app.ui.adapter.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class BasePageAdapter extends FragmentStatePagerAdapter {

	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();;
//	public List<CategorysEntity> tabs = new ArrayList<CategorysEntity>();

	private Activity mActivity;

	public BasePageAdapter(FragmentActivity activity) {
		super(activity.getSupportFragmentManager());
		this.mActivity = activity;
	}

	public void addFragment( ) {
	}
	/**
	 * 只加载listview不包含 tabs
	 * @param listObject
	 */
	public void addFragment( List<Object> listObject) {

		for (int i = 0; i < listObject.size(); i++) {
			Object object = listObject.get(i);
		}
	}

	public void addNullFragment() {
	}

	public void Clear() {
		mFragments.clear();
	}

	public void addTab(Fragment fragment) {
		mFragments.add(fragment);
		notifyDataSetChanged();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "11";
	}

	@Override
	public Fragment getItem(int arg0) {
		return mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	//

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.destroyItem(container, position, object);
	}
}
