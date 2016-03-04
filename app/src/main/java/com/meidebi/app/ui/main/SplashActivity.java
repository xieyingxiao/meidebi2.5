package com.meidebi.app.ui.main;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;

import com.meidebi.app.R;
import com.meidebi.app.service.dao.CategoryDao;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.base.BaseFragmentActivity;

import java.util.List;

public class SplashActivity extends BaseFragmentActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        hideNavigation();
        CategoryDao.getCategory(null);
        if (!hasShortcut(this)) {
            checkShortCut();
        }
//		if (!XApplication.getInstance().startedApp) {// 运行中
        View view = View.inflate(this, R.layout.activity_splash, null);
        setContentView(view);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goHome();
            }
        }, 1000);
//		} else {
//			defaultFinish();
//		}

    }


    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int getLayoutResource() {
        return 0;
    }

    private void goHome() {

        if (SharePrefUtility.firstStart()) {
            IntentUtil.start_activity(this, GuideActivity.class);
        } else {
            IntentUtil.start_activity(this, MainTabHostActivity.class);
        }
        finish();
    }

    public void checkShortCut() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(SplashActivity.this);
        // 是否在桌面上添加了快捷方式
        boolean never_check_shortCut = sp.getBoolean("never_check_shortCut",
                false);
        // 存在快捷方式或者不允许添加，return
        if (never_check_shortCut) {
            return;
        } else {
            addShortCut();
            // 保存已经添加了快捷方式的信息，以便程序下次启动的不再提示
            Editor editor = sp.edit();
            editor.putBoolean("never_check_shortCut", true);
            editor.commit();
        }
    }

    public void addShortCut() {
        // 添加快捷方式
        // 指定快捷方式的Action
        Intent installShortCut = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        // 添加快捷方式的名称
        installShortCut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                getString(R.string.app_name));
        installShortCut.putExtra("duplicate", false);
        ComponentName comp = new ComponentName(
                SplashActivity.this.getPackageName(), "."
                + SplashActivity.this.getLocalClassName());
        installShortCut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
                Intent.ACTION_MAIN).setComponent(comp));
        // 指定图标
        ShortcutIconResource iconRes = ShortcutIconResource.fromContext(
                SplashActivity.this, R.drawable.ic_launcher);
        installShortCut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        // 发送广播
        sendBroadcast(installShortCut);
    }

    /**
     * 判断桌面是否已添加快捷方式
     *
     * @param cx
     * @param titleName 快捷方式名称
     * @return
     */
    public boolean hasShortcut(Context cx) {
        boolean result = false;
        // 获取当前应用名称
        String title = null;
        try {
            final PackageManager pm = cx.getPackageManager();
            title = pm.getApplicationLabel(
                    pm.getApplicationInfo(cx.getPackageName(),
                            PackageManager.GET_META_DATA)).toString();
        } catch (Exception e) {
        }

        String uriStr;
        uriStr = getAuthorityFromPermission(cx, "READ_SETTINGS");
        if (TextUtils.isEmpty(uriStr)) {
            uriStr = getAuthorityFromPermission(cx, "WRITE_SETTINGS");
        }
        if (!TextUtils.isEmpty(uriStr)) {
            final Uri CONTENT_URI = Uri.parse(uriStr);
            final Cursor c = cx.getContentResolver().query(CONTENT_URI, null,
                    "title=?", new String[]{title}, null);
            if (c != null && c.getCount() > 0) {
                result = true;
            }
        }
        return result;
    }

    static String getAuthorityFromPermission(Context context, String permission) {
        if (permission == null)
            return null;
        List<PackageInfo> packs = context.getPackageManager()
                .getInstalledPackages(PackageManager.GET_PROVIDERS);
        if (packs != null) {
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (provider.readPermission != null) {
                            if (provider.readPermission.toString().contains(
                                    permission))
                                return provider.authority;
                        }
                        if (provider.writePermission != null) {
                            if (provider.writePermission.toString().contains(
                                    permission))
                                return provider.authority;
                        }
                    }
                }
            }
        }
        return null;
    }

    int currentApiVersion;

    public void hideNavigation() {

        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }

    }


    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
