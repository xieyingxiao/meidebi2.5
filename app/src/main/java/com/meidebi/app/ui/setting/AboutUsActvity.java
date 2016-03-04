package com.meidebi.app.ui.setting;

import org.apache.http.message.BasicNameValuePair;

import com.meidebi.app.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.meidebi.app.support.component.UmengUtil;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;
import com.meidebi.app.ui.browser.BrowserWebActivity;

public class AboutUsActvity extends BasePullToRefreshActivity implements OnClickListener{
	private RelativeLayout layout_update,layout_about_disclaimer,layout_about_legalnotice,layout_about_privacypolicy;
	private UmengUtil umengUtil;
	public UmengUtil getUmengUtil() {
		if(umengUtil==null){
			umengUtil = new UmengUtil(this);
		}
		return umengUtil;
	}
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_about_us);
		setActionBar("关于我们");
//		initTitle();
//		setTitleText(getString(R.string.menu_about));
		initView();

	}

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_about_us;
    }

    private void initView() {
		// TODO Auto-generated method stub
		layout_update = (RelativeLayout)findViewById(R.id.version_update);
		layout_update.setOnClickListener(this);
//		layout_about_disclaimer = (RelativeLayout)findViewById(R.id.about_disclaimer);
//		layout_about_disclaimer.setOnClickListener(this);
		layout_about_legalnotice = (RelativeLayout)findViewById(R.id.about_legalnotice);
		layout_about_legalnotice.setOnClickListener(this);
		layout_about_privacypolicy = (RelativeLayout)findViewById(R.id.about_privacypolicy);
		layout_about_privacypolicy.setOnClickListener(this);
		LinearLayout logoview =  (LinearLayout)findViewById(R.id.layout_about_logo_view);
		Animation animation=AnimationUtils.loadAnimation(this, R.anim.alpha);
		logoview.startAnimation(animation);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.version_update:
			getUmengUtil().checkUpdate();
			break;
//		case R.id.about_disclaimer:
//			break;
		case R.id.about_legalnotice:
        	IntentUtil.jumpToBroswerActivity(this, BrowserWebActivity.class,  new BasicNameValuePair(
                    "url", getString(R.string.url_about_usernotice)), new BasicNameValuePair(
                            "title", getString(R.string.about_usernotice)));   
//			IntentUtil.jumpToBroswer(getString(R.string.url_about_usernotice), this);
			break;
		case R.id.about_privacypolicy:
        	IntentUtil.jumpToBroswerActivity(this, BrowserWebActivity.class,  new BasicNameValuePair(
                    "url", getString(R.string.url_about_privacypolicy)), new BasicNameValuePair(
                            "title", getString(R.string.about_privacypolicy)));   
//			IntentUtil.jumpToBroswer(getString(R.string.url_about_privacypolicy), this);
			break;
		default:
			break;
		}
	}

	
	
}
