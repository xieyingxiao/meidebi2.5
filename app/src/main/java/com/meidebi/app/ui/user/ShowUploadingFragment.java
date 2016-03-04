package com.meidebi.app.ui.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.service.dao.user.UserOrderShowDao;
import com.meidebi.app.support.component.UploadImageService;
import com.meidebi.app.support.component.bus.MainThreadBusProvider;
import com.meidebi.app.support.component.bus.UploadProgressEvent;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.ui.adapter.UploadingAdapter;
import com.meidebi.app.ui.adapter.base.InterRecyclerOnItemClick;
import com.meidebi.app.ui.widget.DividerItemDecoration;
import com.squareup.otto.Subscribe;

/**
 * Created by mdb-ii on 15-2-26.
 */
public class ShowUploadingFragment extends Fragment implements InterRecyclerOnItemClick{

    private UserOrderShowDao dao;
    private UploadingAdapter mAdapter;
    public UserOrderShowDao getDao() {
        if (dao == null) {
            dao = new UserOrderShowDao();
        }
        return dao;
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
        View view = null;
         if (UploadImageService.instanced!=null&&UploadImageService.instanced.uploading_list.size()!=0) {
             view = inflater.inflate(R.layout.common_recyclerview, container, false);
           RecyclerView  upload_recyclerView = (RecyclerView)view.findViewById(R.id.common_recy);
            mAdapter = new UploadingAdapter(getActivity(), UploadImageService.instanced.uploading_list);
            mAdapter.setOnItemClick(this);
            upload_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            upload_recyclerView.setItemAnimator(new DefaultItemAnimator());
            upload_recyclerView.setAdapter(mAdapter);
             AppLogger.e("size"+UploadImageService.instanced.uploading_list.size());
            upload_recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        }else{
             view = inflater.inflate(R.layout.common_loading, null);
             view.findViewById(R.id.view_loading).setVisibility(View.VISIBLE);
             TextView tv_err= (TextView)view.findViewById(R.id.tv_load_err);
             tv_err.setVisibility(View.VISIBLE);
             tv_err.setText("无上传中的晒单");
         }
            return view;
    }








    @Override
    public void OnItemClick(int position) {
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("bean", mAdapter.getData().get(position));
//        IntentUtil.start_activity(getActivity(), OrderShowDetailActivity.class,
//                bundle);
        AppLogger.e("uploadnum");
    }

    @Override
    public void OnFoooterClick(int position) {

    }

    @Subscribe
    public void onUploadProgressChange(UploadProgressEvent event){
        if(mAdapter!=null) {
            mAdapter.notifyDataSetChanged();
            AppLogger.e("event" + event.progress);
        }
    }


}