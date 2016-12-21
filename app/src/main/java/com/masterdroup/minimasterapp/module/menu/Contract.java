package com.masterdroup.minimasterapp.module.menu;

import com.masterdroup.minimasterapp.BasePresenter;
import com.masterdroup.minimasterapp.BaseView;
import com.masterdroup.minimasterapp.model.Menu;

/**
 * Created by 11473 on 2016/12/21.
 */

public class Contract {


    interface Presenter extends BasePresenter {

        void gettingData(int menuId);

    }

    interface MenuAloneView extends BaseView<Presenter> {


        void settingData(Menu menu);


    }

    interface MenuListView extends BaseView<Presenter> {


    }
}
