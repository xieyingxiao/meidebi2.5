package com.meidebi.app.ui.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.meidebi.app.R;
import com.meidebi.app.support.utils.anim.AnimateFirstDisplayListener;
import com.meidebi.app.ui.widget.ViewLoading;
import com.meidebi.app.ui.widget.action.ILoadingAction;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;

public abstract class BaseFragmentActivity extends ActionBarActivity implements
        ILoadingAction {
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    protected Fragment mContent = null;
    public final int NETERR = 404;
    public final int DATAERR = 400;
    protected ViewLoading view_load;
    private MaterialDialog lastDialog = null;


    protected ViewLoading getView_load() {
        if (view_load == null) {
            view_load = new ViewLoading(this);
            view_load.setAction(this);
        }
        return view_load;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
//        hideNavigation();
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onBackPressed() {
        AnimateFirstDisplayListener.displayedImages.clear();
        imageLoader.stop();
        super.onBackPressed();
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.zoom_in_center, R.anim.slide_out_right);
    }

    public void defaultFinish() {
        super.finish();
    }

    /**
     * 替换通用的fragment
     *
     * @param fragment 传人需要的frgament
     * @param resId    替换的id
     */
    protected void replaceFragment(int resId,
                                   Fragment fragment) {
        if (mContent != fragment) {
            mContent = fragment;
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.replace(resId, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    /**
     * 替换通用的fragment
     *
     * @param fragment 传人需要的frgament
     */
    protected void addFragment(Fragment fragment) {
        if (mContent != fragment) {
            mContent = fragment;
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.add(R.id.common_fragment, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public void switchContent(Fragment from,
                              Fragment to, int rid) {
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction = this.getSupportFragmentManager()
                    .beginTransaction();
            transaction.setCustomAnimations(R.anim.alpha_in, R.anim.alpha_out);
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(from).add(rid, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    public void switchContentWithBack(Fragment from,
                                      Fragment to) {
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction = this.getSupportFragmentManager()
                    .beginTransaction();
            transaction.setCustomAnimations(R.anim.alpha_in, R.anim.alpha_out);
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(from).add(R.id.common_fragment, to); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to); // 隐藏当前的fragment，显示下一个
            }
            transaction.commitAllowingStateLoss();
        }
    }

    public void showLoading() {
        dissmissDialog();
        lastDialog = new MaterialDialog.Builder(this).backgroundColor(Color.WHITE)
                .customView(R.layout.dialog_progressbar, true)
                .cancelable(false)
                .build();
        lastDialog.show();
    }

    public void showLoading(int res) {
        dissmissDialog();
        lastDialog = new MaterialDialog.Builder(this).backgroundColor(Color.WHITE)
                .customView(R.layout.dialog_progressbar, true)
                .cancelable(false)
                .build();
        ((TextView) lastDialog.getCustomView().findViewById(R.id.tv_dialog_progress)).setText(getString(res));
        lastDialog.show();

    }

    public void showLoading(String res) {
        dissmissDialog();
        lastDialog = new MaterialDialog.Builder(this).backgroundColor(Color.WHITE)
                .customView(R.layout.dialog_progressbar, true)
                .cancelable(false)
                .build();
        ((TextView) lastDialog.getCustomView().findViewById(R.id.tv_dialog_progress)).setText(res);
        lastDialog.show();
    }

    public void showErr(int res) {
        dissmissDialog();
        lastDialog = new MaterialDialog.Builder(this).backgroundColor(Color.WHITE)
                .content(res)
                .show();

    }

    public void showErr(String res) {
        dissmissDialog();
        lastDialog = new MaterialDialog.Builder(this).backgroundColor(Color.WHITE)
                .content(res)
                .show();

    }

    public void dissmissDialog() {
        if (lastDialog != null) {
            lastDialog.dismiss();
            lastDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            System.out.println("ORIENTATION_LANDSCAPE="
                    + Configuration.ORIENTATION_LANDSCAPE);// 当前为横屏

        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            System.out.println("ORIENTATION_PORTRAIT="
                    + Configuration.ORIENTATION_PORTRAIT);// 当前为竖屏

        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onReload() {
        // TODO Auto-generated method stub

    }


    protected void doBroadCastAction(Intent intent) {

    }

    public void setConfigCallback(WindowManager windowManager) {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field = field.getType().getDeclaredField("mBrowserFrame");
            field = field.getType().getDeclaredField("sConfigCallback");
            field.setAccessible(true);
            Object configCallback = field.get(null);

            if (null == configCallback) {
                return;
            }
            field = field.getType().getDeclaredField("mWindowManager");
            field.setAccessible(true);
            field.set(configCallback, windowManager);
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // case R.id.action_refresh:
            // // mContentFragment.loadFirstPageAndScrollToTop();
            // return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    protected abstract int getLayoutResource();


}
