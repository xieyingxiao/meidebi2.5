package com.meidebi.app.ui.user;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.service.bean.user.UserinfoBean;
import com.meidebi.app.service.dao.user.UserCenterDao;
import com.meidebi.app.support.file.FileManager;
import com.meidebi.app.support.utils.anim.AnimateFirstDisplayListener;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;
import com.meidebi.app.ui.view.roundedview.RoundedImageView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.orm.SugarRecord;
import com.orm.query.Select;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCenterActivity extends BasePullToRefreshActivity implements
        MaterialDialog.ListCallback {
    private SimpleAdapter adapter;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private TextView tv_credits, tv_lv;
    private RoundedImageView iv_avatar;
    private TextView tv_fan, tv_news, tv_showorder;
    private final int NETERR = 4;
    private final int upload_sucess = 11;
    private final int upload_fail = 12;
    private UserCenterDao userDao;

    private final int GETINFO = 101;

    private String picPath = "";
    private Uri imageFileUri = null;
    private static final int CAMERA_RESULT = 0;
    private static final int PIC_RESULT = 1;
    private UserinfoBean bean;

    public UserCenterDao getUserDao() {
        if (userDao == null) {
            userDao = new UserCenterDao(this);
        }
        return userDao;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setActionBar("个人中心");
        BuildUserLoginedView();

        ListView listview = (ListView) findViewById(R.id.common_list_view);
        initData();
        adapter = new SimpleAdapter(UserCenterActivity.this, list,
                R.layout.adapter_common, new String[]{AppConfigs.LIST_TEXT},
                new int[]{android.R.id.text1}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub.
                View view = super.getView(position, convertView, parent);
                ImageView iv = (ImageView) view.findViewById(R.id.iv_msg_num);
                if (position == 0
                        && XApplication.getInstance().getAccountBean()
                        .getMsgNum() > 0) {
                    iv.setVisibility(View.VISIBLE);
                } else {
                    iv.setVisibility(View.GONE);
                }
                return view;
            }
        };
        listview.setAdapter(adapter);
        listview.setDivider(null);
        // listview.setDividerHeight(2);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                ItemClick(arg2);
            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getUserInfo();
    }

    private void BuildUserLoginedView() {

        findViewById(R.id.iv_change_avantar).setVisibility(View.VISIBLE);

        TextView tv_name = (TextView) findViewById(R.id.tv_user_name);
        tv_name.setVisibility(View.VISIBLE);
        tv_name.setText(XApplication.getInstance().getAccountBean().getUsername());

        iv_avatar = (RoundedImageView) findViewById(R.id.iv_user_avatar);
        iv_avatar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addPic();
            }
        });
        tv_lv = (TextView) findViewById(R.id.tv_user_lv);
        tv_lv.setVisibility(View.VISIBLE);
        tv_credits = (TextView) findViewById(R.id.tv_user_score);
        tv_credits.setVisibility(View.VISIBLE);
        tv_fan = (TextView) findViewById(R.id.tv_user_fan_count);
        tv_news = (TextView) findViewById(R.id.tv_user_news_count);
        tv_showorder = (TextView) findViewById(R.id.tv_user_showorder_count);

        refreshUserInfo();
    }

    /**
     * 登陆前的列表
     */
    private void initData() {
        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put(AppConfigs.LIST_TEXT, XApplication.getInstance()
                .getResources().getString(R.string.users_msg_center));
        list.add(map4);
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put(AppConfigs.LIST_TEXT, XApplication.getInstance()
                .getResources().getString(R.string.users_fav));
        list.add(map3);
        Map<String, Object> map8 = new HashMap<String, Object>();
        map8.put(AppConfigs.LIST_TEXT, XApplication.getInstance()
                .getResources().getString(R.string.users_coupon));
        list.add(map8);

        Map<String, Object> map6 = new HashMap<String, Object>();
        map6.put(AppConfigs.LIST_TEXT, XApplication.getInstance()
                .getResources().getString(R.string.users_score_driction));
        list.add(map6);
        Map<String, Object> map7 = new HashMap<String, Object>();
        map7.put(AppConfigs.LIST_TEXT, XApplication.getInstance()
                .getResources().getString(R.string.pref_loginout));
        list.add(map7);
    }

    public void ItemClick(int position) {
        switch (position) {
//		case 0:
//			IntentUtil.start_activity(this, UserMsgActivity.class);
//			break;
//		case 1:
//			IntentUtil.start_activity(this, UserFavActivity.class);
//			break;
//		case 2:
//			IntentUtil.start_activity(this, MyCouponListActivity.class);
//			break;
//		case 3:
//			IntentUtil.start_activity(this, ScoreDirectionVpActivity.class);
//			break;
//		case 4:
//			IntentUtil.sendActionBroadCast(AppAction.CALLLOGINEDOUT);
//			finish();
//			break;
            default:
                break;
        }
    }

    public void refreshUserInfo() {
        UserinfoBean userbean = Select.from(UserinfoBean.class)
                .where("ID = ?", new String[]{LoginUtil.getId()}).first();
        ImageLoader.getInstance().displayImage(userbean.getHeadImgUrl(),
                iv_avatar, AppConfigs.AVATAR_OPTIONS,
                new AnimateFirstDisplayListener());

        String str_money = "";
        String str_score = "";
        String str_contrubuid = "";
        if (TextUtils.isEmpty(userbean.getCopper())) {
            str_money = "铜币:0";
        } else {
            str_money = "铜币:" + userbean.getCopper();
        }
        if (TextUtils.isEmpty(userbean.getCoins())) {
            str_score = "   积分:0";
        } else {
            str_score = "   积分:" + userbean.getCoins();
        }
        if (TextUtils.isEmpty(userbean.getContribution())) {
            str_contrubuid = "   贡献值:0";
        } else {
            str_contrubuid = "   贡献值:" + userbean.getContribution();
        }
        tv_credits.setText(str_money + str_score + str_contrubuid);
        if (!TextUtils.isEmpty(userbean.getTotallevel())) {
            tv_lv.setText(XApplication.getInstance().getString(R.string.level)
                    + userbean.getTotallevel());
        }
        tv_lv.setVisibility(View.VISIBLE);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        tv_news.setText(userbean.getLinkcount());
        tv_showorder.setText(userbean.getShowdancount());
        tv_fan.setText(userbean.getFancount());
    }


    public void getUserInfo() {
//		new Thread() {
//			@Override
//			public void run() {
//				CommonJson<UserinfoBean> bean = getUserDao().getUserInfo();
//				Message message = new Message();
//				if (bean != null) {
//					if (bean.getStatus() == 1) {
//						message.obj = bean.getData();
//						message.what = GETINFO;
//						mHandler.sendMessage(message);
//					}
//				}
//			}
//		}.start();
    }


    /**
     * 数据回调处理
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // dissmissDialog();
            switch (msg.what) {

                case NETERR:
                    Toast.makeText(UserCenterActivity.this,
                            getString(R.string.timeout), Toast.LENGTH_SHORT).show();
                    break;
                case GETINFO:
                    bean = ((UserinfoBean) msg.obj);
                    XApplication.getInstance().getAccountBean()
                            .setMsgNum(bean.getMessagenum());
                    bean.setId(bean.getUserid());
                    refreshUserInfo();
                    break;
                case upload_sucess:
                    dissmissDialog();
                    Toast.makeText(UserCenterActivity.this, "修改成功",
                            Toast.LENGTH_SHORT).show();
                    changeAvantar();
                    break;
                case upload_fail:
                    showErr("上传失败");
                    break;
                default:// 失败
                    // Toast.makeText(LoginActivity.this,
                    // getString(R.string.login_failed), Toast.LENGTH_SHORT)
                    // .show();
                    break;
            }
        }

        ;

    };

//	@Override
//	public void onListItemSelected(String value, int number) {
//		switch (number) {
//		case 0:
//			imageFileUri = getContentResolver().insert(
//					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//					new ContentValues());
//			if (imageFileUri != null) {
//				Intent i = new Intent(
//						MediaStore.ACTION_IMAGE_CAPTURE);
//				i.putExtra(MediaStore.EXTRA_OUTPUT,
//						imageFileUri);
//				// if (Utility.isIntentSafe(WriteWeiboActivity.this, i)) {
//				startActivityForResult(i, CAMERA_RESULT);
//				// } else {
//				// Toast.makeText(WriteWeiboActivity.this,
//				// getString(R.string.dont_have_camera_app),
//				// Toast.LENGTH_SHORT).show();
//				// }
//			} else {
//				Toast.makeText(UserCenterActivity.this,
//						getString(R.string.upload_avatar_err),
//						Toast.LENGTH_SHORT).show();
//			}
//			break;
//		case 1:
//			Intent choosePictureIntent = new Intent(
//					Intent.ACTION_PICK,
//					MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//			startActivityForResult(choosePictureIntent, PIC_RESULT);
//			break;
//		}
//	}

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_RESULT:
                    IntentUtil.JumpToCrop(imageFileUri, this);
                    // picPath = Utility.getPicPathFromUri(imageFileUri, this);
                    // AppLogger.e("picpath" + picPath);
                    break;
                case PIC_RESULT:
                    Uri imageFileUri = intent.getData();
                    IntentUtil.JumpToCrop(imageFileUri, this);
                    // picPath = Utility.getPicPathFromUri(imageFileUri, this);
                case IntentUtil.cropImageCode:
                    Bitmap bmap = intent.getParcelableExtra("data");
                    boolean isSaved;
                    if (bmap != null) {
                        try {
                            isSaved = imageLoader.getDiscCache().save(
                                    "avantar.jpg", bmap);
                            if (isSaved) {
                                Md5FileNameGenerator md5FileNameGenerator = new Md5FileNameGenerator();
                                picPath = FileManager.getSdCardPath()
                                        + "/"
                                        + md5FileNameGenerator
                                        .generate("avantar.jpg");
                                uploadUserPhoto(picPath);
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

    public void uploadUserPhoto(final String pic) {
//		showLoading("正在上传头像");
//		new Thread() {
//			@Override
//			public void run() {
//				Boolean ifUpload = getUserDao().uploadAvantar(pic);
//				Message message = new Message();
//				if (ifUpload) {
//					message.what = upload_sucess;
//					mHandler.sendMessage(message);
//				} else {
//					message.what = upload_fail;
//					mHandler.sendMessage(message);
//				}
//			}
//		}.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_center, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_change_avantar:
                addPic();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void addPic() {
        new MaterialDialog.Builder(this)
                .title("头像上传")
                .items(R.array.upload_pic)
                .itemsCallback(this)
                .show();
//
//		ListDialogFragment
//				.createBuilder(this, getSupportFragmentManager())
//				.setTitle("头像上传")
//				.setItems(
//						new String[] {
//								getString(R.string.upload_dialog_take_photo),
//								getString(R.string.upload_dialog_photolib) })
//				.show();
    }

    public void changeAvantar() {
        if (bean != null) {
            MemoryCacheUtils.removeFromCache(bean.getHeadImgUrl(),
                    imageLoader.getMemoryCache());
            DiskCacheUtils.removeFromCache(bean.getHeadImgUrl(),
                    imageLoader.getDiskCache());
            imageLoader.displayImage(bean.getHeadImgUrl(), iv_avatar);
            setResult(RESULT_FIRST_USER);
        }
    }

    @Override
    public void titleOnclick() {
        super.titleOnclick();
        if (bean != null) {
            SugarRecord.save(bean);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_user_center;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (bean != null) {
            SugarRecord.save(bean);
        }
    }

    @Override
    public void onSelection(MaterialDialog materialDialog, View view, int number, CharSequence charSequence) {
        switch (number) {
            case 0:
                imageFileUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new ContentValues());
                if (imageFileUri != null) {
                    Intent i = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    i.putExtra(MediaStore.EXTRA_OUTPUT,
                            imageFileUri);
                    // if (Utility.isIntentSafe(WriteWeiboActivity.this, i)) {
                    startActivityForResult(i, CAMERA_RESULT);
                    // } else {
                    // Toast.makeText(WriteWeiboActivity.this,
                    // getString(R.string.dont_have_camera_app),
                    // Toast.LENGTH_SHORT).show();
                    // }
                } else {
                    Toast.makeText(UserCenterActivity.this,
                            getString(R.string.upload_avatar_err),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                Intent choosePictureIntent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(choosePictureIntent, PIC_RESULT);
                break;
        }
    }
}
