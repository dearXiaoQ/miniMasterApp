package com.mastergroup.smartcook.module.menu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mastergroup.smartcook.Constant;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.model.CollectionRecipes;
import com.mastergroup.smartcook.util.ImageLoader;
import com.mastergroup.smartcook.util.ToastUtils;
import com.mastergroup.smartcook.util.Utils;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavoriteListActivity extends Activity implements Contract.FavoriteListView {


    @Bind(R.id.iv_return)
    ImageView ivReturn;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_more_button)
    TextView tvMoreButton;
    @Bind(R.id.rv)
    PullLoadMoreRecyclerView rv;

    Contract.Presenter mPresenter;
    Context mContext;
    List<CollectionRecipes.RecipesBean> recipesBeans = new ArrayList<>();

    FavoriteListRvAdapter favoriteListRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu_list);
        mContext = this;
        ButterKnife.bind(this);
        new MenuPresenter(this);
        mPresenter.start();
        initView();
        initData();
    }

    private void initData() {
        mPresenter.getFavoriteRecipes();
    }

    private void initView() {
        tvTitle.setText(R.string.my_favorite_list);
        tvMoreButton.setVisibility(View.GONE);

        rv.setLinearLayout();
        favoriteListRvAdapter = new FavoriteListRvAdapter();
        rv.setAdapter(favoriteListRvAdapter);


        rv.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                mPresenter.getFavoriteRecipes();
            }

            @Override
            public void onLoadMore() {
                mPresenter.getFavoriteRecipes();
            }
        });
    }



    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mPresenter = Utils.checkNotNull(presenter);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onGetMyFavoriteListSuccess(List<CollectionRecipes.RecipesBean> recipesBeenList) {
        this.recipesBeans = recipesBeenList;
        favoriteListRvAdapter.notifyDataSetChanged();
        rv.setPullLoadMoreCompleted();
    }

    @Override
    public void onGetMyFavoriteListFailure(String info) {
        ToastUtils.showCustomToast(mContext, ToastUtils.TOAST_BOTTOM, info);
    }

    class FavoriteListRvAdapter extends RecyclerView.Adapter<FavoriteListRvHolder> {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        @Override
        public FavoriteListRvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FavoriteListRvHolder(layoutInflater.inflate(R.layout.collection_lv_item, parent, false));
        }

        @Override
        public void onBindViewHolder(FavoriteListRvHolder holder, int position) {

            final CollectionRecipes.RecipesBean recipesBean = recipesBeans.get(position);
            ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + recipesBean.getDetail().getImgSrc(), holder.menuIv, mContext, false);
            holder.titleTv.setText(recipesBean.getName());
            holder.favoriteNumTv.setText(recipesBean.getFavorites().size() + "");
            holder.timeTv.setText(recipesBean.getDate());

        }

        @Override
        public int getItemCount() {
            return recipesBeans.size();
        }
    }

    class FavoriteListRvHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.menu_iv)
        ImageView menuIv;
        @Bind(R.id.imageView4)
        ImageView imageView4;
        @Bind(R.id.title_tv)
        TextView titleTv;
        @Bind(R.id.time_tv)
        TextView timeTv;
        @Bind(R.id.favorite_num_tv)
        TextView favoriteNumTv;
        @Bind(R.id.linearLayout)
        LinearLayout linearLayout;
        @Bind(R.id.rl)
        RelativeLayout rl;

        public FavoriteListRvHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @OnClick({R.id.iv_return})
    public void onClick(View v){
        switch (v.getId()) {

            case R.id.iv_return:
                this.finish();
                break;

        }
    }

}
