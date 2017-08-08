package com.mastergroup.smartcook.module.device;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.view.CircleView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CookingActivity extends AppCompatActivity {

    @Bind(R.id.wave_view)
    CircleView mCircleView;
    @Bind(R.id.power)
    TextView power;
    @Bind(R.id.seekBar)
    SeekBar seekBar;

    private int max = 1024;
    private int min = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking2);
        ButterKnife.bind(this);

        init();

    }


    private void init() {
        /** 初始化波浪进度  */
        //  设置多高，float, 0.1-1F
        mCircleView.setmWaterLevel(0.1F);
        //  开始执行
        mCircleView.startWave();


        /** 跟随水波纹动 */
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCircleView.setmWaterLevel((float) progress / 100);
                // 创建一个消息
                Message message = new Message();
                Bundle bundle = new Bundle();
                // put一个int值
                bundle.putInt("progress", progress);
                // 装载
                message.setData(bundle);
                // 发送消息
                handler.sendMessage(message);
                // 创建表示
                message.what = 1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }



    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                int num = msg.getData().getInt("progress");
                Log.i("num", num + "");
                power.setText((float) num / 100 * max + "M/" + max + "M");
            }
        }
    };

    @Override
    protected void onDestroy() {
        mCircleView.stopWave();
        mCircleView = null;
        super.onDestroy();
    }
}
