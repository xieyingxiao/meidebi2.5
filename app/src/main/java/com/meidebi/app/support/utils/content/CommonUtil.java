package com.meidebi.app.support.utils.content;

import java.io.File;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.meidebi.app.R;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.opengl.GLES10;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import com.meidebi.app.XApplication;

public class CommonUtil {

	/**
	 * 检测sdcard是否可用
	 * 
	 * @return true为可用，否则为不可用
	 */
	public static boolean sdCardIsAvailable() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return true;
	}

	/**
	 * Checks if there is enough Space on SDCard
	 * 
	 * @param updateSize
	 *            Size to Check
	 * @return True if the Update will fit on SDCard, false if not enough space
	 *         on SDCard Will also return false, if the SDCard is not mounted as
	 *         read/write
	 */
	public static boolean enoughSpaceOnSdCard(long updateSize) {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return (updateSize < getRealSizeOnSdcard());
	}

	/**
	 * get the space is left over on sdcard
	 */
	public static long getRealSizeOnSdcard() {
		File path = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * Checks if there is enough Space on phone self
	 * 
	 */
	public static boolean enoughSpaceOnPhone(long updateSize) {
		return getRealSizeOnPhone() > updateSize;
	}

	/**
	 * get the space is left over on phone self
	 */
	public static long getRealSizeOnPhone() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		long realSize = blockSize * availableBlocks;
		return realSize;
	}

	/**
	 * 根据手机分辨率从dp转成px
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f) - 15;
	}
	
    public static int dip2px(int dipValue) {
         float reSize = XApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    public static int px2dip(int pxValue) {
        float reSize = XApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) ((pxValue / reSize) + 0.5);
    }

    public static float sp2px(int spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, XApplication.getInstance().getResources().getDisplayMetrics());
    }

    
	/**
	 * 属性内容处理
	 * 
	 * @param tv
	 *            textview
	 * @param pre
	 *            前缀
	 * @param string
	 *            内容
	 */
	public static void combineTwoText(TextView tv, String first_txt, String sec_txt) {
		if (!TextUtils.isEmpty(first_txt)||!TextUtils.isEmpty(sec_txt))
			tv.setVisibility(View.VISIBLE);
		StringBuffer sb = new StringBuffer();
		if(!TextUtils.isEmpty(first_txt)){
			sb.append(first_txt+" ");
		}
		if(!TextUtils.isEmpty(sec_txt)){
			sb.append(sec_txt);
		}
		tv.setText(sb.toString());
	}
	/**
	 * 属性内容处理
	 * 
	 * @param tv
	 *            textview
	 * @param pre
	 *            前缀
	 * @param string
	 *            内容
	 */
	public static void BuildFieldText(TextView tv, String pre, String string) {
		if (!TextUtils.isEmpty(string))
			tv.setVisibility(View.VISIBLE);
		tv.setText(pre + " " + string);
	}
	

	/**
	 * 文字处理
	 * 
	 * @param tv
	 *            textview
	 * @param pre
	 *            前缀
	 * @param string
	 *            内容
	 */
	public static void BuildFieldText(TextView tv, String pre, Boolean b) {
		String str = null;
		if (b) {
			str = XApplication.getInstance().getString(R.string.yes);
		} else {
			str = XApplication.getInstance().getString(R.string.no);
		}
		tv.setVisibility(View.VISIBLE);
		tv.setText(pre + ":" + str);
	}

	/**
	 * 文字处理
	 * 
	 * @param tv
	 *            textview
	 * @param pre
	 *            前缀
	 * @param string
	 *            内容
	 */
	public static void BuildFieldText(TextView tv, String pre, int time) {
		tv.setVisibility(View.VISIBLE);
		tv.setText(pre + ":" + TimeUtil.getDate(time));
	}

	public static boolean isPkgInstalled(String pkgName) {  
		PackageInfo packageInfo = null;  
		try {  
		    packageInfo = XApplication.getInstance().getPackageManager().getPackageInfo(pkgName, 0);  
		} catch (NameNotFoundException e) {  
		    packageInfo = null;  
		    e.printStackTrace();  
		}  
		if (packageInfo == null) {  
		    return false;  
		} else {  
		    return true;  
		}  
	}
	
    public static int getScreenWidth() {
        Activity activity = XApplication.getInstance().getActivity();
        if (activity != null) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            return metrics.widthPixels;
        }

        return 480;
    }

    public static int getScreenHeight() {
        Activity activity = XApplication.getInstance().getActivity();
        if (activity != null) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            return metrics.heightPixels;
        }
        return 800;
    }

    //sometime can get value, sometime can't, so I define it is 2048x2048
    public static int getBitmapMaxWidthAndMaxHeight() {
        int[] maxSizeArray = new int[1];
        GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSizeArray, 0);

        if (maxSizeArray[0] == 0) {
            GLES10.glGetIntegerv(GL11.GL_MAX_TEXTURE_SIZE, maxSizeArray, 0);
        }
//        return maxSizeArray[0];
        return 2048;
    }

    public static void formatTextView(TextView tv,String text){
        tv.setText(String.format(tv.getText().toString(),text));

    }

    public static void formatTextView(TextView tv,String text,String format_str){
        tv.setText(String.format(format_str.toString(),text));

    }

    public static void formatTextView(TextView tv,String text,int res){
        tv.setText(String.format(XApplication.getInstance().getResources().getString(res),text));

    }

    public static void  formatTextView(TextView tv,boolean iscan){
        tv.setText(String.format(tv.getText().toString(),iscan?"是":"否"));

    }


}
