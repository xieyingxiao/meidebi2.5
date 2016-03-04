package com.meidebi.app.ui.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.meidebi.app.R;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.dao.user.RegDao;
import com.meidebi.app.support.lib.URLSpan;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;
import com.meidebi.app.ui.browser.BrowserWebActivity;

import org.apache.http.message.BasicNameValuePair;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.hoang8f.widget.FButton;

public class RegisitActivity extends BasePullToRefreshActivity implements OnClickListener {



	@InjectView(R.id.regist_btn_regist) FButton _btnRegist; // 注册按钮
    @InjectView(R.id.regist_et_name) EditText _etName; // 用户名
    @InjectView(R.id.regist_et_pwd) EditText _etPwd; // 密码
    @InjectView(R.id.regist_et_pwd_again) EditText _etPwdAgain;// 重复密码
    @InjectView(R.id.regist_et_email) EditText _etMail; // mail
    @InjectView(R.id.reg_deagree) TextView deagee; // mail

    private RegDao dao;

	public RegDao getDao() {
		if (dao == null) {
			dao = new RegDao(this);
		}
		return dao;
	}

	public void setDao(RegDao dao) {
		this.dao = dao;
	}

	private Handler mHandler = new Handler() {
		@SuppressLint("ValidFragment")
		public void handleMessage(Message msg) {
			dissmissDialog();
			switch (msg.what) {
			case 1:// 刷新
				final CommonJson json =(CommonJson) msg.obj;
                new MaterialDialog.Builder(RegisitActivity.this).title(R.string.reg_sucess).content(R.string.reg_activite_tip)                  .cancelable(false)
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
                                IntentUtil.start_activity(RegisitActivity.this,BrowserWebActivity.class,new BasicNameValuePair("url",(String)json.getData()),new BasicNameValuePair("title","激活邮箱"));

                            }
                        })
                        .build().show();
//				finish();
				break;
			case DATAERR:
				CommonJson json2 =(CommonJson) msg.obj;
				showErr(json2.getInfo());
				break;
			case NETERR:
				showErr(R.string.timeout);
				break;
			}
		};

	};

	private void doReg(final String name, final String pw, final String email) {
		showLoading(R.string.hint_reging);
		new Thread() {
			public void run() {
				CommonJson<String> json = getDao().doReg(name, pw, email);
				Message message = new Message();

				if (json != null) {
					message.obj = json;
					if (json.getStatus() == 1) {
						message.what = 1;
					} else {
						message.what = DATAERR;
					}
				} else {
					message.what = NETERR;
				}
				mHandler.sendMessage(message);
			}
		}.start();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
 		setActionBar("注册");
        ButterKnife.inject(this);
		String deagree_str = getString(R.string.reg_deagree);
		SpannableString spStr = new SpannableString(deagree_str);
		URLSpan urlSpan = new URLSpan(getString(R.string.url_reg_deagree));
		spStr.setSpan(urlSpan, 0, deagree_str.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		deagee.setText(spStr);
		deagee.setMovementMethod(LinkMovementMethod.getInstance());

        _etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        _etPwdAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.acvivity_regist;
    }

    @OnClick(R.id.regist_btn_regist)
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.regist_btn_regist:
			this.btnRegistOnClick();
			break;

		default:
			break;
		}
	}

	/**
	 * 注册按钮点击
	 */
	void btnRegistOnClick() {
		if (TextUtils.isEmpty(_etName.getText())) {
			_etName.setError(getString(R.string.login_name_isempty));
			// Toast.makeText(this, getString(R.string.login_name_isempty),
			// Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(_etPwd.getText())) {
			_etPwd.setError(getString(R.string.login_pwd_isempty));
			// Toast.makeText(this, getString(R.string.login_pwd_isempty),
			// Toast.LENGTH_SHORT).show();
			return;
		}
		if (_etPwd.getText().length() < 6) {
			_etPwd.setError(getString(R.string.login_pwd_is_too_sort));
			// Toast.makeText(this, getString(R.string.login_pwd_is_too_sort),
			// Toast.LENGTH_SHORT).show();
			return;
		} else if (!_etPwd.getText().toString()
				.equals(_etPwdAgain.getText().toString())) {
			_etPwdAgain
					.setError(getString(R.string.regist_pwd_again_isnot_same));
			return;
		}
		if (TextUtils.isEmpty(_etMail.getText())) {
			_etMail.setError(getString(R.string.regist_eamil_isempty));
			// Toast.makeText(this, getString(R.string.regist_eamil_isempty),
			// Toast.LENGTH_SHORT).show();
			return;
		}
		String name = _etName.getText().toString();
		String pwd = _etPwd.getText().toString();
		String eamil = _etMail.getText().toString();
		doReg(name, pwd, eamil);
	}
}
