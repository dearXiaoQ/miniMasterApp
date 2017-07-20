package com.mastergroup.smartcook.module.settings;

import android.content.Context;
import android.support.annotation.Nullable;

import com.mastergroup.smartcook.BasePresenter;
import com.mastergroup.smartcook.BaseView;
import com.mastergroup.smartcook.model.User;

/**
 * Created by 11473 on 2017/2/20.
 */

public class Contract {


    interface Presenter extends BasePresenter {
        ///更新信息
        void upDate(User user, @Nullable String headUrl);

        //获取用户信息
        void getUserDate();

        //退出登录
        void logout();

        //意见反馈接口
        void feedBack(String msg, String type);
    }

    interface View extends BaseView<Presenter> {
        Context onGetContext();

        void putUserHeadUrl(String url);

        String getUserHeadUrl();

        void putUserHeadServerUrl(String url);

        String getUserHeadServerUrl();

        void setUserDate(User userDate);
    }

    interface FeedBackView extends BaseView<Presenter> {
        Context onGetContext();

        void onFeedBackInfo(String info);
    }

}
