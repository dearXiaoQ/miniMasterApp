package com.masterdroup.minimasterapp.module.menu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.masterdroup.minimasterapp.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuListActivity extends Activity implements Contract.MenuListView {


    @Bind(R.id.rv)
    PullLoadMoreRecyclerView mRv;
    Contract.Presenter presenter;
    @Bind(R.id.tv_more_button)
    TextView mTvMoreButton;
    @Bind(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        ButterKnife.bind(this);
        mTvMoreButton.setVisibility(View.GONE);

        presenter = new MenuPresenter(this);
        presenter.start();

        initView();

    }

    void initView() {
        String title = getIntent().getStringExtra("title");
        mTvTitle.setText(title);

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.initRV(mRv);
        presenter.getRecipesListData();//发起请求
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
