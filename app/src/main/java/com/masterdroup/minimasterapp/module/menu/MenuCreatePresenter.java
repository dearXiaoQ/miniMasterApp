package com.masterdroup.minimasterapp.module.menu;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.api.Network;
import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.CookingStep;
import com.masterdroup.minimasterapp.model.DescribeStep;
import com.masterdroup.minimasterapp.model.Food;
import com.masterdroup.minimasterapp.model.MenuID;
import com.masterdroup.minimasterapp.model.MenuPicture;
import com.masterdroup.minimasterapp.model.Recipes;
import com.masterdroup.minimasterapp.model.Url;
import com.masterdroup.minimasterapp.module.progress.ProgressSubscriber;
import com.masterdroup.minimasterapp.util.JxUtils;
import com.masterdroup.minimasterapp.view.MenuCookingStepRVAdapter;
import com.masterdroup.minimasterapp.view.MenuStepRVAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.internal.util.ActionSubscriber;
import rx.schedulers.Schedulers;

/**
 * Created by 11473 on 2017/2/22.
 */

public class MenuCreatePresenter implements Contract.MenuCreatePresenter {
    Contract.MenuCreateView menuCreateView;
    Context mContext;

    Observable o_uploadFile, o_uploadFile_step, o_uploadFile_cooking_step, o_upDate, o_delete, o_userInfo;
    Subscriber s_uploadFile, s_uploadFile_step, s_uploadFile_cooking_step, s_upDate, s_delete, s_userInfo;


    public MenuCreatePresenter(Contract.MenuCreateView menuCreateView) {
        this.menuCreateView = menuCreateView;
        menuCreateView.setPresenter(this);

    }

    @Override
    public void submitNewMenu() {

        //// TODO: 2017/3/7 分别保存&提交封面图片,准备步骤,烹饪步骤的本地图片url


        //提交封面图片
        if (FileUtils.isFileExists(menuCreateView.getMenuCoverLocalUrl())) {

            File file = new File(menuCreateView.getMenuCoverLocalUrl());
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestBody);
            o_uploadFile = Network.getMainApi().uploadFile(body);//上传图片接口
        } else {

            ToastUtils.showShortToast("请添加封面");
            return;
        }

        MultipartBody.Builder builder = new MultipartBody.Builder();
        MultipartBody.Builder builder2 = new MultipartBody.Builder();
        List<File> files_dstep = new ArrayList<>();
        List<File> files_cooking_step = new ArrayList<>();

        for (DescribeStep dStep : mSteps) {
            if (FileUtils.isFileExists(dStep.getPicture_url())) {
                File file = new File(dStep.getPicture_url());
                files_dstep.add(file);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
                builder.addFormDataPart("picture", file.getName(), requestBody);
            }
        }

        if (files_dstep.size() != 0) {
            builder.setType(MultipartBody.FORM);
            MultipartBody multipartBody = builder.build();
            o_uploadFile_step = Network.getMainApi().uploadFiles(multipartBody);//上传图片接口
        } else if (files_dstep.size() != mSteps.size()) {
            ToastUtils.showShortToast("请添加准备步骤的步骤图");
            return;
        }


        for (CookingStep cStep : mCookingSteps) {
            if (FileUtils.isFileExists(cStep.getPicture_url())) {
                File file = new File(cStep.getPicture_url());
                files_cooking_step.add(file);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
                builder2.addFormDataPart("picture", file.getName(), requestBody);
            }
        }

        if (files_cooking_step.size() != 0) {
            builder2.setType(MultipartBody.FORM);
            MultipartBody multipartBody = builder2.build();

            o_uploadFile_cooking_step = Network.getMainApi().uploadFiles(multipartBody);//上传图片接口

        } else if (files_cooking_step.size() != mCookingSteps.size()) {
            ToastUtils.showShortToast("请添加烹饪步骤的步骤图");
            return;
        }

        Observable all = Observable.zip(o_uploadFile, o_uploadFile_step, o_uploadFile_cooking_step, new Func3<Base<Url>, Base<Url>, Base<Url>, MenuPicture>() {
            @Override
            public MenuPicture call(Base<Url> o, Base<Url> o2, Base<Url> o3) {
                return addBase3(o, o2, o3);
            }
        });


