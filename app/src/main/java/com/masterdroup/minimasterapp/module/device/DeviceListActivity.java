package com.masterdroup.minimasterapp.module.device;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.blankj.utilcode.utils.NetworkUtils;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.api.GizWifiSSID;
import com.gizwits.gizwifisdk.enumration.GizWifiConfigureMode;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.enumration.GizWifiGAgentType;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.masterdroup.minimasterapp.R;
import com.yuyh.library.imgsel.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static com.gizwits.gizwifisdk.enumration.GizWifiErrorCode.GIZ_SDK_DEVICE_CONFIG_IS_RUNNING;

public class DeviceListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);


        //
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        LogUtils.d("wifiInfo::::::::::::", wifiInfo.toString());
        LogUtils.d("SSID::::::::::", wifiInfo.getSSID());
        String ssid = whetherToRemoveTheDoubleQuotationMarks(wifiInfo.getSSID());


        //在 Soft-AP 模式时，获得设备的 SSID 列表。SSID 列表通过异步回调方式返回
//        GizWifiSDK.sharedInstance().setListener(getSSIDListListener);
////        GizWifiSDK.sharedInstance().getSSIDList();
//
//
//         //AirLink配置
//        GizWifiSDK.sharedInstance().setListener(AirLink_mListener);
//        List<GizWifiGAgentType> types = new ArrayList<>();
//        types.add(GizWifiGAgentType.GizGAgentESP);
//        GizWifiSDK.sharedInstance().setDeviceOnboarding(ssid, "Mk2925288", GizWifiConfigureMode.GizWifiAirLink, null, 60, types);

        //SoftAP配置
//        GizWifiSDK.sharedInstance().setListener(SoftAP_mListener);
//        GizWifiSDK.sharedInstance().setDeviceOnboarding(ssid, "Mk2925288", GizWifiConfigureMode.GizWifiSoftAP, "XPG-GAgent-", 60, null);


    }

    // 实现回调
    GizWifiSDKListener getBoundDevices_mListener = new GizWifiSDKListener() {
        @Override
        public void didDiscovered(GizWifiErrorCode result,
                                  List<GizWifiDevice> deviceList) {
            // 提示错误原因
            if (result != GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                Log.d("", "result: " + result.name());
            }
            // 显示设备列表
            Log.d("", "discovered deviceList: " + deviceList);
        }
    };


    GizWifiSDKListener getSSIDListListener = new GizWifiSDKListener() {
        @Override
        public void didGetSSIDList(GizWifiErrorCode result, List<GizWifiSSID> ssidInfoList) {
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                // 获取成功
                LogUtils.d("GizWifiSDK", "getSSIDList--获取成功");


            } else {
                // 获取失败
                LogUtils.d("GizWifiSDK", "getSSIDList--获取失败");

            }
        }
    };


    GizWifiSDKListener AirLink_mListener = new GizWifiSDKListener() {
        //等待配置完成或超时，回调配置完成接口
        @Override
        public void didSetDeviceOnboarding(GizWifiErrorCode result, String mac, String did, String productKey) {
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                // 配置成功
                LogUtils.d("GizWifiSDK", "AirLink模式-配置成功");

            } else if (result == GIZ_SDK_DEVICE_CONFIG_IS_RUNNING) {
                // 正在配置
                LogUtils.d("GizWifiSDK", "AirLink模式-正在配置");
            } else {
                // 配置失败
                LogUtils.d("GizWifiSDK", "AirLink模式-配置失败");

            }
        }
    };

    //模块收到配置信息，尝试连接路由器并自动关闭热点
    //让手机连接到配置的wifi上
    GizWifiSDKListener SoftAP_mListener = new GizWifiSDKListener() {
        //等待配置完成或超时，回调配置完成接口
        @Override
        public void didSetDeviceOnboarding(GizWifiErrorCode result, String mac, String did, String productKey) {
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                // 配置成功
                LogUtils.d("GizWifiSDK", "SoftAP模式-配置成功");
            } else {
                // 配置失败
                LogUtils.d("GizWifiSDK", "SoftAP模式-配置失败");
            }
        }
    };


    //根据Android的版本判断获取到的SSID是否有双引号
    public String whetherToRemoveTheDoubleQuotationMarks(String ssid) {

        //获取Android版本号
        int deviceVersion = Build.VERSION.SDK_INT;

        if (deviceVersion >= 17) {

            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {

                ssid = ssid.substring(1, ssid.length() - 1);
            }

        }
        return ssid;
    }
}
