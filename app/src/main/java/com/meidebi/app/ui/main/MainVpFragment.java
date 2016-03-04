package com.meidebi.app.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.meidebi.app.R;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.ListViewUtils;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.ui.base.BaseFragmentActivity;
import com.meidebi.app.ui.base.SwipeBackActivity;
import com.meidebi.app.ui.commonactivity.CommonFragmentActivity;
import com.meidebi.app.ui.search.SearchActivity;
import com.meidebi.app.ui.setting.SettingActivity;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by mdb-ii on 15-1-6.
 */
public class MainVpFragment extends AllChannelVpFragment implements ObservableScrollViewCallbacks {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    //   @InjectView(R.id.header)
//   View mHeaderView;
    @InjectView(R.id.container)
    View containerLayout;
    @InjectView(R.id.toolbarView)
    View toolbarView;
    View bottom_tab;

      private boolean ShowActionbar = true;

    public MainVpFragment() {
    }
    @SuppressLint("ValidFragment")
    public MainVpFragment(String parma) {
        super(parma);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null && args.containsKey(CommonFragmentActivity.PARAM)) {
             setCategory_parm(args.getString(CommonFragmentActivity.PARAM));
        }
        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_all_channel, container, false);
            ButterKnife.inject(this, rootView);
            initViewPager();
            mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int arg0) {
                    // TODO Auto-generated method stub
                    SingleChanelListFragment fragment = (SingleChanelListFragment) mPagerAdapter
                            .getItem(arg0);

                    if (!fragment.getIsLoadCompelte()) {
                        fragment.onStartRefresh();
                    } else if (fragment.getIsHot() != isHot()) {
                        fragment.setIsHot(isHot());
                        fragment.loadData();//获取最新数据
                    }
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                    // TODO Auto-generated method stub

                }
            });

            BaseFragmentActivity parentActivity = ((BaseFragmentActivity) (getActivity()));
            parentActivity.setSupportActionBar(toolbar);
            View title = inflater
                    .inflate(R.layout.toolbar_filter, null);

            if (args != null && args.containsKey(CommonFragmentActivity.PARAM2)) {//有参数转换为搜索页面
                parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                parentActivity.getSupportActionBar().setHomeButtonEnabled(true);
                parentActivity.getSupportActionBar().setTitle(args.getString(CommonFragmentActivity.PARAM2));
                toolbar.setNavigationIcon(R.drawable.btn_toolbar_back_sel);
                ((SingleChanelListFragment) getCurrentFragment()).onStartRefresh();
                title.findViewById(R.id.toolbar_main_search).setVisibility(View.GONE);
                ((SwipeBackActivity)parentActivity).setUseSwipe(false);
            }else{
                ((SingleChanelListFragment) getCurrentFragment()).setUseBanner(true);
                bottom_tab = getActivity().findViewById(R.id.bottom_tab);
                ((ImageButton) title.findViewById(R.id.toolbar_main_search)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentUtil.start_activity(getActivity(), SearchActivity.class);
                    }
                });
            }

            ((RadioButton)title.findViewById(R.id.rb_toolbar_fliter_hot)).setChecked(true);
            ((RadioGroup) title.findViewById(R.id.rg_toolbar_fliter)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    setIsHot(checkedId == R.id.rb_toolbar_fliter_hot);
                    ((SingleChanelListFragment) getCurrentFragment()).setIsHot(checkedId == R.id.rb_toolbar_fliter_hot);
                    ((SingleChanelListFragment) getCurrentFragment()).onStartRefresh();
                }
            });



            parentActivity.getSupportActionBar().setCustomView(title, new ActionBar.LayoutParams(Gravity.RIGHT));
            parentActivity.getSupportActionBar().setDisplayShowCustomEnabled(true);
            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ListViewUtils.smoothScrollListViewToTop((android.support.v7.widget.RecyclerView) getCurrentFragment().getView().findViewById(R.id.common_recyclerview));
                }
            });

        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
        {
            parent.removeView(rootView);
        }





        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                IntentUtil.start_activity(getActivity(), SettingActivity.class);
                return true;
            case R.id.search:
                IntentUtil.start_activity(getActivity(), SearchActivity.class);
                return true;

            case android.R.id.home:
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
             if (scrollState == ScrollState.UP) {
                if (ShowActionbar) {
                    moveToolbar(-toolbar.getHeight());
                    hideBottomBar();
                    ShowActionbar = false;
                }
            } else if (scrollState == ScrollState.DOWN) {
                if (!ShowActionbar) {
                    moveToolbar(0);
                    showBottomBar();
                    ShowActionbar = true;
                }
            }


    }


    private Fragment getCurrentFragment() {
        return mPagerAdapter.getItem(mViewPager.getCurrentItem());
    }

    private void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(toolbarView) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(toolbarView), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(toolbarView, translationY);
                ViewHelper.setTranslationY( containerLayout, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) (containerLayout).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                (  containerLayout).requestLayout();
            }
        });
        animator.start();
    }

    protected int getScreenHeight() {
        return getActivity().findViewById(android.R.id.content).getHeight();
    }





    private void hideBottomBar() {
        if(bottom_tab!=null) {
            float headerTranslationY = ViewHelper.getTranslationY(bottom_tab);
            if (headerTranslationY != -bottom_tab.getHeight()) {
                ViewPropertyAnimator.animate(bottom_tab).cancel();
                ViewPropertyAnimator.animate(bottom_tab).translationY(getResources().getDimensionPixelOffset(R.dimen.bottom_tab_height)).setDuration(200).start();
            }
        }
    }

    private void showBottomBar() {
        if(bottom_tab!=null) {
            float headerTranslationY = ViewHelper.getTranslationY(bottom_tab);
            if (headerTranslationY != 0) {
                ViewPropertyAnimator.animate(bottom_tab).cancel();
                ViewPropertyAnimator.animate(bottom_tab).translationY(0).setDuration(200).start();
            }
        }
    }

}
