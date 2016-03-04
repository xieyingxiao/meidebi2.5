package com.meidebi.app.ui.setting;

import android.os.Bundle;
import android.view.MenuItem;

import com.meidebi.app.R;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;
import com.umeng.message.PushAgent;

public class PushSettingActivity extends BasePullToRefreshActivity {
	private NotificationFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setActionBar("推送设置");
		if (savedInstanceState == null) {
			fragment = new NotificationFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.common_fragment, fragment).commit();
		}
        AppLogger.e("devicetoken" + PushAgent.getInstance(this).getRegistrationId());

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.common_fragment_activity;
    }

    public void refreshFragmentDilagTimePicker(int starttime, int endTime,
			Boolean on) {
		fragment.refrshSetTime(starttime, endTime, on);
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
        // TODO Auto-generated method stub
        super.onBackPressed();
        //PushDao.submitNewSetting("",null);
    }
}
