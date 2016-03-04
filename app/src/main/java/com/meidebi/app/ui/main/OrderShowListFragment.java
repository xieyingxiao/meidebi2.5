package com.meidebi.app.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.meidebi.app.R;
import com.meidebi.app.service.bean.base.LinkListJson;
import com.meidebi.app.service.bean.msg.OrderShowBean;
import com.meidebi.app.service.dao.ShowOrderDao;
import com.meidebi.app.support.lib.MyAsyncTask;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.Utility;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.support.utils.content.ContentUtils;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.adapter.OrderShowRecylerAdapter;
import com.meidebi.app.ui.base.BaseFragmentActivity;
import com.meidebi.app.ui.base.BaseRecycleViewFragment;
import com.meidebi.app.ui.commonactivity.CommonFragmentActivity;
import com.meidebi.app.ui.search.SearchActivity;
import com.meidebi.app.ui.setting.SettingActivity;
import com.melnykov.fab.FloatingActionButton;
import com.orm.SugarRecord;

import org.apache.http.message.BasicNameValuePair;

import java.util.Iterator;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
@SuppressLint("ValidFragment")
public class OrderShowListFragment extends
        BaseRecycleViewFragment<OrderShowBean>   implements ObservableScrollViewCallbacks {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    private ShowOrderDao dao;
    private boolean isHot;
    private DBTask dbTask;
    private Menu mMenu;
    @InjectView(R.id.fab)
    FloatingActionButton mFab;


    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean isHot) {
        this.isHot = isHot;
    }

    public ShowOrderDao getDao() {
        if (dao == null) {
            dao = new ShowOrderDao(null);
        }
        return dao;
    }

    public OrderShowListFragment() {
        setHasOptionsMenu(true);
        enbleDBDATA = !SharePrefUtility.getAutoRefresh();
        layout_res = R.layout.fragment_main_show_order;
    }

    public OrderShowListFragment(boolean isHot) {
        setHasOptionsMenu(true);
        enbleDBDATA = !SharePrefUtility.getAutoRefresh();
        layout_res = R.layout.fragment_main_show_order;
        this.isHot = isHot;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = super.onCreateView(inflater, container, savedInstanceState);

        toolbar.setTitle("晒单");
        BaseFragmentActivity parentActivity = ((BaseFragmentActivity)getActivity());
        parentActivity.setSupportActionBar(toolbar);

        if(mAdapter==null) {
            mAdapter = new OrderShowRecylerAdapter(getActivity(), items_list, R.layout.card_showorder);
            mAdapter.setData(items_list);
            mAdapter.setOnItemClickListener(this);
        }
        View title = inflater
                .inflate(R.layout.toolbar_filter, null);
       (title.findViewById(R.id.toolbar_main_search)).setVisibility(View.GONE);
        ((RadioButton)title.findViewById(R.id.rb_toolbar_fliter_hot)).setChecked(true);
        ((RadioGroup) title.findViewById(R.id.rg_toolbar_fliter)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
               setHot(checkedId == R.id.rb_toolbar_fliter_hot);
                onStartRefresh();
            }
        });
        parentActivity.getSupportActionBar().setCustomView(title, new ActionBar.LayoutParams(Gravity.RIGHT));
        parentActivity.getSupportActionBar().setDisplayShowCustomEnabled(true);

        mFab.setVisibility(View.VISIBLE);
