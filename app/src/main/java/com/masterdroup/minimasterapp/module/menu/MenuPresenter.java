package com.masterdroup.minimasterapp.module.menu;


import android.content.Context;
import android.widget.LinearLayout;

import com.masterdroup.minimasterapp.api.Network;
import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.Menu;
import com.masterdroup.minimasterapp.model.RecipesList;
import com.masterdroup.minimasterapp.module.progress.ProgressSubscriber;
import com.masterdroup.minimasterapp.util.JxUtils;
import com.masterdroup.minimasterapp.util.Utils;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by 11473 on 2016/12/21.
 */

public class MenuPresenter implements Contract.Presenter {
    Context mContext;

    @Override
    public void start() {
        mContext = menuListView.getContext();
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

    int index = 0;//从第index个开始获取
    int count = 10;//页数

    @Override
    public void getRecipesListData() {
        Observable o_recipesList = Network.getMainApi().getRecipesList(index, count);
        Subscriber s_recipesList = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<RecipesList>>() {
            @Override
            public void onNext(Base<RecipesList> o) {

                if (o.getErrorCode() == 0) {
                }


            }
        }, mContext);

        JxUtils.toSubscribe(o_recipesList, s_recipesList);
    }

    @Override
    public void initRV(PullLoadMoreRecyclerView rv) {
        //// TODO: 2017/3/1 初始化 RecyclerView
    }

}
