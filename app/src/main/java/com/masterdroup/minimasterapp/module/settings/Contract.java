package com.masterdroup.minimasterapp.module.settings;

import android.content.Context;
import android.support.annotation.Nullable;

import com.masterdroup.minimasterapp.BasePresenter;
import com.masterdroup.minimasterapp.BaseView;
import com.masterdroup.minimasterapp.model.User;

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
    }

    interface View extends BaseView<Presenter> {
        Context onGetContext();

        void putUserHeadUrl(String url);

        String getUserHeadUrl();

        void putUserHeadServerUrl(String url);

        String getUserHeadServerUrl();

        void setUserDate(User userDate);
    }
}
