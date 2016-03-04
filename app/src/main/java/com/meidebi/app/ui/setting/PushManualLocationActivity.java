package com.meidebi.app.ui.setting;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.lbs.AddressBean;
import com.meidebi.app.service.dao.GetAddressDao;
import com.meidebi.app.support.component.bus.LocationChooseEvent;
import com.meidebi.app.support.component.bus.MainThreadBusProvider;
import com.meidebi.app.support.lib.MyAsyncTask;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.ui.anim.Rotate3dAnimation;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;

import java.util.List;

public class PushManualLocationActivity extends BasePullToRefreshActivity implements
		OnClickListener {
	private AddressFragment province_card, city_card, town_card;
	private AddressFragment lastFragment;
	private int type;
	private String id;
	private Boolean firstAdd = false;
	private TextView tv_provine, tv_city, tv_town;
 	public AddressFragment getProvince_card() {
		if (province_card == null) {
			province_card = new AddressFragment(1);
		}
		return province_card;
	}

	public AddressFragment getCity_card() {
		if (city_card == null) {
			city_card = new AddressFragment(2);
		}
		return city_card;
	}

	public AddressFragment getTown_card() {
		if (town_card == null) {
			town_card = new AddressFragment(3);
		}
		return town_card;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setTitle("选择推送地区");
 		tv_provine = (TextView) findViewById(R.id.tv_add_province);
		tv_city = (TextView) findViewById(R.id.tv_add_city);
		tv_town = (TextView) findViewById(R.id.tv_add_town);
		tv_provine.setOnClickListener(this);
		tv_city.setOnClickListener(this);
		tv_town.setOnClickListener(this);
		refreshView("0", 1, "");
	}

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_push_manual_location;
    }

    // frgment回调
	public void refreshView(String id, int type, String result) {
		switch (type) {
		case 2:
			tv_provine.setText(result);
			doTvAinm(tv_provine, true);
			break;
		case 3:
			tv_city.setText(result);
			doTvAinm(tv_city, true);
			break;
		// case 4:
		// tv_town.setText(result);
		// doTvAinm(tv_town, true);
		// break;
		default:
			break;
		}
		if (type != 4) {
			this.id = id;
			this.type = type;
			getAddTask task = new getAddTask();
			getView_load().onLoad();
			task.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
		} else {
            MainThreadBusProvider.getInstance().post(new LocationChooseEvent(tv_provine.getText().toString()
                    + tv_city.getText().toString() + result));
//			Intent intent = new Intent();
// 			intent.putExtra("result",tv_provine.getText().toString()
//					+ tv_city.getText().toString() + result);
//			setResult(RESULT_OK, intent);
			finish();
		}
	}

	public class getAddTask extends
			MyAsyncTask<Void, List<AddressBean>, List<AddressBean>> {

		private XException e;

		public getAddTask() {
		}

		private GetAddressDao dao;

		public GetAddressDao getDao() {
			if (dao == null) {
				dao = new GetAddressDao();
			}
			return dao;
		}

		@Override
		protected List<AddressBean> doInBackground(Void... params) {
			ListJson<AddressBean> json = null;
			getDao().setType(type);
			json = getDao().MapperJson(id);
			if (json != null) {
				return json.getData();
			} else {
				cancel(true);
			}
			return null;
		}
		
		

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			getView_load().onFaied();
		}

		@Override
		protected void onPostExecute(List<AddressBean> bean) {
			super.onPostExecute(bean);
			getView_load().onDone();
			if (!firstAdd) {
				getProvince_card().setList_address(bean);
				addFragment(getProvince_card());
				lastFragment = getProvince_card();
				firstAdd = true;
			} else {
				switch (type) {
				case 1:
					swithFragment(bean, getProvince_card());
					break;
				case 2:
					swithFragment(bean, getCity_card());
					break;
				case 3:
					swithFragment(bean, getTown_card());
					break;
				default:
					break;
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_add_province:
			swithFragment(getProvince_card());
			doTvAinm(tv_provine, false);
			doTvAinm(tv_city, false);
			doTvAinm(tv_town, false);
			break;
		case R.id.tv_add_city:
			swithFragment(getCity_card());
			doTvAinm(tv_city, false);
			doTvAinm(tv_town, false);
			break;
		case R.id.tv_add_town:
			doTvAinm(tv_town, false);
			swithFragment(getTown_card());
			break;
		default:
			break;
		}
	}

	private void swithFragment(List<AddressBean> bean, AddressFragment fragment) {
		if (bean != null) {
			fragment.setList_address(bean);
			fragment.refresh();
			AppLogger.e("addBean");
		}
		switchContentWithBack(lastFragment, fragment);
		lastFragment = fragment;
	}

	private void swithFragment(AddressFragment fragment) {
		switchContentWithBack(lastFragment, fragment);
		lastFragment = fragment;
	}

	private void doTvAinm(final TextView tv, final Boolean add) {
		if (!tv.isShown() && add || tv.isShown() && !add) {
			if (add) {// 让第一次动画执行
				tv.setVisibility(View.INVISIBLE);
			}
			float centerX = tv.getWidth() / 2;
			float fromDegree;
			float toDegegree;
			if (add) {
				fromDegree = 270f;
				toDegegree = 360f;
			} else {
				fromDegree = 360f;
				toDegegree = 270f;
			}
			// 构建3D旋转动画对象，旋转角度为270到360度，这使得ImageView将会从不可见变为可见
			final Rotate3dAnimation rotation = new Rotate3dAnimation(
					fromDegree, toDegegree, 0, 0, 0.0f, false, false);
			// 动画持续时间500毫秒
			rotation.setDuration(500);
			// 动画完成后保持完成的状态
			rotation.setInterpolator(new AccelerateInterpolator());
			tv.setAnimation(rotation);
			rotation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					if (add) {
						tv.setVisibility(View.VISIBLE);
					} else {
						tv.setVisibility(View.GONE);
					}
				}
			});
			rotation.start();
		}
	}
	@Override
	public void onReload() {//点击回调
		// TODO Auto-generated method stub
		getAddTask task = new getAddTask();
		getView_load().onLoad();
		task.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
		}

    @Override public void onResume() {
        super.onResume();
        MainThreadBusProvider.getInstance().register(this);
    }

    @Override public void onPause() {
        super.onPause();
        MainThreadBusProvider.getInstance().unregister(this);
    }




}