//        mFab.attachToRecyclerView(getListView());
         getListView().setAdapter(mAdapter);
        getListView().setScrollViewCallbacks(this);
        if(mAdapter.getData()!=null&&mAdapter.getData().size()==0){
            onStartRefresh();
        }

        return view;
    }


    @OnClick(R.id.fab)
    protected void onClick(){
        if(LoginUtil.isAccountLogin(getActivity())) {
            IntentUtil.start_activity(getActivity(), CommonFragmentActivity.class,
                    new BasicNameValuePair(CommonFragmentActivity.KEY, UploadShowOrderFragment.class.getName()));
        }
    }

    dealJsonTask jsontask;
    @Override
    protected void getData(final int type) {
         getDao().setPage(mPage);
          getDao().getResult(isHot,new RestHttpUtils.RestHttpHandler<LinkListJson>() {
           @Override
           public void onSuccess(LinkListJson result) {
               jsontask = new dealJsonTask();
               jsontask.setContent(result);
               jsontask.setType(type);
               jsontask.execute();
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


    @Override
    public void getDBData() {
        dbTask = new DBTask();
        dbTask.execute();
    }

    @Override
    public void onScrollChanged(int dy, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(com.github.ksoichiro.android.observablescrollview.ScrollState scrollState) {
        if (scrollState == com.github.ksoichiro.android.observablescrollview.ScrollState.UP) {
            mFab.hide(true);
        } else if (scrollState == com.github.ksoichiro.android.observablescrollview.ScrollState.DOWN) {
            mFab.show(true);
        }
    }


    class DBTask extends MyAsyncTask<Void, Void, List<OrderShowBean>> {
        @Override
        protected void onPostExecute(List<OrderShowBean> Result) {
            if (Result == null) {
                loadData();
            } else {
                onLoad();

                mAdapter.setData(getItems_list());
                getListView().setAdapter(mAdapter);
//
//                ((MainActivity) getActivity()).showToast(Result.get(0)
//                        .getINSERTTIME());
                setData_insert_time(Result.get(0)
                        .getINSERTTIME());
            }
        }

        @Override
        protected List<OrderShowBean> doInBackground(Void... params) {
            // TODO Auto-generated method stub
            mPage = 1;
            // if (SugarRecord.tabbleIsExist(MsgDetailBean.class)
            // && SugarRecord.tabbleIsExist(Chanel.class)) {
            int isall = isHot ? 1 : 0;
            String sql = "SELECT MDB.* ,C.INSERTTIME FROM OrderShowBean AS MDB LEFT JOIN Chanel AS C ON MDB.ID = C.DID"
                    + " WHERE C.M_CHANEL = '"
                    + "OrderShow"
                    + "'  AND C.IS_ALL = "
                    + isall
                    + " ORDER BY C.INSERTTIME ASC";
            List<OrderShowBean> _chanel = SugarRecord.sql(sql,
                    OrderShowBean.class);
            if (_chanel != null) {
                if (_chanel.size() != 0) {
                    for (int i = 0; i < _chanel.size(); i++) {
                        OrderShowBean odb = _chanel.get(i);
                        odb.setContent(ContentUtils.getTinyTextFromHtml(odb
                                .getContent()));
                    }
                    setItems_list(_chanel);

                    return _chanel;
                }
            }
            //
            // }
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            mPullToRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mPullToRefreshLayout.setRefreshing(true);
                }
            });
            super.onPreExecute();
        }

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Utility.cancelTasks(dbTask);
        Utility.cancelTasks(jsontask);
        super.onDestroy();
    }

    @Override
    public void doSomething() {
//        ((MainActivity) getActivity()).showNewestToast();
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                IntentUtil.start_activity(getActivity(), SettingActivity.class);
                return true;
            case R.id.search:
                IntentUtil.start_activity(getActivity(), SearchActivity.class);
                return true;
//
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeMenu(){
        // TODO Auto-generated method stub
        setHot(!isHot());
//        mMenu.findItem(R.id.choose).setTitle(isHot() ? "精华" : "最新");
        loadData();
    }

    class dealJsonTask extends MyAsyncTask<Void,Void,LinkListJson> {
        LinkListJson content;
        private int type;

        public void setContent(LinkListJson content) {
            this.content = content;
        }

        public void setType(int type) {
            this.type = type;
        }



        @Override
        protected void onPostExecute(LinkListJson result) {
            OnCallBack(type,  result.getData().getLinklist());

        }

        @Override
        protected LinkListJson doInBackground(Void... params) {
            // TODO Auto-generated method stub

            Iterator<OrderShowBean> Ite_new = content.getData().getLinklist().iterator();
            while (Ite_new.hasNext()) {
                OrderShowBean bean = Ite_new.next();
                bean.setContent(ContentUtils.getTextFromHtml(bean.getContent()));
            }

            return  content;
        }



    }

 }
