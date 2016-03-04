package com.meidebi.app.support.utils.component;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.service.bean.user.AccountBean;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.user.LoginActivity;
import com.orm.SugarRecord;

public class LoginUtil {

	public static Boolean isAccountLogined() {
		AccountBean bean = XApplication.getInstance().getAccountBean();
		if (bean != null) {
			if (bean.getIsLogin() && !TextUtils.isEmpty(bean.getUserKey())) {
				return true;
			}
		}
		return false;
	}

//	public static void initAccountBean() {
//		if (!TextUtils.isEmpty(SharePrefUtility.getDefaultAccountId())) {
//			XApplication.getInstance().getAccountBean()
//					.setUserKey(SharePrefUtility.getDefaultAccountId());
//			XApplication.getInstance().getAccountBean().setIsLogin(true);
//			XApplication.getInstance().getAccountBean()
//					.setUsername(SharePrefUtility.getDefaultAccountName());
//			XApplication.getInstance().getAccountBean()
//					.setPhotoUrl((SharePrefUtility.getAvatarUrl()));
//			XApplication.getInstance().getAccountBean()
//					.setLevel((SharePrefUtility.getLevel()));
//			XApplication.getInstance().getAccountBean()
//					.setScore((SharePrefUtility.getCredits()));
//			XApplication.getInstance().getAccountBean()
//					.setMoney(SharePrefUtility.getMoney());
//		}
//	}

	public static Boolean isAccountLogin(Activity activity) {
		AccountBean bean = XApplication.getInstance().getAccountBean();
		if (bean.getIsLogin() && !TextUtils.isEmpty(bean.getUserKey())) {
			AppLogger.e("userkey"+bean.getUserKey());
			return true;
		}  else {
			Toast.makeText(activity,
					activity.getString(R.string.please_first_login),
					Toast.LENGTH_SHORT).show();
			IntentUtil.start_result_activity(activity, LoginActivity.class);
		}
		return false;
	}

	public static Boolean isAccountLogin() {
		AccountBean bean = XApplication.getInstance().getAccountBean();
		if (bean.getIsLogin() && !TextUtils.isEmpty(bean.getUserKey())) {
			return true;
		}
		return false;
	}

	public static void saveAccount(String id, String username, String url,
			String userkey) {
		SharePrefUtility.setDefaultAccountId(id);
		SharePrefUtility.setDefaultAccountName(username);
		SharePrefUtility.setAvatarUrl(url);
		XApplication.getInstance().getAccountBean().setId(id);
		XApplication.getInstance().getAccountBean().setUserKey(userkey);
		XApplication.getInstance().getAccountBean().setUsername(username);
		XApplication.getInstance().getAccountBean().setIsLogin(true);
		XApplication.getInstance().getAccountBean().setPhotoUrl(url);
		SugarRecord.save(XApplication.getInstance().getAccountBean());
	}

	public static void saveAccountInfo(String lv, String credits, String money,
			String url) {
		SharePrefUtility.setAvatarUrl(url);
		// SharePrefUtility.setLevel(lv);
		// XApplication.getInstance().getAccountBean().setPhotoUrl(url);
		// XApplication.getInstance().getAccountBean().setLevel(lv);
		if (!TextUtils.isEmpty(money)) {
			// SharePrefUtility.setMoney(money);
			XApplication.getInstance().getAccountBean().setMoney(money);
		}
		if (!TextUtils.isEmpty(credits)) {
			// SharePrefUtility.setCredits(credits);
			XApplication.getInstance().getAccountBean().setScore(credits);
		}
	}

	public static void LogoutAccount() {
		// SugarRecord.deleteAll(AccountBean.class,"id=?",new
		// String[]{XApplication.getInstance().getAccountBean().getId()});
		XApplication.getInstance().getAccountBean().setIsLogin(false);
		SugarRecord.save(XApplication.getInstance().getAccountBean());
        XApplication.getInstance().setAccountBean(null);
		// SharePrefUtility.setDefaultAccountId("");
		// SharePrefUtility.setDefaultAccountName("");
		// SharePrefUtility.setAvatarUrl("");
		// SharePrefUtility.setLevel("");
		// SharePrefUtility.setMoney("");
		// SharePrefUtility.setCredits("");
	}

	public static String getUid() {
		return XApplication.getInstance().getAccountBean().getUserKey();
	}
	
	public static String getId() {
		return XApplication.getInstance().getAccountBean().getId();
	}

}
