package com.masterdroup.minimasterapp.module.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.masterdroup.minimasterapp.Constant;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.model.Recipes;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.masterdroup.minimasterapp.util.Utils;
import com.melnykov.fab.FloatingActionButton;

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


    @Bind(R.id.tv_menu_note)
    TextView tvMenuNote;

    @Bind(R.id.rv_food)
    RecyclerView rv_food;

    @Bind(R.id.rv_step)
    RecyclerView rv_step;

    @Bind(R.id.tv_more_button)
    TextView mTvMoreButton;

    @Bind(R.id.rv_cooking_step)
    RecyclerView rv_cookingStep;

    Contract.Presenter mPresenter;

    @Bind(R.id.vsv)
    NestedScrollView mVsv;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.rv_like)
    RecyclerView rv_like;

    boolean islike;//点赞 0 否 ，1 是

    /**
     * 菜谱id
     */
    String recipesBeanID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_view);
        ButterKnife.bind(this);
        new MenuPresenter(this);
        mPresenter.start();
        initView();
        initData();
    }

    private void initView() {
        mTvMoreButton.setVisibility(View.VISIBLE);


        mVsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    mFab.hide();
                } else {
                    mFab.show();
                }
            }
        });
    }



    private void initData() {


        mPresenter.initMenuViewRV(rv_food, rv_step, rv_cookingStep, rv_like);


        recipesBeanID = getIntent().getStringExtra("_id");

        mPresenter.gettingData(recipesBeanID);


    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mPresenter = Utils.checkNotNull(presenter);
    }

    @OnClick({R.id.iv_return, R.id.fab, R.id.tv_more_button})
    public void onClick(View v) {
        if (v.getId() == R.id.iv_return)
            finish();
        if (v.getId() == R.id.fab) {

            Intent intent = new Intent(MenuViewActivity.this, DeviceSelectActivity.class);
            intent.putExtra("_id", recipesBeanID);

            startActivity(intent);
        }
        if (v.getId() == R.id.tv_more_button) {

            mPresenter.like();
        }

    }

    @Override
    public void settingData(Recipes.RecipesBean recipesBean) {
        tvTitle.setText(recipesBean.getName());
        tvMenuName.setText(recipesBean.getName());
//        tvMenuInfo.setText(String.format("%s综合评分 %s人收藏", recipesBean.getScore(), recipesBean.getScore()));
        tvUserName.setText(recipesBean.getOwner().getOwnerUid().getName());
        ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + recipesBean.getDetail().getImgSrc(), ivCover, this, false);
        ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + recipesBean.getOwner().getOwnerUid().getHeadUrl(), ivUserHead, this, true);
        tvMenuNote.setText(recipesBean.getDetail().getDescribe());
    }

    @Override
    public Context getContext() {
        return this;
    }

}
