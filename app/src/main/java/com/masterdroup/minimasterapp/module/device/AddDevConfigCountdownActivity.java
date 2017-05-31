package com.masterdroup.minimasterapp.module.device;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiConfigureMode;
import com.gizwits.gizwifisdk.enumration.GizWifiGAgentType;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.view.RoundProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AddDevConfigCountdownActivity extends WiFiConfigModuleBaseActivity {

    /** The tv Time */
    private TextView tvTimer;

    /** The rpb Config */
    private RoundProgressBar rpbConfig;

    /** The timer */
    Timer timer;

    /** 倒计时  */
    int secondleft = 60;

    /** 配置用参数 */
    String workSSID, workSSIDPsw;

    /** The String */
    String timerText;

    List<GizWifiGAgentType>  modeList, modeDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dev_config_countdown);

        initData();
        initView();
        startAirlink();

    }

    private void initData() {
        workSSID = spf.getString("workSSID", "");
        workSSIDPsw = spf.getString("workSSIDPsw", "");
        modeDataList = new ArrayList<GizWifiGAgentType>();
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
        modeList = new ArrayList<GizWifiGAgentType>();

        modeList.add(modeDataList.get(AddDevSettingWifiActivity.modeNum));

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

}
