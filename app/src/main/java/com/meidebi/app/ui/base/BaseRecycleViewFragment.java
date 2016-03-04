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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.ListViewUtils;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;
import com.meidebi.app.ui.adapter.base.InterRecyclerOnItemClick;
import com.meidebi.app.ui.widget.LoadStatus;
import com.meidebi.app.ui.widget.ViewLoading;
import com.meidebi.app.ui.widget.action.ILoadingAction;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

@SuppressLint("NewApi")
public abstract class BaseRecycleViewFragment<T> extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, ILoadingAction, InterRecyclerOnItemClick {

    protected List<T> items_list = new ArrayList<T>();
    protected BaseRecyclerAdapter<T> mAdapter;
    protected int mPage;
    private final int GET_NEW_DATA = 1;// 刷新
    protected final int GET_MORE_DATA = 2;// 下一页
    protected final int GET_EMPTY_DATA = 3;//
    private final int GET_DB_DATA = 100;// 空数据
    protected final int NETWROKERROR = 4;// 空数据
    protected int layout_res = R.layout.common_swipe_recycleview;
    protected ViewLoading mViewLoading;
    private Boolean firstLoad = false;

    private Boolean isLoadCompelte = false;
    private volatile boolean enableRefreshTime = true;
    private int lastKnownFirst;
    protected Boolean enbleDBDATA = false;
    private int ScrollState;
     private long data_insert_time;
    public @InjectView(R.id.ptr_layout)  SwipeRefreshLayout mPullToRefreshLayout;
    @InjectView(R.id.common_recyclerview)
    ObservableRecyclerView pullToRefreshListView;

    public long getData_insert_time() {
        return data_insert_time;
    }

