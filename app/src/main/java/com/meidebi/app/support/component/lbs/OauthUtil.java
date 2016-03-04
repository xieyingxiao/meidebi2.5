package com.meidebi.app.support.component.lbs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.meidebi.app.XApplication;
import com.meidebi.app.service.bean.user.AwardJson;
import com.meidebi.app.service.dao.user.AwardDao;
import com.meidebi.app.service.dao.user.SocailPlatform;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.auth.AccessToken;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsAccount;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.SocializeUser;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OauthUtil {
    private UMSocialService mController = null;
    private Context mContext = null;
    private final String TAG = "TestData";
    private final SHARE_MEDIA SinaMedia = SHARE_MEDIA.SINA;
    private final SHARE_MEDIA QQMedia = SHARE_MEDIA.QZONE;
    private final SHARE_MEDIA QQFRDMedia = SHARE_MEDIA.QQ;

    private TopAndroidClient client;
    private IAuthListener authlistener;// 淘宝回调listener
    private AccessToken taobao_token;
    // private UMImage mUMImgBitmap;
    private AwardDao aw_dao;

    public AwardDao getDao() {
        if (aw_dao == null) {
            aw_dao = new AwardDao((Activity) mContext);
        }
        return aw_dao;
    }


    public OauthUtil(Context activity) {
        initConfig(activity);

    }

    /**
     * @功能描述 : 初始化与SDK相关的成员变量
     */
    public void initConfig(Context activity) {
        mContext = activity;
        mController = UMServiceFactory.getUMSocialService(
                SocialConstants.DESCRIPTOR, RequestType.SOCIAL);

        // 添加新浪和QQ空间的SSO授权支持
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        mController.getConfig().setDefaultShareLocation(false);
        mController.getConfig().setSsoHandler(
                new QZoneSsoHandler((Activity) mContext, SocialConstants.QQID,
                        SocialConstants.QQKEY));
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) mContext,
                SocialConstants.QQID, SocialConstants.QQKEY);
        qqSsoHandler.addToSocialSDK();
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
                (Activity) mContext, SocialConstants.QQID,
                SocialConstants.QQKEY);
        qZoneSsoHandler.addToSocialSDK();
        // 添加腾讯微博SSO支持
        // mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
        // mController.getConfig().supportQQPlatform((Activity) mContext,
        // SocialConstants.QQID, SocialConstants.QQKEY,
        // SocialConstants.SOCIAL_LINK);
        // com.umeng.socom.Log.LOG = false;
        // mController.getConfig().supportWXPlatform((Activity) mContext,
        // "wx967daebe835fbeac", "http://www.umeng.com/social") ;
        // mController.getConfig().supportWXCirclePlatform((Activity) mContext,
        // "wx967daebe835fbeac", "http://www.umeng.com/social") ;

        // 支持微信朋友圈

        /************************************* 设置授权完成后马上添加一个好友 **************************/
        // final SocializeConfig config = mController.getConfig();

        // config.addFollow(mTestMedia, "1914100420");
        // config.setOauthDialogFollowListener(new MulStatusListener() {
        // //授权关注账号
        // @Override
        // public void onStart() {
        // Log.d(TAG, "Follow Start");
        // }
        //
        // @Override
        // public void onComplete(MultiStatus multiStatus, int st,
        // SocializeEntity entity) {
        // if (st == 200) {
        // Map<String, Integer> allChildren = multiStatus
        // .getAllChildren();
        // Set<String> set = allChildren.keySet();
        // for (String fid : set)
        // Log.d(TAG, fid + "    " + allChildren.get(fid));
        // }
        // }
        // });
    }

    /**
     * @功能描述 : 新浪授权（功能底层接口） Bundle[{uid=3768766257,
     * access_secret=2.00XQ3DHE37PKQD351ceb5a32wnykhD,
     * access_key=2.00XQ3DHE37PKQD351ceb5a32wnykhD}]
     */
    public void doSinaOauth() {
        mController.doOauthVerify(mContext, SinaMedia, new UMAuthListener() {
            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                getAuthlistener().onError(null);
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                    String uid = value.getString("uid");
                    String access_token = (String) value
                            .get("access_key");
                    getAuthlistener().onIdComplete(uid, null, null, access_token,
                            SocailPlatform.Weibo);
                } else {
                    getAuthlistener().onError(null);

                }
            }

            @Override
            public void onCancel(SHARE_MEDIA arg0) {
                getAuthlistener().onError(null);
            }

            @Override
            public void onStart(SHARE_MEDIA arg0) {
            }

        });
    }

    public void getUserInfo() {
        mController.getUserInfo(mContext, new SocializeListeners.FetchUserListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(int i, SocializeUser user) {
                if (user != null && user.mAccounts != null) {
                    List<SnsAccount> lists = user.mAccounts;
                    for (SnsAccount snsAccount : lists) {
                        if (snsAccount.getPlatform().equals("sina")) {
                            String userName = snsAccount.getUserName();
                            String headerImage = snsAccount.getAccountIconUrl();
                            getAuthlistener().onLoadUserComplete(userName,headerImage);
                        }
                    }
                }

            }
        });
    }

    /**
     * @功能描述 : qq授权（功能底层接口） Bundle[{uid=B1B9D08DCFC237D20A29AC78CFA3B441,
     * expires_in=7776000, openid=B1B9D08DCFC237D20A29AC78CFA3B441,
     * access_token=E0C2F89993B10F6BCB5DFD1EC945E2C8}]
     */
    public void doQQOauth() {
        // if (!UMInfoAgent.isOauthed(mContext, QQMedia)) {
        // Toast.makeText(mContext, "QQ已经授权.", 1).show();
        // getAuthlistener().onIdComplete(SharePrefHelper.getSharedPreferences(mContext,
        // SharePrefHelper.QQ_ID,""),SocailMethod.Weibo);
        //
        // } else {
        mController.doOauthVerify(mContext, QQMedia, new UMAuthListener() {
            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                getAuthlistener().onError(null);
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                    mController.getPlatformInfo(mContext, SHARE_MEDIA.QZONE,
                            new UMDataListener() {
                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onComplete(int status,
                                                       Map<String, Object> info) {

                                    if (status == 200
                                            && !TextUtils.isEmpty((String) info
                                            .get("openid"))) {
                                        StringBuilder sb = new StringBuilder();
                                        Set<String> keys = info.keySet();
                                        for (String kStr : keys) {
                                            sb.append(kStr + "="
                                                    + info.get(kStr).toString()
                                                    + "\r\n");

                                        }
                                        String openid = (String) info
                                                .get("openid");
                                        String screen_name = (String) info
                                                .get("screen_name");
                                        String profile_image_url = (String) info
                                                .get("profile_image_url");
                                        String access_token = (String) info
                                                .get("access_token");
                                        getAuthlistener().onIdComplete(openid, screen_name, profile_image_url, access_token,
                                                SocailPlatform.QQ);
                                    } else {
                                        getAuthlistener().onAuthException(null);
                                    }
                                }
                            });
                } else {
                    getAuthlistener().onError(null);
                    // getDialogLoading().loadDialog(R.string.hint_oatufailed);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA arg0) {
                getAuthlistener().onCancel();
            }

            @Override
            public void onStart(SHARE_MEDIA arg0) {
            }

        });
    }

    // }

    // 淘宝oauth
    public void doTBOauth() {
        if (!TbIsOauthed()) {
            client.authorize((Activity) mContext);
        }
    }

    public void initTbclient() {// 初始化
//		if (client == null) {
//			client = TopAndroidClient
//					.getAndroidClientByAppKey(SocialConstants.TAOBAO_KEY);
//			Intent intent = ((Activity) mContext).getIntent();
//			Uri uri = intent.getData();
//			Uri u = Uri.parse(client.getRedirectURI());
//			if (uri != null && uri.getScheme().equals(u.getScheme())
//					&& uri.getHost().equals(u.getHost())
//					&& uri.getPort() == u.getPort()
//					&& uri.getPath().equals(u.getPath())) {
//
//				String errorStr = uri.getQueryParameter("error");
//				IAuthListener listener = getAuthlistener();
//				if (errorStr == null) {// 授权成功
//					// String ret = url.substring(url.indexOf("#") + 1);
//					String ret = uri.getFragment();
//					String[] kv = ret.split("&");
//					Bundle values = new Bundle();
//					for (String each : kv) {
//						String[] ss = each.split("=");
//						if (ss != null && ss.length == 2) {
//							values.putString(ss[0], ss[1]);
//						}
//					}
//					final AccessToken token = TOPUtils
//							.convertToAccessToken(values);
//					// Android3.0后ui主线程中同步访问网络会有限制。
//					// 使用ExecutorService.invokeAll()阻塞主线程的方式起一个线程再去调用api
//					Callable<Date> task = new Callable<Date>() {
//						@Override
//						public Date call() throws Exception {
//							Date date = client.getTime();
//							return date;
//						}
//					};
//					List<Callable<Date>> tasks = new ArrayList<Callable<Date>>();
//					tasks.add(task);
//					ExecutorService es = Executors.newSingleThreadExecutor();
//					try {
//						List<Future<Date>> results = es.invokeAll(tasks);
//						Future<Date> future = results.get(0);
//						token.setStartDate(future.get());
//						client.addAccessToken(token);
//					} catch (Exception e) {
//						listener.onAuthException(new AuthException(e));
//					}
//
//					String tb_id = token.getAdditionalInformation().get(
//							AccessToken.KEY_SUB_TAOBAO_USER_ID);
//					taobao_token = token;
//					getAuthlistener()
//							.onIdComplete(tb_id, SocailPlatform.Taobao);
//				} else {// 授权失败
//					String errorDes = uri
//							.getQueryParameter("error_description");
//					AuthError error = new AuthError();
//					error.setError(errorStr);
//					error.setErrorDescription(errorDes);
//					listener.onError(error);
//					getAuthlistener().onError(error);
//				}
//			} else {
//				getAuthlistener().onCancel();
//			}
//		}
    }

    public Boolean TbIsOauthed() {// 判断淘宝是否授权
        if (taobao_token != null) {
            return true;
        }
        return false;
    }

    public IAuthListener getAuthlistener() {
        return authlistener;
    }

    public void setAuthlistener(IAuthListener authlistener) {
        this.authlistener = authlistener;
    }

    public void sendInfo(SocailPlatform method) {
        switch (method) {
            case QQ:
                SendQQInfo();
                break;
            case Weibo:
                SendSinaInfo();
                break;
            case Taobao:
                sendTbInfo(taobao_token);
                break;
            default:
                break;
        }
    }

    /**
     * uid=B1B9D08DCFC237D20A29AC78CFA3B441 screen_name=Kidsic
     * openid=B1B9D08DCFC237D20A29AC78CFA3B441
     * profile_image_url=http://qzapp.qlogo
     * .cn/qzapp/100311602/B1B9D08DCFC237D20A29AC78CFA3B441/100
     * access_token=E0C2F89993B10F6BCB5DFD1EC945E2C8 verified=0
     */
    public void SendQQInfo() {
        // if (!UM.isOauthed(mContext, QQMedia)) {
        // doQQOauth();
        // } else {
        mController.getPlatformInfo(mContext, SHARE_MEDIA.QZONE,
                new UMDataListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {

                        if (status == 200 && info != null) {
                            StringBuilder sb = new StringBuilder();
                            Set<String> keys = info.keySet();
                            for (String kStr : keys) {
                                sb.append(kStr + "="
                                        + info.get(kStr).toString() + "\r\n");

                            }
                            String name = (String) info.get("screen_name");
                            String openid = (String) info.get("openid");
                            String access_token = (String) info
                                    .get("access_token");
                            getAuthlistener().onInfoComplete(access_token,
                                    openid, name, SocailPlatform.QQ);
                        } else {
                        }
                    }
                });
        // }
    }

    /**
     * uid=3768766257 favourites_count=0 location=其他 description= verified=false
     * friends_count=19 gender=1 screen_name=用户3768766257 statuses_count=0
     * followers_count=8
     * profile_image_url=http://tp2.sinaimg.cn/3768766257/180/5673150802/1
     * access_token=2.00XQ3DHE37PKQD351ceb5a32wnykhD
     */
    public void SendSinaInfo() {
        // if (!UMInfoAgent.isOauthed(mContext, SinaMedia)) {
        // doSinaOauth();
        // } else {
        mController.getPlatformInfo(mContext, SHARE_MEDIA.SINA,
                new UMDataListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        if (status == 200 && info != null) {
                            StringBuilder sb = new StringBuilder();
                            Set<String> keys = info.keySet();
                            for (String kStr : keys) {
                                sb.append(kStr + "="
                                        + info.get(kStr).toString() + "\r\n");
                            }
                            String name = (String) info.get("screen_name");
                            String uid = String.valueOf(info.get("uid"));
                            String access_token = (String) info
                                    .get("access_token");
                            getAuthlistener().onInfoComplete(access_token, uid,
                                    name, SocailPlatform.Weibo);
                        } else {
                            Toast.makeText(mContext, "mcode:" + status, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
        // }
    }

    /**
     * 淘宝信息获取
     *
     * @param accessToken
     */
    private void sendTbInfo(AccessToken accessToken) {
        if (!TbIsOauthed()) {
            doTBOauth();
        } else {
            Log.d(TAG, "callback");
            String tb_id = accessToken.getAdditionalInformation().get(
                    AccessToken.KEY_SUB_TAOBAO_USER_ID);
            if (tb_id == null) {
                tb_id = accessToken.getAdditionalInformation().get(
                        AccessToken.KEY_TAOBAO_USER_ID);
            }
            // Long so_userId=Long.parseLong(id);
            // nick=accessToken.getAdditionalInformation().get(AccessToken.KEY_SUB_TAOBAO_USER_NICK);
            // if(nick==null){
            // nick=accessToken.getAdditionalInformation().get(AccessToken.KEY_TAOBAO_USER_NICK);
            // }
            String r2_expires = accessToken.getAdditionalInformation().get(
                    AccessToken.KEY_R2_EXPIRES_IN);
            String nickname = accessToken.getAdditionalInformation().get(
                    AccessToken.KEY_SUB_TAOBAO_USER_NICK);
            Date start = accessToken.getStartDate();
            Date end = new Date(start.getTime() + Long.parseLong(r2_expires)
                    * 1000L);
            String token = accessToken.getAdditionalInformation().get(
                    AccessToken.KEY_ACCESS_TOKEN);

            getAuthlistener().onInfoComplete(token, tb_id, nickname,
                    SocailPlatform.Taobao);
        }
    }

    // share start

    /**
     * @功能描述 : 图文分享（功能底层接口）
     */
    public void ShareToSina(String content, String picUrl) {
        mController.setShareContent(content);
        UMImage mUMImgBitmap = new UMImage(mContext, picUrl);
        mController.setShareMedia(mUMImgBitmap);
        mController.postShare(mContext, SinaMedia, new SnsPostListener() {

            @Override
            public void onComplete(SHARE_MEDIA arg0, int arg1,
                                   SocializeEntity arg2) {
                if (arg1 == 200) {
                    getAward();
                }
            }

            @Override
            public void onStart() {
            }
        });
    }

    /**
     * @功能描述 : 图文分享（功能底层接口）
     */
    public void ShareToWeixin(final String title, final String content, final String picUrl,
                              final String orlUrl) {

        mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN_CIRCLE);
        UMWXHandler circleHandler = new UMWXHandler((Activity) mContext,
                SocialConstants.WEIXINID);
        circleHandler.setToCircle(true);
        circleHandler.addToSocialSDK();
        ImageLoader.getInstance().loadImage(picUrl, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                UMImage mUMImgBitmap = new UMImage(mContext, arg2);
                CircleShareContent circleMedia = new CircleShareContent();
                circleMedia.setShareContent(content);
                circleMedia.setTitle(title);
                circleMedia.setShareImage(mUMImgBitmap);
                circleMedia.setTargetUrl(orlUrl);
                mController.setShareContent(content);
                mController.setShareMedia(circleMedia);
                mController.postShare(mContext, SHARE_MEDIA.WEIXIN_CIRCLE,
                        new SnsPostListener() {
                            @Override
                            public void onComplete(SHARE_MEDIA arg0, int arg1,
                                                   SocializeEntity arg2) {
                                if (arg1 == 200) {
                                    getAward();
                                }
                            }

                            @Override
                            public void onStart() {
                                // getDialogLoading().loadDialog(
                                // R.string.hint_shareing);
                            }
                        });
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                // TODO Auto-generated method stub

            }
        });

    }

    /**
     * @功能描述 : 图文分享（功能底层接口）
     */
    public void ShareToWeixinFrd(final String title, final String content,
                                 final String picUrl, final String orlUrl) {
        mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN);
        UMWXHandler wxHandler = new UMWXHandler((Activity) mContext,
                SocialConstants.WEIXINID);
        wxHandler.setToCircle(false);
        wxHandler.addToSocialSDK();
        // 设置分享标题
        ImageLoader.getInstance().loadImage(picUrl, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                // TODO Auto-generated method stub
                WeiXinShareContent weixinContent = new WeiXinShareContent();
                UMImage mUMImgBitmap = new UMImage(mContext, arg2);
                weixinContent.setTitle(title);
                weixinContent.setTargetUrl(orlUrl);
                weixinContent.setShareContent(content);
                weixinContent.setShareImage(mUMImgBitmap);
                // mController.setShareContent(content);
                mController.setShareMedia(weixinContent);
                mController.postShare(mContext, SHARE_MEDIA.WEIXIN,
                        new SnsPostListener() {
                            @Override
                            public void onComplete(SHARE_MEDIA arg0, int arg1,
                                                   SocializeEntity arg2) {
                                // if (arg1 == 200) {
                                // getAward();
                                // }
                            }

                            @Override
                            public void onStart() {
                                // getDialogLoading().loadDialog(
                                // R.string.hint_shareing);
                            }
                        });
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                // TODO Auto-generated method stub

            }
        });

    }

    /**
     * @功能描述 : 图文分享（功能底层接口）
     */
    public void ShareToTecentWB(String content, String picUrl) {
        mController.setShareContent(content);
        UMImage mUMImgBitmap = new UMImage(mContext, picUrl);
        mController.setShareMedia(mUMImgBitmap);
        mController.postShare(mContext, SHARE_MEDIA.TENCENT,
                new SnsPostListener() {

                    @Override
                    public void onComplete(SHARE_MEDIA arg0, int arg1,
                                           SocializeEntity arg2) {

                        if (arg1 == 200) {
                            // getAward();
                            Toast.makeText(mContext, "分享完成", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onStart() {
                    }
                });
    }

    /**
     * @功能描述 : 图文分享（功能底层接口）
     */
    public void ShareToQzone(String title, String content, String picUrl,
                             String url) {
        UMImage mUMImgBitmap = new UMImage(mContext, picUrl);
        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(content);
        qzone.setTargetUrl(url);
        qzone.setTitle(title);
        qzone.setShareImage(mUMImgBitmap);
        mController.setShareMedia(qzone);
        mController.postShare(mContext, QQMedia, new SnsPostListener() {
            @Override
            public void onComplete(SHARE_MEDIA arg0, int arg1,
                                   SocializeEntity arg2) {
                if (arg1 == 200) {
                    Toast.makeText(mContext, "分享完成", Toast.LENGTH_SHORT).show();
                    // getAward();
                }
            }

            @Override
            public void onStart() {
            }
        });
    }

    /**
     * @功能描述 : 图文分享（功能底层接口）
     */
    public void ShareToQQ(String title, String content, String picUrl,
                          String url) {
        UMImage mUMImgBitmap = new UMImage(mContext, picUrl);
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(content);
        qqShareContent.setTitle(title);
        qqShareContent.setTargetUrl(url);
        qqShareContent.setShareImage(mUMImgBitmap);
        mController.setShareMedia(qqShareContent);
        mController.setShareContent(title);
        // mController.setShareMedia(mUMImgBitmap);
        mController.postShare(mContext, QQFRDMedia, new SnsPostListener() {
            @Override
            public void onComplete(SHARE_MEDIA arg0, int arg1,
                                   SocializeEntity arg2) {
                if (arg1 == 200) {
                    Toast.makeText(mContext, "分享完成", Toast.LENGTH_SHORT).show();
                    // getAward();
                }
            }

            @Override
            public void onStart() {
            }
        });
    }

    // share end
    private void getAward() {
        new Thread() {
            @Override
            public void run() {
                AwardJson bean = getDao().getAwardJson();
                Message message = new Message();
                if (bean != null) {
                    if (bean.getStatus() == 1) {
                        message.obj = bean;
                        message.what = 1;
                        mHandler.sendMessage(message);
                    }
                } else {
                    message.what = 4;
                    mHandler.sendMessage(message);
                }
            }
        }.start();
    }

    /**
     * 数据回调处理
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // getDialogLoading().removeDialog();
            Toast.makeText(mContext, "分享完成", Toast.LENGTH_SHORT).show();
            switch (msg.what) {
                case 1:
                    AwardJson bean_cre = ((AwardJson) msg.obj);
                    String str_money = "";
                    String str_score = "";
                    if (bean_cre.getData().getAddmoney() > 0) {
                        str_money = " 铜币 +" + bean_cre.getData().getAddmoney();
                    }
                    if (bean_cre.getData().getAddscore() > 0) {
                        str_score = " 积分 +" + bean_cre.getData().getAddscore();
                    }
                    if (!TextUtils.isEmpty(str_money)
                            || !TextUtils.isEmpty(str_score)) {
                        // getDialogLoading().errDialog(str_money + str_score);
                    }
                    LoginUtil.saveAccountInfo(XApplication.getInstance()
                                    .getAccountBean().getLevel(), bean_cre.getData()
                                    .getScore(), bean_cre.getData().getMoney(),
                            XApplication.getInstance().getAccountBean()
                                    .getPhotoUrl());
                    break;
            }
        }

        ;
    };

}
