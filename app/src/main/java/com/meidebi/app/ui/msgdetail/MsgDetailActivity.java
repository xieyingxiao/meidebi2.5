package com.meidebi.app.ui.msgdetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.AppAction;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.base.MsgBaseBean;
import com.meidebi.app.service.bean.detail.MsgDetailJson;
import com.meidebi.app.service.bean.msg.MsgDetailBean;
import com.meidebi.app.service.dao.detail.MsgDetailDao;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.Utility;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.support.utils.content.CommonUtil;
import com.meidebi.app.support.utils.content.TimeUtil;
import com.meidebi.app.ui.base.BaseFadingActivity;
import com.meidebi.app.ui.browser.BrowserImgActivity;
import com.meidebi.app.ui.browser.BrowserWebActivity;
import com.meidebi.app.ui.picker.PickerActivity;
import com.meidebi.app.ui.submit.CommentActivity;
import com.meidebi.app.ui.view.asynimagewebview.InterFaceLoadWebview;
import com.meidebi.app.ui.view.asynimagewebview.Parser;
import com.meidebi.app.ui.view.asynimagewebview.WebViewHelper;
import com.meidebi.app.ui.widget.DialogSharePlatfrom;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
 import info.hoang8f.widget.FButton;

@SuppressLint("NewApi")
public class MsgDetailActivity extends BaseFadingActivity implements InterFaceLoadWebview{

    private MsgDetailDao dao;
    private MsgDetailBean bean;
    // views
    private Boolean hasBean = true;

    private String detail_id;
    private Boolean Hasvoted = false;
    private MsgBaseBean baseBean;
    private Boolean isFav = false;
    private Menu mMenu;
    private String userid = XApplication.getInstance().getAccountBean().getId();
    private DialogSharePlatfrom dialog_share;
    private WebViewHelper webViewHelper;
    private boolean loadImage = false;
    @InjectView(R.id.btn_msg_detail_good)
    TextView tv_good;
    @InjectView(R.id.btn_msg_detail_fav)
    TextView tv_fav;
    @InjectView(R.id.btn_msg_detail_comment)
    TextView tv_comment;
    @InjectView(R.id.btn_msg_detail_goto_browser)
    FButton tv_goto;
    @InjectView(R.id.tv_msg_detail_title)
    TextView tv_title;
    @InjectView(R.id.tv_user_name)
    TextView tv_nickname;
    @InjectView(R.id.tv_post_time)
    TextView tv_time;
    @InjectView(R.id.iv_user_avantar)
    ImageView iv_writer;
    @InjectView(R.id.ll_web)
    LinearLayout ll_webview;
    @InjectView(R.id.tv_msg_detail_pro_price)
    TextView tv_proprice;

    @InjectView(R.id.tv_msg_detail_field1)
    TextView tv_field1;
    @InjectView(R.id.tv_msg_detail_field2)
    TextView tv_field2;
    @InjectView(R.id.tv_msg_detail_field3)
    TextView tv_field3;
    @InjectView(R.id.tv_msg_detail_field4)
    TextView tv_field4;
    @InjectView(R.id.tv_msg_detail_field5)
    TextView tv_field5;
    @InjectView(R.id.tv_msg_detail_field6)
    TextView tv_field6;
    @InjectView(R.id.tv_msg_detail_field7)
    TextView tv_field7;
    @InjectView(R.id.tv_detail_site)
    TextView tv_detail_site;
//    @InjectView(R.id.ll_iv)
//    FrameLayout ll_iv;
    @InjectView(R.id.web)
    ObservableWebView mWebView;
//    @InjectView(R.id.iv_blur)
//    ImageView iv_blur;

    @InjectView(R.id.image)
    ImageView mImageView;
    @InjectView(R.id.parent_layout)
    View parent_layout;
    public WebViewHelper getWebViewHelper(){
        if(webViewHelper==null) {
            webViewHelper = new WebViewHelper(MsgDetailActivity.this);
            webViewHelper.setInterFaceLoadWebview(this);
        }
        return webViewHelper;
    }

