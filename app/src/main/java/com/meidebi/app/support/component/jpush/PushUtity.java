package com.meidebi.app.support.component.jpush;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.meidebi.app.XApplication;

public class PushUtity {
	private static String appId = null;
	public static String channelId = null;
	public static String clientId = null;
	private static boolean hasBind = false;

	public PushUtity() {
	}

//	public void startWork() {
//		
//		PushManager.startWork(XApplication.getInstance(),
//				PushConstants.LOGIN_TYPE_API_KEY,
//				PushUtils.getMetaValue(XApplication.getInstance(), "api_key"));
//	}
//
//	public void init(Activity activity) {
//		Resources resource = XApplication.getInstance().getResources();
//		String pkgName = XApplication.getInstance().getPackageName();
//		CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
//				resource.getIdentifier("notification_custom_builder", "layout",
//						pkgName), resource.getIdentifier("notification_icon",
//						"id", pkgName), resource.getIdentifier(
//						"notification_title", "id", pkgName),
//				resource.getIdentifier("notification_text", "id", pkgName));
//		cBuilder.setNotificationFlags(Notification.FLAG_ONGOING_EVENT);
//		cBuilder.setNotificationFlags(Notification.FLAG_NO_CLEAR);
//		cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
//				| Notification.DEFAULT_VIBRATE);
//		cBuilder.setStatusbarIcon(XApplication.getInstance()
//				.getApplicationInfo().icon);
//		cBuilder.setLayoutDrawable(resource.getIdentifier(
//				"simple_notification_icon", "drawable", pkgName));
//		PushManager.setNotificationBuilder(activity, 1, cBuilder);
//	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public boolean isHasBind() {
		return hasBind;
	}

	public void setHasBind(boolean hasBind) {
		this.hasBind = hasBind;
	}

	public static void getIds() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(XApplication.getInstance());
		appId = sp.getString("appid", "");
		channelId = sp.getString("channel_id", "");
		clientId = sp.getString("user_id", "");
		hasBind = sp.getBoolean("hasBind", false);
	}

//	public void restartWork() {
//		if (Utility.isConnected(XApplication.getInstance())) {
//			getIds();
//			if (!isHasBind()) {
//				PushManager.startWork(XApplication.getInstance(),
//						PushConstants.LOGIN_TYPE_API_KEY, PushUtils
//								.getMetaValue(XApplication.getInstance(),
//										"api_key"));
//			}
//		}
//	}

}
