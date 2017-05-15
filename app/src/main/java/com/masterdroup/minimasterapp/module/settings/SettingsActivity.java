package com.masterdroup.minimasterapp.module.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.masterdroup.minimasterapp.Constant;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.model.User;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.masterdroup.minimasterapp.util.Utils;
import com.yuyh.library.imgsel.ImgSelActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends Activity implements Contract.View {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        //init();
    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {

    }

    @Override
    public Context onGetContext() {
        return null;
    }

    @Override
    public void putUserHeadUrl(String url) {

    }

    @Override
    public String getUserHeadUrl() {
        return null;
    }

    @Override
    public void putUserHeadServerUrl(String url) {

    }

    @Override
    public String getUserHeadServerUrl() {
        return null;
    }

    @Override
    public void setUserDate(User userDate) {

    }
}