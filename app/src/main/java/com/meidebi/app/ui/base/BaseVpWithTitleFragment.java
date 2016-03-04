package com.meidebi.app.ui.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meidebi.app.R;
import com.meidebi.app.ui.adapter.base.BasePagerAdapter;
import com.meidebi.app.ui.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

@SuppressLint({ "ValidFragment", "NewApi" })
public abstract class BaseVpWithTitleFragment extends BaseFragment {
    @InjectView(R.id.common_vp)protected ViewPager mViewPager;
    @InjectView(R.id.sliding_tabs) protected SlidingTabLayout mIndicator;
    protected PagerAdapter mBasePageAdapter;
    protected int layout_res = R.layout.common_vp;
    private String fragment_name;
    private boolean isFirstInit = true;
    private boolean toRefresh = false;
    public void setToRefresh(Boolean toRefresh) {
        this.toRefresh = toRefresh;
    }
    public BaseVpWithTitleFragment(String fragment_name) {
        this.fragment_name = fragment_name;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        ((SwipeBackActivity)getActivity()).setUseSwipe(false);
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(layout_res, container, false);
        ButterKnife.inject(this,view);
        initViewPager();
        return view;
    }

    // start vp
    protected void initViewPager() {
        mIndicator.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mIndicator.setSelectedIndicatorColors(getResources().getColor(R.color.text_orage_color));

        mBasePageAdapter = new PagerAdapter(getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mBasePageAdapter);
        mIndicator.setViewPager(mViewPager);
        isFirstInit = false;
    }

    public class PagerAdapter extends BasePagerAdapter {

        private List<Fragment> Fragmentlist = new ArrayList<Fragment>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            Fragmentlist = initFrogmentList();
        }
        private int mScrollY;


//        public void setScrollY(int scrollY) {
//            mScrollY = scrollY;
//        }
//
//        @Override
//        protected Fragment createItem(int position) {
//            // Initialize fragments.
//            // Please be sure to pass scroll position to each fragments using setArguments.
//            Fragment f;
//            final int pattern = position % 3;
//            switch (pattern) {
//                case 0: {
//                    f = new ViewPagerTabScrollViewFragment();
//                    if (0 <= mScrollY) {
//                        Bundle args = new Bundle();
//                        args.putInt(ViewPagerTabScrollViewFragment.ARG_SCROLL_Y, mScrollY);
//                        f.setArguments(args);
//                    }
//                    break;
//                }
//                case 1: {
//                    f = new ViewPagerTabListViewFragment();
//                    if (0 < mScrollY) {
//                        Bundle args = new Bundle();
//                        args.putInt(ViewPagerTabListViewFragment.ARG_INITIAL_POSITION, 1);
//                        f.setArguments(args);
//                    }
//                    break;
//                }
//                case 2:
//                default: {
//                    f = new ViewPagerTabRecyclerViewFragment();
//                    if (0 < mScrollY) {
//                        Bundle args = new Bundle();
//                        args.putInt(ViewPagerTabRecyclerViewFragment.ARG_INITIAL_POSITION, 1);
//                        f.setArguments(args);
//                    }
//                    break;
//                }
//            }
//            return f;
//        }


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
        return this.getChildFragmentManager().findFragmentByTag(
                fragment_name + num);
    }


    protected abstract List<Fragment> initFrogmentList();

    protected abstract CharSequence getTitle(int position);


    public void refresh() {
//        if (!isFirstInit) {
            toRefresh = true;
//            initFrogmentList();
            mBasePageAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}