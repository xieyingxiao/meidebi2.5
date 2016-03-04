package com.meidebi.app.support.component;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.meidebi.app.ui.base.BaseFragmentActivity;
import com.umeng.fb.ConversationActivity;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateConfig;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class UmengUtil {
	FeedbackAgent agent;// 友盟意见反馈
	private Activity activity;
	public UmengUtil(Activity activity) {
		this.activity = activity;
	}

	public void onStart() {
//		agent = new FeedbackAgent(activity);
//		agent.sync();
		UpdateConfig.setDebug(false);
		UmengUpdateAgent.update(activity);
	}

	public static void destroyUpdate() {
		UmengUpdateAgent.setUpdateOnlyWifi(true);
		UmengUpdateAgent.setUpdateAutoPopup(true);
		UmengUpdateAgent.setUpdateListener(null);
		UmengUpdateAgent.setDownloadListener(null);
		UmengUpdateAgent.setDialogListener(null);
	}

	public void startFb(Activity activity) {
		Intent intent = new Intent();
		intent.setClass(activity, ConversationActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(intent);
//		agent.startFeedbackActivity();
	}

	public void checkUpdate() {
		// 如果想程序启动时自动检查是否需要更新， 把下面两行代码加在Activity 的onCreate()函数里。
//		UmengUpdateAgent.setUpdateCheckConfig(false);
        ((BaseFragmentActivity)activity).showLoading("正在检查");

		UmengUpdateAgent.setUpdateOnlyWifi(true); // 目前我们默认在Wi-Fi接入情况下才进行自动提醒。如需要在其他网络环境下进行更新自动提醒，则请添加该行代码
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(updateListener);
		UmengUpdateAgent.setDownloadListener(new UmengDownloadListener() {

			@Override
			public void OnDownloadStart() {
				Toast.makeText(activity, "download start",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void OnDownloadUpdate(int progress) {
				Toast.makeText(activity,
						"download progress : " + progress + "%",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void OnDownloadEnd(int result, String file) {
				// Toast.makeText(activity, "download result : " + result ,
				// Toast.LENGTH_SHORT).show();
				Toast.makeText(activity, "download file path : " + file,
						Toast.LENGTH_SHORT).show();
			}
		});
		UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {

			@Override
			public void onClick(int status) {
				switch (status) {
				case UpdateStatus.Update:
					break;
				case UpdateStatus.Ignore:
					break;
				case UpdateStatus.NotNow:
					break;
				}
			}
		});
		UmengUpdateAgent.forceUpdate(activity);
	}

	private UmengUpdateListener updateListener = new UmengUpdateListener() {
		@Override
		public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
			switch (updateStatus) {
			case 0: // has update
				Log.i("--->", "callback result");
                ((BaseFragmentActivity)activity).dissmissDialog();

                UmengUpdateAgent.showUpdateDialog(activity, updateInfo);
				break;
			case 1: // has no updateu
                ((BaseFragmentActivity)activity).dissmissDialog();
                Toast.makeText(activity, "没有更新", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2: // none wifi
                ((BaseFragmentActivity)activity).dissmissDialog();
                Toast.makeText(activity, "没有wifi连接， 只在wifi下更新",
						Toast.LENGTH_SHORT).show();
				break;
			case 3: // time out
                ((BaseFragmentActivity)activity).dissmissDialog();
                Toast.makeText(activity, "超时", Toast.LENGTH_SHORT).show();
				break;
			case 4: // is updating
                ((BaseFragmentActivity)activity).dissmissDialog();

				/*
				 * Toast.makeText(activity, "正在下载更新...",
				 * Toast.LENGTH_SHORT) .show();
				 */
				break;
			}

		}
	};
}
