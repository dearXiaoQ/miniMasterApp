package com.masterdroup.minimasterapp.module.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.model.Menu;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.masterdroup.minimasterapp.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuViewActivity extends Activity implements Contract.MenuAloneView {


    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_cover)
    ImageView ivCover;
    @Bind(R.id.tv_menu_name)
    TextView tvMenuName;
    @Bind(R.id.tv_menu_info)
    TextView tvMenuInfo;
    @Bind(R.id.iv_user_head)
    ImageView ivUserHead;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;

    Contract.Presenter mPresenter;
    @Bind(R.id.tv_menu_note)
    TextView tvMenuNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_view);
        ButterKnife.bind(this);
        new MenuPresenter(this);

        initData();
    }

    private void initData() {

        int i = getIntent().getIntExtra("menuId", -1);
        if (i != -1)
            mPresenter.gettingData(i);

        Menu menu = new Menu();
        menu.cover_url = "https://images.pexels.com/photos/203089/pexels-photo-203089.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb";
        menu.head_url = "https://static.pexels.com/users/avatars/592/pawel-malinowski-908-medium.jpeg";
        menu.user_name = "0078";
        menu.menu_name = "土豆丝";
        menu.score = "9.3";
        settingData(menu);


    }


    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mPresenter = Utils.checkNotNull(presenter);
    }

    @OnClick(R.id.iv_return)
    public void onClick() {
        finish();
    }

    @Override
    public void settingData(Menu menu) {
        tvTitle.setText(menu.getMenu_name());
        tvMenuName.setText(menu.getMenu_name());
        tvMenuInfo.setText(String.format("%s综合评分 %s人收藏", menu.getScore(), menu.getScore()));
        tvUserName.setText(menu.getUser_name());
        ImageLoader.getInstance().displayGlideImage(menu.getCover_url(), ivCover, this, false);
        ImageLoader.getInstance().displayGlideImage(menu.getHead_url(), ivUserHead, this, true);
    }

}
