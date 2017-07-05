package com.mastergroup.smartcook.module.device;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.api.GizWifiSSID;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.util.NetUtils;
import com.yuyh.library.imgsel.utils.LogUtils;

import java.util.List;

import butterknife.ButterKnife;

import static com.gizwits.gizwifisdk.enumration.GizWifiErrorCode.GIZ_SDK_DEVICE_CONFIG_IS_RUNNING;

public class DeviceListActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        ButterKnife.bind(this);


        String ssid = NetUtils.getCurentWifiSSID(this);
        String paw = "Mk2925288";


        //SoftAP配置    红灯长亮
//        GizWifiSDK.sharedInstance().setListener(SoftAP_mListener);
//        GizWifiSDK.sharedInstance().setDeviceOnboarding(ssid, paw, GizWifiConfigureMode.GizWifiSoftAP, "XPG-GAgent-DF4A", 60, null);

        //在 Soft-AP 模式时，获得设备的 SSID 列表。SSID 列表通过异步回调方式返回
        GizWifiSDK.sharedInstance().setListener(getSSIDListListener);
        GizWifiSDK.sharedInstance().getSSIDList();

//         //AirLink配置   绿灯闪亮
//        GizWifiSDK.sharedInstance().setListener(AirLink_mListener);
//        List<GizWifiGAgentType> types = new ArrayList<>();
//        types.add(GizWifiGAgentType.GizGAgentESP);
//        GizWifiSDK.sharedInstance().setDeviceOnboarding(ssid, paw, GizWifiConfigureMode.GizWifiAirLink, null, 60, types);
//
//
//        String giz_uid = App.spUtils.getString(App.mContext.getString(R.string.giz_uid));
//        String giz_token = App.spUtils.getString(App.mContext.getString(R.string.giz_token));

//        GizWifiSDK.sharedInstance().setListener(getBoundDevices_mListener);
//        GizWifiSDK.sharedInstance().getBoundDevices(giz_uid,
//                giz_token, null);

    }


    // 实现回调
    GizWifiSDKListener getBoundDevices_mListener = new GizWifiSDKListener() {
        @Override
        public void didDiscovered(GizWifiErrorCode result,
                                  List<GizWifiDevice> deviceList) {
            // 提示错误原因
            if (result != GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                LogUtils.e("GizWifiSDK", "result: " + result.name());
            } else {
                // 显示设备列表
                Log.d("GizWifiSDK", "discovered deviceList: " + deviceList);
            }
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


}
