package com.masterdroup.minimasterapp.module.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.masterdroup.minimasterapp.Constant;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.model.User;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.masterdroup.minimasterapp.util.Utils;
import com.yuyh.library.imgsel.ImgSelActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingsActivity extends AppCompatActivity implements Contract.View, View.OnClickListener{

    @Bind(R.id.set_info_rl)
    RelativeLayout userRl;

    @Bind(R.id.tv_title)
    TextView titleTv;

    @Bind(R.id.iv_return)
    ImageView backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initView() {
        titleTv.setText("设置");
    }

    private void initEvent() {

        userRl.setOnClickListener(this);
        backIV.setOnClickListener(this);

    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {

    }

    @Override
    public Context onGetContext() {
        return null;
    }

    @Override
    public void putUserHeadUrl(String url) {

    }

    @Override
    public String getUserHeadUrl() {
        return null;
    }

    @Override
    public void putUserHeadServerUrl(String url) {

    }

    @Override
    public String getUserHeadServerUrl() {
        return null;
    }

    @Override
    public void setUserDate(User userDate) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.set_info_rl:
                startActivity(new Intent(SettingsActivity.this, UserInfoActivity.class));
                break;

            case R.id.back_iv:
                SettingsActivity.this.finish();
                break;

            default:

                break;

        }
    }
}