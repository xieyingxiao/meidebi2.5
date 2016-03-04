package com.meidebi.app.ui.user;

import android.support.v4.app.Fragment;

import com.meidebi.app.R;
import com.meidebi.app.ui.base.BaseVpWithTitleFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdb-ii on 15-2-6.
 */
public class MyFavVpFragment extends BaseVpWithTitleFragment {

    private String[] title_res = {"优惠","晒单","领券"};

    public MyFavVpFragment() {
        super(MyFavVpFragment.class.getName());
        layout_res = R.layout.common_sliding_layout;

    }

    @Override
    protected List<Fragment> initFrogmentList() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        for (int i = 0; i < title_res.length; i++) {
            if (getFragment(i) == null) {//晒单
                switch (i){
                    case 0:
                        fragmentList.add(new UserFavFragment(UserFavFragment.SINGLE_PARAM));

                        break;
                    case 1:
                        fragmentList.add(new UserFavFragment(UserFavFragment.ODERSHOW_PARAM));

                        break;
                    case 2:
                        fragmentList.add(new UserFavFragment(UserFavFragment.COUPON_PARAM));

                        break;
                }

            } else {
                fragmentList.add(getFragment(i));
            }
        }
        return fragmentList;
    }

    @Override
    protected CharSequence getTitle(int position) {
        return title_res[position];
    }
}
