package com.meidebi.app.ui.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.SwitchPreference;
import android.text.TextUtils;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.location.BDLocation;
import com.meidebi.app.R;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.lbs.GeoJson;
import com.meidebi.app.service.bean.lbs.LocationBean;
import com.meidebi.app.service.dao.GetAddressDao;
import com.meidebi.app.service.dao.PushDao;
import com.meidebi.app.support.component.bus.LocationChooseEvent;
import com.meidebi.app.support.component.bus.MainThreadBusProvider;
import com.meidebi.app.support.component.lbs.ILocListener;
import com.meidebi.app.support.component.lbs.Location;
import com.meidebi.app.support.component.upush.UpushUtity;
import com.meidebi.app.support.lib.MyAsyncTask;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.view.preferenfragment.PreferenceFragment;
import com.meidebi.app.ui.widget.DialogNumpicker;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class NotificationFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener, ILocListener {
    private BDLocation bd_location;
    private final int BINDLOC = 2;
    private final int BINDLOCFAIL = 6;
    private final int NETERR = 404;
    private final int DATAERR = 400;
    private Preference silent_time, loc;
    private Boolean isBind = false;
    private Boolean isCustomLocation = false;

    private List<Preference> preferenceList = new ArrayList<Preference>(4);


    public NotificationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);
        if (VERSION.SDK_INT >= 14) {
            addPreferencesFromResource(R.xml.push_pref);
        } else {
            addPreferencesFromResource(R.xml.push_pref_);
        }
//		preferenceList.add(findPreference(SettingActivity.PUSH_AIMODE));
        preferenceList.add(findPreference(SettingActivity.PUSH_LOC));
        preferenceList.add(findPreference(SettingActivity.PUSH_SELIENTIME));
        preferenceList.add(findPreference(SettingActivity.PUSH_CONTENT));
        preferenceList.add(findPreference(SettingActivity.PUSH_VIBRATE));
        preferenceList.add(findPreference(SettingActivity.PUSH_SOUND));

        switchPre(SharePrefUtility.getPushOn());

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        silent_time = findPreference(SettingActivity.PUSH_SELIENTIME);
        loc = findPreference(SettingActivity.PUSH_LOC);
        loc.setSummary(SharePrefUtility.getPushLoc());

        Preference pre_content = findPreference(SettingActivity.PUSH_CONTENT);
        pre_content
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        // TODO Auto-generated method stub
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("location",
                                SharePrefUtility.getPushLocEnble());
                        IntentUtil.start_activity(getActivity(),
                                PushContentSettingActivity.class, bundle);
                        return false;
                    }
                });

        // init summuy
        refrshSetTime(SharePrefUtility.getPushStartTime(),
                SharePrefUtility.getPushEndTime(),
                SharePrefUtility.getPushSilentMode());
        String text = "周一至周五" + AppConfigs.WORKDAYPUSHSTATTIME + ":00-"
                + AppConfigs.WORKDAYPUSHENDTIME + ":00" + "|" + "周六至周日"
                + AppConfigs.WEEKENDPUSHSTATTIME + ":00-"
                + (AppConfigs.WEEKENDPUSHENDTIME - 1) + ":"
                + AppConfigs.WEEKENDPUSHENDTIMEMIN + "开启推送";
        text = text.replace("|", "\n");
        loc.setSummary(SharePrefUtility.getPushLoc());
//		if (VERSION.SDK_INT >= 14) {
//			SwitchPreference push_ai = (SwitchPreference) findPreference(SettingActivity.PUSH_AIMODE);
//			push_ai.setSummaryOn(text);
//			push_ai.setSummaryOff("任意时段开启推送");
//		} else {
//			CheckBoxPreference push_ai = (CheckBoxPreference) findPreference(SettingActivity.PUSH_AIMODE);
//			push_ai.setSummaryOn(text);
//			push_ai.setSummaryOff("任意时段开启推送");
//		}
        MainThreadBusProvider.getInstance().register(this);

    }

    // confirm getActivity() is not null
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // buildSummary();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // String ringTonePath = "";
            // uri =
            // data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            // if (uri != null) {
            // ringTonePath = uri.toString();
            // }
            //
            // SharedPreferences sharedPref =
            // PreferenceManager.getDefaultSharedPreferences(getActivity());
            // sharedPref.edit().putString(SettingActivity.ENABLE_RINGTONE,
            // ringTonePath).commit();
            // buildSummary();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainThreadBusProvider.getInstance().unregister(this);
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(SettingActivity.PUSH_ON)) {
            boolean value = sharedPreferences.getBoolean(key, false);
            switchPre(value);
            UpushUtity.SwitchPush(getActivity());
//			if (value) {
//				JushUtity.neverStopPush();
//			} else {
//				JPushInterface.stopPush(getActivity());
//			}
        }
