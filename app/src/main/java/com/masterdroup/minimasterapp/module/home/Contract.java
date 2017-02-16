package com.masterdroup.minimasterapp.module.home;

import com.masterdroup.minimasterapp.BasePresenter;
import com.masterdroup.minimasterapp.BaseView;
import com.masterdroup.minimasterapp.model.UserInfo;

/**
 * Created by 11473 on 2017/2/16.
 */

public class Contract {

    interface Presenter extends BasePresenter {

        void getUserInfo();

    }

    interface UserView extends BaseView<Presenter> {

        void onShowUserInfo(UserInfo userInfo);

    }

}
