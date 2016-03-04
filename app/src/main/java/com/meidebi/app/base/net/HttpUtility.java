package com.meidebi.app.base.net;


import com.meidebi.app.base.error.XException;
import com.meidebi.app.support.file.FileDLHelper;
import com.meidebi.app.support.utils.Utility;

import java.util.Map;

public class HttpUtility {

    private static HttpUtility httpUtility = new HttpUtility();

    private HttpUtility() {
    }

    public static HttpUtility getInstance() {
        return httpUtility;
    }


    public String executeNormalTask(HttpMethod httpMethod, String url, Map<String, String> param) throws XException {
        if (param != null) {
            param.put("devicetype", "2");
            param.put("brand", Utility.getMobileBrand());
            param.put("model", Utility.getMobileMoldel());

        }
        return new HttpConnection().executeNormalTask(httpMethod, url, param);
    }

    public boolean executeDownloadTask(String url, String path, FileDLHelper.DownloadListener downloadListener) {
        return !Thread.currentThread().isInterrupted() && new HttpConnection().doGetSaveFile(url, path, downloadListener);
    }

    public boolean executeUploadTask(String url, Map<String, String> param, String path, String imageParamName) throws XException {
        return !Thread.currentThread().isInterrupted() && new HttpConnection().doUploadFile(url, param, path, imageParamName);
    }
}

