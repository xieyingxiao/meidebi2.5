package com.meidebi.app.ui.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meidebi.app.R;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.base.MsgBaseBean;
import com.meidebi.app.service.bean.user.FavBean;
import com.meidebi.app.service.dao.user.MyFavDao;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.ui.adapter.MyFavListAdapter;
import com.meidebi.app.ui.commonactivity.BackPressListFragment;
import com.meidebi.app.ui.msgdetail.MsgDetailActivity;
import com.meidebi.app.ui.msgdetail.OrderShowDetailActivity;
import com.meidebi.app.ui.widget.DividerItemDecoration;

@SuppressLint("ValidFragment")
public class UserFavFragment extends BackPressListFragment<FavBean> {
	private MyFavDao dao;
	private int linke_type;
    public static final int ODERSHOW_PARAM = 4;
    public static final int SINGLE_PARAM = 1;
    public static final int COUPON_PARAM = 5;

	public MyFavDao getDao() {
		if (dao == null)
			dao = new MyFavDao(getActivity());
		return dao;
	}

	public UserFavFragment(int type) {
		this.linke_type = type;
	}

	public UserFavFragment() {
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = super.onCreateView(inflater, null, savedInstanceState);// sld
		 setEmptyView(R.drawable.ic_search_result_empty,
		 R.string.users_fav_empty);
		mAdapter = new MyFavListAdapter(this.getActivity(), items_list);
		((MyFavListAdapter) mAdapter).setType(linke_type);
		mAdapter.setData(items_list);
        mAdapter.setOnItemClickListener(this);
        getListView().setAdapter(mAdapter);
        getListView().addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        setTitle(getString(R.string.my_fav));
        onStartRefresh();
        //		getListView().setMultiChoiceModeListener(new MultiChoiceModeListener() {
//
//			private int nr = 0;
//
//			@Override
//			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//				mode.getMenuInflater().inflate(R.menu.action_del, menu);
//				return true;
//			}
//
//			@Override
//			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//				return false;
//			}
//
//			@Override
//			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//				return false;
//			}
//
//			@Override
//			public void onDestroyActionMode(ActionMode mode) {
//				nr = 0;
//			}
//
//			@Override
//			public void onItemCheckedStateChanged(ActionMode mode,
//					int position, long id, boolean checked) {
//				if (checked) {
//					nr++;
//				} else {
//					nr--;
//				}
//				mode.setTitle(nr + " rows selected!");
//			}
//		});
		return view;
	}

    @Override
    public void onBackPress() {

    }

    @Override
	protected void getData(final int type) {
		// TODO Auto-generated method
        getDao().setPage(mPage);
        getDao().getResult(String.valueOf(linke_type), new RestHttpUtils.RestHttpHandler<ListJson>() {
            @Override
            public void onSuccess(ListJson result) {
                OnCallBack(type, result.getData());
            }

            @Override
            public void onStart() {
                OnStart(type);
            }

            @Override
            public void onFailed() {
                netWorkErr();
            }
        });

	}



//	@SuppressLint("NewApi")
//	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
//
//		// Called when the action mode is created; startActionMode() was called
//		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//			// inflate a menu resource providing context menu items
//			MenuInflater inflater = mode.getMenuInflater();
//			// assumes that you have "contexual.xml" menu resources
//			inflater.inflate(R.menu.action_del, menu);
//			return true;
//		}
//
//		// called each time the action mode is shown. Always called after
//		// onCreateActionMode, but
//		// may be called multiple times if the mode is invalidated.
//		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//			return false; // Return false if nothing is done
//		}
//
//		// called when the user selects a contextual menu item
//		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//			return false;
//			// switch (item.getItemId()) {
//			// // case R.id.toast:
//			// // Toast.makeText(MainActivity.this, "Selected menu",
//			// // Toast.LENGTH_LONG).show();
//			// // mode.finish(); // Action picked, so close the CAB
//			// return true;
//			// default:
//			// return false;
//			// }
//		}
//
//		// called when the user exits the action mode
//		public void onDestroyActionMode(ActionMode mode) {
//			// mActionMode = null;
//		}
//	};

    @Override
    public void OnItemClick(int position) {
         FavBean item = mAdapter.getData().get(position);
        MsgBaseBean bean = new MsgBaseBean();
        bean.setTitle(item.getStrUp());
        bean.setId(item.getId());
        Bundle bundle = new Bundle();
        bundle.putSerializable("id", bean.getId());
        if (linke_type == ODERSHOW_PARAM) {//晒单
            bundle.putSerializable("isShowOrder", true);
            IntentUtil.start_activity(getActivity(),
                    OrderShowDetailActivity.class, bundle);
        } else if (linke_type == COUPON_PARAM) {

            bundle.putSerializable("id", bean.getId());
            IntentUtil.start_activity(getActivity(),
                    OrderShowDetailActivity.class, bundle);
        } else {
            IntentUtil.start_activity(getActivity(), MsgDetailActivity.class,
                    bundle);
        }
    }
}
