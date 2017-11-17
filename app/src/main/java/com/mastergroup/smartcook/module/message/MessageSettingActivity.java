package com.mastergroup.smartcook.module.message;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.mastergroup.smartcook.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MessageSettingActivity extends AppCompatActivity {


    Context mContext;
    @Bind(R.id.iv_return)
    ImageView ivReturn;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_more_button)
    TextView tvMoreButton;
    @Bind(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @Bind(R.id.comment_sw)
    Switch commentSw;
    @Bind(R.id.comment_rl)
    RelativeLayout commentRl;
    @Bind(R.id.response_sw)
    Switch responseSw;
    @Bind(R.id.response_rl)
    RelativeLayout responseRl;
    @Bind(R.id.like_sw)
    Switch likeSw;
    @Bind(R.id.like_rl)
    RelativeLayout likeRl;
    @Bind(R.id.fans_sw)
    Switch fansSw;
    @Bind(R.id.fans_rl)
    RelativeLayout fansRl;
    @Bind(R.id.system_message_sw)
    Switch systemMessageSw;
    @Bind(R.id.system_message_rl)
    RelativeLayout systemMessageRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_setting);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.iv_return})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_return :
                finish();
                break;
        }
    }

}
