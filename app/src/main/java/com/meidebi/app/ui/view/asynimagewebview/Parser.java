package com.meidebi.app.ui.view.asynimagewebview;

import android.app.Activity;
import android.text.TextUtils;
import android.webkit.WebView;

import com.meidebi.app.support.file.FileManager;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * 
 * 
 */

public abstract class Parser {
	public static String filePath = FileManager.getSdCardPath() + "/";
	private WebView webView;
	public static String Js2JavaInterfaceName = "MedebiApp";
	private ArrayList<String> imgUrls = new ArrayList<String>();
	private Activity activity;
	public Md5FileNameGenerator md5FileNameGenerator = new Md5FileNameGenerator();
	private static String DEFAULT_IMAGE_URI = "file:///android_asset/js/default_pic_content_image.png";

	private static String DEFAULT_LOADING_IMAGE_URI = "file:///android_asset/js/default_pic_content_image_loading.gif";

	public static String DEFAULT_LOADING_FAILED_IMAGE_URI = "file:///android_asset/js/default_pic_content_image_failed.png";
	private static String LOCAL_CSS = "file:///android_asset/js/style.css";

	public Parser(String mUrl, WebView webView, Activity activity) {
		this.webView = webView;
		this.activity = activity;
	}

	public ArrayList<String> getImgUrls() {
		return imgUrls;
	}

	public String  loadData() {
		String html = downloadHtml();
		if (FileManager.isExternalStorageMounted()) {
			Document doc = Jsoup.parse(html);
			imgUrls.clear();
            int fix_position = 0 ;
			Elements es = doc.getElementsByTag("img");
 			for (int i = 0;i<es.size();i++) {
                Element e = es.get(i);
 				String imgUrl = e.attr("src");
				if (TextUtils.isEmpty(imgUrl)) {
					String temp = e.attr("data-lazyload");
					if (!TextUtils.isEmpty(temp)) {
						imgUrl = temp;
						e.removeAttr("data-lazyload");
					} else {
						e.remove();
 					}
 				}
                String str = null;
				if (!imgUrl.contains("file://")) {


					if (SharePrefUtility.getEnablePic()) {
						e.attr("src", DEFAULT_LOADING_IMAGE_URI);
						if (!TextUtils.isEmpty(imgUrl)&&!imgUrl.endsWith(".gif")) {
                            imgUrls.add(imgUrl);
                            str = "window." + "onImageClick(this,"+(fix_position)+")";
                            fix_position = fix_position+1;
							e.attr("onclick", str);
						}
					} else {
						if (imgUrl.endsWith(".gif")) {
							e.remove();
						} else {
							e.attr("src", DEFAULT_IMAGE_URI);
                            imgUrls.add(imgUrl);
                            str = "window." + "onImageClick(this,"+(fix_position)+")";
                            fix_position = fix_position+1;
 							e.attr("onclick", str);
						}
					}
					e.attr("original-src", imgUrl);
				} else {
					e.remove();
				}
			}
			return doc.html();
		} else {
            return html;
 		}
	}

	public abstract String downloadHtml();

	public String getfilePath(String imgurl) {
		if (imgurl != null) {
			File file = new File(imgurl);
			String imgName = file.getName();
			return "file://"
					+ StorageUtils.getCacheDirectory(activity)
							.getAbsolutePath()
							.replace("/mnt/sdcard/", "/sdcard/") + "/"
					+ imgName;
		}
		return null;
	}

	public static void openWebView(String data, final WebView webView) {
		data = data.replaceAll("<html>", "");
		data = data.replaceAll("</html>", "");
		data = data.replaceAll("<body>", "");
		data = data.replaceAll("</body>", "");
        data = data.replaceAll("<p></p>", "");
        webView.getSettings().setBlockNetworkImage(false);
        final String finalData = data;
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
                webView.loadDataWithBaseURL("file:///android_asset/",
                        getHtmlData(finalData), "text/html", "utf-8", null);
//            }
//        }, 300);
	}

	public static String getHtmlData(String bodyHTML) {
		Pattern p_script = Pattern.compile("<STYLE[^>]*?>[\\s\\S]*?<\\/style>", Pattern.CASE_INSENSITIVE);
		bodyHTML = p_script.matcher(bodyHTML).replaceAll("");
		String lazyjs =
		// "<script src=\"file:///android_asset/js/jquery.js\" type=\"text/javascript\"></script>"
		// +"<script src=\"file:///android_asset/js/jquery.lazyload.js\" type=\"text/javascript\"></script>"
		"<script src=\"file:///android_asset/js/javascript.js\" type=\"text/javascript\"></script>";
		String css = "<link rel=\"stylesheet\" href='" + LOCAL_CSS + "' />";
		String head = "<head>"
				+ lazyjs
				+ css
				+ "<style>p{margin-top: 10px;auto;margin-bottom: 10px;}a {color:#005aa0;text-decoration:none}</style></head>";
		return "<html>" + head + "<body id=\"content\">" + bodyHTML + "</body>"
				+ "</html>";
	}
}
