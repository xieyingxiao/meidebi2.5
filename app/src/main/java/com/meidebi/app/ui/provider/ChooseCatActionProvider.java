package com.meidebi.app.ui.provider;///*
// * Copyright (C) 2011 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.meidebi.app.ui.provider;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.ContextWrapper;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.TextView;
//
//import com.actionbarsherlock.view.ActionProvider;
//import com.meidebi.app.R;
//import com.meidebi.app.ui.main.MainActivity;
//
//public class ChooseCatActionProvider extends ActionProvider {
//
//	/**
//	 * Context for accessing resources.
//	 */
//	private ContextWrapper mContextWrapper;
//
//	private List<CustomChooser> list;
//
//	private Activity activity;
//
//	private TextView tv_title;
//
//	private boolean isHot = true;
//
//	public boolean isHot() {
//		return isHot;
//	}
//
//	public void setHot(boolean isHot) {
//		if (isHot) {
//			tv_title.setText(list
//					.get(0).getTitle());
//		} else {
//			tv_title.setText(list
//					.get(1).getTitle());
//		}
//		this.isHot = isHot;
//	}
//
//	public Activity getActivity() {
//		return activity;
//	}
//
//	public void setActivity(Activity activity) {
//		this.activity = activity;
//	}
//
//	/**
//	 * Creates a new instance.
//	 * 
//	 * @param context
//	 *            Context for accessing resources.
//	 */
//	public ChooseCatActionProvider(Context context) {
//		super(context);
//		mContextWrapper = (ContextWrapper) context;
//		list = new ArrayList<CustomChooser>();
//		CustomChooser customChooser = new CustomChooser();
//		customChooser.setTitle("精华");
//		list.add(customChooser);
//		customChooser = new CustomChooser();
//		customChooser.setTitle("最新");
//		list.add(customChooser);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public View onCreateActionView() {
//		// Create the view and set its data model.
//		LayoutInflater inflater = LayoutInflater.from(mContextWrapper);
//		View view = inflater.inflate(R.layout.provider_choose_cat, null);
//		tv_title = (TextView) view.findViewById(R.id.abs__text);
//		tv_title.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				onClickListener();
//			}
//		});
//		tv_title.setBackgroundResource(R.drawable.bg_titlebar_drop_tv);
//		tv_title.setText(list
//				.get(0).getTitle());
//		return view;
//	}
//
//	public void onClickListener() {
// 		isHot = !isHot;
//		final int position = isHot ? 0 : 1;
//		// TODO Auto-generated method stub
//		tv_title.setText(list.get(position).getTitle());
//		// customChooserView.setExpandActivityOverflowTitleResource(list.get(position).getTitle());
//		((MainActivity) getActivity()).changeMenu(isHot);
//	}
//
//	// /**
//	// * {@inheritDoc}
//	// */
//	// @Override
//	// public boolean hasSubMenu() {
//	// return true;
//	// }
//
//	public void selectedItem(boolean isHot) {
//		int position = isHot ? 0 : 1;
//		// customChooserView.setExpandActivityOverflowTitleResource(list.get(
//		// position).getTitle());
//	}
//
//	// @Override
//	// public void onItemChooser(int position) {
//	// customChooserView.setExpandActivityOverflowTitleResource(list.get(
//	// position).getTitle());
//	// isHot = position == 0 ? true : false;
//	// ((MainActivity) getActivity()).changeMenu(isHot);
//	// // customChooserView.sett(list.get(position).getTitle());
//	// }
//}
