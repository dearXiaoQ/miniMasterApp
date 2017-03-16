package com.masterdroup.minimasterapp.module.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.masterdroup.minimasterapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DeviceControlActivity extends AppCompatActivity {

    @Bind(R.id.textView2)
    TextView mTextView2;
    @Bind(R.id.tvMAC)
    TextView mTvMAC;

    /**
     * The GizWifiDevice device
     */
    private GizWifiDevice device;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);
        ButterKnife.bind(this);
        initDevice();
        initView();
    }

    private void initView() {

        mTvMAC = (TextView) findViewById(R.id.tvMAC);
        if (null != device) {

            mTvMAC.setText(device.getMacAddress().toString());

        }

    }

    private void initDevice() {
        Intent intent = getIntent();
        device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        Log.i("Apptest", device.getDid());
    }
}
