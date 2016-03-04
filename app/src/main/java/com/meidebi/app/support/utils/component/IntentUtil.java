package com.meidebi.app.support.utils.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.Pair;
import android.view.View;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;

import org.apache.http.message.BasicNameValuePair;

@SuppressLint("NewApi")
public class IntentUtil {
	private static int requestCode = 1;
	public final static int cropImageCode =200;
	public static void start_activity(Activity activity, Class<?> cls,
			BasicNameValuePair... name) {
		Intent intent = new Intent();
		intent.setClass(activity, cls);
		for (int i = 0; i < name.length; i++) {
			intent.putExtra(name[i].getName(), name[i].getValue());
		}
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.slide_in_right,
				R.anim.zoom_out_center);
	}
	
	public static void start_activity_normal(Activity activity, Class<?> cls,
			BasicNameValuePair... name) {
		Intent intent = new Intent();
		intent.setClass(activity, cls);
		for (int i = 0; i < name.length; i++) {
			intent.putExtra(name[i].getName(), name[i].getValue());
		}
		activity.startActivity(intent);
	}
	

	public static void start_activity(Activity activity, Class<?> cls,
			Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(activity, cls);
		intent.putExtras(bundle);
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.slide_in_right,
				R.anim.zoom_out_center);
	}

	public static void start_bean_to_activity(Activity activity, Class<?> cls,
			BasicNameValuePair... name) {
		Intent intent = new Intent();
		intent.setClass(activity, cls);
		for (int i = 0; i < name.length; i++) {
			intent.putExtra(name[i].getName(), name[i].getValue());
		}
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.slide_in_right,
				R.anim.zoom_out_center);
	}

	public static void start_result_activity(Activity activity, Class<?> cls,
			BasicNameValuePair... name) {
		Intent intent = new Intent();
		intent.setClass(activity, cls);
		for (int i = 0; i < name.length; i++) {
			intent.putExtra(name[i].getName(), name[i].getValue());
		}
		activity.startActivityForResult(intent, requestCode);
		activity.overridePendingTransition(R.anim.slide_in_right,
				R.anim.zoom_out_center);
	}

	// public static void nofystartActivity(Intent intent, Context activtty,
	// Class<?> cls) {
	// Intent aIntent = new Intent();
	// aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// aIntent.setClass(activtty, cls);
	// aIntent.putExtra("nofy", true);
	// String title = intent
	// .getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
	// aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_TITLE, title);
	// String content = intent
	// .getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
	// aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT, content);
	// String ex_content = intent.getStringExtra(PushConstants.EXTRA_EXTRA);
	// aIntent.putExtra(PushConstants.EXTRA_EXTRA, ex_content);
	// activtty.startActivity(aIntent);
	// }

	public static void jumpToBroswer(String url, Activity activtty) {
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		// 加下面这句话就是启动系统自带的浏览器打开上面的网址， 不加下面一句话， 如果你有多个浏览器，就会弹出让你选择某一浏览器，
		// 然后改浏览器就会打开该网址...............
		// intent.setClassName("com.android.browser",
		// "com.android.browser.BrowserActivity");
		activtty.startActivity(intent);
	}

	public static void jumpToBroswerActivity(Activity activtty, Class<?> cls,
			BasicNameValuePair... name) {
		Intent intent = new Intent();
		intent.setClass(activtty, cls);
		for (int i = 0; i < name.length; i++) {
			intent.putExtra(name[i].getName(), name[i].getValue());
		}
		activtty.startActivity(intent);
		activtty.overridePendingTransition(R.anim.present_up,
				R.anim.zoom_out_center);
	}

	public static void sendActionBroadCast(String action) {
		Intent intent = new Intent();
 		intent.setAction(action);
		LocalBroadcastManager.getInstance(XApplication.getInstance())
				.sendBroadcast(intent);
	}
	
	public static void JumpToCrop(Uri mUri,Activity activity){
        if(null == mUri)return;  
        Intent intent = new Intent();  
        intent.setAction("com.android.camera.action.CROP");  
        intent.setDataAndType(mUri, "image/*");// mUri是已经选择的图片Uri  
        intent.putExtra("crop", "true");  
        intent.putExtra("aspectX", 1);// 裁剪框比例  
        intent.putExtra("aspectY", 1);  
        intent.putExtra("outputX", 150);// 输出图片大小  
        intent.putExtra("outputY", 150);  
        intent.putExtra("return-data", true);  
        activity.startActivityForResult(intent, cropImageCode);  
	}

    public static void AnimationJump(Bundle bundle,Activity activity,Class<?>  targerActivity, Pair<View, String>... sharedElements){
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElements);
        Intent intent = new Intent(activity,targerActivity);
        intent.putExtras(bundle);
        activity.startActivity(intent, transitionActivityOptions.toBundle());

    }
}
