package com.meidebi.app.ui.user;

import android.support.v4.app.Fragment;

import com.meidebi.app.R;
import com.meidebi.app.ui.base.BaseVpWithTitleFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdb-ii on 15-2-6.
 */
public class MyCouponVpFragment extends BaseVpWithTitleFragment {

    private String[] title_res = {"可使用的","已过期的"};

    public MyCouponVpFragment() {
        super(MyCouponVpFragment.class.getName());
        layout_res = R.layout.common_sliding_layout;

    }

    @Override
    protected List<Fragment> initFrogmentList() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        for (int i = 0; i < title_res.length; i++) {
            if (getFragment(i) == null) {
                fragmentList.add(new MyCouponListFragment(i==0?false:true));
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
