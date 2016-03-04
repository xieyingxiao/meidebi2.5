package com.meidebi.app.support.utils.content;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.widget.TextView;

import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.service.bean.comment.CommentBean;
import com.meidebi.app.service.bean.user.MsgCenterBean;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentUtils {
	private static ContentUtils instance = new ContentUtils();
	private Map<String, String> map = new LinkedHashMap<String, String>();
	private Map<String, String> map2 = new LinkedHashMap<String, String>();
    private Map<String, String> map3 = new LinkedHashMap<String, String>();

	public static ContentUtils getInstance() {
		return instance;
	}

	public Map<String, String> getEmotion1() {
		return map;
	}

	public Map<String, String> getEmotion2() {
		return map2;
	}

    public Map<String, String> getEmotion3() {
        return map3;
    }

	public ContentUtils() {

	}

	public void Emotion1() {
               map.put(":default_微笑" ,"smiley_0.png");
               map.put(":default_撇嘴" ,"smiley_1.png");
               map.put(":default_色" ,"smiley_2.png");
               map.put(":default_发呆" ,"smiley_3.png");
               map.put(":default_得意" ,"smiley_4.png");
               map.put(":default_流泪" ,"smiley_5.png");
               map.put(":default_害羞" ,"smiley_6.png");
               map.put(":default_闭嘴" ,"smiley_7.png");
               map.put(":default_睡" ,"smiley_8.png");
               map.put(":default_大哭" ,"smiley_9.png");
               map.put(":default_尴尬" ,"smiley_10.png");
               map.put(":default_发怒" ,"smiley_11.png");
               map.put(":default_调皮" ,"smiley_12.png");
               map.put(":default_龇牙" ,"smiley_13.png");
               map.put(":default_惊讶" ,"smiley_14.png");
               map.put(":default_难过" ,"smiley_15.png");
               map.put(":default_酷" ,"smiley_16.png");
               map.put(":default_冷汗" ,"smiley_17.png");
               map.put(":default_抓狂" ,"smiley_18.png");
               map.put(":default_吐" ,"smiley_19.png");
               map.put(":default_偷笑" ,"smiley_20.png");
               map.put(":default_可爱" ,"smiley_21.png");
               map.put(":default_白眼" ,"smiley_22.png");
               map.put(":default_傲慢" ,"smiley_23.png");
               map.put(":default_饥饿" ,"smiley_24.png");
               map.put(":default_困" ,"smiley_25.png");
               map.put(":default_惊恐" ,"smiley_26.png");
               map.put(":default_流汗" ,"smiley_27.png");
               map.put(":default_憨笑" ,"smiley_28.png");
               map.put(":default_大兵" ,"smiley_29.png");
               map.put(":default_奋斗" ,"smiley_30.png");
               map.put(":default_咒骂" ,"smiley_31.png");
               map.put(":default_疑问" ,"smiley_32.png");
               map.put(":default_嘘" ,"smiley_33.png");
               map.put(":default_晕" ,"smiley_34.png");
               map.put(":default_折磨" ,"smiley_35.png");
               map.put(":default_衰" ,"smiley_36.png");
               map.put(":default_骷髅" ,"smiley_37.png");
               map.put(":default_敲打" ,"smiley_38.png");
               map.put(":default_再见" ,"smiley_39.png");
               map.put(":default_擦汗" ,"smiley_40.png");
               map.put(":default_抠鼻" ,"smiley_41.png");
               map.put(":default_鼓掌" ,"smiley_42.png");
               map.put(":default_糗大了" ,"smiley_43.png");
               map.put(":default_坏笑" ,"smiley_44.png");
               map.put(":default_左哼哼" ,"smiley_45.png");
               map.put(":default_右哼哼" ,"smiley_46.png");
               map.put(":default_哈欠" ,"smiley_47.png");
               map.put(":default_鄙视" ,"smiley_48.png");
               map.put(":default_委屈" ,"smiley_49.png");
               map.put(":default_快哭了" ,"smiley_50.png");
               map.put(":default_阴险" ,"smiley_51.png");
               map.put(":default_亲亲" ,"smiley_52.png");
               map.put(":default_吓" ,"smiley_53.png");
               map.put(":default_可怜" ,"smiley_54.png");
        
        
        
        
        
        
        
        
        
        
        
        
//		map.put(":0.gif", "smiley_0.png");
//		map.put(":1.gif", "smiley_1.png");
//		map.put(":2.gif", "smiley_2.png");
//		map.put(":3.gif", "smiley_3.png");
//		map.put(":4.gif", "smiley_4.png");
//		map.put(":5.gif", "smiley_5.png");
//		map.put(":6.gif", "smiley_6.png");
//		map.put(":7.gif", "smiley_7.png");
//		map.put(":8.gif", "smiley_8.png");
//		map.put(":9.gif", "smiley_9.png");
//		map.put(":10.gif", "smiley_10.png");
//		map.put(":11.gif", "smiley_11.png");
//		map.put(":12.gif", "smiley_12.png");
//		map.put(":13.gif", "smiley_13.png");
//		map.put(":14.gif", "smiley_14.png");
//		map.put(":15.gif", "smiley_15.png");
//		map.put(":16.gif", "smiley_16.png");
//		map.put(":17.gif", "smiley_17.png");
//		map.put(":18.gif", "smiley_18.png");
//		map.put(":19.gif", "smiley_19.png");
//		map.put(":20.gif", "smiley_20.png");
//		map.put(":21.gif", "smiley_21.png");
//		map.put(":22.gif", "smiley_22.png");
//		map.put(":23.gif", "smiley_23.png");
//		map.put(":24.gif", "smiley_24.png");
//		map.put(":25.gif", "smiley_25.png");
//		map.put(":26.gif", "smiley_26.png");
//		map.put(":27.gif", "smiley_27.png");
//		map.put(":28.gif", "smiley_28.png");
//		map.put(":29.gif", "smiley_29.png");
//		map.put(":30.gif", "smiley_30.png");
//		map.put(":31.gif", "smiley_31.png");
//		map.put(":32.gif", "smiley_32.png");
//		map.put(":33.gif", "smiley_33.png");
//		map.put(":34.gif", "smiley_34.png");
//		map.put(":35.gif", "smiley_35.png");
//		map.put(":36.gif", "smiley_36.png");
//		map.put(":37.gif", "smiley_37.png");
//		map.put(":38.gif", "smiley_38.png");
//		map.put(":39.gif", "smiley_39.png");
//		map.put(":40.gif", "smiley_40.png");
//		map.put(":41.gif", "smiley_41.png");
//		map.put(":42.gif", "smiley_42.png");
//		map.put(":43.gif", "smiley_43.png");
//		map.put(":44.gif", "smiley_44.png");
//		map.put(":45.gif", "smiley_45.png");
//		map.put(":46.gif", "smiley_46.png");
//		map.put(":47.gif", "smiley_47.png");
//		map.put(":48.gif", "smiley_48.png");
//		map.put(":49.gif", "smiley_49.png");
//		map.put(":50.gif", "smiley_50.png");
//		map.put(":51.gif", "smiley_51.png");
//		map.put(":52.gif", "smiley_52.png");
//		map.put(":53.gif", "smiley_53.png");
//		map.put(":54.gif", "smiley_54.png");
	}

	public void Emotion2() {
        map2.put(":mdb_哎呀" , "0.png");
                map2.put(":mdb_傲慢" , "1.png");
                map2.put(":mdb_暴怒" , "2.png");
                map2.put(":mdb_鄙视" , "3.png");
                map2.put(":mdb_奋斗" , "4.png");
                map2.put(":mdb_愤怒" , "5.png");
                map2.put(":mdb_干什么" , "6.png");
                map2.put(":mdb_可爱" , "7.png");
                map2.put(":mdb_酷酷" , "8.png");
                map2.put(":mdb_流泪" , "9.png");
                map2.put(":mdb_秒杀" , "10.png");
                map2.put(":mdb_哦错了" , "11.png");
                map2.put(":mdb_伤心" , "12.png");
                map2.put(":mdb_生气" , "13.png");
                map2.put(":mdb_思索" , "14.png");
                map2.put(":mdb_微笑" , "15.png");
                map2.put(":mdb_委屈" , "16.png");
                map2.put(":mdb_无语" , "17.png");







//
//		map2.put(":2_0.gif", "0.png");
//		map2.put(":2_1.gif", "1.png");
//		map2.put(":2_2.gif", "2.png");
//		map2.put(":2_3.gif", "3.png");
//		map2.put(":2_4.gif", "4.png");
//		map2.put(":2_5.gif", "5.png");
//		map2.put(":2_6.gif", "6.png");
//		map2.put(":2_7.gif", "7.png");
//		map2.put(":2_8.gif", "8.png");
//		map2.put(":2_9.gif", "9.png");
//		map2.put(":2_10.gif", "10.png");
//		map2.put(":2_11.gif", "11.png");
//		map2.put(":2_12.gif", "12.png");
//		map2.put(":2_13.gif", "13.png");
//		map2.put(":2_14.gif", "14.png");
//		map2.put(":2_15.gif", "15.png");
//		map2.put(":2_16.gif", "16.png");
//		map2.put(":2_17.gif", "17.png");
	}

    public void Emotion3() {
                map3.put(":dog_微笑" , "1.gif");
                map3.put(":dog_喜欢" , "2.gif");
                map3.put(":dog_发呆" , "3.gif");
                map3.put(":dog_大哭" , "4.gif");
                map3.put(":dog_亲亲" , "5.gif");
                map3.put(":dog_发火" , "6.gif");
                map3.put(":dog_大笑" , "7.gif");
                map3.put(":dog_惊讶" , "8.gif");
                map3.put(":dog_伤心" , "9.gif");
                map3.put(":dog_冷汗" , "10.gif");
                map3.put(":dog_可爱" , "11.gif");
                map3.put(":dog_加油" , "12.gif");
                map3.put(":dog_疑问" , "13.gif");
                map3.put(":dog_嘘" , "14.gif");
                map3.put(":dog_无语" , "15.gif");
    }


        public static void addEmotions(TextView view) {
		CharSequence content = view.getText();
		view.setText(convertNormalStringToSpannableString(content.toString()));
	}

	public static void addJustHighLightLinks(CommentBean bean) {
		bean.setListViewSpannableString(convertNormalStringToSpannableString(bean
				.getContent()));
		if (!TextUtils.isEmpty(bean.getRefercontent())) {
			bean.setListViewSpannableStringReferTo(convertNormalStringToSpannableString(bean
					.getRefercontent()));
		}
	}

	public static void addJustHighLightLinks(MsgCenterBean bean) {
        String str =null;
        if(!TextUtils.isEmpty(bean.getRelatenickname())){
            str = "@"+bean.getRelatenickname();
        }else{
            str = "@匿名用户";
        }

		bean.setListViewSpannableString(convertNormalStringToSpannableString(str+" "+bean
				.getCon()));
	}

	public static SpannableString convertNormalStringToSpannableString(
			String txt) {
		// hack to fix android imagespan bug,see
		// http://stackoverflow.com/questions/3253148/imagespan-is-cut-off-incorrectly-aligned
		// if string only contains emotion tags,add a empty char to the end
		String hackTxt;
		if (txt.startsWith("/[") && txt.endsWith("/]")) {
			hackTxt = txt + " ";
		} else {
			hackTxt = txt;
		}
		hackTxt = hackTxt.replace(HttpUrl.EMO_URL, "");
		SpannableString value = SpannableString.valueOf(hackTxt);
		URLSpan[] urlSpans = value.getSpans(0, value.length(), URLSpan.class);
		URLSpan weiboSpan = null;
		for (URLSpan urlSpan : urlSpans) {
			weiboSpan = new URLSpan(urlSpan.getURL());
			int start = value.getSpanStart(urlSpan);
			int end = value.getSpanEnd(urlSpan);
			value.removeSpan(urlSpan);
			value.setSpan(weiboSpan, start, end,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		addEmotions(value);
//		addEmotionsOld(value);
        matchPeople(value);
		hackTxt = stripHtml(hackTxt);
 		Linkify.addLinks(value, AppConfigs.WEB_URL, AppConfigs.WEB_SCHEME);
		// Linkify.addLinks(value, WeiboPatterns.TOPIC_URL,
		// WeiboPatterns.TOPIC_SCHEME);
		return value;
	}

	@SuppressLint("NewApi")
	private static void addEmotions(SpannableString value) {
		Matcher localMatcher = AppConfigs.EMOTION_URL.matcher(value);
		while (localMatcher.find()) {
			int k = localMatcher.start();
			int m = localMatcher.end();
			// if (m - k < 8) {
			Bitmap bitmap = XApplication.getInstance().getEmotionsPics()
					.get(localMatcher.group(1));

			if (bitmap != null) {
				ImageSpan localImageSpan = new ImageSpan(XApplication
						.getInstance().getActivity(), bitmap,
						ImageSpan.ALIGN_BASELINE);
				value.setSpan(localImageSpan, k, m,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				// }
			}
		}
	}


    @SuppressLint("NewApi")
    private static void matchPeople(SpannableString value) {
        Matcher localMatcher = AppConfigs.MENTION_URL.matcher(value);
        while (localMatcher.find()) {
            int k = localMatcher.start();
            int m = localMatcher.end();
            value.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), k, m,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            value.setSpan(AppConfigs.SPAN_USER_COLOR,k, m, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
    }

	@SuppressLint("NewApi")
	private static void addEmotionsOld(SpannableString value) {
		Matcher localMatcher = AppConfigs.IMG_SRC_OLD.matcher(value);
          while (localMatcher.find()) {
			int k = localMatcher.start();
			int m = localMatcher.end();
			// if (m - k < 8) {
			Bitmap bitmap = XApplication.getInstance().getEmotionsPics()
					.get(localMatcher.group(1));

			if (bitmap != null) {
				ImageSpan localImageSpan = new ImageSpan(XApplication
						.getInstance().getActivity(), bitmap,
						ImageSpan.ALIGN_BASELINE);
				value.setSpan(localImageSpan, k, m,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				// }
			}
		}
	}




	public static String filterHtml(String hackTxt) {
		Matcher HtmlMatcher = AppConfigs.Ex_html.matcher(hackTxt);
		return HtmlMatcher.replaceAll(""); // 过滤html标签
	}

	/**
	 * 去掉字符串里面的html代码。<br>
	 * 要求数据要规范，比如大于小于号要配套,否则会被集体误杀。
	 * 
	 * @param content
	 *            内容
	 * @return 去掉后的内容
	 */
	public static String stripHtml(String content) {
		// <p>段落替换为换行
		content = content.replaceAll("<p .*?>", "\r\n");
		// <br><br/>替换为换行
		content = content.replaceAll("<br\\s*/?>", "\r\n");
		// 去掉其它的<>之间的东西
		content = content.replaceAll("\\<.*?>", "");
		content = content.replaceAll("&nbsp;", "");
		content = content.replaceAll("\\s{2,}", " "); // 多空格
		// 还原HTML
		// content = HTMLDecoder.decode(content);
		return content;
	}

	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
	private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
	private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
	private static final String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符

	/**
	 * @param htmlStr
	 * @return 删除Html标签
	 */
	public static String delHTMLTag(String htmlStr) {
		Pattern p_script = Pattern.compile(regEx_script,
				Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern
				.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		Pattern p_space = Pattern
				.compile(regEx_space, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
		return htmlStr.trim(); // 返回文本字符串
	}

	public static String getTextFromHtml(String htmlStr) {
		htmlStr = delHTMLTag(htmlStr);
		htmlStr = htmlStr.replaceAll("&nbsp;", "");
		// htmlStr = htmlStr.substring(0, htmlStr.indexOf("。") + 1);
		return htmlStr;
	}

	public static String getTinyTextFromHtml(String htmlStr) {
        htmlStr = delHTMLTag(htmlStr);
        htmlStr = htmlStr.replaceAll("&nbsp;", "");
        String str = htmlStr.substring(0, htmlStr.indexOf("。") + 1);
        if(TextUtils.isEmpty(str)){
            return htmlStr;
        }
		return str;
	}

	public static String cutTextFromPosition(String text, int start_postion,
			int end_position) {
		if (text.length() < end_position) {
			return text;
		}
		String subtext = text.substring(start_postion, end_position);
		return subtext;
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static String insertStringXml(int res, String str) {
		return String.format(XApplication.getInstance().getResources()
				.getString(res), str);
	}

	public static String replaceBlank(String str) {

		String dest = "";

		if (str != null) {

			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);

			dest = m.replaceAll("");

		}

		return dest;

	}
}
