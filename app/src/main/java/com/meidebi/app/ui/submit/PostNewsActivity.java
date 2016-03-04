package com.meidebi.app.ui.submit;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.meidebi.app.R;
import com.meidebi.app.service.dao.PostDao;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.content.ContentUtils;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PostNewsActivity extends BasePullToRefreshActivity {
    // private RadioButton rb_singel,rb_act,rb_coupon;
    private fragment_post_news fragment_post;
    private Menu mMenu;
    //    private fragment_post_sucess sucess_fragment;
    private PostDao dao;

    public PostDao getDao() {
        if (dao == null) {
            dao = new PostDao();
        }
        return dao;
    }

//    public fragment_post_sucess getSucess_fragment() {
//        if (sucess_fragment == null) {
//            sucess_fragment = new fragment_post_sucess();
//        }
//        return sucess_fragment;
//    }

    public Fragment getFragment_post() {
        if (fragment_post == null) {
            fragment_post = new fragment_post_news();
        }
        return fragment_post;
    }

    // public Fragment getFragment_sucess() {
    // if(fragment_sucess==null){
    // fragment_sucess = new fragment_post_sucess();
    // }
    // return fragment_sucess; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (SharePrefUtility.firstPostGuide()) {
            IntentUtil.start_activity(PostNewsActivity.this, post_guide_activity.class);
        }
        setActionBar(getString(R.string.iwantpost));
        setContentView(R.layout.common_fragment_activity);
        addFragment(getFragment_post());
    }

    public void BackToPost() {
        // switchContent(getFragment_sucess(), getFragment_post(),
        // R.id.common_fragment);
        ((fragment_post_news) getFragment_post()).clearEditText();
        // mMenu.findItem(R.id.menu_comment).setVisible(true);
    }

    //
    // public void JumpToSucess(){
    // switchContent(getFragment_post(), getFragment_sucess(),
    // R.id.common_fragment);
    // mMenu.findItem(R.id.menu_comment).setVisible(false);
    // }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.submit, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_comment:
                ((fragment_post_news) getFragment_post()).submit();
                break;
        }
        return false;

    }

    @Override
    protected int getLayoutResource() {
        return 0;
    }

    public void jumpToSucess(String money, String score) {
        ShowSucessDialog(money, score);
//        getSucess_fragment().setMoney(money);
//        getSucess_fragment().setScore(score);
//        getSucess_fragment().show(getSupportFragmentManager(), "");
    }

    @InjectView(R.id.tv_score)
    TextView tv_score;
    @InjectView(R.id.tv_money)
    TextView tv_money;

    private void ShowSucessDialog(String score, String money) {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.fragment_post_sucuss, true)
                .dismissListener(new MaterialDialog.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        BackToPost();
                    }
                }).build();
        ButterKnife.inject(dialog.getCustomView());
        tv_score.setText(ContentUtils.insertStringXml(R.string.score, score));
        tv_money.setText(ContentUtils.insertStringXml(R.string.money, money));
        dialog.show();
    }
}