        Subscriber all_s = new ProgressSubscriber<>(new ProgressSubscriber.SubscriberOnNextListener<MenuPicture>() {
            @Override
            public void onNext(MenuPicture mp) {
                if (mp.getErrorCode() == 0) {
                    menuCreateView.setMenuCoverServerUrl(mp.getMenuCover());

                    for (int i = 0; i < mp.getSetpPicture().size(); i++) {
                        mSteps.get(i).setImgSrc(mp.getSetpPicture().get(i));
                    }
                    for (int i = 0; i < mp.getCookingSetpPicture().size(); i++) {
                        mCookingSteps.get(i).setImgSrc(mp.getCookingSetpPicture().get(i));
                    }

                    createRecipes();
                } else {
                    Toast.makeText(mContext, "上传图片失败！", Toast.LENGTH_SHORT).show();
                }
            }

        }, mContext);

        JxUtils.toSubscribe(all, all_s);

    }


    MenuPicture addBase3(Base<Url> o, Base<Url> o2, Base<Url> o3) {
        MenuPicture mp = new MenuPicture();

        mp.setMenuCover(o.getRes().getUrl().get(0));
        mp.setSetpPicture(o2.getRes().getUrl());
        mp.setCookingSetpPicture(o3.getRes().getUrl());

        int i = (o.getErrorCode() == 0 && o2.getErrorCode() == 0 && o3.getErrorCode() == 0) ? 0 : 1;
        mp.setErrorCode(i);

        return mp;
    }


    private void createRecipes() {
        Observable o_submit = Network.getMainApi().createRecipes(getRecipesDate());
        Subscriber s_submit = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<MenuID>>() {
            @Override
            public void onNext(Base<MenuID> o) {

                if (o.getErrorCode() == 0) {
                    ToastUtils.showShortToast("提交成功");
                    ((Activity) mContext).finish();
                } else
                    ToastUtils.showShortToast("提交失败");

            }
        }, menuCreateView.getContext());
        JxUtils.toSubscribe(o_submit, s_submit);
    }

    public Recipes getRecipesDate() {

        Recipes recipes = new Recipes();
        Recipes.RecipesBean bean = recipes.new RecipesBean();
        bean.setName(menuCreateView.getMenuName());
        Recipes.RecipesBean.Detail detail = bean.new Detail();
        detail.setDescribe(menuCreateView.getMenuDescribe());//菜谱简介
        detail.setImgSrc(menuCreateView.getMenuCoverServerUrl());//菜谱cover
        bean.setDetail(detail);
        bean.setDescribeSteps(mSteps);
        bean.setCookingStep(mCookingSteps);

//        List<Food> foods = new ArrayList<>();
//
//        Food food1 = new Food();
//        food1.setAmount(100);
//        food1.setFoodType("白糖");
//        foods.add(food1);
//
//        Food food2 = new Food();
//        food2.setAmount(22100);
//        food2.setFoodType("白糖");
//        foods.add(food2);
//
//        Food food3 = new Food();
//        food3.setAmount(130);
//        food3.setFoodType("白糖");
//        foods.add(food3);

        bean.setFoodList(mFoods);
        recipes.setRecipesBean(bean);
        return recipes;
    }


    MenuStepRVAdapter adapter;
    MenuCookingStepRVAdapter c_adapter;
    FoodsAdapter f_adapter;

    int step_number = 1;//当前总步骤数 默认为1
    int cooking_step_number = 1;//当前烹饪总步骤数 默认为1
    int food_list_number = 1;//食材列表 默认为1

    public static List<DescribeStep> mSteps = new ArrayList<>();
    public static List<CookingStep> mCookingSteps = new ArrayList<>();
    public static List<Food> mFoods = new ArrayList<>();

    @Override
    public void initStepRecyclerView(RecyclerView rv, RecyclerView rv_cooking, RecyclerView rv_food) {
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        rv.setItemAnimator(new DefaultItemAnimator());
        for (int i = 1; i <= step_number; i++) {
            DescribeStep step = new DescribeStep();
            step.setStepNo(i);
            step.setResultCode(i);
            mSteps.add(step);
        }
        adapter = new MenuStepRVAdapter(mContext);
        rv.setAdapter(adapter);
        rv.setNestedScrollingEnabled(false);


        //烹饪步骤rv
        rv_cooking.setLayoutManager(new LinearLayoutManager(mContext));
        rv_cooking.setItemAnimator(new DefaultItemAnimator());
        for (int i = 1; i <= cooking_step_number; i++) {
            CookingStep step = new CookingStep();
            step.setStepNo(i);
            step.setResultCode(i + 100);
            mCookingSteps.add(step);
        }
        c_adapter = new MenuCookingStepRVAdapter(mContext);
        rv_cooking.setAdapter(c_adapter);
        rv_cooking.setNestedScrollingEnabled(false);


        //// TODO: 2017/3/10 烹饪用料
        rv_food.setLayoutManager(new LinearLayoutManager(mContext));
        rv_food.setItemAnimator(new DefaultItemAnimator());
        for (int i = 0; i < food_list_number; i++) {
            mFoods.add(new Food());
        }
        f_adapter = new FoodsAdapter(mContext);
        rv_food.setAdapter(f_adapter);
        rv_food.setNestedScrollingEnabled(false);


    }

    @Override
    public void addStep() {
        step_number++;
        DescribeStep step = new DescribeStep();
        step.setStepNo(step_number);
        step.setResultCode(step_number);
        mSteps.add(step);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void addCookingStep() {
        cooking_step_number++;
        CookingStep step = new CookingStep();
        step.setStepNo(cooking_step_number);
        step.setResultCode(cooking_step_number + 100);
        mCookingSteps.add(step);
        c_adapter.notifyDataSetChanged();
    }

    @Override
    public void addFood() {

        food_list_number++;
        Food f = new Food();
        mFoods.add(f);
        f_adapter.notifyDataSetChanged();

    }

    @Override
    public void setStepPicture(String url, int requestCode) {
        //大于100表示是烹饪步骤
        if (requestCode < 100) {
            for (DescribeStep step : mSteps) {
                if (step.getResultCode() == requestCode) {
                    step.setPicture_url(url);
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            for (CookingStep step : mCookingSteps) {
                if (step.getResultCode() == requestCode) {
                    step.setPicture_url(url);
                    c_adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void initDescribeStep() {
        mSteps = new ArrayList<>(step_number);
        mCookingSteps = new ArrayList<>(cooking_step_number);
        mFoods = new ArrayList<>(food_list_number);
    }

    @Override
    public void start() {
        mContext = menuCreateView.getContext();
    }


    class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.FoodsHolder> {
        LayoutInflater layoutInflater;
        Context context;

        public FoodsAdapter(Context context) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public FoodsAdapter.FoodsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FoodsAdapter.FoodsHolder(layoutInflater.inflate(R.layout.view_menu_food_item, parent, false));

        }

        @Override
        public void onBindViewHolder(FoodsAdapter.FoodsHolder holder, final int position) {

            holder.mTvFoodType.setText(mFoods.get(position).getFoodType());
            holder.mTvAmount.setText(String.valueOf(mFoods.get(position).getAmount()));

            //1、为了避免TextWatcher在第2步被调用，提前将他移除。
            if ((holder).mTvFoodType.getTag() instanceof TextWatcher) {
                (holder).mTvFoodType.removeTextChangedListener((TextWatcher) ((holder).mTvFoodType.getTag()));
            }

            // 第2步：移除TextWatcher之后，设置EditText的Text。
            (holder).mTvFoodType.setText(MenuCreatePresenter.mFoods.get(position).getFoodType());


            TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {


                    MenuCreatePresenter.mFoods.get(position).setFoodType(editable.toString());
                }
            };

            (holder).mTvFoodType.addTextChangedListener(watcher);
            (holder).mTvFoodType.setTag(watcher);


            //1、为了避免TextWatcher在第2步被调用，提前将他移除。
            if ((holder).mTvAmount.getTag() instanceof TextWatcher) {
                (holder).mTvAmount.removeTextChangedListener((TextWatcher) ((holder).mTvAmount.getTag()));
            }

            // 第2步：移除TextWatcher之后，设置EditText的Text。
            if (MenuCreatePresenter.mFoods.get(position).getAmount() != 0)
                (holder).mTvAmount.setText(String.valueOf(MenuCreatePresenter.mFoods.get(position).getAmount()));
            else
                (holder).mTvAmount.setText("");


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
                            MenuCreatePresenter.mFoods.get(position).setAmount(temperature);

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        ToastUtils.showShortToast("请输入正确的数值！");
                    }
                }
            };

            (holder).mTvAmount.addTextChangedListener(watcher2);
            (holder).mTvAmount.setTag(watcher2);

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
}
