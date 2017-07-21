package com.mastergroup.smartcook.module.message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mastergroup.smartcook.R;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }
}



//评论： 当有其他用户评论本人创建的菜谱，就会收到通知
//回复： 有人@你
//点赞   当有其他用户点赞本人创建的菜谱，就会收到通知
//新的粉丝： 当有其他用户点赞本人创建的菜谱，就会收到通知
//系统消息： 系统推送的小心，例如新版本提示。
