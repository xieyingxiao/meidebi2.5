package com.meidebi.app.service.dao;

import android.app.Activity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.base.net.HttpMethod;
import com.meidebi.app.base.net.HttpUtility;
import com.meidebi.app.service.bean.base.BaseItemBean;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.base.LinkListJson;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.GsonUtils;
import com.meidebi.app.support.utils.RestHttpUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaseDao {

    protected Activity mActivity;
    protected int page = 1;

    //ObjectMapper mObjectMapper = new ObjectMapper();
    protected Gson gson = new Gson();

    public BaseDao() {
    }

    public BaseDao(Activity activity) {
        mActivity = activity;
    }

    private static Toast mToast;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public <T> CommonJson<T> postMapperJson(String url, Map param) {
        // TODO Auto-generated method stub
        CommonJson<T> commonjson;
        try {
            String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Post, url, param);
            //String result = HttpUtils.postByHttpClient(mActivity, HttpUrl.LOGIN_URL,param);
            AppLogger.e("result" + result);
            commonjson = gson.fromJson(result, CommonJson.class);
            if (commonjson == null) {
                return null;
            }
            return commonjson;
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getStringFromUrl(String url) {
        try {
            String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Get, url, null);
            //String result = HttpUtils.postByHttpClient(mActivity, HttpUrl.LOGIN_URL,param);
            AppLogger.e("result" + result);
            if (result == null) {
                return null;
            }
            return result;
            // TODO Auto-generated catch block
        } catch (XException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public CommonJson getMapperJson(String url, Map param) {
        // TODO Auto-generated method stub
        CommonJson commonjson;
        try {
            String result = HttpUtility.getInstance().executeNormalTask(HttpMethod.Get, url, param);
            //String result = HttpUtils.postByHttpClient(mActivity, HttpUrl.LOGIN_URL,param);
            AppLogger.e("res" + result);
            commonjson = gson.fromJson(result, CommonJson.class);
            if (commonjson == null) {
                return null;
            }
            return commonjson;
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, String> getMapFromJson(String url, Map<String, String> param) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            String result = HttpUtility.getInstance().executeNormalTask(
                    HttpMethod.Get, url, param);
            JSONObject jsonObject = new JSONObject(result);
            String data = jsonObject.getString("data");
            LinkedHashMap<String, Object> retMap2 = gson.fromJson(data.toString(),
                    new TypeToken<LinkedHashMap<String, Object>>() {
                    }.getType());
            for (String key : retMap2.keySet()) {
                BaseItemBean bean = gson.fromJson(String.valueOf(retMap2.get(key)), BaseItemBean.class);
                map.put(key, bean.getName());
                String map1 = "\"" + key + "\"";
                String map2 = "\"" + bean.getName() + "\"";
                System.out.println("map.put(" + map1 + ", " + map2 + ");");
//				AppLogger.e(bean.getId()+bean.getName());
            }
            return null;
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ListJson toListJson(String json, Class Class) {
        return GsonUtils.fromListJson(json, Class);
    }

    public static void getListResult(String url, RequestParams params, final RestHttpUtils.RestHttpHandler<ListJson> handler, final Class type) {

        RestHttpUtils.get(url, params, new TextHttpResponseHandler() {
            public void onStart() {
                handler.onStart();
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                if (statusCode == 0) {
                    ShowToast("网络断开请连接后再试");
                } else {
                    ShowToast("服务器开了小差,状态码为:" + statusCode);
                }
                if (handler != null) {
                    handler.onFailed();
                }
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                AppLogger.e("responseString" + responseString);
                if (handler != null) {
                    ListJson json = GsonUtils.fromListJson(responseString, type);
                    if (json != null) {
                        handler.onSuccess(json);
                    } else {
                        handler.onFailed();
                    }
                }
            }
        });
    }

    public static void postListResult(String url, RequestParams params, final RestHttpUtils.RestHttpHandler<ListJson> handler, final Class type) {

        RestHttpUtils.post(url, params, new TextHttpResponseHandler() {
            public void onStart() {
                handler.onStart();
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                if (statusCode == 0) {
                    ShowToast("网络断开请连接后再试");
                } else {
                    ShowToast("服务器开了小差,状态码为:" + statusCode);
                }
                if (handler != null) {
                    handler.onFailed();
                }
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                AppLogger.e("responseString" + responseString);
                if (handler != null) {
                    ListJson json = GsonUtils.fromListJson(responseString, type);
                    if (json != null) {
                        handler.onSuccess(json);
                    } else {
                        handler.onFailed();
                    }
                }
            }
        });
    }

    public static void getCommonResult(String url, RequestParams params, final RestHttpUtils.RestHttpHandler<CommonJson> handler, final Class type) {
        RestHttpUtils.get(url, params, new TextHttpResponseHandler() {
            public void onStart() {
                if (handler != null) {
                    handler.onStart();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                if (statusCode == 0) {
                    ShowToast("网络断开请连接后再试");
                } else {
                    ShowToast("服务器开了小差,状态码为:" + statusCode);
                }
                if (handler != null) {
                    handler.onFailed();
                }
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                AppLogger.e("res" + responseString);
                if (handler != null) {
                    CommonJson json = GsonUtils.fromJson(responseString, type);
                    if (json != null) {
                        handler.onSuccess(json);
                    } else {
                        handler.onFailed();
                    }
                }
            }
        });
    }

    public static void postCommonResult(String url, RequestParams params, final RestHttpUtils.RestHttpHandler<CommonJson> handler, final Class type) {


        RestHttpUtils.post(url, params, new TextHttpResponseHandler() {
            public void onStart() {
                if (handler != null) {
                    handler.onStart();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                if (statusCode == 0) {
                    ShowToast("网络断开请连接后再试");
                } else {
                    ShowToast("服务器开了小差,状态码为:" + statusCode);
                }
                if (handler != null) {
                    handler.onFailed();
                }
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                AppLogger.e("res" + responseString);
                if (handler != null) {
                    CommonJson json = GsonUtils.fromJson(responseString, type);
                    if (json != null) {
                        handler.onSuccess(json);
                    } else {
                        handler.onFailed();
                    }
                }
            }
        });
    }

    public static void getLinkListResult(String url, RequestParams params, final RestHttpUtils.RestHttpHandler<LinkListJson> handler, final Class type) {
        RestHttpUtils.get(url, params, new TextHttpResponseHandler() {
            public void onStart() {
                handler.onStart();
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                if (statusCode == 0) {
                    ShowToast("网络断开请连接后再试");
                } else {
                    ShowToast("服务器开了小差,状态码为:" + statusCode);
                }
                if (handler != null) {
                    handler.onFailed();
                }
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                AppLogger.e("responseString" + responseString);
                LinkListJson json = GsonUtils.fromLinkListJson(responseString, type);
                if (json != null) {
                    handler.onSuccess(json);
                } else {
                    handler.onFailed();
                }
            }
        });
    }


    public static void postLinkListResult(String url, RequestParams params, final RestHttpUtils.RestHttpHandler<LinkListJson> handler, final Class type) {
        RestHttpUtils.post(url, params, new TextHttpResponseHandler() {
            public void onStart() {
                handler.onStart();
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                if (statusCode == 0) {
                    ShowToast("网络断开请连接后再试");
                } else {
                    ShowToast("服务器开了小差,状态码为:" + statusCode);
                }
                if (handler != null) {
                    handler.onFailed();
                }
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                AppLogger.e("responseString" + responseString);
                LinkListJson json = GsonUtils.fromLinkListJson(responseString, type);
                if (json != null) {
                    handler.onSuccess(json);
                } else {
                    handler.onFailed();
                }
            }
        });
    }

    private static void ShowToast(String string) {
        if (mToast == null) {
            mToast = Toast.makeText(XApplication.getInstance(), string, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(string);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }


}