    private DialogSharePlatfrom getDialogSharePlatfrom() {
        if (dialog_share == null) {
            dialog_share = new DialogSharePlatfrom(this);

        }
        if (bean == null && baseBean != null) {
            dialog_share.setDetail_id(baseBean.getId());
            dialog_share.setTitle(baseBean.getTitle());
            dialog_share.setPrice(baseBean.getPrice());
        } else {
            dialog_share.setDetail_id(bean.getId());
            dialog_share.setTitle(bean.getTitle());
            dialog_share.setPrice(bean.getPrice());
        }
        return dialog_share;
    }

    // [end]
    /**
     * 数据回调处理
     */
    private Handler mHandler = new Handler() {
        @SuppressLint("ValidFragment")
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case 1:// 刷新
//                    // getView_load().onDone();

                     bean = (MsgDetailBean) msg.obj;
                    if (bean.getContry() != null) {
                        bean.setmContryName(bean.getContry().getName());
                    }
                    isFav = (bean.getIsfav() == 1);
                    setFavSelected(isFav);
//                    BuildWebView();
                    SetWebViewData();
                    RefreshViewData();

                    break;
                case 2:// 赞 弱 成功
                    // dissmissDialog();
                    Hasvoted = true;
                    if (msg.arg2 == 1) {
                        tv_good.setText(String.valueOf(bean.getVotesp() + msg.arg2));
                        bean.setVotesp(Integer.parseInt(tv_good.getText()
                                .toString()));
                        bean.setHasVoteSp(userid);
                        tv_good.setSelected(true);
                    }
//                    else {
//                        tv_weak.setText(String.valueOf(bean.getVotem() + msg.arg2));
//                        bean.setVotesp(Integer.parseInt(tv_weak.getText()
//                                .toString()));
//                        bean.setHasVoteEm(userid);
//                        tv_weak.setSelected(true);
//                    }
                    break;
                case 3:// 赞 弱 失败
                    bean.setHasVoteSp(userid);
                    showErr(R.string.hasvote);
                    tv_good.setSelected(true);
                    // Toast.makeText(MsgDetailActivity.this,
                    // getString(R.string.hasvote), Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    isFav = !isFav;
                    setFavSelected(isFav);
                    bean.setIsfav(isFav ? 1 : 0);
                    if (isFav) {
                        bean.setFavnum(bean.getFavnum()+1);
                        showErr("已收藏!");
                    } else {
                        bean.setFavnum(bean.getFavnum()-1);
                    }

                    tv_fav.setText(bean.getFavnum()+"");

                    // showErr(R.string.msg_fav_sucess);
                    break;
                case NETERR:
                    getView_load().onFaied();
                    // dissmissDialog();
                    showErr(R.string.timeout);
                    break;
                case DATAERR:
                   getView_load().onFaied();
                    // dissmissDialog();
                    break;
                case 5:
                    parse((String) msg.obj);
                    bean.setWebViewCache((String) msg.obj);
                    break;
                case 6:
                    bean = (MsgDetailBean) msg.obj;
                    if (bean.getContry() != null) {
                        bean.setmContryName(bean.getContry().getName());
                    }
                    isFav = (bean.getIsfav() == 1);
                    setFavSelected(isFav);
                    RefreshViewData();
                    downloadHtml();
                    break;
            }
        }

    };


    /**
     * 改变intro视图
     */
    private void RefreshViewData() {
//        if (!hasBean) {
            setViewData(bean);
//        }
        BuildFeildViewData();
        // BuildShowOrderWebView();
        imageLoader.displayImage(bean.getPhoto(), iv_writer,
                AppConfigs.IMG_SMALLOPTIONS);
        if (!TextUtils.isEmpty(bean.getHasVoteSp())
                && bean.getHasVoteSp().equals(userid)) {
            tv_good.setSelected(true);
        }
//        else if (!TextUtils.isEmpty(bean.getHasVoteEm())
//                && bean.getHasVoteEm().equals(userid)) {
//            tv_.setSelected(true);
//        }
    }

    private void SetWebViewData() {
        if (!TextUtils.isEmpty(bean.getWebViewCache())) {
            parse(bean.getWebViewCache());
        } else {
            downloadHtml();
        }
    }

    /**
     * 获取详情数据
     */
    private void doGetDetail() {
        // showLoading();
          getView_load().onLoad();
        new Thread() {
            @Override
            public void run() {
                Message message = new Message();
                MsgDetailBean dbbean = Select
                        .from(MsgDetailBean.class)
                        .where(new Condition[]{new Condition("ID")
                                .eq(detail_id)}).first();
                if (dbbean != null && dbbean.getOuturl() != null) {
                    MsgDetailJson json = null;
                    message.obj = dbbean;
                    message.what = 1;
                    mHandler.sendMessage(message);
                    json = getDao().getDetail(detail_id);
                    if (json != null) {
                        if (json.getStatus() == 1) {
                            message = new Message();
                            message.obj = json.getData();
                            message.what = 6;
                            mHandler.sendMessage(message);
                        }
                    }
                } else {
                    MsgDetailJson bean = null;
                    bean = getDao().getDetail(detail_id);
                    if (bean != null) {
                        if (bean.getStatus() == 1) {
                            message.obj = bean.getData();
                            message.what = 1;
                            mHandler.sendMessage(message);
                        }
                    } else {
                        message.what = DATAERR;
                        mHandler.sendMessage(message);
                    }
                }

            }
        }.start();
    }

    /**
     * 赞
     */
    private void doVote(final String type) {
        if (!Hasvoted) {
            if (LoginUtil.isAccountLogin(this)) {
                new Thread() {
                    @Override
                    public void run() {
                        CommonJson<Object> bean = null;
                        bean = getDao().doVote(detail_id, type, "1");
                        Message message = new Message();
                        if (bean != null) {
                            message.obj = bean.getData();
                            message.arg2 = Integer.parseInt(type);
                            if (bean.getStatus() == 1) {
                                message.what = 2;
                            } else if (bean.getStatus() == 0) {
                                message.what = 3;
                            }
                            mHandler.sendMessage(message);
                        } else {
                            message.what = NETERR;
                            mHandler.sendMessage(message);
                        }
                    }
                }.start();
            }
        } else {
            showErr(R.string.hasvote);
        }
    }

    /**
     * 收藏
     */
    private void doFav(final String type) {
        if (LoginUtil.isAccountLogin(this)) {
            new Thread() {
                @Override
                public void run() {
                    CommonJson bean = null;
                    bean = getDao().doFav(detail_id, type);
                    Message message = new Message();
                    if (bean != null) {
                        message.obj = bean.getData();
                        if (bean.getStatus() == 1) {
                            message.what = 4;
                        }
                        mHandler.sendMessage(message);
                    } else {
                        message.what = NETERR;
                        mHandler.sendMessage(message);
                    }
                }
            }.start();
        }
    }

    public MsgDetailDao getDao() {
        if (dao == null) {
            dao = new MsgDetailDao(this);
            dao.setType(2);
        }
        return dao;
    }

    public void setDao(MsgDetailDao dao) {
        this.dao = dao;
    }

    // [start]生命周期
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ButterKnife.inject(this);

        BuildViewData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWebViewHelper().onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        getWebViewHelper().onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // [end]


    public void dealActionBarTitle() {
        if (baseBean.isIsabroad() == 1) {
            setTitle("海淘优惠");


        } else {
            switch (baseBean.getLinktype()) {
                case 1:
                    setTitle("国内优惠");

                    break;
                case 2:
                    setTitle("活动详情");

                    break;
                case 3:
                    setTitle("优惠券详情");

                    break;
                default:
                    break;
            }
        }
    }


    // [end]

    // [start]继承方法
    @OnClick({R.id.btn_msg_detail_good,R.id.btn_msg_detail_fav, R.id.btn_msg_detail_goto_browser, R.id.btn_msg_detail_comment,R.id.image})
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.btn_msg_detail_comment) {
            IntentUtil.start_activity(
                    this,
                    CommentActivity.class,
                    new BasicNameValuePair("id", detail_id),
                    new BasicNameValuePair("linkType", AppAction.Comment_NORMAL));

        } else if (bean != null) {

            switch (v.getId()) {
                case R.id.btn_msg_detail_good:
                    doVote(String.valueOf(1));
                    break;
//            case R.id.btn_msg_detail_weak:
//                doVote(String.valueOf(-1));
//                break;
                case R.id.btn_msg_detail_goto_browser:
                    IntentUtil.jumpToBroswerActivity(this,
                            BrowserWebActivity.class, new BasicNameValuePair("url",
                                    bean.getOuturl()), new BasicNameValuePair(
                                    "title", bean.getTitle()));

                    break;
                case R.id.btn_msg_detail_fav:
                    doFav(String.valueOf(bean.getLinktype()));
                    // getSharePlatfrom().showActionWindow(v);
                    break;

                case R.id.image:
                    // String Imageurl = null;

                    ArrayList<String> singleImage = new ArrayList<String>();
                    if (bean.getImage() != null) {
                        singleImage.add(bean.getImage());
                    } else {
                        singleImage.add(baseBean.getImage());

                    }

                    Intent intent = new Intent(MsgDetailActivity.this,
                            BrowserImgActivity.class);
                    intent.putStringArrayListExtra(PickerActivity.ALBUM_KEY, singleImage);
                    intent.putExtra(BrowserImgActivity.IMAGE_POSITION, 0);
                    startActivity(intent);
//                IntentUtil.start_activity(this, BrowserImgActivity.class,
//                        new BasicNameValuePair("path", bean.getRemoteimage()),
//                        new BasicNameValuePair("thumb", bean.getImage()));
                    break;
                default:
                    break;
            }
        } else {
            Toast.makeText(MsgDetailActivity.this, "数据加载中，请稍等", Toast.LENGTH_SHORT).show();
        }

    }

    // [end]

    // buidView start


    private void BuildFeildViewData() {
        // TODO Auto-generated method stub
         switch (bean.getLinktype()) {
            case 1:
                BuildViewSingel();// 单品
                break;
            case 2:
                BuildViewAct();// 促销
                break;
            case 3:
                BuildViewCoupon();// 优惠券
                break;
            default:
                break;
        }
    }

    private void BuildViewData() {
        Bundle bundle = getIntent().getExtras();
//        String ex_content = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        if (ex_content == null) {
            baseBean = (MsgDetailBean) getIntent().getSerializableExtra("bean");
            if (baseBean != null) {
                detail_id = baseBean.getId();
                setViewData(baseBean);
                dealActionBarTitle();
            } else {
                hasBean = false;
                detail_id = getIntent().getStringExtra("id");
            }
//        } else {
//            parseData(ex_content);
//
//        }

            doGetDetail();

    }

    private void setViewData(final MsgBaseBean bean) {
        if (!TextUtils.isEmpty(bean.getImage())) {
            if(!loadImage) {
                String imgUrl = bean.getLinktype() == 2 ? bean.getRemoteimage()
                        : bean.getImage();

                imageLoader.displayImage(imgUrl, mImageView,
                        AppConfigs.IMG_SMALLOPTIONS, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view,
                                                        FailReason failReason) {
                            }

                            @Override
                            public void onLoadingComplete(String imageUri,
                                                          View view, Bitmap loadedImage) {
                                loadImage = true;
                                double size = (double) (Math.round(Utility
                                        .getScreenWidth(MsgDetailActivity.this)
                                        * 100.0 / loadedImage.getWidth()) / 100.0);
                                int padding = getResources().getDimensionPixelSize(
                                        R.dimen.detail_layout_padding);
                                int minHeight = getResources()
                                        .getDimensionPixelSize(
                                                R.dimen.detail_img_min_height);
                                if (loadedImage.getWidth()
                                        - loadedImage.getHeight() > 10) {
                                    int finalheight = (int) (loadedImage
                                            .getHeight() * size);
                                    FrameLayout.LayoutParams layoutparms = new FrameLayout.LayoutParams(
                                            Utility.getScreenWidth(MsgDetailActivity.this),
                                            AppConfigs.getCoverHeight());
//                                    LinearLayout.LayoutParams parent_layoutparms = new LinearLayout.LayoutParams(
//                                            Utility.getScreenWidth(MsgDetailActivity.this),
//                                            finalheight);
                                    AppLogger.e("finalheight" + finalheight);
                                    mImageView.setMaxHeight(finalheight);
 //                                ll_iv.setLayoutParams(parent_layoutparms);
                                    mImageView.setLayoutParams(layoutparms);

//                                helper.updateHeaderHeight(finalheight);
                                } else {
                                    FrameLayout.LayoutParams layoutparms = (FrameLayout.LayoutParams) mImageView.getLayoutParams();
                                    layoutparms.height = minHeight;
                                    layoutparms.width = minHeight;
//                                 int pixel  = loadedImage.getPixel(loadedImage.getWidth()-10, 10);
//                                int pixel2  = loadedImage.getPixel(10, loadedImage.getHeight()-10);//对角
//
//                                if(Utility.getBestMatchingColorName(pixel).equals("white")&&Utility.getBestMatchingColorName(pixel2).equals("white")) {
//                                    layoutparms.setMargins(0, padding, 0, padding);
                                    mImageView.setLayoutParams(layoutparms);
//                                    ll_iv.setBackgroundColor(pixel2);
//                                 }else{
//                                    int fillCoverHeight = getResources()
//                                            .getDimensionPixelSize(
//                                                    R.dimen.detail_img_min_height)+getActionBarSize()+padding;
//                                    layoutparms.setMargins(0, getActionBarSize()+padding, 0, padding);
//                                    mImageView.setLayoutParams(layoutparms);
//                                    FrameLayout.LayoutParams blur_layoutparms = new FrameLayout.LayoutParams(
//                                            AppConfigs.getScreenWidth(), fillCoverHeight);
//                                    iv_blur.setLayoutParams(blur_layoutparms);
//                                    Bitmap cropScaleBitmap = Bitmap.createBitmap(loadedImage,
//                                            loadedImage.getWidth()*3/10,loadedImage.getHeight()*3/10,
//                                            (loadedImage.getWidth()-(loadedImage.getWidth()*3/5)), (loadedImage.getWidth()-(loadedImage.getWidth()*3/5)));
//                                    Bitmap bmpBlurred = Blur.fastblur(MsgDetailActivity.this, cropScaleBitmap, 25);
//                                    bmpBlurred = Bitmap.createScaledBitmap(bmpBlurred, AppConfigs.getScreenWidth(), fillCoverHeight, false);
//                                    iv_blur.setImageBitmap(bmpBlurred);
//                                    cropScaleBitmap.recycle();

//                                }

                                }
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri,
                                                           View view) {
                            }
                        });
            }
        } else {
            mImageView.setVisibility(View.GONE);
            ll_img.setVisibility(View.GONE);
//            FrameLayout.LayoutParams parent_layoutparms = new FrameLayout.LayoutParams(0,
//                    0);
//            ll_iv.setLayoutParams(parent_layoutparms);
//            helper.updateHeaderHeight(getResources().getDimensionPixelSize(R.dimen.header_gradient_height));
        }

        // iv_msg.setImageUrl(bean.getImage(),
        // AppConfigs.Loading_List_Img_Bg_Mid);
        tv_detail_site.setText(bean.getSitename());
        mImageView.setClickable(this.bean != null);
        tv_title.setText(bean.getTitle());
        tv_nickname.setText(bean.getNickname());
        if (!TextUtils.isEmpty(bean.getPrice())) {
            tv_proprice.setVisibility(View.VISIBLE);
            if (!bean.getPrice().substring(0, 1).equals("￥")) {
                tv_proprice.setText("￥" + bean.getPrice());
            } else {
                tv_proprice.setText(bean.getPrice());
                bean.setPrice(bean.getPrice().replaceAll("￥", ""));
            }
        }
        tv_good.setText(String.valueOf(bean.getVotesp()));
        tv_fav.setText(String.valueOf(bean.getFavnum()));
        tv_comment.setText(String.valueOf(bean.getCommentcount()));
        tv_time.setText(TimeUtil.getListTime(bean.getCreatetime()) + "推荐");
        tv_time.setVisibility(View.VISIBLE);
    }

    private void BuildViewSingel() {
        // TODO Auto-generated method stub
        // tv_proprice_pre.setVisibility(View.VISIBLE);
        tv_proprice.setVisibility(View.VISIBLE);
        String pre_price = getString(R.string.msg_price);// 正常价格前缀
         if (!bean.getPrice().substring(0, 1).equals("￥")) {
            tv_proprice.setText("￥" + bean.getPrice());
        } else {
            tv_proprice.setText(bean.getPrice());
            bean.setPrice(bean.getPrice().replaceAll("￥", ""));
        }

        if (!bean.getOrginprice().equals("￥0.00")
                && !bean.getPrice().equals(bean.getOrginprice())
                && !bean.getOrginprice().equals("￥0")) {
            CommonUtil.BuildFieldText(tv_field1, pre_price,
                    bean.getOrginprice());
            tv_field1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        tv_field3.setVisibility(View.VISIBLE);
        String postage = bean.getPostage();
        if (!postage.substring(0, 1).equals("￥")
                && (bean.getPostage().length() > 0)
                && (Character.isDigit(bean.getPostage().charAt(0)))) {
            postage = "￥" + bean.getPostage();


        } else {
            postage =  bean.getPostage();

         }
        String freight = "";
        if (!bean.getFreight().substring(0, 1).equals("$")) {
            freight = "$" + bean.getFreight();
         } else {
            freight =  bean.getFreight();

        }

         if (bean.isIsabroad() == 1) {
               if(!freight.equals("$0.00")){
                   String directtariff = "";
                   if (!bean.getDirecttariff().substring(0, 1).equals("$")) {
                       directtariff = "$" + bean.getDirecttariff();
                   } else {
                       directtariff =  bean.getDirecttariff();

                   }


                   tv_field3.setText("推荐的物流方式：直邮");
                   tv_field4.setText("直邮费用："+freight+"　直邮关税："+directtariff);
                   tv_field4.setVisibility(View.VISIBLE);
               }else{
                   if(bean.getTransitcompany()!=null) {
                       tv_field3.setText("海外转运：" + bean.getTransitcompany().getName());
                   }
                   tv_field4.setText("所在国家："+bean.getmContryName()+"　本土邮费："+postage);
                   tv_field4.setVisibility(View.VISIBLE);
                }

        } else {
             tv_field3.setText("货物运费："+postage);
        }

        CommonUtil.combineTwoText(tv_field2, "", BuildWhorsubsitesLayout());
    }

    private void BuildViewCoupon() { // 优惠券
        setTitle(getString(R.string.msg_coupon_title));
        String pre_whole = getString(R.string.msg_coupon_the_whole);// 限制全站
        String pre_new = getString(R.string.msg_coupon_limit_the_new);// 限制新用户
        String pre_start_time = getString(R.string.msg_coupon_time);
        String pre_use_start_time = getString(R.string.msg_coupon_use_time);// 使用开始时间
        String pre_use_status = getString(R.string.msg_coupon_status);
        BuildWhorsubsitesLayout(tv_field2);
        CommonUtil.BuildFieldText(
                tv_field3,
                pre_start_time,
                TimeUtil.getDate(bean.getGetstarttime()) + "-"
                        + TimeUtil.getDate(bean.getGetendtime()));
        CommonUtil.BuildFieldText(
                tv_field4,
                pre_use_start_time,
                TimeUtil.getDate(bean.getDostarttime()) + "-"
                        + TimeUtil.getDate(bean.getDoendtime()));
        CommonUtil.BuildFieldText(tv_field5, pre_new,
                bean.getLimitnew() == 0 ? false : true);
        CommonUtil.BuildFieldText(tv_field6, pre_whole,
                bean.getQuanchang() == 0 ? false : true);
        CommonUtil.BuildFieldText(tv_field7, pre_use_status, BuildCouponText());
        tv_goto.setText(getString(R.string.msg_coupon_get_it));
    }

    private void BuildViewAct() {

    }

    private String BuildWhorsubsitesLayout() {// 处理仓库行文字
        if (bean.getWhorsubsites() != null) {// 仓库有值否
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bean.getWhorsubsites().size(); i++) {
                sb.append(bean.getWhorsubsites().get(i) + " ");
            }
            if (TextUtils.isEmpty(sb.toString())) {
                return null;
            }
            String pre_ware = "";
            switch (bean.getSubsiteorwh()) {
                case 1:
                    pre_ware = getString(R.string.msg_warehouse);// 显示为仓库
                    break;
                case 2:
                    pre_ware = getString(R.string.msg_substation);// 显示为子站
                    break;
                default:
                    break;
            }
            if (bean.getLinktype() == 2 || bean.getLinktype() == 3) {// 促销
                pre_ware = getString(R.string.msg_act_warehouse) + pre_ware;// 仓库添加促销两字
            }

            return pre_ware + " " + sb.toString();
        }
        return null;
    }

    private void BuildWhorsubsitesLayout(TextView tx) {// 处理仓库行文字
        if (bean.getWhorsubsites() != null) {// 仓库有值否
            tx.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bean.getWhorsubsites().size(); i++) {
                sb.append(bean.getWhorsubsites().get(i) + " ");
            }
            String pre_ware = "";
            switch (bean.getSubsiteorwh()) {
                case 1:
                    pre_ware = getString(R.string.msg_warehouse);// 显示为仓库
                    break;
                case 2:
                    pre_ware = getString(R.string.msg_substation);// 显示为子站
                    break;
                default:
                    break;
            }
            if (bean.getLinktype() == 2 || bean.getLinktype() == 3) {// 促销
                pre_ware = getString(R.string.msg_act_warehouse) + pre_ware;// 仓库添加促销两字
            }
            tx.setText(pre_ware + " " + sb.toString());
        }

    }

    private String BuildCouponText() {
        String status = null;
        switch (bean.getStatus()) {
            case 0:
                status = getString(R.string.msg_coupon_status_no);
                break;
            case 1:
                status = getString(R.string.msg_coupon_status_yes);
                break;

            default:
                break;
        }
        return status;
    }

    // buildview end

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** 使用SSO授权必须添加如下代码 */
        UMSocialService mController = UMServiceFactory.getUMSocialService(
                "com.umeng.share", RequestType.SOCIAL);
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
                requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public void parseData(String json) {
        try {
            JSONObject jsonContent = new JSONObject(json);
            detail_id = jsonContent.getString("linkid");
        } catch (JSONException e) {
        }
    }


    @Override
    public void onReload() {// 点击回调
        // TODO Auto-generated method stub
        doGetDetail();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
         super.onDestroy();
        getWebViewHelper().onDestroy();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        mMenu = menu;
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_share:
                getDialogSharePlatfrom().show();
                 return true;
//            case R.id.menu_unfav:
//                if (bean != null)
//                    doFav(String.valueOf(bean.getLinktype()));
//                return true;
//            case R.id.menu_faved:
//                if (bean != null)
//                    doFav(String.valueOf(bean.getLinktype()));
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void setFavSelected(final boolean isfav) {
        tv_fav.setSelected(isfav);
//        if (mMenu != null) {
//            FavSelected(isfav);
//        } else {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    FavSelected(isfav);
//                }
//            }, 1000);
//        }
    }

//    private void FavSelected(boolean isfav) {
//        if (isfav) {
//
//        } else {
//
//        }
//    }


    private void downloadHtml() {
        new Thread() {
            @Override
            public void run() {
                Message message = new Message();
                String result = getDao().getStringFromUrl(
                        HttpUrl.BASIC_URL + bean.getDescription());
                if (result != null) {
                    message.obj = result;
                    message.what = 5;
                    mHandler.sendMessage(message);
                }
            }
        }.start();
    }

    public void parse(final String html) {
        final Parser mParser = new Parser(null, mWebView, this) {
            @Override
            public String downloadHtml() {
                return html;
            }
        };
          getWebViewHelper().execute(mWebView, mParser);


        }



        @Override
        protected int getLayoutResource () {
            return R.layout.activity_detail;
        }

        @Override
        public void onBackPressed () {
            super.onBackPressed();
            if (bean != null) {
                SugarRecord.save(bean);
            }
        }


    @Override
    public void loadWebViewFinish() {
        getView_load().onDone();
        mWebView.setVisibility(View.VISIBLE);
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
//        alphaAnimation.setFillBefore(true);
//        alphaAnimation.setDuration(500);
//        alphaAnimation.setStartOffset( 100);
//        mWebView.startAnimation(alphaAnimation);

    }

    @Override
    public void loadWebViewFailed() {
        getView_load().onFaied();

    }

    @Override
    public void SwipeLeft() {
        IntentUtil.start_activity(
                this,
                CommentActivity.class,
                new BasicNameValuePair("id", detail_id),
                new BasicNameValuePair("linkType", AppAction.Comment_NORMAL));
     }
}
