package com.meidebi.app.base.config;

public class HttpUrlBackup {
	// "http://a.meidebi.com/"
	// 192.168.1.168
	public static final String BASIC_URL = "http://a.meidebi.com/";

	public static final String BASIC_URL2 = "http://api.meidebidev.com/";

	// 登陆
	public static final String LOGIN_URL = BASIC_URL + "User-login.html";
	// 注册regist
	public static final String REGIST_URL = BASIC_URL + "User-reg.html";
	// 第三方登陆
	public static final String TB_OAUTHLOGIN = BASIC_URL
			+ "User-tbauthlogin.html";// 淘宝
	public static final String WB_OAUTHLOGIN = BASIC_URL
			+ "User-weiboauthlogin.html";// 微博
	public static final String QQ_OAUTHLOGIN = BASIC_URL
			+ "User-qqauthlogin.html";// 微博
	public static final String USER_AOTUREG = BASIC_URL + "User-aotureg.html";// 跳过注册
	// 首页
	public static final String CAT_GET_CAT_URL = BASIC_URL + "Link-getcatgorys";// 获取分类
	// 热门单品列表
	private static final String CAT_HOT_LIST_SINGLE_URL = BASIC_URL
			+ "Link-dphotlist";
	// 热门活动列表
	private static final String CAT_HOT_LIST_ACTIVE_URL = BASIC_URL
			+ "Link-achotlist";
	// 热门优惠券列表
	private static final String CAT_HOT_LIST_COUPON_URL = BASIC_URL
			+ "Link-vohotlist";
	// 热门活动列表
	public static final String CAT_HOT_LIST_All_URL = BASIC_URL
			+ "Link-allhotlist";
	// 主页banner
	public static final String INDEX_BANNER = BASIC_URL + "Link-showactive";
	//
	public static final String ACTIVE_GETCOUPONS = BASIC_URL
			+ "Active-getcoupons";

	// 单品列表
	private static final String CAT_LIST_SINGLE_URL = BASIC_URL + "Link-dplist";
	// 活动列表
	private static final String CAT_LIST_ACTIVE_URL = BASIC_URL + "Link-aclist";
	// 优惠券列表
	private static final String CAT_LIST_COUPON_URL = BASIC_URL + "Link-volist";
	// 全部列表
	public static final String CAT_LIST_All_URL = BASIC_URL + "Link-alllist";
	// 搜索
	public static final String SEARCH_GET_HOTKEY_URL = BASIC_URL
			+ "Link-gethotwords.html";// 热词
	public static final String SEARCH_URL = BASIC_URL + "Link-search.html";// 搜索
	// 详情
	public static final String MSG_DETAIL_URL = BASIC_URL + "Link-onelink";
	public static final String MSG_DETAIL_VOTE_URL = BASIC_URL
			+ "Comment-dovote.html";// 赞 弱
	// public static final String MSG_DETAIL_COMMENTLIST_URL = BASIC_URL
	// + "Comment-commentlist.html";
	public static final String MSG_DETAIL_COMMENTLIST_URL = BASIC_URL
			+ "Comment-comlist.html";
	public static final String MSG_DETAIL_SUBMIT_COMMENT_URL = BASIC_URL
			+ "Comment-index.html";// 添加评论

	public static final String GET_SHAREWORDS_URL = BASIC_URL
			+ "Link-getshare.html";

	public static final String GET_UNIONSHAREWORDS_URL = BASIC_URL
			+ "User-unionshare.html";

	// 推送
	// public static final String PUSH_INIT_URL=
	// BASIC_URL+"Appconfig-usersubmit.html";
	public static final String PUSH_INIT_URL = BASIC_URL
			+ "Appconfig-usersubmit.html";

	// public static final String PUSH_GETCONFIG_URL=
	// BASIC_URL+"Appconfig-getusercate.html";
	public static final String PUSH_GETCONFIG_URL = BASIC_URL
			+ "Appconfig-getjpushcate.html";

	// public static final String PUSH_SETLOCATION_URL=
	// BASIC_URL+"Appconfig-setaddress.html";
	public static final String PUSH_SETLOCATION_URL = BASIC_URL
			+ "Appconfig-setjpushaddress";

	// public static final String PUSH_SETCONFIG_URL=
	// BASIC_URL+"Appconfig-setusercate.html";

	public static final String PUSH_SETCONFIG_URL = BASIC_URL
			+ "Appconfig-setjpushcate";

	public static final String PUSH_SUBMITTAG_URL = BASIC_URL
			+ "Appconfig-successjpush";

	public static final String PUSH_MANUAL_LOCATION_URL = BASIC_URL
			+ "User-getcity.html";

	public static final String UPLOAD_IMAGE_TEXT = BASIC_URL + "Ipad-upload";

	// 表情
	public static final String EMO_URL = "http://img.meidebi.com/mdbcss/public/Kindeditor/plugins/emoticons/images/";

