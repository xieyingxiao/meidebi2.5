package com.meidebi.app.support.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.support.lib.MyAsyncTask;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Utility {
	private static final int MAX_NONCE = 0 + 10;

	private static final String LABEL_App_sign = "api_sign";
	private static final String LABEL_TIME = "timestamp";
	private static final String LABEL_NONCE = "nonce";
	private static final String LABEL_UID = "uid";

	private static final SecureRandom sRandom = new SecureRandom();

	private static char sHexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static String getNonce() {
		byte[] bytes = new byte[MAX_NONCE / 2];
		sRandom.nextBytes(bytes);
		return hexString(bytes);
	}

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String hexString(byte[] source) {
		if (source == null || source.length <= 0) {
			return "";
		}

		final int size = source.length;
		final char str[] = new char[size * 2];
		int index = 0;
		byte b;
		for (int i = 0; i < size; i++) {
			b = source[i];
			str[index++] = sHexDigits[b >>> 4 & 0xf];
			str[index++] = sHexDigits[b & 0xf];
		}
		return new String(str);
	}

	private static long getTimestamp() {
		Date date = new Date();
		long i = date.getTime();
		return i;
	}

	private static String getAPIsig(String key, long timestamp, String nonce,
			String uid) {
		// api_sig =
		// MD5("api_key"+@api_key+"nonce"+@nonce+"timestamp"+@timestamp)
		String result = null;
		StringBuilder builder = new StringBuilder();
		synchronized (builder) {
			builder.append(key);
			builder.append(timestamp);
			builder.append(nonce);
			builder.append(uid);
			result = MD5.encode(builder.toString());
			builder.delete(0, builder.length());
		}
		return result;
	}

	/**
	 * &…………………………
	 *
	 * @param key
	 * @return
	 */
	public static String getParams(String key) {
		String result = "";
		try {
			String[] temp = key.split(":");
			long timestamp = getTimestamp();
			String nonce = getNonce();
			String api_sign = getAPIsig(key, timestamp, nonce, temp[1]);

			StringBuilder builder = new StringBuilder();

			synchronized (result) {
				builder.append(String.format("&" + LABEL_UID + "=%s", temp[1]));
				builder.append(String.format("&" + LABEL_NONCE + "=%s", nonce));
				builder.append(String.format("&" + LABEL_TIME + "=%s",
						timestamp));
				builder.append(String.format("&" + LABEL_App_sign + "=%s",
						api_sign));
				result = builder.toString();
				builder.delete(0, builder.length());
			}
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	public static String getScreenParams(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return "&screen="
				+ (dm.heightPixels > dm.widthPixels ? dm.widthPixels + "*"
						+ dm.heightPixels : dm.heightPixels + "*"
						+ dm.widthPixels);
	}

	public static String getEncodeUrl(Map<String, String> param) {
		if (param == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("-");
		Set<String> keys = param.keySet();
		boolean first = true;

		for (String key : keys) {
			String value = param.get(key);
			// pain...EditMyProfileDao params' values can be empty
			if (!TextUtils.isEmpty(value) || key.equals("description")
					|| key.equals("url")) {
				if (first)
					first = false;
				else
					sb.append("-");
				try {
					sb.append(URLEncoder.encode(key, "UTF-8")).append("-")
							.append(URLEncoder.encode(param.get(key), "UTF-8"));
				} catch (UnsupportedEncodingException e) {

				}
			}

		}

		return sb.toString();
	}

	public static String encodeUrl(Map<String, String> param) {
		if (param == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		Set<String> keys = param.keySet();
		boolean first = true;

		for (String key : keys) {
			String value = param.get(key);
			// pain...EditMyProfileDao params' values can be empty
			if (value != null || key.equals("description") || key.equals("url")) {
				if (first)
					first = false;
				else
					sb.append("&");
				try {
					sb.append(URLEncoder.encode(key, "UTF-8")).append("=")
							.append(URLEncoder.encode(param.get(key), "UTF-8"));
				} catch (UnsupportedEncodingException e) {

				}
			}

		}
		return sb.toString();
	}

	public static String buildSetToString(HashSet<String> set) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		Iterator ir = set.iterator();
		while (ir.hasNext()) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			sb.append(ir.next());
		}
		return sb.toString();
	}

	public static void closeSilently(Closeable closeable) {
		if (closeable != null)
			try {
				closeable.close();
			} catch (IOException ignored) {

			}
	}

	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		return networkInfo != null && networkInfo.isConnected();
	}

	public static String getPicPathFromUri(Uri uri, Activity activity) {
		String value = uri.getPath();

		if (value.startsWith("/external")) {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else {
			return value;
		}
	}

	public static String getImeiCode() {
		String imei = ((TelephonyManager) XApplication.getInstance()
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		if (TextUtils.isEmpty(imei) || imei.equals("000000000000000")) {
			return SharePrefUtility.getUUID();
		} else {
			return imei;
		}
	}



	public static String getUUID() {
		String uuid = file2String(new File(AppConfigs.TOKENDIR), "UTF-8");
		AppLogger.e("uuid" + uuid);
		if (TextUtils.isEmpty(uuid)) {
			uuid = ((TelephonyManager) XApplication.getInstance()
					.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
			if (TextUtils.isEmpty(uuid) || uuid.equals("000000000000000")) {
				uuid = Utility.getTimesamp();
			}
			string2File(uuid, AppConfigs.TOKENDIR);
			return uuid;
		} else {
			return uuid;
		}
	}

	/**
	 * 将字符串写入指定文件(当指定的父路径中文件夹不存在时，会最大限度去创建，以保证保存成功！)
	 *
	 * @param res
	 *            原字符串
	 * @param filePath
	 *            文件路径
	 * @return 成功标记
	 */
	public static boolean string2File(String res, String filePath) {
		boolean flag = true;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			File distFile = new File(filePath);
			if (!distFile.getParentFile().exists())
				distFile.getParentFile().mkdirs();
			bufferedReader = new BufferedReader(new StringReader(res));
			bufferedWriter = new BufferedWriter(new FileWriter(distFile));
			char buf[] = new char[1024]; // 字符缓冲区
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * 文本文件转换为指定编码的字符串
	 *
	 * @param file
	 *            文本文件
	 * @param encoding
	 *            编码类型
	 * @return 转换后的字符串
	 * @throws java.io.IOException
	 */
	public static String file2String(File file, String encoding) {
		InputStreamReader reader = null;
		StringWriter writer = new StringWriter();
		try {
			if (encoding == null || "".equals(encoding.trim())) {
				reader = new InputStreamReader(new FileInputStream(file),
						encoding);
			} else {
				reader = new InputStreamReader(new FileInputStream(file));
			}
			// 将输入流写入输出流
			char[] buffer = new char[1024];
			int n = 0;
			while (-1 != (n = reader.read(buffer))) {
				writer.write(buffer, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		// 返回转换结果
		if (writer != null)
			return writer.toString();
		else
			return null;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	// public static int getScreenHeight(Context context) {
	// int screenHeight = 0;
	// DisplayMetrics metrics = new DisplayMetrics();
	// Display display = ((Activity) context).getWindowManager()
	// .getDefaultDisplay();
	// display.getMetrics(metrics);
	// int ver = Build.VERSION.SDK_INT;
	// if (ver < 13) {
	// screenHeight = metrics.heightPixels;
	// } else if (ver == 13) {
	// try {
	// Method method = display.getClass().getMethod("getRealHeight");
	// screenHeight = (Integer) method.invoke(display);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// } else if (ver > 13 && ver < 17) {
	// try {
	// Method method = display.getClass().getMethod("getRawHeight");
	// screenHeight = (Integer) method.invoke(display);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// } else if (ver >= 17) {
	// Point outSize = new Point();
	// display.getRealSize(outSize);
	// screenHeight = outSize.y;
	// }
	// return screenHeight;
	//
	// // return context.getResources().getDisplayMetrics().heightPixels;
	// }

	public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;

	}

	private static int getNavHeight(Context context) {
		Resources resources = context.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			return resources.getDimensionPixelSize(resourceId);
		}
		return 0;
	}

	public static String getTimesamp() {
		long start = System.nanoTime();
		// long end = System.nanoTime();
		String uuid = String.valueOf(start);
		SharePrefUtility.setUUID(uuid);
		return uuid;
	}

	public static void cancelTasks(MyAsyncTask... tasks) {
		for (MyAsyncTask task : tasks) {
			if (task != null)
				task.cancel(true);
		}
	}

	public static String getMobileBrand() {
		return Build.BRAND;
	}

	public static String getMobileMoldel() {
		return Build.MODEL;
	}

    public static String encrypt(String seed, String cleartext) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] result = encrypt(rawKey, cleartext.getBytes());
        return toHex(result);
    }

    public static String decrypt(String seed, String encrypted) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available      
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }


    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }
    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2*buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }
    private final static String HEX = "0123456789ABCDEF";
    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
    }

    public static boolean isLink(String url){
    	if(url.contains("http://")||url.contains("https://")){
    		return true;
    	}
    	return false;
    }

	public static boolean isKitkat() {
		// TODO Auto-generated method stub
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
			return true;
		}
		return false;
	}


    public static void showFab(View mFab) {
             ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();

    }

    public static void hideFab(View mFab) {
             ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
    }


   public static Map<String, Integer> mColors = new HashMap<String, Integer>();








    public static String getBestMatchingColorName(int pixelColor) {
        if(mColors.isEmpty()){
            mColors.put("red", Color.rgb(255, 0, 0));
            mColors.put("pink", Color.rgb(255, 192, 203));
            mColors.put("voilet", Color.rgb(36, 10, 64));
            mColors.put("blue", Color.rgb(0, 0, 255));
            mColors.put("green", Color.rgb(0, 255, 0));
            mColors.put("yellow", Color.rgb(255, 255, 0));
            mColors.put("orange", Color.rgb(255, 104, 31));
            mColors.put("white", Color.rgb(255, 255, 255));
            mColors.put("black", Color.rgb(0, 0, 0));
            mColors.put("gray", Color.rgb(128, 128, 128));
            mColors.put("tea", Color.rgb(193, 186, 176));
            mColors.put("cream", Color.rgb(255, 253, 208));
        }
        // largest difference is 255 for every colour component
        int currentDifference = 3 * 255;
        // name of the best matching colour
        String closestColorName = null;
        // get int values for all three colour components of the pixel
        int pixelColorR = Color.red(pixelColor);
        int pixelColorG = Color.green(pixelColor);
        int pixelColorB = Color.blue(pixelColor);

        Iterator<String> colorNameIterator = mColors.keySet().iterator();
        // continue iterating if the map contains a next colour and the difference is greater than zero.
        // a difference of zero means we've found an exact match, so there's no point in iterating further.
        while (colorNameIterator.hasNext() && currentDifference > 0) {
            // this colour's name
            String currentColorName = colorNameIterator.next();
            // this colour's int value
            int color = mColors.get(currentColorName);
            // get int values for all three colour components of this colour
            int colorR = Color.red(color);
            int colorG = Color.green(color);
            int colorB = Color.blue(color);
            // calculate sum of absolute differences that indicates how good this match is
            int difference = Math.abs(pixelColorR - colorR) + Math.abs(pixelColorG - colorG) + Math.abs(pixelColorB - colorB);
            // a smaller difference means a better match, so keep track of it
            if (currentDifference > difference) {
                currentDifference = difference;
                closestColorName = currentColorName;
            }
        }
        return closestColorName;
    }

    public static int getVersionCode(Context context)//获取版本号(内部识别号)
    {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

}
