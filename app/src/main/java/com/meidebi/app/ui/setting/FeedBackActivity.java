package com.meidebi.app.ui.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.meidebi.app.R;
import com.umeng.fb.ConversationActivity;

public class FeedBackActivity extends ConversationActivity {


    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_toolbar_back_sel);
        toolbar.setTitle(getString(R.string.pref_feeback));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        overridePendingTransition(R.anim.zoom_in_center, R.anim.slide_out_right);
    }


}
