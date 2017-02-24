package com.masterdroup.minimasterapp.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.model.Step;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.masterdroup.minimasterapp.util.Utils;
import com.yuyh.library.imgsel.ImgSelActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 11473 on 2017/2/24.
 */

public class MenuStepRVAdapter extends RecyclerView.Adapter<MenuStepRVAdapter.MenuStepViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    List<Step> list;

    public MenuStepRVAdapter(Context context, List<Step> list) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.list = list;
    }


    @Override
    public MenuStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MenuStepViewHolder(mLayoutInflater.inflate(R.layout.view_menu_step_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MenuStepViewHolder holder, int position) {

        final Step step = list.get(position);
        holder.mTvNo.setText("步骤 " + step.getStepNo());
        holder.mEtDescribe.setText(step.getDes());
        ImageLoader.getInstance().displayGlideImage(step.getPicture_url(), holder.mIvMenuStepPicture, mContext, false);

        holder.mIvMenuStepPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openImageSelector(mContext, step.getResultCode());

            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class MenuStepViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_step_no)
        TextView mTvNo;
        @Bind(R.id.iv_picture)
        ImageView mIvMenuStepPicture;
        @Bind(R.id.tv_dec)
        EditText mEtDescribe;

        MenuStepViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
