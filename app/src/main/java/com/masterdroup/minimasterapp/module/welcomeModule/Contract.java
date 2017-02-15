package com.masterdroup.minimasterapp.module.welcomeModule;

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

        void login(String name, String pwd);

    }

    interface MainView extends BaseView<Presenter> {

        void onShowLoginView();

        void onRegisteredView();

        void onShowMainView();

    }

    interface LoginView extends BaseView<Presenter> {

        void onLogin();

        void onLoginSuccess();

        void onLoginFailure(@Nullable String info);
    }

    interface RegisteredView extends BaseView<Presenter> {


    }
}
