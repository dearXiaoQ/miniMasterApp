package com.mastergroup.smartcook.module.device;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiConfigureMode;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.enumration.GizWifiGAgentType;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.module.CommonModule.GIZBaseActivity;
import com.mastergroup.smartcook.util.ToastUtils;
import com.mastergroup.smartcook.view.RoundProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;


public class AddDevConfigCountdownActivity extends GIZBaseActivity {


    @Bind(R.id.cancel_tv)
    TextView cancelTv;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.rpbConfig)
    RoundProgressBar rpbConfig;

    @Bind(R.id.textView6)
    TextView textView6;
    @Bind(R.id.find_device_tv)
    TextView findDeviceTv;
    @Bind(R.id.textView9)
    TextView textView9;
    @Bind(R.id.device_register_giz)
    TextView deviceRegisterGiz;
    @Bind(R.id.textView7)
    TextView textView7;
    @Bind(R.id.init_device)
    TextView initDevice;
    /**
     * The tv Time
     */
    private TextView tvTimer;


    /**
     * The timer
     */
    Timer timer;

    /**
     * 倒计时
     */
    int secondleft = 60;

    /**
     * 配置用参数
     */
    String workSSID, workSSIDPsw;

    /**
     * The String
     */
    String timerText;

    List<GizWifiGAgentType> modeList, modeDataList;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_count_down_item);
        ButterKnife.bind(this);
        // ButterKnife.bind(this);
        mContext = this;
       /* initData();
        initView();
        startAirlink();*/

        tvTimer = (TextView) findViewById(R.id.title_tv);

        initData();
        startAirlink();


    }


    @Override
    protected void onResume() {
        super.onResume();
        GizWifiSDK.sharedInstance().setListener(mListener);
    }

    private void initData() {
        workSSID = spf.getString("workSSID", "");
        workSSIDPsw = spf.getString("workSSIDPsw", "");
        modeDataList = new ArrayList<>();
        modeDataList.add(GizWifiGAgentType.GizGAgentMXCHIP);
        modeDataList.add(GizWifiGAgentType.GizGAgentHF);
        modeDataList.add(GizWifiGAgentType.GizGAgentRTK);
        modeDataList.add(GizWifiGAgentType.GizGAgentWM);
        modeDataList.add(GizWifiGAgentType.GizGAgentESP);
        modeDataList.add(GizWifiGAgentType.GizGAgentQCA);
        modeDataList.add(GizWifiGAgentType.GizGAgentTI);
        modeDataList.add(GizWifiGAgentType.GizGAgentFSK);
        modeDataList.add(GizWifiGAgentType.GizGAgentMXCHIP3);
        modeDataList.add(GizWifiGAgentType.GizGAgentBL);
        modeList = new ArrayList<>();

        modeList.add(GizWifiGAgentType.GizGAgentESP);

        GizWifiSDK.sharedInstance().setDeviceOnboarding(workSSID, workSSIDPsw, GizWifiConfigureMode.GizWifiAirLink, null, 60, modeList);

    }

    private void initView() {
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        rpbConfig = (RoundProgressBar) findViewById(R.id.rpbConfig);
    }

    private void startAirlink() {
        GizWifiSDK.sharedInstance().setDeviceOnboarding(workSSID, workSSIDPsw, GizWifiConfigureMode.GizWifiAirLink,
                null, 60, modeList);
        handler.sendEmptyMessage(handler_key.START_TIMER.ordinal());

    }

    private enum handler_key {

        /**
         * 倒计时提示
         */
        TIMER_TEXT,

        /**
         * 倒计时开始
         */
        START_TIMER,

        /**
         * 配置成功
         */
        SUCCESSFUL,

        /**
         * 配置失败
         */
        FAILED,

    }


    /**
     * The handler.
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler_key key = handler_key.values()[msg.what];
            switch (key) {
                case TIMER_TEXT:

                    tvTimer.setText(timerText);
                    break;

                case START_TIMER:
                    isStartTimer();
                    break;

                case SUCCESSFUL:
                    Toast.makeText(AddDevConfigCountdownActivity.this, R.string.configuration_successful,
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;

                case FAILED:
                    Toast.makeText(AddDevConfigCountdownActivity.this, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddDevConfigCountdownActivity.this, AddDevSetDevStateActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                default:
                    break;

            }
        }

    };

    // 倒计时
    public void isStartTimer() {

        secondleft = 60;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                secondleft--;

                rpbConfig.setProgress((60 - secondleft) * (100 / 60.0));
                if (secondleft == 58) {
                    timerText = (String) getText(R.string.searching_device);
                    handler.sendEmptyMessage(handler_key.TIMER_TEXT.ordinal());
                } else if (secondleft == 30) {
                    timerText = (String) getText(R.string.searched_device);
                    handler.sendEmptyMessage(handler_key.TIMER_TEXT.ordinal());
                } else if (secondleft == 28) {
                    timerText = (String) getText(R.string.trying_join_device);
                    handler.sendEmptyMessage(handler_key.TIMER_TEXT.ordinal());
                }
            }
        }, 1000, 1000);
    }


    GizWifiSDKListener mListener = new GizWifiSDKListener() {
        //等待配置完成或超时，回调配置完成接口
        @Override
        public void didSetDeviceOnboarding(GizWifiErrorCode result, String mac, String did, String productKey) {
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                // 配置成功
                ToastUtils.showCustomToast(mContext, ToastUtils.TOAST_BOTTOM, "配置成功！");
            } else if (result == GizWifiErrorCode.GIZ_SDK_DEVICE_CONFIG_IS_RUNNING) {
                // 正在配置
                ToastUtils.showCustomToast(mContext, ToastUtils.TOAST_BOTTOM, "正在配置！");
            } else {
                // 配置失败
                ToastUtils.showCustomToast(mContext, ToastUtils.TOAST_BOTTOM, "配置失败！");
            }
        }
    };

}
