//package com.meidebi.app.support.component.jpush;
//
//import android.annotation.SuppressLint;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.os.Bundle;
//import android.os.Handler;
//import android.preference.PreferenceManager;
//import android.util.Log;
//
//import com.meidebi.app.XApplication;
//import com.meidebi.app.service.dao.PushDao;
//import com.meidebi.app.ui.msgdetail.MsgDetailActivity;
//
//import cn.jpush.android.api.JPushInterface;
//
///**
// * 自定义接收器
// *
// * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
// */
//public class JpushReceiver extends BroadcastReceiver {
//	private static final String TAG = "JpushReceiver";
//	private PushDao dao;
//
//	public PushDao getDao() {
//		if (dao == null) {
//			dao = new PushDao();
//		}
//		return dao;
//	}
//
////	private JushUtity pushUtity;
////
////	public JushUtity getPushUtity() {
////		if (pushUtity == null) {
////			pushUtity = new JushUtity();
////		}
////		return pushUtity;
////	}
//
//	@Override
//	public void onReceive(Context context, Intent intent) {
//		Bundle bundle = intent.getExtras();
//		Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: "
//				+ printBundle(bundle));
//
//		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//			String regId = bundle
//					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//
//			// }
//			// Log.d(TAG, "接收Registration Id : " + regId);
//			// send the Registration Id to your server...
//		} else if (JPushInterface.ACTION_UNREGISTER.equals(intent.getAction())) {
//			String regId = bundle
//					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//			Log.d(TAG, "接收UnRegistration Id : " + regId);
//			// send the UnRegistration Id to your server...
//		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
//				.getAction())) {
//			Log.d(TAG,
//					"接收到推送下来的自定义消息: "
//							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
//			processCustomMessage(context, bundle);
//
//		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
//				.getAction())) {
//			Log.d(TAG, "接收到推送下来的通知");
//			int notifactionId = bundle
//					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//			Log.d(TAG, "接收到推送下来的通知的ID: " + notifactionId);
//
//		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
//				.getAction())) {
//			Log.d(TAG, "用户点击打开了通知");
//			Intent i = new Intent();
//			i.putExtras(bundle);
//			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			i.putExtra("nofy", true);
//			if (XApplication.getInstance().startedApp) {
//				i.setClass(context, MsgDetailActivity.class);
//			} else {
////				i.setClass(context, MainActivity.class);
//			}
//			Log.d(TAG,
//					"用户收到到RICH PUSH CALLBACK: "
//							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
//			context.startActivity(i);
//		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
//				.getAction())) {
//			Log.d(TAG,
//					"用户收到到RICH PUSH CALLBACK: "
//							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
//			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
//			// 打开一个网页等..
//
//		} else {
//			Log.d(TAG, "Unhandled intent - " + intent.getAction());
//		}
//	}
//
//	// 打印所有的 intent extra 数据
//	private static String printBundle(Bundle bundle) {
//		StringBuilder sb = new StringBuilder();
//		for (String key : bundle.keySet()) {
//			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
//				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
//			} else {
//				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
//			}
//		}
//		return sb.toString();
//	}
//
//	// send msg to MainActivity
//	private void processCustomMessage(Context context, Bundle bundle) {
//		// if (MainActivity.isForeground) {
//		// String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//		// String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//		// Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//		// msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//		// if (!JpushUtil.isEmpty(extras)) {
//		// try {
//		// JSONObject extraJson = new JSONObject(extras);
//		// if (null != extraJson && extraJson.length() > 0) {
//		// msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//		// }
//		// } catch (JSONException e) {
//		//
//		// }
//		//
//		// }
//		// context.sendBroadcast(msgIntent);
//		// }
//	}
//
////	public void BindToServer() {
////		getPushUtity().getIds();
////		if (!getPushUtity().isHasBind()) {
////			new Thread() {
////				@Override
////				public void run() {
////					CommonJson bean = null;
////					String imei = ((TelephonyManager) XApplication
////							.getInstance().getSystemService(
////									Context.TELEPHONY_SERVICE)).getDeviceId();
////					bean = getDao().initPush(null, null, imei);
////					if (bean != null) {
////						Message message = new Message();
////						message.obj = bean.getData();
////						message.what = bean.getStatus();
////						mHandler.sendMessage(message);
////					}
////				}
////			}.start();
////		}
////	}
//
//	/**
//	 * 数据回调处理
//	 */
//	protected Handler mHandler = new Handler() {
//		@SuppressLint("ValidFragment")
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case 0:
//
//				break;
//			case 1:
//				SharedPreferences sp = PreferenceManager
//						.getDefaultSharedPreferences(XApplication.getInstance());
//				Editor editor = sp.edit();
//				editor.putBoolean("hasBind", true);
//				editor.commit();
//				break;
//			case 3:
//				break;
//			case 4:
//				break;
//			}
//		};
//
//	};
//}
