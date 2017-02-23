package com.masterdroup.minimasterapp.module.home;

import android.app.Activity;
import android.content.Intent;

import com.blankj.utilcode.utils.SPUtils;
import com.masterdroup.minimasterapp.App;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.api.Network;
import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.User;
import com.masterdroup.minimasterapp.module.progress.ProgressSubscriber;
import com.masterdroup.minimasterapp.module.welcomeModule.WelcomeActivity;
import com.masterdroup.minimasterapp.util.JxUtils;
import com.masterdroup.minimasterapp.util.Utils;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by 11473 on 2017/2/16.
 */

public class HomePresenter implements Contract.Presenter {

    Contract.UserView mUserView;

    @Override
    public void start() {

    }


    public HomePresenter(Contract.UserView mUserView) {
        this.mUserView = mUserView;
        mUserView = Utils.checkNotNull(mUserView, " cannot be null!");
        mUserView.setPresenter(this);
    }


    @Override
    public void getUserInfo() {


        Observable o = Network.getMainApi().getUserInfo();
        Subscriber<Base<User>> s = new ProgressSubscriber<>(new ProgressSubscriber.SubscriberOnNextListener<Base<User>>() {
            @Override
            public void onNext(Base<User> b) {
                if (b.getErrorCode() == 0)
                    mUserView.onShowUserInfo(b.getRes());
                else ;
            }

        }, mUserView.ongetContext());


        JxUtils.toSubscribe(o, s);

    }

    @Override
    public void outLogin() {
        App.spUtils.remove(App.mContext.getString(R.string.key_token));
        Activity activity = (Activity) mUserView.ongetContext();
        activity.startActivity(new Intent(activity, WelcomeActivity.class));
        activity.finish();

    }
}
