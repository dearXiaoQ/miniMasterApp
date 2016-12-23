package com.masterdroup.minimasterapp.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.masterdroup.minimasterapp.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 11473 on 2016/12/23.
 */

public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.FoodsHolder> {
    LayoutInflater layoutInflater;
    Context context;
    List<String> list;


    public FoodsAdapter(Context context, List<String> strings) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.list = strings;
    }

    @Override
    public FoodsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FoodsHolder(layoutInflater.inflate(R.layout.view_menu_food_item, parent, false));

    }

    @Override
    public void onBindViewHolder(FoodsHolder holder, int position) {

        holder.tvFood.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class FoodsHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_food)
        TextView tvFood;

        FoodsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
