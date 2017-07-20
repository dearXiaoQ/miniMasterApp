package com.mastergroup.smartcook.module.settings;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mastergroup.smartcook.App;
import com.mastergroup.smartcook.Constant;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.model.User;
import com.mastergroup.smartcook.util.ImageLoader;
import com.mastergroup.smartcook.util.StringFormatCheckUtils;
import com.mastergroup.smartcook.util.ToastUtils;
import com.mastergroup.smartcook.util.Utils;
import com.yuyh.library.imgsel.ImgSelActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/** 用户个人信息修改 */
public class UserInfoActivity extends Activity implements Contract.View {

    @Bind(R.id.iv_return)
    ImageView mIvReturn;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_more_button)
    TextView mTvMoreButton;

    Contract.Presenter mPresenter;
    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.iv_user_head)
    ImageView mIvUserHead;
    public static final int REQUEST_CODE = 0;

    //保存用户选择图片的本地路径
    private static String user_head_local_url = "";

    //保存服务器里用户信息的头像图片路径
    private static String user_head_server_url = "";
    @Bind(R.id.et_age)
    EditText mEtAge;
    @Bind(R.id.rb_male)
    RadioButton mRbMale;
    @Bind(R.id.rb_female)
    RadioButton mRbFemale;
    @Bind(R.id.et_address)
    EditText mEtAddress;
    @Bind(R.id.signature_et)
    EditText signatureEt;
    @Bind(R.id.radioGroup)
    RadioGroup sexRG;

    @Bind(R.id.old_et)
    EditText oldEt;

    private static final int MALE   = 1;
    private static final int FEMALE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
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
                //userBean.setPhoneNum(mEtPhone.getText().toString());
                String signatureStr = signatureEt.getText().toString().trim();
                if(signatureStr.length() != 0)
                    userBean.setSignature(signatureStr);

                String addressStr = mEtAddress.getText().toString().trim();
                if(addressStr.length() != 0)
                    userBean.setAddress(mEtAddress.getText().toString());

                String ageStr = oldEt.getText().toString().trim();
                if( StringFormatCheckUtils.isAgeLegal(ageStr) )
                    userBean.setAge(Integer.valueOf(ageStr));
                else {
                    ToastUtils.showCustomToast(App.mContext, ToastUtils.TOAST_CENTER, App.mContext.getString(R.string.input_age_not_legal));
                    return;
                }

                if (mRbMale.isChecked())
                    userBean.setSex(MALE);
                if (mRbFemale.isChecked())
                    userBean.setSex(FEMALE);

                user.setUser(userBean);


                mPresenter.upDate(user, getUserHeadUrl());
                break;
            case R.id.iv_user_head:
                //点击头像 选择图片
                Utils.openImageSelector(this, REQUEST_CODE);
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
        mEtAge.setText(userBean.getAge() == 0 ? null : String.valueOf(userBean.getAge()));
        int rbId;
        if (userBean.getSex() == MALE)
            rbId = R.id.radio_1;
        else
            rbId = R.id.radio_2;

        sexRG.check(rbId);

        oldEt.setText(userBean.getAge() + "");

        mRbFemale.setChecked(true);
        mEtAddress.setText(userBean.getAddress());
        signatureEt.setText(userBean.getSingnature());

        if( !(userBean.getHeadUrl().equals("")) ) {
            ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + userBean.getHeadUrl(), mIvUserHead, this, true);
        }
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