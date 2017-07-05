package com.mastergroup.smartcook.module.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mastergroup.smartcook.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AppRegisterAndAgreement extends AppCompatActivity {

    @Bind(R.id.iv_return)
    ImageView backIv;

    @Bind(R.id.tv_title)
    TextView titleTv;

    @Bind(R.id.tv_more_button)
    TextView submitTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_register_and_agreement);
        ButterKnife.bind(this);
        viewInit();

    }

    private void viewInit() {

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppRegisterAndAgreement.this.finish();
            }
        });

        titleTv.setText(getString(R.string.use_agreement));

        submitTv.setVisibility(View.GONE);
    }
}
