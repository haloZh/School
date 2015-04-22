package com.ronguan.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by haloZh on 2015/4/21.
 */
public class Httputil {

    /**
     * 代理设置
     *

     * @param context
     * @return
     */
    private static HttpResponse request(Context context, HttpRequestBase request)
            throws ClientProtocolException, IOException {
        HttpResponse response = null;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        if (isCMWAP(context)) {
            try {
                HttpHost proxy = new HttpHost("10.0.0.172", 80, "http");
                httpClient.getParams().setParameter(
                        ConnRoutePNames.DEFAULT_PROXY, proxy);
                response = httpClient.execute(request);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            response = httpClient.execute(request);
        }
        return response;
    }

    /**
     * 联网状态
     *
     * @param context
     * @return
     */
    private static boolean isCMWAP(Context context) {
        boolean isCMWAP = false;
        try {
            ConnectivityManager con = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = con.getActiveNetworkInfo();
            if (networkInfo != null
                    && "WIFI".equals(networkInfo.getTypeName().toUpperCase())) {
                return isCMWAP;
            } else {
                Cursor cursor = context.getContentResolver().query(
                        Uri.parse("content://telephony/carriers/preferapn"),
                        new String[]{"apn"}, null, null, null);
                cursor.moveToFirst();
                if (cursor.isAfterLast()) {
                    isCMWAP = false;
                }
                try {
                    if ("cmwap".equals(cursor.getString(0))
                            || "uniwap".equals(cursor.getString(0))) {
                        isCMWAP = true;
                    } else {
                        isCMWAP = false;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                return isCMWAP;
            }
        } catch (Exception ex) {

        }
        return isCMWAP;
    }

    /**
     * 联网状态
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager con = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();
        if (networkInfo != null
                && "WIFI".equals(networkInfo.getTypeName().toUpperCase())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 联网检查
     *
     * @param context
     * @return
     */
    public static boolean checkNet(Context context) {
        try {
            ConnectivityManager con = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = con.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isAvailable()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
    }
}
