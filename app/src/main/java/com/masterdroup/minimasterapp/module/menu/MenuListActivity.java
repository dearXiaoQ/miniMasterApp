package com.masterdroup.minimasterapp.module.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.model.Menu;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuListActivity extends Activity implements Contract.MenuListView {


    @Bind(R.id.rv)
    PullLoadMoreRecyclerView mRv;
    Contract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        ButterKnife.bind(this);


        presenter = new MenuPresenter(this);
        presenter.start();
        presenter.initRV(mRv);
        presenter.getRecipesListData();//发起请求



//        Menu menu = new Menu();
//        menu.score = "2.0分";
//        menu.menu_name = "面包";
//        menu.user_name = "kija";
//        menu.cover_url = "https://images.pexels.com/photos/5506/bread-food-salad-sandwich.jpg?w=1260&h=750&auto=compress&cs=tinysrgb";
//        menu.head_url = "https://static.pexels.com/users/avatars/2656/jaymantri-693-medium.jpeg";
//        menus.add(menu);
//
//
//        Menu menu2 = new Menu();
//        menu2.score = "9.9分";
//        menu2.menu_name = "Sponsored";
//        menu2.user_name = "Josh Sorenson";
//        menu2.cover_url = "https://images.pexels.com/photos/7401/pexels-photo.jpg?w=1260&h=750&auto=compress&cs=tinysrgb";
//        menu2.head_url = "https://static.pexels.com/users/avatars/11929/josh-sorenson-243-medium.jpeg";
//        menus.add(menu2);
//
//        Menu menu3 = new Menu();
//        menu3.score = "9.9分";
//        menu3.menu_name = "Sponsored";
//        menu3.user_name = "Daniel Lindstrom";
//        menu3.cover_url = "https://images.pexels.com/photos/14737/pexels-photo.jpg?w=1260&h=750&auto=compress&cs=tinysrgb";
//        menu3.head_url = "https://www.gravatar.com/avatar/156096673227a23cbbed3bfa9784167e?s=200&d=mm";
//        menus.add(menu3);



    }

    @OnClick(R.id.iv_return)
    public void onClick() {
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        this.presenter = presenter;
    }


}
