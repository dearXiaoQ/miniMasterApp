package com.mastergroup.smartcook.module.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.mastergroup.smartcook.App;
import com.mastergroup.smartcook.Constant;
import com.mastergroup.smartcook.api.Network;
import com.mastergroup.smartcook.model.Base;
import com.mastergroup.smartcook.model.RecipesList;
import com.mastergroup.smartcook.model.UpdateEntity;
import com.mastergroup.smartcook.model.User;
import com.mastergroup.smartcook.module.progress.ProgressSubscriber;
import com.mastergroup.smartcook.module.welcomeModule.WelcomeActivity;
import com.mastergroup.smartcook.util.AppInnerDownLoder;
import com.mastergroup.smartcook.util.JxUtils;
import com.mastergroup.smartcook.util.Utils;


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

        }, mUserView.onGetContext());

        JxUtils.toSubscribe(o, s);

    }

    @Override
    public void getUpdateInfo() {
        Observable o = Network.getMainApi().getUpdateInfo();
        Subscriber<Base<UpdateEntity>> s = new ProgressSubscriber<>(new ProgressSubscriber.SubscriberOnNextListener<Base<UpdateEntity>>() {
            @Override
            public void onNext(Base<UpdateEntity> b) {
                UpdateEntity update = b.getRes();
                String version = update.getServerVersion();
                double serverVersion = Double.valueOf(version);
                if(serverVersion > Constant.VERSION)
                    // final Context context, final String appName, final String downUrl, final String updateinfo
                    forceUpdate(mUserView.onGetContext(), update.getAppName(), update.getUpdateUrl(), update.getDesc());
                else
                    com.blankj.utilcode.utils.ToastUtils.showShortToast("无版本更新");

                Log.i("updateInfo", b.getRes().getAppName() + "\n" +  b.getRes().getServerVersion() + "\n" + b.getRes().getUpdateUrl() );

            }

        }, mUserView.onGetContext());

        JxUtils.toSubscribe(o, s);
    }

    @Override
    public void outLogin() {
        //App.spUtils.remove(App.mContext.getString(R.string.key_token));
        App.spUtils.clear();
        Activity activity = (Activity) mUserView.onGetContext();
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


    private void forceUpdate(final Context context, final String appName, final String downUrl, final String updateinfo) {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle("smartCook" + "又更新咯！");
        mDialog.setMessage(updateinfo);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //      DownLoadApk.download(MainActivity.this,downUrl,updateinfo,appName);
                AppInnerDownLoder.downLoadApk(HomeActivity.mContext, downUrl, appName);
            }
        }).setCancelable(true).create().show();
    }

}
