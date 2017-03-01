package com.masterdroup.minimasterapp.module.menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.masterdroup.minimasterapp.BasePresenter;
import com.masterdroup.minimasterapp.BaseView;
import com.masterdroup.minimasterapp.model.Menu;
import com.masterdroup.minimasterapp.model.Recipes;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

/**
 * Created by 11473 on 2016/12/21.
 */

public class Contract {


    interface Presenter extends BasePresenter {

        void gettingData(int menuId);

        void getRecipesListData();

        void initRV(PullLoadMoreRecyclerView rv);

    }

    interface MenuCreatePresenter extends BasePresenter {

        //提交新创建的菜谱
        void submitNewMenu();


        void initStepRecyclerView(RecyclerView rv);


        //增加一个步骤
        void addStep();

        void setStepPicture(String url, int requestCode);

        void initDescribeStep();//
    }

    interface MenuAloneView extends BaseView<Presenter> {
        void settingData(Menu menu);
    }


    interface MenuListView extends BaseView<Presenter> {
        Context getContext();


    }

    interface MenuCreateView extends BaseView<MenuCreatePresenter> {


        Context getContext();

        String getMenuCoverLocalUrl();

        void setMenuCoverLocalUrl(String url);

        String getMenuCoverServerUrl();

        void setMenuCoverServerUrl(String url);

        String getMenuName();

        String getMenuDescribe();

    }


}
