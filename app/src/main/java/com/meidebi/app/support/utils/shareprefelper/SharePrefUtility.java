package com.meidebi.app.support.utils.shareprefelper;

import android.content.Context;
import android.text.TextUtils;
import com.meidebi.app.XApplication;
import com.meidebi.app.support.utils.Net;
import com.meidebi.app.support.utils.Utility;
import com.meidebi.app.ui.setting.SettingActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 设置
 * 
 * @author mdb-ii
 * 
 */
public class SharePrefUtility {

	private static final String FIRSTSTART = "firststart3.0";
    private static final String ClearCache = "clear_cache3.1";

    private static final String INDEX_JSON = "indexjson";
	private static final String SEARCH_HISTORY = "search_history";
	private static final String PUSH_START_TIME = "push_start_time";
	private static final String PUSH_END_TIME = "push_end_time";
	private static final String PUSHSILENT = "push_silent";
	private static final String PUSHSOUND = "push_sound";
	private static final String PUSHVB = "push_vb";
	private static final String PUSHOEPN = "push_on";
	private static final String PUSHFIRSTOEPN = "push_first_on";
	private static final String PUSHLOC = "PUSH_LOC";

    private static final String CAT_LIST = "CAT_LIST";
	private static final String POST_FIRST_GUIDE = "post_first_guide";
    private static final String UMENG_ID = "UMENG_ID";

	private SharePrefUtility() {

	}

    public static void setUMENGID(String id) {
        SharePrefHelper.setEditor(getContext(), UMENG_ID, id);
    }

    public static String  getUMENGID() {
        return SharePrefHelper.getSharedPreferences(getContext(), UMENG_ID, "");
    }

	public static void setDefaultAccountId(String id) {
		SharePrefHelper.setEditor(getContext(), "id", id);
	}

	public static String getDefaultAccountId() {
		return SharePrefHelper.getSharedPreferences(getContext(), "id", "");
	}

	public static void setDefaultAccountName(String name) {
		SharePrefHelper.setEditor(getContext(), "username", name);
	}

	public static String getDefaultAccountName() {
		return SharePrefHelper.getSharedPreferences(getContext(), "username",
				"");
	}

	public static void setAvatarUrl(String url) {
		SharePrefHelper.setEditor(getContext(), "avatar", url);
	}

	public static String getAvatarUrl() {
		return SharePrefHelper.getSharedPreferences(getContext(), "avatar", "");
	}

	public static void setLevel(String url) {
		SharePrefHelper.setEditor(getContext(), "level", url);
	}

	public static String getLevel() {
		return SharePrefHelper.getSharedPreferences(getContext(), "level", "");
	}

	public static void setCredits(String url) {
		SharePrefHelper.setEditor(getContext(), "credits", url);
	}

	public static String getCredits() {
		return SharePrefHelper.getSharedPreferences(getContext(), "credits",
				"0");
	}

	public static void setMoney(String url) {
		SharePrefHelper.setEditor(getContext(), "money", url);
	}

	public static String getMoney() {
		return SharePrefHelper.getSharedPreferences(getContext(), "money", "0");
	}

	private static Context getContext() {
		return XApplication.getInstance();
	}

	public static boolean firstStart() {
		boolean value = SharePrefHelper.getSharedPreferences(getContext(),
				FIRSTSTART, true);
		if (value)
			SharePrefHelper.setEditor(getContext(), FIRSTSTART, false);
		return value;
	}

    public static boolean needClearCache() {
        boolean value = SharePrefHelper.getSharedPreferences(getContext(),
                ClearCache, true);
        if (value)
            SharePrefHelper.setEditor(getContext(), ClearCache, false);
        return value;
    }

	public static boolean firshSetPush() {
		boolean value = SharePrefHelper.getSharedPreferences(getContext(),
				PUSHFIRSTOEPN, true);
		if (value)
			SharePrefHelper.setEditor(getContext(), PUSHFIRSTOEPN, false);
		return value;
	}

	public static void setIndexCache(String json) {
		SharePrefHelper.setEditor(getContext(), INDEX_JSON, json);
	}

	public static String getIndexCache() {
		return SharePrefHelper.getSharedPreferences(getContext(), INDEX_JSON,
				"");
	}

	public static String[] getSearchHistory() {
		String search_Str = SharePrefHelper.getSharedPreferences(getContext(),
				SEARCH_HISTORY, "");
		return search_Str.split(",");
	}

	public static void SaveSearchHistory(String keyword) {
		String his_Str = SharePrefHelper.getSharedPreferences(getContext(),
				SEARCH_HISTORY, "");
		Pattern p = Pattern.compile("[.,\"\\?!:';-]");// 增加对应的标点
		Matcher m = p.matcher(keyword);
		keyword = m.replaceAll("");
		p = Pattern.compile(" {2,}");// 去除多余空格
		m = p.matcher(keyword);
		keyword = m.replaceAll(" ");
		String[] hisArrays = getSearchHistory();
		for (int i = 0; i < hisArrays.length; i++) {
			if (hisArrays[i].equals(keyword)) {
				// return hisArrays;
				return;
			}
		}
		StringBuilder sb = new StringBuilder(keyword);
		sb.append("," + his_Str);
		SharePrefHelper.setEditor(getContext(), SEARCH_HISTORY, sb.toString());
		// return sb.toString().split(",");
	}

