package com.masterdroup.minimasterapp.module.welcomeModule;

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


    }

    interface MainView extends BaseView<Presenter> {

        void onShowLoginView();

        void onRegisteredView();

        void onShowMainView();
    }

    interface LoginView extends BaseView<Presenter> {

    }

    interface RegisteredView extends BaseView<Presenter> {

    }
}
