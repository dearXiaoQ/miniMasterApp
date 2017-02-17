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

        void login(String name, String pwd);

        void registered(String name, String password, @Nullable String phoneNum);

    }

    interface MainView extends BaseView<Presenter> {

        void onShowLoginView();

        void onRegisteredView();

        void onShowMainView();

    }

    interface LoginView extends BaseView<Presenter> {
        Context onGetContext();

        void onLogin();

        void onLoginSuccess(String token);

        void onLoginFailure(@Nullable String info);
    }

    interface RegisteredView extends BaseView<Presenter> {
        Context onGetContext();

        void onRegistered();

        void onRegisteredSuccess();

        void onRegisteredFailure(@Nullable String info);

    }
}
