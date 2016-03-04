package com.meidebi.app.ui.widget.dialog;//package com.meidebi.app.ui.widget.dialog;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.content.DialogInterface;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.SimpleAdapter;
//import android.widget.TextView;
//
//import com.meidebi.app.R;
//import com.meidebi.app.XApplication;
//import com.meidebi.app.base.config.AppConfigs;
//
//public class DialogListFragment extends DialogsAlertDialogFragment {
//	private int listres;
//	private String mTitle;
//	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//
//	public DialogListFragment(String mTitle, List<Map<String, Object>> list) {
//		this.list = list;
//		this.mTitle = mTitle;
//	}
//
//
//
//	@Override
//	protected void prepareBuilder(Builder builder) {
//		if(LIST_CLICK_LISTENER==null){
//		LIST_CLICK_LISTENER = (DialogInterface.OnClickListener) getActivity();
//		}	
//		builder.setAdapter(new SimpleAdapter(getActivity(), list,
//				R.layout.adapter_common, new String[] { AppConfigs.LIST_TEXT },
//				new int[] { android.R.id.text1 }),
//				LIST_CLICK_LISTENER);
//	}
//
//	@Override
//	public void getListView(AlertDialog alertDialog) {
//		alertDialog.getListView().setDivider(null);
//		if(!TextUtils.isEmpty(mTitle)){
//		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_title,null);
//		((TextView)v.findViewById(R.id.tv_dialog_title)).setText(mTitle);
//		alertDialog.getListView().addHeaderView(v);
//		}
//	}
//
//	public DialogListFragment(int listres) {
//		this.listres = listres;
//		String[] str_arr = XApplication.getInstance().getResources().getStringArray(listres);
//		for (int i = 0; i < str_arr.length; i++) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put(AppConfigs.LIST_TEXT,
//					str_arr[i]);
//			list.add(map);
//		}
//	}
//
//}
