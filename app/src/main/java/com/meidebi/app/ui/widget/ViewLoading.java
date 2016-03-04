package com.meidebi.app.ui.widget;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.ui.widget.action.ILoadingAction;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class ViewLoading implements OnClickListener {
	@InjectView(R.id.iv_load_progressBar) CircularProgressBar pb_loading;
    @InjectView(R.id.tv_load_err) TextView tv_err;
    @InjectView(R.id.iv_err)
    ImageView iv_err;

    private ILoadingAction action;
	private View view;
	private LoadStatus mState = LoadStatus.Idle;
    private View attchView;
    private int empty_img;
    private String empty_text;
	public ViewLoading(Activity context) {
		initView(context);
	}

	public static enum State {
		Idle, TheEnd, Loading, err
	}

	public ViewLoading(View v) {
		this.view = ((ViewStub) v.findViewById(R.id.common_layout_loading)).inflate();
 		initView();
	}

	public void initView() {
		// view = (View)context.findViewById(R.id.common_layout_loading);
        ButterKnife.inject(this,view);
	}

    public void toAttchView(View view){
        this.attchView = view;
//        if(attchView!=null){
//            attchView.setVisibility(View.VISIBLE);
//        }
    }

	public void initView(Activity context) {
		view = ((ViewStub) context.findViewById(R.id.common_layout_loading)).inflate();
        view.setOnClickListener(this);
        ButterKnife.inject(this, view);
	}

	public void onLoad() {
        if(attchView!=null){
            attchView.setVisibility(View.GONE);
        }
		view.setVisibility(View.VISIBLE);
		pb_loading.setVisibility(View.VISIBLE);
 		tv_err.setVisibility(View.GONE);
        iv_err.setVisibility(View.GONE);
        view.setOnClickListener(null);
	}

	public void onDone() {
        if(attchView!=null){
            attchView.setVisibility(View.VISIBLE);
        }
		view.setVisibility(View.GONE);
		pb_loading.setVisibility(View.GONE);
		tv_err.setVisibility(View.GONE);
        iv_err.setVisibility(View.GONE);
        view.setOnClickListener(null);

	}

	public void onFaied() {
        if(attchView!=null){
            attchView.setVisibility(View.GONE);
        }
		view.setVisibility(View.VISIBLE);
		pb_loading.setVisibility(View.GONE);
		tv_err.setVisibility(View.VISIBLE);
        iv_err.setVisibility(View.VISIBLE);
        view.setOnClickListener(this);
        iv_err.setImageResource(R.drawable.ic_no_network);
        tv_err.setText(XApplication.getInstance().getString(R.string.pull_listview_err));
	}

	public void onEmpty() {
        if(attchView!=null){
            attchView.setVisibility(View.GONE);
        }
		view.setVisibility(View.VISIBLE);
		pb_loading.setVisibility(View.GONE);
		tv_err.setVisibility(View.VISIBLE);
        iv_err.setVisibility(View.VISIBLE);
         iv_err.setImageResource(empty_img);
        tv_err.setText(empty_text);
        view.setOnClickListener(null);

    }

	public ILoadingAction getAction() {
		return action;
	}

	public void setAction(ILoadingAction action) {
		this.action = action;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		onLoad();
        if(action!=null) {
            action.onReload();
        }
	}

	public void onDestroy() {
		view = null;
	}

	public void setRes(int res, String text) {
        this.empty_img = res;
        this.empty_text = text;

	}

	public void setState(LoadStatus status) {
		if (mState == status) {
			return;
		}
		mState = status;
		switch (status) {
		case Loading:
			onLoad();
			break;
		case TheEnd:
			onDone();
			break;
		case err:
			onFaied();
			break;
		case empty:
			onEmpty();
			break;
		default:
			onDone();
			break;
		}
	}
}
