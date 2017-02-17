package com.masterdroup.minimasterapp.module.welcomeModule;


import android.support.annotation.Nullable;

import com.blankj.utilcode.utils.LogUtils;
import com.masterdroup.minimasterapp.App;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.api.Network;
import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.Null;
import com.masterdroup.minimasterapp.model.Token;
import com.masterdroup.minimasterapp.model.User;
import com.masterdroup.minimasterapp.module.progress.ProgressSubscriber;
import com.masterdroup.minimasterapp.util.DebugUtils;
import com.masterdroup.minimasterapp.util.JxUtils;
import com.masterdroup.minimasterapp.util.Utils;

import rx.Observable;
import rx.Subscriber;

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
    public boolean isLogin() {
        //判读token值是否存在
        return App.spUtils.contains(App.mContext.getString(R.string.key_token));

    }

    @Override
    public void login(String name, String pwd) {
        DebugUtils.d("WelcomePresenter", "login()");

        if (name.isEmpty() || pwd.isEmpty()) {
            loginView.onLoginFailure(App.mContext.getString(R.string.err_login_1));
        } else {

            User user = new User();
            User.UserBean userBean = user.new UserBean();
            userBean.setName(name);
            userBean.setPassword(pwd);
            user.setUserBean(userBean);

            Observable observable = Network.getMainApi().login(user);

//            observable.subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Action1<Base<Token>>() {
//                        @Override
//                        public void call(Base<Token> tokenBase) {
//                            if (tokenBase.getErrorCode() == 0)
//                                loginView.onLoginSuccess(tokenBase.getRes().getToken());
//                            else {
//                                loginView.onLoginFailure(tokenBase.getMessage());
//                                LogUtils.e("login()", tokenBase.getMessage());
//                            }
//                        }
//                    }, new Action1<Throwable>() {
//                        @Override
//                        public void call(Throwable throwable) {
//
//                        }
//                    });

            Subscriber<Base<Token>> subscriber = new Subscriber<Base<Token>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Base<Token> tokenBase) {
                    if (tokenBase.getErrorCode() == 0)
                        loginView.onLoginSuccess(tokenBase.getRes().getToken());
                    else {
                        loginView.onLoginFailure(tokenBase.getMessage());
                        LogUtils.e("login()", tokenBase.getMessage());
                    }
                }
            };


            Subscriber<Base<Token>> s = new ProgressSubscriber<>(new ProgressSubscriber.SubscriberOnNextListener<Base<Token>>() {
                @Override
                public void onNext(Base<Token> o) {
                    if (o.getErrorCode() == 0)
                        loginView.onLoginSuccess(o.getRes().getToken());
                    else {
                        loginView.onLoginFailure(o.getMessage());
                    }
                }

            }, loginView.onGetContext());


//            Subscriber<Base<Token>> s = new ActionSubscriber<>(new Action1<Base<Token>>() {
//                @Override
//                public void call(Base<Token> tokenBase) {
//                    if (tokenBase.getErrorCode() == 0)
//                        loginView.onLoginSuccess(tokenBase.getRes().getToken());
//                    else {
//                        loginView.onLoginFailure(tokenBase.getMessage());
//                        LogUtils.e("login()", tokenBase.getMessage());
//                    }
//                }
//            }, new Action1<Throwable>() {
//                @Override
//                public void call(Throwable throwable) {
//                    throwable.printStackTrace();
//                }
//            }, new Action0() {
//                @Override
//                public void call() {
//
//                }
//            });

//            JxUtils.toSubscribe(observable, subscriber);
            JxUtils.toSubscribe(observable, s);
        }


    }

    @Override
    public void registered(String name, String password, @Nullable String phoneNum) {
        DebugUtils.d("WelcomePresenter", "registered()");

        if (name.isEmpty() || password.isEmpty()) {
            registeredView.onRegisteredFailure(App.mContext.getString(R.string.registered_f));
        } else {

            User user = new User();
            User.UserBean userBean = user.new UserBean();
            userBean.setName(name);
            userBean.setPassword(password);
            userBean.setPhoneNun(phoneNum);
            user.setUserBean(userBean);

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

    }


}
