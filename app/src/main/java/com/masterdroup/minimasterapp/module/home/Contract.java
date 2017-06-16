package com.masterdroup.minimasterapp.module.home;

import android.content.Context;

import com.masterdroup.minimasterapp.BasePresenter;
import com.masterdroup.minimasterapp.BaseView;
import com.masterdroup.minimasterapp.model.Recipes;
import com.masterdroup.minimasterapp.model.User;

import java.util.List;

/**
 * Created by 11473 on 2017/2/16.
 */

public class Contract {

    interface Presenter extends BasePresenter {

        /***
         *  获取菜谱列表
         * @param index 菜谱其实
         * @param count
         */
        void getRecuoes(int index, int count);



        void getUserInfo();
        void outLogin();


    }

    interface UserView extends BaseView<Presenter> {
        Context ongetContext();

        void onShowUserInfo(User user);

    }

    interface MenuView extends BaseView<Presenter> {
        /** 获取菜谱成功 */
        void onGetRecipesSuccess(List<Recipes.RecipesBean> recipes_list);

        /** 获取菜谱失败 */
        void onGetRecipesFailure(String info);


    }

}
