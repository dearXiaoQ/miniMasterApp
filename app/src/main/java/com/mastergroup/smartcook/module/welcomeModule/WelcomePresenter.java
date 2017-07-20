package com.mastergroup.smartcook.module.welcomeModule;


import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizUserAccountType;
import com.google.gson.Gson;
import com.mastergroup.smartcook.App;
import com.mastergroup.smartcook.Constant;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.api.Network;
import com.mastergroup.smartcook.model.Base;
import com.mastergroup.smartcook.model.Null;
import com.mastergroup.smartcook.model.PhoneAndToken;
import com.mastergroup.smartcook.model.Token;
import com.mastergroup.smartcook.model.User;
import com.mastergroup.smartcook.module.progress.ProgressSubscriber;
import com.mastergroup.smartcook.util.DebugUtils;
import com.mastergroup.smartcook.util.JxUtils;
import com.mastergroup.smartcook.util.StringFormatCheckUtils;
import com.mastergroup.smartcook.util.ToastUtils;
import com.mastergroup.smartcook.util.Utils;
import com.yuyh.library.imgsel.utils.LogUtils;

import java.util.HashMap;

import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by 11473 on 2016/11/29.
 */

public class WelcomePresenter implements Contract.Presenter {


    Contract.MainView mainView;
    Contract.LoginView loginView;
    Contract.RegisteredView registeredView;
    Contract.RetrievePwdView retrievePwdView;


    public WelcomePresenter(Contract.MainView mView) {
        this.mainView = mView;
        mainView = Utils.checkNotNull(mView, "mView cannot be null!");
        mainView.setPresenter(this);
    }

    public WelcomePresenter(Contract.LoginView mView) {
        this.loginView = mView;
        loginView = Utils.checkNotNull(mView, "mView cannot be null!");
        loginView.setPresenter(this);
    }

    public WelcomePresenter(Contract.RegisteredView mView) {
        this.registeredView = mView;
        registeredView = Utils.checkNotNull(mView, "mView cannot be null!");
        registeredView.setPresenter(this);
    }

    public WelcomePresenter(Contract.RetrievePwdView mView) {
        this.retrievePwdView = mView;
        retrievePwdView = Utils.checkNotNull(mView, "mView cannot be null!");
        retrievePwdView.setPresenter(this);
    }


    @Override
    public void start() {
    }

    @Override
    public void showLoginView() {
        mainView.onShowLoginView();
    }

    @Override
    public void showRegisteredView() {
        mainView.onRegisteredView();
    }

    @Override
    public void showMainView() {
        mainView.onShowMainView();
    }

    @Override
    public void showRetrieveView() { loginView.onRetrieveView();}

    @Override
    public boolean isLogin() {

        //判读token值是否存在
        try {

            return App.spUtils.contains(App.mContext.getString(R.string.key_token));

        } catch (Exception e) {
            return false;
        }


    }

    /** 登陆机智云成功后，将信息抛给我们的服务器 */
    @Override
    public void login(final String name, String pwd, final String giz_uid, final String giz_token) {
        DebugUtils.d("WelcomePresenter", "login()");

        if (name.isEmpty() || pwd.isEmpty()) {
            loginView.onLoginFailure(App.mContext.getString(R.string.err_login_1));
        } else {

            User user = new User();
            User.UserBean userBean = user.new UserBean();
            userBean.setName(name);
            userBean.setPassword(pwd);
            user.setUser(userBean);

            Observable observable = Network.getMainApi().login(user);

            Subscriber<Base<Token>> s = new ProgressSubscriber<>(new ProgressSubscriber.SubscriberOnNextListener<Base<Token>>() {
                @Override
                public void onNext(Base<Token> o) {
                    if (o.getErrorCode() == 0)
                        loginView.onLoginSuccess(name,o.getRes().getToken(), giz_uid, giz_token);
                    else {
                        loginView.onLoginFailure(o.getMessage());
                    }
                }

            }, loginView.onGetContext());


            JxUtils.toSubscribe(observable, s);
        }


    }


