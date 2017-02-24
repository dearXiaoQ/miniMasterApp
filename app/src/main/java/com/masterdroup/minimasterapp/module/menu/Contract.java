package com.masterdroup.minimasterapp.module.menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.masterdroup.minimasterapp.BasePresenter;
import com.masterdroup.minimasterapp.BaseView;
import com.masterdroup.minimasterapp.model.Menu;
import com.masterdroup.minimasterapp.model.Recipes;

/**
 * Created by 11473 on 2016/12/21.
 */

public class Contract {


    interface Presenter extends BasePresenter {

        void gettingData(int menuId);

    }

    interface MenuCreatePresenter extends BasePresenter {

        //提交新创建的菜谱
        void submitNewMenu();


        void initStepRecyclerView(RecyclerView rv);


        //增加一个步骤
        void addStep();

        void setStepPicture(String url,int requestCode);
    }

    interface MenuAloneView extends BaseView<Presenter> {
        void settingData(Menu menu);
    }

    interface MenuCreateView extends BaseView<MenuCreatePresenter> {

        //打包菜谱数据
        Recipes getRecipesDate();

        Context getContext();

        String getMenuCoverLocalUrl();

        void setMenuCoverLocalUrl(String url);

        String getMenuCoverServerUrl();

        void setMenuCoverServerUrl(String url);

    }

    interface MenuListView extends BaseView<Presenter> {


    }
}
