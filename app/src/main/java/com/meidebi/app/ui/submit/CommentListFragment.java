package com.meidebi.app.ui.submit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.meidebi.app.R;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.comment.CommentBean;
import com.meidebi.app.service.dao.CommentDao;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.ui.adapter.CommentAdapter;
import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;
import com.meidebi.app.ui.base.BaseRecycleViewFragment;
import com.meidebi.app.ui.widget.DividerItemDecoration;

@SuppressLint("ValidFragment")
public class CommentListFragment extends
        BaseRecycleViewFragment<CommentBean> implements OnClickListener {
	// private List<CommentBean> AllCommentList = new ArrayList<CommentBean>();
	private CommentDao dao;
	private String linkid;
	private int linktype;

	// private ViewCommentReply et_reply;
	public CommentListFragment(String linkid, int linkType) {
		this.linkid = linkid;
		this.linktype = linkType;
	}

	public CommentListFragment() {
	}

	public CommentDao getDao() {
		if (dao == null) {
			dao = new CommentDao(getActivity());
		}
		return dao;
	}

	public void setDao(CommentDao dao) {
		this.dao = dao;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        setIsLoadCompelte(true);
 		View view = super.onCreateView(inflater, null, savedInstanceState);// sld
        mAdapter = new CommentAdapter(getActivity(), getItems_list());
		mAdapter.setData(getItems_list());
        mAdapter.setOnItemClickListener(this);
        getListView().addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        getListView().setAdapter(mAdapter);
        onStartRefresh();
		return view;
	}

	@Override
	protected void getData(final int type) {
		// TODO Auto-generated method stub

        getDao().getResult(linkid, mPage, linktype,new RestHttpUtils.RestHttpHandler<ListJson>() {
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


	private void showReplyActivity(String cid, int typeid, CommentBean bean) {
		if (LoginUtil.isAccountLogin(this.getActivity())) {
			Intent it = new Intent();
			it.putExtra("bean", bean);
			it.putExtra("type", String.valueOf(typeid));
			it.putExtra("id", cid);
			it.setClass(this.getActivity(), CommentEditSubmitActivity.class);
			startActivity(it);
			getActivity().overridePendingTransition(R.anim.present_up,
					R.anim.zoom_out_center);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_comment_send:
			showReplyActivity(linkid, linktype, null);
			break;

		default:
			break;
		}
	}

    @Override
    public void OnItemClick(int position) {
        showReplyActivity(linkid, linktype, mAdapter.getData().get(position));
    }


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// if (resultCode == getActivity().RESULT_OK) {
		// this.startRefresh();
		// }
	}

    public void replaceSameData(int type){
        if(type==GET_MORE_DATA) {
            for (int i = 0; i < getItems_list().size(); i++) {
                CommentBean newData = getItems_list().get(i);
                for (int j = 0; j < mAdapter.getData().size(); j++) {
                    CommentBean oldData = mAdapter.getData().get(j);
                    if (newData.getId().equals(oldData.getId())) {
                         getItems_list().remove(i);
                    }
                }
            }
        }
    }

}
