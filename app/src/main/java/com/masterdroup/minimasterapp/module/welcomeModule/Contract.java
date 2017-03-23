package com.masterdroup.minimasterapp.module.welcomeModule;

import android.content.Context;
import android.support.annotation.Nullable;

import com.masterdroup.minimasterapp.BasePresenter;
import com.masterdroup.minimasterapp.BaseView;


/**
 * Created by 11473 on 2016/11/29.
 */

public class Contract {
    interface Presenter extends BasePresenter {

        void showLoginView();

        void showRegisteredView();

        void showMainView();

        boolean isLogin();//是否有用户登录

        void login(String name, String pwd, String giz_uid, String giz_token);

        void gizLogin(String name, String pwd);//机智云登录

        void registered(String name, String password, @Nullable String phoneNum, String uid);


        /**
         * @param name     注册用户名（可以是手机号、邮箱或普通用户名）
         * @param password 注册密码
         */
        void gizRegistered(String name, String password);//机智云注册


        void sendPhoneSMSCode(String phone);
    }

    interface MainView extends BaseView<Presenter> {

        void onShowLoginView();

        void onRegisteredView();

        void onShowMainView();

    }

    interface LoginView extends BaseView<Presenter> {
        Context onGetContext();

        void onLoginSuccess(String name,String token, String giz_uid, String giz_token);

        void onLoginFailure(@Nullable String info);

    }

    interface RegisteredView extends BaseView<Presenter> {
        Context onGetContext();

        void onRegisteredSuccess();

        void onRegisteredFailure(@Nullable String info);

    }
}
