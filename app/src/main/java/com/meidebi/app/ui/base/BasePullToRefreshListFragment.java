package com.meidebi.app.ui.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.support.utils.ListViewUtils;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.ui.adapter.base.BaseArrayAdapter;
import com.meidebi.app.ui.widget.ListViewLoadingFooter;
import com.meidebi.app.ui.widget.LoadStatus;
import com.meidebi.app.ui.widget.ViewLoading;
import com.meidebi.app.ui.widget.action.ILoadingAction;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

@SuppressLint("NewApi")
public abstract class BasePullToRefreshListFragment<T> extends Fragment
		implements SwipeRefreshLayout.OnRefreshListener, ILoadingAction {

    protected List<T> items_list = new ArrayList<T>();
	protected BaseArrayAdapter<T> mAdapter;
	protected int mPage;
	private final int GET_NEW_DATA = 1;// 刷新
    protected final int GET_MORE_DATA = 2;// 下一页
	protected final int GET_EMPTY_DATA = 3;// 空数据
	private final int GET_DB_DATA = 100;// 空数据
	protected final int NETWROKERROR = 4;// 空数据
	protected int layout_res = R.layout.common_ptr_listview;
	private ListViewLoadingFooter mLoadingFooter;
	protected ViewLoading mViewLoading;
	private Boolean firstLoad = false;
	public Boolean isError = false;
	boolean isRefreshFromTop;

	private Boolean isLoadCompelte = false;
	private volatile boolean enableRefreshTime = true;
 	private LoadStatus status = LoadStatus.TheEnd;
	protected Boolean enbleDBDATA = false;
	private int ScrollState;
 	private Boolean isLoading = false;
    private Boolean enableLoadMore = true;

	private long data_insert_time;

	public long getData_insert_time() {
		return data_insert_time;
	}

	public void setData_insert_time(long data_insert_time) {
		this.data_insert_time = data_insert_time;
	}

	public Boolean getEnableLoadMore() {
		return enableLoadMore;
	}

	public void setEnableLoadMore(Boolean enableLoadMore) {
		this.enableLoadMore = enableLoadMore;
	}

	public int getScrollState() {
		return ScrollState;
	}

	public void setScrollState(int scrollState) {
		ScrollState = scrollState;
	}

	public Boolean getIsLoadCompelte() {
		return isLoadCompelte;
	}

	public void setIsLoadCompelte(Boolean isLoadCompelte) {
		this.isLoadCompelte = isLoadCompelte;
	}

	public List<T> getItems_list() {
		return items_list;
	}

	public void setItems_list(List<T> items_list) {
		this.items_list = items_list;
	}

    @InjectView(R.id.ptr_layout)protected SwipeRefreshLayout mPullToRefreshLayout;
   @InjectView(R.id.common_list_view) ListView pullToRefreshListView;
    @InjectView(R.id.iv_srcoll_top)ImageView iv_scroll_top;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(layout_res, container, false);
        ButterKnife.inject(this,view);
		iv_scroll_top.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ListViewUtils.smoothScrollListViewToTop(getListView());
			}
		});
		initViewLoading(view);
		// initLoadLayout(view);
		if (!getIsLoadCompelte()) {
			pullToRefreshListView.setVisibility(View.GONE);
		} else {
			pullToRefreshListView.setVisibility(View.VISIBLE);
		}
		initPullToRefreshAttcher(view);
		InitListView(inflater);
		return view;
	}

	protected void initViewLoading(View view) {
		mViewLoading = new ViewLoading(view);
		mViewLoading.setState(LoadStatus.Idle);
		mViewLoading
				.setRes(R.drawable.ic_comment_empty, XApplication.getInstance()
						.getResources().getString(R.string.comment_empty));
	}

	public void setEmptyView(int imgres, int textres) {
		mViewLoading.setRes(imgres, XApplication.getInstance().getResources()
				.getString(textres));
	}

	public void setEmptyView(int imgres, String textres) {
		mViewLoading.setRes(imgres, textres);
	}

	public void initPullToRefreshAttcher(View view) {

		onCreateSwipeToRefresh(mPullToRefreshLayout);
		// onCreateSwipeToRefresh(mPullEmptyLayout);
	}

	private void onCreateSwipeToRefresh(SwipeRefreshLayout refreshLayout) {
		refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.titlebar_bg);
