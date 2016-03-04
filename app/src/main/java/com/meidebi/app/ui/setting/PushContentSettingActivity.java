package com.meidebi.app.ui.setting;

import android.os.Bundle;
import android.view.MenuItem;

import com.meidebi.app.R;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;

public class PushContentSettingActivity extends BasePullToRefreshActivity {
    private PushContentFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setActionBar("推送内容");
        if (savedInstanceState == null) {
            fragment = new PushContentFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.common_fragment, fragment).commit();
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fragment.saveSetting();
    }

    public void titleOnclick() {
        fragment.saveSetting();
        finish();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.common_fragment_activity;
    }

}