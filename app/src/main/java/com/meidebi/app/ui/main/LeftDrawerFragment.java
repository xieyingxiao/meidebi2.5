//package com.meidebi.app.ui.main;
//
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.Fragment;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.meidebi.app.R;
//import com.meidebi.app.XApplication;
//import com.meidebi.app.base.config.AppConfigs;
//import com.meidebi.app.service.bean.user.AccountBean;
//import com.meidebi.app.service.bean.user.AwardJson;
//import com.meidebi.app.service.bean.user.UserCentrerJson;
//import com.meidebi.app.service.bean.user.UserinfoBean;
//import com.meidebi.app.service.dao.user.UserCenterDao;
//import com.meidebi.app.service.init.ChanneInit;
//import com.meidebi.app.support.utils.anim.AnimateFirstDisplayListener;
//import com.meidebi.app.support.utils.component.IntentUtil;
//import com.meidebi.app.support.utils.component.LoginUtil;
//import com.meidebi.app.ui.adapter.LeftDrawerAdapter;
//import com.meidebi.app.ui.setting.SettingActivity;
//import com.meidebi.app.ui.submit.PostNewsActivity;
//import com.meidebi.app.ui.user.LoginActivity;
//import com.meidebi.app.ui.user.UserCenterActivity;
//import com.meidebi.app.ui.view.roundedview.RoundedImageView;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.orm.SugarRecord;
//import com.orm.query.Select;
//
//public class LeftDrawerFragment extends Fragment implements OnClickListener {
//	private final int GETINFO = 1;
//	private final int SIGNSC = 2;
//	private final int SIGNFAIED = 3;
//	private final int NETERR = 4;
//	private final int OAUTHFAIL = 5;
//	private Boolean isFillData = false;
//	private ListView mListView;
//	private AccountBean accountBean = XApplication.getInstance()
//			.getAccountBean();
// 	private LeftDrawerAdapter mAdapter;
//	private RoundedImageView iv_avatar;
//	private UserCenterDao userDao;
//	private TextView   tv_lv,tv_user_name;
//	private MainActivity mActivity;
// 	private ImageView iv_msg_num;
//	private TextView tv_sign;
//    private UserinfoBean userbean;
// 	public UserCenterDao getUserDao() {
//		if (userDao == null) {
//			userDao = new UserCenterDao(getActivity());
//		}
//		return userDao;
//	}
//
//	/**
//	 * 数据回调处理
//	 */
//	private Handler mHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			// dissmissDialog();
//			switch (msg.what) {
//			case GETINFO:
//				UserinfoBean bean = ((UserinfoBean) msg.obj);
//				accountBean.setIsSign((bean.getIsSign() == 1));
//				accountBean.setMsgNum(bean.getMessagenum());
//                accountBean.setPhotoUrl(bean.getHeadImgUrl());
//				bean.setId(bean.getUserid());
//				SugarRecord.save(bean);
//				refreshUserInfo();
//				break;
//			case OAUTHFAIL:
//				LoginOut();
//				break;
//			case SIGNSC:
//				AwardJson bean_cre = ((AwardJson) msg.obj);
//				String str_money = "";
//				String str_score = "";
//				if (bean_cre.getData().getAddmoney() > 0) {
//					str_money = " 铜币 +" + bean_cre.getData().getAddmoney();
//				}
//				if (bean_cre.getData().getAddscore() > 0) {
//					str_score = " 积分 +" + bean_cre.getData().getAddscore();
//                    }
//                if(!TextUtils.isEmpty(str_money)||!TextUtils.isEmpty(str_score)) {
//                    new MaterialDialog.Builder(getActivity())
//                            .content(str_money + str_score)
//                            .show();
//                 }
//                    accountBean.setIsSign(true);
//                tv_sign.setText(bean_cre.getData().getContent());
//                userbean.setSigntimes(userbean.getSigntimes()+1);
//                SugarRecord.save(userbean);
//                refreshUserInfo();
//				break;
//			case SIGNFAIED:
//				AwardJson bean_cre2 = ((AwardJson) msg.obj);
//				if (bean_cre2 != null) {
//					accountBean.setIsSign(true);
//					Toast.makeText(getActivity(), bean_cre2.getInfo(),
//							Toast.LENGTH_SHORT).show();
//				} else {
//					accountBean.setIsSign(false);
//					Toast.makeText(getActivity(), "今天已签到", Toast.LENGTH_SHORT)
//							.show();
//                    getUserInfo();
//                }
//				break;
//			case NETERR:
//				Toast.makeText(XApplication.getInstance(), XApplication.getInstance().getString(R.string.timeout),
//						Toast.LENGTH_SHORT).show();
//				break;
//			default:// 失败
//				break;
//			}
//		};
//
//	};
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		mActivity = (MainActivity) getActivity();
//		View contentView = inflater
//				.inflate(R.layout.fragment_left_drawer, null);
//  		contentView.findViewById(R.id.iv_menu_setting).setOnClickListener(this);
//		tv_sign = (TextView) contentView.findViewById(R.id.tv_user_sign);
////        tv_lv = (TextView)contentView.findViewById(R.id.tv_user_lv);
//		contentView.findViewById(R.id.iv_menu_post).setOnClickListener(this);
//        tv_user_name = (TextView)contentView.findViewById(R.id.tv_user_name);
//		// tv_reg = (TextView) contentView.findViewById(R.id.tv_menu_goto_reg);
//		BuildUserView(contentView);
//		BuildChannelListView(contentView);
//		if (LoginUtil.isAccountLogined()) {// 是否登陆
//			afterLogin();
//		} else {
//			beforeLogin();
//		}
//
//		return contentView;
//	}
//
//	private void BuildChannelListView(View view) {
//		mListView = (ListView) view.findViewById(R.id.listView);
//		mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
//		mAdapter = new LeftDrawerAdapter(mListView);
//		mListView.setAdapter(mAdapter);
//		mListView.setItemChecked(0, true);
//		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				mListView.setItemChecked(position, true);
//				mActivity.setCategory(ChanneInit.getInstance().get()
//						.get(position));
//				mAdapter.notifyDataSetChanged();
//			}
//		});
//	}
//
//	private void BuildUserView(View view) {
//		iv_avatar = (RoundedImageView) view.findViewById(R.id.iv_user_avatar);
//
//
//		iv_msg_num = (ImageView) view.findViewById(R.id.iv_msg_num);
//	}
//
//	private void beforeLogin() {
// 		iv_avatar.setImageResource(R.drawable.iv_no_avantar);
//		iv_avatar.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				IntentUtil.start_result_activity(getActivity(),
//						LoginActivity.class);
//			}
//		});
////		tv_lv.setVisibility(View.GONE);
//        tv_user_name.setText(XApplication.getInstance().getString(R.string.menu_login));
//
//        tv_user_name.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				IntentUtil.start_result_activity(getActivity(),
//						LoginActivity.class);
//			}
//		});
//
//         tv_sign.setEnabled(true);
//
//        tv_sign.setText(XApplication.getInstance().getString(R.string.menu_sign_in));
//        tv_sign.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				IntentUtil.start_result_activity(getActivity(),
//						LoginActivity.class);
//			}
//		});
//        changeSignLeftIcon(R.drawable.iv_sign_sel);
//		iv_msg_num.setVisibility(View.GONE);
//
//
// 	}
//
//	/**
//	 * 登陆后的列表
//	 */
//	public void afterLogin() {
//		// getAfterData();
//		isFillData = true;
//		refreshUserInfo();
//		getUserInfo();
//	}
//
//	public void getUserInfo() {
//		new Thread() {
//			@Override
//			public void run() {
//				UserCentrerJson bean = getUserDao().getUserInfo();
//				Message message = new Message();
//				if (bean != null) {
//					if (bean.getStatus() == 1) {
//						message.obj = bean.getData();
//						message.what = GETINFO;
//						mHandler.sendMessage(message);
//					} else {
//						message.what = OAUTHFAIL;
//						mHandler.sendMessage(message);
//					}
//				} else {
//					message.what = NETERR;
//					mHandler.sendMessage(message);
//				}
//			}
//		}.start();
//	}
//
//	private void signIn() {
//		new Thread() {
//			@Override
//			public void run() {
//				AwardJson bean = getUserDao().SignIn();
//				Message message = new Message();
//				if (bean != null) {
//					if (bean.getStatus() == 1) {
//						message.obj = bean;
//						message.what = SIGNSC;
//						mHandler.sendMessage(message);
//					} else {
//						message.what = SIGNFAIED;
//						mHandler.sendMessage(message);
//					}
//				} else {
//					message.what = NETERR;
//					mHandler.sendMessage(message);
//				}
//			}
//		}.start();
//	}
//
//	public void refreshUserInfo() {
//		  userbean = Select.from(UserinfoBean.class)
//				.where("ID = ?", new String[] { LoginUtil.getId() }).first();
//		if(userbean!=null){
//		ImageLoader.getInstance().displayImage(userbean.getHeadImgUrl(),
//				iv_avatar, AppConfigs.AVATAR_OPTIONS,
//				new AnimateFirstDisplayListener());
//		iv_avatar.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				IntentUtil.start_result_activity(getActivity(),
//                        UserCenterActivity.class);
//			}
//		});
//            tv_user_name.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    IntentUtil.start_result_activity(getActivity(),
//                            UserCenterActivity.class);
//                }
//            });
////          tv_lv.setText(getString(R.string.level)+userbean.getLevel());
////            tv_lv.setVisibility(View.VISIBLE);
//            tv_user_name.setVisibility(View.VISIBLE);
//            tv_user_name.setText(XApplication.getInstance().getAccountBean().getUsername());
//		refreshSighTx();
//		}
//	}
//
//	public void refreshMsgNum() {
//		if (LoginUtil.isAccountLogin()) {
//			if (accountBean.getMsgNum() > 0) {
//				iv_msg_num.setVisibility(View.VISIBLE);
//			} else {
//				iv_msg_num.setVisibility(View.GONE);
//			}
//		}
//	}
//
//	void refreshSighTx() {
//		if (accountBean.getIsSign()) {
//             tv_sign.setOnClickListener(new OnClickListener() {
//                 @Override
//                 public void onClick(View arg0) {
//                     // TODO Auto-generated method stub
//                     Toast.makeText(getActivity(), "今天已签到", Toast.LENGTH_SHORT).show();
//                 }
//             });
//            tv_sign.setText("已连续签到"+userbean.getSigntimes()+"天");
//             changeSignLeftIcon(R.drawable.iv_signed);
//        } else {
//			accountBean.setIsSign(false);
//             tv_sign.setText(XApplication.getInstance().getString(R.string.menu_sign_in));
//
//            tv_sign.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					signIn();
//				}
//			});
//            changeSignLeftIcon(R.drawable.iv_sign_sel);
//
//        }
//	}
//
//    private void changeSignLeftIcon(int res){
//        Drawable left_draw = XApplication.getInstance().getResources().getDrawable(res);
//        if(res==R.drawable.iv_signed) {
//            left_draw.setBounds(0, 0, left_draw.getMinimumWidth()/3*2, left_draw.getMinimumHeight()/3*2);
//        }else{
//            left_draw.setBounds(0, 0, left_draw.getMinimumWidth(), left_draw.getMinimumHeight());
//        }
//        tv_sign.setCompoundDrawables(left_draw, null, null, null);
//
//     }
//
//	/**
//	 * 注销后刷新
//	 */
//	public void LoginOut() {
//		// list.clear();
//		// getData();
//		beforeLogin();
//		isFillData = false;
//        accountBean.setLogintime(0);
//
//        LoginUtil.LogoutAccount();
//
//        accountBean = XApplication.getInstance().getAccountBean();
//		Toast.makeText(getActivity(), "已登出", Toast.LENGTH_SHORT).show();
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.iv_menu_setting:
//			IntentUtil.start_activity(getActivity(), SettingActivity.class);
//			break;
//		case R.id.iv_menu_post:
//			IntentUtil.start_activity(getActivity(), PostNewsActivity.class);
//			break;
//		default:
//			break;
//		}
//	}
//    public void changeAvantar() {
//        if(LoginUtil.isAccountLogin())
//        ImageLoader.getInstance().displayImage(accountBean.getPhotoUrl(), iv_avatar);
//    }
//
//
//}