	public static final String PRE_EMO_URL = "<img src=\"http://img.meidebi.com/mdbcss/public/Kindeditor/plugins/emoticons/images/";

	public static final String PRE_EMO_URL2 = "<img src=\"http://css.meidebi.com/js/lib/Kindeditor/plugins/emoticons/images/";

	public static final String LAST_EMO_URL = "\" border=\"0\" alt=\"\" />";

	// 个人中心
	public static final String USER_CENTER_URL = BASIC_URL + "User-usercenter";

	public static final String USER_SIGNIN = BASIC_URL + "User-dosign";
	// 收藏
	public static final String DETAIL_FAV = BASIC_URL + "Ipad-unfavorite";
	public static final String GET_MY_FAV = BASIC_URL
			+ "User-usercenter-type-4";
	// 奖励
	public static final String SHARE_AWARD = BASIC_URL + "User-coinshare";
	//
	public static final String BAIDU_GEOCODER_URL = "http://api.map.baidu.com/geocoder";

	// 热门单品列表
	public static final String GET_CHANEL_DATA = BASIC_URL
			+ "Link-mainsingle.html";

	// 优惠券交易
	public static final String COUPONLIST_URL = BASIC_URL
			+ "Link-mainsingle-type-quan-limit-10";

	public static final String COUPONSITE_URL = BASIC_URL + "Link-couponssite";

	public static final String COUPON_DETAI_URL = BASIC_URL + "Link-onecoupon";
	public static final String COUPON_EXCHAGE_URL = BASIC_URL
			+ "User-dealcoupon";
	public static final String GET_MY_COUPON_URL = BASIC_URL
			+ "User-mycoupon.html";
	// 晒单
	public static final String ORDERSHOW_URL = BASIC_URL + "Link-showdanlist";
	// 消息中心
	public static final String USER_MSGCENTER_URL = BASIC_URL
			+ "User-mymessage";
	public static final String USER_READMSG_URL = BASIC_URL
			+ "User-readmessage";
	public static final String USER_UPLOADAVATAR_URL = BASIC_URL
			+ "User-avatar";

	public static final String POST_NEWS = BASIC_URL + "User-link";

	// * 获取商品列表的url地址
	// * @return String url地址
	// */
	// public static String getCatListUrl() {
	// PublicDataBean dataBean = PublicDataBean.getInstance();
	// String url=CAT_LIST_All_URL;
	// if (dataBean.getCatType() == 1) { // 全部 ，不热门
	// switch (dataBean.getCatChooseId()) {
	// case 1://全部
	// url=CAT_LIST_All_URL;
	// break;
	// case 2: //单品
	// url=CAT_LIST_SINGLE_URL;
	// break;
	// case 3: //活动
	// url=CAT_LIST_ACTIVE_URL;
	// break;
	// case 4://优惠券
	// url=CAT_LIST_COUPON_URL;
	// break;
	// }
	// }
	// if (dataBean.getCatType()==2) {
	// switch (dataBean.getCatChooseId()) {
	// case 1:
	// url=CAT_HOT_LIST_All_URL;
	// break;
	// case 2:
	// url=CAT_HOT_LIST_SINGLE_URL;
	// break;
	// case 3:
	// url=CAT_HOT_LIST_ACTIVE_URL;
	// break;
	// case 4:
	// url=CAT_HOT_LIST_COUPON_URL;
	// break;
	// }
	// }
	// return url;
	// }

	/**
	 * 获取商品列表的url地址
	 * 
	 * @return String url地址
	 * @param typeId
	 *            分类类型
	 * @param isAll
	 *            是否全部
	 */
	public static String getCatListUrl(int typeId, Boolean isAll) {
		String url = CAT_LIST_All_URL;
		if (isAll) { // 全部 ，不热门
			switch (typeId) {
			case 1:// 全部
				url = CAT_LIST_All_URL;
				break;
			case 2: // 单品
				url = CAT_LIST_SINGLE_URL;
				break;
			case 3: // 活动
				url = CAT_LIST_ACTIVE_URL;
				break;
			case 4:// 优惠券
				url = CAT_LIST_COUPON_URL;
				break;
			}
		} else
			switch (typeId) {
			case 1:
				url = CAT_HOT_LIST_All_URL;
				break;
			case 2:
				url = CAT_HOT_LIST_SINGLE_URL;
				break;
			case 3:
				url = CAT_HOT_LIST_ACTIVE_URL;
				break;
			case 4:
				url = CAT_HOT_LIST_COUPON_URL;
				break;
			}
		return url;
	}

	public static String getAvantarUrl(String uid, AvatarType type) {
		String url = "http://bbs.meidebi.com/uc_server/avatar.php?uid=" + uid
				+ "&type=virtual&size=";
		switch (type) {
		case Large:
			url = url + "big";
			break;
		case Middle:
			url = url + "middle";
			break;
		case Small:
			url = url + "small";
			break;
		default:
			break;
		}
		return url;
	}
}
