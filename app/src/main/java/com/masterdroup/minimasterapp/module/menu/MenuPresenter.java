package com.masterdroup.minimasterapp.module.menu;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.masterdroup.minimasterapp.api.Network;
import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.Menu;
import com.masterdroup.minimasterapp.model.Recipes;
import com.masterdroup.minimasterapp.model.RecipesList;
import com.masterdroup.minimasterapp.module.progress.ProgressSubscriber;
import com.masterdroup.minimasterapp.util.JxUtils;
import com.masterdroup.minimasterapp.util.Utils;
import com.masterdroup.minimasterapp.view.MenuListRVAdapter;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

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
    MenuListRVAdapter adapter;
    public static List<Recipes.RecipesBean> list;

    @Override
    public void start() {
        mContext = menuListView.getContext();
        list = new ArrayList<>();
        adapter = new MenuListRVAdapter(mContext, list);
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
    int count = 3;//页数

    Observable o_recipesList;
    Subscriber s_recipesList;

    @Override
    public void getRecipesListData() {
        o_recipesList = Network.getMainApi().getRecipesList(index, count);
        s_recipesList = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<RecipesList>>() {
            @Override
            public void onNext(Base<RecipesList> o) {

                if (o.getErrorCode() == 0) {
                    list = o.getRes().getList();
                    adapter.notifyDataSetChanged();
                }


            }
        }, mContext);

        JxUtils.toSubscribe(o_recipesList, s_recipesList);
    }

    @Override
    public void initRV(final PullLoadMoreRecyclerView rv) {
        //// TODO: 2017/3/1 初始化 RecyclerView

        rv.setAdapter(adapter);
        rv.setLinearLayout();
        rv.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                refreshRV(rv);
            }

            @Override
            public void onLoadMore() {
                loadMore(rv);

            }
        });


    }

    void refreshRV(final PullLoadMoreRecyclerView rv) {
        s_recipesList = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<RecipesList>>() {
            @Override
            public void onNext(Base<RecipesList> o) {

                if (o.getErrorCode() == 0) {
                    list = o.getRes().getList();
                    adapter.notifyDataSetChanged();
                    rv.setPullLoadMoreCompleted();

                }
            }
        }, mContext);
        JxUtils.toSubscribe(o_recipesList, s_recipesList);
    }

    void loadMore(final PullLoadMoreRecyclerView rv) {
        count = count + 1;
        o_recipesList = Network.getMainApi().getRecipesList(index, count);
        s_recipesList = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<RecipesList>>() {
            @Override
            public void onNext(Base<RecipesList> o) {


                if (list.size() == o.getRes().getList().size()) {
                    Toast.makeText(mContext, "没有更多了", Toast.LENGTH_SHORT).show();
                } else {
                    list = o.getRes().getList();
                    adapter.notifyDataSetChanged();
                }
                rv.setPullLoadMoreCompleted();
            }
        }, mContext);

        JxUtils.toSubscribe(o_recipesList, s_recipesList);

    }
}
