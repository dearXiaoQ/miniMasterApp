package com.mastergroup.smartcook.module.device;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.view.GifView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddDevSetDevStateActivity extends WiFiConfigModuleBaseActivity  {


    Context mContext;
    @Bind(R.id.cancel_tv)
    TextView cancelTv;
    @Bind(R.id.next_btn)
    Button nextBtn;
    @Bind(R.id.cbSelect)
    CheckBox cbSelect;
    @Bind(R.id.tvSelect)
    TextView tvSelect;
    @Bind(R.id.textView1)
    TextView textView1;
    @Bind(R.id.softreset)
    GifView softreset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dev_set_dev_satae_actiivty);
        ButterKnife.bind(this);
        mContext = this;
        initData();
        initEvent();
    }

    private void initEvent() {
        nextBtn.setEnabled(false);


        cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(mContext, AddDevConfigCountdownActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void initData() {

        /** 加载Gif */
        GifView gif = (GifView) findViewById(R.id.softreset);
        gif.setMovieResource(R.drawable.airlink);
    }



    @OnClick({R.id.cancel_tv})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_tv:

                this.finish();
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
