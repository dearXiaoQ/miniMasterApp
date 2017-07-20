package com.mastergroup.smartcook.module.menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mastergroup.smartcook.BasePresenter;
import com.mastergroup.smartcook.BaseView;
import com.mastergroup.smartcook.model.CollectionRecipes;
import com.mastergroup.smartcook.model.DetailRecipes;
import com.mastergroup.smartcook.model.Recipes;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by 11473 on 2016/12/21.
 */

public class Contract {


    interface Presenter extends BasePresenter {

        void gettingData(String menuId);

        void getRecipesListData();

        void initRV(PullLoadMoreRecyclerView rv);

        void initMenuViewRV(RecyclerView food_rv, RecyclerView step_rv, RecyclerView cooking_step_rv, RecyclerView comment_rv, GridView likeGView, ImageView likeIv, ImageView favoriteIv, TextView likeNumTv);

        void like();//点赞

        boolean isLike();//是否like

        boolean isOwner();// 是否菜谱创建者

        void reLike(final String menuId);

        void favorite(); //收藏

        boolean isFavorite();//是否收藏

        /** 获取用户自身创建的菜谱 */
        void getMyMenu(int path, int index);

        /** 获取用户自身 */
        void getFavoriteRecipes();

        void share(); //分享

        void search(String searchStr, int index, int count);  //搜索

        /** 新增评论 */
        void sendComment(String commentStr, String menuId);

        /** 获取评论 */
        void getComment(String menuId, TextView commentCountTv);

        /** 跳转到界面 */
        void jumpLikeView();
    }

    interface MenuCreatePresenter extends BasePresenter {

        //提交新创建的菜谱
        void submitNewMenu();


        void initStepRecyclerView(RecyclerView rv, RecyclerView rv_cooking, RecyclerView rv_food);


        //增加一个步骤
        void addStep();


        //增加一个烹饪步骤
        void addCookingStep();

        //增加一列食材
        void addFood();

        void setStepPicture(String url, int requestCode);

        void initDescribeStep();//
    }

    interface MenuAloneView extends BaseView<Presenter> {
        void settingData(DetailRecipes.RecipesBean b);

        Context getContext();

        void onIsOwner(boolean o);

        void onIsLike(boolean o);
        /** 分享缩略图 */
        Bitmap getCoverBitmap();

    }


    interface MenuListView extends BaseView<Presenter> {
        Context getContext();


    }


    interface MyMenuListView extends BaseView<Presenter> {
        Context getContext();

        void onGetMyMenuSuccess(List<CollectionRecipes.RecipesBean> recipesBeanList);

    }


    interface FavoriteListView extends BaseView<Presenter> {
        Context getContext();

        void  onGetMyFavoriteListSuccess(List<CollectionRecipes.RecipesBean> recipesBeenList);

        void  onGetMyFavoriteListFailure(String info);
    }

    interface CollectionListView extends BaseView<Presenter> {
        Context getContext();

        void onGetMyCollectionSuccess(List<CollectionRecipes.RecipesBean> recipesBeanList);
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

    interface SearchView extends BaseView<Presenter> {

        Context getContext();

        /** 获取菜谱成功 */
        void onGetRecipesSuccess(List<Recipes.RecipesBean> recipesList);

        /** 获取菜谱失败 */
        void onGetRecipesFailure(String info);

    }


}