    @Override
    public void gizLogin(String nameOrPhone, final String pwd) {

        User user =  new User();
        User.UserBean userBean = user.new UserBean();

        if(pwd.length() == 0) {
            Toast.makeText(App.mContext, App.mContext.getString(R.string.pwd_not_null), Toast.LENGTH_SHORT).show();
            return ;
        }
        else if(pwd.length() < 6) {
            Toast.makeText(App.mContext, App.mContext.getString(R.string.pwd_not_enough), Toast.LENGTH_SHORT).show();
            return ;
        } else if(pwd.length() > 12){
            Toast.makeText(App.mContext, App.mContext.getString(R.string.pwd_over_enough), Toast.LENGTH_SHORT).show();
            return ;
        }

        userBean.setPassword(pwd);



        if(StringFormatCheckUtils.isPhoneLegal(nameOrPhone)) {   //电话号码登录

            userBean.setPhoneNum(nameOrPhone);

        } else { //用户名登录

            int nicknameResult = StringFormatCheckUtils.isNickNameLegal(nameOrPhone);
            if(nicknameResult == R.string.nickname_right) {

                userBean.setName(nameOrPhone);

            } else {

                ToastUtils.showCustomToast(App.mContext, ToastUtils.TOAST_CENTER, App.mContext.getString(nicknameResult));
                return;
            }

        }

        user.setUser(userBean);

        Observable observable = Network.getMainApi().signin(user);
        Subscriber<Base<PhoneAndToken>> s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<PhoneAndToken>>(){

            @Override
            public void onNext(Base<PhoneAndToken> o) {
                if(o.getErrorCode() == 0) {
                    loginView.onLoginSmartNetwork(o.getRes().getUserName(), o.getRes().getPhone(), pwd, o.getRes().getToken(), o.getRes().getHeadUrl());
                } else {
                    loginView.onLoginFailure(o.getMessage());
                }
            }
        }, loginView.onGetContext()
        );

