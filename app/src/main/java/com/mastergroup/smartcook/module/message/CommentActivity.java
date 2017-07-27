package com.mastergroup.smartcook.module.message;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.view.VerticalSwipeRefreshLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentActivity extends AppCompatActivity {

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
    @Bind(R.id.comment_lv)
    ListView commentLv;
    @Bind(R.id.id_swipe_ly)
    VerticalSwipeRefreshLayout idSwipeLy;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        mContext = this;
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
