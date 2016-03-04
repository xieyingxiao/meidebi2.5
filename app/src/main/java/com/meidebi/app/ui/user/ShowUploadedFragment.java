package com.meidebi.app.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meidebi.app.R;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.msg.OrderShowBean;
import com.meidebi.app.service.dao.user.UserOrderShowDao;
import com.meidebi.app.support.component.bus.MainThreadBusProvider;
import com.meidebi.app.support.component.bus.UploadSucessEvent;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.ui.adapter.MyShowAdapter;
import com.meidebi.app.ui.commonactivity.BackPressListFragment;
import com.meidebi.app.ui.msgdetail.OrderShowDetailActivity;
import com.meidebi.app.ui.widget.DividerItemDecoration;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by mdb-ii on 15-1-9.
 */
public class ShowUploadedFragment extends BackPressListFragment<OrderShowBean> {

     private UserOrderShowDao dao;
     public UserOrderShowDao getDao() {
        if (dao == null) {
            dao = new UserOrderShowDao();
        }
        return dao;
    }



    @Override
    public void onStart() {
        super.onStart();
      }


    @Override
    public void onResume() {
        MainThreadBusProvider.getInstance().register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        MainThreadBusProvider.getInstance().unregister(this);
        super.onPause();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = super.onCreateView(inflater, null, savedInstanceState);// sld
        setTitle("我的晒单");
        mAdapter = new MyShowAdapter(getActivity(), items_list);
         mAdapter.setData(items_list);
        mAdapter.setOnItemClickListener(this);
        getListView().setAdapter(mAdapter);
        getListView().addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        onStartRefresh();
        setEmptyView(R.drawable.ic_search_result_empty, "亲,你还没晒过单哦");

        return view;
    }





    @Override
    public void onBackPress() {

    }

    @Override
    protected void getData(final int type) {
         getDao().setPage(mPage);
        getDao().getResult(new RestHttpUtils.RestHttpHandler<ListJson>() {
            @Override
            public void onSuccess(ListJson result) {
                OnCallBack(type,  result.getData());
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

//    @Override
//    public void replaceSameData(int type) {
//        if (type == GET_MORE_DATA) {
//            Iterator<OrderShowBean> Ite_new = items_list.iterator();
//            while (Ite_new.hasNext()) {
//                OrderShowBean new_odb = Ite_new.next();
//                if (type == GET_MORE_DATA) {
//                    Iterator<OrderShowBean> Ite_old = mAdapter.getData().iterator();
//                    while (Ite_old.hasNext()) {
//                        OrderShowBean old_data = Ite_old.next();
//                        if (new_odb.getId().equals(old_data.getId())) {
//                            AppLogger.e("remove " + new_odb.getTitle());
//                            Ite_new.remove();
//                        }
//                    }
//                }
//            }
//
//        }
//    }



    @Override
    public void OnItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", mAdapter.getData().get(position));
        IntentUtil.start_activity(getActivity(), OrderShowDetailActivity.class,
                bundle);
        AppLogger.e("uploadnum");
    }


//    @Override
//    public void getNewData(int type,List<OrderShowBean> data){
//         if (data != null&&data.size() > 0) {
//
//            setItems_list(data);
//             refreshCompelte();
//        }else{
//            getEmptyData();
//        }
//    }

    @Subscribe
    public void onUploadSucess(UploadSucessEvent event){
        if(mAdapter!=null) {
            mAdapter.notifyDataSetChanged();
        }
     }


}