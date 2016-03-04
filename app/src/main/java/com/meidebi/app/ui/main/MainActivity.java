//package com.meidebi.app.ui.main;
//
//import android.annotation.SuppressLint;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.res.Configuration;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.preference.PreferenceActivity;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Toast;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.meidebi.app.R;
//import com.meidebi.app.XApplication;
//import com.meidebi.app.base.config.AppAction;
//import com.meidebi.app.base.error.XException;
//import com.meidebi.app.service.bean.CategoryJson;
//import com.meidebi.app.service.bean.base.BaseItemBean;
//import com.meidebi.app.service.dao.CategoryDao;
//import com.meidebi.app.service.init.ChanneInit;
//import com.meidebi.app.service.init.ChannelInitBean;
//import com.meidebi.app.support.component.UmengUtil;
//import com.meidebi.app.support.component.jpush.JushUtity;
//import com.meidebi.app.support.lib.MyAsyncTask;
//import com.meidebi.app.support.utils.AppLogger;
//import com.meidebi.app.support.utils.component.IntentUtil;
//import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
//import com.meidebi.app.ui.base.BasePullToRefreshActivity;
//import com.meidebi.app.ui.msgdetail.MsgDetailActivity;
//import com.meidebi.app.ui.search.SearchActivity;
//import com.meidebi.app.ui.setting.PushContentSettingActivity;
//
//import org.apache.http.message.BasicNameValuePair;
//
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//
////import com.github.johnpersano.supertoasts.SuperCardToast;
////import com.github.johnpersano.supertoasts.SuperToast;
//
//@SuppressLint("NewApi")
//public class MainActivity extends BasePullToRefreshActivity  {
//	private DrawerLayout mDrawerLayout;
//	private ActionBarDrawerToggle mDrawerToggle;
//	private Fragment mContentFragment;
//	private ChannelInitBean mCategory;
//	private Menu mMenu;
//	private LeftDrawerFragment leftDrawerFragment;
//	private SingelChanelVpFragment guo;
//	private UmengUtil umengUtil;
////	private SuperCardToast superCardToast;
////	private UploadShowOrderFragment uploadShowOrderFragment;
//
//	public UmengUtil getUmengUtil() {
//		if (umengUtil == null) {
//			umengUtil = new UmengUtil(this);
//		}
//		return umengUtil;
//	}
//
//	public void setUmengUtil(UmengUtil umengUtil) {
//		this.umengUtil = umengUtil;
//	}
//
////	public UploadShowOrderFragment getUploadShowOrderFragment() {
////		if (uploadShowOrderFragment == null) {
////			uploadShowOrderFragment = new UploadShowOrderFragment();
////		}
////		return uploadShowOrderFragment;
////	}
//
//	public SingelChanelVpFragment getGuo(boolean refresh) {
//		if (guo == null || refresh) {
//			guo = new SingelChanelVpFragment(mCategory);
//		}
//		return guo;
//	}
//
//	public SingelChanelVpFragment getIndex(boolean refresh) {
//		if (index == null || refresh) {
//			index = new SingelChanelVpFragment(mCategory);
//		}
//		return index;
//	}
//
//	public SingelChanelVpFragment getTmall(boolean refresh) {
//		if (tmall == null || refresh) {
//			tmall = new SingelChanelVpFragment(mCategory);
//		}
//		return tmall;
//	}
//
//	public SingelChanelVpFragment getHaitao(boolean refresh) {
//		if (haitao == null || refresh) {
//			haitao = new SingelChanelVpFragment(mCategory);
//		}
//		return haitao;
//	}
//
//	public void setHaitao(SingelChanelVpFragment haitao) {
//		this.haitao = haitao;
//	}
//
//	public OrderShowListFragment getShaidan(boolean refresh) {
//		if (shaidan == null || refresh) {
//			shaidan = new OrderShowListFragment();
//		}
//		return shaidan;
//	}
//
//	public CouponListFragment getCouponlist() {
//		if (couponlist == null) {
//			couponlist = new CouponListFragment();
//		}
//		return couponlist;
//	}
//
//	public void setCouponlist(CouponListFragment couponlist) {
//		this.couponlist = couponlist;
//	}
//
//	private SingelChanelVpFragment index;
//	private SingelChanelVpFragment haitao;
//	private SingelChanelVpFragment tmall;
//	private OrderShowListFragment shaidan;
//	private CouponListFragment couponlist;
// 	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		XApplication.getInstance().setActivity(this);
//		XApplication.getInstance().setStartedApp(true);
// 		initDrawer();
//		setBackButtonGone();
//		setActionBar("首页");
//		showDialog();
//		DoNofyIntent();
//		if (XApplication.getInstance().getCatlist().size() == 0) {
//			CatagerogyInfoTask task = new CatagerogyInfoTask();
//			task.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
//		}else{
//            buildDrawer();
//            getView_load().onDone();
//            mCategory = ChanneInit.getInstance().get().get(0);
//             replaceFragment(R.id.content_frame, getIndex(false));
//             mContentFragment = getIndex(false);
//        }
////		SuperCardToast.onRestoreState(savedInstanceState, MainActivity.this);
// 	}
//
//	private void showDialog(){
//  		if (SharePrefUtility.firshSetPush()) {
//            new MaterialDialog.Builder(this)
//                .title(R.string.push_dialog_title)
//                .content(R.string.push_dialog_content)
//                .positiveText(R.string.close)
//                .negativeText(R.string.open)
//                 .callback(new MaterialDialog.ButtonCallback() {
//                     @Override
//                     public void onPositive(MaterialDialog dialog) {
//                         super.onPositive(dialog);
//                         SharePrefUtility.setPushOn(false);
//
//                     }
//
//                     @Override
//                     public void onNegative(MaterialDialog dialog) {
//                         super.onNegative(dialog);
//                         SharePrefUtility.setPushOn(true);
//                         JushUtity.stopPush();
//                         IntentUtil.start_activity(MainActivity.this,
//                                 PushContentSettingActivity.class, new BasicNameValuePair(
//                                         "openall", "open"));
//                     }
//                 })
//                .show();
//
//         }else{
//      		getUmengUtil().onStart();
// 	    }
//	}
//
//	private void initDrawer() {
//		// TODO Auto-generated method stub
//		findViews();
//		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
//				GravityCompat.START);
//		mDrawerLayout.setScrimColor(Color.argb(100, 0, 0, 0));// 阴影
//		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
//				actionBar, R.string.drawer_open,
//				R.string.drawer_close) {
//			public void onDrawerClosed(View view) {
//				// mMenu.findItem(R.id.action_refresh).setVisible(true);
//			}
//
//			//
//			public void onDrawerOpened(View drawerView) {
//				// mMenu.findItem(R.id.action_refresh).setVisible(false);
//			}
//		};
//	}
//
//	private void buildDrawer() {
//		mDrawerLayout.setDrawerListener(mDrawerToggle);
//		leftDrawerFragment = new LeftDrawerFragment();
//		FragmentManager fragmentManager = getSupportFragmentManager();
//		fragmentManager.beginTransaction()
//				.replace(R.id.left_drawer, leftDrawerFragment).commitAllowingStateLoss();
//		// getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
//	}
//
//	@Override
//	protected void onPostCreate(Bundle savedInstanceState) {
//		super.onPostCreate(savedInstanceState);
//		if (mDrawerToggle != null) {
//			mDrawerToggle.syncState();
//		}
//	}
//
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//		if (mDrawerToggle != null) {
//			mDrawerToggle.onConfigurationChanged(newConfig);
//		}
//	}
//
//	private void findViews() {
//		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		super.onCreateOptionsMenu(menu);
//		getMenuInflater().inflate(R.menu.main_action_provider, menu);
//		// provider = (ChooseCatActionProvider)
//		// MenuItemCompat.getActionProvider(menu.findItem(R.id.choose));
//		// provider.setActivity(this);
//		mMenu = menu;
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		if (mDrawerToggle.onOptionsItemSelected(item)) {
//			return true;
//		}
//		switch (item.getItemId()) {
//		// case R.id.action_refresh:
//		// // mContentFragment.loadFirstPageAndScrollToTop();
//		// return true;
//		case R.id.action_settings:
//			startActivity(new Intent(this, PreferenceActivity.class));
//			return true;
//		case R.id.search:
//			IntentUtil.start_activity(MainActivity.this, SearchActivity.class);
//			return true;
//		case R.id.choose:
//			changeMenu();
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//
//	}
//
//	public void setCategory(ChannelInitBean category) {
//		mDrawerLayout.closeDrawer(GravityCompat.START);
//		if (mCategory == category) {
//			AppLogger.e("abs");
//			return;
//		}
//		mCategory = category;
//		refreshView(false);
//	}
//
//	public void refreshView(boolean refresh) {
//		setActionBar(mCategory.getName());
//		// actionBar.setTitle(mCategory.getName());
//		// actionBar.
//		if (mCategory.getName().equals(getString(R.string.cat_buycoupon))) {
//			switchFragment(getCouponlist());
////			if (superCardToast != null) {
////				if (superCardToast.isShowing()) {
////					superCardToast.dismiss();
////				}
////			}
//			hideChooseMenu();
//		} else if (mCategory.getName()
//				.equals(getString(R.string.cat_ordershow))) {
//			getShaidan(refresh).setHot(mCategory.isHot());
//			switchFragment(getShaidan(false));
//			showLatestTime(false, getShaidan(false));
//			selectedMenu(getShaidan(false).isHot());
//			visChooseMenu();
//		} else if (mCategory.getName().equals(getString(R.string.cat_index))) {
//			switchFragment(getIndex(refresh));
//			showLatestTime(true, getIndex(refresh));
//			selectedMenu(getIndex(false).isHot());
//			visChooseMenu();
//		} else if (mCategory.getName().equals(getString(R.string.cat_inland))) {
//			switchFragment(getGuo(refresh));
//			showLatestTime(true, getGuo(false));
//			selectedMenu(getGuo(false).isHot());
//			visChooseMenu();
//		} else if (mCategory.getName().equals(getString(R.string.cat_haitao))) {
//			switchFragment(getHaitao(refresh));
//			showLatestTime(true, getHaitao(false));
//			selectedMenu(getHaitao(false).isHot());
//			visChooseMenu();
//		} else if (mCategory.getName().equals(getString(R.string.cat_tmall))) {
//			switchFragment(getTmall(refresh));
//			showLatestTime(true, getTmall(false));
//			selectedMenu(getTmall(false).isHot());
//			visChooseMenu();
//		}
////        else if (mCategory.getName().equals("云相机")) {
////			switchFragment(getUploadShowOrderFragment());
////			hideChooseMenu();
////		}
//	}
//
//	public void showLatestTime(boolean isVp, Fragment fragment) {
//		if (isVp) {
//			showToast(((SingelChanelVpFragment) fragment).getData_insert_time());
//		} else {
////			showToast(((BasePullToRefreshListFragment) fragment)
////					.getData_insert_time());
//		}
//	}
//
//	private void switchFragment(Fragment fragment) {
//		if (mContentFragment != null) {
//			mContentFragment.onPause();
//			switchContent(mContentFragment, fragment, R.id.content_frame);
//			mContentFragment = fragment;
//		}
//	}
//
//
//
//
////    @Override
////    public Toolbar getToolBar() {
////        return actionBar;
////    }
//
//    class CatagerogyInfoTask extends
//			MyAsyncTask<Void, List<BaseItemBean>, List<BaseItemBean>> {
//
//		private XException e;
//
//		public CatagerogyInfoTask() {
//		}
//
//		private CategoryDao dao;
//
//		public CategoryDao getDao() {
//			if (dao == null) {
//				dao = new CategoryDao();
//			}
//			return dao;
//		}
//
//		@Override
//		protected List<BaseItemBean> doInBackground(Void... params) {
//			CategoryJson json = null;
//			json = getDao().getCategory();
//			if (json != null) {
//				return json.getData();
//			} else {
//				cancel(true);
//    		}
//			return null;
//		}
//
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			getView_load().onLoad();
//			super.onPreExecute();
//		}
//
//		@Override
//		protected void onCancelled() {
//			// TODO Auto-generated method stub
//			getView_load().onFaied();
//			super.onCancelled();
//		}
//
//		@Override
//		protected void onPostExecute(List<BaseItemBean> bean) {
//			super.onPostExecute(bean);
//			buildDrawer();
//			getView_load().onDone();
//			mCategory = ChanneInit.getInstance().get().get(0);
//			XApplication.getInstance().setCatlist(bean);
//			replaceFragment(R.id.content_frame, getIndex(false));
//			// addFragment(getFr_main());
//			mContentFragment = getIndex(false);
//		}
//	}
//
//	@Override
//	public void onReload() {
//		// TODO Auto-generated method stub
//		super.onReload();
//		if (XApplication.getInstance().getCatlist().size() == 0) {
//			CatagerogyInfoTask task = new CatagerogyInfoTask();
//			task.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
//		}
//	}
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//         if (leftDrawerFragment != null) {
//			leftDrawerFragment.refreshMsgNum();
//		}
//	}
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//     }
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == RESULT_OK) {
//			regBroadCast(AppAction.CALLLOGINEDOUT);
//		}else if(resultCode == RESULT_FIRST_USER){
//             if(leftDrawerFragment!=null){
//                 leftDrawerFragment.changeAvantar();
//             }
//        }
//	}
//
//	public void changeMenu() {
//		// TODO Auto-generated method stub
//		if (mCategory != null) {
//			mCategory.changeHot();
//			refreshView(true);
//		}
//	}
//
//	public void selectedMenu(boolean isHot) {// 切换后
//		// TODO Auto-generated method stub
//		mCategory.setHot(isHot);
//		mMenu.findItem(R.id.choose).setTitle(isHot ? "精华" : "最新");
//		// provider.selectedItem(isHot);
//	}
//
//	public void hideChooseMenu() {
//		if (mMenu != null)
//			mMenu.findItem(R.id.choose).setVisible(false);
//	}
//
//	public void visChooseMenu() {
//		if (mMenu != null)
//			mMenu.findItem(R.id.choose).setVisible(true);
//	}
//
//	@Override
//	public void onStart() {
//		super.onStart();
//		regBroadCast(AppAction.CALLLOGINEDOUT);
//		regSignBroadCast();
//		regLoginBroadCast();
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		unregBroadCast(AppAction.CALLLOGINEDOUT);
//		LocalBroadcastManager.getInstance(this).unregisterReceiver(
//				SignBroadcastReceiver);
//		LocalBroadcastManager.getInstance(this).unregisterReceiver(
//				LoginBroadcastReceiver);
//	}
//
//	@Override
//	protected void doBroadCastAction(Intent intent) {
//		// TODO Auto-generated method stub
//		super.doBroadCastAction(intent);
//		AppLogger.e(AppAction.CALLLOGINEDOUT);
//		leftDrawerFragment.LoginOut();
//		unregBroadCast(AppAction.CALLLOGINEDOUT);
//	}
//
//	public void regSignBroadCast() {
//		LocalBroadcastManager.getInstance(this).registerReceiver(
//				SignBroadcastReceiver, new IntentFilter(AppAction.CALLSIGN));
//	}
//
//	public void regLoginBroadCast() {
//		LocalBroadcastManager.getInstance(this)
//				.registerReceiver(LoginBroadcastReceiver,
//						new IntentFilter(AppAction.CALLLOGINED));
//	}
//
//	private BroadcastReceiver SignBroadcastReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			leftDrawerFragment.refreshSighTx();
//			LocalBroadcastManager.getInstance(MainActivity.this)
//					.unregisterReceiver(SignBroadcastReceiver);
//		}
//	};
//
//	private BroadcastReceiver LoginBroadcastReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			leftDrawerFragment.afterLogin();
//			LocalBroadcastManager.getInstance(MainActivity.this)
//					.unregisterReceiver(LoginBroadcastReceiver);
//		}
//	};
//
//	private int keyBackClickCount = 0;
//
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
//				switch (keyBackClickCount++) {
//				case 0:
//					Toast.makeText(
//							this,
//							getResources().getString(R.string.press_again_exit),
//							Toast.LENGTH_SHORT).show();
//					Timer timer = new Timer();
//					timer.schedule(new TimerTask() {
//						@Override
//						public void run() {
//							keyBackClickCount = 0;
//						}
//					}, 3000);
//					break;
//				case 1:
//					XApplication.getInstance().startedApp = false;
//					android.os.Process.killProcess(android.os.Process.myPid());
//					System.exit(1);
//					break;
//				default:
//					break;
//				}
//				return true;
//			}
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//
//	private void DoNofyIntent() {
//		Boolean nofy = getIntent().getBooleanExtra("nofy", false);
//		if (nofy) {
//			getIntent().setClass(this, MsgDetailActivity.class);
//			startActivity(getIntent());
//		}
//	}
//
//	public void titleOnclick() {
//		if (mDrawerLayout != null) {
//			if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
//				mDrawerLayout.openDrawer(Gravity.LEFT);
//			} else {
//				mDrawerLayout.closeDrawer(Gravity.LEFT);
//			}
//		}
//	}
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.activity_main;
//    }
//
//    public void showToast(long time) {
//		if (time == 0) {
//			return;
//		}
////		if (superCardToast != null) {
////			if (superCardToast.isShowing()) {
////				return;
////			}
////		}
////		superCardToast = new SuperCardToast(this, SuperToast.Type.STANDARD);
////		superCardToast.setAnimations(SuperToast.Animations.POPUP);
////		superCardToast.setBackground(R.color.transparent);
////		superCardToast.setTextColor(getResources().getColor(
////				R.color.text_gery_color));
////		superCardToast.setText("显示的是" + TimeUtil.getListTime(time) + "的缓存");
////		superCardToast.show();
//
//	}
//
//	public void showNewestToast() {
////		if (superCardToast != null) {
////			if (superCardToast.isShowing()) {
////				return;
////			}
////		}
////		superCardToast = new SuperCardToast(this, SuperToast.Type.STANDARD);
////		superCardToast.setAnimations(SuperToast.Animations.POPUP);
////		superCardToast.setBackground(R.color.transparent);
////		superCardToast.setTextColor(getResources().getColor(
////				R.color.text_gery_color));
////		superCardToast.setText("已载入的最新数据");
////		superCardToast.show();
//	}
//
//
//
//
//
//
//}