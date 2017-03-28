package com.masterdroup.minimasterapp.module.settings;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.masterdroup.minimasterapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreActivity extends Activity {

    @Bind(R.id.wv)
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        ButterKnife.bind(this);
        initView();
    }



    private void initView(){
            wv.loadUrl("https://momscook.tmall.com/");
    }


    @OnClick(R.id.iv_return)
    public void onViewClicked() {
        finish();
    }
}
