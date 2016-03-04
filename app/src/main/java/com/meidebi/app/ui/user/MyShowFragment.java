package com.meidebi.app.ui.user;

import android.support.v4.app.Fragment;

import com.meidebi.app.R;
import com.meidebi.app.ui.base.BaseVpWithTitleFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdb-ii on 15-2-26.
 */
public class MyShowFragment extends BaseVpWithTitleFragment {

    private String[] title_res = {"已完成","上传中"};

    public MyShowFragment() {
        super(MyFavVpFragment.class.getName());
        layout_res = R.layout.common_sliding_layout;

    }

    @Override
    protected List<Fragment> initFrogmentList() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        if (getUploadedFragment() == null) {
            fragmentList.add(new ShowUploadedFragment());
        } else {
            fragmentList.add(getUploadedFragment());
        }
        if (getUploadingFragment() == null) {
            fragmentList.add(new ShowUploadingFragment());
        } else {
            fragmentList.add(getUploadingFragment());
        }
        return fragmentList;
    }

    @Override
    protected CharSequence getTitle(int position) {
        return title_res[position];
    }

    private Fragment getUploadedFragment() {
        return getActivity().getSupportFragmentManager().findFragmentByTag(
                ShowUploadedFragment.class.getName() + 0);
    }

    private Fragment getUploadingFragment() {
        return getActivity().getSupportFragmentManager().findFragmentByTag(
                ShowUploadingFragment.class.getName() + 1);
    }

}