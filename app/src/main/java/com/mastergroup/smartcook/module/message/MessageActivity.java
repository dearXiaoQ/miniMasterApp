package com.mastergroup.smartcook.module.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.model.Like;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends AppCompatActivity {

    @Bind(R.id.iv_return)
    ImageView ivReturn;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_more_button)
    TextView tvMoreButton;
    @Bind(R.id.message_setting_iv)
    ImageView messageSettingIV;
    @Bind(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @Bind(R.id.comment_iv)
    ImageView commentIv;
    @Bind(R.id.comment_rl)
    RelativeLayout commentRl;
    @Bind(R.id.response_iv)
    ImageView responseIv;
    @Bind(R.id.response_rl)
    RelativeLayout responseRl;
    @Bind(R.id.like_iv)
    ImageView likeIv;
    @Bind(R.id.like_rl)
    RelativeLayout likeRl;
    @Bind(R.id.fans_iv)
    ImageView fansIv;
    @Bind(R.id.fans_rl)
    RelativeLayout fansRl;
    @Bind(R.id.system_message_iv)
    ImageView systemMessageIv;
    @Bind(R.id.system_message_rl)
    RelativeLayout systemMessageRl;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick({R.id.iv_return, R.id.message_setting_iv, R.id.comment_rl, R.id.response_rl, R.id.like_rl, R.id.fans_rl, R.id.system_message_rl})
    public void Onclick(View view) {
        switch (view.getId()) {

            case R.id.iv_return:
                   MessageActivity.this.finish();
                break;

            case R.id.message_setting_iv:
                startActivity(new Intent(mContext, MessageSettingActivity.class));
                break;

            case R.id.comment_rl:
                startActivity(new Intent(mContext, CommentActivity.class));
                break;
            case R.id.response_rl:
                startActivity(new Intent(mContext, ResponseActivity.class));
                break;

            case R.id.like_rl:
                startActivity(new Intent(mContext, LikeActivity.class));
                break;

            case R.id.fans_rl:
                startActivity(new Intent(mContext, FansActivity.class));
                break;

            case R.id.system_message_rl:
                startActivity(new Intent(mContext, SystemMessageActivity.class));
                break;

        }
    }

}


//评论： 当有其他用户评论本人创建的菜谱，就会收到通知
//回复： 有人@你
//点赞   当有其他用户点赞本人创建的菜谱，就会收到通知
//新的粉丝： 当有其他用户点赞本人创建的菜谱，就会收到通知
//系统消息： 系统推送的小心，例如新版本提示。
