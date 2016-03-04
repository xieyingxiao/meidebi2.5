package com.meidebi.app.ui.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.dao.user.LoginDao;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.ViewUtils;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;
import com.meidebi.app.ui.browser.BrowserWebActivity;

import org.apache.http.message.BasicNameValuePair;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import info.hoang8f.widget.FButton;

/**
 * Created by mdb-ii on 15-3-7.
 */
public class LoginForgetActivity extends BasePullToRefreshActivity{
    @InjectView(R.id.login_forget_et_email)
    EditText et_emali;
    @InjectView(R.id.login_forget_send_email)
    FButton btn_emali;
    @InjectView(R.id.tv_forget_email)
    TextView tv_forget_email;

     @InjectViews({R.id.tv_forget_sucess1,R.id.tv_forget_email,R.id.tv_forget_sucess2,R.id.login_forget_jump_browser})
    List<View> sucessViews;
    CommonJson email_last;
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_forget_password;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar("找回密码");
        ButterKnife.inject(this);
        unSend();
        // initTitle();
        // setTitleText(getString(R.string.menu_login));
      }
    @OnClick({R.id.login_forget_send_email,R.id.login_forget_jump_browser})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_forget_send_email:
                btnSendClick();
                break;
            case R.id.login_forget_jump_browser:
                AppLogger.e((String)email_last.getData());
                IntentUtil.start_activity(LoginForgetActivity.this,
                        BrowserWebActivity.class,new BasicNameValuePair("url",(String)email_last.getData()),new BasicNameValuePair("title","激活邮箱"));
                 break;
        }
    }

    /**
     * 登陆按钮点击
     */
    void btnSendClick() {
        // 非空验证
        if (TextUtils.isEmpty(et_emali.getText())) {
            et_emali.setError(getString(R.string.login_forget_no_empty));

            return;
        } else if (!isEmail(et_emali.getText().toString())) {
            et_emali.setError(getString(R.string.login_forget_format_err));

            return;
        }
       final String email = et_emali.getText().toString();
        LoginDao.findPassWord(email, new RestHttpUtils.RestHttpHandler<CommonJson>() {
            @Override
            public void onSuccess(CommonJson result) {
                if(result.getStatus()==1){
                    email_last =result;
                    tv_forget_email.setText(email);
                    sendSucees();
                }else{
                    showErr(result.getInfo());
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

    //判断email格式是否正确
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    private void sendSucees(){
        ButterKnife.apply(sucessViews,ViewUtils.VISIBLE);
        et_emali.setVisibility(View.GONE);
        btn_emali.setText("再次发送");
    }


    private void unSend(){
        ButterKnife.apply(sucessViews,ViewUtils.GONE);
        et_emali.setVisibility(View.VISIBLE);
        btn_emali.setText(getString(R.string.login_forget_send_email));
    }

}
