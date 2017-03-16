package com.masterdroup.minimasterapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.util.List;


/**
 * Description: 有关网络的工具类
 * Created by andmobi003 on 2016/7/13 15:50
 */
public class NetUtils {

    public static final String TAG = "NetUils";

    /**
     * 是否有网络
     */
    public static boolean hasNetwork(Context context) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork.isConnectedOrConnecting();
        } catch (Exception e) {
            DebugUtils.e(TAG, e.getMessage());
            return false;
        }
    }


    /**
     * 是否是使用wifi网络
     */

    public static boolean isWiFi(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork.isConnectedOrConnecting()) {
                return activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            } else {
                return false;
            }
        } catch (Exception e) {
            DebugUtils.e(TAG, e.getMessage());
            return false;
        }

    }

    /**
     * 获取当前WIFI的SSID.
     *
     * @param context
     *            上下文
     * @return ssid
     *
     *         *
     */
    public static String getCurentWifiSSID(Context context) {
        String ssid = "";
        if (context != null) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            if(wifiInfo!=null){
                ssid = wifiInfo.getSSID();
                if (ssid.substring(0, 1).equals("\"") && ssid.substring(ssid.length() - 1).equals("\"")) {
                    ssid = ssid.substring(1, ssid.length() - 1);
                }
            }

        }
        return whetherToRemoveTheDoubleQuotationMarks(ssid);
    }


    //根据Android的版本判断获取到的SSID是否有双引号
    public static String whetherToRemoveTheDoubleQuotationMarks(String ssid) {

        //获取Android版本号
        int deviceVersion = Build.VERSION.SDK_INT;

        if (deviceVersion >= 17) {

            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {

                ssid = ssid.substring(1, ssid.length() - 1);
            }

        }
        return ssid;
    }
    /**
     * 用来获得手机扫描到的所有wifi的信息.
     *
     * @param c
     *            上下文
     * @return the current wifi scan result
     */
    static public List<ScanResult> getCurrentWifiScanResult(Context c) {
        WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        return wifiManager.getScanResults();
    }

    /**
     * 检查网络连通性（工具方法）
     *
     * @param context
     * @return
     */
    public static boolean checkNetwork(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = conn.getActiveNetworkInfo();
        if (net != null && net.isConnected()) {
            return true;
        }
        return false;
    }

}
