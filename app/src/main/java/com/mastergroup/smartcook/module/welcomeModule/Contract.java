package com.mastergroup.smartcook.module.welcomeModule;

import android.content.Context;
import android.support.annotation.Nullable;

import com.mastergroup.smartcook.BasePresenter;
import com.mastergroup.smartcook.BaseView;


/**
 * Created by 11473 on 2016/11/29.
 */

public class Contract {
    interface Presenter extends BasePresenter {


        void showLoginView();

        void showRegisteredView();

        void showMainView();

        void showRetrieveView();

        boolean isLogin();//是否有用户登录

        void login(String name, String pwd, String giz_uid, String giz_token);

        /**
         * 登录
         * @param nameOrPhone  该参数可以是用户名、或者注册手机号。 当使用用户名登录时，登录到我们的云服务器， 当使用注册手机号登录时，登录到机智云平台
         * @param pwd
         */
        void gizLogin(String nameOrPhone, String pwd);//机智云登录

        void registered(String nickName, String password, String againPwd, @Nullable String phoneNum, String uid);


        /**
         *
         * @param nickName 昵称
         * @param phoneNum 注册号码
         * @param ver      注册手机号接收的验证码
         * @param pwd      密码
         * @param againPwd 密码（第二次输入，要与第一次输入的验证）
         */
        void gizRegistered(String nickName, String phoneNum, String ver, String pwd, String againPwd);//机智云注册

        /**
         *
         * @param phone  注册手机号
         */
        void sendPhoneSMSCode(String phone);

        /** 参数检测和机制端密码修改 */
        void CheckRetrievePwdPara(String phoneNum, String verification,String newPwd, String againNewPwd);

        /** 服务器端修改密码 */
        void Retrieve(String phoneNum, String newPwd);

        /** 通过smartCookApp服务器获取验证码 */
        void getVerification(String trim);

    }

    interface MainView extends BaseView<Presenter> {

        void onShowLoginView();

        void onRegisteredView();

        void onShowMainView();

    }

    interface LoginView extends BaseView<Presenter> {

        /** 是否使用电话号码登录 */

        Context onGetContext();

        void onLoginSuccess(String name,String token, String giz_uid, String giz_token);

        void onLoginFailure(String info);

        void onGetUserInfoSuccess();


        /** 登录smartApp服务器成功后，再登录Gzi的服务器 */
        void onLoginSmartNetwork(String userName, String phone, String pwd, String smart_token, String headUrl);
        /** 找回密码界面 */
        void onRetrieveView();

    }

    interface RegisteredView extends BaseView<Presenter> {
        Context onGetContext();

        void onRegisteredSuccess();

        void onRegisteredFailure(String info);

    }


    interface  RetrievePwdView extends BaseView<Presenter> {
        Context onGetContext();

        void onRetrievePwdViewSuccess();

        void onRetrievePwdFailure(String info);
        /** 获取验证码成功 */
        void onGetVerSuccess();
        /** 获取验证码失败 */
        void onGetVerFailure(String info);

    }

}
