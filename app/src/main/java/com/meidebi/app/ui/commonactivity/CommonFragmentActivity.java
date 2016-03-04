package com.meidebi.app.ui.commonactivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.meidebi.app.R;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;

/**
 * Created by mdb-ii on 15-1-9.
 */
public class CommonFragmentActivity extends BasePullToRefreshActivity {
    protected OnBackPressedListener onBackPressedListener;
    public static final String KEY = "fragment";
    public static final String PARAM = "param";
    public static final String PARAM2 = "param2";

    public static final String USETOOLBAR = "toolbar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            showFragment(getIntent().getStringExtra(this.KEY));
        }
    }

    @Override
    protected int getLayoutResource() {
        if(getIntent().getStringExtra(USETOOLBAR)!=null) {
            return R.layout.common_no_title_fragment_activity;
        }
        return R.layout.common_fragment_activity;
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    private void showFragment(String className){
         try {
             Class<?> subClass = Class.forName(className);
            Fragment fragment = (Fragment)subClass.newInstance();
             if(getIntent().getStringExtra(PARAM)!=null) {
                 Bundle args = new Bundle();
                 args.putString(PARAM, getIntent().getStringExtra(PARAM));
                 args.putString(PARAM2, getIntent().getStringExtra(PARAM2));

                 fragment.setArguments(args);
             }
             addFragment(fragment) ;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
             e.printStackTrace();
         }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // case R.id.action_refresh:
            // // mContentFragment.loadFirstPageAndScrollToTop();
            // return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener.doBack();
            super.onBackPressed();
        }
        else {
            super.onBackPressed();
        }
    }
}
