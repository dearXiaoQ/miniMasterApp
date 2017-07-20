package com.mastergroup.smartcook.module.welcomeModule;

/**
 * Created by xiaoQ on 2017/6/12.
 */

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

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.mastergroup.smartcook.App;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.module.CommonModule.GIZBaseActivity;
import com.mastergroup.smartcook.util.ToastUtils;
import com.mastergroup.smartcook.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 找回密码 */
public class RetrievePwdFragment extends Fragment implements Contract.RetrievePwdView {
    Contract.Presenter mPresenter;

    @Bind(R.id.phone_et)
    EditText phoneNumEt;

    @Bind(R.id.verification_et)
    EditText verification;

    @Bind(R.id.new_password_et)
    EditText newPwdEt;

    @Bind(R.id.again_new_password_et)
    EditText againNewPwdEt;

    @Bind(R.id.get_verification_tv)
    TextView getVerTv;

    @Bind(R.id.btn_registered)
    Button submitBtn;

    private TimeCount timeCount;

    private static int COUNT_TIME = 60000;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new WelcomePresenter(this);
        //每次启动activity都要注册一次sdk监听器，保证sdk状态能正确回调
        GizWifiSDK.sharedInstance().setListener(mListener);
        timeCount = new TimeCount(COUNT_TIME, 1000);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_welcome_view4, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private GizWifiSDKListener mListener = new GizWifiSDKListener() {
        @Override
        public void didChangeUserPassword(GizWifiErrorCode result) {
            if(result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                onRetrievePwdViewSuccess();
            } else {
                ToastUtils.showCustomToast(App.mContext, ToastUtils.TOAST_CENTER,  ((GIZBaseActivity)getActivity()).toastError(result));
            }
        }
    };

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mPresenter = Utils.checkNotNull(presenter);
    }

    @Override
    public Context onGetContext() {
        return getActivity();
    }

    @Override
    public void onRetrievePwdViewSuccess() {

        //   mPresenter.Retrieve(phoneNumEt.getText().toString().trim(), newPwdEt.getText().toString().trim());
        ToastUtils.showCustomToast(App.mContext, ToastUtils.TOAST_BOTTOM, App.mContext.getString(R.string.retrieve_pwd_success));
        //返回到欢迎页面
        ((WelcomeActivity) getActivity()).mPresenter.showMainView();
    }

    private void onGizChangePwdSuuccess() {
        ToastUtils.showCustomToast(App.mContext, ToastUtils.TOAST_BOTTOM, App.mContext.getString(R.string.retrieve_pwd_success));
    }

    @Override
    public void onRetrievePwdFailure(String info) {

        ToastUtils.showCustomToast(App.mContext, ToastUtils.TOAST_BOTTOM, info);
    }

    @Override
    public void onGetVerSuccess() {
        timeCount.start();
        ToastUtils.showCustomToast(App.mContext, ToastUtils.TOAST_BOTTOM, App.mContext.getString(R.string.get_verification_success));
    }

    @Override
    public void onGetVerFailure(String info) {

        ToastUtils.showCustomToast(App.mContext, ToastUtils.TOAST_BOTTOM, info);

    }


    @OnClick({R.id.btn_registered, R.id.get_verification_tv})
    public void OnClick(View view) {

        switch (view.getId()) {

            case R.id.btn_registered:
                mPresenter.CheckRetrievePwdPara(phoneNumEt.getText().toString().trim(),
                        verification.getText().toString().trim(),
                        newPwdEt.getText().toString().trim(),
                        againNewPwdEt.getText().toString().trim()
                );
                break;

            case R.id.get_verification_tv:
                mPresenter.getVerification(phoneNumEt.getText().toString());
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
            if( getVerTv != null ) {
                getVerTv.setClickable(false);
                getVerTv.setText(millisUntilFinished / 1000 + "s");
            }
        }

        @Override
        public void onFinish() {
            if(getVerTv != null ) {
                getVerTv.setClickable(true);
                getVerTv.setText("重新获取验证码");
            }
        }
    }

}
