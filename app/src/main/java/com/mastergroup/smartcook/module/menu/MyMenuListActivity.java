package com.mastergroup.smartcook.module.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mastergroup.smartcook.Constant;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.model.CollectionRecipes;
import com.mastergroup.smartcook.util.ImageLoader;
import com.mastergroup.smartcook.util.Utils;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的菜谱
 */
public class MyMenuListActivity extends Activity implements Contract.MyMenuListView {

    MenuPresenter menuPresenter;

    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_more_button)
    TextView mTvMoreButton;
    /*  @Bind(R.id.rv)
      RecyclerView mRv;*/
    @Bind(R.id.rv)
    PullLoadMoreRecyclerView mRv;
    List<CollectionRecipes.RecipesBean> recipesBeans = new ArrayList<>();


    Contract.Presenter mPresenter;
    Context mContext;
    MyMenuListRvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu_list);
        ButterKnife.bind(this);
        new MenuPresenter(this);
        mContext = this;
        mPresenter.start();
        initView();
        initData();
    }

    void initView() {
//        adapter = new RecipesBeansAdapter(R.layout.view_menu_item, RecipesBeans);
        mTvTitle.setText("我的菜谱");
        adapter = new MyMenuListRvAdapter();
        mTvMoreButton.setVisibility(View.GONE);
        mRv.setAdapter(adapter);
        mRv.setLinearLayout();
        mRv.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                mPresenter.getMyMenu(0, recipesBeans.size());
            }

            @Override
            public void onLoadMore() {
                mPresenter.getMyMenu(recipesBeans.size(), 10);
            }
        });
    }

    private void initData() {
        mPresenter.getMyMenu(recipesBeans.size(), 10);
    }

    @OnClick(R.id.iv_return)
    public void onClick() {
        finish();
    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mPresenter = Utils.checkNotNull(presenter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onGetMyMenuSuccess(List<CollectionRecipes.RecipesBean> recipesBeanList) {
        for (CollectionRecipes.RecipesBean bean : recipesBeanList) {
            int size = recipesBeans.size();
            for(int i = 0; i < size; i++) {
                if(recipesBeans.get(i).getName().equals(bean.getName())) {
                    recipesBeans.remove(i);
                    break;
                }
            }
            recipesBeans.add(bean);
        }
        adapter.notifyDataSetChanged();
        mRv.setPullLoadMoreCompleted();
    }


    class MyMenuListRvAdapter extends RecyclerView.Adapter<MyMenuListRvHolder> {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        @Override
        public MyMenuListRvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyMenuListRvHolder(layoutInflater.inflate(R.layout.collection_lv_item, parent, false));
        }

        @Override
        public void onBindViewHolder(MyMenuListRvHolder holder, int position) {

          //  holder.menuIv
            final CollectionRecipes.RecipesBean  recipesBean = recipesBeans.get(position);
            ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + recipesBean.getDetail().getImgSrc(), holder.menuIv, mContext, false);
            holder.titleTv.setText(recipesBean.getName());
            //holder.timeTv;
            holder.favoriteNumTv.setText(recipesBean.getFavorites().size() + "");
            if(recipesBean.getFavorites().size() > 0) {
                holder.timeTv.setText(recipesBean.getFavorites().get(0).getDate());
            }
            holder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, MenuViewActivity.class).putExtra("_id", recipesBean.get_id()));
                }
            });

        }

        @Override
        public int getItemCount() {
            return recipesBeans.size();
        }
    }


    class MyMenuListRvHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.menu_iv)
        ImageView menuIv;
        @Bind(R.id.title_tv)
        TextView titleTv;
        @Bind(R.id.time_tv)
        TextView timeTv;
        @Bind(R.id.favorite_num_tv)
        TextView favoriteNumTv;
        @Bind(R.id.rl)
        RelativeLayout rl;

        public MyMenuListRvHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}


