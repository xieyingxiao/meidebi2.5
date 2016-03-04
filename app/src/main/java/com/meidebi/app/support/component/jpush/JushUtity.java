//package com.meidebi.app.support.component.jpush;
//
//import android.app.Activity;
//import android.app.AlarmManager;
//import android.app.Notification;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.SystemClock;
//import android.preference.PreferenceManager;
//
//import com.meidebi.app.R;
//import com.meidebi.app.XApplication;
//import com.meidebi.app.base.config.AppConfigs;
//import com.meidebi.app.support.utils.AppLogger;
//import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
//
//import java.util.Calendar;
//import java.util.Set;
//import java.util.TimeZone;
//
//import cn.jpush.android.api.BasicPushNotificationBuilder;
//import cn.jpush.android.api.JPushInterface;
//
//public class JushUtity {
//	private static String appId = null;
//	private static String channelId = null;
//	private static String clientId = null;
//	private static boolean hasBind = false;
//	public static Set<Integer> days;
//	private static final int NOT_ID = 1;
//	private static String OPENPUSH = "OPEN";
//	public static String STOPPUSH = "STOP";
//
//	public JushUtity() {
//		// init();
//	}
//
//	public static void init() {
//		// TODO Auto-generated method stub
//		JPushInterface.init(XApplication.getInstance());
//		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
//		initSoundAndVB(XApplication.getInstance().getApplicationContext(),
//				SharePrefUtility.getPushSound(), SharePrefUtility.getPushVB());
//		JPushInterface
//				.setLatestNotifactionNumber(XApplication.getInstance(), 5);// 只保留5个
//		stopPush();
//		if (!SharePrefUtility.getPushOn()) {
//			stopPush();
//		} else if (JPushInterface.isPushStopped(XApplication.getInstance())) {
//			startPush();
//		}
//	}
//
//	public static void startPush() {
//		neverStopPush();
//	}
//
//	public static void neverStopPush() {
//		StartAlarmPush(XApplication.getInstance());
//		if (SharePrefUtility.getPushAIMode()) {
//			PushAIMode();
//			ControllalarmManagerPush(XApplication.getInstance(),
//					AppConfigs.WORKDAYPUSHSTATTIME,
//					AppConfigs.WORKDAYPUSHENDTIME, true);
//		} else {
//			JPushInterface.resumePush(XApplication.getInstance());
//		}
//	}
//
//	public static void stopPush() {
//		AppLogger.e("sssss");
//		JPushInterface.stopPush(XApplication.getInstance());
//		StopAlarmPush(XApplication.getInstance());
//		StopControllalarmManagerPush(XApplication.getInstance());
//	}
//
//	public static void getIds() {
//		SharedPreferences sp = PreferenceManager
//				.getDefaultSharedPreferences(XApplication.getInstance());
//		appId = sp.getString("appid", "");
//		channelId = sp.getString("channel_id", "");
//		clientId = sp.getString("user_id", "");
//		hasBind = sp.getBoolean("hasBind", false);
//	}
//
//	public String getAppId() {
//		return appId;
//	}
//
//	public void setAppId(String appId) {
//		this.appId = appId;
//	}
//
//	public String getChannelId() {
//		return channelId;
//	}
//
//	public void setChannelId(String channelId) {
//		this.channelId = channelId;
//	}
//
//	public String getClientId() {
//		return clientId;
//	}
//
//	public void setClientId(String clientId) {
//		this.clientId = clientId;
//	}
//
//	public boolean isHasBind() {
//		return hasBind;
//	}
//
//	public void setHasBind(boolean hasBind) {
//		this.hasBind = hasBind;
//	}
//
//	// public static void setPushTime(int startTime, int endTime, Boolean on) {
//	// SharePrefUtility.setPushSilentMode(on);
//	// if (!on) {
//	// JPushInterface.setPushTime(XApplication.getInstance()
//	// .getApplicationContext(), null, startTime, endTime);
//	// } else {
//	// int rightstart = 0;
//	// int rightend = 0;
//	// if (startTime == 0) {
//	// rightend = 23;
//	// } else {
//	// rightend = startTime - 1;
//	// }
//	// // if(endTime == 24){
//	// // rightstart = 0;
//	// // }
//	// rightstart = endTime;
//	// AppLogger.e("start" + rightstart + "end" + rightend);
//	// JPushInterface.setPushTime(XApplication.getInstance()
//	// .getApplicationContext(), days, rightstart, rightend);
//	// SharePrefUtility.setPushTime(startTime, endTime);
//	//
//	// }
//	// }
//
//	public static void setQuiteTime(Activity activity, int startTime,
//			int endTime, Boolean on) {
//		// SharePrefUtility.setPushSilentMode(on);
//		// if (!on) {
//		// // stopAlarmManager(activity);
//		// JPushInterface.setSilenceTime(XApplication.getInstance()
//		// .getApplicationContext(), 0, 0, 0, 0);
//		// } else {
//		// // alarmManagerPush(activity, startTime, 0, false);
//		// // alarmManagerPush(activity, endTime, 0, true);
//		SharePrefUtility.setPushTime(startTime, endTime);
//
//		// }
//	}
//
//	public static void initSoundAndVB(Context con, Boolean s_on, Boolean vb_on) {
//		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(
//				con);
//		builder.statusBarDrawable = R.drawable.push_icon;
//		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL; // 设置为自动消失
//		// if (s_on) {
//		// builder.notificationDefaults |= Notification.DEFAULT_SOUND;
//		// } else {
//		// builder.notificationDefaults &= ~(Notification.DEFAULT_SOUND);
//		// }
//		// if (vb_on) {
//		// builder.notificationDefaults = Notification.DEFAULT_VIBRATE;
//		// } else {
//		// builder.notificationDefaults = Notification.DEFAULT_VIBRATE;
//		//
//		// builder.notificationDefaults &= ~(Notification.DEFAULT_VIBRATE);
//		// }
//		// ;
//		// 声音和震动
//		if (s_on && vb_on) {
//			builder.notificationDefaults = Notification.DEFAULT_SOUND
//					| Notification.DEFAULT_VIBRATE;
//		} else if (s_on && !vb_on) {
//			builder.notificationDefaults = Notification.DEFAULT_SOUND;
//			builder.notificationDefaults &= ~(Notification.DEFAULT_VIBRATE);
//		} else if (!s_on && vb_on) {
//			builder.notificationDefaults = Notification.DEFAULT_VIBRATE;
//			builder.notificationDefaults &= ~(Notification.DEFAULT_SOUND);
//		} else {
//			builder.notificationDefaults = ~(Notification.DEFAULT_SOUND)
//					& ~(Notification.DEFAULT_VIBRATE);
//			// builder.notificationDefaults |= ~(Notification.DEFAULT_VIBRATE);
//			AppLogger.e("b" + builder.notificationDefaults);
//		}
//		JPushInterface.setPushNotificationBuilder(NOT_ID, builder);
//		// SharePrefUtility.setPushSound(s_on);
//		// SharePrefUtility.setPushVB(vb_on);
//	}
//
//	// public static void setSoundAndVB(Context con, Boolean s_on, Boolean
//	// vb_on) {
//	// BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(
//	// con);
//	// builder.statusBarDrawable = R.drawable.ic_launcher;
//	// builder.notificationFlags = Notification.FLAG_AUTO_CANCEL; // 设置为自动消失
//	// if(s_on){
//	// builder.notificationDefaults |= ~(Notification.DEFAULT_SOUND);
//	// SharePrefUtility.setPushSound(!SharePrefUtility.getPushSound());
//	// }
//	// if(vb_on){
//	// builder.notificationDefaults |= ~(Notification.DEFAULT_VIBRATE);
//	// SharePrefUtility.setPushVB(!SharePrefUtility.getPushVB());
//	// }
//	// JPushInterface.setPushNotificationBuilder(NOT_ID, builder);
//	// }
//	/*
//	 * 自定义推送时间
//	 */
//	public static void alarmManagerPush(Activity activity, int startTime,
//			int min, Boolean on) {
//		PendingIntent sender = null;
//		Intent intent = new Intent(activity, JpushOnRepateAlarmReceiver.class);
//		intent.setAction(OPENPUSH);
//		sender = PendingIntent.getBroadcast(activity, 10087, intent, 0);
//		long firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)
//		long systemTime = System.currentTimeMillis();
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(System.currentTimeMillis());
//		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 这里时区需要设置一下，不然会有8个小时的时间差
//		calendar.set(Calendar.MINUTE, min);
//		calendar.set(Calendar.HOUR_OF_DAY, startTime);
//		calendar.set(Calendar.SECOND, 0);
//		calendar.set(Calendar.MILLISECOND, 0);
//		// 选择的每天定时时间
//		long selectTime = calendar.getTimeInMillis();
//		// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
//		if (systemTime > selectTime) {
//			calendar.add(Calendar.DAY_OF_MONTH, 1);
//			selectTime = calendar.getTimeInMillis();
//		}
//		// 计算现在时间到设定时间的时间差
//		long time = selectTime - systemTime;
//		firstTime += time;
//		// 进行闹铃注册
//		AlarmManager manager = (AlarmManager) activity
//				.getSystemService(Context.ALARM_SERVICE);
//		// manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
//		// 1000 * 60 * 60 * 24, sender);// 间隔时间为一天
//		manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
//				1000 * 60 * 3, sender);// 间隔时间为一天
//	}
//
//	public static void ControllalarmManagerPush(Context activity,
//			int startTime, int min, Boolean on) {
//		PendingIntent sender = null;
//		if (on) {
//			Intent intent = new Intent(activity, ControllAlarmReceiver.class);
//			intent.setAction(OPENPUSH);
//			sender = PendingIntent.getBroadcast(activity, 10087, intent, 0);
//		}
//		long firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)
//		long systemTime = System.currentTimeMillis();
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(System.currentTimeMillis());
//		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 这里时区需要设置一下，不然会有8个小时的时间差
//		calendar.set(Calendar.MINUTE, min);
//		calendar.set(Calendar.HOUR_OF_DAY, startTime);
//		calendar.set(Calendar.SECOND, 0);
//		calendar.set(Calendar.MILLISECOND, 0);
//		// 选择的每天定时时间
//		long selectTime = calendar.getTimeInMillis();
//		// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
//		if (systemTime > selectTime) {
//			calendar.add(Calendar.DAY_OF_MONTH, 1);
//			selectTime = calendar.getTimeInMillis();
//		}
//		// 计算现在时间到设定时间的时间差
//		long time = selectTime - systemTime;
//		firstTime += time;
//		// 进行闹铃注册
//		AlarmManager manager = (AlarmManager) activity
//				.getSystemService(Context.ALARM_SERVICE);
//		manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
//				AppConfigs.STARTALARMCONTROLLER, sender);// 间隔时间为一天
//	}
//
//	public static void StopControllalarmManagerPush(Context context) {
//		Intent intent = new Intent(context, ControllAlarmReceiver.class);
//		intent.setAction(OPENPUSH);
//		PendingIntent sender = PendingIntent.getBroadcast(context, 10087,
//				intent, 0);
//		AlarmManager manager = (AlarmManager) context
//				.getSystemService(Context.ALARM_SERVICE);
//		manager.cancel(sender);
//	}
//
//	public static void StopAlarmPush(Context context) {
//		if (SharePrefUtility.getPushAIMode()) {
//			Intent intent = new Intent(context,
//					JpushOnRepateAlarmReceiver.class);
//			intent.setAction(OPENPUSH);
//			PendingIntent sender = PendingIntent.getBroadcast(context, 10088,
//					intent, 0);
//			AlarmManager manager = (AlarmManager) context
//					.getSystemService(Context.ALARM_SERVICE);
//			manager.cancel(sender);
//		}
//	}
//
//	public static void PushAIMode() {
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(System.currentTimeMillis());
//		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 这里时区需要设置一下，不然会有8个小时的时间差
//		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//		int hour = calendar.get(Calendar.HOUR_OF_DAY);
//		AppLogger.e("dayOfWeek"+dayOfWeek);
//		if (dayOfWeek==1||dayOfWeek==7) {
//			if (hour >= AppConfigs.WEEKENDPUSHSTATTIME
//					&& hour < AppConfigs.WEEKENDPUSHENDTIME) {
//				AppLogger.e("weekend_start");
//				openPush(XApplication.getInstance());
//			} else {
//				AppLogger.e("weekend_end");
//				StopPush(XApplication.getInstance());
//			}
//		} else {
//			if (hour >= AppConfigs.WORKDAYPUSHSTATTIME
//					&& hour < AppConfigs.WORKDAYPUSHENDTIME) {
//				AppLogger.e("workday_start");
//				openPush(XApplication.getInstance());
//			} else {
//				AppLogger.e("workday_end");
//				StopPush(XApplication.getInstance());
//			}
//		}
//	}
//
//	public static void openPush(Context context) {
//		if (JPushInterface.isPushStopped(context)) {
//			JPushInterface.resumePush(context);
//		}
//	}
//
//	public static void StopPush(Context context) {
//		JPushInterface.stopPush(context);
//
//	}
//
//	public static void StartAlarmPush(Context context) {// 半小时开启service
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(System.currentTimeMillis());
//		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 这里时区需要设置一下，不然会有8个小时的时间差
//		int min = calendar.get(Calendar.MINUTE);
//		int hour = calendar.get(Calendar.HOUR_OF_DAY);
//		if (min <= 30) {
//			min = 30;
//		} else {
//			min = 0;
//			hour = hour + 1;
//		}
//		calendar.set(Calendar.MINUTE, min);
//		calendar.set(Calendar.HOUR_OF_DAY, hour);
//		calendar.set(Calendar.SECOND, 0);
//		calendar.set(Calendar.MILLISECOND, 0);
//		long selectTime = calendar.getTimeInMillis();
//		long firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)
//		long systemTime = System.currentTimeMillis();
//		long time = selectTime - systemTime;
//		firstTime += time;
//		Intent intent = new Intent(context, JpushOnRepateAlarmReceiver.class);
//		intent.setAction(OPENPUSH);
//		PendingIntent sender = PendingIntent.getBroadcast(context, 10088,
//				intent, 0);
//		AlarmManager manager = (AlarmManager) context
//				.getSystemService(Context.ALARM_SERVICE);
//		manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
//				AppConfigs.CHECKPUSHRUN, sender);//
//	}
//
//}
