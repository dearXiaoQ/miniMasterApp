package com.masterdroup.minimasterapp.welcomeModule;

import com.masterdroup.minimasterapp.util.Utils;

/**
 * Created by 11473 on 2016/11/29.
 */

public class WelcomePresenter implements Contract.Presenter {

    Contract.MainView mianView;
    Contract.LoginView loginView;
    Contract.RegisteredView registeredView;

    public WelcomePresenter(Contract.MainView mView) {
        this.mianView = mView;
        mianView = Utils.checkNotNull(mView, "mView cannot be null!");
        mianView.setPresenter(this);
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


    @Override
    public void start() {

    }

    @Override
    public void showLoginView() {
        mianView.onShowLoginView();
    }

    @Override
    public void showRegisteredView() {
        mianView.onRegisteredView();
    }

    @Override
    public void showMainView() {
        mianView.onShowMainView();
    }
}
