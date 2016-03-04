package com.meidebi.app.support.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class IOUtils {
    public static boolean isWifiAvailable(Context context) {
        boolean isWifiAvailable = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo!=null && activeNetInfo.getType()==ConnectivityManager.TYPE_WIFI) {
            isWifiAvailable = true;
        }
        return isWifiAvailable;
    }

    public static String stream2String(final InputStream instream) throws IOException {
        final StringBuilder sb = new StringBuilder();
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(instream,
                    "UTF-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } finally {
            closeStream(instream);
        }
        return sb.toString();
    }
    
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
                byte[] bytes = new byte[buffer_size];
                for (;;) {
                        int count = is.read(bytes, 0, buffer_size);
                        if (count == -1)
                                break;
                        os.write(bytes, 0, count);
                }
        } catch (Exception ex) {
         }
    }

    public static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                android.util.Log.e("IOUtils", "Could not close stream", e);
            }
        }
    }
}
