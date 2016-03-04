package com.meidebi.app.ui.submit;//package com.meidebi.app.ui.submit;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.meidebi.app.R;
//
//import android.app.Activity;
//import android.content.DialogInterface;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.TextView;
//
//import com.meidebi.app.support.utils.content.ContentUtils;
//import com.meidebi.app.ui.widget.dialog.DialogsAlertDialogFragment;
//
//class fragment_post_sucess implements MaterialDialog.OnDismissListener{
//	private TextView tv_score,tv_money;
//	private String score,  money;
//    private Activity activity;
//	public fragment_post_sucess(Activity activity){
//		setLayout_res(R.layout.fragment_post_sucuss);
//	}
//
//
//
//	public String getScore() {
//		return score;
//	}
//
//
//
//	public void setScore(String score) {
//		this.score = score;
//	}
//
//
//
//	public String getMoney() {
//		return money;
//	}
//
//
//
//	public void setMoney(String money) {
//		this.money = money;
//	}
//
//
//
//	protected  void initViews(View view) {
//		tv_score = (TextView) view.findViewById(R.id.tv_score);
//		tv_money = (TextView)view.findViewById(R.id.tv_money);
////		view.findViewById(R.id.btn_post_continu).setOnClickListener(new OnClickListener() {
////
////			@Override
////			public void onClick(View arg0) {
////				// TODO Auto-generated method stub
////			}
////		});
//		if(TextUtils.isEmpty(money)){
//			view.findViewById(R.id.ll_price).setVisibility(View.GONE);
//		}
//		buidViews();
//
//	}
//
//	public void buidViews(){
//		tv_score.setText(ContentUtils.insertStringXml(R.string.score, score));
//		tv_money.setText(ContentUtils.insertStringXml(R.string.money, money));
//	}
//
//
//	@Override
//	public void onDismiss(DialogInterface dialog) {
//		// TODO Auto-generated method stub
//		super.onDismiss(dialog);
//		((PostNewsActivity) getActivity()).BackToPost();
//	}
//
//
//}
