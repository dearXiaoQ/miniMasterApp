package com.mastergroup.smartcook.module.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mastergroup.smartcook.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LikeActivity extends AppCompatActivity {

    @Bind(R.id.iv_return)
    ImageView ivReturn;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_more_button)
    TextView tvMoreButton;
    @Bind(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @Bind(R.id.no_comment_iv)
    ImageView noCommentIv;
    @Bind(R.id.no_comment_tv)
    TextView noCommentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_return})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                this.finish();
                break;
        }

    }

}
