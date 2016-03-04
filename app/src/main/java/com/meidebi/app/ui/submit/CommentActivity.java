package com.meidebi.app.ui.submit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.meidebi.app.R;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.comment.CommentBean;
import com.meidebi.app.service.dao.CommentDao;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.support.utils.content.EmoPickerUtility;
import com.meidebi.app.support.utils.content.TimeUtil;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;
import com.meidebi.app.ui.widget.EmoPicker;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.hoang8f.widget.FButton;

public class CommentActivity extends BasePullToRefreshActivity {
    private String detail_id;
    //	private LinearLayout ll_comment_sumit;
    private CommentListFragment commentListFragment;
    private int type;
    @InjectView(R.id.et_comment_reply)
    EditText et_repply;
    @InjectView(R.id.emo_picker)
    EmoPicker smiley;
    @InjectView(R.id.container)
    View container;
    @InjectView(R.id.btn_comment_send)
    FButton btn_send;


    private String referid, toUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        detail_id = getIntent().getStringExtra("id");
        String linktype = getIntent().getStringExtra("linkType");//判断是否晒单
        if (!TextUtils.isEmpty(linktype)) {
            type = Integer.parseInt(linktype);

         }
          setActionBar("评论");
        commentListFragment = new CommentListFragment(detail_id, type);
        replaceFragment(R.id.common_fragment, commentListFragment);
        if (!LoginUtil.isAccountLogin()) {
            btn_send.setText("登录");
        }
        buildEmoPicker();




    }

    @OnClick({R.id.et_comment_reply, R.id.btn_comment_emo, R.id.btn_comment_send})
    protected void OnClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.btn_comment_emo:
                if (smiley.isShown()) {
                    hideSmileyPicker(true);
                } else {
                    showSmileyPicker(EmoPickerUtility
                            .isKeyBoardShow(CommentActivity.this));
                }
                break;
            case R.id.et_comment_reply:
                hideSmileyPicker(true);
                break;
            case R.id.btn_comment_send:
                if (LoginUtil
                        .isAccountLogin(CommentActivity.this)) {
                    if (!TimeUtil.isCommenttimeInLimit(detail_id)) {
                        String content = et_repply.getText().toString();

                        if (!TextUtils.isEmpty(content)) {

                            subComment(content);

                        } else {
                            et_repply.setError(getString(R.string.comment_conent_isnot_empty));
                            // Toast.makeText(
                            // CommentEditSubmit.this,
                            // getString(
                            // R.string.comment_conent_isnot_empty),
                            // Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        et_repply.setError("发送评论间隔不能超过30秒");
                    }
                }
                break;

            default:

                break;
        }
    }


    private void subComment(String content) {

        CommentDao.postComment(content, detail_id, String.valueOf(type), referid, toUserId, new RestHttpUtils.RestHttpHandler<CommonJson>() {
            @Override
            public void onSuccess(CommonJson result) {
                if (result.getStatus() == 1) {
                    dissmissDialog();
                    clearEditText();
                     Toast.makeText(CommentActivity.this,
                            getString(R.string.comment_sucess), Toast.LENGTH_SHORT)
                            .show();
                    TimeUtil.addCommentTimeMap(detail_id);
                    commentListFragment.onStartRefresh();
                } else {
                    showErr(result.getInfo());
                }
            }

            @Override
            public void onStart() {
                showLoading(R.string.hint_sumit);
            }

            @Override
            public void onFailed() {
                dissmissDialog();
            }
        });

    }


    @Override
    protected int getLayoutResource() {
        return R.layout.comment_fragment_activity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            commentListFragment.onStartRefresh();
            if (LoginUtil.isAccountLogin()) {
                btn_send.setText("发送");
            }
        }

    }


    private void buildEmoPicker() {
        smiley.setEditText(CommentActivity.this,
                ((LinearLayout) findViewById(R.id.root_layout)), et_repply);

    }

    private void showSmileyPicker(boolean showAnimation) {
        this.smiley.show(CommentActivity.this, showAnimation);
        lockContainerHeight(EmoPickerUtility
                .getAppContentHeight(CommentActivity.this));

    }

    public void hideSmileyPicker(boolean showKeyBoard) {
        if (this.smiley.isShown()) {
            if (showKeyBoard) {
                // this time softkeyboard is hidden
                LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) this.container
                        .getLayoutParams();
                localLayoutParams.height = smiley.getTop();
                localLayoutParams.weight = 0.0F;
                this.smiley.hide(CommentActivity.this);
                EmoPickerUtility.showKeyBoard(et_repply);
                et_repply.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        unlockContainerHeightDelayed();
                    }
                }, 200L);
            } else {
                this.smiley.hide(CommentActivity.this);
                unlockContainerHeightDelayed();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (smiley.isShown()) {
            hideSmileyPicker(false);
        } else {
            super.onBackPressed();
        }
    }

    private void lockContainerHeight(int paramInt) {
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) this.container
                .getLayoutParams();
        localLayoutParams.height = paramInt;
        localLayoutParams.weight = 0.0F;
    }

    public void unlockContainerHeightDelayed() {
        ((LinearLayout.LayoutParams) CommentActivity.this.container
                .getLayoutParams()).weight = 1.0F;
    }

    public void onClickListItem(CommentBean bean, String referid, String toUserId) {
        this.referid = referid;
        this.toUserId = toUserId;
        AppLogger.e("toUserId" + toUserId);
        if (!TextUtils.isEmpty(referid)) {
            et_repply.setHint("引用@" + bean.getNickname() + ":");

        } else if (!TextUtils.isEmpty(toUserId)) {
            et_repply.setHint("回复@" + bean.getNickname() + ":");

        } else {

            clearEditText();
        }
    }

    private void clearEditText() {
        if (smiley.isShown()) {
            hideSmileyPicker(false);
        }
        closeInputMethod();
        et_repply.clearFocus();
        container.requestFocus();

        this.referid = "";
        this.toUserId = "";
        et_repply.setText("");
        et_repply.setHint(getResources().getString(R.string.comment_hint));
    }

    private void closeInputMethod() {
       InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
       boolean isOpen = imm.isActive();
       if (isOpen) {
            imm.hideSoftInputFromWindow(et_repply.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
         }
    }
}
