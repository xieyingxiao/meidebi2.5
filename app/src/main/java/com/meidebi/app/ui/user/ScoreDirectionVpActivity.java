package com.meidebi.app.ui.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.meidebi.app.R;
import com.meidebi.app.service.init.ChannelInitBean;
import com.meidebi.app.service.init.ScoreDirectionBean;
import com.meidebi.app.service.init.ScoreDirectionInit;
import com.meidebi.app.ui.base.BaseVpWithTitleActivity;
import com.meidebi.app.ui.browser.BrowserHideControllerWebFragment;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment") public class ScoreDirectionVpActivity extends BaseVpWithTitleActivity {
	private List<ScoreDirectionBean> cat_list = ScoreDirectionInit.getInstance().getList();
	private ChannelInitBean category;
	private long data_insert_time;
	
	

	public long getData_insert_time() {
		return data_insert_time;
	}

	public void setData_insert_time(long data_insert_time) {
		this.data_insert_time = data_insert_time;
	}

	public boolean isHot() {
		return category.isHot();
	}
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout_res = R.layout.vp_coupon_list;
		super.onCreate(savedInstanceState);
//		setActionBar(getString(R.string.users_score_driction));
	}

    @Override
    protected int getLayoutResource() {
        return 0;
    }

//    @Override
//    protected int getLayoutResource() {
//        return 0;
//    }


    @Override
	protected List<Fragment> initFrogmentList() {
		// TODO Auto-generated method stub
		List<Fragment> fragmentList = new ArrayList<Fragment>();
		for (int i = 0; i < cat_list.size(); i++) {
			if (getFragment(i) == null) {
				fragmentList.add(new BrowserHideControllerWebFragment(cat_list.get(i).getUrl(),false));
			} else {
				fragmentList.add(getFragment(i));
			}
		}
		return fragmentList;
	}

	@Override
	protected CharSequence getTitle(int position) {
		// TODO Auto-generated method stub
		return cat_list.get(position).getName();
	}


}
