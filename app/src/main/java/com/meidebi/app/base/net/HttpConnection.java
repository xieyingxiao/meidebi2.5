package com.meidebi.app.base.net;

import android.text.TextUtils;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.support.file.FileDLHelper;
import com.meidebi.app.support.file.FileManager;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class HttpConnection {
    private static final int CONNECT_TIMEOUT = 10 * 1000;
    private static final int READ_TIMEOUT = 10 * 1000;

    private static final int DOWNLOAD_CONNECT_TIMEOUT = 15 * 1000;
    private static final int DOWNLOAD_READ_TIMEOUT = 60 * 1000;

    private static final int UPLOAD_CONNECT_TIMEOUT = 15 * 1000;
    private static final int UPLOAD_READ_TIMEOUT = 5 * 60 * 1000;

    public String executeNormalTask(HttpMethod httpMethod, String url, Map<String, String> param) throws XException {
        switch (httpMethod) {
            case Post:
                return doPost(url, param);
            case Get:
                return doGet(url, param);
        }
        return "";
    }

    private static Proxy getProxy() {
        String proxyHost = System.getProperty("http.proxyHost");
        String proxyPort = System.getProperty("http.proxyPort");
        if (!TextUtils.isEmpty(proxyHost) && !TextUtils.isEmpty(proxyPort))
            return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer.valueOf(proxyPort)));
        else
            return null;
    }

    public String doPost(String urlAddress, Map<String, String> param) throws XException {
        XApplication xApplication = XApplication.getInstance();
        String errorStr = xApplication.getString(R.string.timeout);
        xApplication = null;
        try {
            URL url = new URL(urlAddress);
            Proxy proxy = getProxy();
            HttpURLConnection uRLConnection;
            if (proxy != null)
                uRLConnection = (HttpURLConnection) url.openConnection(proxy);
            else
                uRLConnection = (HttpURLConnection) url.openConnection();

            uRLConnection.setDoInput(true);
            uRLConnection.setDoOutput(true);
            uRLConnection.setRequestMethod("POST");
            uRLConnection.setUseCaches(false);
            uRLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            uRLConnection.setReadTimeout(READ_TIMEOUT);
            uRLConnection.setInstanceFollowRedirects(false);
            uRLConnection.setRequestProperty("Connection", "Keep-Alive");
            uRLConnection.setRequestProperty("Charset", "UTF-8");
            uRLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            uRLConnection.connect();

            DataOutputStream out = new DataOutputStream(uRLConnection.getOutputStream());
            AppLogger.e("param" + Utility.encodeUrl(param));
            out.write(Utility.encodeUrl(param).getBytes());
            out.flush();
            out.close();
            return handleResponse(uRLConnection);
        } catch (IOException e) {
            e.printStackTrace();
            throw new XException(errorStr, e);
        }
    }


    private String handleResponse(HttpURLConnection httpURLConnection) throws XException {
        XApplication xApplication = XApplication.getInstance();
        String errorStr = xApplication.getString(R.string.timeout);
        xApplication = null;
        int status = 0;
        try {
            status = httpURLConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            httpURLConnection.disconnect();
            throw new XException(errorStr, e);
        }

        if (status != HttpURLConnection.HTTP_OK) {
            return handleError(httpURLConnection);
        }

        return readResult(httpURLConnection);
    }

    private String handleError(HttpURLConnection urlConnection) throws XException {

        String result = readError(urlConnection);
        String err = null;
        int errCode = 0;
        try {
            AppLogger.e("error=" + result);
            JSONObject json = new JSONObject(result);
            err = json.optString("error_description", "");
            if (TextUtils.isEmpty(err))
                err = json.getString("error");
            errCode = json.getInt("error_code");
            XException exception = new XException();
            exception.setError_code(errCode);
            exception.setOriError(err);
            throw exception;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return result;
    }

    public static String readResult(HttpURLConnection urlConnection) throws XException {
        InputStream is = null;
        BufferedReader buffer = null;
        XApplication xApplication = XApplication.getInstance();
        String errorStr = xApplication.getString(R.string.timeout);
        xApplication = null;
        try {
            is = urlConnection.getInputStream();

            String content_encode = urlConnection.getContentEncoding();

            if (null != content_encode && !"".equals(content_encode) && content_encode.equals("gzip")) {
                is = new GZIPInputStream(is);
            }

            buffer = new BufferedReader(new InputStreamReader(is));
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                strBuilder.append(line);
            }
            AppLogger.e("result=" + strBuilder.toString());
            return strBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new XException(errorStr, e);
        } finally {
            Utility.closeSilently(is);
            Utility.closeSilently(buffer);
            urlConnection.disconnect();
        }

    }

    private String readError(HttpURLConnection urlConnection) throws XException {
        InputStream is = null;
        BufferedReader buffer = null;
        XApplication xApplication = XApplication.getInstance();
        String errorStr = xApplication.getString(R.string.timeout);

        try {
            is = urlConnection.getErrorStream();

            if (is == null) {
                errorStr = xApplication.getString(R.string.unknown_network_error);
                throw new XException(errorStr);
            }

            String content_encode = urlConnection.getContentEncoding();

            if (null != content_encode && !"".equals(content_encode) && content_encode.equals("gzip")) {
                is = new GZIPInputStream(is);
            }

            buffer = new BufferedReader(new InputStreamReader(is));
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                strBuilder.append(line);
            }
            AppLogger.d("error result=" + strBuilder.toString());
            return strBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new XException(errorStr, e);
        } finally {
            Utility.closeSilently(is);
            Utility.closeSilently(buffer);
            urlConnection.disconnect();
            xApplication = null;
        }

    }

    public String doGet(String urlStr, Map<String, String> param) throws XException {
        XApplication xApplication = XApplication.getInstance();
        String errorStr = xApplication.getString(R.string.timeout);
        xApplication = null;
        InputStream is = null;
        try {

            StringBuilder urlBuilder = new StringBuilder(urlStr);
            urlBuilder.append("?").append(Utility.encodeUrl(param));
            AppLogger.e("param" + Utility.encodeUrl(param));

            URL url = new URL(urlBuilder.toString());
            AppLogger.d("get request" + url);
            Proxy proxy = getProxy();
            HttpURLConnection urlConnection;
            if (proxy != null)
                urlConnection = (HttpURLConnection) url.openConnection(proxy);
            else
                urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(false);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");

            urlConnection.connect();

            return handleResponse(urlConnection);
        } catch (IOException e) {
            e.printStackTrace();
            throw new XException(errorStr, e);
        }


    }

    public boolean doGetSaveFile(String urlStr, String path, FileDLHelper.DownloadListener downloadListener) {

        File file = FileManager.createNewFileInSDCard(path);
        if (file == null) {
            return false;
        }

        FileOutputStream out = null;
        InputStream in = null;
        HttpURLConnection urlConnection = null;
        try {

            URL url = new URL(urlStr);
            AppLogger.d("download request=" + urlStr);
            Proxy proxy = getProxy();
            if (proxy != null)
                urlConnection = (HttpURLConnection) url.openConnection(proxy);
            else
                urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(false);
            urlConnection.setConnectTimeout(DOWNLOAD_CONNECT_TIMEOUT);
            urlConnection.setReadTimeout(DOWNLOAD_READ_TIMEOUT);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");

            urlConnection.connect();

            int status = urlConnection.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK) {
                return false;
            }


            int bytetotal = (int) urlConnection.getContentLength();
            int bytesum = 0;
            int byteread = 0;
            out = new FileOutputStream(file);
            in = urlConnection.getInputStream();

            final Thread thread = Thread.currentThread();
            byte[] buffer = new byte[1444];
            while ((byteread = in.read(buffer)) != -1) {
                if (thread.isInterrupted()) {
                    file.delete();
                    throw new InterruptedIOException();
                }

                bytesum += byteread;
                out.write(buffer, 0, byteread);
                if (downloadListener != null && bytetotal > 0) {
                    downloadListener.pushProgress(bytesum, bytetotal);
                }
            }
            if (downloadListener != null) {
                downloadListener.completed();
            }
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utility.closeSilently(in);
            Utility.closeSilently(out);
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return false;
    }

    private static String getBoundry() {
        StringBuffer _sb = new StringBuffer();
        for (int t = 1; t < 12; t++) {
            long time = System.currentTimeMillis() + t;
            if (time % 3 == 0) {
                _sb.append((char) time % 9);
            } else if (time % 3 == 1) {
                _sb.append((char) (65 + time % 26));
            } else {
                _sb.append((char) (97 + time % 26));
            }
        }
        return _sb.toString();
    }

    private String getBoundaryMessage(String boundary, Map params, String fileField, String fileName, String fileType) {
        StringBuffer res = new StringBuffer("--").append(boundary).append("\r\n");

        Iterator keys = params.keySet().iterator();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = (String) params.get(key);
            res.append("Content-Disposition: form-data; name=\"")
                    .append(key).append("\"\r\n").append("\r\n")
                    .append(value).append("\r\n").append("--")
                    .append(boundary).append("\r\n");
        }
        res.append("Content-Disposition: form-data; name=\"").append(fileField)
                .append("\"; filename=\"").append(fileName)
                .append("\"\r\n").append("Content-Type: ")
                .append(fileType).append("\r\n\r\n");

        return res.toString();
    }

    public boolean doUploadFile(String urlStr, Map<String, String> param, String path, String imageParamName) throws XException {
        String BOUNDARYSTR = getBoundry();

        File targetFile = new File(path);

        byte[] barry = null;
        int contentLength = 0;
        String sendStr = "";
        try {
            barry = ("--" + BOUNDARYSTR + "--\r\n").getBytes("UTF-8");

            sendStr = getBoundaryMessage(BOUNDARYSTR, param, imageParamName, new File(path).getName() + ".jpg", "image/jpg");
            contentLength = sendStr.getBytes("UTF-8").length + (int) targetFile.length() + 2 * barry.length;
        } catch (UnsupportedEncodingException e) {

        }
        int totalSent = 0;
        String lenstr = Integer.toString(contentLength);

        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        FileInputStream fis = null;
//	        XApplication XApplication = XApplication.getInstance();
//	        String errorStr = XApplication.getString(R.string.timeout);
//	        XApplication = null;
        try {
            URL url = null;

            url = new URL(urlStr);

            Proxy proxy = getProxy();
//	            if (proxy != null)
//	                urlConnection = (HttpURLConnection) url.openConnection(proxy);
//	            else
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setConnectTimeout(UPLOAD_CONNECT_TIMEOUT);
            urlConnection.setReadTimeout(UPLOAD_READ_TIMEOUT);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setRequestProperty("Content-type", "multipart/form-data;boundary=" + BOUNDARYSTR);
            urlConnection.setRequestProperty("Content-Length", lenstr);
            ((HttpURLConnection) urlConnection).setFixedLengthStreamingMode(contentLength);
            urlConnection.connect();

            out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(sendStr.getBytes("UTF-8"));
            totalSent += sendStr.getBytes("UTF-8").length;


            fis = new FileInputStream(targetFile);

            int bytesRead;
            int bytesAvailable;
            int bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024;

            bytesAvailable = fis.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fis.read(buffer, 0, bufferSize);
            long transferred = 0;
            final Thread thread = Thread.currentThread();
            while (bytesRead > 0) {

                if (thread.isInterrupted()) {
                    targetFile.delete();
                    throw new InterruptedIOException();
                }
                out.write(buffer, 0, bufferSize);
                bytesAvailable = fis.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fis.read(buffer, 0, bufferSize);
                transferred += bytesRead;
                if (transferred % 50 == 0)
                    out.flush();
//	                if (listener != null)
//	                    listener.transferred(transferred);

            }


            out.write(barry);
            totalSent += barry.length;
            out.write(barry);
            totalSent += barry.length;
            out.flush();
            out.close();
//	            if (listener != null) {
//	                listener.waitServerResponse();
//	            }
            int status = urlConnection.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK) {
                String error = handleError(urlConnection);
                throw new XException(error);
            }
            readResult(urlConnection);
        } catch (IOException e) {
            e.printStackTrace();
//	            throw new XException(errorStr, e);
            return false;

        } finally {
            Utility.closeSilently(fis);
            Utility.closeSilently(out);
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return true;
    }
}
