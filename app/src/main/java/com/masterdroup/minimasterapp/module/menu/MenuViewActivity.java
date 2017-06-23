package com.masterdroup.minimasterapp.module.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.masterdroup.minimasterapp.Constant;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.model.Like;
import com.masterdroup.minimasterapp.model.Recipes;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.masterdroup.minimasterapp.util.Utils;
import com.melnykov.fab.FloatingActionButton;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuViewActivity extends Activity implements Contract.MenuAloneView {


    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_cover)
    ImageView ivCover;
    /*@Bind(R.id.tv_menu_name)
    TextView tvMenuName;* /
   /* @Bind(R.id.tv_menu_info)
    TextView tvMenuInfo;*/
    @Bind(R.id.iv_user_head)
    ImageView ivUserHead;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.like_gv)
    GridView gridView;


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
    @Bind(R.id.favorite)
    ImageView favoriteIv;


    /**
     * 菜谱id
     */
    String recipesBeanID;
    String userHeadUrl;

    @Bind(R.id.rv_menu_comment)
    RecyclerView rvMenuComment;
    @Bind(R.id.tv_comment_count)
    TextView tvCommentCount;
    @Bind(R.id.ll_comment)
    LinearLayout llComment;
    @Bind(R.id.like_iv)
    ImageView likeIv;

    /** 点赞列表 */
    List<Like> likes;

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


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.gettingData(recipesBeanID);
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

        gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Intent likeIntent = new Intent(MenuViewActivity.this, LikeListActivity.class);
                likeIntent.putExtra("likes", (Serializable) likes);
                startActivity(likeIntent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent likeIntent = new Intent(MenuViewActivity.this, LikeListActivity.class);
                likeIntent.putExtra("likes", (Serializable) likes);
                startActivity(likeIntent);
                return false;
            }
        });

    }

    private void initData() {

        mPresenter.initMenuViewRV(rv_food, rv_step, rv_cookingStep , rvMenuComment, gridView, likeIv, favoriteIv);

        recipesBeanID = getIntent().getStringExtra("_id");

        mPresenter.gettingData(recipesBeanID);
    }


    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mPresenter = Utils.checkNotNull(presenter);
    }


    @OnClick({R.id.iv_return, R.id.fab, R.id.tv_more_button, R.id.tv_comment_count, R.id.like_iv, R.id.favorite})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_return:
                finish();
                break;

            case  R.id.fab:
                Intent fabIntent = new Intent(MenuViewActivity.this, DeviceSelectActivity.class);
                fabIntent.putExtra("_id", recipesBeanID);
                startActivity(fabIntent);
                break;

            case R.id.tv_more_button:

                break;

            case R.id.like_iv:
                mPresenter.like();
                break;

            case R.id.tv_comment_count:
                Intent intent = new Intent(MenuViewActivity.this, CommentListActivity.class);
                intent.putExtra("_id", recipesBeanID);
                startActivity(intent);
                break;

            case R.id.favorite:
                mPresenter.favorite();
                break;




        }

    }

    @Override
    public void settingData(Recipes.RecipesBean recipesBean) {
//        tvTitle.setText(recipesBean.getName());
        //      tvMenuName.setText(recipesBean.getName());
        tvUserName.setText(recipesBean.getOwner().getOwnerUid().getName());
        userHeadUrl = recipesBean.getDetail().getImgSrc();
        ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + userHeadUrl, ivCover, this, false);
        ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + recipesBean.getOwner().getOwnerUid().getHeadUrl(), ivUserHead, this, true);
        tvMenuNote.setText(recipesBean.getDetail().getDescribe());


        if (recipesBean.getComment().size() == 0) {
            tvCommentCount.setText(String.format("评论"));
        } else
            tvCommentCount.setText(String.format("%s条评论", recipesBean.getComment().size()));

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onIsOwner(boolean o) {
        //是否隐藏点赞按键
        if (o)
            mTvMoreButton.setVisibility(View.INVISIBLE);
        else mTvMoreButton.setVisibility(View.VISIBLE);

    }

    @Override
    public void onIsLike(boolean o) {
        //根据是否点赞去设置点赞按键样式
        if (o)
            mTvMoreButton.setText(getString(R.string.liking));
        else
            mTvMoreButton.setText(getString(R.string.like));
        mPresenter.reLike(recipesBeanID);
    }

    @Override
    public void setLikeList(List<Like> likeList) {
        likes = likeList;
    }


}

