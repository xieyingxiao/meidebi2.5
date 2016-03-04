	package com.meidebi.app;

    import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Display;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.meidebi.app.service.bean.base.BaseItemBean;
import com.meidebi.app.service.bean.user.AccountBean;
import com.meidebi.app.support.component.upush.Umsg;
import com.meidebi.app.support.utils.GsonUtils;
import com.meidebi.app.support.utils.component.CrashHandler;
import com.meidebi.app.support.utils.content.CommonUtil;
import com.meidebi.app.support.utils.content.ContentUtils;
import com.meidebi.app.ui.msgdetail.MsgDetailActivity;
import com.meidebi.app.ui.msgdetail.OrderShowDetailActivity;
import com.meidebi.app.ui.picker.AlbumUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.orm.SugarDb;
import com.orm.query.Select;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

    public class XApplication extends Application {

	private static XApplication xApplication = null;

	private Activity activity = null;
	private DisplayMetrics displayMetrics = null;
	private Map<String, Bitmap> emotionsPic = new LinkedHashMap<String, Bitmap>();
	private NotificationManager mNotificationManager;
	public boolean startedApp = false;
	private AccountBean accountBean;
	private HashMap<String, Long> commentTimeMap = new HashMap<String, Long>();
	private List<BaseItemBean> catlist = new ArrayList<BaseItemBean>();
    private SparseArray<AlbumUtil.PhotoEntry> seletedPhoto;

    public AccountBean getAccountBean() {
		if (accountBean == null) {
			accountBean = Select.from(AccountBean.class).where("IS_LOGIN = 1")
					.first();
			if (accountBean == null) {
				accountBean = new AccountBean();
			}
		}
		return accountBean;
	}

        public void setSeletedPhoto(SparseArray<AlbumUtil.PhotoEntry> seletedPhoto) {
            this.seletedPhoto = seletedPhoto;
        }

        public SparseArray<AlbumUtil.PhotoEntry> getSeletedPhoto() {
            if(seletedPhoto==null){
                seletedPhoto= new SparseArray<AlbumUtil.PhotoEntry>();
            }
            return seletedPhoto;
        }

        public HashMap<String, Long> getCommentTimeMap() {
		return commentTimeMap;
	}

	public void setCommentTimeMap(HashMap<String, Long> commentTimeMap) {
		this.commentTimeMap = commentTimeMap;
	}

	public boolean isStartedApp() {
		return startedApp;
	}

	public void setStartedApp(boolean startedApp) {
		this.startedApp = startedApp;
	}

	public void setAccountBean(AccountBean accountBean) {
		this.accountBean = accountBean;
	}

	@Override
	public void onCreate() {
		super.onCreate();
         xApplication = this;
 		initComponent();
		onCreateSugarApp();
    }

	public void initComponent() {
 		 CrashHandler.getInstance().init(this);

  		initImageLoader(getApplicationContext());
//		TopAndroidClient.registerAndroidClient(getApplicationContext(),
//				SocialConstants.TAOBAO_KEY, SocialConstants.TAOBAO_SECRECT,
//				SocialConstants.TAOBAO_REDIRCET_URI);// 淘宝注册
        InitUmengPush();
//		JushUtity.init();
	}




	public static XApplication getInstance() {
		return xApplication;
	}

	public DisplayMetrics getDisplayMetrics() {
		if (displayMetrics != null) {
			return displayMetrics;
		} else {
			Activity a = getActivity();
			if (a != null) {
				Display display = getActivity().getWindowManager()
						.getDefaultDisplay();
				DisplayMetrics metrics = new DisplayMetrics();
				display.getMetrics(metrics);
				this.displayMetrics = metrics;
				return metrics;
			} else {
				// default screen is 800x480
				DisplayMetrics metrics = new DisplayMetrics();
				metrics.widthPixels = 480;
				metrics.heightPixels = 800;
				return metrics;
			}
		}
	}

	public NotificationManager getNotificationManager() {
		if (mNotificationManager == null)
			mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		return mNotificationManager;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public synchronized Map<String, Bitmap> getEmotionsPics() {
		if (emotionsPic != null && emotionsPic.size() > 0) {
			return emotionsPic;
		} else {
			getEmotionsTask();
			getEmotionsTask2();
            getEmotionsTask3();
            return emotionsPic;
		}
	}

	private void getEmotionsTask() {
		ContentUtils.getInstance().Emotion1();
		Map<String, String> emotions = ContentUtils.getInstance().getEmotion1();
		List<String> index = new ArrayList<String>();
		index.addAll(emotions.keySet());
		for (String str : index) {
			String name = emotions.get(str);
			AssetManager assetManager = XApplication.getInstance().getAssets();
			InputStream inputStream;
			try {
				inputStream = assetManager.open(name);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				if (bitmap != null) {
					Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
							CommonUtil.dip2px(25), CommonUtil.dip2px(25), true);
					if (bitmap != scaledBitmap) {
						bitmap.recycle();
						bitmap = scaledBitmap;
					}
					emotionsPic.put(str, bitmap);
				}
			} catch (IOException ignored) {

			}
		}
	}

	private void getEmotionsTask2() {
		ContentUtils.getInstance().Emotion2();
		Map<String, String> emotions = ContentUtils.getInstance().getEmotion2();
		List<String> index = new ArrayList<String>();
		index.addAll(emotions.keySet());
		for (String str : index) {
			String name = emotions.get(str);
			AssetManager assetManager = XApplication.getInstance().getAssets();
			InputStream inputStream;
			try {
				inputStream = assetManager.open(name);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				if (bitmap != null) {
					Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
							CommonUtil.dip2px(XApplication.getInstance(), 80),
							CommonUtil.dip2px(XApplication.getInstance(), 80),
							true);
					if (bitmap != scaledBitmap) {
						bitmap.recycle();
						bitmap = scaledBitmap;
					}
					emotionsPic.put(str, bitmap);
				}
			} catch (IOException ignored) {

			}
		}
	}

        private void getEmotionsTask3() {
            ContentUtils.getInstance().Emotion3();
            Map<String, String> emotions = ContentUtils.getInstance().getEmotion3();
            List<String> index = new ArrayList<String>();
            index.addAll(emotions.keySet());
            for (String str : index) {
                String name = emotions.get(str);
                AssetManager assetManager = XApplication.getInstance().getAssets();
                InputStream inputStream;
                try {
                    inputStream = assetManager.open(name);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap != null) {
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                                CommonUtil.dip2px(XApplication.getInstance(), 80),
                                CommonUtil.dip2px(XApplication.getInstance(), 80),
                                true);
                        if (bitmap != scaledBitmap) {
                            bitmap.recycle();
                            bitmap = scaledBitmap;
                        }
                        emotionsPic.put(str, bitmap);
                    }
                } catch (IOException ignored) {

                }
            }
        }


	public static void initImageLoader(Context context) {

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
	}





	private SugarDb sugarDb;

	public void onCreateSugarApp() {

		this.sugarDb = new SugarDb(this);
	}

	public void onTerminate() {
		if (this.sugarDb != null) {
			this.sugarDb.getDB().close();
		}
		super.onTerminate();
	}

	public SugarDb getSugarDb() {
		return sugarDb;
	}


    public void InitUmengPush(){
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);


        /**
         * 该Handler是在IntentService中被调用，故
         * 1. 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * 2. IntentService里的onHandleIntent方法是并不处于主线程中，因此，如果需调用到主线程，需如下所示;
         * 	      或者可以直接启动Service
         * */
        UmengMessageHandler messageHandler = new UmengMessageHandler(){
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler(getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
//                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public Notification getNotification(Context context,
                                                UMessage msg) {
                switch (msg.builder_id) {
                    case 1:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView);
                        Notification mNotification = builder.build();
                        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                        mNotification.contentView = myNotificationView;
                        return mNotification;
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Umsg umsg = GsonUtils.parse(msg.custom, Umsg.class);
                Intent intent = new Intent();

                switch (umsg.getActivity_type()){
                    case 1:
                        intent.setClass(XApplication.this, MsgDetailActivity.class);
                        break;
                    case 2:
                        intent.setClass(XApplication.this, OrderShowDetailActivity.class);
                        break;
                    default:
                        Toast.makeText(context, "需更新到最新版本", Toast.LENGTH_LONG).show();
                        break;
                }
                intent.putExtra("id",umsg.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
             }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }
}
