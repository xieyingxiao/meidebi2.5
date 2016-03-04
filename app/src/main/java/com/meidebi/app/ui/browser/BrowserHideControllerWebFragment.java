package com.meidebi.app.ui.browser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.support.component.js.JavaScriptObject;
import com.meidebi.app.support.utils.Utility;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

@SuppressLint({ "ValidFragment", "NewApi" })

public class BrowserHideControllerWebFragment extends Fragment implements
		OnClickListener , ObservableScrollViewCallbacks {
  	private boolean mIsWebViewAvailable;
	private String mUrl = null;
 	private boolean isHtml;
    private View mToolbar;
   ProgressBar mProgressBar;
    @InjectView(R.id.iv_titlebar_back)
    ImageView iv_back;
    @InjectView(R.id.iv_titlebar_forward)
    ImageView iv_forward;
    @InjectView(R.id.iv_titlebar_refresh)
    ImageView iv_refresh;
    private int toolbarHeight;
    @InjectView(R.id.webView)
    ObservableWebView mWebView;
    @InjectView(R.id.bottom_tab)
    View Bottombar;

	public BrowserHideControllerWebFragment(String url, boolean isHtml) {
		super();
		mUrl = url;
		this.isHtml = isHtml;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setHasOptionsMenu(true);
//		setRetainInstance(true);
		/**
		 * some devices for example Nexus 7 4.2.2 version will receive website
		 * favicon, but some devices may cant, Galaxy Nexus 4.2.2 version
		 */
		// String path = FileManager.getWebViewFaviconDirPath();
		// if (!TextUtils.isEmpty(path))
		// WebIconDatabase.getInstance().open(FileManager.getWebViewFaviconDirPath());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_browserweb_layout,
				container, false);
		if (mWebView != null) {
			mWebView.destroy();
		}
        ButterKnife.inject(this,view);

 		mWebView.addJavascriptInterface(new JavaScriptObject(getActivity()), "doJsObj");

		mProgressBar = ((BrowserWebActivity)getActivity()).mProgressBar;
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
		if (isHtml) {
			mWebView.loadDataWithBaseURL(null, mUrl, "text/html", "utf-8", null);
		} else {
			mWebView.loadUrl(mUrl);
		}
		mIsWebViewAvailable = true;
		WebSettings settings = mWebView.getSettings();
        if(android.os.Build.VERSION.SDK_INT>=11){
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);
        }
		settings.setJavaScriptEnabled(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
        setWebViewCache(settings);
        mWebView.setScrollViewCallbacks(this);
        mToolbar = ((BrowserWebActivity)getActivity()).actionbarView;
        toolbarHeight = getActionBarSize();
		return view;
	}



	public void loadUrl(String url) {
		if (mIsWebViewAvailable)
			getWebView().loadUrl(mUrl = url);
		else
			Log.w("ImprovedWebViewFragment",
					"WebView cannot be found. Check the view and fragment have been loaded.");
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
	public void onDestroyView() {
		mIsWebViewAvailable = false;
        ButterKnife.reset(this);
        super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		if (mWebView != null) {
 			mWebView.destroy();
			mWebView = null;
		}
		super.onDestroy();
	}

	public WebView getWebView() {
		return mIsWebViewAvailable ? mWebView : null;
	}

	public void startRefreshAnimation() {
		Animation rotation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.refresh);
		iv_refresh.startAnimation(rotation);
		rotation.setAnimationListener(new AnimationListener() {

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
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

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
			if (getActivity() == null)
				return;

		}
	}

	private class InnerWebChromeClient extends WebChromeClient {
		@Override
		public void onReceivedTitle(WebView view, String sTitle) {
			super.onReceivedTitle(view, sTitle);
			if (sTitle != null && sTitle.length() > 0) {
				if (getActivity() == null)
					return;
			}
		}

		// website icon is too small
		@Override
		public void onReceivedIcon(WebView view, Bitmap icon) {
			super.onReceivedIcon(view, icon);
			if (getActivity() == null)
				return;
			// getActivity().getActionBar().setIcon(new
			// BitmapDrawable(getActivity().getResources(), icon));
		}

		public void onProgressChanged(WebView view, int progress) {
			if (getActivity() == null)
				return;
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

	@OnClick({R.id.iv_titlebar_other_browser,R.id.iv_titlebar_refresh,R.id.iv_titlebar_forward,R.id.iv_titlebar_back})
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
			IntentUtil.jumpToBroswer(mUrl, getActivity());
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
        return ViewHelper.getTranslationY(mToolbar) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mToolbar) == -toolbarHeight;
    }

    private void showToolbar() {
        moveToolbar(0);
//        showBottomBar();
    }

    private void hideToolbar() {
        moveToolbar(-toolbarHeight);
//        hideBottomBar();
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
         if (headerTranslationY != -toolbarHeight) {
            ViewPropertyAnimator.animate(Bottombar).cancel();
            ViewPropertyAnimator.animate(Bottombar).translationY(getResources().getDimensionPixelOffset(R.dimen.bottom_tab_height)).setDuration(200).start();
        }
     }



    private void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(mToolbar) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mToolbar), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(mToolbar, translationY);
                ViewHelper.setTranslationY( mWebView, translationY);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) (mWebView).getLayoutParams();
                lp.height = (int) -translationY + Utility.getScreenHeight(getActivity()) - lp.topMargin;
                (  mWebView).requestLayout();
            }
        });
        animator.start();
    }

    protected int getActionBarSize() {
        Activity activity = getActivity();
        if (activity == null) {
            return 0;
        }
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = activity.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }
}