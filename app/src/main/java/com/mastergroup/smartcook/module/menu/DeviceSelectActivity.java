package com.mastergroup.smartcook.module.menu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.mastergroup.smartcook.App;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.module.cooking.CookingActivity;
import com.yuyh.library.imgsel.utils.LogUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceSelectActivity extends AppCompatActivity {


    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_more_button)
    TextView mTvMoreButton;
    @Bind(R.id.rv_device)
    RecyclerView mRvDevice;

    @Bind(R.id.sch_model)
    Switch schModel;


    List<GizWifiDevice> devices = new ArrayList<>();

    QuickAdapter adapter;

    Context mContext;



    /**
     * 菜谱id
     */
    String recipesBeanID;


    int cooking_model = 0;//烹饪模式 默认自动
    /** 到万用板的数据 */
    byte[] recipesData = null;
    /** 菜谱的数据  */
    ArrayList<String>  soundSourceData = null;
    ArrayList<Integer> cookTimeList = null;
    //int cookStep, cookTimeSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_select);
        mContext = this;

        ButterKnife.bind(this);
        initView();
        initData();

    }

    private void initView() {
        setProgressDialog();
        mTvTitle.setText("选择设备");
        mTvMoreButton.setVisibility(View.GONE);
        Intent intent = getIntent();
        recipesBeanID = intent.getStringExtra("_id");
        recipesData = intent.getByteArrayExtra("recipesData");
        soundSourceData = intent.getStringArrayListExtra("soundSourceData");
        //cookStep = intent.getIntExtra("cookStep", 0);
        //cookTimeSecond = intent.getIntExtra("cookTimeSecond", 0);
        cookTimeList = intent.getIntegerArrayListExtra("cookTimeList");

        initSwitch();

        schModel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    cooking_model = 1;
                else
                    cooking_model = 0;
                initSwitch();
            }
        });
    }

    void initSwitch() {
        if (cooking_model == 0) {
            schModel.setChecked(false);
            schModel.setText(R.string.auto_model);
        } else {
            schModel.setText(R.string.manual_model);
            schModel.setChecked(true);
        }
    }

    private void initData() {
        adapter = new QuickAdapter();

        mRvDevice.setLayoutManager(new LinearLayoutManager(this));

        mRvDevice.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
          /*      GizWifiDevice device = devices.get(position);
                device.setListener(gizWifiDeviceListener);
                String productKey = device.getProductKey();
                //订阅设备
                device.setSubscribe(productKey, true);*/
                Intent intent = new Intent(mContext, com.mastergroup.smartcook.module.device.CookingActivity.class);
                intent.putExtra("gizDevice", devices.get(position));
                intent.putExtra("recipesData", recipesData);
                intent.putStringArrayListExtra("soundSourceData", soundSourceData);
              //  intent.putExtra("cookStep", cookStep);
              //  intent.putExtra("cookTimeSecond", cookTimeSecond);
                intent.putIntegerArrayListExtra("cookTimeList", cookTimeList);
                startActivity(intent);
                finish();

            }
        });
        String uid = App.spUtils.getString(App.mContext.getString(R.string.giz_uid));
        String token = App.spUtils.getString(App.mContext.getString(R.string.giz_token)); //
        GizWifiSDK.sharedInstance().setListener(gizWifiSDKListener);
        GizWifiSDK.sharedInstance().getBoundDevices(uid, token, null);
    }


    private GizWifiSDKListener gizWifiSDKListener = new GizWifiSDKListener() {

        public void didDiscovered(GizWifiErrorCode result, List<GizWifiDevice> list) {

            /** 超过7天使用自动登录，会出现token过期现象 */
            if(result == GizWifiErrorCode.GIZ_OPENAPI_TOKEN_EXPIRED) {
                //提示重新登录
            }

            if (result != GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                LogUtils.e("GizWifiSDK", "result: " + result.name());
            } else {
                // 显示设备列表
                LogUtils.d("GizWifiSDK", "discovered deviceList: " + list);
                List<GizWifiDevice> des = new ArrayList<>();
                for (GizWifiDevice gizWifiDevice : list) {
                    if (GizWifiDeviceNetStatus.GizDeviceOnline == gizWifiDevice.getNetStatus()
                            || GizWifiDeviceNetStatus.GizDeviceControlled == gizWifiDevice.getNetStatus()) {
                        if (gizWifiDevice.isBind() || gizWifiDevice.isLAN()) {
                            gizWifiDevice.setSubscribe(true);
                            des.add(gizWifiDevice);
                        }
                    }
                }

                devices = des;
                mRvDevice.setAdapter(new QuickAdapter());
            }
        }
    };

    /**
     * 设备监听
     */

    protected GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {

        @Override
        public void didSetSubscribe(GizWifiErrorCode result, final GizWifiDevice device, boolean isSubscribed) {
            progressDialogCancel();

            if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {

                gotoCooking(device);


            } else {
                if (device.isBind()) {
                    Toast.makeText(DeviceSelectActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }

    };


    private void gotoCooking(GizWifiDevice device) {

        Intent intent = new Intent(DeviceSelectActivity.this, CookingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("GizWifiDevice", device);
        bundle.putString("_id", recipesBeanID);
        bundle.putInt("cooking_model", cooking_model);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
        finish();

    }


    public class QuickAdapter extends BaseQuickAdapter<GizWifiDevice, BaseViewHolder> {
        public QuickAdapter() {
            super(R.layout.activity_device_select_item, devices);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, GizWifiDevice item) {

            String devName = mContext.getString(R.string.device_name);
            if(item.getProductName().equals("smartCook")) {
                devName = mContext.getString(R.string.rhood);
            }

            viewHolder.setText(R.id.tvDeviceName, item.getProductName())
                    .setText(R.id.tvDeviceMac, item.getMacAddress())
                    .setText(R.id.tvDeviceStatus, devName);

        }
    }


    @OnClick(R.id.iv_return)
    public void onClick() {
        finish();
    }


    /**
     * 等待框
     */
    public ProgressDialog progressDialog;

    /**
     * 设置ProgressDialog
     */
    public void setProgressDialog() {
        progressDialog = new ProgressDialog(this);
        String loadingText = getString(R.string.loadingtext);
        progressDialog.setMessage(loadingText);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void progressDialogCancel() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.cancel();

    }
}
