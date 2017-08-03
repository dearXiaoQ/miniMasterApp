package com.mastergroup.smartcook.module.device;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.mastergroup.smartcook.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddDevSelectTypeActivity extends AppCompatActivity {

    @Bind(R.id.iv_return)
    ImageView ivReturn;
    @Bind(R.id.message_iv)
    ImageView messageIv;
    @Bind(R.id.head_rl)
    RelativeLayout headRl;
    @Bind(R.id.comment_iv)
    ImageView commentIv;
    @Bind(R.id.ih_rood_rl)
    RelativeLayout ihRoodRl;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dev_select_type);
        ButterKnife.bind(this);
        mContext = this;
    }



    @OnClick({R.id.iv_return, R.id.ih_rood_rl})
    public void OnClick(View view) {
        switch(view.getId()) {
            case R.id.iv_return:
                this.finish();
                break;

            case R.id.ih_rood_rl:
                startActivity(new Intent(mContext, AddDevSettingWifiActivity.class));
                break;
        }
    }

}
