package com.meidebi.app.support.utils.content;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.meidebi.app.XApplication;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;

/**
 * User: qii
 * Date: 13-1-18
 * from top to bottom:statusbar, actionbar, app content, keyboard
 */
public class EmoPickerUtility {
    public static void hideSoftInput(View paramEditText) {
        ((InputMethodManager) XApplication.getInstance().getSystemService("input_method")).hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
    }

    public static void showKeyBoard(final View paramEditText) {
        paramEditText.requestFocus();
        paramEditText.post(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager) XApplication.getInstance().getSystemService("input_method")).showSoftInput(paramEditText, 0);
            }
        });
    }


    public static int getScreenHeight(Activity paramActivity) {
        Display display = paramActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static int getStatusBarHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.top;

    }

    public static int getActionBarHeight(Activity paramActivity) {
        //test on samsung 9300 android 4.1.2, this value is 96px
        //but on galaxy nexus android 4.2, this value is 146px
        //statusbar height is 50px
        //I guess 4.1 Window.ID_ANDROID_CONTENT contain statusbar
        int contentViewTop =
                paramActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

//        return contentViewTop - getStatusBarHeight(paramActivity);

        int[] attrs = new int[]{com.meidebi.app.R.attr.actionBarSize};
        TypedArray ta = paramActivity.obtainStyledAttributes(attrs);
        return ta.getDimensionPixelSize(0, CommonUtil.dip2px(48));
    }

    //below status bar,include actionbar, above softkeyboard
    public static int getAppHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.height();
    }

    //below actionbar, above softkeyboard
    public static int getAppContentHeight(Activity paramActivity) {
        return EmoPickerUtility.getScreenHeight(paramActivity)
                - EmoPickerUtility.getStatusBarHeight(paramActivity)
                - EmoPickerUtility.getKeyboardHeight(paramActivity);
    }

    public static int getKeyboardHeight(Activity paramActivity) {

        int height = EmoPickerUtility.getScreenHeight(paramActivity)
                - EmoPickerUtility.getStatusBarHeight(paramActivity)
                - EmoPickerUtility.getAppHeight(paramActivity);
        if (height == 0) {
            height = SharePrefUtility.getDefaultSoftKeyBoardHeight();
        }

        SharePrefUtility.setDefaultSoftKeyBoardHeight(height);

        return height;
    }

    public static boolean isKeyBoardShow(Activity paramActivity) {
        int height = EmoPickerUtility.getScreenHeight(paramActivity)
                - EmoPickerUtility.getStatusBarHeight(paramActivity)
                - EmoPickerUtility.getAppHeight(paramActivity);
        return height != 0;
    }
}
