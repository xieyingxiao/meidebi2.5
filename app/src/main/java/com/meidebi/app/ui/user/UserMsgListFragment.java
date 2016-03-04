package com.meidebi.app.ui.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.meidebi.app.R;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.comment.CommentBean;
import com.meidebi.app.service.bean.user.MsgCenterBean;
import com.meidebi.app.service.dao.user.MsgCenterDao;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.ui.adapter.MsgCenterAdapter;
import com.meidebi.app.ui.commonactivity.BackPressListFragment;
import com.meidebi.app.ui.msgdetail.MsgDetailActivity;
import com.meidebi.app.ui.msgdetail.OrderShowDetailActivity;
import com.meidebi.app.ui.submit.CommentEditSubmitActivity;
import com.meidebi.app.ui.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class UserMsgListFragment extends BackPressListFragment<MsgCenterBean> implements
        OnClickListener, MaterialDialog.ListCallback {
    private List<MsgCenterBean> AllCommentList = new ArrayList<MsgCenterBean>();
    private MsgCenterDao dao;
    private String linkid;
    private final int msg_single = 1;
    private final int msg_shaidan = 2;
    private final int msg_quan = 3;
    private final int msg_gonggao = 4;
    private MsgCenterBean item = null;

    // private ViewCommentReply et_reply;
    public UserMsgListFragment(String linkid) {
        this.linkid = linkid;
    }

    public UserMsgListFragment() {
    }

    public MsgCenterDao getDao() {
        if (dao == null) {
            dao = new MsgCenterDao();
        }
        return dao;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = super.onCreateView(inflater, container, savedInstanceState);// sld
        setTitle(getString(R.string.my_msg));
        getListView().setVisibility(View.VISIBLE);
        mAdapter = new MsgCenterAdapter(getActivity(), AllCommentList);
        mAdapter.setData(AllCommentList);
        mAdapter.setOnItemClickListener(this);
        getListView().setAdapter(mAdapter);
        getListView().addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        setEmptyView(R.drawable.ic_search_result_empty, "亲,还没人给你留言哦");

        onStartRefresh();
        return view;
    }

    @Override
    protected void getData(final int type) {
        // TODO Auto-generated method stub
        getDao().setPage(mPage);
        getDao().getResult(new RestHttpUtils.RestHttpHandler<ListJson>() {
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

    private String getIsReadIDS(List<MsgCenterBean> list) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isIsread() == 0) {
                // if (contentList.get(i).getKey() != 0) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(list.get(i).getId());
                // }
            }
        }
        AppLogger.e(sb.toString());
        return sb.toString();
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_comment_send:
                break;

            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

    }


    public void commitUnreadMsg() {
        if (mAdapter != null) {
            HashMap<String, Boolean> unreadList = ((MsgCenterAdapter) mAdapter)
                    .getUnReadList();
            if (unreadList != null) {
                if (unreadList.size() > 0) {
                    final StringBuilder sb = new StringBuilder();
                    boolean first = true;
                    Iterator iter = unreadList.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        if (first) {
                            first = false;
                            sb.append(entry.getKey());
                        } else {
                            sb.append(",");
                            sb.append(entry.getKey());
                        }
                    }
                    if (!TextUtils.isEmpty(sb.toString())) {

                        getDao().setRead(sb.toString());

                    }
                }
            }
        }
    }

    @Override
    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
        mAdapter.notifyDataSetChanged();
        switch (i) {
            case 0:
                Intent it1 = new Intent();
                it1.putExtra("id", item.getMainid());
                if (item.getType() == msg_single) {
                    it1.setClass(getActivity(), MsgDetailActivity.class);
                } else if (item.getType() == msg_shaidan) {
                    it1.setClass(getActivity(), OrderShowDetailActivity.class);
                }
                startActivity(it1);
                getActivity().overridePendingTransition(R.anim.present_up,
                        R.anim.zoom_out_center);
                break;
            case 1:
                Intent it = new Intent();
                CommentBean bean = new CommentBean();
                bean.setContent(item.getCon());
                bean.setId(item.getCommentid());
                bean.setNickname(item.getRelatenickname());
                it.putExtra("bean", bean);
                it.putExtra("type", String.valueOf(item.getType()));
                it.putExtra("id", item.getMainid());
                it.setClass(getActivity(), CommentEditSubmitActivity.class);
                startActivity(it);
                getActivity().overridePendingTransition(R.anim.present_up,
                        R.anim.zoom_out_center);
                break;
            default:
                break;
        }
    }


    public void OnItemClick(int position) {
        MsgCenterBean item = mAdapter.getData().get(position);
        if (!TextUtils.isEmpty(item.getMainid())
                && !TextUtils.isEmpty(item.getCommentid())) {
            new MaterialDialog.Builder(getActivity())
                    .title("操作")
                    .items(R.array.MsgCenter)
                    .itemsCallback(this)
                    .show();
            this.item = item;
        } else {
            Toast.makeText(getActivity(), "该条消息无法回复", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    @Override
    public void onBackPress() {
        AppLogger.e("onbackpress");
        commitUnreadMsg();
    }
}
