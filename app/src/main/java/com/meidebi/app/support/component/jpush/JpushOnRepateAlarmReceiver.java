//package com.meidebi.app.support.component.jpush;
//
//import java.util.Calendar;
//import java.util.TimeZone;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import cn.jpush.android.api.JPushInterface;
//
//import com.meidebi.app.XApplication;
//import com.meidebi.app.base.config.AppConfigs;
//import com.meidebi.app.support.utils.AppLogger;
//import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
//
//public class JpushOnRepateAlarmReceiver extends BroadcastReceiver {
//
//	@Override
//	public void onReceive(Context context, Intent intent) {
//		if (SharePrefUtility.getPushOn()) {
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTimeInMillis(System.currentTimeMillis());
//			calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 这里时区需要设置一下，不然会有8个小时的时间差
//			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//			int hour = calendar.get(Calendar.HOUR_OF_DAY);
//			if (dayOfWeek == 1 || dayOfWeek == 7) {
//				if (hour >= AppConfigs.WEEKENDPUSHSTATTIME
//						&& hour < AppConfigs.WEEKENDPUSHENDTIME) {
//					AppLogger.e("weekend_start");
//					openPush(context);
//				} else {
//					AppLogger.e("weekend_end");
//					StopPush(context);
//					if (hour >= AppConfigs.WEEKENDPUSHENDTIME) {
//						AppLogger.e("weekend_stop");
//						JushUtity.StopAlarmPush(context);
//					}
//				}
//			} else {
//				if (hour >= AppConfigs.WORKDAYPUSHSTATTIME
//						&& hour < AppConfigs.WORKDAYPUSHENDTIME) {
//					AppLogger.e("workday_start");
//					openPush(context);
//				} else {
//					AppLogger.e("workday_end");
//					StopPush(context);
//					if (hour >= AppConfigs.WORKDAYPUSHENDTIME) {
//						AppLogger.e("workday_stop");
//						JushUtity.StopAlarmPush(context);
//					}
//				}
//			}
//		}
//	}
//
//	public void openPush(Context context) {
//		if (JPushInterface.isPushStopped(context)) {
//			JPushInterface.resumePush(context);
//			JPushInterface.setLatestNotifactionNumber(
//					XApplication.getInstance(), 5);// 只保留5个
//		}
//	}
//
//	public void StopPush(Context context) {
//		JPushInterface.stopPush(context);
//
//	}
//
//}
