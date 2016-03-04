package com.meidebi.app.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.AppAction;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.user.UserinfoBean;
import com.meidebi.app.service.dao.user.UserCenterDao;
import com.meidebi.app.support.component.upush.UpushUtity;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.base.BaseFragmentActivity;
import com.meidebi.app.ui.widget.DialogPush;
import com.orm.SugarRecord;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by mdb-ii on 15-1-5.
 */
public class MainTabActivity extends BaseFragmentActivity{

    @InjectView(R.id.tab_main)
    RadioButton tab_main;
    @InjectView(R.id.tab_show)
    RadioButton tab_show;
    @InjectView(R.id.tab_coupon)
    RadioButton tab_coupon;
    @InjectView(R.id.tab_me)
    RadioButton tab_me;
    public @InjectView(R.id.iv_msg_num)
    ImageView iv_msg_num;
    private RadioButton[] rbs ;
    public static final String[] ALLOWED_ACTIONS = {AppAction.CALLLOGINED};

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            AppLogger.e("getbroadcast"+action);

            if (AppAction.CALLLOGINED.equals(action)){
                if(userCenterFragment!=null){
                    getUserCenterFragment().logined();
                }

            }


        }
    };




    @Override
    public void onResume() {
        super.onResume();
         if(userCenterFragment!=null&& LoginUtil.isAccountLogin()){
             getUserCenterFragment().getUserInfo();
         }else if(LoginUtil.isAccountLogin()){
                  UserCenterDao.getUserInfo(new RestHttpUtils.RestHttpHandler<CommonJson>() {
                     @Override
                     public void onSuccess(CommonJson result) {
                         UserinfoBean userbean = ((UserinfoBean) result.getData());
                         XApplication.getInstance().getAccountBean()
                                 .setMsgNum(userbean.getMessagenum());
                         userbean.setId(userbean.getUserid());
                          SugarRecord.save(userbean);

                         if(XApplication.getInstance().getAccountBean().getMsgNum()>0){
                             iv_msg_num.setVisibility(View.VISIBLE);
                         }else{
                             iv_msg_num.setVisibility(View.GONE);
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
    public void onDestroy() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected int getLayoutResource() {
        return 0;
    }


    private UserCenterFragment userCenterFragment;
    private CouponListFragment couponListFragment;
    private OrderShowListFragment orderShowListFragment;
     private MainVpFragment channelFragment;

    public MainVpFragment getChannelFragment() {
        if(channelFragment ==null){
            channelFragment = new MainVpFragment("0");
        }
        return channelFragment;
    }

    public OrderShowListFragment getOrderShowListFragment() {
        if(orderShowListFragment ==null){
            orderShowListFragment = new OrderShowListFragment();
        }
        return orderShowListFragment;
    }

    public CouponListFragment getCouponListFragment() {
        if(couponListFragment ==null){
            couponListFragment = new CouponListFragment();
        }
        return couponListFragment;
    }

    public UserCenterFragment getUserCenterFragment() {
        if(userCenterFragment ==null){
            userCenterFragment = new UserCenterFragment();
        }
        return userCenterFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        UpushUtity.OnStart(this);
        ButterKnife.inject(this);
        if(SharePrefUtility.firshSetPush()) {
            new DialogPush().getPushDialog(this).show();
        }
        rbs =new RadioButton[] {tab_main,tab_show,tab_coupon,tab_me};
        if (savedInstanceState == null) {
            addFragment(getChannelFragment());
            mContent=getChannelFragment();
        }
        regBroadcast();

    }


    private void regBroadcast(){
        IntentFilter iFilter = new IntentFilter();
        for (String action : ALLOWED_ACTIONS) {
            iFilter.addAction(action);
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,iFilter);
    }

    @OnClick({R.id.tab_main,R.id.tab_show,R.id.tab_coupon,R.id.tab_me})
    protected void OnClick(View view){
        selectTab(view.getId());
         switch (view.getId()){
            case R.id.tab_main:
                switchContent(mContent, getChannelFragment(), R.id.common_fragment);
                break;
            case R.id.tab_show:
                switchContent(mContent, getOrderShowListFragment(), R.id.common_fragment);
                break;
            case R.id.tab_coupon:
                switchContent(mContent, getCouponListFragment(), R.id.common_fragment);

                break;
            case R.id.tab_me:
                switchContent(mContent, getUserCenterFragment(), R.id.common_fragment);
                break;

        }
    }


    private void selectTab(int resid){
        for (int i=0;i<rbs.length;i++){
            if(rbs[i].getId()==resid){
                rbs[i].setChecked(true);
            }else{
                rbs[i].setChecked(false);
            }
        }
    }




}
