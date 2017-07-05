package com.mastergroup.smartcook.module.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.mastergroup.smartcook.R;
import com.yuyh.library.imgsel.utils.LogUtils;

import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceControlActivity extends AppCompatActivity {

    @Bind(R.id.textView2)
    TextView mTextView2;
    @Bind(R.id.tvMAC)
    TextView mTvMAC;
    @Bind(R.id.b_switch)
    Button mBSwitch;
    @Bind(R.id.tv_device_info)
    TextView mTvDeviceInfo;

    /**
     * The GizWifiDevice device
     */
    private GizWifiDevice device;
    /**
     * 在云端创建的数据点标识名
     */
    public static final String SWITCH = "power";
    public static final String STATUS = "status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);
        ButterKnife.bind(this);
        initDevice();
        initView();
    }

    private void initDevice() {
        Intent intent = getIntent();
        device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        Log.i("Apptest", device.getDid());
    }

    private void initView() {

        mTvMAC = (TextView) findViewById(R.id.tvMAC);
        if (null != device) {
            mTvMAC.setText(device.getMacAddress().toString());
        }

        // mDevice是从设备列表中获取到的设备实体对象
        device.setListener(mListener);
        device.getDeviceStatus(null);
    }

    int sn;
    // 实现回调
    GizWifiDeviceListener mListener = new GizWifiDeviceListener() {
        @Override
        public void didReceiveData(GizWifiErrorCode result, GizWifiDevice
                device, ConcurrentHashMap<String, Object> dataMap, int sn_) {
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                // 查询成功
                LogUtils.d("GizWifiDeviceListener", "查询成功*****" + "\n设备：" + device.toString() + "\n节点：" + dataMap.toString());
                if (dataMap.get("data") != null) {
                    sn = sn_;
                    ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("data");
                    // 根据标识名，在回调的map中找到设备上报的值
                    if (map.get(STATUS) != null) {
                        int switchs = (int) map.get(STATUS);
                        String info = "";

                        switch (switchs) {
                            case 0:
                                info = "关";
                                break;
                            case 1:
                                info = "开";
                                break;
                        }
                        mTvDeviceInfo.setText(info);
                    }
                }
            } else {
                // 查询失败
            }
        }
    };


    @OnClick(R.id.b_switch)
    public void onClick() {
        //开关
        ConcurrentHashMap<String, Object> command = new ConcurrentHashMap<String, Object>();
        // map中key为云端创建数据点的标识名，value为需要传输的值
        command.put(SWITCH, true);
        // 调用write方法即可下发命令
        device.write(command, sn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出控制页面，取消设备订阅
        device.setSubscribe(false);
    }
}
