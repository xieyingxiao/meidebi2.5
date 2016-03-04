package com.meidebi.app.ui.commonactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meidebi.app.ui.base.BaseFragmentActivity;
import com.meidebi.app.ui.base.BaseRecycleViewFragment;

/**
 * Created by mdb-ii on 15-1-8.
 */
public abstract  class BackPressListFragment<T> extends BaseRecycleViewFragment<T> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CommonFragmentActivity activity = (CommonFragmentActivity)getActivity();
        activity.setOnBackPressedListener(new BaseBackPressedListener());

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setTitle(String title){
        ((BaseFragmentActivity)getActivity()).getSupportActionBar().setTitle(title);
    }

    public abstract void onBackPress();

    public class BaseBackPressedListener implements OnBackPressedListener {


        @Override
        public void doBack() {
            onBackPress();
         }
    }
}
