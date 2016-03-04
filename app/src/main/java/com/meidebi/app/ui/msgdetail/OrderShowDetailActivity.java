package com.meidebi.app.ui.msgdetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.AppAction;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.service.bean.base.BaseItemBean;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.base.MsgBaseBean;
import com.meidebi.app.service.bean.detail.InventoryBean;
import com.meidebi.app.service.bean.detail.OrderShowDetailJson;
import com.meidebi.app.service.bean.msg.OrderShowBean;
import com.meidebi.app.service.dao.detail.MsgDetailDao;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.support.utils.content.TimeUtil;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.base.BaseFadingActivity;
import com.meidebi.app.ui.browser.BrowserImgActivity;
import com.meidebi.app.ui.submit.CommentActivity;
import com.meidebi.app.ui.view.asynimagewebview.InterFaceLoadWebview;
import com.meidebi.app.ui.view.asynimagewebview.Parser;
import com.meidebi.app.ui.view.asynimagewebview.WebViewHelper;
import com.meidebi.app.ui.widget.DialogSharePlatfrom;
import com.meidebi.app.ui.widget.ViewBuyList;
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

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

@SuppressLint("NewApi")
public class OrderShowDetailActivity extends BaseFadingActivity implements InterFaceLoadWebview{

    // [start]变量

    private MsgDetailDao dao;
    private OrderShowBean bean;
    // views
    @InjectView(R.id.btn_msg_detail_good)
    TextView tv_good;
    @InjectView(R.id.btn_msg_detail_fav)
    TextView tv_fav;
    @InjectView(R.id.btn_msg_detail_comment)
    TextView tv_comment;
    @InjectView(R.id.btn_msg_detail_goto_browser)
    TextView tv_goto;
    @InjectView(R.id.adapter_title)
    TextView tv_title;
    @InjectView(R.id.tv_msg_detail_sitename)
    TextView tv_siteName;
    @InjectView(R.id.tv_user_name)
    TextView tv_nick;
    @InjectView(R.id.tv_post_time)
    TextView tv_time;
    @InjectView(R.id.iv_user_avantar)
    ImageView iv_write;
    @InjectView(R.id.web)
    ObservableWebView mWebView;
    @InjectView(R.id.scroll)
    ObservableScrollView scrollView;
    @InjectView(R.id.adapter_image)
    ImageView mImageView;
    private boolean loadImage = false;

