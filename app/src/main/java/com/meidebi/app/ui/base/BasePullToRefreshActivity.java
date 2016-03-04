package com.meidebi.app.ui.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.meidebi.app.R;

public abstract class BasePullToRefreshActivity extends SwipeBackActivity {

    public Toolbar actionBar;

    private Boolean useActionbar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (getLayoutResource() != 0) {
            setContentView(getLayoutResource());
            if (useActionbar) {
                initActionBar();
            }
        }

    }

    public void initActionBar() {
        // TODO Auto-generated method stub
        actionBar = (Toolbar) findViewById(R.id.toolbar);
        if (actionBar != null) {
            setSupportActionBar(actionBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            actionBar.setNavigationIcon(R.drawable.btn_toolbar_back_sel);

//              // actionBar.setIcon(R.drawable.ic_actionbar);
//              getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(
//                      R.color.titlebar_bg));
//              getSupportActionBar().setDisplayShowCustomEnabled(true);
//              getSupportActionBar().setCustomView(R.layout.actionbar_title);
//              title = (TextView) findViewById(android.R.id.text1);
//              iv_back = (ImageView) findViewById(android.R.id.text2);
//              getSupportActionBar().setIcon(R.color.transparent);
//              LinearLayout ll = (LinearLayout) findViewById(R.id.ll_actionbar_back);
//              ll.setOnClickListener(new View.OnClickListener() {
//                  @Override
//                  public void onClick(View arg0) {
//                      // TODO Auto-generated method stub
//                      titleOnclick();
//                  }
//              });
        }
    }

    public void setBackButtonGone() {
//		iv_back.setVisibility(View.GONE);
    }


    public void setActionBar(String text) {
        getSupportActionBar().setTitle(text);
//		title.setText(text);
    }

    // private void initPullToRefreshAttacher() {
    // // TODO Auto-generated method stub
    // PullToRefreshAttacher.Options options = new
    // PullToRefreshAttacher.Options();
    // options.headerInAnimation = R.anim.pulldown_fade_in;
    // options.headerOutAnimation = R.anim.pulldown_fade_out;
    // options.refreshScrollDistance = 0.3f;
    // options.headerLayout = R.layout.pulldown_header;
    // mPullToRefreshAttacher = new PullToRefreshAttacher(this, options);
    // }


    public void titleOnclick() {
        finish();
    }


}
