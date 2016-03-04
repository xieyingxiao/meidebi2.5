package com.meidebi.app.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.service.bean.user.AccountBean;
import com.meidebi.app.service.bean.user.LoginJson;
import com.meidebi.app.service.dao.user.LoginDao;
import com.meidebi.app.service.dao.user.SocailLoginDao;
import com.meidebi.app.service.dao.user.SocailPlatform;
import com.meidebi.app.support.component.bus.LoginSucessEvent;
import com.meidebi.app.support.component.bus.MainThreadBusProvider;
import com.meidebi.app.support.component.lbs.IAuthListener;
import com.meidebi.app.support.component.lbs.OauthUtil;
import com.meidebi.app.support.component.lbs.SocialConstants;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;
import com.meidebi.app.ui.browser.BrowserWebActivity;
import com.meidebi.app.ui.view.CleanableEditText;
import com.orm.query.Select;
import com.taobao.top.android.auth.AuthError;
import com.taobao.top.android.auth.AuthException;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

import org.apache.http.message.BasicNameValuePair;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends BasePullToRefreshActivity implements IAuthListener {


    @InjectView(R.id.login_et_name)
    CleanableEditText _etName;
    @InjectView(R.id.login_et_pwd)
    CleanableEditText _etPwd;


//    View[] views ;

    private OauthUtil oauthUtil;
    private LoginDao loginDao;
    private SocailLoginDao oauthDao;
    private final int loginsucess = 1;
    private final int needautoreg = 3;
    private final int bindemail = 2;
    private final int loginfailed = 0;
    private final int networkerr = 4;

    private String headerImageUrl = null;
    private String userName = null;
    private String uid;
    private String token;
    private SocailPlatform method;

    /**
     * 数据回调处理
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case loginsucess:// 登陆成功
                    dissmissDialog();
                    LoginJson bean = (LoginJson) msg.obj;
                    String username = (TextUtils.isEmpty(bean.getUser().getUsername())) ? bean.getUser().getNickname() : bean.getUser().getUsername();
                    if (TextUtils.isEmpty(username)) {
                        username = userName;
                    }
                    if (TextUtils.isEmpty(bean.getUser().getPhoto())) {
                        bean.getUser().setPhoto(headerImageUrl);
                    }
                    LoginUtil.saveAccount(bean.getUser().getId(), username, bean.getUser().getPhoto(), bean.getData());
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.login_sucess), Toast.LENGTH_SHORT)
                            .show();
                    setResult(RESULT_OK);
                    MainThreadBusProvider.getInstance().post(new LoginSucessEvent());
                    finish();
                    break;
                case needautoreg:
                    oauthUtil.sendInfo((SocailPlatform) msg.obj);
                    break;
                case loginfailed:
                    dissmissDialog();
                    LoginJson bean2 = (LoginJson) msg.obj;
                    showErr(bean2.getInfo());
                    break;
                case bindemail:
                    dissmissDialog();
                    final LoginJson bean3 = (LoginJson) msg.obj;
                    new MaterialDialog.Builder(LoginActivity.this).title(R.string.reg_sucess).content(R.string.reg_activite_tip).cancelable(false)
                            .positiveText(R.string.reg_activite_positve)
                            .negativeText(R.string.reg_activite_negtive)
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    super.onPositive(dialog);
                                    finish();
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    super.onNegative(dialog);
                                    IntentUtil.start_activity(LoginActivity.this,
                                            BrowserWebActivity.class, new BasicNameValuePair("url", bean3.getData()), new BasicNameValuePair("title", "激活邮箱"));

                                }
                            })
                            .build().show();


                    break;
                case networkerr:
                    dissmissDialog();
                    showErr(R.string.timeout);
                    break;
                default:// 失败
                    dissmissDialog();
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.login_failed), Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
        }

        ;

    };

    /**
     * 登录获取数据
     */
    private void doLogin(final String name, final String pw) {
        showLoading(getString(R.string.hint_logining));
        new Thread() {
            @Override
            public void run() {
                if (loginDao == null) {
                    loginDao = new LoginDao();
                }
                loginDao.setName(name);
                loginDao.setPw(pw);
                loginDao.mapperJson();
                LoginJson bean = loginDao.mapperJson();
                Message message = new Message();
                XApplication.getInstance().getAccountBean().setLogintime(System.currentTimeMillis());
                if (bean != null) {
                    if (bean.getStatus() == 1) {
                        message.obj = bean;
                        message.what = bean.getStatus();
                        mHandler.sendMessage(message);
                    } else if (bean.getStatus() == bindemail) {
                        message.obj = bean;
                        message.what = bean.getStatus();
                        mHandler.sendMessage(message);
                    } else {
                        message.obj = bean;
                        message.what = loginfailed;
                        mHandler.sendMessage(message);
                    }
                } else {
                    message.what = NETERR;
                    mHandler.sendMessage(message);
                }
            }
        }.start();
    }

    /**
     * 登录获取数据
     */
    private void doOauthLogin() {
        showLoading(getString(R.string.hint_oatusuceess));
        new Thread() {
            public void run() {
                if (oauthDao == null) {
                    oauthDao = new SocailLoginDao();
                }
                oauthDao.setSid(uid);
                oauthDao.setNickname(userName);
                oauthDao.setToken(token);
                oauthDao.setHeaderImage(headerImageUrl);
                LoginJson json = oauthDao.OauthLogin(method);
                Message message = new Message();
                if (json != null) {
                    if (json.getStatus() == 1) {
                        message.obj = json;
                        message.what = 1;
                        mHandler.sendMessage(message);
                    } else {
                        message.what = needautoreg;
                        message.obj = method;
                        mHandler.sendMessage(message);
                    }
                } else {
                    message.what = NETERR;
                    mHandler.sendMessage(message);
                }
            }
        }.start();
    }

    /**
     * 自动注册
     */
    private void doAutoReg(final String uid, final String token,
                           final String nickname, final SocailPlatform method) {
        showLoading(getString(R.string.hint_reging));
        new Thread() {
            public void run() {
                if (oauthDao == null) {
                    oauthDao = new SocailLoginDao();
                }
                oauthDao.setSid(uid);
                oauthDao.setToken(token);
                oauthDao.setNickname(nickname);
                LoginJson bean = oauthDao.AutoReg(method);
                Message message = new Message();
                if (bean != null) {
                    if (bean.getStatus() == 1) {
                        message.obj = bean;
                        message.what = 1;
                        mHandler.sendMessage(message);
                    }
                } else {
                    message.what = NETERR;
                    mHandler.sendMessage(message);
                }
            }
        }.start();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar("登录");
        // initTitle();
        // setTitleText(getString(R.string.menu_login));
        initView();
        initOauthUtil();
    }

    /**
     * 第三方登录
     */
    public void initOauthUtil() {
        if (oauthUtil == null) {
            oauthUtil = new OauthUtil(this);
            oauthUtil.setAuthlistener(this);
            oauthUtil.initTbclient();// 淘宝单独
        }
    }

    /**
     * 绑定ui控件
     */
    private void initView() {
        ButterKnife.inject(this);
        _etPwd.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        AccountBean bean = Select.from(AccountBean.class).orderBy("LOGINTIME DESC").first();
        if (bean != null && bean.getLogintime() > 0) {
            _etName.setText(bean.getUsername());
            _etPwd.requestFocus();
        }


    }

    @OnClick({R.id.login_btn_login, R.id.login_btn_qq, R.id.login_btn_sina, R.id.tv_forger_ps})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_login:
                this.btnLoginClick();
                break;
            case R.id.login_btn_regist:
                this.btnRegistClick();
                break;
            case R.id.login_btn_qq:
                oauthUtil.doQQOauth();
                break;
            case R.id.login_btn_sina:
                oauthUtil.doSinaOauth();
                break;
            case R.id.tv_forger_ps:
                IntentUtil.start_activity(LoginActivity.this, LoginForgetActivity.class);
                break;
//            case R.id.login_btn_taobao:
//                oauthUtil.doTBOauth();
//                break;
        }

    }

    /**
     * 登陆按钮点击
     */
    void btnLoginClick() {
        // 非空验证
        if (TextUtils.isEmpty(_etName.getText())) {
            _etName.setError(getString(R.string.login_name_isempty));

            return;
        } else if (TextUtils.isEmpty(_etPwd.getText())) {
            _etPwd.setError(getString(R.string.login_pwd_isempty));

            return;
        } else if (_etPwd.getText().length() < 6) {
            _etPwd.setError(getString(R.string.login_pwd_is_too_sort));

            return;
        }
        // 获取用户名
        String name = _etName.getText().toString();
        // 获取密码
        String pwd = _etPwd.getText().toString();
        doLogin(name, pwd);

    }

    /**
     * 注册按钮点击
     */
    void btnRegistClick() {
        Intent intent = new Intent(this, RegisitActivity.class);
        startActivity(intent);
    }

    @Override
    public void onInfoComplete(String accessToken, String uid, String nickname,
                               SocailPlatform method) {
        this.doAutoReg(uid, accessToken, nickname, method);
    }

    @Override
    /**
     * auth成功认证回调
     */
    public void onIdComplete(String uid, String name, String imageUrl, String token, SocailPlatform method) {
        this.headerImageUrl = imageUrl;
        this.uid = uid;
        this.token = token;
        this.method = method;
        this.userName = name;
        if (TextUtils.isEmpty(this.userName)) {
            oauthUtil.getUserInfo();
        } else {
            doOauthLogin();
        }
    }

    public void onLoadUserComplete(String userName, String headerImageUrl) {
        this.userName = userName;
        this.headerImageUrl = headerImageUrl;
        doOauthLogin();
    }

    /**
     * auth失败认证回调
     */
    @Override
    public void onError(AuthError e) {
        // TODO Auto-generated method stub
        showErr(R.string.hint_oatufailed);
    }

    /**
     * auth异常认证回调
     */
    @Override
    public void onAuthException(AuthException e) {
        // TODO Auto-generated method stub
        dissmissDialog();
        showErr("授权失败");
    }

    /**
     * 用户中断操作
     */
    @Override
    public void onCancel() {
        // TODO Auto-generated method stub
        dissmissDialog();
        // showErr("授权失败");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {// sso跳转回来
        super.onActivityResult(requestCode, resultCode, data);
        UMSocialService controller = UMServiceFactory.getUMSocialService(
                SocialConstants.DESCRIPTOR, RequestType.SOCIAL);
        // 根据requestCode获取对应的SsoHandler
        UMSsoHandler ssoHandler = controller.getConfig().getSsoHandler(
                requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reg:
                IntentUtil
                        .start_activity(LoginActivity.this, RegisitActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //
    @Override
    protected int getLayoutResource() {
        return R.layout.activtiy_login;
    }


    @Override
    public void onResume() {
        super.onResume();
        MainThreadBusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MainThreadBusProvider.getInstance().unregister(this);
    }

}
