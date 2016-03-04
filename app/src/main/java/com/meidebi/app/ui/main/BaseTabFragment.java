package com.meidebi.app.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meidebi.app.ui.base.BaseFragment;

/**
 * Created by mdb-ii on 15-3-6.
 */
public abstract class BaseTabFragment extends BaseFragment {
    private View rootView;// 缓存Fragment view

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(getLayoutResouce(), null);
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
        {
            parent.removeView(rootView);
        }
        return rootView;
    }

    protected abstract  int getLayoutResouce();


}
