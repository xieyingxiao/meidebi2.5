package com.meidebi.app.ui.main;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meidebi.app.R;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.detail.CouponBean;
import com.meidebi.app.service.bean.detail.CouponListJson;
import com.meidebi.app.service.bean.detail.CouponSingleBean;
import com.meidebi.app.service.dao.CouponDao;
import com.meidebi.app.support.lib.MyAsyncTask;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.ui.adapter.CouponListAdapter;
import com.meidebi.app.ui.base.BaseFragmentActivity;
import com.meidebi.app.ui.base.BaseRecycleViewFragment;
import com.meidebi.app.ui.msgdetail.CouponDetailActivity;
import com.meidebi.app.ui.widget.LoadStatus;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class CouponListFragment extends
        BaseRecycleViewFragment<CouponBean> {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
	private List<CouponBean> items_list = new ArrayList<CouponBean>();
	private List<CouponSingleBean> siteBeans;

	private CouponDao dao;
//    @InjectView(R.id.lv_coupon_selction)
//    RecyclerView selection_lv;
	private String str_last_selection = "";
//	private CouponSiteListAdapter couponSiteAdapter;
	private boolean isScrolling = false;
	private int selection_position = 0;

	public CouponDao getDao() {
		if (dao == null) {
			dao = new CouponDao(getActivity());
		}
		return dao;
	}


	public CouponListFragment() {
 		layout_res = R.layout.couponlistview_fragment;
 	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = super.onCreateView(inflater, null, savedInstanceState);// sld
        toolbar.setTitle(getString(R.string.tab_coupon));
        ((BaseFragmentActivity)getActivity()).setSupportActionBar(toolbar);
        mAdapter = new CouponListAdapter( getActivity(),
				items_list);
		mAdapter.setData(items_list);
        mAdapter.setOnItemClickListener(this);
		getListView().setAdapter(mAdapter);
//        couponSiteAdapter = new CouponSiteListAdapter(this, siteBeans);
//        selection_lv.setLayoutManager(new LinearLayoutManager(getActivity()));
//        selection_lv.setItemAnimator(new DefaultItemAnimator());
//		selection_lv.setAdapter(couponSiteAdapter);
        if(mAdapter.getData()!=null&&mAdapter.getData().size()==0){
            onStartRefresh();
        }
		return view;
	}

	@Override
	protected void getData(int type) {
		// CouponListJson json = null;
		// getDao().setPage(mPage);
		// getDao().setLimit(12);
		// json = getDao().mapperJson();
		// Message message = new Message();
		// if (json != null) {
		// // list_data = BuildData(json.getData());
		// if (json.getData() != null) {
		// items_list = (json.getData());
		// setItems_list(items_list);
		// message.what = type;
		// } else {
		// message.what = GET_EMPTY_DATA;
		// }
		// mHandler.sendMessage(message);
		// } else {
		// message.what = NETWROKERROR;
		// mHandler.sendMessage(message);
		// }
	}

	@Override
	public void loadData() {
        getDao().getResult(new RestHttpUtils.RestHttpHandler<ListJson>() {
            @Override
            public void onSuccess(ListJson result) {
                onLoad();
                 if (result.getData() == null) {

                     mViewLoading.setState(LoadStatus.err);
                } else {
                    // onStartRefresh();
                     mViewLoading.onDone();
                    siteBeans = result.getData();
                    rebuildCoponList(siteBeans);
                    mAdapter.setData(getItems_list());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onStart() {
               mPullToRefreshLayout.setRefreshing(true);

            }

            @Override
            public void onFailed() {
                mViewLoading.onFaied();
             }
        });
 	}



	class GetSiteTask extends MyAsyncTask<Void, Void, List<CouponSingleBean>> {
		@Override
		protected void onPostExecute(List<CouponSingleBean> Result) {
            onLoad();
            if (Result == null) {
                mViewLoading.setState(LoadStatus.err);
			} else {
				// onStartRefresh();
				siteBeans = Result;
				rebuildCoponList(Result);
				mAdapter.setData(getItems_list());
				mAdapter.notifyDataSetChanged();
//				couponSiteAdapter.setData(siteBeans);
//				couponSiteAdapter.notifyDataSetChanged();
//                couponSiteAdapter.setOnItemClickListener(new InterRecyclerOnItemClick() {
//                    @Override
//                    public void OnItemClick(int position) {
//                        String nowselction = siteBeans.get(position).getSitename();
//                        int selction = filterCouponListSelectionPosition(nowselction);
//                        ListViewUtils.smoothScrollListView(getListView(), selction);
//                        couponSiteAdapter.setSelect_position(position);
//                    }
//
//                    @Override
//                    public void OnFoooterClick() {
//
//                    }
//                });
//				selection_lv.setOnItemClickListener(new OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> arg0, View arg1,
//							int arg2, long arg3) {
//						// TODO Auto-generated method stub
//						isScrolling = true;
//						String nowselction = siteBeans.get(arg2).getSitename();
//						int selction = filterCouponListSelectionPosition(nowselction);
//						selection_lv.setItemChecked(arg2, true);
//						ListViewUtils.smoothScrollListViewToTop(getListView());
//						str_last_selection = nowselction;
//						couponSiteAdapter.notifyDataSetChanged();
//						selection_position = selction;
////						getListView().setItemChecked(selction, true);
//						mAdapter.notifyDataSetChanged();
//						new Handler().postDelayed(new Runnable() {
//							@Override
//							public void run() {
//								setOnScrollListener();
//							}
//						}, 1000);
//					}
//				});
			}
		}

		@Override
		protected List<CouponSingleBean> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			mPage = 1;
			CouponListJson json = getDao().getSites();
			if (json != null) {
				return json.getData();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
            mPullToRefreshLayout.post(new Runnable() {
                @Override public void run() {
                    mPullToRefreshLayout.setRefreshing(true);
                }
            });
			super.onPreExecute();
		}

	}

	private int filterSiteListSelectionPosition(String str) {
		for (int i = 0; i < siteBeans.size(); i++) {
			if (siteBeans.get(i).getSitename().equals(str)) {
				return i;
			}
		}
		return 0;
	}

	private int filterCouponListSelectionPosition(String str) {
		for (int i = 0; i < mAdapter.getData().size(); i++) {
			if (mAdapter.getData().get(i).getSitename().equals(str)) {
				return i;
			}
		}
		return 0;
	}

//	@Override
//	public void onViewScroll(AbsListView view, int firstVisibleItem,
//			int visibleItemCount, int totalItemCount) {
//		if (mAdapter != null) {
//			if (mAdapter.getData().size() > 0
//					&& firstVisibleItem < mAdapter.getItemCount()
//					&& !isScrolling
//					&& getScrollState() != AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//				String nowselction = mAdapter.getData().get(firstVisibleItem)
//						.getSitename();
//				if (!str_last_selection.equals(nowselction)) {
//					int selction = filterSiteListSelectionPosition(nowselction);
//					selection_lv.setItemChecked(selction, true);
//					ListViewUtils.smoothScrollListView(selection_lv, selction);
//					str_last_selection = nowselction;
//					couponSiteAdapter.notifyDataSetChanged();
//
//				}
//			} else if (!isScrolling
//					&& getScrollState() == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
////				getListView().setItemChecked(0, true);
//				mAdapter.notifyDataSetChanged();
//				isScrolling = false;
//			} else {
//				isScrolling = false;
//			}
//		}
//	}



	private void rebuildCoponList(List<CouponSingleBean> result) {
		items_list.clear();
		for (int i = 0; i < result.size(); i++) {
			CouponSingleBean single = result.get(i);
			if (single != null) {
				for (int j = 0; j < single.getData().size(); j++) {
					CouponBean couponBean = single.getData().get(j);
					couponBean.setImgUrl(single.getLogo1());
					couponBean.setSitename(single.getSitename());
					items_list.add(couponBean);
				}
			}
		}
		setItems_list(items_list);
	}

    @Override
    public void onReload() {
        super.onReload();
        loadData();
    }

    @Override
    public void OnItemClick(int position) {
        super.OnItemClick(position);

        IntentUtil.start_activity(getActivity(),
                CouponDetailActivity.class,new BasicNameValuePair("id",mAdapter.getData().get(position).getId()));
    }
}