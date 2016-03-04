package com.meidebi.app.ui.submit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.meidebi.app.R;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.user.postBean;
import com.meidebi.app.service.dao.PostDao;
import com.meidebi.app.support.utils.Utility;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.ui.view.CleanableEditText;

import java.util.regex.Matcher;
@SuppressLint("ValidFragment")
class fragment_post_news extends Fragment {
	private CleanableEditText et_url;
	private CleanableEditText et_reason;
	private String str_url, str_reason;
	private CheckBox cb_anonymous;
	private final int NETERR = 404;
	private final int DATAERR = 400;
	private PostDao dao;

	public fragment_post_news() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View contentView = inflater.inflate(R.layout.fragment_post_news, null);
 
		initViews(contentView);
		handlerIntent();
		return contentView;
	}

	private void initViews(View view) {
		et_url = (CleanableEditText) view.findViewById(R.id.et_post_url);
		et_reason = (CleanableEditText) view.findViewById(R.id.et_post_reason);
		cb_anonymous = (CheckBox) view.findViewById(R.id.cb_anonymous);
		view.findViewById(R.id.tv_jump_to_guide).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				IntentUtil.start_activity(getActivity(), post_guide_activity.class);
			}
		});
		if (LoginUtil.isAccountLogin()) {
			cb_anonymous.setVisibility(View.GONE);
		} else {
			cb_anonymous.setVisibility(View.VISIBLE);
		}
	}

	public void clearEditText() {
		et_url.setText("");
		et_reason.setText("");
	}

	private void handlerIntent() {
		Intent intent = getActivity().getIntent();
		// 获得Intent的Action
		String action = intent.getAction();
		// 获得Intent的MIME type
		String type = intent.getType();

		if (Intent.ACTION_SEND.equals(action) && type != null) {
			// 我们这里处理所有的文本类型
			if (type.startsWith("text/")) {
				// 处理获取到的文本，这里我们用TextView显示
				handleSendText(intent);
			}
		}
	}

	private void handleSendText(Intent intent) {
		// TODO Auto-generated method stub
		String ori_content = getActivity().getIntent().getStringExtra(
				Intent.EXTRA_TEXT);
		Matcher m = AppConfigs.WEB_URL.matcher(ori_content);
		if (m.find()) {
			et_url.setText(m.group(0));
		}
		// et_url.setText(getIntent().getStringExtra(Intent.EXTRA_TEXT));
	}

	public void submit() {
		if (btnSendClick()) {
			if (!cb_anonymous.isChecked()) {
				if (LoginUtil.isAccountLogin(getActivity())) {
					doPostJson(str_url,str_reason);
				}
			} else {
				doPostJson(str_url,str_reason);

			}
		}
	}

	/**
	 * 登陆按钮点击
	 */
	boolean btnSendClick() {
		str_url = et_url.getText().toString();
		str_reason = et_reason.getText().toString();
		if (TextUtils.isEmpty(str_url)) {
			et_url.setError(getString(R.string.err_url_empty));
			return false;
		} else if (!Utility.isLink(str_url)) {
			et_url.setError(getString(R.string.err_url_unleagal));
			return false;
		} else if (TextUtils.isEmpty(str_reason)) {
			et_reason.setError(getString(R.string.err_reason_empty));
			return false;
		}
		return true;
	}
	
	public void doPostJson(final String url, final String description) {
		((PostNewsActivity)getActivity()).showLoading("正在提交");
		new Thread() {
			@Override
			public void run() {
				CommonJson<postBean> json = getDao().post(url, description);
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

	private Handler mHandler = new Handler() {
		@SuppressLint("ValidFragment")
		public void handleMessage(Message msg) {
			((PostNewsActivity)getActivity()).dissmissDialog();
			switch (msg.what) {
			case 1:// 刷新
                CommonJson<postBean> json =(CommonJson<postBean>) msg.obj;
				if(cb_anonymous.isChecked()){
					((PostNewsActivity) getActivity()).jumpToSucess("","");
				}else{
//				((PostNewsActivity) getActivity()).jumpToSucess(String.valueOf(
//						json.getData().getData().get(0).getRule()),String.valueOf(json.getData().getData().get(1).getRule()));
				}
				break;
			case DATAERR:
				CommonJson<postBean> json2 =(CommonJson<postBean>) msg.obj;
				((PostNewsActivity)getActivity()).showErr(json2.getInfo());
				break;
			case NETERR:
				((PostNewsActivity)getActivity()).showErr(R.string.timeout);
				break;
			}
		};

	};
	
	public PostDao getDao() {
		if (dao == null) {
			dao = new PostDao();
		}
		return dao;
	}
	
 

}
