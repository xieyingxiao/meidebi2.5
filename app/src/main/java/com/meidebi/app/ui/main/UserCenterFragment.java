package com.meidebi.app.ui.main;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.loopj.android.http.TextHttpResponseHandler;
import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.AppAction;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.user.AwardBean;
import com.meidebi.app.service.bean.user.UserinfoBean;
import com.meidebi.app.service.dao.user.UserCenterDao;
import com.meidebi.app.support.component.bus.LoginSucessEvent;
import com.meidebi.app.support.component.bus.MainThreadBusProvider;
import com.meidebi.app.support.component.bus.UserInfoRefreshEvent;
import com.meidebi.app.support.lib.MyAsyncTask;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.ViewUtils;
import com.meidebi.app.support.utils.anim.AnimateFirstDisplayListener;
import com.meidebi.app.support.utils.component.CleanCacheWebView;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.support.utils.content.CommonUtil;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.base.BaseFragmentActivity;
import com.meidebi.app.ui.commonactivity.CommonFragmentActivity;
import com.meidebi.app.ui.setting.SettingActivity;
import com.meidebi.app.ui.user.MyCouponVpFragment;
import com.meidebi.app.ui.user.MyFavVpFragment;
import com.meidebi.app.ui.user.MyShowFragment;
import com.meidebi.app.ui.user.UserMsgListFragment;
import com.meidebi.app.ui.user.UserNewsFragment;
import com.meidebi.app.ui.view.preferenfragment.PreferenceFragment;
import com.meidebi.app.ui.view.roundedview.RoundedImageView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.orm.SugarRecord;
import com.orm.query.Select;
import com.squareup.otto.Subscribe;

import org.apache.http.Header;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import info.hoang8f.widget.FButton;

/**
 * Created by mdb-ii on 15-1-5.
 */
