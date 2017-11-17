package com.mastergroup.smartcook.module.device;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gelitenight.waveview.library.WaveView;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.model.Null;
import com.mastergroup.smartcook.view.WaveHelper;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CookingActivity extends Activity {
    @Bind(R.id.wave)
    WaveView mWaveView;
    @Bind(R.id.seekBar)
    SeekBar seekBar;
    @Bind(R.id.border)
    TextView border;
    @Bind(R.id.iv_return)
    ImageView ivReturn;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @Bind(R.id.progress_tv)
    TextView progressTv;
    @Bind(R.id.temperature_text_tv)
    TextView temperatureTextTv;
    @Bind(R.id.temperature_tv)
    TextView temperatureTv;
    @Bind(R.id.wave_rl)
    RelativeLayout waveRl;
    @Bind(R.id.start_time_tv)
    TextView startTimeTv;
    @Bind(R.id.start_time_value_tv)
    TextView startTimeValueTv;
    @Bind(R.id.end_time_tv)
    TextView endTimeTv;
    @Bind(R.id.end_time_value_tv)
    TextView endTimeValueTv;
    @Bind(R.id.time_Ll)
    RelativeLayout timeLl;

    Context mContext;

    SpeechSynthesizer mTts;  //科大讯飞
    private WaveHelper mWaveHelper;

    private int mBorderColor = Color.parseColor("#44FFFFFF");
    private int mBorderWidth = 2;


    GizWifiDevice gizWifiDevice;
    int sn = 5;
    byte[] recipesData = null;

    ArrayList<String> soundSourceData = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cooking_wave_view);
        ButterKnife.bind(this);
        mContext = this;

        initData();

        if(gizWifiDevice != null) {
            gizWifiDevice.setSubscribe(true);
            gizWifiDevice.setListener(mListener);
        } else {
            Log.i("gizWifiDevice", "gizWifiDevice = null");
        }



        mWaveView.setBorder(mBorderWidth, mBorderColor);
        mWaveView.setWaterLevelRatio(0F);
        mWaveHelper = new WaveHelper(mWaveView);

        mWaveView.setWaveColor(
                Color.parseColor("#26C6E2FF"),  //#66C6E2FF
                Color.parseColor("#56C6E2FF")); //#96C6E2FF
        mBorderColor = Color.parseColor("#C6E2FF");
        mWaveView.setBorder(mBorderWidth, mBorderColor);

        initTTS();


        ((SeekBar) findViewById(R.id.seekBar))
                .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        // mBorderWidth = i;
                        //  mWaveView.setBorder(mBorderWidth, mBorderColor);
                        //    int temp = new Random().nextInt(150);
                        int temp = 100 + (new Random().nextInt(30));
                        progressTv.setText("" + i);
                        mWaveView.setWaterLevelRatio((float) i / 100);
                        temperatureTv.setText(temp + "℃");

                        if(i == 0) {
                            mTts.startSpeaking("开始烹饪!", null);
                        } else if(i == 100) {
                            mTts.startSpeaking("烹饪完成!", null);
                        } else if(temp > 120) {
                            mTts.startSpeaking("温度过高!", null);
                        }

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

    }

    private void initData() {
        gizWifiDevice = getIntent().getParcelableExtra("gizDevice");
        soundSourceData = getIntent().getStringArrayListExtra("soundSourceData");
        recipesData = getIntent().getByteArrayExtra("recipesData");
        int dataSize = recipesData.length;
        for(int i = 0; i < dataSize; i ++) {

        }
    }

    private void sendDataToRhood() {
        if(gizWifiDevice != null) {
            gizWifiDevice.setListener(mListener);
            byte[] sendData = new byte[512];
            int recipesDataSize = recipesData.length;
            for (int i = 0; i < recipesDataSize; i++) {
                sendData[i] = recipesData[i];
            }
            ConcurrentHashMap<String, Object> command = new ConcurrentHashMap<String, Object>();
            command.put("RECIPES_DATA", sendData);
            gizWifiDevice.write(command, sn);
        } else {
            Log.i("gizWifiDevice", "gizWifiDevice = null");
        }

    }

    private void initTTS() {
        try {
            //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
            mTts = SpeechSynthesizer.createSynthesizer(mContext, null);
            //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
            mTts.setParameter(SpeechConstant.SPEED, "30");
            mTts.setParameter(SpeechConstant.VOLUME, "80");
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);//设置云端
            //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
            //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
            //如果不需要保存合成音频，注释该行代码
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @OnClick({R.id.iv_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_return:

                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWaveHelper.start();
    }

    /** 电磁炉相关字段 */
    public static final String PAN_TEMP = "PAN_TEMP";
    public static final String IH_POWER = "IH_POWER";
    public static final String COOK_STEP = "COOK_STEP";
    public static final String IH_SW = "IH_SW";
    public static final String RECIPES_DATA = "RECIPES_DATA";
    public static final String IH_ID = "IH_ID";
    public static final String IH_MID = "IH_MID";
    public static final String PAN_ID = "PAN_ID";
    public static final String PAN_MID = "PAN_MID";
    public static final String INTERRUPT = "INTERRUPT";
    public static final String RECIPES_ID = "RECIPES_ID";
    public static final String RECIPES_COOKING_STATE = "RECIPES_COOKING_STATE";

    byte[] IhId, panMid, IhRecipesData, panId, recipesId, IhMid;
    int cookStep, IhPower, cookingState;
    float panTemp;
    boolean interrupt, IhSw;

    /** 机智云回调 */
    GizWifiDeviceListener mListener = new GizWifiDeviceListener() {
        @Override
        public  void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
            // Log.i("didReceiveData", "result = " + result);
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                if(dataMap.get("data") != null) {
                    ConcurrentHashMap<String, Object> mMap = (ConcurrentHashMap<String, Object>) dataMap.get("data");
                    Log.i("didReceiveData", "dataMap = " + dataMap.get("data").toString());
                    freshViewData(mMap);
                }
                if (sn == 5) {
                    Log.i("sendSuccess", "发送数据成功!");
                } else {
                    // 其他命令的ack或者数据上报
                }
            } else {
                // 操作失败
                Log.i("sendSuccess", "result = " + result);
            }
        }

        @Override
        public  void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                // 订阅或解除订阅成功
                sendDataToRhood();
            } else {
                // 失败
            }
        }

    };


    /** 处理机智云上报的数据 */
    private void freshViewData(ConcurrentHashMap<String, Object> mMap) {
        Set<String> mSet = mMap.keySet();
       Log.i("didReceiveDataFresh", "mSet.toString() = " + mSet.toString());
        for(String key : mMap.keySet()) {
            if (key.equals(IH_ID)) IhId = (byte[]) mMap.get(IH_ID);
            if (key.equals(IH_MID)) IhMid = (byte[]) mMap.get(IH_MID);
            if (key.equals(PAN_ID)) panId = (byte[]) mMap.get(PAN_ID);
            if (key.equals(PAN_MID)) panMid = (byte[]) mMap.get(PAN_MID);
            if (key.equals(COOK_STEP)) cookStep = (int) mMap.get(COOK_STEP);
            if (key.equals(RECIPES_COOKING_STATE)) {
                IhRecipesData = (byte[]) mMap.get(RECIPES_COOKING_STATE);
                Log.i("didReceiveDataFresh", "IhRecipesData = " + IhRecipesData);
            }
            if (key.equals(PAN_TEMP)) panTemp = (float) mMap.get(panTemp);
            if (key.equals(IH_SW)) IhSw = (boolean) mMap.get(IH_SW);
        }
    }


}
