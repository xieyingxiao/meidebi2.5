package com.meidebi.app.support.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by mdb-ii on 15-1-30.
 */
public class RestHttpUtils {
    public static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.addHeader("accept", "application/json");
    }

    public static void  get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if(params==null){
            params = new RequestParams();
        }
         params.put("devicetoken", Utility.getImeiCode());
        params.put("devicetype", "2");
        params.put("brand", Utility.getMobileBrand());
        params.put("model", Utility.getMobileMoldel());
        client.get(url, params, responseHandler);
    }

    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
        client.get(url, null, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if(params==null){
            params = new RequestParams();
        }
        params.put("devicetoken", Utility.getImeiCode());
        params.put("devicetype", "1");
        params.put("brand", Utility.getMobileBrand());
        params.put("model", Utility.getMobileMoldel());
        client.post(url, params, responseHandler);
    }

    public static void uploadImage(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if(params==null){
            params = new RequestParams();
        }

//        params.put("devicetoken", Utility.getImeiCode());
        params.put("devicetype", "2");
//        params.put("brand", Utility.getMobileBrand());
//        params.put("model", Utility.getMobileMoldel());
//         client.addHeader("Content-Type", "image/jpg");
//        client.addHeader("Content-Disposition", "inline; filename=avantar.jpg" );

        client.post(url, params, responseHandler);
    }

    public interface RestHttpHandler<T> {
        public  void onSuccess(T result);
        public  void onStart();
        public  void onFailed();


    }
}