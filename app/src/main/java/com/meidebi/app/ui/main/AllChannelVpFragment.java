package com.meidebi.app.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.meidebi.app.R;
import com.meidebi.app.service.init.ChanneInit;
import com.meidebi.app.service.init.ChannelInitBean;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.ui.adapter.base.BasePagerAdapter;
import com.meidebi.app.ui.base.BaseFragment;
import com.meidebi.app.ui.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;


public class AllChannelVpFragment extends BaseFragment {

    @InjectView(R.id.common_vp)protected ViewPager mViewPager;
    @InjectView(R.id.sliding_tabs) protected SlidingTabLayout mIndicator;
    private long data_insert_time;
    private List<ChannelInitBean> channel_list = ChanneInit.getInstance().get();
    private String category_parm;
    private Boolean isHot = true;
    private List<ChannelInitBean> channelList = ChanneInit.getInstance().get();
    public  SinglePagerAdapter mPagerAdapter;

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    public long getData_insert_time() {
        return data_insert_time;
    }

    public void setData_insert_time(long data_insert_time) {
        this.data_insert_time = data_insert_time;
    }

    public void setCategory_parm(String category_parm) {
        this.category_parm = category_parm;
    }

    public boolean isHot() {
        return isHot;
    }

     public AllChannelVpFragment(String category_parm) {
         this.category_parm = category_parm;
     }

    public AllChannelVpFragment() {
      }
    public View rootView;// 缓存Fragment view





    protected void initViewPager() {
        mIndicator.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mIndicator.setSelectedIndicatorColors(getResources().getColor(R.color.text_orage_color));



        mPagerAdapter = new SinglePagerAdapter(getChildFragmentManager());
         mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mIndicator.setViewPager(mViewPager);
    }





    public class SinglePagerAdapter extends BasePagerAdapter {

        public List<Fragment> Fragmentlist = new ArrayList<Fragment>();

        public SinglePagerAdapter(FragmentManager fm) {
            super(fm);
            for(int i = 0;i<channel_list.size();i++){
                if(getSingleFragment(i)==null){
                Fragmentlist.add(creatSingleFragment(i));

                    }else{
                    Fragmentlist.add(getSingleFragment(i));

                }
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container,position);
            if (0 < mScrollY) {
                Bundle args = new Bundle();
                args.putInt(SingleChanelListFragment.ARG_INITIAL_POSITION, 1);
                fragment.setArguments(args);
            }
            return  fragment;
        }

            private int mScrollY;

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }

        public int getScrollY() {
            return mScrollY;
         }


        @Override
        public CharSequence getPageTitle(int position) {
            return channel_list.get(position).getName();
        }

        public Fragment getItem(int position) {
            return Fragmentlist.get(position);
        }

        public Fragment getMangerFragmet(int position) {
            String name = getTag(position);
            Fragment fragment = getChildFragmentManager().findFragmentByTag(name);

            if (fragment == null) {
                fragment = getItem(position);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return Fragmentlist.size();
        }

        @Override
        protected String getTag(int position) {
            // TODO Auto-generated method stub
            List<String> tagList = new ArrayList<String>();
            tagList.add(SingleChanelListFragment.class.getName() + 0);
            tagList.add(SingleChanelListFragment.class.getName() + 1);
            tagList.add(SingleChanelListFragment.class.getName() + 2);
            tagList.add(SingleChanelListFragment.class.getName() + 3);
            return tagList.get(position);
        }
    }
    private Fragment getSingleFragment(int num) {
        return this.getChildFragmentManager().findFragmentByTag(
                SingleChanelListFragment.class.getName() + num);
    }



    private SingleChanelListFragment creatSingleFragment(int num){
           SingleChanelListFragment singleChanelListFragment =new  SingleChanelListFragment(isHot(),category_parm,channelList.get(num).getParam(),num);
         return  singleChanelListFragment;
    }




}
