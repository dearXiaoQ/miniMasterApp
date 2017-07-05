package com.mastergroup.smartcook.util;

import android.util.Log;

/**
 * Description:
 * Created by andmobi003 on 2016/7/8 14:28
 */
public class DebugUtils {


    public static final String TAG = "DebugUtils";

    public static void e(String tag, String str) {
        Log.e(String.format("%s: %s", TAG, tag), str);
    }

    public static void d(String tag, String str) {
        Log.d(String.format("%s: %s", TAG, tag), str);
    }



}
