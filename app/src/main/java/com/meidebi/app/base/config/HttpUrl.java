package com.meidebi.app.base.config;

public class HttpUrl {
	// "http://a.meidebi.com/"
	// 192.168.1.168
	public static final String BASIC_URL = "http://a.meidebi.com/";
    //public static final String BASIC_URL = "http://192.168.1.249:8088";

	public static final String BASIC_URL2 = "http://api.meidebidev.com/";

	// 登陆
	public static final String LOGIN_URL = BASIC_URL + "Customer-login.html";

    public static final String LOGIN_FIND_PS_URL = BASIC_URL + "User-findpass.html";

    // 注册regist
	public static final String REGIST_URL = BASIC_URL + "Customer-reg.html";
	// 第三方登陆
	public static final String TB_OAUTHLOGIN = BASIC_URL
			+ "Customer-tbauthlogin.html";// 淘宝
	public static final String WB_OAUTHLOGIN = BASIC_URL
			+ "Customer-weiboauthlogin.html";// 微博
	public static final String QQ_OAUTHLOGIN = BASIC_URL
			+ "Customer-qqauthlogin.html";// 微博
	public static final String USER_AOTUREG = BASIC_URL + "Customer-aotureg.html";// 跳过注册
	// 首页
	public static final String CAT_GET_CAT_URL = BASIC_URL + "Share-getcatgorys";// 获取分类
	// 热门单品列表
	private static final String CAT_HOT_LIST_SINGLE_URL = BASIC_URL
			+ "Share-dphotlist";
	// 热门活动列表
	private static final String CAT_HOT_LIST_ACTIVE_URL = BASIC_URL
			+ "Share-achotlist";
	// 热门优惠券列表
	private static final String CAT_HOT_LIST_COUPON_URL = BASIC_URL
			+ "Share-vohotlist";
	// 热门活动列表
	public static final String CAT_HOT_LIST_All_URL = BASIC_URL
			+ "Share-allhotlist";
	// 主页banner
	public static final String INDEX_BANNER = BASIC_URL + "Share-getslide";
	//
	public static final String ACTIVE_GETCOUPONS = BASIC_URL
			+ "Active-getcoupons";

	// 单品列表
	private static final String CAT_LIST_SINGLE_URL = BASIC_URL + "Share-dplist";
	// 活动列表
	private static final String CAT_LIST_ACTIVE_URL = BASIC_URL + "Share-aclist";
	// 优惠券列表
	private static final String CAT_LIST_COUPON_URL = BASIC_URL + "Share-volist";
	// 全部列表
	public static final String CAT_LIST_All_URL = BASIC_URL + "Share-alllist";
	// 搜索
	public static final String SEARCH_GET_HOTKEY_URL = BASIC_URL
			+ "Share-gethotwords.html";// 热词
	public static final String SEARCH_URL = BASIC_URL + "Share-search";// 搜索
	// 详情
	public static final String MSG_DETAIL_URL = BASIC_URL + "Share-onelink";
	public static final String MSG_DETAIL_VOTE_URL = BASIC_URL
			+ "Discuss-dovote.html";// 赞 弱
	// public static final String MSG_DETAIL_COMMENTLIST_URL = BASIC_URL
	// + "Discuss-commentlist.html";
	public static final String MSG_DETAIL_COMMENTLIST_URL = BASIC_URL
			+ "Discuss-clist.html";
	public static final String MSG_DETAIL_SUBMIT_COMMENT_URL = BASIC_URL
			+ "Discuss-com.html";// 添加评论
 
	public static final String GET_SHAREWORDS_URL = BASIC_URL
			+ "Share-getshare.html";

	public static final String GET_UNIONSHAREWORDS_URL = BASIC_URL
			+ "Customer-unionshare.html";

	// 推送
	// public static final String PUSH_INIT_URL=
	// BASIC_URL+"Pushconfig-usersubmit.html";
	public static final String PUSH_INIT_URL = BASIC_URL
			+ "Pushconfig-usersubmit.html";

