package com.mastergroup.smartcook.module.device;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.mastergroup.smartcook.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InductionTestActivity extends Activity {

    @Bind(R.id.iv_return)
    ImageView ivReturn;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_more_button)
    TextView tvMoreButton;
    @Bind(R.id.share)
    ImageView share;
    @Bind(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @Bind(R.id.switch1)
    Switch switch1;
    @Bind(R.id.rl_switch)
    RelativeLayout rlSwitch;
    @Bind(R.id.cook_step_tv)
    TextView cookStepTv;
    @Bind(R.id.rl_cook_step)
    RelativeLayout rlCookStep;
    @Bind(R.id.power_tv)
    TextView powerTv;
    @Bind(R.id.reduce_iv)
    ImageView reduceIv;
    @Bind(R.id.rl_ih_power)
    RelativeLayout rlIhPower;
    @Bind(R.id.seekBar)
    SeekBar seekBar;
    @Bind(R.id.add_iv)
    ImageView addIv;


    GizWifiDevice gizDev;

    Context mContext;

    int power = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_induction_test);
        ButterKnife.bind(this);
        mContext = this;
        setData();

    }

    private void setData() {
        final ArrayList<Parcelable> devList = (ArrayList<Parcelable>) getIntent().getSerializableExtra("gizDev");
        gizDev = (GizWifiDevice) devList.get(0);
        gizDev.setSubscribe(true);
        gizDev.setListener(mListener);

        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int sn = 5;
                ConcurrentHashMap<String, Object> command = new ConcurrentHashMap<>();
                command.put("IH_SW", switch1.isChecked());
                gizDev.write(command, sn);
            }
        });

        seekBar.setMax(10);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int sn = 5;
                ConcurrentHashMap<String, Object> command = new ConcurrentHashMap<>();
                command.put("IH_POWER", progress);
                gizDev.write(command, sn);
                power = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    /*    reduceIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(power > 0) {
                    power --;
                    int sn = 5;
                    ConcurrentHashMap<String, Object> command = new ConcurrentHashMap<>();
                    command.put("IH_SW", power);
                    gizDev.write(command, sn);
                }
            }
        });

        addIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(power < 10) {
                    power ++;
                    int sn = 5;
                    ConcurrentHashMap<String, Object> command = new ConcurrentHashMap<>();
                    command.put("IH_SW", power);
                    gizDev.write(command, sn);
                }
            }
        });*/

    }

    @OnClick({R.id.reduce_iv, R.id.add_iv})
    public void OnClick(View view) {

        switch (view.getId()) {
            case R.id.reduce_iv:
                power --;
                break;

            case R.id.add_iv:
                power ++;
                break;
        }
        int sn = 5;
        ConcurrentHashMap<String, Object> command = new ConcurrentHashMap<>();
        command.put("IH_POWER", power);
        gizDev.write(command, sn);
    }

    private GizWifiDeviceListener mListener = new GizWifiDeviceListener() {
   /*     @Override
        public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
            if(result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                boolean IH_SW = (boolean) dataMap.get("IH_SW");
                switch1.setChecked(IH_SW);
                int IH_POWER = (int) dataMap.get("IH_POWER");
                powerTv.setText(IH_POWER + "");
            }
        }
*/


        @Override
        public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                // 已定义的设备数据点，有布尔、数值和枚举型数据
                if (dataMap.get("data") != null) {
                    ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("data");
                    // 普通数据点，打印对应的key和value
                    StringBuilder sb = new StringBuilder();
                    for (String key : map.keySet()) {
                        sb.append(key + "  : " + map.get(key) + "\r\n");
                        Toast.makeText(mContext,
                                sb.toString(), Toast.LENGTH_SHORT).show();
                        if (key.equals("IH_SW")) {
                            switch1.setChecked((Boolean) map.get(key));
                        }

                        if (key.equals("IH_POWER")) {
                            power = Integer.valueOf((String) map.get(key));
                            seekBar.setProgress((power));
                            powerTv.setText(power + "");
                        }
                    }
                    // 扩展数据点，key如果是“BBBB”
                    byte[] bytes = (byte[]) map.get("IH_ID");
                    String string = Arrays.toString(bytes);
                    Toast.makeText(mContext,
                            string, Toast.LENGTH_SHORT).show();
                }
                // 已定义的设备故障数据点，设备发生故障后该字段有内容，没有发生故障则没内容
                if (dataMap.get("faults") != null) {
                    ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("faults");
                    StringBuilder sb = new StringBuilder();
                    for (String key : map.keySet()) {
                        sb.append(key + "  :" + map.get(key) + "\r\n");
                        Toast.makeText(mContext,
                                sb.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                // 已定义的设备报警数据点，设备发生报警后该字段有内容，没有发生报警则没内容
                if (dataMap.get("alerts") != null) {
                    ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("alerts");
                    StringBuilder sb = new StringBuilder();
                    for (String key : map.keySet()) {
                        sb.append(key + "  :" + map.get(key) + "\r\n");
                        Toast.makeText(mContext,
                                sb.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                // 透传数据，无数据点定义，适合开发者自行定义协议自行解析
                if (dataMap.get("binary") != null) {
                    byte[] binary = (byte[]) dataMap.get("binary");
                  /*  Log.i("", "Binary data:"
                            + bytesToHex(binary, 0, binary.length));*/
                }
            }
        }


    };

}
