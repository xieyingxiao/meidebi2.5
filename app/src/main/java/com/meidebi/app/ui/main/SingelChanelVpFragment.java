package com.meidebi.app.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.meidebi.app.R;
import com.meidebi.app.service.bean.base.BaseItemBean;
import com.meidebi.app.service.init.ChanneInit;
import com.meidebi.app.service.init.ChannelInitBean;
import com.meidebi.app.ui.base.BaseFragment;
import com.meidebi.app.ui.view.SlidingTabLayout;

import java.util.List;

import butterknife.InjectView;

@SuppressLint("ValidFragment") public class SingelChanelVpFragment extends BaseFragment {
	private View view;
	private List<BaseItemBean> cat_list;
	private ChannelInitBean category;
	private long data_insert_time;
	private List<ChannelInitBean> channelList = ChanneInit.getInstance().get();
    @InjectView(R.id.common_vp)protected ViewPager mViewPager;
    @InjectView(R.id.sliding_tabs) protected SlidingTabLayout mIndicator;
    protected  NavigationAdapter mPagerAdapter;
	public long getData_insert_time() {
		return data_insert_time;
	}

	public void setData_insert_time(long data_insert_time) {
		this.data_insert_time = data_insert_time;
	}

	public boolean isHot() {
		return category.isHot();
	}

	public SingelChanelVpFragment(ChannelInitBean category) {
//		super(SingleChanelListFragment.class.getName());
//		layout_res = R.layout.vp_coupon_list;
		this.category = category;
	}

	public SingelChanelVpFragment() {
//		super(SingleChanelListFragment.class.getName());
//		layout_res = R.layout.vp_coupon_list;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.vp_coupon_list,container,false);
        initViewPager();

		return view;
	}

    protected void initViewPager() {
        mIndicator.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mIndicator.setSelectedIndicatorColors(getResources().getColor(R.color.text_orage_color));



        mPagerAdapter = new NavigationAdapter(getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mPagerAdapter);
        mIndicator.setViewPager(mViewPager);
     }


    /**
     * This adapter provides two types of fragments as an example.
     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
     */
    public   class NavigationAdapter extends CacheFragmentStatePagerAdapter {


        private int mScrollY;

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }

        @Override
        protected Fragment createItem(int position) {
            // Initialize fragments.
            // Please be sure to pass scroll position to each fragments using setArguments.
             switch (position) {
                case 0:
                    return getMainFragment();

                 case 1:
                    return getGuoneiFragment();


                case 2:
                    return getTmallFragment();

                  case 3:
                     return getHaitaoFragment();

                 default:
                    break;

            }
            return getHaitaoFragment();

        }

        @Override
        public int getCount() {
            return channelList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return channelList.get(position).getName();
        }
    }

    private SingleChanelListFragment fragment_main,fragment_haitao,fragment_tmall,Fragment_guonei;

    private SingleChanelListFragment getMainFragment(){
        if(fragment_main==null){
            fragment_main = creatSingleFragment(0);
        }
        return fragment_main;
    }
    private SingleChanelListFragment getGuoneiFragment(){
        if(Fragment_guonei==null){
            Fragment_guonei = creatSingleFragment(1);
        }
        return Fragment_guonei;
    }

    private SingleChanelListFragment getTmallFragment(){
        if(fragment_tmall==null){
            fragment_tmall = creatSingleFragment(2);
        }
        return fragment_tmall;
    }

    private SingleChanelListFragment getHaitaoFragment(){
        if(fragment_haitao==null){
            fragment_haitao = creatSingleFragment(3);
        }
        return fragment_haitao;
    }



    private SingleChanelListFragment creatSingleFragment(int num){
        return new SingleChanelListFragment(category.isHot(),category.getParam(),channelList.get(num).getParam(),num);
    }

}


