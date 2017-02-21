package com.masterdroup.minimasterapp.module.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.masterdroup.minimasterapp.Constant;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.model.User;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends Activity implements Contract.View {

    @Bind(R.id.iv_return)
    ImageView mIvReturn;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_more_button)
    TextView mTvMoreButton;

    Contract.Presenter mPresenter;
    @Bind(R.id.et_name)
    EditText mEtName;
    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.iv_user_head)
    ImageView mIvUserHead;
    public static final int REQUEST_CODE = 0;

    //保存用户选择图片的本地路径
    private static String user_head_local_url = "";

    //保存服务器里用户信息的头像图片路径
    private static String user_head_server_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        new SettingsPresenter(this);
        mTvTitle.setText(getString(R.string.settings_user_info));

        //获取用户信息
        mPresenter.getUserDate();
    }

    @OnClick({R.id.iv_return, R.id.tv_more_button, R.id.iv_user_head})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                finish();
                break;
            case R.id.tv_more_button:
                //保存修改信息然后提交
                User user = new User();
                User.UserBean userBean = user.new UserBean();
                userBean.setPhoneNum(mEtPhone.getText().toString());
                user.setUser(userBean);
                mPresenter.upDate(user, getUserHeadUrl());
                break;
            case R.id.iv_user_head:
                //点击头像 选择图片
                mPresenter.openImageSelector();
                break;
        }
    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Context onGetContext() {
        return this;
    }

    @Override
    public void putUserHeadUrl(String url) {
        user_head_local_url = url;
    }

    @Override
    public String getUserHeadUrl() {
        return user_head_local_url;
    }

    @Override
    public void putUserHeadServerUrl(String url) {
        user_head_server_url = url;
    }

    @Override
    public String getUserHeadServerUrl() {
        return user_head_server_url;
    }

    @Override
    public void setUserDate(User user) {
        User.UserBean userBean = user.getUser();
        mEtPhone.setText(userBean.getPhoneNum());
        ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + userBean.getHeadUrl(), mIvUserHead, this, true);
        putUserHeadServerUrl(userBean.getHeadUrl());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            for (String path : pathList) {
                ImageLoader.getInstance().displayGlideImage(path, mIvUserHead, this, true);
                putUserHeadUrl(pathList.get(0));
            }
        }
    }
}
