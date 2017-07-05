package com.mastergroup.smartcook.module.settings;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mastergroup.smartcook.R;

import butterknife.Bind;
import butterknife.ButterKnife;

//意见反馈页面
public class FeedBackActivity extends Activity {

    @Bind(R.id.iv_return)
    ImageView ivReturn;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_more_button)
    TextView tvMoreButton;
    @Bind(R.id.phone_num_et)
    EditText phoneNumEt;
    @Bind(R.id.phone_ll)
    LinearLayout phoneLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText(R.string.feedback);
        
    }
}
