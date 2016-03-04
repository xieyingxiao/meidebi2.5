package com.meidebi.app.ui.main;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.user.UserinfoBean;
import com.meidebi.app.service.dao.ShowOrderDao;
import com.meidebi.app.service.dao.user.UserCenterDao;
import com.meidebi.app.support.component.UmengUtil;
import com.meidebi.app.support.component.bus.MainThreadBusProvider;
import com.meidebi.app.support.component.bus.UserInfoRefreshEvent;
import com.meidebi.app.support.component.upush.UpushUtity;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.base.BaseFragmentActivity;
import com.meidebi.app.ui.widget.DialogPush;
import com.orm.SugarRecord;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mdb-ii on 15-3-5.
 */
public class MainTabHostActivity extends BaseFragmentActivity {

    private FragmentTabHost mTabHost;
    public View msg_num3;
    // 定义数组来存放Fragment界面
    private Class mFragments[] = {MainVpFragment.class, OrderShowListFragment.class, CouponListFragment.class, UserCenterFragment.class};

    // 定义数组来存放按钮图片
    private int mTabDrawables[] = {R.drawable.ic_tab_youhui, R.drawable.ic_tab_ordershow, R.drawable.ic_tab_coupon, R.drawable.ic_tab_mine};

    // Tab选项卡的文字
    private int mTabTitles[] = {R.string.tab_main, R.string.tab_show, R.string.tab_coupon, R.string.tab_me};
//    public static final String[] ALLOWED_ACTIONS = {AppAction.CALLLOGINED};

    private UmengUtil umengUtil;

    public UmengUtil getUmengUtil() {
        if (umengUtil == null) {
            umengUtil = new UmengUtil(this);
        }
        return umengUtil;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabhost);
        XApplication.getInstance().setActivity(this);
        ShowOrderDao.uploadUnsendPhoto(this);

        UpushUtity.OnStart(this);
        getUmengUtil().onStart();
        if (SharePrefUtility.firshSetPush()) {
            new DialogPush().getPushDialog(this).show();
        }
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);

        for (int i = 0; i < mFragments.length; i++) {
            Bundle b = new Bundle();
            b.putString("key", "Simple" + i);
            mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(i)).setIndicator(getView(i)), mFragments[i], b);
        }

        mTabHost.setCurrentTab(0);

    }

    private View getView(int i) {

        View view = View.inflate(this, R.layout.tabspec_item, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textView = (TextView) view.findViewById(R.id.text);
        switch (i) {
            case 3:
                msg_num3 = view.findViewById(R.id.iv_msg_num);
                break;
        }
        imageView.setImageResource(mTabDrawables[i]);

        textView.setText(getResources().getText(mTabTitles[i]));

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        MainThreadBusProvider.getInstance().register(this);
        if (LoginUtil.isAccountLogin()) {
            UserCenterDao.getUserInfo(new RestHttpUtils.RestHttpHandler<CommonJson>() {
                @Override
                public void onSuccess(CommonJson result) {
                    if (result.getStatus() == 1) {
                        UserinfoBean userbean = ((UserinfoBean) result.getData());
                        XApplication.getInstance().getAccountBean()
                                .setMsgNum(userbean.getMessagenum());
                        userbean.setId(userbean.getUserid());
                        SugarRecord.save(userbean);
                        MainThreadBusProvider.getInstance().post(new UserInfoRefreshEvent(userbean));

                    }
                    if (XApplication.getInstance().getAccountBean().getMsgNum() > 0) {
                        msg_num3.setVisibility(View.VISIBLE);
                    } else {
                        msg_num3.setVisibility(View.GONE);
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

    }

    @Override
    protected int getLayoutResource() {
        return 0;
    }


    @Override
    public void onPause() {
        super.onPause();
        MainThreadBusProvider.getInstance().unregister(this);
    }

    private int keyBackClickCount = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (keyBackClickCount++) {
                case 0:
                    Toast.makeText(
                            this,
                            getResources().getString(R.string.press_again_exit),
                            Toast.LENGTH_SHORT).show();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            keyBackClickCount = 0;
                        }
                    }, 3000);
                    break;
                case 1:
                    XApplication.getInstance().startedApp = false;
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                    break;
                default:
                    break;
            }
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }


}
