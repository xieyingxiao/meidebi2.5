package com.meidebi.app.ui.browser;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class BrowserWebActivity extends BasePullToRefreshActivity implements View.OnClickListener, ObservableScrollViewCallbacks {
    private String url;
    @InjectView(R.id.toolbarlayout)
    View actionbarView;
    @InjectView(R.id.progressbar)
    ProgressBar mProgressBar;
    @InjectView(R.id.iv_titlebar_back)
    ImageView iv_back;
    @InjectView(R.id.iv_titlebar_forward)
    ImageView iv_forward;
    @InjectView(R.id.iv_titlebar_refresh)
    ImageView iv_refresh;
    @InjectView(R.id.webView)
    ObservableWebView mWebView;
    @InjectView(R.id.bottom_tab)
    View Bottombar;
    private boolean mIsWebViewAvailable;
    private String mUrl = null;
    private boolean isHtml;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUseSwipe(false);
        super.onCreate(savedInstanceState);
        if (mWebView != null) {
            mWebView.destroy();
        }
        ButterKnife.inject(this);
        url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
//		setContentView(R.layout.activity_browser);
        // CheatSheet.setup(BrowserWebActivity.this, shareCountBtn,
        // R.string.share_sum);
        if (title != null) {
            setActionBar(title);
        }
//		AppLogger.e("url"+url);
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.replace(R.id.common_fragment, new BrowserHideControllerWebFragment(url,false))
//					.commit();
//		}
        AppLogger.e("url" + url);

//        mWebView.addJavascriptInterface(new JavaScriptObject(BrowserWebActivity.this), "doJsObj");

        mWebView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                }
                return false;
            }

        });
        mWebView.setWebViewClient(new InnerWebViewClient());
        mWebView.setWebChromeClient(new InnerWebChromeClient());
        WebSettings settings = mWebView.getSettings();
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);
        }
        settings.setJavaScriptEnabled(true);
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
        setWebViewCache(settings);

//        if (isHtml) {
//            mWebView.loadDataWithBaseURL(null, mUrl, "text/html", "utf-8", null);
//        } else {
        mWebView.loadUrl(url);
        //        }
        mIsWebViewAvailable = true;

        mWebView.setScrollViewCallbacks(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onResume() {
        mWebView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_browser;
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        overridePendingTransition(R.anim.zoom_in_center, R.anim.present_down);
    }


    public WebView getWebView() {
        return mIsWebViewAvailable ? mWebView : null;
    }

    public void startRefreshAnimation() {
        Animation rotation = AnimationUtils.loadAnimation(this,
                R.anim.refresh);
        iv_refresh.startAnimation(rotation);
        rotation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                iv_refresh.setEnabled(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                iv_refresh.setEnabled(true);
            }
        });
    }

    public void finishRefreshAnimation() {
        // TODO Auto-generated method stub
        iv_refresh.clearAnimation();
    }


    private class InnerWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            doWebViewBtngroups();
            startRefreshAnimation();
            // startRefreshAnimation();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            doWebViewBtngroups();
            finishRefreshAnimation();


        }
    }

    private class InnerWebChromeClient extends WebChromeClient {

        public void onProgressChanged(WebView view, int progress) {

            if (!mProgressBar.isShown())
                mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(progress);
            if (progress == 100)
                mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void doWebViewBtngroups() {
        iv_back.setEnabled(mWebView.canGoBack());
        iv_forward.setEnabled(mWebView.canGoForward());
    }

    @OnClick({R.id.iv_titlebar_other_browser, R.id.iv_titlebar_refresh, R.id.iv_titlebar_forward, R.id.iv_titlebar_back})
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_titlebar_back:
                mWebView.goBack();
                break;
            case R.id.iv_titlebar_forward:
                mWebView.goForward();
                break;
            case R.id.iv_titlebar_refresh:
                mWebView.reload();
                break;
            case R.id.iv_titlebar_other_browser:
                IntentUtil.jumpToBroswer(url, BrowserWebActivity.this);
                break;
            default:
                break;
        }
    }


    private void setWebViewCache(WebSettings webseting) {
        webseting.setDomStorageEnabled(true);
        // 应用可以有数据库
        webseting.setDatabaseEnabled(true);
        String dbPath = XApplication.getInstance().getApplicationContext()
                .getDir(AppConfigs.WEBVIEW_DB_NAME, Context.MODE_PRIVATE)
                .getPath();
        webseting.setDatabasePath(dbPath);
        webseting.setAppCacheMaxSize(1024 * 1024 * 100);// 设置缓冲大小
        String appCacheDir = XApplication.getInstance().getApplicationContext()
                .getDir("cache", Context.MODE_PRIVATE).getPath();
        webseting.setAppCachePath(appCacheDir);
        webseting.setAllowFileAccess(true);
        webseting.setAppCacheEnabled(true);
        webseting.setCacheMode(WebSettings.LOAD_DEFAULT);
        webseting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        Log.e("DEBUG", "onUpOrCancelMotionEvent: " + scrollState);
        if (scrollState == ScrollState.UP) {
            if (toolbarIsShown()) {
                hideToolbar();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (toolbarIsHidden()) {
                showToolbar();
            }
        }
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(actionbarView) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(actionbarView) == -actionBar.getHeight();
    }

    private void showToolbar() {
        moveToolbar(0);
        showBottomBar();
    }

    private void hideToolbar() {
        moveToolbar(-actionBar.getHeight());
        hideBottomBar();
    }

    private void showBottomBar() {
        float headerTranslationY = ViewHelper.getTranslationY(Bottombar);
        if (headerTranslationY != 0) {
            ViewPropertyAnimator.animate(Bottombar).cancel();
            ViewPropertyAnimator.animate(Bottombar).translationY(0).setDuration(200).start();
        }
    }

    private void hideBottomBar() {
        float headerTranslationY = ViewHelper.getTranslationY(Bottombar);
        if (headerTranslationY != -actionBar.getHeight()) {
            ViewPropertyAnimator.animate(Bottombar).cancel();
            ViewPropertyAnimator.animate(Bottombar).translationY(getResources().getDimensionPixelOffset(R.dimen.bottom_tab_height)).setDuration(200).start();
        }
    }


    private void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(actionbarView) == toTranslationY && mWebView != null) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(actionbarView), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(actionbarView, translationY);
                ViewHelper.setTranslationY(mWebView, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) (mWebView).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                (mWebView).requestLayout();
            }
        });
        animator.start();
    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }


}
