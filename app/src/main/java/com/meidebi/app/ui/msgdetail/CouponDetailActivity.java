package com.meidebi.app.ui.msgdetail;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.meidebi.app.R;
import com.meidebi.app.base.config.AppAction;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.detail.CouponBean;
import com.meidebi.app.service.bean.detail.CouponData;
import com.meidebi.app.service.dao.CouponDao;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.anim.AnimateFirstDisplayListener;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.support.utils.content.CommonUtil;
import com.meidebi.app.support.utils.content.ContentUtils;
import com.meidebi.app.support.utils.content.TimeUtil;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;
import com.meidebi.app.ui.browser.BrowserWebActivity;
import com.meidebi.app.ui.commonactivity.CommonFragmentActivity;
import com.meidebi.app.ui.submit.CommentActivity;
import com.meidebi.app.ui.user.MyCouponVpFragment;
import com.meidebi.app.ui.view.asynimagewebview.Parser;
import com.meidebi.app.ui.view.asynimagewebview.WebViewHelper;

import org.apache.http.message.BasicNameValuePair;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.hoang8f.widget.FButton;

public class CouponDetailActivity extends BasePullToRefreshActivity implements
        OnClickListener {

    // [start]变量
    /**
     * 数字代表列表顺序
     */

    private CouponDao dao;
    private CouponBean bean;
    @InjectView(R.id.ll_tv_good)
    View ll_good;
    @InjectView(R.id.btn_msg_detail_fav)
    TextView tv_fav;
    @InjectView(R.id.btn_msg_detail_comment)
    TextView tv_comment;
    @InjectView(R.id.btn_msg_detail_goto_browser)
    FButton tv_goto;
    @InjectView(R.id.tv_mycoupon_pass)
    TextView tv_pass;


    @InjectView(R.id.iv_coupon_intro)
    ImageView iv_msg;
    @InjectView(R.id.tv_coupon_detail_title)
    TextView tv_title;
    @InjectView(R.id.tv_coupon_detail_price)
    TextView tv_price;
    @InjectView(R.id.tv_coupon_buy_level)
    TextView tv_lv;
    @InjectView(R.id.tv_coupon_limit_whole)
    TextView tv_whole;
    @InjectView(R.id.tv_coupon_use_start_time)
    TextView tv_start_time;
    @InjectView(R.id.tv_coupon_use_end_time)
    TextView tv_end_time;
    @InjectView(R.id.tv_coupon_get_end_time)
    TextView tv_get_end_time;
    @InjectView(R.id.tv_coupon_limit_new)
    TextView tv_limit_new;
    @InjectView(R.id.tv_coupon_useinfo)
    WebView coupon_use;

    private String detail_id;
    private Boolean isFav = false;

    private boolean isExchange;
    private WebViewHelper webViewHelper;

    // [end]
    /**
     * 数据回调处理
     */
    private Handler mHandler = new Handler() {
        @SuppressLint("ValidFragment")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:// 刷新
                    // getView_load().onDone();
                    bean = (CouponBean) msg.obj;
                    isFav = bean.isFav();
                    RefreshViewData();
                    getView_load().onDone();
                    break;
                case 2:// 赞 弱 成功
                    // dissmissDialog();
                    break;
                case 3:// 赞 弱 失败
                    showErr(R.string.hasvote);
                    break;
                case 4:
                    isFav = !isFav;
                    tv_fav.setSelected(isFav);
                    if (isFav) {
                        bean.setFavnum(bean.getFavnum() + 1);
                        showErr("已收藏!");
                    } else {
                        tv_fav.setText(bean.getFavnum() - 1);
                    }

                    tv_fav.setText(bean.getFavnum() + "");

                    // showErr(R.string.msg_fav_sucess);
                    break;
                case NETERR:
                    getView_load().onFaied();
                    break;
                case DATAERR:
                    getView_load().onFaied();
                    break;
                case 7:// 兑换
                    showErr((String) msg.obj);
                    break;
                case 8:// 兑换
                    showErr("兑换失败");
                    break;
                case 9:
                    showErr("兑换成功");
                    break;
            }
        }

        ;
    };

    /**
     * 改变intro视图
     */
    private void RefreshViewData() {
        setViewData(bean);
        BuildFeildViewData();
    }

    /**
     * 获取详情数据
     */
    private void doGetDetail() {
        // showLoading();
        getDao().getDetail(detail_id, new RestHttpUtils.RestHttpHandler<CommonJson>() {
            @Override
            public void onSuccess(CommonJson result) {
                bean = ((CouponData) result.getData()).getCoupon();
                isFav = bean.isFav();
                RefreshViewData();
                getView_load().onDone();
            }

            @Override
            public void onStart() {
                getView_load().onLoad();

            }

            @Override
            public void onFailed() {

            }
        });


    }

    /**
     * 收藏
     */
    private void doFav() {
        if (LoginUtil.isAccountLogin(this)) {
            new Thread() {
                @Override
                public void run() {
                    CommonJson bean = null;
                    bean = getDao().doFav(detail_id);
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

    /**
     * 兑换
     */
    private void doExchange() {
        if (LoginUtil.isAccountLogin(this)) {
            new MaterialDialog.Builder(this)
                    .title("铜币兑换")
                    .content("兑换该券将扣除" + bean.getCopper() + "铜币")
                    .positiveText(R.string.cancel)
                    .negativeText(R.string.make_sure)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            exchange();
                        }

                    })
                    .show();
        }
    }

    private void exchange() {
        getDao().doExchange(detail_id, new RestHttpUtils.RestHttpHandler<CommonJson>() {
            @Override
            public void onSuccess(CommonJson result) {
                CommonJson<Object> bean = result;
                if (bean.getStatus() == 1) {
                    showErr("兑换成功");
                } else {
                    showErr(bean.getInfo());
                }

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailed() {

            }
        });
    }

    public CouponDao getDao() {
        if (dao == null) {
            dao = new CouponDao(this);
        }
        return dao;
    }

    // [start]生命周期
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar("优惠券详情");
//		setContentView(R.layout.activity_coupon_detail);
        ButterKnife.inject(this);
        ll_good.setVisibility(View.GONE);

        BuildViewData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // [end]


    // [end]

    // [start]继承方法
    @OnClick({R.id.btn_msg_detail_good, R.id.btn_msg_detail_fav, R.id.btn_msg_detail_goto_browser, R.id.btn_msg_detail_comment})
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.btn_msg_detail_comment) {
            IntentUtil.start_activity(
                    this,
                    CommentActivity.class,
                    new BasicNameValuePair("id", detail_id),
                    new BasicNameValuePair("linkType", AppAction.Comment_COUPON));

        } else if (bean != null) {

            switch (v.getId()) {
                case R.id.btn_msg_detail_goto_browser:
                    if (!isExchange) {
                        doExchange();
                    } else {
                        ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        cmb.setText(tv_price.getText().toString());
                        Toast.makeText(CouponDetailActivity.this, "券码已经复制到剪贴板",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_msg_detail_fav:
                    doFav(String.valueOf(bean.getLinktype()));
                case R.id.iv_msg_intro:
                    break;
                case R.id.btn_msg_detail_comment:
                    IntentUtil.start_activity(
                            this,
                            CommentActivity.class,
                            new BasicNameValuePair("id", detail_id),
                            new BasicNameValuePair("linkType", AppAction.Comment_COUPON));

                    break;
                default:
                    break;
            }
        } else {
            Toast.makeText(CouponDetailActivity.this, "数据加载中，请稍等", Toast.LENGTH_SHORT).show();
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
                    bean = getDao().doFav(detail_id);
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

    // [end]

    // buidView start
    private void BuildFeildViewData() {
        // TODO Auto-generated method stub
        BuildViewCoupon();// 优惠券
    }

    private void BuildViewData() {
        Bundle bundle = getIntent().getExtras();
//        String ex_content = bundle.getString(JPushInterface.EXTRA_EXTRA);
        isExchange = bundle.getBoolean("isExchange", false);
        detail_id = getIntent().getStringExtra("id");
        // if (ex_content == null) {
        // // detail_id = baseBean.getId();
        // // setViewData(baseBean);
        // } else {
        // parsePushData(ex_content);// 解析推送字段
        // }
        if (!isExchange) {
            doGetDetail();
        } else {
            bean = (CouponBean) bundle.getSerializable("bean");
            RefreshViewData();
            getView_load().onDone();
        }
    }

    private void setViewData(CouponBean bean) {
        imageLoader
                .displayImage(bean.getImgUrl(), iv_msg,
                        AppConfigs.IMG_MIDDLEOPTIONS,
                        new AnimateFirstDisplayListener());
        // iv_msg.setImageUrl(bean.getImage(),
        // AppConfigs.Loading_List_Img_Bg_Mid);
        iv_msg.setOnClickListener(this);
        iv_msg.setClickable(this.bean != null);
        tv_title.setText(bean.getTitle());
        parse(bean.getDescription());
        tv_fav.setText(bean.getFavnum() + "");
        tv_comment.setText(bean.getCommentcount() + "");
    }

    private void BuildViewCoupon() { // 优惠券

        CommonUtil.formatTextView(tv_whole, !bean.getAllgoods());
        if (bean.getBuylevel() != 0) {
            CommonUtil.formatTextView(tv_lv, "Lv " + String.valueOf(bean.getBuylevel()) + "以上");
        } else {
            CommonUtil.formatTextView(tv_lv, "无限制");
        }
        CommonUtil.formatTextView(tv_limit_new, !bean.getLimitnewuser());
        CommonUtil.formatTextView(tv_get_end_time, TimeUtil.getDate(bean.getGetend()));
        CommonUtil.formatTextView(tv_start_time, TimeUtil.getDate(bean.getUsestart()));
        CommonUtil.formatTextView(tv_end_time, TimeUtil.getDate(bean.getUseend()));


        if (isExchange) {
            // coupon_use.setText("复制到剪贴板");
            tv_goto.setText("复制到剪贴板");
            tv_price.setText(String.valueOf(bean.getCard()));
            if (!TextUtils.isEmpty(ContentUtils.replaceBlank(bean.getPass()))) {
                tv_pass.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.tv_mycoupon_pass)).setText(bean
                        .getPass());
            }
        } else {
            CommonUtil.formatTextView(tv_price, bean.getCopper());
            tv_goto.setText(getString(R.string.msg_gotoExchange));
        }

    }


    // public void parsePushData(String json) {
    // try {
    // JSONObject jsonContent = new JSONObject(json);
    // detail_id = jsonContent.getString("linkid");
    // } catch (JSONException e) {
    // }
    // }

    @Override
    public void onReload() {// 点击回调
        // TODO Auto-generated method stub
        doGetDetail();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        getView_load().onDestroy();
        view_load = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.coupondetail, menu);
        if (LoginUtil.isAccountLogin()) {
            menu.findItem(R.id.menu_mycoupon).setVisible(true);
            if (isExchange) {
                menu.findItem(R.id.menu_mycoupon).setTitle("去购物");
            }
        } else {
            menu.findItem(R.id.menu_mycoupon).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_mycoupon:
                if (isExchange) {
                    AppLogger.e("price" + bean.getSiteurl());
                    if (!TextUtils.isEmpty(bean.getSiteurl())) {
                        IntentUtil.start_activity(CouponDetailActivity.this,
                                BrowserWebActivity.class, new BasicNameValuePair(
                                        "url", bean.getSiteurl()));
                    } else {
                        IntentUtil.start_activity(CouponDetailActivity.this,
                                BrowserWebActivity.class, new BasicNameValuePair(
                                        "url", bean.getHost()));
                    }
                } else {
                    BasicNameValuePair classValuePair = new BasicNameValuePair(CommonFragmentActivity.KEY, MyCouponVpFragment.class.getName());
                    IntentUtil.start_activity(CouponDetailActivity.this, CommonFragmentActivity.class, classValuePair);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_coupon_detail;
    }

    public void parse(final String html) {
        Parser mParser = new Parser(null, coupon_use, this) {
            @Override
            public String downloadHtml() {
                return html;
            }
        };
        webViewHelper = new WebViewHelper(CouponDetailActivity.this);
        // webViewHelper.setBackgroundcolour(R.color.detail_color);
        webViewHelper.execute(coupon_use, mParser);
        webViewHelper.setTitle(bean.getTitle());
    }

    @Override
    public void SwipeLeft() {
        IntentUtil.start_activity(
                this,
                CommentActivity.class,
                new BasicNameValuePair("id", detail_id),
                new BasicNameValuePair("linkType", AppAction.Comment_COUPON));
    }
}

