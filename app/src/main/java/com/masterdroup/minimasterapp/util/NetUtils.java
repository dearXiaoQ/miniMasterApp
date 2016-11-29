package com.masterdroup.minimasterapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


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
}
