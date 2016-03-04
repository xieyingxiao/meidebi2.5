package com.meidebi.app.ui.submit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meidebi.app.R;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.comment.CommentBean;
import com.meidebi.app.service.dao.CommentDao;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.support.utils.content.EmoPickerUtility;
import com.meidebi.app.support.utils.content.TimeUtil;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;
import com.meidebi.app.ui.widget.EmoPicker;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CommentEditSubmitActivity extends BasePullToRefreshActivity {
	private CommentDao dao;
	private String cid;// 详情id
	private CommentBean bean;// @的用户名id
	private String typeId = "0";

    @InjectView(R.id.tv_adapter_comment_quote_name) TextView _quote_name;
    @InjectView(R.id.tv_adapter_comment_quote_time_ago) TextView _quote_time;
    @InjectView(R.id._ll_quote) View _ll_quote;
    @InjectView(R.id.tv_adapter_quote_content) TextView _quote_content;
     @InjectView(R.id.emo_picker) EmoPicker smiley;
    @InjectView(R.id.et_comment_reply) EditText et_reply;
     @InjectView(R.id.container) View container;

	public String getToUserId() {
		if (bean == null) {
			return "0";
		}
		return bean.getId();
	}


	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public CommentDao getDao() {
		if (dao == null) {
			dao = new CommentDao(this);
		}
		return dao;
	}

	public void setDao(CommentDao dao) {
		this.dao = dao;
	}

	private Handler mHandler = new Handler() {
		@SuppressLint("ValidFragment")
		public void handleMessage(Message msg) {
			dissmissDialog();
			switch (msg.what) {
			case 1:// 刷新
				Toast.makeText(CommentEditSubmitActivity.this,
						getString(R.string.comment_sucess), Toast.LENGTH_SHORT)
						.show();
				setResult(RESULT_OK);
				TimeUtil.addCommentTimeMap(cid);
				finish();
				break;
			case DATAERR:
				showErr(R.string.hint_sumitfailed);
				break;
			case NETERR:
				showErr(R.string.timeout);
				break;
			}
		};

	};

	private void subComment(final String content) {
		showLoading(R.string.hint_sumit);
		new Thread() {
			public void run() {
				CommonJson<Object> json = getDao().submitComment(content, cid, typeId,
						getToUserId());
				Message message = new Message();

				if (json != null) {
					message.obj = json.getInfo();
					if (json.getStatus() == 1) {
						message.what = 1;
					} else {
						message.what = DATAERR;
					}
				} else {
					message.what = NETERR;

				}
				mHandler.sendMessage(message);

			}
		}.start();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
 		setActionBar("回复评论");
        ButterKnife.inject(this);
		getData();
		buildViewData();
		buildEmoPicker();
	}

	private void getData() {
		cid = getIntent().getStringExtra("id");
		typeId = getIntent().getStringExtra("type");
		bean = (CommentBean) getIntent().getSerializableExtra("bean");
	}

	private void buildViewData() {
//		TextView btn_emo = (TextView) findViewById(R.id.btn_comment_emo);
//		btn_emo.setOnClickListener(this);
		// initTitle();
		// setRightTextView(getString(R.string.send));
		// Button btn_send = (Button) findViewById(R.id.btn_comment_send);
		// btn_send.setOnClickListener(this);
 		et_reply.setFocusable(true);
		et_reply.setFocusableInTouchMode(true);
		et_reply.requestFocus();
 		if (bean != null) {
            _ll_quote.setVisibility(View.VISIBLE);

            _quote_name.setText(bean.getNickname());
            _quote_content.setText(bean.getListViewSpannableString());
		} else {
            _ll_quote.setVisibility(View.GONE);
		}

	}

	private void buildEmoPicker() {
 		smiley.setEditText(CommentEditSubmitActivity.this,
				((LinearLayout) findViewById(R.id.root_layout)), et_reply);
	}

    @OnClick({R.id.et_comment_reply,R.id.btn_comment_emo})
    protected void OnClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_comment_emo:
			if (smiley.isShown()) {
				hideSmileyPicker(true);
			} else {
				showSmileyPicker(EmoPickerUtility
						.isKeyBoardShow(CommentEditSubmitActivity.this));
			}
			break;
		case  R.id.et_comment_reply:
            hideSmileyPicker(true);
			break;

		default:
			break;
		}
	}


	private void showSmileyPicker(boolean showAnimation) {
		this.smiley.show(CommentEditSubmitActivity.this, showAnimation);
		lockContainerHeight(EmoPickerUtility
				.getAppContentHeight(CommentEditSubmitActivity.this));

	}

	public void hideSmileyPicker(boolean showKeyBoard) {
		if (this.smiley.isShown()) {
			if (showKeyBoard) {
				// this time softkeyboard is hidden
				LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) this.container
						.getLayoutParams();
				localLayoutParams.height = smiley.getTop();
				localLayoutParams.weight = 0.0F;
				this.smiley.hide(CommentEditSubmitActivity.this);
				EmoPickerUtility.showKeyBoard(et_reply);
				et_reply.postDelayed(new Runnable() {
					@Override
					public void run() {
						unlockContainerHeightDelayed();
					}
				}, 200L);
			} else {
				this.smiley.hide(CommentEditSubmitActivity.this);
				unlockContainerHeightDelayed();
			}
		}

	}

	@Override
	public void onBackPressed() {
		if (smiley.isShown()) {
			hideSmileyPicker(false);
		} else {
			super.onBackPressed();
		}
	}

	private void lockContainerHeight(int paramInt) {
		LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) this.container
				.getLayoutParams();
		localLayoutParams.height = paramInt;
		localLayoutParams.weight = 0.0F;
	}

	public void unlockContainerHeightDelayed() {
		((LinearLayout.LayoutParams) CommentEditSubmitActivity.this.container
				.getLayoutParams()).weight = 1.0F;
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.zoom_in_center, R.anim.present_down);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.comment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.menu_comment:
			if (!TimeUtil.isCommenttimeInLimit(cid)) {
				String content = et_reply.getText().toString();

				if (!TextUtils.isEmpty(content)) {
					if (LoginUtil
							.isAccountLogin(CommentEditSubmitActivity.this)) {
						subComment(content);
					}
				} else {
					et_reply.setError(getString(R.string.comment_conent_isnot_empty));
					// Toast.makeText(
					// CommentEditSubmit.this,
					// getString(
					// R.string.comment_conent_isnot_empty),
					// Toast.LENGTH_SHORT).show();
				}
			} else {
				et_reply.setError("发送评论间隔不能超过30秒");
			}
			break;
		}
		return false;

	}

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_comment_editreply;
    }
}
