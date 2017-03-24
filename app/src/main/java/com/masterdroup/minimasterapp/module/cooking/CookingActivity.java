package com.masterdroup.minimasterapp.module.cooking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.api.Network;
import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.CookingStep;
import com.masterdroup.minimasterapp.model.Recipes;
import com.masterdroup.minimasterapp.module.progress.ProgressSubscriber;
import com.masterdroup.minimasterapp.util.JxUtils;
import com.yuyh.library.imgsel.utils.LogUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class CookingActivity extends AppCompatActivity {

    @Bind(R.id.tv_menu_name)
    TextView mTvMenuName;
    @Bind(R.id.tv_step_no)
    TextView mTvStepNo;
    @Bind(R.id.tv_step_info_current)
    TextView mTvStepInfoCurrent;
    @Bind(R.id.tv_step_info_next)
    TextView mTvStepInfoNext;
    @Bind(R.id.tv_power)
    TextView mTvPower;
    @Bind(R.id.tv_temperature)
    TextView mTvTemperature;
    @Bind(R.id.tv_device_info)
    TextView mTvDeviceInfo;
    @Bind(R.id.tv_step_time)
    TextView mTvStepTime;
    @Bind(R.id.tv_all_time)
    TextView mTvAllTime;
    @Bind(R.id.b_close_device)
    Button mBCloseDevice;
    @Bind(R.id.b_end_cooking)
    Button mBEndCooking;
    @Bind(R.id.activity_cooking)
    LinearLayout mActivityCooking;
    @Bind(R.id.ll_cooking)
    LinearLayout mLlCooking;
    @Bind(R.id.b_exit)
    Button mBExit;
    @Bind(R.id.ll_complete)
    LinearLayout mLlComplete;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    /**
     * 设备实体对像
     */
    private GizWifiDevice device;

    /**
     * 设备运行状态
     */
    private int device_run_status;

    /**
     * 设备当前功率
     */
    private int device_info_power;

    /**
     * 设备当前温度
     */
    private String device_info_temperature;

    /**
     * 烹饪总时间
     */
    private int cooking_all_time;


    /**
     * 菜谱对像
     */
    private Recipes.RecipesBean mRecipesBean;

    /**
     * 烹饪步骤列表
     */
    private List<CookingStep> cookingSteps;

    /**
     * 当前烹饪步骤
     */
    private CookingStep current_cs;

    /**
     * 下一步烹饪步骤
     */
    private CookingStep next_cs;


    /**
     * 当前步骤
     */
    private int step = 0;

    /**
     * 菜谱id
     */
    String recipesBeanID;

    int cooking_model;//烹饪模式 默认自动


    /**
     * 在云端创建的数据点标识名
     */
    public static final String SWITCH = "power";
    public static final String STATUS = "status";
    public static final String TEMPERATURE = "temperature";
    public static final String INCREASE = "increase";//增大功率
    public static final String DECREASE = "decrease";//减少功率
    public static final String POWER_LEVEL = "power_level";//电磁炉功率

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking);
        ButterKnife.bind(this);
        init();
        getRecipesBean();

    }


    private void init() {
        // mDevice是从设备列表中获取到的设备实体对象
        device = (GizWifiDevice) getIntent().getParcelableExtra("GizWifiDevice");
        device.setListener(mListener);


        recipesBeanID = getIntent().getStringExtra("_id");
        cooking_model = getIntent().getIntExtra("cooking_model", -1);
    }


    private void getRecipesBean() {
        Observable o = Network.getMainApi().getRecipesDetail(recipesBeanID);
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<Recipes.RecipesBean>>() {
            @Override
            public void onNext(Base<Recipes.RecipesBean> o) {
                if (o.getErrorCode() == 0) {
                    mRecipesBean = o.getRes();
                    cookingSteps = mRecipesBean.getCookingStep();
                    setData();
                }
            }
        }, this);

        JxUtils.toSubscribe(o, s);

    }

    private void setData() {
        device.getDeviceStatus(null);

        mTvMenuName.setText(mRecipesBean.getName());

    }

    private void setStepInfo(int step) {
        if (step + 1 < cookingSteps.size()) {
            current_cs = cookingSteps.get(step);
            mTvStepNo.setText(String.format("当前步骤:第%s步", step + 1));
            mTvStepInfoCurrent.setText(current_cs.getDescribe());
            next_cs = cookingSteps.get(step + 1);
            mTvStepInfoNext.setText(next_cs.getDescribe());
            startStepCountDown(current_cs.getDuration() * 10);

            upDataPower();
        } else if (step == cookingSteps.size() - 1) {

            current_cs = cookingSteps.get(step);
            mTvStepNo.setText(String.format("当前步骤:第%s步", step + 1));
            mTvStepInfoCurrent.setText(current_cs.getDescribe());
            mTvStepInfoNext.setText("无");
            startStepCountDown(current_cs.getDuration() * 10);
            upDataPower();
        } else {
            mLlCooking.setVisibility(View.GONE);
            mLlComplete.setVisibility(View.VISIBLE);
            LogUtils.d("CookingActivity:startStepCountDown:onCompleted()", String.format("烹饪完成，完成%d个步骤", step));
            mTvStepNo.setText("烹饪完成");

            mTvTime.setText("用时" + time + "秒");


            mTvStepInfoCurrent.setText("无");
            mTvStepInfoNext.setText("无");

            if (device_run_status == 1)//如果设备处于待机或工作状态 就发送关闭指令
                upDataDeviceInfoSWITCH();
        }

    }

    void up1(int i) {
        int de = device_info_power;
        if (de == 0)
            de = 400;


        if (de > i) {
            int j = (de - i) / 100;
            for (int t = 0; t < j; t++) {
                upDataDeviceInfoPower(DECREASE);
            }

        }
        if (de < i) {
            int j = (i - de) / 100;
            for (int t = 0; t < j; t++) {
                upDataDeviceInfoPower(INCREASE);
            }
        }

    }

    void upDataPower() {
        int i = 0;
        switch (current_cs.getPower()) {
            case 0:
                i = 0;
                break;
            case 1:
                i = 4;
                break;
            case 2:
                i = 8;
                break;
        }

        upDataDeviceInfoPower(i);
    }


    /**
     * 直接设置功率
     *
     * @param power 功率
     */
    void upDataDeviceInfoPower(int power) {

        //开关
        ConcurrentHashMap<String, Object> command = new ConcurrentHashMap<String, Object>();

        // map中key为云端创建数据点的标识名，value为需要传输的值
        command.put(POWER_LEVEL, power);

        // 调用write方法即可下发命令
        device.write(command, sn);

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
                    if (map.get(TEMPERATURE) != null) {
                        device_info_temperature = map.get(TEMPERATURE).toString();
                    }
                    if (map.get(STATUS) != null) {
                        int switchs = (int) map.get(STATUS);

                        String info = "";
                        switch (switchs) {
                            case 0:
                                device_info_power = 0;
                                info = "关闭";
                                device_run_status = 0;
                                break;
                            case 1:
                                device_run_status = 1;
                                device_info_power = 0;
                                info = "待机";
                                break;
                            case 2:
                                device_info_power = 400;
                                device_run_status = 1;
                                info = "工作";
                                break;
                            case 3:
                                device_info_power = 500;
                                device_run_status = 1;
                                info = "工作";
                                break;
                            case 4:
                                device_info_power = 800;
                                device_run_status = 1;
                                info = "工作";
                                break;
                            case 5:
                                device_info_power = 1000;
                                device_run_status = 1;
                                info = "工作";
                                break;
                            case 6:
                                device_info_power = 1200;
                                device_run_status = 1;
                                info = "工作";
                                break;
                            case 7:
                                device_info_power = 1400;
                                device_run_status = 1;
                                info = "工作";
                                break;
                            case 8:
                                device_info_power = 1600;
                                device_run_status = 1;
                                info = "工作";
                                break;
                            case 9:
                                device_info_power = 1800;
                                device_run_status = 1;
                                info = "工作";
                                break;
                            case 10:
                                device_info_power = 2100;
                                device_run_status = 1;
                                info = "工作";
                                break;
                        }
                        mTvDeviceInfo.setText("状态：" + info);
                        mTvPower.setText(device_info_power != 0 ? String.format("火力：%dW", device_info_power) : "火力:-");
                        mTvTemperature.setText(String.format("锅温：%s℃", device_info_temperature));
                    }
                }
            } else {
                // 查询失败
            }
        }
    };

    long time;

    Subscription subscription_cookingTiming, subscription_countDown;

    /**
     * 开始计时，烹饪总时间
     */
    private void startCookingTiming() {

        Observable o_timing = Observable.interval(1, TimeUnit.SECONDS);
        Subscriber s_timing = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long o) {
                LogUtils.d("CookingActivity", "烹饪总时间" + o + "s");

                mTvAllTime.setText(String.format("烹饪总时间:%d秒", o));
                time = o;
            }
        };
        subscription_cookingTiming = JxUtils.toSubscribeRe(o_timing, s_timing);

    }

    /**
     * 开始倒计时，步骤时间
     */
    private void startStepCountDown(int time) {

        final int countTime = time;
        Observable o_countDown = Observable.interval(1, TimeUnit.SECONDS).take(time + 1);
        Subscriber s_countDown = new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                LogUtils.d("CookingActivity:startStepCountDown:onCompleted()", String.format("步骤%d完成，下一步骤为步骤%d", step + 1, step + 2));

                step++;

                setStepInfo(step);

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long o) {
                LogUtils.d("CookingActivity", String.format("当前步骤倒计时:%d秒", countTime - o.intValue()));
                mTvStepTime.setText(String.format("当前步骤:%d秒", countTime - o.intValue()));
            }
        };
        subscription_countDown = JxUtils.toSubscribeRe(o_countDown, s_countDown);

    }


    private void upDataDeviceInfoSWITCH() {


        ConcurrentHashMap<String, Object> command = new ConcurrentHashMap<String, Object>();
        // map中key为云端创建数据点的标识名，value为需要传输的值
        command.put(SWITCH, true);
        // 调用write方法即可下发命令
        device.write(command, sn);
    }

    private void upDataDeviceInfoPower(String crease) {

        ConcurrentHashMap<String, Object> command = new ConcurrentHashMap<String, Object>();

        // map中key为云端创建数据点的标识名，value为需要传输的值
        command.put(crease, true);

        // 调用write方法即可下发命令
        device.write(command, sn);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unSubscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 退出控制页面，取消设备订阅
        if (device != null)
            device.setSubscribe(device.getProductKey(), false);


    }

    private void unSubscribe() {

        if (null != subscription_cookingTiming)
            subscription_cookingTiming.unsubscribe();
        if (null != subscription_countDown)
            subscription_countDown.unsubscribe();
    }


    @OnClick({R.id.b_close_device, R.id.b_end_cooking, R.id.b_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_close_device:
                unSubscribe();
                finish();
                break;
            case R.id.b_end_cooking:
                if (device_run_status == 0)//如果设备处于关闭状态 就发送待机指令
                    upDataDeviceInfoSWITCH();
                setStepInfo(step);//开始烹饪
                startCookingTiming();
                break;
            case R.id.b_exit:
                finish();
                break;
        }
    }
}
