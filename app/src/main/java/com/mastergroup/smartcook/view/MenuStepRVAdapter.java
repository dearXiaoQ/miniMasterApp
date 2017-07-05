package com.mastergroup.smartcook.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.module.menu.MenuCreatePresenter;
import com.mastergroup.smartcook.util.ImageLoader;
import com.mastergroup.smartcook.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 11473 on 2017/2/24.
 */

public class MenuStepRVAdapter extends RecyclerView.Adapter<MenuStepRVAdapter.MenuStepViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public MenuStepRVAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

    }


    @Override
    public MenuStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MenuStepViewHolder(mLayoutInflater.inflate(R.layout.view_menu_step_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MenuStepViewHolder holder, final int position) {


        holder.mTvNo.setText("步骤 " + MenuCreatePresenter.mSteps.get(position).getStepNo());
//        holder.mEtDescribe.setText(MenuCreatePresenter.mSteps.get(position).getDescribe());
        ImageLoader.getInstance().displayGlideImage(MenuCreatePresenter.mSteps.get(position).getPicture_url(), holder.mIvMenuStepPicture, mContext, false);

        holder.mIvMenuStepPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openImageSelector(mContext, MenuCreatePresenter.mSteps.get(position).getResultCode());
            }
        });

        //1、为了避免TextWatcher在第2步被调用，提前将他移除。
        if ((holder).mEtDescribe.getTag() instanceof TextWatcher) {
            (holder).mEtDescribe.removeTextChangedListener((TextWatcher) ((holder).mEtDescribe.getTag()));
        }

        // 第2步：移除TextWatcher之后，设置EditText的Text。
        (holder).mEtDescribe.setText(MenuCreatePresenter.mSteps.get(position).getDescribe());


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                MenuCreatePresenter.mSteps.get(position).setDescribe(editable.toString());
            }
        };

        (holder).mEtDescribe.addTextChangedListener(watcher);
        (holder).mEtDescribe.setTag(watcher);
    }

    @Override
    public int getItemCount() {
        return MenuCreatePresenter.mSteps == null ? 0 : MenuCreatePresenter.mSteps.size();
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
