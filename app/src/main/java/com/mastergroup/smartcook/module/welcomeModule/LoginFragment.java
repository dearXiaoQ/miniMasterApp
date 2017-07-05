package com.mastergroup.smartcook.module.welcomeModule;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.ActivityUtils;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.mastergroup.smartcook.App;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.module.CommonModule.GIZBaseActivity;
import com.mastergroup.smartcook.module.home.HomeActivity;
import com.mastergroup.smartcook.util.Utils;
import com.yuyh.library.imgsel.utils.LogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 11473 on 2016/11/29.
 */

public class LoginFragment extends Fragment implements Contract.LoginView {
    Contract.Presenter mPresenter;
    @Bind(R.id.btn_registered)
    Button mBtnRegistered;
    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.et_pwd)
    EditText mEtPwd;
    @Bind(R.id.tv_pwd_forget)
    TextView forgetPwdTv;


    private String smart_token;

    String headUrl;
    String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new WelcomePresenter(this);
        //每次启动activity都要注册一次sdk监听器，保证sdk状态能正确回调
        GizWifiSDK.sharedInstance().setListener(mListener);
    }

    private GizWifiSDKListener mListener = new GizWifiSDKListener() {
        @Override
        public void didUserLogin(GizWifiErrorCode result, String uid, String giz_token) {
            LogUtils.d("GizWifiSDK", "机智云登录==>   uid = " + uid + "     result = " + result.toString() + "      token = " + giz_token);
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                LogUtils.d("GizWifiSDK", "机智云登录==>成功");
              //  mPresenter.login(mEtPhone.getText().toString(), mEtPwd.getText().toString(), uid, giz_token);
                onLoginSuccess(mEtPhone.getText().toString().trim(), smart_token, uid, giz_token);

            } else {
                LogUtils.e("GizWifiSDK", "机智云登录==>失败");
                // 登录失败
                // ToastUtils.showShortToast("机智云登录 失败");
                com.mastergroup.smartcook.util.ToastUtils.showCustomToast(getActivity(), com.mastergroup.smartcook.util.ToastUtils.TOAST_CENTER, ((GIZBaseActivity)getActivity()).toastError(result));
            }


        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_welcome_view2, container, false);
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

    /**
     * 登录成功
     */
    @Override
    public void onLoginSuccess(String phone, String token, String giz_uid, String giz_token) {

        App.spUtils.putString(App.mContext.getString(R.string.key_token), token);

        App.spUtils.putString(App.mContext.getString(R.string.name), phone);

        App.spUtils.putString(App.mContext.getString(R.string.username), username);

        App.spUtils.putString(App.mContext.getString(R.string.giz_uid), giz_uid);

        App.spUtils.putString(App.mContext.getString(R.string.giz_token), giz_token);

        App.spUtils.putString(App.mContext.getString(R.string.user_headurl), headUrl);

        startActivity(new Intent(this.getActivity(), HomeActivity.class));
        this.getActivity().finish();

    }


    /**
     * 登录失败
     */
    @Override
    public void onLoginFailure(String info) {
        Toast.makeText(ActivityUtils.getTopActivity(), "登录失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetUserInfoSuccess() {
        startActivity(new Intent(this.getActivity(), HomeActivity.class));
        this.getActivity().finish();
    }

    /** 登录机智云 */
    @Override
    public void onLoginSmartNetwork(String userName, String phone, String pwd, String smart_token, String headUrl) {
        this.smart_token = smart_token;
        this.username = userName;
        this.headUrl = headUrl;
        GizWifiSDK.sharedInstance().userLogin(phone, pwd);
    }



    @Override
    public void onRetrieveView() {
        ((WelcomeActivity) getActivity()).onRetrieveView();
    }


    @OnClick({R.id.btn_registered, R.id.tv_pwd_forget})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_registered:
                mPresenter.gizLogin(mEtPhone.getText().toString().trim(), mEtPwd.getText().toString().trim());
                break;

            case R.id.tv_pwd_forget:
                mPresenter.showRetrieveView();
                break;

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
