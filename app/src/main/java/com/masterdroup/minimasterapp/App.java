package com.masterdroup.minimasterapp;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.utils.SPUtils;
import com.blankj.utilcode.utils.Utils;
import com.gizwits.gizwifisdk.api.GizWifiSDK;

/**
 * Created by 11473 on 2016/11/29.
 */

public class App extends Application {
    public static Context mContext;


    public static SPUtils spUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        GizWifiSDK.sharedInstance().startWithAppID(getApplicationContext(), Constant.APP_ID);//启动机智云SDK


        Utils.init(mContext);
        try {
            setSPUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSPUp() throws Exception {
        if (spUtils == null) {
            /**
             * SharedPreferences存储在sd卡中的文件名字
             */
            spUtils = new SPUtils(getPackageName());

        }
    }
}