public class UserCenterFragment extends PreferenceFragment implements
        Preference.OnPreferenceClickListener, MaterialDialog.ListCallback {
    @InjectView(R.id.iv_user_avatar)
    RoundedImageView iv_avatar;
    @InjectView(R.id.tv_user_name)
    TextView tv_user_name;
    @InjectView(R.id.tv_user_score)
    TextView tv_user_score;
    @InjectView(R.id.tv_user_lv)
    TextView tv_user_lv;
    @InjectView(R.id.tv_user_money)
    TextView tv_user_money;
    @InjectView(R.id.btn_user_sign)
    FButton btn_user_sign;
    @InjectView(R.id.iv_msg_num)
    ImageView iv_msg_num;
    @InjectViews({R.id.ll_core, R.id.tv_user_lv, R.id.iv_change_avantar, R.id.btn_user_sign})
    List<View> logindedViews;
    ListView listview;
    FButton btn_login_out;
    private Uri imageFileUri = null;

    private UserinfoBean userbean;

    public UserCenterFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainThreadBusProvider.getInstance().register(this);
        if (Build.VERSION.SDK_INT >= 14) {
            addPreferencesFromResource(R.xml.setting_pref);
        } else {
            addPreferencesFromResource(R.xml.setting_pref_);
        }
        findPreference(SettingActivity.MARKET).setOnPreferenceClickListener(this);
        findPreference(SettingActivity.CLEAN).setOnPreferenceClickListener(this);
        findPreference(SettingActivity.UPDATE).setOnPreferenceClickListener(this);
        findPreference(SettingActivity.UPDATE).setSummary("当前版本:" + getString(R.string.versioname));
    }


    public void unLogin() {
        ButterKnife.apply(logindedViews, ViewUtils.GONE);
        btn_login_out.setVisibility(View.GONE);
        iv_avatar.setImageResource(R.drawable.iv_no_avantar);
        tv_user_name.setText(getString(R.string.menu_login));
        ((MainTabHostActivity) getActivity()).msg_num3.setVisibility(View.GONE);
        iv_msg_num.setVisibility(View.GONE);
    }

    @Subscribe
    public void onAfterLogined(LoginSucessEvent event) {
        logined();
        getUserInfo();
        AppLogger.e("event");
    }

    @Subscribe
    public void onRefreshUserInfo(UserInfoRefreshEvent event) {
        refreshUserInfo(event.userinfoBean);
        refreshMsgNum();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        MainThreadBusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    public void logined() {
        ButterKnife.apply(logindedViews, ViewUtils.VISIBLE);
        btn_login_out.setVisibility(View.VISIBLE);
    }

    public void getUserInfo() {
        UserCenterDao.getUserInfo(new RestHttpUtils.RestHttpHandler<CommonJson>() {
            @Override
            public void onSuccess(CommonJson result) {
                userbean = ((UserinfoBean) result.getData());
                XApplication.getInstance().getAccountBean()
                        .setMsgNum(userbean.getMessagenum());
                userbean.setId(userbean.getUserid());
                refreshUserInfo();
                SugarRecord.save(userbean);
            }

            @Override
            public void onStart() {
                refreshUserInfo();
            }

            @Override
            public void onFailed() {

            }
        });
    }

    public void refreshUserInfo() {
        if (userbean == null) {
            userbean = Select.from(UserinfoBean.class)
                    .where("ID = ?", new String[]{LoginUtil.getId()}).first();
        }
        if (userbean != null) {
            refreshUserInfo(userbean);
        }
        refreshMsgNum();
    }


    public void refreshUserInfo(UserinfoBean userbean) {
        String userName = userbean.getName();
        if (TextUtils.isEmpty(userName)) {
            userName = SharePrefUtility.getDefaultAccountName();
        }
        tv_user_name.setText(userName);
        tv_user_lv.setText(userbean.getLevel());
        CommonUtil.formatTextView(tv_user_money, userbean.getCoins(), "积分:%s");
        CommonUtil.formatTextView(tv_user_score, userbean.getCopper(), "铜币:%s");
        String headerImageUrl = SharePrefUtility.getAvatarUrl();
        if (TextUtils.isEmpty(headerImageUrl)) {
             headerImageUrl = userbean.getHeadImgUrl();
        }
        ImageLoader.getInstance().displayImage(headerImageUrl, iv_avatar, AppConfigs.AVATAR_OPTIONS,
                new AnimateFirstDisplayListener());
        XApplication.getInstance().getAccountBean().setIsSign(userbean.getIsSign() == 0 ? false : true);
        if (XApplication.getInstance().getAccountBean().getIsSign()) {
            btn_user_sign.setText("已连续签到" + userbean.getSigntimes() + "天");
        } else {
            XApplication.getInstance().getAccountBean().setIsSign(false);
            btn_user_sign.setText(XApplication.getInstance().getString(R.string.menu_sign_in));
        }


    }


    private void sign() {
        UserCenterDao.SignIn(new RestHttpUtils.RestHttpHandler<CommonJson>() {
            @Override
            public void onSuccess(CommonJson json) {
                if (json.getStatus() == 1) {
                    CommonJson<AwardBean> result = (CommonJson<AwardBean>) json;
                    String str_money = "";
                    String str_score = "";
                    if (result.getData().getAddmoney() > 0) {
                        str_money = " 铜币 +" + result.getData().getAddmoney();
                    }
                    if (result.getData().getAddscore() > 0) {
                        str_score = " 积分 +" + result.getData().getAddscore();
                    }
                    if (!TextUtils.isEmpty(str_money) || !TextUtils.isEmpty(str_score)) {
                        new MaterialDialog.Builder(getActivity())
                                .content(str_money + str_score)
                                .show();
                    }
                    XApplication.getInstance().getAccountBean().setIsSign(true);
                    refreshUserInfo();
                    userbean.setCoins(result.getData().getScore());
                    userbean.setCopper(result.getData().getMoney());
                    userbean.setSigntimes(userbean.getSigntimes() + 1);
                    SugarRecord.save(userbean);
                    refreshUserInfo(userbean);
                    btn_user_sign.setText(result.getData().getContent());

                } else {
                    CommonJson<Integer> result = (CommonJson<Integer>) json;
                    if (result != null) {
                        XApplication.getInstance().getAccountBean().setIsSign(true);
                        Toast.makeText(getActivity(), result.getInfo(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        XApplication.getInstance().getAccountBean().setIsSign(false);
                        Toast.makeText(getActivity(), "今天已签到", Toast.LENGTH_SHORT)
                                .show();
                    }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_user_center, null, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("个人中心");
        ((BaseFragmentActivity) getActivity()).setSupportActionBar(toolbar);
        View headerView = inflater.inflate(R.layout.layout_user_center_header, null, false);
        ButterKnife.inject(this, headerView);
        View footerview = inflater.inflate(R.layout.layout_user_center_footer, null, false);
        btn_login_out = (FButton) footerview.findViewById(R.id.btn_login_out);
        btn_login_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUtil.LogoutAccount();
                userbean = null;
                unLogin();
            }
        });
        listview = (ListView) view.findViewById(android.R.id.list);
        listview.setDivider(getResources().getDrawable(R.drawable.list_divider));
        listview.setDividerHeight(getResources().getDimensionPixelSize(R.dimen.divider_height));
        listview.addHeaderView(headerView);
        listview.addFooterView(footerview);
        try {
            findPreference(SettingActivity.CLEAN).setSummary("大小:" + CleanCacheWebView.getCacheSize(XApplication.getInstance()
                    .getApplicationContext().getExternalCacheDir()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        showCacheSize();
        if (LoginUtil.isAccountLogin()) {
            logined();
            getUserInfo();
        } else {
            unLogin();
        }


        return view;
    }

    private void showCacheSize() {
        try {
            findPreference(SettingActivity.CLEAN).setSummary("总计:" + CleanCacheWebView.getCacheSize(XApplication.getInstance()
                    .getApplicationContext().getExternalCacheDir()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.tv_my_fav, R.id.tv_my_msg, R.id.tv_my_ordershow,
            R.id.tv_my_coupon, R.id.tv_my_news, R.id.btn_user_sign, R.id.iv_user_avatar, R.id.tv_user_name,})
    protected void onClick(View view) {
        if (LoginUtil.isAccountLogin(getActivity())) {
            BasicNameValuePair classValuePair;
            switch (view.getId()) {
                case R.id.tv_my_fav:
                    classValuePair = new BasicNameValuePair(CommonFragmentActivity.KEY, MyFavVpFragment.class.getName());
                    IntentUtil.start_activity(getActivity(), CommonFragmentActivity.class, classValuePair);
                    break;
                case R.id.tv_my_msg:
                    classValuePair = new BasicNameValuePair(CommonFragmentActivity.KEY, UserMsgListFragment.class.getName());
                    IntentUtil.start_activity(getActivity(), CommonFragmentActivity.class, classValuePair);
                    break;

                case R.id.tv_my_ordershow:
                    classValuePair = new BasicNameValuePair(CommonFragmentActivity.KEY, MyShowFragment.class.getName());
                    IntentUtil.start_activity(getActivity(), CommonFragmentActivity.class, classValuePair);
                    break;
                case R.id.tv_my_news:
                    classValuePair = new BasicNameValuePair(CommonFragmentActivity.KEY, UserNewsFragment.class.getName());
                    IntentUtil.start_activity(getActivity(), CommonFragmentActivity.class, classValuePair);
                    break;
                case R.id.tv_my_coupon:
                    classValuePair = new BasicNameValuePair(CommonFragmentActivity.KEY, MyCouponVpFragment.class.getName());
                    IntentUtil.start_activity(getActivity(), CommonFragmentActivity.class, classValuePair);
                    break;
                case R.id.btn_user_sign:
                    if (XApplication.getInstance().getAccountBean().getIsSign()) {
                        Toast.makeText(getActivity(), "今天已签到", Toast.LENGTH_SHORT).show();
                    } else {
                        sign();
                    }
                    break;
                case R.id.iv_user_avatar:
                    AppLogger.e("sss");
                    showChangeDialog();
                    break;
            }
        }
    }


    public void refreshMsgNum() {
        if (LoginUtil.isAccountLogin()) {
            if (XApplication.getInstance().getAccountBean().getMsgNum() > 0) {
                ((MainTabHostActivity) getActivity()).msg_num3.setVisibility(View.VISIBLE);
                iv_msg_num.setVisibility(View.VISIBLE);
            } else {
                iv_msg_num.setVisibility(View.GONE);
                ((MainTabHostActivity) getActivity()).msg_num3.setVisibility(View.GONE);

            }
        }
    }

    private void clearCache() {
        new clearCacheTask().execute();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(SettingActivity.CLEAN)) {
            clearCache();
        } else if (key.equals(SettingActivity.MARKET)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id="
                    + getActivity().getPackageName()));
            startActivity(intent);
        }
//			if (key.equals(SettingActivity.EXIT)) {
//				XApplication.getInstance().startedApp = false;
//				android.os.Process.killProcess(android.os.Process.myPid());
//				System.exit(1);
//			}

        else if (key.equals(SettingActivity.UPDATE)) {
            ((MainTabHostActivity) getActivity()).getUmengUtil().checkUpdate();
        }
        return false;
    }


    class clearCacheTask extends
            MyAsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ImageLoader.getInstance().clearDiscCache();
            CleanCacheWebView.clearCacheFolder(XApplication.getInstance()
                            .getApplicationContext().getCacheDir(),
                    System.currentTimeMillis());
            CleanCacheWebView.deleteCacheFile(XApplication.getInstance());
//				CleanCacheWebView.cleanApplicationData(XApplication.getInstance());
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            ((BaseFragmentActivity) getActivity()).showLoading("正在清理");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            ((BaseFragmentActivity) getActivity()).dissmissDialog();
            Toast.makeText(XApplication.getInstance(), getString(R.string.cahce_clear_compelte),
                    Toast.LENGTH_SHORT).show();
            showCacheSize();
            super.onPostExecute(result);
        }

    }


    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = getActivity().obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    private void showChangeDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("头像上传")
                .items(R.array.upload_pic)
                .itemsCallback(this)
                .show();
    }

    @Override
    public void onSelection(MaterialDialog materialDialog, View view, int number, CharSequence charSequence) {
        switch (number) {
            case 0:
                imageFileUri = getActivity().getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new ContentValues());
                if (imageFileUri != null) {
                    Intent i = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    i.putExtra(MediaStore.EXTRA_OUTPUT,
                            imageFileUri);
                    // if (Utility.isIntentSafe(WriteWeiboActivity.this, i)) {
                    startActivityForResult(i, AppAction.PHOTOCAMERA);
                    // } else {
                    // Toast.makeText(WriteWeiboActivity.this,
                    // getString(R.string.dont_have_camera_app),
                    // Toast.LENGTH_SHORT).show();
                    // }
                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.upload_avatar_err),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                Intent choosePictureIntent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(choosePictureIntent, AppAction.PHOTORESULT);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case AppAction.PHOTOCAMERA:
                    JumpToCrop(imageFileUri);

                    // picPath = Utility.getPicPathFromUri(imageFileUri, this);
                    // AppLogger.e("picpath" + picPath);
                    break;
                case AppAction.PHOTORESULT:
                    Uri imageFileUri = intent.getData();
                    JumpToCrop(imageFileUri);
                    // picPath = Utility.getPicPathFromUri(imageFileUri, this);
                case AppAction.PHOTOCROP:
                    Bitmap bmap = intent.getParcelableExtra("data");
                    boolean isSaved;
                    if (bmap != null) {
                        try {
                            isSaved = ImageLoader.getInstance().getDiskCache().save(
                                    "avantar.jpg", bmap);
                            if (isSaved) {
                                Md5FileNameGenerator md5FileNameGenerator = new Md5FileNameGenerator();

                                uploadUserPhoto();
                            }
                            bmap.recycle();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    }
            }

        }

    }

    public void uploadUserPhoto() {
        UserCenterDao.uploadAvantar(new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                ((BaseFragmentActivity) getActivity()).showLoading("正在上传头像");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getActivity(), "修改失败",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                AppLogger.e(statusCode + responseString);
                ((BaseFragmentActivity) getActivity()).dissmissDialog();
                Toast.makeText(getActivity(), "修改成功",
                        Toast.LENGTH_SHORT).show();
                changeAvantar();
            }
        });

    }

    public void changeAvantar() {
        if (userbean != null) {
            MemoryCacheUtils.removeFromCache(userbean.getHeadImgUrl(),
                    ImageLoader.getInstance().getMemoryCache());
            DiskCacheUtils.removeFromCache(userbean.getHeadImgUrl(),
                    ImageLoader.getInstance().getDiskCache());
            ImageLoader.getInstance().displayImage(userbean.getHeadImgUrl(), iv_avatar);
        }
    }

    void refreshSighTx() {
        if (XApplication.getInstance().getAccountBean().getIsSign()) {
            btn_user_sign.setText("已连续签到" + userbean.getSigntimes() + "天");
        } else {
            XApplication.getInstance().getAccountBean().setIsSign(false);
            btn_user_sign.setText(XApplication.getInstance().getString(R.string.menu_sign_in));
        }
    }

    private void JumpToCrop(Uri mUri) {
        if (null == mUri) return;
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(mUri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);// 输出图片大小
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, AppAction.PHOTOCROP);
    }
}
