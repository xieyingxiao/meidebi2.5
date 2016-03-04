package com.meidebi.app.ui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.NumberPicker;

import com.afollestad.materialdialogs.MaterialDialog;
import com.meidebi.app.R;
import com.meidebi.app.support.component.upush.UpushUtity;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.setting.NotificationFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;


@SuppressLint("NewApi")
public class DialogNumpicker {
    @InjectView(R.id.np_from)
    NumberPicker np_from;
    @InjectView(R.id.np_to)
    NumberPicker np_to;
    private MaterialDialog dialog;
    private Activity activity;
    public static String TAG = "timepicker";

    public void show() {
        dialog.show();
    }

    public DialogNumpicker(final Fragment fragment) {
        this.activity = activity;
        dialog = new MaterialDialog.Builder(fragment.getActivity())
                .title("选择时段")
                .customView(R.layout.dialog_timepicker, true)
                .negativeText(R.string.make_sure)
                .positiveText(R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        UpushUtity.setSlientTime(0, 0);
                        ((NotificationFragment) fragment).refrshSetTime(0, 0, false);
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        SharePrefUtility.setPushTime(np_from.getValue(), np_to.getValue());
                        UpushUtity.setSlientTime(np_from.getValue(), np_to.getValue());
                        ((NotificationFragment) fragment).refrshSetTime(np_from.getValue(), np_to.getValue(), true);

                        dialog.dismiss();
                    }
                })
                .build();
        initnp();
    }


    public void initnp() {
        ButterKnife.inject(this, dialog.getCustomView());
        np_from.setMaxValue(23);
        np_from.setMinValue(0);
        np_from.setFocusable(true);
        np_from.setFocusableInTouchMode(true);
        np_to.setMaxValue(23);
        np_to.setMinValue(0);
        np_to.setFocusable(true);
        np_to.setFocusableInTouchMode(true);
    }

//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setCancelable(false);
//		int style = DialogFragment.STYLE_NORMAL, theme = 0;
//		setStyle(style, theme);
//	}

//	@Override
//	public BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
//		View view = LayoutInflater.from(getActivity()).inflate(
//				R.layout.dialog_timepicker, null);
//		// inform the dialog it has a custom View
//		builder.setTitle("选择时段");
//		builder.setView(view);
//		// and if you need to call some method of the class
//		np_from = (NumberPicker) view.findViewById(R.id.np_from);
//		np_to = (NumberPicker) view.findViewById(R.id.np_to);
//		// create the dialog from the builder then show
//		builder.setNegativeButton("确定", new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				((PushSettingActivity) getActivity())
//						.refreshFragmentDilagTimePicker(np_from.getValue(),
//								np_to.getValue(), false);
//				dismiss();
//			}
//		});
//
//		builder.setPositiveButton(R.string.cancel,
//				new View.OnClickListener() {
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						((PushSettingActivity) getActivity())
//								.refreshFragmentDilagTimePicker(
//										np_from.getValue(), np_to.getValue(),
//										false);
//						dismiss();
//					}
//				});
//
//		initnp();
//		setCancelable(false);
//		return builder;
//	}


}