	// public static final String PUSH_GETCONFIG_URL=
	// BASIC_URL+"Pushconfig-getusercate.html";
	public static final String PUSH_GETCONFIG_URL = BASIC_URL
			+ "Pushconfig-getjpushcate.html";

	// public static final String PUSH_SETLOCATION_URL=
	// BASIC_URL+"Pushconfig-setaddress.html";
	public static final String PUSH_SETLOCATION_URL = BASIC_URL
			+ "Pushconfig-setjpushaddress";



	// public static final String PUSH_SETCONFIG_URL=
	// BASIC_URL+"Pushconfig-setusercate.html";

	public static final String PUSH_SETCONFIG_URL = BASIC_URL
			+ "Pushconfig-setjpushcate";

	public static final String PUSH_SUBMITTAG_URL = BASIC_URL
			+ "Pushconfig-successjpush";

	public static final String PUSH_MANUAL_LOCATION_URL = BASIC_URL
			+ "Customer-getcity.html";

	public static final String UPLOAD_IMAGE_TEXT = BASIC_URL + "Share-upload";

	// 表情
	public static final String EMO_URL = "http://img.meidebi.com/mdbcss/public/Kindeditor/plugins/emoticons/images/";

	public static final String PRE_EMO_URL = "<img src=\"http://img.meidebi.com/mdbcss/public/Kindeditor/plugins/emoticons/images/";

	public static final String PRE_EMO_URL2 = "<img src=\"http://css.meidebi.com/js/lib/Kindeditor/plugins/emoticons/images/";

	public static final String LAST_EMO_URL = "\" border=\"0\" alt=\"\" />";

	// 个人中心
	public static final String USER_CENTER_URL = BASIC_URL + "Customer-usercenter";

	public static final String USER_SIGNIN = BASIC_URL + "Customer-dosign";

    public static final String USER_MY_LINK = BASIC_URL + "User-mylink.html";

    public static final String USER_MY_ORDERSHOW = BASIC_URL + "User-myshoppingexp.html";


    // 收藏
	public static final String DETAIL_FAV = BASIC_URL + "Customer-favorite";
	public static final String GET_MY_FAV = BASIC_URL
			+ "Customer-usercenter-type-4";
	// 奖励
	public static final String SHARE_AWARD = BASIC_URL + "Customer-coinshare";
	//
	public static final String BAIDU_GEOCODER_URL = "http://api.map.baidu.com/geocoder";

	// 热门单品列表
	public static final String GET_CHANEL_DATA = BASIC_URL
			+ "Share-mainsingle.html";

	// 优惠券交易
	public static final String COUPONLIST_URL = BASIC_URL
			+ "Share-mainsingle-type-quan-limit-10";

	public static final String COUPONSITE_URL = BASIC_URL + "Share-couponssite";

	public static final String COUPON_DETAI_URL = BASIC_URL + "Share-onecoupon";
	public static final String COUPON_EXCHAGE_URL = BASIC_URL
			+ "Customer-dealcoupon";
	public static final String GET_MY_COUPON_URL = BASIC_URL
			+ "Customer-mycoupon";
	// 晒单
	public static final String ORDERSHOW_URL = BASIC_URL + "Share-showdanlist";
	// 消息中心
	public static final String USER_MSGCENTER_URL = BASIC_URL
			+ "Customer-mymessage";
	public static final String USER_READMSG_URL = BASIC_URL
			+ "Customer-readmessage";
	public static final String USER_UPLOADAVATAR_URL = BASIC_URL
			+ "Customer-avatar";

	public static final String POST_NEWS = BASIC_URL + "Customer-link";


    public static final String PUSH_UMENG_CONFIG = BASIC_URL + "Appconfig-umengconfig.html";

    public static final String PUSH_UMENG_GETCONFIG = BASIC_URL + "Appconfig-getumengconfig.html";


    public static final String PUSH_UMENG_BINDADRESS = BASIC_URL + "Appconfig-setumengaddress.html";

    //upolad

    public static final String UPLOAD_GET_TOKEN = BASIC_URL + "User-uploadtoken";
    public static final String UPLOAD_SUBMIT_SHOW = BASIC_URL + "User-savese";


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
