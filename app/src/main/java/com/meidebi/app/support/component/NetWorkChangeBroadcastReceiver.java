package com.meidebi.app.support.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.meidebi.app.support.utils.Utility;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;

public class NetWorkChangeBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		if (Utility.isConnected(context)) {
//			PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY,
//					PushUtils.getMetaValue(context, "api_key"));
			if(SharePrefUtility.getPushOn()){
//				JushUtity.startPush();
			}else{
//				JushUtity.stopPush();
 			}
		}

	}

}