//		refreshLayout.setColorScheme(R.color.progress1, R.color.progress2,
//				R.color.progress1, R.color.progress2);

	}

	public void onStartRefresh() {
		ListViewUtils.smoothScrollListViewToTop(getListView());
		if (enbleDBDATA) {
			getDBData();
		} else {
			loadFirstPage();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public void openActivity(Class<?> pClass) {
		IntentUtil.start_activity(getActivity(), pClass);
	}

	/**
	 * listview加载完数据
	 */
	public void onLoad() {
		getListView().setVisibility(View.VISIBLE);
		setIsLoadCompelte(true);
		if (mPage > 2) {
			iv_scroll_top.setVisibility(View.VISIBLE);
		} else {
			iv_scroll_top.setVisibility(View.GONE);
		}
		mViewLoading.setState(LoadStatus.Idle);
		mLoadingFooter.setState(LoadStatus.Idle);
		mPullToRefreshLayout.setRefreshing(false);
		isLoading = false;
	}

	public void loadData() { // TODO Auto-generated method stub
        mPullToRefreshLayout.post(new Runnable() {
            @Override public void run() {
                mPullToRefreshLayout.setRefreshing(true);
             }
        });
		new Thread() {
			@Override
			public void run() {
				mPage = 1;
				getData(GET_NEW_DATA);
			}
		}.start();
	}

	/**
	 * 实现加载下一页接口
	 */
	public void onLoadMore() {
		mLoadingFooter.setState(LoadStatus.Loading);
		// sendActionBroadCast(AppAction.DOREFRESH, true);
		new Thread() {
			@Override
			public void run() {
				mPage = mPage + 1;
				getData(GET_MORE_DATA);
			}
		}.start();
	}

	public void loadCurrentData() {
		new Thread() {
			@Override
			public void run() {
				getData(GET_MORE_DATA);
			}
		}.start();
	}

	/**
	 * 数据回调处理
	 */
	protected Handler mHandler = new Handler() {
		@SuppressLint("ValidFragment")
		public void handleMessage(Message msg) {
			onLoad();
			switch (msg.what) {
			case GET_NEW_DATA:// 刷新
				mAdapter.setData(getItems_list());
				mAdapter.notifyDataSetChanged();
				doSomething();

                break;
			case GET_MORE_DATA:
				isError = false;
				mAdapter.addAllItem(getItems_list());
				break;
			case GET_EMPTY_DATA:
				isError = false;
				if (mPage == 1) {
					mViewLoading.setState(LoadStatus.err);
					mLoadingFooter.setState(LoadStatus.TheEnd);
				} else {
					mLoadingFooter.setState(LoadStatus.err);
					mLoadingFooter.setRes(XApplication.getInstance()
							.getResources()
							.getString(R.string.xlistview_footer_hint_empty));
				}
				break;
			case GET_DB_DATA:
				break;
			case NETWROKERROR:
				isError = true;
				mLoadingFooter.setState(LoadStatus.err);
				mLoadingFooter.setRes(XApplication.getInstance().getResources()
						.getString(R.string.pull_listview_err));
				break;
			}
			status = mLoadingFooter.getState();
		};
	};

	protected abstract void getData(int type);

	public void InitListView(LayoutInflater inflater) {
		mLoadingFooter = new ListViewLoadingFooter(getActivity());
		getListView().setFastScrollEnabled(false);
		mLoadingFooter.setAction(this);
		mLoadingFooter.setState(status);
		getListView().addFooterView(mLoadingFooter.getView());
		setOnScrollListener();
		setOnItemOnClickListener();
	}

	public void setOnItemOnClickListener() {
		getListView().setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
 						if (position < mAdapter.getCount() && position >= 0) {
							int index = position;
							jumpToNext(mAdapter.getData().get(index));
						}
					}
				});
	}

	protected boolean pauseOnScroll = true;
	protected boolean pauseOnFling = true;

	public void setOnScrollListener() {
             getListView().setOnScrollListener(
                    new PauseOnScrollListener(ImageLoader.getInstance(),
                            pauseOnScroll, pauseOnFling, new OnScrollListener() {

                        @Override
                        public void onScrollStateChanged(AbsListView view,
                                                         int scrollState) {
                            setScrollState(scrollState);
                            onViewScrollStateChanged(scrollState);
                        }

                        @Override
                        public void onScroll(AbsListView view,
                                             int firstVisibleItem, int visibleItemCount,
                                             int totalItemCount) {
                            onViewScroll(view, firstVisibleItem,
                                    visibleItemCount, totalItemCount);
                            if (enableLoadMore) {
                                if (mLoadingFooter.getState() == LoadStatus.Loading
                                        || mLoadingFooter.getState() == LoadStatus.err) {
                                    return;
                                }
                                if (firstVisibleItem + visibleItemCount >= totalItemCount
                                        && totalItemCount != 0
                                        && totalItemCount != getListView()
                                        .getHeaderViewsCount()
                                        + getListView()
                                        .getFooterViewsCount()
                                        && mAdapter.getCount() > 0) {
                                    onLoadMore();
                                }
                            }
                        }
                    }));




	}




