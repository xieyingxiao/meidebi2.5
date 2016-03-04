package com.meidebi.app.support.component.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;

public class ControllAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		AppLogger.e("Just A New Day");
		if(SharePrefUtility.getPushOn()){
//		JushUtity.StartAlarmPush(context);
		}
	}
}
