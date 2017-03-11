package com.masterdroup.minimasterapp.module.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.masterdroup.minimasterapp.util.Utils;
import com.yuyh.library.imgsel.ImgSelActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuCreateActivity extends Activity implements Contract.MenuCreateView {

    Contract.MenuCreatePresenter mMenuCreatePresenter;
    @Bind(R.id.iv_return)
    ImageView mIvReturn;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_more_button)
    TextView mTvMoreButton;
    @Bind(R.id.iv_menu_cover)
    ImageView mIvMenuCover;
    @Bind(R.id.et_menu_name)
    EditText mEtMenuName;
    @Bind(R.id.et_menu_describe)
    EditText mEtMenuDescribe;


    @Bind(R.id.rv_menu_step)
    RecyclerView mRvMenuStep;

    @Bind(R.id.tv_add_step)
    TextView mTvAddStep;
    @Bind(R.id.rv_menu_cooking_step)
    RecyclerView mRvMenuCookingStep;

    @Bind(R.id.tv_add_cooking_step)
    TextView mTvAddCookingStep;

    @Bind(R.id.rv_food)
    RecyclerView mRvFood;

    @Bind(R.id.tv_add_food)
    TextView mTvAddFood;


    //保存用户选择的菜谱封面的本地路径
    private static String menu_cover_local_url = "";
    //保存上传成功的菜谱封面的服务器路径
    private static String menu_cover_server_url = "";

    private static final int menu_cover_requestCode = 0;//菜谱封面选择requestCode


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_create);
        ButterKnife.bind(this);
        new MenuCreatePresenter(this);
        init();

    }


    private void init() {
        mTvTitle.setText("创建菜谱");
        mMenuCreatePresenter.start();
        mMenuCreatePresenter.initStepRecyclerView(mRvMenuStep, mRvMenuCookingStep, mRvFood);
    }


    @Override
    public void setPresenter(Contract.MenuCreatePresenter presenter) {
        mMenuCreatePresenter = presenter;
    }

    @OnClick({R.id.tv_more_button, R.id.iv_return, R.id.iv_menu_cover, R.id.tv_add_step, R.id.tv_add_cooking_step, R.id.tv_add_food})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_more_button:
                mMenuCreatePresenter.submitNewMenu();
                break;
            case R.id.iv_return:
//                mMenuCreatePresenter.initDescribeStep();
                finish();
                break;
            case R.id.iv_menu_cover:
                Utils.openImageSelector(this, menu_cover_requestCode);
                break;
            case R.id.tv_add_step:
                mMenuCreatePresenter.addStep();
                break;
            case R.id.tv_add_cooking_step:
                mMenuCreatePresenter.addCookingStep();
                break;
            case R.id.tv_add_food:
                mMenuCreatePresenter.addFood();
                break;

        }
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public String getMenuCoverLocalUrl() {
        return menu_cover_local_url;
    }

    @Override
    public void setMenuCoverLocalUrl(String url) {
        menu_cover_local_url = url;
    }

    @Override
    public String getMenuCoverServerUrl() {
        return menu_cover_server_url;
    }

    @Override
    public void setMenuCoverServerUrl(String url) {
        menu_cover_server_url = url;
    }

    @Override
    public String getMenuName() {
        return mEtMenuName.getText().toString();
    }

    @Override
    public String getMenuDescribe() {
        return mEtMenuDescribe.getText().toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (resultCode == RESULT_OK && data != null) {
            String url = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT).get(0);
            switch (requestCode) {
                case menu_cover_requestCode:
                    setMenuCoverLocalUrl(url);
                    ImageLoader.getInstance().displayGlideImage(url, mIvMenuCover, this, false);
                    break;
                default:
                    mMenuCreatePresenter.setStepPicture(url, requestCode);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMenuCreatePresenter.initDescribeStep();
    }
}
