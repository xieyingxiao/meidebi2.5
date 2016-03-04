package com.meidebi.app.ui.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.support.component.UmengUtil;
import com.meidebi.app.support.lib.MyAsyncTask;
import com.meidebi.app.support.utils.component.CleanCacheWebView;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;
import com.meidebi.app.ui.view.preferenfragment.PreferenceFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SettingActivity extends BasePullToRefreshActivity {
	// index
	public static final String ENBLE_PIC = "pic";
    public static final String MARKET = "market";
	public static final String UPDATE = "update";
	public static final String EXIT = "exit";
    public static final String CLEAN = "clean";
	public static final String LOGINOUT = "loginout";

	// push
	public static final String PUSH_ON = "push_on";
    public static final String AUTO_REFRESH = "refresh";

    public static final String PUSH_VIBRATE = "push_vibrate";
	public static final String ENABLE_PUSH = "push";
	public static final String PUSH_SELIENTIME = "push_selientime";
	public static final String PUSH_LOC = "push_loc";
	public static final String PUSH_CONTENT = "push_content";
	public static final String PUSH_SOUND = "push_sound";
    public static final String PUSH_AIMODE = "push_aimode";
    public static final String PUSH_RECENT_PRICE = "push_recent_price";
    public static final String PUSH_HISTORY_PRICE = "push_history_price";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
 		setActionBar("设置");
		// replaceFragment(R.id.common_fragment, new SettingsFragment());
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.common_fragment, new SettingsFragment())
					.commit();
		}
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.common_fragment_activity;
    }

    public static class SettingsFragment extends PreferenceFragment implements
			OnPreferenceClickListener {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			if(VERSION.SDK_INT >= 14){
				addPreferencesFromResource(R.xml.setting_pref);
			}else{
			addPreferencesFromResource(R.xml.setting_pref_);
			}
			Preference market = findPreference(SettingActivity.MARKET);
			market.setOnPreferenceClickListener(this);
			Preference clean = findPreference(SettingActivity.CLEAN);
			clean.setOnPreferenceClickListener(this);
		}

		@Override
		public void onDetach() {
			super.onDetach();
			// PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);

		}

		@Override
		public boolean onPreferenceClick(Preference preference) {
			// TODO Auto-generated method stub
			String key = preference.getKey();
			if (key.equals(SettingActivity.CLEAN)) {
				clearCache();
			}
//			if (key.equals(SettingActivity.LOGINOUT)) {
//				IntentUtil.sendActionBroadCast(AppAction.CALLLOGINEDOUT);
//			}
			if (key.equals(SettingActivity.MARKET)) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id="
						+ getActivity().getPackageName()));
				startActivity(intent);
			}
//			if (key.equals(SettingActivity.EXIT)) {
//				XApplication.getInstance().startedApp = false;
//				android.os.Process.killProcess(android.os.Process.myPid());
//				System.exit(1);
//			}
//			if (key.equals(SettingActivity.UPDATE)) {
//				getUmengUtil().checkUpdate();
//			}
			return false;
		}
		
		private UmengUtil umengUtil;
		public UmengUtil getUmengUtil() {
			if(umengUtil==null){
				umengUtil = new UmengUtil(getActivity());
			}
			return umengUtil;
		}
		
		private void clearCache() {
			new clearCacheTask().execute();
		}

		class clearCacheTask extends
		MyAsyncTask<Void, Void,Void> {

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				ImageLoader.getInstance().clearDiscCache();
				CleanCacheWebView.clearCacheFolder(XApplication.getInstance()
						.getApplicationContext().getCacheDir(),
						System.currentTimeMillis());
				CleanCacheWebView.deleteCacheFile(XApplication.getInstance());
//				CleanCacheWebView.cleanApplicationData(XApplication.getInstance());
				return null;
			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				((BasePullToRefreshActivity)getActivity()).showLoading("正在清理");
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				((BasePullToRefreshActivity)getActivity()).dissmissDialog();
				Toast.makeText(XApplication.getInstance(), getString(R.string.cahce_clear_compelte),
						Toast.LENGTH_SHORT).show();
				super.onPostExecute(result);
			}
			
		}
		
	}

}
