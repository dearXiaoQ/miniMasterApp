package com.mastergroup.smartcook.module.welcomeModule;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.mastergroup.smartcook.App;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.module.CommonModule.GIZBaseActivity;
import com.mastergroup.smartcook.util.ToastUtils;
import com.mastergroup.smartcook.util.Utils;
import com.yuyh.library.imgsel.utils.LogUtils;



import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 11473 on 2016/11/29.
 */

public class RegisteredFragment extends Fragment implements Contract.RegisteredView {
    Contract.Presenter mPresenter;

    @Bind(R.id.nickname_et)
    EditText nicknameEt;

    @Bind(R.id.phone_num_et)
    EditText phoneNumEt;

    @Bind(R.id.verification_et)
    EditText verificationEt;

    @Bind(R.id.password_et)
    EditText pwdEt;

    @Bind(R.id.again_password_et)
    EditText againPwdEt;

    @Bind(R.id.get_verification_tv)
    TextView getVerTv;

    @Bind(R.id.btn_registered)
    Button mBtnRegistered;


    private TimeCount timeCount;

    private static int COUNT_TIME = 60000;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new WelcomePresenter(this);
        //每次启动activity都要注册一次sdk监听器，保证sdk状态能正确回调
        GizWifiSDK.sharedInstance().setListener(mListener);
        timeCount = new TimeCount(COUNT_TIME, 1000);
    }


    /** 13533566275  123456*/
    private GizWifiSDKListener mListener = new GizWifiSDKListener() {
        @Override
        public void didRegisterUser(GizWifiErrorCode result, String uid, String token) {

            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                // 注册成功
                LogUtils.d("GizWifiSDK", "机智云注册 成功======》" + "uid:" + uid + "     result:" + result.toString() + "      token" + token);
                ToastUtils.showCustomToast(getActivity(), ToastUtils.TOAST_CENTER, getActivity().getString(R.string.registered_s));
                mPresenter.registered(nicknameEt.getText().toString(), pwdEt.getText().toString(), againPwdEt.getText().toString(), phoneNumEt.getText().toString(), uid);
            } else {
                // 注册失败
                LogUtils.d("GizWifiSDK", "机智云注册 失败======》" + "result:" + result.toString());
                ToastUtils.showCustomToast(getActivity(), ToastUtils.TOAST_CENTER, ((GIZBaseActivity)getActivity()).toastError(result));
            }
        }

        @Override
        public void didRequestSendPhoneSMSCode(GizWifiErrorCode result, String token) {
            // 实现逻辑
            if(result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                // 请求成功
                timeCount.start();
                LogUtils.d("机智云验证码发送======》成功", "     result：" + result.toString() + "      token：" + token);
            } else {
                //请求失败
                LogUtils.d("机智云验证码发送======》失败", "     result:" + result.toString() + "      token" + token);
                ToastUtils.showCustomToast(getActivity(), ToastUtils.TOAST_CENTER, ((GIZBaseActivity)getActivity()).toastError(result));
                //Toast
            }
        }
    };


    /** */
    //jun
    //123
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_welcome_view3, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mPresenter = Utils.checkNotNull(presenter);
    }

    @Override
    public Context onGetContext() {
        return getActivity();
    }



    @Override
    public void onRegisteredSuccess() {
        Toast.makeText(App.mContext, getString(R.string.registered_s), Toast.LENGTH_SHORT).show();
        //隐藏键盘
        KeyboardUtils.hideSoftInput(getActivity());
        //返回到欢迎页面
        ((WelcomeActivity) getActivity()).mPresenter.showMainView();


    }

    @Override
    public void onRegisteredFailure(@Nullable String info) {
        if(info.equals("user existed")) {
            info = "用户已存在！";
        }
        ToastUtils.showCustomToast(App.mContext, ToastUtils.TOAST_CENTER, info);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_registered, R.id.get_verification_tv})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_registered:

                mPresenter.gizRegistered(nicknameEt.getText().toString().trim(),
                        phoneNumEt.getText().toString().trim(),
                        verificationEt.getText().toString().trim(),
                        pwdEt.getText().toString().trim(),
                        againPwdEt.getText().toString().trim()

                );
              //  mPresenter.registered(nicknameEt.getText().toString(), pwdEt.getText().toString(), againPwdEt.getText().toString(), phoneNumEt.getText().toString(), "123456s");

                break;


            case R.id.get_verification_tv:
                mPresenter.sendPhoneSMSCode(phoneNumEt.getText().toString());
                break;

        }
    }

    /** 验证码60s倒计时 */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }


        @Override
        public void onTick(long millisUntilFinished) {
            if(getVerTv != null) {
                getVerTv.setClickable(false);
                getVerTv.setText(millisUntilFinished / 1000 + "s");
            }
        }

        @Override
        public void onFinish() {
            if(getVerTv != null) {
                getVerTv.setClickable(true);
                getVerTv.setText("重新获取验证码");
            }
        }
    }


}