	public static void RemoveHistoyOneItem(String keyword) {
		String his_Str = SharePrefHelper.getSharedPreferences(getContext(),
				SEARCH_HISTORY, "");
		his_Str = his_Str.replace(keyword, "");
		SharePrefHelper.setEditor(getContext(), SEARCH_HISTORY, his_Str);
	}

	public static void ClearAllHistoy() {
		SharePrefHelper.setEditor(getContext(), SEARCH_HISTORY, "");
	}

	public static void setDefaultSoftKeyBoardHeight(int height) {
		SharePrefHelper.setEditor(getContext(), "default_softkeyboard_height",
				height);
	}

	public static int getDefaultSoftKeyBoardHeight() {
		return SharePrefHelper.getSharedPreferences(getContext(),
				"default_softkeyboard_height", 400);
	}

	public static String getUUID() {
		String uuid = SharePrefHelper.getSharedPreferences(getContext(),
				"UUID", "");
		if (TextUtils.isEmpty(uuid)) {
			return Utility.getTimesamp();
		} else {
			return uuid;
		}
	}

	// push start
	public static void setUUID(String uuid) {
		SharePrefHelper.setEditor(getContext(), "UUID", uuid);
	}

	public static void setPushSilentMode(Boolean on) {
		SharePrefHelper.setEditor(getContext(),
				SettingActivity.PUSH_SELIENTIME, on);
	}

	public static Boolean getPushSilentMode() {
		return SharePrefHelper.getSharedPreferences(getContext(),
				SettingActivity.PUSH_SELIENTIME, false);
	}

	public static void setPushTime(int starTime, int endTime) {
		SharePrefHelper.setEditor(getContext(), PUSH_START_TIME, starTime);
		SharePrefHelper.setEditor(getContext(), PUSH_END_TIME, endTime);
	}

	public static int getPushStartTime() {
		return SharePrefHelper.getSharedPreferences(getContext(),
				PUSH_START_TIME, 0);
	}

	public static int getPushEndTime() {
		return SharePrefHelper.getSharedPreferences(getContext(),
				PUSH_END_TIME, 0);
	}

	public static Boolean getPushAIMode() {
		return SharePrefHelper.getSharedPreferences(getContext(),
				SettingActivity.PUSH_AIMODE, true);
	}

	public static void setPushSound(Boolean on) {
		SharePrefHelper.setEditor(getContext(), PUSHSOUND, on);
	}

	public static Boolean getPushSound() {
		return SharePrefHelper.getSharedPreferences(getContext(),
				SettingActivity.PUSH_SOUND, true);
	}

	public static void setPushVB(Boolean on) {
		SharePrefHelper.setEditor(getContext(), PUSHVB, on);
	}

	public static Boolean getPushVB() {
		return SharePrefHelper.getSharedPreferences(getContext(),
				SettingActivity.PUSH_VIBRATE, true);
	}

	public static void setPushOn(Boolean on) {
		SharePrefHelper.setEditor(getContext(), SettingActivity.PUSH_ON, on);
	}

	public static Boolean getPushOn() {
		return SharePrefHelper.getSharedPreferences(getContext(),
				SettingActivity.PUSH_ON, true);
	}

	public static void setPushLoc(String loc) {
		SharePrefHelper.setEditor(getContext(), PUSHLOC, loc);
	}

	public static String getPushLoc() {
		return SharePrefHelper.getSharedPreferences(getContext(), PUSHLOC, "");
	}


    public static void setPushLocEnble(Boolean loc) {
        SharePrefHelper.setEditor(getContext(), SettingActivity.PUSH_LOC, loc);
    }

    public static Boolean getPushLocEnble() {
        return SharePrefHelper.getSharedPreferences(getContext(), SettingActivity.PUSH_LOC, false);
    }

	// push end

	// init data
	public static void setCatList(String loc) {
		SharePrefHelper.setEditor(getContext(), CAT_LIST, loc);
	}

	public static String getCatList() {
		return SharePrefHelper.getSharedPreferences(getContext(), CAT_LIST, "");
	}

	// init data end
	// setting start
	public static boolean getEnablePic() {
		return SharePrefHelper.getSharedPreferences(getContext(),
				SettingActivity.ENBLE_PIC, true) || Net.isWifi(getContext());
	}
	
	public static boolean firstPostGuide() {
		boolean value = SharePrefHelper.getSharedPreferences(getContext(),
				POST_FIRST_GUIDE, true);
		if (value)
			SharePrefHelper.setEditor(getContext(), POST_FIRST_GUIDE, false);
		return value;
	}

    public static boolean getAutoRefresh() {
        return SharePrefHelper.getSharedPreferences(getContext(),
                SettingActivity.AUTO_REFRESH, false );
    }
}