//		if (key.equals(SettingActivity.PUSH_AIMODE)) {
//			boolean value = sharedPreferences.getBoolean(key, false);
//			if (value) {
//				JushUtity.neverStopPush();
//			} else {
//				JushUtity.neverStopPush();
//			}
//		}
        if (key.equals(SettingActivity.PUSH_SELIENTIME)) {
            boolean value = sharedPreferences.getBoolean(key, false);
            if (value) {
                new DialogNumpicker(this).show();
            } else {
                UpushUtity.setSlientTime(0, 0);
                silent_time.setSummary("关闭");
            }
        }

        if (key.equals(SettingActivity.PUSH_LOC)) {
            boolean value = sharedPreferences.getBoolean(key, false);
            if (value) {
                sendAddressToServer();
            } else {
                loc.setSummary("全国");
            }
        }

    }

    private void switchPre(boolean value) {
        for (Preference p : preferenceList) {
            p.setEnabled(value);
        }

    }

    public void refrshSetTime(int starttime, int endTime, Boolean on) {
        if (on) {
            silent_time.setSummary("从" + starttime + "时到" + endTime + "时");
        } else {
            if (VERSION.SDK_INT >= 14) {
                ((SwitchPreference) silent_time).setChecked(false);
            } else {
                ((CheckBoxPreference) silent_time).setChecked(false);
            }
            silent_time.setSummary("关闭");
        }
    }

    public class geoCoderTask extends
            MyAsyncTask<Void, LocationBean, LocationBean> {

        private XException e;
        private GetAddressDao dao;
        private String add;

        public geoCoderTask(String add) {
            this.add = add;
        }

        public GetAddressDao getDao() {
            if (dao == null) {
                dao = new GetAddressDao();
            }
            return dao;
        }

        @Override
        protected LocationBean doInBackground(Void... params) {
            GeoJson json = null;
            json = getDao().coderAdd(add);
            if (json != null) {
                return json.getResult().getLocation();
            } else {
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(LocationBean bean) {
            super.onPostExecute(bean);
            BDLocation location = new BDLocation();
            location.setLatitude(bean.getLat());
            location.setLongitude(bean.getLng());
            getLocSuccess(location);
        }
    }

    // 定位回调
    @Override
    public void getLocSuccess(final BDLocation location) {
        // TODO Auto-generated method stub


        if (!TextUtils.isEmpty(location.getCity()) && !isCustomLocation) {
            loc.setSummary(location.getProvince() + location.getCity()
                    + location.getDistrict());
            new MaterialDialog.Builder(getActivity()).title("定位成功").content("当前位置为" + location.getProvince() + location.getCity()
                    + location.getDistrict() + ",是否切换？").cancelable(false)
                    .positiveText("不用了")
                    .negativeText("切换位置")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            sumbitAddress(location);

                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            isCustomLocation = true;
                            IntentUtil.start_activity(getActivity(), PushManualLocationActivity.class);

                        }
                    })
                    .build().show();


        } else if (isCustomLocation) {
            sumbitAddress(location);
        }
    }

    private void sumbitAddress(final BDLocation location) {
        new Thread() {
            @Override
            public void run() {
                if (String.valueOf(location.getLatitude()).equals("4.9E-324")) {
                    Message message = new Message();
                    message.what = BINDLOCFAIL;
                    mHandler.sendMessage(message);
                } else {
                    bd_location = location;
                    CommonJson<Object> bean = null;
                    bean = getDao().submitLocation(
                            String.valueOf(location.getLongitude()),
                            String.valueOf(location.getLatitude()));
                    Message message = new Message();
                    if (bean != null) {
                        if (bean.getStatus() == 1) {
                            message.what = BINDLOC;
                            message.obj = bean.getData();
                        } else {
                            message.what = DATAERR;
                        }
                    } else {
                        message.what = NETERR;
                    }
                    mHandler.sendMessage(message);
                }
            }
        }.start();
    }

    @Override
    public void getLocPoi(BDLocation poiLocation) {
        // TODO Auto-generated method stub
    }

    /**
     * 数据回调处理
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BINDLOCFAIL:
                    // loc.setChecked(false);
                    Toast.makeText(getActivity(), "定位失败,请手动设置位置",
                            Toast.LENGTH_SHORT).show();
                    IntentUtil.start_result_activity(getActivity(),
                            PushManualLocationActivity.class);
                    break;
                case BINDLOC:
                    isBind = true;
                    Toast.makeText(getActivity(), "定位成功", Toast.LENGTH_SHORT)
                            .show();
                    AppLogger.e("loc" + loc.getSummary().toString());
                    SharePrefUtility.setPushLoc(loc.getSummary().toString());
                    break;

            }
        }

        ;
    };

    private PushDao dao;

    public PushDao getDao() {
        if (dao == null) {
            dao = new PushDao();
        }
        return dao;
    }

    // 提交经纬度
    public void sendAddressToServer() {
        getLocation().setLocListener(this);
        getLocation().onStart();
    }

    private Location location;

    public Location getLocation() {
        if (location == null) {
            location = new Location();
        }
        return location;
    }


    @Subscribe//自定义所在位置
    public void onCustomLcoation(LocationChooseEvent event) {
        new geoCoderTask(event.address).execute();
        loc.setSummary(event.address);
        AppLogger.e("address" + event.address);
    }


}
