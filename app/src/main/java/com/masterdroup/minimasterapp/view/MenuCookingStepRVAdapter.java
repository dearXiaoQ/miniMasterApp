package com.masterdroup.minimasterapp.view;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
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


        //1、为了避免TextWatcher在第2步被调用，提前将他移除。
        if ((holder).mEtTemperature.getTag() instanceof TextWatcher) {
            (holder).mEtTemperature.removeTextChangedListener((TextWatcher) ((holder).mEtTemperature.getTag()));
        }

        // 第2步：移除TextWatcher之后，设置EditText的Text。
        (holder).mEtTemperature.setText(String.valueOf(MenuCreatePresenter.mCookingSteps.get(position).getTemperature()));


        TextWatcher watcher1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                int temperature;
                try {
                    temperature = Integer.valueOf(editable.toString());
                    if (temperature > 0)
                        MenuCreatePresenter.mCookingSteps.get(position).setTemperature(temperature);
                    else
                        ToastUtils.showShortToast("请输入正确的温度数值！");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ToastUtils.showShortToast("请输入正确的温度数值！");
                }
            }
        };

        (holder).mEtTemperature.addTextChangedListener(watcher1);
        (holder).mEtTemperature.setTag(watcher1);


        //1、为了避免TextWatcher在第2步被调用，提前将他移除。
        if ((holder).mEtDuration.getTag() instanceof TextWatcher) {
            (holder).mEtDuration.removeTextChangedListener((TextWatcher) ((holder).mEtDuration.getTag()));
        }

        // 第2步：移除TextWatcher之后，设置EditText的Text。
        (holder).mEtDuration.setText(String.valueOf(MenuCreatePresenter.mCookingSteps.get(position).getDuration()));


        TextWatcher watcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                int temperature;
                try {
                    temperature = Integer.valueOf(editable.toString());
                    if (temperature > 0)
                        MenuCreatePresenter.mCookingSteps.get(position).setDuration(temperature);
                    else
                        ToastUtils.showShortToast("请输入正确的时间数值！");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ToastUtils.showShortToast("请输入正确的时间数值！");
                }
            }
        };

        (holder).mEtDuration.addTextChangedListener(watcher2);
        (holder).mEtDuration.setTag(watcher2);


        holder.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int j = 0;
                switch (i) {
                    case R.id.rb_fire1:
                        j = 1;
                        break;
                    case R.id.rb_fire2:
                        j = 2;
                        break;
                    case R.id.rb_fire3:
                        j = 3;
                        break;
                }

                MenuCreatePresenter.mCookingSteps.get(position).setPower(j);

            }
        });


//
//        int i = 0;
//        if (holder.mRbFire1.isChecked())
//            i = 1;
//        else if (holder.mRbFire2.isChecked())
//            i = 2;
//        else if (holder.mRbFire3.isChecked())
//            i = 3;
//        MenuCreatePresenter.mCookingSteps.get(position).setPower(i);


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
        @Bind(R.id.et_temperature)
        EditText mEtTemperature;
        @Bind(R.id.et_duration)
        EditText mEtDuration;
        @Bind(R.id.rg)
        RadioGroup rg;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
