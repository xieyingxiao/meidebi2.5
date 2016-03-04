package com.meidebi.app.support.utils;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.meidebi.app.XApplication;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.base.ErrorJson;
import com.meidebi.app.service.bean.base.LinkListJson;
import com.meidebi.app.service.bean.base.ListJson;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by mdb-ii on 15-1-30.
 */
public class GsonUtils {

    private static Gson _gson = new Gson();

    public static <T> T parse(String json, Class<T> T) {
        return _gson.fromJson(json, T);
    }

    public static <T> T parseList(String json, Class clazz) {
        Type objectType = type(List.class, clazz);
        return _gson.fromJson(json, objectType);
    }

    public static CommonJson fromJson(String json, Class clazz) {
        try {
            Type objectType = type(CommonJson.class, clazz);
            return _gson.fromJson(json, objectType);
        } catch (Exception e) {
            try {
                Type objectType = type(CommonJson.class, Object.class);
                return _gson.fromJson(json, objectType);
            } catch (Exception e2) {
                {
                    try {
                        Type objectType = type(CommonJson.class, String.class);
                        return _gson.fromJson(json, objectType);
                    } catch (Exception ef) {
                        Type objectType = type(CommonJson.class, Integer.class);
                        return _gson.fromJson(json, objectType);
                    }
                }
            }

        }

    }


    public static ListJson fromListJson(String json, Class clazz) {
        try {
            Type objectType = type(ListJson.class, clazz);
            return _gson.fromJson(json, objectType);
        } catch (Exception e) {
            AppLogger.e(e.getMessage(), e);
            return null;
        }
    }

    public static LinkListJson fromLinkListJson(String json, Class clazz) {
        try {
            Type objectType = type(LinkListJson.class, clazz);
            return _gson.fromJson(json, objectType);
        } catch (Exception e) {
            Type objectType = type(ErrorJson.class, clazz);
            Log.e("====",json);
            ErrorJson update_inf = _gson.fromJson(json, ErrorJson.class);
            ErrorJson update_info = _gson.fromJson(json, objectType);
            new MaterialDialog.Builder(XApplication.getInstance().getActivity()).backgroundColor(Color.WHITE)
                    .title("请更新版本")
                    .content(update_info.getContent())
                    .positiveText("退出应用")
                    .negativeText("更新")
                    .cancelable(false)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            final UmengUpdateListener listener = new UmengUpdateListener() {

                                @Override
                                public void onUpdateReturned(int updateStatus,
                                                             UpdateResponse updateInfo) {
                                    switch (updateStatus) {
                                        case UpdateStatus.Yes:
                                            File file = UmengUpdateAgent.downloadedFile(XApplication.getInstance(), updateInfo);
                                            if (file != null) {
                                                UmengUpdateAgent.startInstall(XApplication.getInstance(), file);
                                            } else {
                                                UmengUpdateAgent.startDownload(XApplication.getInstance(), updateInfo);
                                                UmengUpdateAgent.showUpdateNotification(XApplication.getInstance(), updateInfo);
                                            }
                                            break;
                                        case UpdateStatus.No:

                                            Toast.makeText(XApplication.getInstance(), "没有更新", Toast.LENGTH_SHORT)
                                                    .show();
                                            break;
                                        case UpdateStatus.NoneWifi:
                                            Toast.makeText(XApplication.getInstance(), "没有wifi", Toast.LENGTH_SHORT)
                                                    .show();
                                            break;
                                        case UpdateStatus.Timeout:
                                            Toast.makeText(XApplication.getInstance(), "超时", Toast.LENGTH_SHORT)
                                                    .show();
                                            break;
                                    }
                                }

                            };

                            UmengUpdateAgent.setUpdateAutoPopup(false);
                            UmengUpdateAgent.setUpdateListener(listener);
                            UmengUpdateAgent.update(XApplication.getInstance());
                        }

                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            XApplication.getInstance().getActivity().finish();
                        }
                    }).build().show();
            return null;
        }

    }


    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

}
