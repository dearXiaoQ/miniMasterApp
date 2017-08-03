package com.mastergroup.smartcook.module.device;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.view.GifView;

public class AddDevSetDevStateActivity extends WiFiConfigModuleBaseActivity implements View.OnClickListener {

    /** The cb Select */
    private CheckBox cbSelect;

    /** The Tv select */
    private TextView tvSelect;

    /** The Next Btn */
    private Button nextBtn;

    /** The Back Iv */
    private ImageView backIv;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dev_set_dev_satae_actiivty);
        mContext = this;
        initView();
        initEvent();
    }

    private void initEvent() {
        nextBtn.setOnClickListener(this);
        tvSelect.setOnClickListener(this);
        nextBtn.setEnabled(false);

   /*     cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                   // nextBtn.setEnabled(true);
                    Intent intent = new Intent(mContext, AddDevConfigCountdownActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                   // nextBtn.setEnabled(false);
                }
            }
        });*/

        cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Intent intent = new Intent(mContext, AddDevConfigCountdownActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void initView() {
        cbSelect = (CheckBox) findViewById(R.id.cbSelect);
        tvSelect = (TextView) findViewById(R.id.tvSelect);
        nextBtn  = (Button) findViewById(R.id.next_btn);
//        backIv   = (ImageView) findViewById(R.id.back_iv);

        nextBtn.setOnClickListener(this);
 //       backIv.setOnClickListener(this);

        /** 加载Gif */
        GifView gif = (GifView) findViewById(R.id.softreset);
        gif.setMovieResource(R.drawable.airlink);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.back_iv:
                AddDevSetDevStateActivity.this.finish();
                break;

            case R.id.next_btn:
                Intent intent = new Intent(mContext, AddDevConfigCountdownActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.tvSelect:
                if (cbSelect.isChecked()) {
                    cbSelect.setChecked(false);
                } else {
                    cbSelect.setChecked(true);
                }
                break;

            default:
                break;

        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(this, AddDevSettingWifiActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        this.finish();
        return true;
    }
}
