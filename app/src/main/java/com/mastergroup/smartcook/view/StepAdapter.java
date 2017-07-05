package com.mastergroup.smartcook.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.model.DescribeStep;
import com.mastergroup.smartcook.util.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 11473 on 2016/12/23.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {
    LayoutInflater layoutInflater;
    Context context;
    List<DescribeStep> list;


    public StepAdapter(Context context, List<DescribeStep> list) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StepHolder(layoutInflater.inflate(R.layout.view_menu_step_show_item, parent, false));

    }

    @Override
    public void onBindViewHolder(StepHolder holder, int position) {

        DescribeStep step = list.get(position);
        holder.tvStepNo.append(step.getStepNo() + "");
        holder.tvDec.setText(step.getDescribe());
        ImageLoader.getInstance().displayGlideImage(step.getImgSrc(), holder.ivPicture, context, false);

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class StepHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_step_no)
        TextView tvStepNo;
        @Bind(R.id.iv_picture)
        ImageView ivPicture;
        @Bind(R.id.tv_dec)
        TextView tvDec;

        StepHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
