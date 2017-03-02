package com.masterdroup.minimasterapp.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.masterdroup.minimasterapp.Constant;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.model.Recipes;
import com.masterdroup.minimasterapp.module.menu.MenuPresenter;
import com.masterdroup.minimasterapp.util.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 11473 on 2017/3/2.
 */

public class MenuListRVAdapter extends RecyclerView.Adapter<MenuListRVAdapter.MenuListRVHolder> {

    LayoutInflater layoutInflater;
    Context context;
    List<Recipes.RecipesBean> mRecipesBeanList;


    public MenuListRVAdapter(Context context, List<Recipes.RecipesBean> list) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        mRecipesBeanList = list;
    }

    @Override
    public MenuListRVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MenuListRVHolder(layoutInflater.inflate(R.layout.view_menu_item, parent, false));

    }

    @Override
    public void onBindViewHolder(MenuListRVHolder holder, int position) {


//        Recipes.RecipesBean recipesBean = mRecipesBeanList.get(position);
//        holder.score.setText(menu.getScore());
        holder.menu_name.setText(MenuPresenter.list.get(position).getName());
        holder.user_name.setText(MenuPresenter.list.get(position).getOwner().getOwnerUid().getName());
        ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + MenuPresenter.list.get(position).getDetail().getImgSrc(), holder.iv_cover, context, false);
        ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + MenuPresenter.list.get(position).getOwner().getOwnerUid().getHeadUrl(), holder.iv_head, context, true);
        holder.iv_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                context.startActivity(new Intent(context, MenuViewActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return MenuPresenter.list == null ? 0 : MenuPresenter.list.size();
    }

    class MenuListRVHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_menu_score)
        TextView score;
        @Bind(R.id.tv_menu_name)
        TextView menu_name;
        @Bind(R.id.tv_menu_user_name)
        TextView user_name;
        @Bind(R.id.iv_cover)
        ImageView iv_cover;
        @Bind(R.id.iv_head)
        ImageView iv_head;

        MenuListRVHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
