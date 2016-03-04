package com.meidebi.app.base.config;

import android.graphics.Bitmap;
import android.text.style.ForegroundColorSpan;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.support.utils.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.regex.Pattern;

public class AppConfigs {

    public static String DB_NAME = "meidebi.db";
    public static String TABLE_PACKAGE = "com.meidebi.app.service.greendao";
    public static String APP_NAME = "没得比";
    public static String APP_SITE = "http://www.meidebi.com/";
    public static String TOKENDIR = "sdcard/Android/.mmdtoken";
    public static String LOGIN_LOG = "sdcard/Android/.mlogin.log";

    public static String CRASH_REPORT = "sdcard/Android/.CRASH_REPORT";
    public static Boolean OPENBANNER = true;
    public static String SECRETKEY = "432814F4C831E8FE74BB84FB71A727B9";
    public static int Content_ListCacheTime = 5;
    public static int Content_ContentCacheTime = 60 * 24 * 3;
    public static int ImageCacheTime = 60 * 24 * 15;
    public static int Content_DefaultCacheTime = 60 * 24 * 3;
    public static final int unload_bg = R.drawable.iv_no_img;

    public static final int Loading_List_Img_Bg_Small = R.drawable.btn_gery_sel;
    public static final int Loading_List_Img_Bg_Mid = R.drawable.iv_middle_bg;
    public static final int AVANTAR_DEFAULT = R.drawable.iv_no_avantar;
    public static DisplayImageOptions IMG_SMALLOPTIONS = new DisplayImageOptions.Builder()
            .showImageOnLoading(AppConfigs.Loading_List_Img_Bg_Small)
            .showImageForEmptyUri(AppConfigs.unload_bg)
            .showImageOnFail(AppConfigs.Loading_List_Img_Bg_Small)
            .cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
            .build();

    public static DisplayImageOptions AVATAR_OPTIONS = new DisplayImageOptions.Builder()
            .showImageOnLoading(AppConfigs.AVANTAR_DEFAULT)
            .showImageForEmptyUri(AppConfigs.unload_bg)
            .showImageOnFail(AppConfigs.AVANTAR_DEFAULT).cacheInMemory(true)
            .cacheOnDisc(true).resetViewBeforeLoading(true)
                    // .displayer(new RoundedBitmapDisplayer(180))

                    // .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .bitmapConfig(Bitmap.Config.RGB_565)

            .considerExifParams(true).build();
    public static DisplayImageOptions IMG_MIDDLEOPTIONS = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.iv_order_show_no_img)
            .showImageOnFail(R.drawable.iv_middle_bg)
            .resetViewBeforeLoading(true).cacheOnDisc(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
            .displayer(new FadeInBitmapDisplayer(300)).build();
    public static DisplayImageOptions WEBIMGOPTIONS = new DisplayImageOptions.Builder()
            // .showImageForEmptyUri(R.drawable.iv_middle_bg)
            // .showImageOnFail(R.drawable.iv_middle_bg)
            .cacheInMemory(false).cacheOnDisc(true).build();
    // .displayer(new FadeInBitmapDisplayer(300)).build();

    public static final Pattern IMG_SRC = Pattern
            .compile("\\<img>(\\S+?)\\</img>");
    public static final Pattern IMG_SRC_OLD = Pattern
            .compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
    public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
    public static final Pattern Ex_html = Pattern.compile("<[^>]+>"); // 定义HTML标签的正则表达式
    public static final Pattern WEB_URL = Pattern
            .compile("http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]");
    public static final Pattern MENTION_URL = Pattern.compile("@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}");
    public static final String MENTION_SCHEME = "com.meidebi.app://";
    public static final Pattern WHOLE_URL = Pattern
            .compile(
                    "(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$",
                    Pattern.CASE_INSENSITIVE);

    public static final String WEB_SCHEME = "http://";
    public static final String WEBVIEW_DB_NAME = "webViewDb";
    public static int SETTING_FRAGMENT_WIDTH = 0;
    public static final int COMMENT_TIME_LIMIT = 5000;
    public static int ScreenHeight = 0;

    public static final String LIST_TEXT = "text";
    public static final String LIST_IMAGEVIEW = "img";

    public static final long CHECKPUSHRUN = 60 * 1000 * 60 * 24;// 频率30分钟
    public static final long STARTALARMCONTROLLER = 60 * 1000 * 60 * 24;// 一天运行一次
    public static final int WORKDAYPUSHSTATTIME = 8;// 工作日开始时间
    public static final int WORKDAYPUSHENDTIME = 22;//
    public static final int WEEKENDPUSHSTATTIME = 9;// 周末开始时间
    public static final int WEEKENDPUSHENDTIME = 22;//
    public static final int WEEKENDPUSHENDTIMEMIN = 30;//


    public static int cover_height = 0;
    public static int screen_width = 0;
    public static int screen_height = 0;


    public static int getCoverHeight() {
        if (cover_height == 0) {
            double size = (double) (Math
                    .round((Utility.getScreenWidth(XApplication.getInstance()
                    ) - (XApplication.getInstance().getResources().getDimensionPixelSize(R.dimen.layout_padding) * 2))
                            * 100.0 / 686) / 100.0);
            cover_height = (int) (320 * size);
        }
        return cover_height;
    }

    public static int getScreenWidth() {
        if (screen_width == 0) {
            screen_width = Utility.getScreenWidth(XApplication.getInstance());
        }
        return screen_width;
    }

    public static int getScreenHeight() {
        if (screen_height == 0) {
            screen_height = Utility.getScreenHeight(XApplication.getInstance());
        }
        return screen_height;
    }

    public static int upload_token = 0;

    public static ForegroundColorSpan SPAN_USER_COLOR = new ForegroundColorSpan(XApplication.getInstance().getResources().getColor(R.color.text_black_color));
}

