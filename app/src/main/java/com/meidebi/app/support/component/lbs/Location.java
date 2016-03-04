package com.meidebi.app.support.component.lbs;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Vibrator;
import android.preference.PreferenceManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.meidebi.app.XApplication;

public class Location {
	private static Location instance = new Location();
	private ILocListener locListener;
	public static Location getInstance() {
		return instance;
	}

	private LocationClient mLocationClient = null;
	private MyLocationListenner myListener = new MyLocationListenner();
	public Vibrator mVibrator01;
	public String TAG = "Location";
	private static String lat, lon;
	public GeofenceClient mGeofenceClient;
 
	public ILocListener getLocListener() {
		return locListener;
	}

	public void setLocListener(ILocListener locListener) {
		this.locListener = locListener;
	}

	public Location() {
		getLoc();
//		if (TextUtils.isEmpty(lat)) {
//			onStart();
//		}
	}

	// 设置相关参数
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(false); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setPoiExtraInfo(false);
		option.setScanSpan(500); // 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
		option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
		option.setPoiNumber(10);
		option.setAddrType("all");
		option.setPoiExtraInfo(true);
		option.disableCache(true);
		mLocationClient.setLocOption(option);
	}

	public void onStart() {
		mLocationClient = new LocationClient(XApplication.getInstance());
		/**
		 * ——————————————————————————————————————————————————————————————————
		 * 这里的AK和应用签名包名绑定，如果使用在自己的工程中需要替换为自己申请的Key
		 * ——————————————————————————————————————————————————————————————————
		 */

		mLocationClient.setAK("3286c125d438e9699e92b3c052857b62");
		mLocationClient.registerLocationListener(myListener);
		setLocationOption();
		mLocationClient.start();
 		// 位置提醒相关代码
		// mNotifyer = new NotifyLister();
		// mNotifyer.SetNotifyLocation(40.047883,116.312564,3000,"gps");//4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
		// mLocationClient.registerNotify(mNotifyer);

	}

	/**
	 * 监听函数，有更新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			saveLoc(String.valueOf(location.getLatitude()),
					String.valueOf(location.getLongitude()));
			getLocListener().getLocSuccess(location);
			 
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if(poiLocation!=null){
				getLocListener().getLocPoi(poiLocation);
 			}
		}
	}

	public void saveLoc(String lat, String lon) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(XApplication.getInstance());
		Editor editor = sp.edit();
		editor.putString("lat", lat);
		editor.putString("lon", lon);
		editor.commit();
	}

	public static void getLoc() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(XApplication.getInstance());
		lat = sp.getString("lat", "");
		lon = sp.getString("lon", "");
	}
}