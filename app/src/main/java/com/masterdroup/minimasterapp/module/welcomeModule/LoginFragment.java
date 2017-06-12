package com.masterdroup.minimasterapp.module.welcomeModule;

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
import android.widget.Toast;

import com.blankj.utilcode.utils.ActivityUtils;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.masterdroup.minimasterapp.App;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.module.CommonModule.GIZBaseActivity;
import com.masterdroup.minimasterapp.module.home.HomeActivity;
import com.masterdroup.minimasterapp.util.Utils;
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

    //是否使用电话号码登录
    public static boolean PHONE_NUMBER_LOGIN = false;

    private String smart_token;

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
                // 登录成功
                mPresenter.login(mEtPhone.getText().toString(), mEtPwd.getText().toString(), uid, giz_token);
                if(PHONE_NUMBER_LOGIN) {    //使用号码登录
                    mPresenter.login(mEtPhone.getText().toString().trim(), mEtPwd.getText().toString().trim(), uid, giz_token);
                } else {     //使用用户名登录   //直接跳转
                    onLoginSuccess(mEtPhone.getText().toString().trim(), smart_token, uid, giz_token);
                }
            } else {
                LogUtils.e("GizWifiSDK", "机智云登录==>失败");
                // 登录失败
                // ToastUtils.showShortToast("机智云登录 失败");
                com.masterdroup.minimasterapp.util.ToastUtils.showCustomToast(getActivity(), com.masterdroup.minimasterapp.util.ToastUtils.TOAST_CENTER, ((GIZBaseActivity)getActivity()).toastError(result));
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
    public void onLoginSuccess(String name, String token, String giz_uid, String giz_token) {

        App.spUtils.putString(App.mContext.getString(R.string.key_token), token);

        App.spUtils.putString(App.mContext.getString(R.string.name), name);

        App.spUtils.putString(App.mContext.getString(R.string.giz_uid), giz_uid);

        App.spUtils.putString(App.mContext.getString(R.string.giz_token), giz_token);

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

    /** 登录机智云 */
    @Override
    public void onLoginSmartNetwork(String phone, String pwd, String smart_token) {
        this.smart_token = smart_token;
        GizWifiSDK.sharedInstance().userLogin(phone, pwd);
    }


    @OnClick(R.id.btn_registered)
    public void onClick() {
        mPresenter.gizLogin(mEtPhone.getText().toString().trim(), mEtPwd.getText().toString().trim());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
