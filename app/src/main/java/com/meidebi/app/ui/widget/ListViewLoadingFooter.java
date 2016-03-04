package com.meidebi.app.ui.widget;

import com.meidebi.app.R;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.meidebi.app.XApplication;
import com.meidebi.app.ui.widget.action.ILoadingAction;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class ListViewLoadingFooter  {
	private View mLoadingFooter;
    @InjectView(R.id.listview_footer) TextView tv;
	@InjectView(R.id.refresh) CircularProgressBar refreshView;
 	private LoadStatus mState = LoadStatus.Idle;
	private ILoadingAction action;
//	private AnimationDrawable animationDrawable;

	public ILoadingAction getAction() {
		return action;
	}

	public void setAction(ILoadingAction action) {
		this.action = action;
	}

	public ListViewLoadingFooter(Context context) {
		mLoadingFooter = LayoutInflater.from(context).inflate(
				R.layout.listview_footer_layout, null);
		mLoadingFooter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 屏蔽点击
			}
		});
        ButterKnife.inject(this,mLoadingFooter);
		setState(LoadStatus.Idle);
	}

	public View getView() {
		return mLoadingFooter;
	}

	public LoadStatus getState() {
		return mState;
	}

	public void setState(final LoadStatus state, long delay) {
		mLoadingFooter.postDelayed(new Runnable() {

			@Override
			public void run() {
				setState(state);
			}
		}, delay);
	}

	public void setRes(String text) {
		tv.setText(text);
	}

	public void setState(LoadStatus status) {
		if (mState == status) {
			return;
		}
		mState = status;

		mLoadingFooter.setVisibility(View.VISIBLE);
		tv.setClickable(false);
		switch (status) {
		case Loading:
			showFooterView();
			break;
		case err:
			showErrorFooterView();
			break;
		case TheEnd:
			dismissFooterView();
			break;
		default:
			break;
		}
	}

	private void showFooterView() {
		tv.setVisibility(View.GONE);

		refreshView.setVisibility(View.VISIBLE);

	}

	private void showErrorFooterView() {
		tv.setVisibility(View.VISIBLE);
//		refreshView.clearAnimation();
		refreshView.setVisibility(View.GONE);
        this.setRes(XApplication.getInstance().getResources()
				.getString(R.string.pull_listview_err));
		tv.setClickable(true);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				action.onReload();
			}
		});
	}

	protected void dismissFooterView() {
        refreshView.setVisibility(View.GONE);
 		tv.setVisibility(
				View.GONE);
        mLoadingFooter.setVisibility(View.GONE);
	}
}
