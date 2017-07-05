package com.mastergroup.smartcook.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.model.Food;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 11473 on 2016/12/23.
 */

public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.FoodsHolder> {
    LayoutInflater layoutInflater;
    Context context;
    List<Food> mFoods;

    public FoodsAdapter(Context context, List<Food> mFoods) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.mFoods = mFoods;
    }

    @Override
    public FoodsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FoodsHolder(layoutInflater.inflate(R.layout.view_menu_food_item, parent, false));

    }

    @Override
    public void onBindViewHolder(FoodsHolder holder, int position) {

        holder.mTvFoodType.setText(mFoods.get(position).getFoodType());
        holder.mTvAmount.setText(String.valueOf(mFoods.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return mFoods == null ? 0 : mFoods.size();
    }

    class FoodsHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_food_type)
        TextView mTvFoodType;
        @Bind(R.id.tv_amount)
        TextView mTvAmount;


        FoodsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
