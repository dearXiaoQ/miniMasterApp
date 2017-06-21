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
         * @param index  页码
         * @param count  数量
         */
        void getRecipes(int index, int count);

        /**
         * 加载更多菜谱
         * @param currentIndex  当前菜谱下标
         * @param count         数量
         */
        void getMoreRecipes(int currentIndex, int count );

        void getUserInfo();
        void outLogin();
        /** 获取轮播图片接口 */
        void getBanner();
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

        Context onGetContext();


        /** 加载更多菜谱成功 */
        void onGetMoreRecipesSuccess(List<Recipes.RecipesBean> recipes_list);

        /** 加载更多菜谱失败 */
        void onGetMoreRecipesFailure(String info);

        /** 获取Banner列表成功 */
        void onGetBannerSuccess(List<Recipes.RecipesBean> banner_list);

        /** 获取Banner列表失败 */
        void onGetBannerFailure(String info);
    }

}