//    public void setQuickReturnListener(){
//          int headerHeight = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);
//        View headView = getActivity().getLayoutInflater().inflate(R.layout.listview_header_layout,null);
//
//        getListView().addHeaderView(headView);
//        QuickReturnListViewOnScrollListener scrollListener = new QuickReturnListViewOnScrollListener(QuickReturnType.TWITTER,
//                mCoordinator.getToolBar(), headerHeight, null, 0)
//        {
//            @Override
//            public void onScrollStateChanged(AbsListView view,
//                                             int scrollState) {
//                super.onScrollStateChanged(view,scrollState);
//                setScrollState(scrollState);
//                onViewScrollStateChanged(scrollState);
//            }
//
//            @Override
//            public void onScroll(AbsListView view,
//                                 int firstVisibleItem, int visibleItemCount,
//                                 int totalItemCount) {
//                super.onScroll(view,
//                firstVisibleItem, visibleItemCount,
//                totalItemCount);
//                onViewScroll(view, firstVisibleItem,
//                        visibleItemCount, totalItemCount);
//                if (enableLoadMore) {
//                    if (mLoadingFooter.getState() == LoadStatus.Loading
//
//                            || mLoadingFooter.getState() == LoadStatus.err) {
//                        return;
//                    }
//                    if (firstVisibleItem + visibleItemCount >= totalItemCount
//                            && totalItemCount != 0
//                            && totalItemCount != getListView()
//                            .getHeaderViewsCount()
//                            + getListView()
//                            .getFooterViewsCount()
//                            && mAdapter.getCount() > 0) {
//                        onLoadMore();
//                    }
//                }
//            }
//        };
//        scrollListener.setCanSlideInIdleScrollState(true);
//
////          getListView().setOnScrollListener(scrollListener);
//        getListView().setOnScrollListener(
//                new PauseOnScrollListener(ImageLoader.getInstance(),
//                        pauseOnScroll, pauseOnFling,scrollListener));
//    }

	public void onViewScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	public void onViewScrollStateChanged(int scrollState) {

	}

	public boolean isListViewFling() {
		return !enableRefreshTime;
	}

	public ListView getListView() {
		return pullToRefreshListView;
	}

	public abstract void jumpToNext(T item);

	public void sendActionBroadCast(String action, Boolean isRefresh) {
		Intent intent = new Intent(action);
		intent.putExtra("doRefresh", isRefresh);
		XApplication.getInstance().sendOrderedBroadcast(intent, null);
		intent.setAction(action);
		LocalBroadcastManager.getInstance(XApplication.getInstance())
				.sendBroadcast(intent);
	}

	public void onRefresh() {
 			loadFirstPage();


	}

	public void loadFirstPage() {		loadData();
	}

	@Override
	public void onReload() {
		if (isError) {
			loadFirstPage();
		} else {
			onLoadMore();
		}
	}

	protected void getDBData() {
	};

	public void doSomething() {

	}

	public void handler(Message message) {

	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


}
