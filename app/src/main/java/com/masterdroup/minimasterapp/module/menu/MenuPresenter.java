package com.masterdroup.minimasterapp.module.menu;


import com.masterdroup.minimasterapp.api.Network;
import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.Menu;
import com.masterdroup.minimasterapp.util.Utils;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by 11473 on 2016/12/21.
 */

public class MenuPresenter implements Contract.Presenter {
    @Override
    public void start() {

    }

    Contract.MenuAloneView menuAloneView;
    Contract.MenuListView menuListView;

    public MenuPresenter(Contract.MenuAloneView View) {
        menuAloneView = Utils.checkNotNull(View, "mView cannot be null!");
        menuAloneView.setPresenter(this);
    }

    public MenuPresenter(Contract.MenuListView View) {
        menuListView = Utils.checkNotNull(View, "mView cannot be null!");
        menuListView.setPresenter(this);
    }


    @Override
    public void gettingData(int menuId) {
        Network.getMainApi().menuInfo(menuId)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Base<Menu>>() {
                    @Override
                    public void call(Base<Menu> menuBase) {
                        menuAloneView.settingData(menuBase.getRes());
                    }
                });




    }
}
