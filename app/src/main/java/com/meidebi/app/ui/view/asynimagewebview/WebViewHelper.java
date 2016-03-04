package com.meidebi.app.ui.view.asynimagewebview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.support.lib.MyAsyncTask;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.browser.BrowserImgActivity;
import com.meidebi.app.ui.browser.BrowserWebActivity;
import com.meidebi.app.ui.picker.PickerActivity;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.http.message.BasicNameValuePair;

/**
 * 
 * @author pengtao.du@downjoy.com
 * 
 */
public class WebViewHelper {

	private WebView mWebView;
	private Activity mContext;
	private String TAG = "WebViewHelper";
	// private DownloadWebImgTask downloadTask;
	private String title;
	public Md5FileNameGenerator md5FileNameGenerator = new Md5FileNameGenerator();
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private int backgroundcolour = 0;
    private int paddingTop = 6;
    private InterFaceLoadWebview interFaceLoadWebview;
    private Boolean isLoadCompelte = false;
    private boolean isOnPause = false;




    public void setInterFaceLoadWebview(InterFaceLoadWebview interFaceLoadWebview) {
        this.interFaceLoadWebview = interFaceLoadWebview;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getBackgroundcolour() {
		return backgroundcolour;
	}

	public void setBackgroundcolour(int backgroundcolour) {
		this.backgroundcolour = backgroundcolour;
	}

	public WebViewHelper(Activity context) {
		mContext = context;
        hardwareAccelerate();
	}

    //硬件加速
    private void hardwareAccelerate(){
        if (this.getPhoneSDKInt() >= 14) {
            mContext.getWindow().setFlags(0x1000000, 0x1000000);
        }
    }

    public void setTitle(String title) {
		this.title = title;
	}
    Parser parser;

	public void execute(WebView webView, final Parser parser) {
		mWebView = webView;
        setmWebViewClient();
        setupWebView();
        this.parser = parser;
        new doHtmlTask().execute();
  	}


    class doHtmlTask extends MyAsyncTask<Void,Void,String> {



       @Override
       protected String doInBackground(Void... params) {
           return parser.loadData();
       }

       @Override
       protected void onPostExecute(final String s) {
           super.onPostExecute(s);
           if(mWebView!=null) {
               mWebView.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       if (mWebView != null) {
                           Parser.openWebView(s, mWebView);
                       }
                   }
               }, 100);
           }
       }
   }

    private void setmWebViewClient(){
        AppLogger.e("load");
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if(interFaceLoadWebview!=null){
                    interFaceLoadWebview.loadWebViewFailed();
                }
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(interFaceLoadWebview!=null){
                    interFaceLoadWebview.loadWebViewFinish();
                }
                isLoadCompelte = true;
                if (SharePrefUtility.getEnablePic()) {

                    if (mWebView != null) {
                        mWebView.loadUrl("javascript:setContentPadding(16,"+paddingTop+",16,50)");

                        mWebView.loadUrl("javascript:onLoaded()");
                        if (backgroundcolour != 0) {
                            mWebView.setBackgroundResource(backgroundcolour);
                        }
                    }
                }

                if(!mWebView.getSettings().getLoadsImagesAutomatically()) {
                    mWebView.getSettings().setLoadsImagesAutomatically(true);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                IntentUtil.jumpToBroswerActivity((Activity) mContext,
                        BrowserWebActivity.class, new BasicNameValuePair("url",
                                url), new BasicNameValuePair("title", title));
                return true;
            }
        });
    }




	@SuppressLint("JavascriptInterface")
	private void setupWebView() {
        if (Build.VERSION.SDK_INT < 19) {
         mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        }
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.setHorizontalScrollBarEnabled(false);
         mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.addJavascriptInterface(new Js2JavaInterface(),
				Parser.Js2JavaInterfaceName);
		mWebView.getSettings()
				.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setBlockNetworkImage(false);

	}

	@TargetApi(VERSION_CODES.HONEYCOMB)
	protected void disableHardwareAcc() {
		mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
		webseting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
	}

	public class Js2JavaInterface {
		@JavascriptInterface
		public void openImage(String imgSrc,int position) {

            Intent intent = new Intent((Activity) mContext,
                    BrowserImgActivity.class);
            intent.putStringArrayListExtra(PickerActivity.ALBUM_KEY,parser.getImgUrls());
            intent.putExtra(BrowserImgActivity.IMAGE_POSITION,position);
            mContext.startActivity(intent);
		}

		@JavascriptInterface
		public void loadImage(final String url) {
			imageLoader.loadImage(url, AppConfigs.WEBIMGOPTIONS,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingFailed(final String imgUrl,
								View arg1, FailReason arg2) {
							// TODO Auto-generated method stub
                            if(mWebView!=null) {
                                mWebView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mWebView != null) {
                                            mWebView.loadUrl("javascript:onImageLoadingFailed('"
                                                    + imgUrl + "')");
                                        }
                                    }
                                }, 100);
                            }
						}

						@Override
						public void onLoadingComplete(final String imgUrl,
								View arg1, Bitmap arg2) {
							final String filePath = "file:///"
									+ ImageLoader.getInstance().getDiscCache()
											.get(imgUrl);
							if (mWebView != null) {
								mWebView.postDelayed(new Runnable() {
									@Override
									public void run() {
										if (mWebView != null) {
											mWebView.loadUrl("javascript:onImageLoadingComplete('"
													+ imgUrl
													+ "','"
													+ filePath
													+ "')");
										}
									}
								}, 100);
							}
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}
					});
		}

		@JavascriptInterface
		public void loadComplte() {
			AppLogger.e("loadComplte");
		}
	}

	public void destroy() {
		// Utility.cancelTasks(downloadTask);
		imageLoader.stop();
		if (mWebView != null) {
			mWebView.removeAllViews();
			mWebView.clearHistory();
			mWebView.clearCache(true);
			mWebView.loadUrl("about:blank");
			mWebView.freeMemory(); // new code
			// mWebView.pauseTimers(); // new code
			mWebView.destroy();
		}
	}

	public Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ImgBean bean = (ImgBean) msg.obj;
			mWebView.loadUrl("javascript:onImageLoadingComplete('"
					+ bean.imgUrl + "','" + bean.getFilepath() + "')");
		};
	};

	class ImgBean {
		private String filepath;

		public String getFilepath() {
			return filepath;
		}

		public void setFilepath(String filepath) {
			this.filepath = filepath;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		private String imgUrl;

	}

    public int getPhoneSDKInt() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return version;
    }


    /**
     * 当Activity执行onPause()时让WebView执行pause
     */
     public void onPause() {
         try {
            if (mWebView != null) {
                mWebView.getClass().getMethod("onPause").invoke(mWebView, (Object[]) null);
                isOnPause = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当Activity执行onResume()时让WebView执行resume
     */
     public void onResume() {
         AppLogger.e("" +
                 "" +
                 ""+isOnPause);
         try {
            if (isOnPause) {
                if (mWebView != null) {
                    mWebView.getClass().getMethod("onResume").invoke(mWebView, (Object[]) null);
                }
                isOnPause = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 该处的处理尤为重要:
     * 应该在内置缩放控件消失以后,再执行mWebView.destroy()
     * 否则报错WindowLeaked
     */
     public void onDestroy() {
         if (mWebView != null) {
             mWebView.setVisibility(View.GONE);
            long delayTime = ViewConfiguration.getZoomControlsTimeout();
             new Handler().postDelayed(new Runnable(){
                 public void run() {
                     mWebView.destroy();
                     mWebView = null;
                 }
             },delayTime);
        }
        isOnPause = false;
    }
}