        JxUtils.toSubscribe(observable, s);

    }


    @Override
    public void registered(String nickName, String password, String againPwd, String phoneNum, String uid) {
        DebugUtils.d("WelcomePresenter", "registered()");

        /*if(nickName.length() == 0 ) {
            registeredView.onRegisteredFailure(App.mContext.getString(R.string.please_input_nickname));
            return ;
        }

        if(nickName.length() < 2) {
            registeredView.onRegisteredFailure(App.mContext.getString(R.string.nickname_not_enough));
            return ;
        }

        if(nickName.length() > 12) {
            registeredView.onRegisteredFailure(App.mContext.getString(R.string.nickname_over_enough));
            return ;
        }


        if(password.length() == 0 ) {
            registeredView.onRegisteredFailure(App.mContext.getString(R.string.pwd_not_null));
            return ;
        }

        if(againPwd.length() == 0) {
            registeredView.onRegisteredFailure(App.mContext.getString(R.string.again_pwd_not_null));
            return ;
        }

        if(againPwd.length() > 12) {
            registeredView.onRegisteredFailure(App.mContext.getString(R.string.pwd_over_enough));
            return ;
        }

        if(againPwd.length() < 6) {
            registeredView.onRegisteredFailure(App.mContext.getString(R.string.pwd_not_enough));
            return ;
        }


        if( !(againPwd.equals(password)) ) {
            registeredView.onRegisteredFailure(App.mContext.getString(R.string.pwd_again_error));
            return ;
        }

        if(!(StringFormatCheckUtils.isPhoneLegal(phoneNum))) {
            registeredView.onRegisteredFailure(App.mContext.getString(R.string.phone_wrong_format));
            return ;
        }*/

            User user = new User();
            User.UserBean userBean = user.new UserBean();
            userBean.setName(nickName);
            userBean.setPassword(password);
            userBean.setPhoneNum(phoneNum);
            userBean.setUid(uid);
            user.setUser(userBean);

            Observable observable = Network.getMainApi().registered(user);
            Subscriber<Base<Null>> s = new ProgressSubscriber<>(new ProgressSubscriber.SubscriberOnNextListener<Base<Null>>() {
                @Override
                public void onNext(Base<Null> o) {
                    if (o.getErrorCode() == 0)
                        registeredView.onRegisteredSuccess();
                    else {
                        registeredView.onRegisteredFailure(o.getMessage());
                    }
                }

            }, registeredView.onGetContext());

            JxUtils.toSubscribe(observable, s);
        }



    @Override
    public void gizRegistered(String nickName, String phoneNum, String ver, String pwd, String againPwd) {
        // 昵称检查
        int nickNameResult =  StringFormatCheckUtils.isNickNameLegal(nickName);
        if(nickNameResult != R.string.nickname_right) {
            showRegisterToast(App.mContext.getString(nickNameResult));
            return;
        }
        // 号码检查
        if( !(StringFormatCheckUtils.isPhoneLegal(phoneNum)) ) {
            showRegisterToast(App.mContext.getString(R.string.phone_wrong_format));
            return;
        }

        //验证码检查
        int verificationResult = StringFormatCheckUtils.isVerificationLegal(ver);
        if(verificationResult != R.string.verification_right) {
            showRegisterToast(App.mContext.getString(verificationResult));
            return ;
        }

        //密码检测
        int pwdResult = StringFormatCheckUtils.isPasswordLegal(pwd);
        if(pwdResult != R.string.pwd_right) {
            showRegisterToast(App.mContext.getString(pwdResult));
            return;
        }

        //再次输入密码检测
        int againPwdResult = StringFormatCheckUtils.isAgainPwdLegal(pwd, againPwd);
        if(againPwdResult != R.string.again_pwd_right) {
            showRegisterToast(App.mContext.getString(againPwdResult));
            return ;
        }

        LogUtils.d("gizRegistered", "注册参数检测通过");
        GizWifiSDK.sharedInstance().registerUser(phoneNum, pwd, ver, GizUserAccountType.GizUserPhone);

    }


    /** 获取机智云验证码 */
    @Override
    public void sendPhoneSMSCode(String phone) {

        if(StringFormatCheckUtils.isPhoneLegal(phone)) {//非法检查

            GizWifiSDK.sharedInstance().requestSendPhoneSMSCode(Constant.APP_Secret, phone);

        } else {
            Log.d("sendPhoneSMSCode", "号码格式有误！");
            showRegisterToast( App.mContext.getString(R.string.phone_wrong_format));
        }
    }

    /** 参数合法性检测和修改smartCookApp服务端代码 */
    @Override
    public void CheckRetrievePwdPara(String phoneNum, String verification, String newPwd, String againNewPwd) {
        // 号码检查
        if( !(StringFormatCheckUtils.isPhoneLegal(phoneNum)) ) {
            showRegisterToast(App.mContext.getString(R.string.phone_wrong_format));
            return;
        }

        //验证码检查
        int verificationResult = StringFormatCheckUtils.isVerificationLegal(verification);
        if(verificationResult != R.string.verification_right) {
            showRegisterToast(App.mContext.getString(verificationResult));
            return ;
        }

        //密码检测
        int pwdResult = StringFormatCheckUtils.isPasswordLegal(newPwd);
        if(pwdResult != R.string.pwd_right) {
            showRegisterToast(App.mContext.getString(pwdResult));
            return;
        }

        //再次输入密码检测
        int againPwdResult = StringFormatCheckUtils.isAgainPwdLegal(newPwd, againNewPwd);
        if(againPwdResult != R.string.again_pwd_right) {
            showRegisterToast(App.mContext.getString(againPwdResult));
            return ;
        }

        LogUtils.d("gizRegistered", "注册参数检测通过");

        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("phone", phoneNum);
        paramsMap.put("code", verification);
        paramsMap.put("password", newPwd);

        String strEntity = gson.toJson(paramsMap);


        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"),strEntity);

        Observable observable = Network.getMainApi().resetPassword(body);


        Subscriber<Base<Null>> s = new ProgressSubscriber<>(new ProgressSubscriber.SubscriberOnNextListener<Base<Null>>() {
            @Override
            public void onNext(Base<Null> o) {
                if(o.getErrorCode() == 0) {
                    retrievePwdView.onRetrievePwdViewSuccess();
                } else {
                    retrievePwdView.onGetVerFailure(o.getMessage());
                }
            }

        }, retrievePwdView.onGetContext());


        JxUtils.toSubscribe(observable, s);

    }

    /** Giz服务器端密码重置成功后,再重置smartCookApp服务器代码 */
    @Override
    public void Retrieve(String phoneNum, String newPwd) {

        User user = new User();
        User.UserBean userBean = user.new UserBean();
        userBean.setPhoneNum(phoneNum);
        userBean.setPassword(newPwd);
        user.setUser(userBean);

        Observable observable = Network.getMainApi().login(user);

        Subscriber<Base<Token>> s = new ProgressSubscriber<>(new ProgressSubscriber.SubscriberOnNextListener<Base<Token>>() {
            @Override
            public void onNext(Base<Token> o) {

            }

        }, loginView.onGetContext());


        JxUtils.toSubscribe(observable, s);

    }



    @Override
    public void getVerification(String phoneNum) {
        if(StringFormatCheckUtils.isPhoneLegal(phoneNum)) {

            Observable observable = Network.getMainApi().getVerification(phoneNum);

            Subscriber<Base<Null>> s = new ProgressSubscriber<>(new ProgressSubscriber.SubscriberOnNextListener<Base<Null>>() {
                @Override
                public void onNext(Base<Null> o) {
                    if(o.getErrorCode() == 0) {
                        retrievePwdView.onGetVerSuccess();
                    } else {
                        retrievePwdView.onGetVerFailure(o.getMessage());
                    }
                }

            }, retrievePwdView.onGetContext());

            JxUtils.toSubscribe(observable, s);

        }
    }



    private void showRegisterToast(String info) {
        ToastUtils.showCustomToast(App.mContext, ToastUtils.TOAST_CENTER, info);
    }

}





