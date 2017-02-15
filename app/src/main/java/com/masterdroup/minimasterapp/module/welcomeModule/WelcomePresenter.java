package com.masterdroup.minimasterapp.module.welcomeModule;


import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.SPUtils;
import com.google.gson.Gson;
import com.masterdroup.minimasterapp.App;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.api.Api;
import com.masterdroup.minimasterapp.api.Network;
import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.Token;
import com.masterdroup.minimasterapp.model.User;
import com.masterdroup.minimasterapp.util.DebugUtils;
import com.masterdroup.minimasterapp.util.SPUtil;
import com.masterdroup.minimasterapp.util.Utils;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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

    @Override
    public void login(String name, String pwd) {
        DebugUtils.d("onClick", "button2");

        if (name.isEmpty() || pwd.isEmpty()) {
            loginView.onLoginFailure(App.mContext.getString(R.string.err_login_1));
        } else {

            User user = new User();
            User.UserBean userBean = user.new UserBean();
            userBean.setName(name);
            userBean.setPassword(pwd);
            user.setUserBean(userBean);
            String body = new Gson().toJson(user);


            Network.getMainApi().login(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Base<Token>>() {
                        @Override
                        public void call(Base<Token> tokenBase) {
                            if (tokenBase.getErrorCode() == 0)
                                SPUtil.putAndApply(App.mContext, "token", tokenBase.getRes().getToken());
                            else {
                                loginView.onLoginFailure(null);
                                LogUtils.e("login()", tokenBase.getMessage());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
//            Network.getMainApi().login(user)
//                    .observeOn(Schedulers.io())
//                    .subscribeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Action1<Base<Token>>() {
//                        @Override
//                        public void call(Base<Token> tokenBase) {
//                            if (tokenBase.getErrorCode() == 0)
//                                SPUtil.putAndApply(App.mContext, "token", tokenBase.getRes().getToken());
//                            else {
//                                loginView.onLoginFailure(null);
//                                LogUtils.e("login()", tokenBase.getMessage());
//                            }
//
//                        }
//                    }, new Action1<Throwable>() {
//                        @Override
//                        public void call(Throwable throwable) {
//                            throwable.printStackTrace();
//                            loginView.onLoginFailure(null);
//                        }
//                    });

        }


    }


}
