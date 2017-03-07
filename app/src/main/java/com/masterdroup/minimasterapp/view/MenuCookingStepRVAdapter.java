package com.masterdroup.minimasterapp.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextClock;
import android.widget.TextView;

import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.module.menu.MenuCreatePresenter;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.masterdroup.minimasterapp.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 11473 on 2017/2/24.
 */

public class MenuCookingStepRVAdapter extends RecyclerView.Adapter<MenuCookingStepRVAdapter.ViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public MenuCookingStepRVAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.view_menu_cooking_step_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        holder.mTvStepNo.setText("步骤 " + MenuCreatePresenter.mCookingSteps.get(position).getStepNo());
        ImageLoader.getInstance().displayGlideImage(MenuCreatePresenter.mCookingSteps.get(position).getPicture_url(), holder.mIvPicture, mContext, false);

        holder.mIvPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openImageSelector(mContext, MenuCreatePresenter.mCookingSteps.get(position).getResultCode());
            }
        });

        //1、为了避免TextWatcher在第2步被调用，提前将他移除。
        if ((holder).mTvDec.getTag() instanceof TextWatcher) {
            (holder).mTvDec.removeTextChangedListener((TextWatcher) ((holder).mTvDec.getTag()));
        }

        // 第2步：移除TextWatcher之后，设置EditText的Text。
        (holder).mTvDec.setText(MenuCreatePresenter.mCookingSteps.get(position).getDescribe());


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                MenuCreatePresenter.mCookingSteps.get(position).setDescribe(editable.toString());
            }
        };

        (holder).mTvDec.addTextChangedListener(watcher);
        (holder).mTvDec.setTag(watcher);
//
//        int i = 0;
//        if (holder.mRbFire1.isChecked())
//            i = 1;
//        else if (holder.mRbFire2.isChecked())
//            i = 2;
//        else if (holder.mRbFire3.isChecked())
//            i = 3;
//        MenuCreatePresenter.mCookingSteps.get(position).setPower(i);
//
//        MenuCreatePresenter.mCookingSteps.get(position).setTemperature(holder.mPbTemperature.getProgress());

    }

    @Override
    public int getItemCount() {
        return MenuCreatePresenter.mCookingSteps == null ? 0 : MenuCreatePresenter.mCookingSteps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_step_no)
        TextView mTvStepNo;
        @Bind(R.id.iv_picture)
        ImageView mIvPicture;
        @Bind(R.id.tv_dec)
        EditText mTvDec;
        @Bind(R.id.rb_fire1)
        RadioButton mRbFire1;
        @Bind(R.id.rb_fire2)
        RadioButton mRbFire2;
        @Bind(R.id.rb_fire3)
        RadioButton mRbFire3;
        @Bind(R.id.pb_temperature)
        ProgressBar mPbTemperature;
        @Bind(R.id.textClock)
        TextClock mTextClock;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
