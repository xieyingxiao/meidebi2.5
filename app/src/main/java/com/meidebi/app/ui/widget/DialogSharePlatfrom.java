package com.meidebi.app.ui.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.meidebi.app.R;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.detail.ShareContentBean;
import com.meidebi.app.service.bean.detail.ShareContentJson;
import com.meidebi.app.service.dao.ShareContentDao;
import com.meidebi.app.support.component.lbs.OauthUtil;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.support.utils.content.ContentUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class DialogSharePlatfrom {
    private OauthUtil oauthUtil;
    private ShareContentBean bean;
    private ShareContentDao dao;
    private String detail_id;
    private final int weixin = 1;
    private final int weibo = 2;
    private final int tecentwb = 3;
    private final int qzone = 4;
    private final int weixin_frd = 5;
    private final int qq_frd = 6;
    private String price;
    private String title;
    private boolean processFlag = true; // 默认可以点击
    private int click_count = 0;
    private Activity context;
    MaterialDialog dialog;

    public DialogSharePlatfrom(Activity context) {
        this.context = context;
        dialog = new MaterialDialog.Builder(context)
                .title("分享给好友")
                .customView(R.layout.dialog_social_share, true)
                .build();
        initViews();
    }

    public void show() {
        dialog.show();
    }
//	

    public String getDetail_id() {
        return detail_id;
    }


    public void setDetail_id(String detail_id) {
        this.detail_id = detail_id;
    }


    public String getPrice() {
        return price;
    }


    public void setPrice(String price) {
        this.price = price;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    protected void initViews() {
        ButterKnife.inject(this, dialog.getCustomView());

    }

    @OnClick({R.id.btn_share_sina, R.id.btn_share_weixin, R.id.btn_share_txwb, R.id.btn_share_qzone, R.id.btn_share_weixin_frd, R.id.btn_share_qq_frd})
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        click_count = click_count + 1;

        new TimeThread().start();
        switch (arg0.getId()) {
            case R.id.btn_share_sina:
                buildShareContent(weibo);
                break;
            case R.id.btn_share_weixin:
                if (processFlag && click_count < 3) {
                    setProcessFlag();//
                    buildShareContent(weixin);
                } else if (click_count > 5) {
                    Toast.makeText(context, "亲,歇一歇再点击。。", Toast.LENGTH_SHORT).show();
                    click_count = 0;
                }
                break;
            case R.id.btn_share_txwb:

                buildShareContent(tecentwb);
                break;
            case R.id.btn_share_qzone:
                buildShareContent(qzone);
                break;
            case R.id.btn_share_weixin_frd:
                if (processFlag && click_count < 3) {
                    setProcessFlag();//
                    buildShareContent(weixin_frd);
                } else if (click_count > 5) {
                    Toast.makeText(context, "亲,歇一歇再点击。。", Toast.LENGTH_SHORT).show();
                    click_count = 0;
                }
                break;
            case R.id.btn_share_qq_frd:
                buildShareContent(qq_frd);
                break;

            default:
                break;
        }
    }

    public OauthUtil getOauthUtil() {
        if (oauthUtil == null) {
            oauthUtil = new OauthUtil(context);
        }
        return oauthUtil;
    }

    // [end]
    /**
     * 数据回调处理
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            bean = (ShareContentBean) msg.obj;
            shareToPlatform(msg.what);
        }
    };

    /**
     * 创建分享的文本内容
     *
     * @return 分类标签+标题+跳转网址
     */
    public void buildShareContent(final int type) {
        if (bean == null) {
            new Thread() {
                @Override
                public void run() {
                    ShareContentJson json = getDao().getShareContent(detail_id);
                    if (json != null) {
                        if (json.getStatus() == 1) {
                            Message message = new Message();
                            ShareContentBean content_bean = new ShareContentBean();
                            if (LoginUtil.isAccountLogin()) {
                                content_bean = json.getData();
                                CommonJson<Object> url_json = getDao().getUniShareUrl(
                                        content_bean.getUrl());
                                if (url_json.getStatus() == 1) {
                                    String include_url = (String) url_json
                                            .getData();
                                    if (!include_url.contains("http://")) {
                                        include_url = AppConfigs.APP_SITE
                                                + include_url;
                                    }
                                    content_bean.setUni_url(include_url);
                                }
                                message.obj = content_bean;
                            } else {
                                message.obj = json.getData();
                            }
                            message.what = type;
                            mHandler.sendMessage(message);
                        }
                    }
                }
            }.start();
        } else {
            shareToPlatform(type);
        }
    }

    public void shareToPlatform(int type) {
        String url = null;
        if (!TextUtils.isEmpty(bean.getUni_url())) {
            url = bean.getUni_url();
            bean.setSina_weibocontent(bean.getSina_weibocontent().replaceAll(
                    bean.getUrl(), bean.getUni_url()));// 替换微博里的链接
            bean.setQq_weibocontent(bean.getQq_weibocontent().replaceAll(
                    bean.getUrl(), bean.getUni_url()));// 替换微博里的链接
        } else {
            url = bean.getUrl();
        }
        switch (type) {
            case weibo:
                getOauthUtil().ShareToSina(bean.getSina_weibocontent(),
                        bean.getImage());
                break;
            case weixin:
                String build_title = null;
                if (TextUtils.isEmpty(price)) {
                    build_title = title;
                } else {
                    build_title = ContentUtils.cutTextFromPosition(title, 0, 25)
                            + "...." + price;
                }
                getOauthUtil().ShareToWeixin(build_title,
                        ContentUtils.getTextFromHtml(bean.getQq_share_content()),
                        bean.getImage(), url);
                break;
            case qzone:
                getOauthUtil().ShareToQzone(bean.getQq_share_title(),
                        ContentUtils.getTextFromHtml(bean.getQq_share_content()),
                        bean.getImage(), url);
                break;
            case tecentwb:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, bean.getQq_share_title() + " "
                        + url);
                context.startActivity(Intent.createChooser(intent, "分享"));
                break;
            case weixin_frd:
                getOauthUtil().ShareToWeixinFrd(bean.getQq_share_title(),
                        ContentUtils.getTextFromHtml(bean.getQq_share_content()),
                        bean.getImage(), url);
                break;
            case qq_frd:
                getOauthUtil().ShareToQQ(bean.getQq_share_title(),
                        ContentUtils.getTextFromHtml(bean.getQq_share_content()),
                        bean.getImage(), url);
                break;
        }

    }

    /**
     * 设置按钮在短时间内被重复点击的有效标识（true表示点击有效，false表示点击无效）
     */
    private synchronized void setProcessFlag() {
        processFlag = false;
    }

    /**
     * 计时线程（防止在一定时间段内重复点击按钮）
     */
    private class TimeThread extends Thread {
        public void run() {
            try {
                sleep(2000);
                processFlag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ShareContentDao getDao() {
        if (dao == null) {
            dao = new ShareContentDao(context);
        }
        return dao;
    }

    public void setContent(String content, String imgUrl, String url) {
        bean = new ShareContentBean();
        bean.setQq_share_content(content);
        bean.setQq_share_title(title);
        bean.setSina_weibocontent(title);
        bean.setQq_weibocontent(content);
        bean.setQq_share_user_default_word(title);
        bean.setImage(imgUrl);
        bean.setUrl(url);
    }

    public static String TAG = "jayne";

//	public static void show(FragmentActivity activity) {
//		new SimpleDialogFragment().show(activity.getSupportFragmentManager(), TAG);
//	}
//
//	@Override
//	public BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
//		builder.setTitle("分享给好友");
//		View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_social_share, null);
//		builder.setView(view);
//		initViews(view);
//		return builder;
//	}
}