    public void setData_insert_time(long data_insert_time) {
        this.data_insert_time = data_insert_time;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(layout_res, container, false);
        ButterKnife.inject(this,view);
        pullToRefreshListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pullToRefreshListView.setItemAnimator(new DefaultItemAnimator());

        // initLoadLayout(view);
        if (mAdapter!=null&&mAdapter.getData()!=null&&mAdapter.getData().size()>0) {
            pullToRefreshListView.setVisibility(View.VISIBLE);

        } else {
            pullToRefreshListView.setVisibility(View.GONE);

        }
        initViewLoading(view);

        initPullToRefreshAttcher(view);
        InitListView();
         View toolbar = view.findViewById(R.id.toolbar);
        if(toolbar!=null) {
            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.common_recyclerview);
                    ListViewUtils.smoothScrollListViewToTop(recyclerView);
                }
            });
        }
        else if(getParentFragment()!=null&&getParentFragment().getView().findViewById(R.id.toolbar)!=null){

        }else if( getActivity().findViewById(R.id.toolbar)!=null){
                AppLogger.e("getActivity");

            toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.common_recyclerview);
                    ListViewUtils.smoothScrollListViewToTop(recyclerView);
                }
            });
        }


         return view;
    }

    protected void initViewLoading(View view) {
     if(mAdapter==null||mAdapter.getData()!=null&&mAdapter.getData().size()==0) {

         mViewLoading = new ViewLoading(view);
         mViewLoading.toAttchView(getListView());
         mViewLoading.setAction(this);
         mViewLoading
                 .setRes(R.drawable.ic_comment_empty, XApplication.getInstance()
                         .getResources().getString(R.string.comment_empty));
         mViewLoading.onLoad();

     }

    }

    public void setEmptyView(int imgres, int textres) {
        mViewLoading.setRes(imgres, XApplication.getInstance().getResources()
                .getString(textres));
    }

    public void setEmptyView(int imgres, String textres) {
        mViewLoading.setRes(imgres, textres);
    }

    public void initPullToRefreshAttcher(View view) {

        // Now give the find the PullToRefreshLayout and set it up

        onCreateSwipeToRefresh(mPullToRefreshLayout);
     }

    private void onCreateSwipeToRefresh(SwipeRefreshLayout refreshLayout) {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.app_main_color);


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
         setIsLoadCompelte(true);
 //        mLoadingFooter.setState(LoadStatus.Idle);
        mPullToRefreshLayout.setRefreshing(false);
     }

    public void loadData() { // TODO Auto-generated method stub
         mPage = 1;
         getData(GET_NEW_DATA);

    }

    /**
     * 实现加载下一页接口
     */
    public void onLoadMore() {
//        mLoadingFooter.setState(LoadStatus.Loading);
        // sendActionBroadCast(AppAction.DOREFRESH, true);
        mAdapter.setState(LoadStatus.Loading);
        mPage = mPage + 1;
        getData(GET_MORE_DATA);
    }

    public void loadCurrentData() {
        getData(GET_MORE_DATA);
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
                    if(getItems_list().size()<10) {
                        mAdapter.setState(LoadStatus.TheEnd);
                    }else {
                        mAdapter.setState(LoadStatus.Idle);
                    }
                    mAdapter.setData(getItems_list());
                    mAdapter.notifyDataSetChanged();
                     doSomething();
                    break;
                case GET_MORE_DATA:
                     setLoadingStatus(LoadStatus.Idle);
                    mAdapter.addAllItem(getItems_list());
                    break;
                case GET_EMPTY_DATA:
                     setLoadingStatus(LoadStatus.TheEnd);
                    break;
                case GET_DB_DATA:
                    break;
                case NETWROKERROR:
                     if(mPage==1){
                        mPage=0;
                    }
                    setLoadingStatus(LoadStatus.err);
                    break;
            }
         }
    };

    public void refreshCompelte(){
        mAdapter.setData(getItems_list());
        mAdapter.notifyDataSetChanged();
        doSomething();
    }

    public void getMoreCompelte(final List<T> data){
        mAdapter.setState(LoadStatus.Idle);
        mAdapter.addAllItem(data);
      }

    public void getEmptyData(){
        if(mPage==1) {
            mViewLoading.onEmpty();
         }else{
            setLoadingStatus(LoadStatus.TheEnd);
        }
    }

    public void netWorkErr(){
        onLoad();
        setLoadingStatus(LoadStatus.err);

        if(mPage==1){
            mPage=0;
            AppLogger.e("networkerr");
         }
        if(mAdapter.getData()!=null&&mAdapter.getData().size()==0){
            mViewLoading.onFaied();
        }
    }

    public void OnStart(int type) {
        if(type==GET_NEW_DATA){
//            mPullToRefreshLayout.post(new Runnable() {
//                @Override public void run() {
                    mPullToRefreshLayout.setRefreshing(true);
//                }
//            });

        }
    }

        public void getNewData(int type,List<T> data){
            if (data != null&&data.size() > 0) {
                if(data.size()<10) {
                    mAdapter.setState(LoadStatus.TheEnd);
                }else {
                    mAdapter.setState(LoadStatus.Idle);
                }
                setItems_list(data);
                replaceSameData(type);
                refreshCompelte();
            }else{
                getEmptyData();
            }
        }

        public void OnCallBack(int type,List<T> data){
        onLoad();
        switch (type){
        case GET_NEW_DATA:
             getNewData(type,data);
            if (data != null&&data.size() > 0) {
                 mViewLoading.onDone();
            }
            break;
        case GET_MORE_DATA:
            if (data != null&&data.size() > 0) {
                if(getItems_list().size()<10) {
                    mAdapter.setState(LoadStatus.TheEnd);
                }else {
                    mAdapter.setState(LoadStatus.Idle);
                }
//                 setItems_list(data);
                  getMoreCompelte(data);
            }else{
                getEmptyData();
            }
            break;
        case GET_DB_DATA:
            break;
        case NETWROKERROR:
            netWorkErr();
            break;
    }

    }

    public void replaceSameData(int type){

    }


    private void setLoadingStatus(LoadStatus state){
        if(state==mAdapter.getmState())
            return;
        mAdapter.setState(state);
        if(state!=LoadStatus.Idle){
            mAdapter.notifyDataSetChanged();
        }

    }

    protected abstract void getData(int type);

    public void InitListView() {
        setOnScrollListener();
        setOnItemOnClickListener();
    }

    public void setOnItemOnClickListener() {
//        getListView().setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view,
//                                            int position, long id) {
//                        if (position < mAdapter.getItemCount() && position >= 0) {
//                            int index = position;
//                            jumpToNext(mAdapter.getData().get(index));
//                        }
//                    }
//                });
    }

    protected boolean pauseOnScroll = true;
    protected boolean pauseOnFling = true;

    public void setOnScrollListener() {
        final LinearLayoutManager mLayoutManager = (LinearLayoutManager) pullToRefreshListView.getLayoutManager();
        getListView().setOnScrollListener(new RecyclerViewPauseOnScrollListener
                    (ImageLoader.getInstance(),pauseOnScroll, pauseOnFling, new RecyclerView.OnScrollListener(){

                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                            setScrollState(newState);
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            if (mAdapter.isUseLoadMore()) {
                                if (mAdapter.getmState()==LoadStatus.Loading||mAdapter.getmState()==LoadStatus.err|| dy <= 0) {
                                    return;
                                }
                                int lastVisibleItem =  mLayoutManager.findLastVisibleItemPosition();
                                int totalItemCount = mAdapter.getItemCount();
                                // dy>0 表示向下滑动
                                if (lastVisibleItem >= totalItemCount - 1 &&totalItemCount>0) {
                                    onLoadMore();
                                }
                            }
                        }
                    }));

    }




    public boolean isListViewFling() {
        return !enableRefreshTime;
    }

    public ObservableRecyclerView getListView() {
        return pullToRefreshListView;
    }


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
              loadFirstPage();

    }

    protected void getDBData() {
    }


    public void doSomething() {

    }

    public void handler(Message message) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
         RestHttpUtils.client.cancelRequests(getActivity(),true);
     }

    @Override
    public void OnItemClick(int position) {

    }

    @Override
    public void OnFoooterClick(int position) {

        onLoadMore();
    }

 
}
