package com.mastergroup.smartcook.module.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.model.User;
import com.mastergroup.smartcook.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SettingsActivity extends AppCompatActivity implements Contract.View, View.OnClickListener{

    Contract.Presenter mSettingsPresenter;

    @Bind(R.id.set_info_rl)
    RelativeLayout userRl;

    @Bind(R.id.tv_title)
    TextView titleTv;

    @Bind(R.id.iv_return)
    ImageView backIV;

    @Bind(R.id.tv_more_button)
    TextView submit;

    @Bind(R.id.use_rl)
    RelativeLayout useRl;

    @Bind(R.id.about_rl)
    RelativeLayout aboutRl;

    @Bind(R.id.auto_clean_rl)
    RelativeLayout autoCleanRl;

    @Bind(R.id.logout_btn)
    Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        new SettingsPresenter(this);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initView() {
        titleTv.setText(getString(R.string.my_setting));
        submit.setVisibility(View.GONE);
    }

    private void initEvent() {

        userRl.setOnClickListener(this);
        backIV.setOnClickListener(this);
        useRl.setOnClickListener(this);
        aboutRl.setOnClickListener(this);
        autoCleanRl.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);

    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mSettingsPresenter = Utils.checkNotNull(presenter);
    }

    @Override
    public Context onGetContext() {

        return this;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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

            case R.id.iv_return:
                SettingsActivity.this.finish();
                break;

            case R.id.use_rl:
                startActivity(new Intent(SettingsActivity.this, AppRegisterAndAgreement.class));
                break;

            case R.id.about_rl:
                startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
                break;

            case R.id.logout_btn:
                mSettingsPresenter.logout();
                break;

            default:
                break;

        }
    }
}