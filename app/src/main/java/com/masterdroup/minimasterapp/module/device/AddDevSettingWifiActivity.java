package com.masterdroup.minimasterapp.module.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.masterdroup.minimasterapp.R;

/**
 * Created by xiaoQ on 2017/5/22.
 */

public class AddDevSettingWifiActivity extends Activity implements View.OnClickListener {

    private ImageView backIv, moreWifiIv;

    private Button nextBtn, visibleBtn;

    private EditText wifiNameEt, pwdEt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_wifi_fragment);

        initView();

    }

    private void initView() {
        nextBtn = (Button) findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.back_iv:

                break;


            case R.id.next_btn:
                startActivity(new Intent(AddDevSettingWifiActivity.this, AddDevSetDevSataeActiivty.class));
                break;


            default:
                break;
        }
    }
}
