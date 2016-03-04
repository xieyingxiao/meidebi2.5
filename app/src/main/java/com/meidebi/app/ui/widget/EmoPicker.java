package com.meidebi.app.ui.widget;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.Spannable;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.support.utils.content.EmoPickerUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class EmoPicker extends LinearLayout {

	private int mPickerHeight;
	private EditText mEditText;
	private LayoutInflater mInflater;
	private List<String> keys;
	private Activity activity;
 	private  LayoutTransition transitioner ;
//	private com.nineoldandroids.animation.ObjectAnimator animIn, animOut;
	private GridView gridView;
	public EmoPicker(Context paramContext) {
		super(paramContext);
	}

	public EmoPicker(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		this.mInflater = LayoutInflater.from(paramContext);
		 gridView = (GridView) this.mInflater.inflate(
				R.layout.layout_emo_picker, null);
		addView(gridView);
		gridView.setAdapter(new SmileyAdapter(paramContext));
	}

	public void setEditText(Activity activity, ViewGroup rootLayout,
			EditText paramEditText) {
		this.mEditText = paramEditText;
		this.activity = activity;
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {
			transitioner= new LayoutTransition();
			rootLayout.setLayoutTransition(transitioner);
			setupAnimations(transitioner);
		}
	}

	public void show(Activity paramActivity, boolean showAnimation) {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {
		if (showAnimation) {
				transitioner.setDuration(200);
		} else {
				transitioner.setDuration(0);
		}
		}
		this.mPickerHeight = EmoPickerUtility.getKeyboardHeight(paramActivity);
		EmoPickerUtility.hideSoftInput(this.mEditText);
		getLayoutParams().height = this.mPickerHeight;
		setVisibility(View.VISIBLE);
		// open smilepicker, press home, press app switcher to return to write
		// weibo interface,
		// softkeyboard will be opened by android system when smilepicker is
		// showing,
		// this method is used to fix this issue
		paramActivity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	public void hide(Activity paramActivity) {
		setVisibility(View.GONE);
		paramActivity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

	}

	private final class SmileyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public SmileyAdapter(Context context) {

			this.mInflater = LayoutInflater.from(context);
			Set<String> keySet = XApplication.getInstance().getEmotionsPics()
					.keySet();
			keys = new ArrayList<String>();
			keys.addAll(keySet);
		}

		private void bindView(final int position, View paramView) {
			ImageView imageView = ((ImageView) paramView
					.findViewById(R.id.smiley_item));
			imageView.setImageBitmap(XApplication.getInstance()
					.getEmotionsPics().get(keys.get(position)));
			paramView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// String ori = mEditText.getText().toString();
					// int index = mEditText.getSelectionStart();
					// StringBuilder stringBuilder = new StringBuilder(ori);
					// stringBuilder.insert(index, keys.get(position));
					setEmo(keys.get(position));
					// mEditText.setText(stringBuilder.toString());
					// mEditText.setSelection(index +
					// keys.get(position).length());
				}
			});
		}

		private void setEmo(String str2) {
			int start = mEditText.getSelectionStart();
			StringBuilder stringBuilder = new StringBuilder("[");
			stringBuilder.append(str2).append("]");
			Spannable ss = mEditText.getText().insert(start,
					stringBuilder.toString());
			Bitmap bitmap = XApplication.getInstance().getEmotionsPics()
					.get(str2);
			ImageSpan span = new ImageSpan(XApplication.getInstance()
					.getActivity(), bitmap, ImageSpan.ALIGN_BASELINE);
			ss.setSpan(span, start,
					start + (stringBuilder.toString()).length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		public int getCount() {
			return XApplication.getInstance().getEmotionsPics().size();
		}

		public Object getItem(int paramInt) {
			return null;
		}

		public long getItemId(int paramInt) {
			return 0L;
		}

		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {
			if (paramView == null)
				paramView = this.mInflater.inflate(
						R.layout.adapter_emo_picker_item, null);
			bindView(paramInt, paramView);
			return paramView;
		}
	}

	private void setupAnimations(LayoutTransition transition) {

		ObjectAnimator animIn = ObjectAnimator
				.ofFloat(null, "translationY",
						EmoPickerUtility.getScreenHeight(this.activity),
						mPickerHeight)
				.setDuration(transition.getDuration(LayoutTransition.APPEARING));
		transition.setAnimator(LayoutTransition.APPEARING, animIn);

		ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "translationY",
				mPickerHeight, EmoPickerUtility.getScreenHeight(this.activity))
				.setDuration(
						transition.getDuration(LayoutTransition.DISAPPEARING));
		transition.setAnimator(LayoutTransition.DISAPPEARING, animOut);

	}

//	private void setupSupportAnimations(View view) {
//
//		animIn = com.nineoldandroids.animation.ObjectAnimator.ofFloat(view,
//				"translationY",
//				EmoPickerUtility.getScreenHeight(this.activity), mPickerHeight)
//				.setDuration(1000);
//
//		animOut = com.nineoldandroids.animation.ObjectAnimator.ofFloat(view,
//				"translationY", 0,
//				mPickerHeight).setDuration(
//				1000);
//
//	}
}