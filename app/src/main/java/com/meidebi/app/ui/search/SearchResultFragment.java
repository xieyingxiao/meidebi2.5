package com.meidebi.app.ui.search;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meidebi.app.R;
import com.meidebi.app.service.bean.base.LinkListJson;
import com.meidebi.app.service.bean.msg.MsgDetailBean;
import com.meidebi.app.service.dao.SearchDao;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.ui.adapter.MainCardAdapter;
import com.meidebi.app.ui.base.BaseRecycleViewFragment;
import com.meidebi.app.ui.msgdetail.MsgDetailActivity;
import com.meidebi.app.ui.widget.DividerItemDecoration;

@SuppressLint("ValidFragment")
public class SearchResultFragment extends BaseRecycleViewFragment<MsgDetailBean> {
	private String keyWord;
	private SearchDao searchDao = new SearchDao();

	public SearchDao getSearchDao() {
		if (searchDao == null) {
			searchDao = new SearchDao();
		}
		return searchDao;
	}

	public SearchResultFragment(String keyWord) {
		this.keyWord = keyWord;
	}

	public SearchResultFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = super.onCreateView(inflater, null, savedInstanceState);// sld
		// pullToRefreshListView.setXLi2stViewListener(this);
		mAdapter = new MainCardAdapter(this, items_list);
        mAdapter.setOnItemClickListener(this);
		mAdapter.setData(items_list);
		((MainCardAdapter) mAdapter).setSearch(true);
		getListView().setAdapter(mAdapter);
        getListView().addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));

        setEmptyView(R.drawable.ic_search_result_empty, R.string.empty_search_result);
		if (!getIsLoadCompelte()) {
			onStartRefresh();
		} else {
			mAdapter.notifyDataSetChanged();
		}
		return view;
	}

	@Override
	protected void getData(final int type) {
		// TODO Auto-generated method stub
        getSearchDao().setKeyword(keyWord);
        getSearchDao().setPage(mPage);
        getSearchDao().getResult(new RestHttpUtils.RestHttpHandler<LinkListJson>() {
            @Override
            public void onSuccess(LinkListJson result) {
                OnCallBack(type,  result.getData().getLinklist());
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
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", mAdapter.getData().get(position));
        IntentUtil.start_activity(getActivity(), MsgDetailActivity.class,
                bundle);
    }


    public void refreshResult(String keyword) {
		this.keyWord = keyword;
 		onStartRefresh();
	}




}
