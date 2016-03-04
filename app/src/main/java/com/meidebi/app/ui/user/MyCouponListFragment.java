package com.meidebi.app.ui.user;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.meidebi.app.R;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.detail.CouponBean;
import com.meidebi.app.service.dao.CouponDao;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.ui.adapter.MyCouponListAdapter;
import com.meidebi.app.ui.commonactivity.BackPressListFragment;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class MyCouponListFragment extends BackPressListFragment<CouponBean> {
	private List<CouponBean> items_list = new ArrayList<CouponBean>();
	private CouponDao dao;
    private boolean isTimeout = false;
//
    @SuppressLint("ValidFragment")
    public MyCouponListFragment(boolean isTimeout){
        this.isTimeout = isTimeout;
    }


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = super.onCreateView(inflater, null, savedInstanceState);// sld
        setTitle(getString(R.string.my_coupon));
		mAdapter = new MyCouponListAdapter(getActivity(), items_list);
		mAdapter.setData(items_list);
        mAdapter.setOnItemClickListener(this);
		getListView().setAdapter(mAdapter);
		getListView().setVisibility(View.VISIBLE);
        setEmptyView(R.drawable.ic_search_result_empty, "亲,赶紧去领券吧");
		onStartRefresh();
		return view;
	}

    @Override
    public void onBackPress() {

    }

    @Override
	protected void getData(final int type) {
		// TODO Auto-generated method stub
        getDao().setPage(mPage);
        getDao().getMyCoupon(isTimeout, new RestHttpUtils.RestHttpHandler<ListJson>() {
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


    @Override
    public void OnItemClick(int position) {
        ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
        cmb.setText(mAdapter.getData().get(position).getCard());
        Toast.makeText(getActivity(), "券码已经复制到剪贴板",
                Toast.LENGTH_SHORT).show();
     }

    public CouponDao getDao() {
		if (dao == null) {
			dao = new CouponDao(getActivity());
		}
		return dao;
	}
}
