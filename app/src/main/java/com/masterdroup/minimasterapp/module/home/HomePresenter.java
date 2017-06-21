package com.masterdroup.minimasterapp.module.home;

import android.app.Activity;
import android.content.Intent;

import com.masterdroup.minimasterapp.App;
import com.masterdroup.minimasterapp.api.Network;
import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.RecipesList;
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

    Contract.MenuView menuView;


    public HomePresenter(Contract.MenuView mView) {
        this.menuView = mView;
        menuView = Utils.checkNotNull(mView, "mView cannot be null");
        menuView.setPresenter(this);
    }

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
        //App.spUtils.remove(App.mContext.getString(R.string.key_token));
        App.spUtils.clear();
        Activity activity = (Activity) mUserView.ongetContext();
        activity.startActivity(new Intent(activity, WelcomeActivity.class));
        activity.finish();

    }

    /** 获取轮播图片 */
    @Override
    public void getBanner() {
        Observable o = Network.getMainApi().getBannerRecipesList();
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<RecipesList>>() {
            @Override
            public void onNext(Base<RecipesList> o) {
                if ((o.getErrorCode() == 0) ) {
                    menuView.onGetBannerSuccess(o.getRes().getList());
                } else {
                    menuView.onGetBannerFailure(o.getMessage());
                }
            }
        }, menuView.onGetContext());

        JxUtils.toSubscribe(o, s);
    }



    @Override
    public void getRecipes(int index, int count) {
        Observable o = Network.getMainApi().getRecipesList(index, count);
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<RecipesList>>() {
            @Override
            public void onNext(Base<RecipesList> o) {
                if ((o.getErrorCode() == 0) ) {
                    menuView.onGetRecipesSuccess(o.getRes().getList());
                } else {
                    menuView.onGetRecipesFailure(o.getMessage());
                }
            }
        }, menuView.onGetContext());

        JxUtils.toSubscribe(o, s);
    }

    @Override
    public void getMoreRecipes(int currentIndex, int count) {
        Observable o = Network.getMainApi().getRecipesList(currentIndex, count);
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<RecipesList>>() {
            @Override
            public void onNext(Base<RecipesList> o) {
                if ((o.getErrorCode() == 0) ) {
                    menuView.onGetMoreRecipesSuccess(o.getRes().getList());
                } else {
                    menuView.onGetMoreRecipesFailure(o.getMessage());
                }

            }
        }, menuView.onGetContext());


        JxUtils.toSubscribe(o, s);
    }

}
