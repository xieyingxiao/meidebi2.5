package com.meidebi.app.ui.search;

import com.meidebi.app.R;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.meidebi.app.support.utils.AppLogger;

/*
 * 封装搜索输入框
 */
public class SearchEditView implements OnClickListener {
	// private View view;
	private EditText et_search;
	private ISearchAction action;
	private Activity activity;

	public ISearchAction getAction() {
		return action;
	}

	public void setAction(ISearchAction action) {
		this.action = action;
	}

	public SearchEditView(Activity activity) {
		// this.view = view;
		this.activity = activity;
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.layout_search_edit, null);
		initView(layout);
	}

	public SearchEditView(Activity activity, View view) {
		// this.view = view;
		this.activity = activity;
		initView(view);
	}

	private Runnable run = new Runnable() {
		@Override
		public void run() {
			InputMethodManager m = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			m.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
		}
	};

	public void initView(final View view) {
		et_search = (EditText) view.findViewById(R.id.search_keyword_et);
//		et_search.requestFocus();
//		InputMethodManager imm = (InputMethodManager) et_search.getContext()
//				.getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

		et_search
				.setOnEditorActionListener(new EditText.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						AppLogger.e("et_search");
						search();
						// searchClick();
						et_search.removeCallbacks(run);
						et_search.postDelayed(run, 500);
						return true;
					}
				});
//		view.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				view.setFocusable(true);
//				view.setFocusableInTouchMode(true);
//				view.requestFocus();
//				InputMethodManager imm = (InputMethodManager) activity
//						.getSystemService(Context.INPUT_METHOD_SERVICE);
//				imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
//				return false;
//			}
//		});
	}

	private void search() {
		String keyword = et_search.getText().toString();
		String for_keyword = keyword.replaceAll(" ", "");
		if (!TextUtils.isEmpty(keyword) && !TextUtils.isEmpty(for_keyword)) {
			getAction().onSearchAction(keyword);
		} else {
			et_search.setError(activity
					.getString(R.string.search_key_cannot_empty));
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	public void removeFouce(){
		et_search.postDelayed(run, 500);
	}
	
}
