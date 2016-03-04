package com.meidebi.app.ui.widget.dialog;//package com.meidebi.app.ui.widget.dialog;
//
//import android.app.AlertDialog;
//import android.support.v4.app.FragmentActivity;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import eu.inmite.android.lib.dialogs.BaseDialogFragment;
//import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
//import eu.inmite.android.lib.dialogs.SimpleDialogFragment;
//
//public class DialogsAlertDialogFragment extends SimpleDialogFragment {
//	private String mTitle;
//	private int layout_res = 0;
//	private String msg;
//	private boolean enbleBtn = false;
//
//	protected OnClickListener LIST_CLICK_LISTENER = null;
//
//	public void setCLICK_LISTENER(OnClickListener lIST_CLICK_LISTENER) {
//		LIST_CLICK_LISTENER = lIST_CLICK_LISTENER;
//	}
//
//	public boolean isEnbleBtn() {
//		return enbleBtn;
//	}
//
//	public void setEnbleBtn(boolean enbleBtn) {
//		this.enbleBtn = enbleBtn;
//	}
//
//	public void setLayout_res(int layout_res) {
//		this.layout_res = layout_res;
//	}
//
//	public DialogsAlertDialogFragment() {
//	}
//
//	@Override
//	public BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
//		if (layout_res != 0) {
//			LayoutInflater inflater = getActivity().getLayoutInflater();
//			View view = inflater.inflate(layout_res, null);
//			builder.setView(view);
//			initViews(view);
//		}
//		if (!TextUtils.isEmpty(mTitle)) {
//			builder.setTitle(mTitle);
//		}
//		if (!TextUtils.isEmpty(msg)) {
//			builder.setMessage(msg);
//		}
//		prepareBuilder(builder);
//		return builder;
//	}
//
//
//	public void getListView(AlertDialog alertDialog) {
//	}
//
//	protected void initViews(View view) {
//
//	}
//
//	protected void prepareBuilder(Builder builder) {
//		if (enbleBtn) {
//			builder.setPositiveButton("确定", new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					ISimpleDialogListener listener = getDialogListener();
//					if (listener != null) {
//						listener.onPositiveButtonClicked(0);
//					}
//					dismiss();
//				}
//			});
//			builder.setNegativeButton("取消", new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					ISimpleDialogListener listener = getDialogListener();
//					if (listener != null) {
//						listener.onPositiveButtonClicked(1);
//					}
//					dismiss();
//				}
//			});
//		}
//	}
//
//	public String getmTitle() {
//		return mTitle;
//	}
//
//	public void setmTitle(String mTitle) {
//		this.mTitle = mTitle;
//	}
//
//	public String getMsg() {
//		return msg;
//	}
//
//	public void setMsg(String msg) {
//		this.msg = msg;
//	}
//
//	public static String TAG = "DialogsAlertDialogFragment";
//
//	public static void show(FragmentActivity activity) {
//		new DialogsAlertDialogFragment().show(
//				activity.getSupportFragmentManager(), TAG);
//	}
//}
