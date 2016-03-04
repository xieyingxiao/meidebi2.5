package com.meidebi.app.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meidebi.app.R;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.msg.MsgDetailBean;
import com.meidebi.app.service.dao.user.UserNewsDao;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.ui.adapter.MyNewsAdapter;
import com.meidebi.app.ui.commonactivity.BackPressListFragment;
import com.meidebi.app.ui.msgdetail.MsgDetailActivity;
import com.meidebi.app.ui.widget.DividerItemDecoration;

import java.util.Iterator;

/**
 * Created by mdb-ii on 15-1-8.
 */
public class UserNewsFragment extends BackPressListFragment<MsgDetailBean> {

    private UserNewsDao dao;

    public UserNewsDao getDao() {
        if(dao==null){
            dao = new UserNewsDao();
        }
        return dao;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = super.onCreateView(inflater, null, savedInstanceState);// sld
        setTitle(getString(R.string.my_news));
        mAdapter = new MyNewsAdapter(getActivity(), items_list);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setData(items_list);
        getListView().addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        getListView().setAdapter(mAdapter);
        setEmptyView(R.drawable.ic_search_result_empty, "亲,你还没爆过料哦");

        onStartRefresh();

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
//        ListJson<MsgDetailBean> bean = null;
//        getDao().setPage(mPage);
//        bean = getDao().getResult();
//        // }
//        if (bean != null) {
//            items_list = bean.getData();
//            if (items_list != null) {
//                if (items_list.size() > 0) {
//                    setItems_list(items_list);
//                    replaceSameData(type);
//                    mHandler.sendEmptyMessage(type);
//                }
//            } else {
//                mHandler.sendEmptyMessage(GET_EMPTY_DATA);
//            }
//        } else {
//            mHandler.sendEmptyMessage(NETWROKERROR);
//        }
    }


    public void replaceSameData(int type){
        if(type==GET_MORE_DATA) {
            Iterator<MsgDetailBean> Ite_new = items_list.iterator();
            while (Ite_new.hasNext()) {
                MsgDetailBean new_odb = Ite_new.next();
                if (type == GET_MORE_DATA) {
                    Iterator<MsgDetailBean> Ite_old = mAdapter.getData().iterator();
                    while (Ite_old.hasNext()) {
                        MsgDetailBean old_data = Ite_old.next();
                        if (new_odb.getId().equals(old_data.getId())) {
                            AppLogger.e("remove " + new_odb.getTitle());
                            Ite_new.remove();
                        }
                    }
                }
            }

        }
    }



    @Override
    public void OnItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", mAdapter.getData().get(position));
        IntentUtil.start_activity(getActivity(), MsgDetailActivity.class,
                bundle);
    }
}
