package com.mastergroup.smartcook.module.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mastergroup.smartcook.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @Bind(R.id.iv_return)
    ImageView backIv;

    @Bind(R.id.tv_title)
    TextView titleTv;

    @Bind(R.id.tv_more_button)
    TextView submitTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.this.finish();
            }
        });

        titleTv.setText(getString(R.string.about));

        submitTv.setVisibility(View.GONE);

    }
}
