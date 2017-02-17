package com.masterdroup.minimasterapp.module.welcomeModule;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizUserAccountType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.masterdroup.minimasterapp.App;
import com.masterdroup.minimasterapp.Constant;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 11473 on 2016/11/29.
 */

public class RegisteredFragment extends Fragment implements Contract.RegisteredView {
    Contract.Presenter mPresenter;
    @Bind(R.id.et_name)
    EditText mEtName;
    @Bind(R.id.et_pwd)
    EditText mEtPwd;
    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.btn_registered)
    Button mBtnRegistered;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new WelcomePresenter(this);
        //每次启动activity都要注册一次sdk监听器，保证sdk状态能正确回调
        GizWifiSDK.sharedInstance().setListener(mListener);
    }

    private GizWifiSDKListener mListener = new GizWifiSDKListener() {
        @Override
        public void didRegisterUser(GizWifiErrorCode result, String uid, String token) {
            // 实现逻辑
            LogUtils.d("机智云注册======》", "uid:" + uid + "     result:" + result.toString() + "      token" + token);
            mPresenter.registered(mEtName.getText().toString(), mEtPwd.getText().toString(), mEtPhone.getText().toString(), uid);

        }

        @Override
        public void didRequestSendPhoneSMSCode(GizWifiErrorCode result, String token) {
            super.didRequestSendPhoneSMSCode(result, token);
            // 实现逻辑
            LogUtils.d("机智云验证码发送======》", "     result:" + result.toString() + "      token" + token);
        }
    };


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
        Toast.makeText(App.mContext, info, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_registered)
    public void onClick() {
        mPresenter.gizRegistered(mEtName.getText().toString(), mEtPwd.getText().toString());
    }
}
