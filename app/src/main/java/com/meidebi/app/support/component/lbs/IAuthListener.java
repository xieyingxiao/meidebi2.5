package com.meidebi.app.support.component.lbs;

import com.meidebi.app.service.dao.user.SocailPlatform;
import com.taobao.top.android.auth.AuthError;
import com.taobao.top.android.auth.AuthException;

public interface IAuthListener {
	/**
	 * 授权完成时回调此方法
	 * 
	 * @param accessToken
	 */
	void onInfoComplete(String accessToken, String uid, String nickname, SocailPlatform method);

	/**
	 * 授权失败时回调此方法<br>
	 * 例如：用户拒绝授权等
	 * 
	 * @param e
	 */
	void onError(AuthError e);

	/**
	 * 用户中断了授权操作回调此方法
	 * 
	 */
	void onCancel();

	/**
	 * 出现一些系统异常是回调此方法<br>
	 * 例如：因为网络问题，页面无法打开等
	 * 
	 * @param e
	 */
	void onAuthException(AuthException e);
	
	void onIdComplete(String uid,String name,String imageUrl,String token, SocailPlatform method);

    void onLoadUserComplete(String userName,String headerImage);
}
