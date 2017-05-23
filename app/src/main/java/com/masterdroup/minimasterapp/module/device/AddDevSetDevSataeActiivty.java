package com.masterdroup.minimasterapp.module.device;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.view.GifView;

public class AddDevSetDevSataeActiivty extends Activity implements View.OnClickListener {

    private CheckBox cbSelect;

    private TextView tvSelect;

    private Button nextBtn;

    private ImageView backIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dev_set_dev_satae_actiivty);

        initView();
    }

    private void initView() {
        cbSelect = (CheckBox) findViewById(R.id.cbSelect);
        tvSelect = (TextView) findViewById(R.id.tvSelect);
        nextBtn  = (Button) findViewById(R.id.next_btn);
        backIv   = (ImageView) findViewById(R.id.back_iv);

        nextBtn.setOnClickListener(this);
        backIv.setOnClickListener(this);

        /** 加载Gif */
        GifView gif = (GifView) findViewById(R.id.softreset);
        gif.setMovieResource(R.drawable.airlink);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.back_iv:

                break;

            case R.id.next_btn:

                break;

            default:
                break;

        }

    }
}