    private ViewBuyList buylist;
    private String detail_id;
    private Boolean Hasvoted = false;
    private MsgBaseBean baseBean;
    private Boolean isFav = false;
    private Menu mMenu;
  	private WebViewHelper webViewHelper;
    private String userid = XApplication.getInstance().getAccountBean().getId();
      // [end]
    /**
     * 数据回调处理
     */
    private Handler mHandler = new Handler() {
        @SuppressLint("ValidFragment")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:// 刷新
                    bean = (OrderShowBean) msg.obj;
                    Gson gson = new Gson();
                    if (TextUtils.isEmpty(bean.getWebViewCache())
                            && !TextUtils.isEmpty(bean.getContent())) {
                        bean.setWebViewCache(bean.getContent());
                        bean.setCommentcount(bean.getUserReviewsData()
                                .getCommentnum());
                        bean.setD_siteList(gson.toJson(bean.getSite()));
                        bean.setD_transList(gson.toJson(bean.getTransitlist()));
                    } else {
                        BaseItemBean siteBean = gson.fromJson(bean.getD_siteList(),
                                BaseItemBean.class);
                        bean.setSite(siteBean);
                        BaseItemBean transiteBean = gson.fromJson(
                                bean.getD_transList(),
                                new TypeToken<BaseItemBean>() {
                                }.getType());
                        bean.setTransitlist(transiteBean);
                    }
                    if (bean.getInventorylist() != null) {
                        bean.setBuylist(gson.toJson(bean.getInventorylist()));
                    } else {
                        List<InventoryBean> inventoryBeans = gson.fromJson(
                                bean.getBuylist(),
                                new TypeToken<List<InventoryBean>>() {
                                }.getType());
                        bean.setInventorylist(inventoryBeans);
                    }
                    isFav = (bean.getIsfav() == 1);
                    setFavSelected(isFav);
                    RefreshViewData();
                    parse();


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
                    showErr(R.string.hasvote);
                    bean.setHasVoteSp(userid);
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
                        tv_fav.setText(bean.getFavnum()-1);
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
                case 110:
                getView_load().onDone();

                    break;
            }
        }

        ;
    };

    private DialogSharePlatfrom dialog_share;


    private DialogSharePlatfrom getDialogSharePlatfrom() {
        if (dialog_share == null) {
            dialog_share = new DialogSharePlatfrom(this);

        }
        if (bean == null && baseBean != null) {
            dialog_share.setDetail_id(baseBean.getId());
            dialog_share.setTitle(baseBean.getTitle());
            dialog_share.setPrice(baseBean.getPrice());
            dialog_share.setContent(baseBean.getDescription(), bean.getPic(),
                    "http://www.meidebi.com/shaidan/" + bean.getId() + ".html");
        } else {
            dialog_share.setDetail_id(bean.getId());
            dialog_share.setTitle(bean.getTitle());
            dialog_share.setPrice(bean.getPrice());
            dialog_share.setContent(bean.getWebViewCache(), bean.getPic(),
                    "http://www.meidebi.com/shaidan/" + bean.getId() + ".html");
        }

        return dialog_share;
    }


    public ViewBuyList getViewBuyList() {
        if (buylist == null) {
            if (bean.getInventorylist() != null) {
                buylist = new ViewBuyList(bean.getInventorylist(), this);
            }
        }
        return buylist;
    }

    /**
     * 改变intro视图
     */
    private void RefreshViewData() {
        if (!TextUtils.isEmpty(bean.getCover())) {
            if(!loadImage) {
                imageLoader.displayImage(bean.getCover(), mImageView,
                        AppConfigs.IMG_SMALLOPTIONS);
            }
            loadImage= true;
        } else {
            mImageView.setVisibility(View.GONE);
        }
        tv_siteName.setText(buildSiteString(bean.getSite(), "商城：") + "    "
                + buildSiteString(bean.getTransitlist(), "转运公司："));
        setViewData(bean);
    }

    private String buildSiteString(BaseItemBean siteList, String initString) {
        if (siteList != null) {
            StringBuffer buffer = new StringBuffer(initString);
            // for (int i = 0; i < siteList.size(); i++) {
            buffer.append(siteList.getName());
            // }
            tv_siteName.setVisibility(View.VISIBLE);
            return buffer.toString();
        } else {
            return "";
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
                OrderShowBean dbbean = Select
                        .from(OrderShowBean.class)
                        .where(new Condition[]{new Condition("ID")
                                .eq(detail_id)}).first();
                if (dbbean != null && dbbean.getWebViewCache() != null) {
                    message.obj = dbbean;
                    message.what = 1;
                    mHandler.sendMessage(message);
                } else {
                    OrderShowDetailJson bean = null;
                    bean = getDao().getOrdewShowDetail(detail_id);
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
                        bean = getDao().doVote(detail_id, type, "3");
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
                    bean = getDao().doFav(detail_id, "4");
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
            dao.setType(1);
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
        setTitle("晒单详情");
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
         getWebViewHelper().onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        getWebViewHelper().onPause();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // [end]

    private void initView() {
        tv_goto.setVisibility(View.GONE);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mImageView.getLayoutParams();
        params.height =AppConfigs.getCoverHeight();
        mImageView.setLayoutParams(params);
        scrollView.setScrollbarFadingEnabled(false);
         BuildViewData();

    }


    public WebViewHelper getWebViewHelper(){
        if(webViewHelper==null){
            webViewHelper = new WebViewHelper(OrderShowDetailActivity.this);
            webViewHelper.setInterFaceLoadWebview(this);
        }
        return webViewHelper;
    }




    // [end]

    // [start]继承方法
    @OnClick({R.id.btn_msg_detail_good,R.id.btn_msg_detail_fav,R.id.btn_msg_detail_goto_browser,R.id.btn_msg_detail_comment,R.id.adapter_image})
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.btn_msg_detail_comment) {
            IntentUtil.start_activity(
                    this,
                    CommentActivity.class,
                    new BasicNameValuePair("id", detail_id),
                    new BasicNameValuePair("linkType", AppAction.Comment_SHOW));

        } else if (bean != null) {
            switch (v.getId()) {
            case R.id.btn_msg_detail_fav:
                doFav(String.valueOf(bean.getLinktype()));
                break;
            case R.id.btn_msg_detail_good:
                doVote(String.valueOf(1));
                break;
            case R.id.btn_msg_detail_weak:
                doVote(String.valueOf(-1));
                break;
            case R.id.btn_msg_detail_goto_browser:
                if (getViewBuyList() != null) {
                    getViewBuyList().showActionWindow(mWebView);
                } else {
                    Toast.makeText(OrderShowDetailActivity.this, "暂无购物清单",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_msg_detail_comment:
                IntentUtil.start_activity(this, CommentActivity.class,
                        new BasicNameValuePair("id", detail_id),
                        new BasicNameValuePair("linkType",  AppAction.Comment_SHOW));
                break;
            case R.id.cover:
             if(!TextUtils.isEmpty(bean.getCover())) {
                            IntentUtil
                                    .start_activity_normal(
                                            OrderShowDetailActivity.this,
                                            BrowserImgActivity.class,
                                            new BasicNameValuePair("path", bean
                                                    .getCover()),
                                            new BasicNameValuePair("thumb", bean
                                                    .getImage()));
                        }

                break;
             default:
                break;
        }
        } else {
            Toast.makeText(OrderShowDetailActivity.this,"数据加载中，请稍等",Toast.LENGTH_SHORT).show();
        }
    }

    // [end]

    private void BuildViewData() {
        Bundle bundle = getIntent().getExtras();
//        String ex_content = bundle.getString(JPushInterface.EXTRA_EXTRA);
        baseBean = (MsgBaseBean) getIntent().getSerializableExtra("bean");
        if (baseBean != null) {
//            if (ex_content == null) {
                detail_id = baseBean.getId();
                setViewData(baseBean);
//            } else {
//                parseData(ex_content);
//            }
        } else {
            detail_id = getIntent().getStringExtra("id");
        }
        AppLogger.e("detail_id"+detail_id);

//        if(Build.VERSION.SDK_INT >= 21&&!AfterEnterTransition){
//                 getWindow().getEnterTransition().addListener(new TransitionAdapter() {
//
//
//                    @Override
//                    public void onTransitionEnd(Transition transition) {
//                        doGetDetail();
//                        getWindow().getEnterTransition().removeListener(this);
//                        AfterEnterTransition = true;
//                    }
//                });
//
//        }else{
            doGetDetail();
//         }

    }

    private void setViewData(MsgBaseBean bean) {
        tv_title.setText(bean.getTitle());
        if (!SharePrefUtility.getEnablePic()) {
            mImageView.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(bean.getCover())&&!loadImage) {
            imageLoader.displayImage(bean.getCover(), mImageView,
                    AppConfigs.IMG_SMALLOPTIONS);
        }

        tv_title.setText(bean.getTitle());
        if(!TextUtils.isEmpty(bean.getName())) {
            tv_nick.setText(bean.getName());
        }else{
            tv_nick.setText(bean.getShareName());
        }
        if (bean.getCreatetime() != 0) {
            tv_time.setText(TimeUtil.getListTime(bean.getCreatetime()));
        } else if (bean.getSharetime() != 0) {
            tv_time.setText(TimeUtil.getListTime(bean.getSharetime()));
        }
        tv_time.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(bean.getHeadphoto())) {
            imageLoader.displayImage(bean.getHeadphoto(), iv_write,
                    AppConfigs.AVATAR_OPTIONS);
        }else{
            AppLogger.e("bean.getSharephoto()"+bean.getSharephoto());
            imageLoader.displayImage(bean.getSharephoto(), iv_write,
                    AppConfigs.AVATAR_OPTIONS);
        }

        tv_good.setText(String.valueOf(bean.getVotesp()));
//        tv_weak.setText(String.valueOf(bean.getVotem()));
        if (bean.getUserReviewsData() != null) {
            tv_comment.setText(String.valueOf(bean.getUserReviewsData()
                    .getCommentlist().size()));
        } else {
            tv_comment.setText(String.valueOf(bean.getCommentcount()));
        }
        if (!TextUtils.isEmpty(bean.getHasVoteSp())
                && bean.getHasVoteSp().equals(userid)) {
            tv_good.setSelected(true);
        }
        tv_fav.setText(bean.getFavnum()+"");
//        else if (!TextUtils.isEmpty(bean.getHasVoteEm())
//                && bean.getHasVoteEm().equals(userid)) {
//            tv_weak.setSelected(true);
//        }
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
        AppLogger.e("onCreateOptionsMenu");
        mMenu = menu;
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                getDialogSharePlatfrom().show();
                // getSharePlatfrom().showActionWindow(mWebView);
                return true;
//            case R.id.menu_unfav:
//                doFav(String.valueOf(bean.getLinktype()));
//                return true;
//            case R.id.menu_faved:
//                doFav(String.valueOf(bean.getLinktype()));
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void setFavSelected(final boolean isfav) {
        tv_fav.setSelected(isfav);

    }

//    private void FavSelected(boolean isfav) {
//        if (isfav) {
//
//        } else {
//
//        }
//    }

    public void parse() {
        if(bean.getDevicetype()==2) {
            // <img src=\"http:\/\/s.meidebi.com\/showdan_201503_54fa61f6bfdafowtysu.jpg-detail1\" alt=\"\" class=\"upic\" \/>
        }
        Parser mParser = new Parser(null, mWebView, this) {
			@Override
			public String downloadHtml() {
				String html = "";
				html = bean.getWebViewCache();

				return html;
			}
		};
		getWebViewHelper().execute(mWebView, mParser);
     }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_order_show_detail;
    }

    @Override
    public void onBackPressed() {
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
        scrollView.setScrollbarFadingEnabled(true);
    }

    @Override
    public void loadWebViewFailed() {
        getView_load().onFaied();

    }

    @Override
    public void SwipeLeft() {
        IntentUtil.start_activity(this, CommentActivity.class,
                new BasicNameValuePair("id", detail_id),
                new BasicNameValuePair("linkType",  AppAction.Comment_SHOW));
     }
}
