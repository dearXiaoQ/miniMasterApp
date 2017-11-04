package com.mastergroup.smartcook;

import android.content.Context;
import android.view.WindowManager;

import com.blankj.utilcode.utils.SPUtils;
import com.blankj.utilcode.utils.Utils;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizEventType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.mob.MobApplication;
import com.yuyh.library.imgsel.utils.LogUtils;

/**
 * Created by 11473 on 2016/11/29.
 */

public class App extends MobApplication implements Thread.UncaughtExceptionHandler {
    public static Context mContext;

    public static SPUtils spUtils;

    public static int DISPLAY_WIDTH  = 0;
    public static int DISPLAY_HEIGHT = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Utils.init(mContext);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DISPLAY_WIDTH = wm.getDefaultDisplay().getWidth();
        DISPLAY_HEIGHT = wm.getDefaultDisplay().getHeight();

        try {
            setSPUp();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            GizWifiSDK.sharedInstance().startWithAppID(getApplicationContext(), Constant.APP_ID, Constant.APP_Secret, null, null, false);//启动机智云SDK
            GizWifiSDK.sharedInstance().setListener(mListener);

        } catch (VerifyError e) {
            e.printStackTrace();
        }



    }

    GizWifiSDKListener mListener = new GizWifiSDKListener() {
        @Override
        public void didNotifyEvent(GizEventType eventType, Object eventSource, GizWifiErrorCode eventID, String eventMessage) {
            if (eventType == GizEventType.GizEventSDK) {
                // SDK的事件通知
                LogUtils.i("GizWifiSDK", "SDK event happened: " + eventID + ", " + eventMessage);
            } else if (eventType == GizEventType.GizEventDevice) {
                // 设备连接断开时可能产生的通知
                GizWifiDevice mDevice = (GizWifiDevice) eventSource;
                LogUtils.i("GizWifiSDK", "device mac: " + mDevice.getMacAddress() + " disconnect caused by eventID: " + eventID + ", eventMessage: " + eventMessage);
            } else if (eventType == GizEventType.GizEventM2MService) {
                // M2M服务返回的异常通知
                LogUtils.i("GizWifiSDK", "M2M domain " + (String) eventSource + " exception happened, eventID: " + eventID + ", eventMessage: " + eventMessage);
            } else if (eventType == GizEventType.GizEventToken) {
                // token失效通知
                LogUtils.i("GizWifiSDK", "token " + (String) eventSource + " expired: " + eventMessage);
            }
        }
    };

    public void setSPUp() throws Exception {
        if (spUtils == null) {
            /**
             * SharedPreferences存储在sd卡中的文件名字
             */
            spUtils = new SPUtils(getPackageName());

        }
    }


    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        throwable.printStackTrace();
    }
}
