package com.meidebi.app.support.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
public class Net {
	private InputStream inputstream;
	private DefaultHttpClient httpClient;
	private boolean isStop = false;
	private NetworkInfo[] info;

	/**
<<<<<<< .mine
	 * ��Դ����
=======
	 * 资源下载
>>>>>>> .r2167
	 * 
	 * @param context
<<<<<<< .mine
	 *            �����Ķ���
=======
	 *            上下文对象
>>>>>>> .r2167
	 * @param url
<<<<<<< .mine
	 *            ����url��ַ
=======
	 *            下载url地址
>>>>>>> .r2167
	 * @return
	 * @throws org.apache.http.client.ClientProtocolException
	 * @throws java.io.IOException
	 */
	public byte[] downloadResource(Context context, String url)
			throws ClientProtocolException, IOException {
		isStop = false;
		ByteArrayBuffer buffer = null;
		HttpGet hp = new HttpGet(url);
		httpClient = new DefaultHttpClient();
		/*
		String netType = isNetType(context);
		if (netType != null & netType.equals("cmwap")) {
			HttpHost proxy = new HttpHost("10.0.0.172", 80);
			httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
					proxy);
		}*/
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),5 * 1000);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 60 * 1000);
		HttpResponse response = httpClient.execute(hp);
		if (response.getStatusLine().getStatusCode() == 200) {
			inputstream = response.getEntity().getContent();
			if (inputstream != null) {
				int i = (int) response.getEntity().getContentLength();
				buffer = new ByteArrayBuffer(1024 * 10);
				byte[] tmp = new byte[1024 * 10];
				int len;
				while (((len = inputstream.read(tmp)) != -1)
						&& (false == isStop)) {
					buffer.append(tmp, 0, len);
				}
			}
			cancel();
		}
		return buffer.toByteArray();
	}

	/**
<<<<<<< .mine
	 * ǿ�ƹر�����
=======
	 * 强制关闭请求
>>>>>>> .r2167
	 * 
	 * @throws java.io.IOException
	 */
	public synchronized void cancel() throws IOException {
		if (null != httpClient) {
			isStop = true;
			httpClient.getConnectionManager().shutdown();
			httpClient = null;
		}
		if (inputstream != null) {
			inputstream.close();
		}
	}

	/**
	 * 判断接入点类型
	 * 
	 * @return
	 */
	public static String isNetType(Context context) {
		String nettype = null;
		if (context == null) {
			return null;
		}
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectivityManager.getActiveNetworkInfo();
		if (mobNetInfo != null) {
			if (mobNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				nettype = mobNetInfo.getTypeName(); // 当前联网类型是WIFI
			} else {
				nettype = mobNetInfo.getExtraInfo();// 当前联网类型是cmnet/cmwap
			}
		}

		return nettype;
	}
	
	
	/**
	 * 判断接入点类型
	 * 
	 * @return
	 */
	public static boolean isWifi(Context context) {
//		boolean iswifi = false;
		if (context == null) {
			return false;
		}
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectivityManager.getActiveNetworkInfo();
		if (mobNetInfo != null) {
			if (mobNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;
//				nettype = mobNetInfo.getTypeName(); // 当前联网类型是WIFI
			} else {
				return false;
			}
		}
		return false;
	}

	public static boolean isNetworkAvailable(Context context) {
//		Context context = mActivity.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}


		/**
		* 检测网络是否存在
		*/
		public static boolean HttpTest(final Activity mActivity)
		{
		if( !isNetworkAvailable( mActivity) ){
			Toast toast = Toast.makeText(mActivity,"网络连接有错误",Toast.LENGTH_SHORT);  
			 
	          toast.show(); 
	          return false;
		}
		return true;
	}


}
