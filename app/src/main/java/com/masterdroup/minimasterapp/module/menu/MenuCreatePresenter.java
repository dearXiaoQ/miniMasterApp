package com.masterdroup.minimasterapp.module.menu;


import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.blankj.utilcode.utils.FileUtils;
import com.masterdroup.minimasterapp.api.Network;
import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.DescribeStep;
import com.masterdroup.minimasterapp.model.Recipes;
import com.masterdroup.minimasterapp.model.Url;
import com.masterdroup.minimasterapp.module.progress.ProgressSubscriber;
import com.masterdroup.minimasterapp.util.JxUtils;
import com.masterdroup.minimasterapp.view.MenuStepRVAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by 11473 on 2017/2/22.
 */

public class MenuCreatePresenter implements Contract.MenuCreatePresenter {
    Contract.MenuCreateView menuCreateView;
    Context mContext;

    Observable o_uploadFile, o_upDate, o_delete, o_userInfo;
    Subscriber s_uploadFile, s_upDate, s_delete, s_userInfo;


    public MenuCreatePresenter(Contract.MenuCreateView menuCreateView) {
        this.menuCreateView = menuCreateView;
        menuCreateView.setPresenter(this);

    }

    @Override
    public void submitNewMenu() {


        List<String> pic_local_urls = new ArrayList<>();//本地图片路径  第一张为封面 其余为步骤图
        pic_local_urls.add(0, menuCreateView.getMenuCoverLocalUrl());//
        for (DescribeStep step : mSteps) {
            pic_local_urls.add(step.getPicture_url());
        }
        MultipartBody.Builder builder = new MultipartBody.Builder();

        List<File> files = new ArrayList<>();

        for (String pic : pic_local_urls) {
            if (FileUtils.isFileExists(pic)) {
                File file = new File(pic);
                files.add(file);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
                builder.addFormDataPart("picture", file.getName(), requestBody);
            }
        }
        if (files.size() != 0) {

            builder.setType(MultipartBody.FORM);
            MultipartBody multipartBody = builder.build();

            o_uploadFile = Network.getMainApi().uploadFiles(multipartBody);//上传图片接口
            s_uploadFile = new ProgressSubscriber<>(new ProgressSubscriber.SubscriberOnNextListener<Base<Url>>() {
                @Override
                public void onNext(Base<Url> urlBase) {
                    if (urlBase.getErrorCode() == 0) {
                        List<String> list = new ArrayList<>();
                        list = urlBase.getRes().getUrl();
                        menuCreateView.setMenuCoverServerUrl(list.get(0));//第一张为封面
                        list.remove(0);
                        for (int i = 0; i < list.size(); i++) {
                            mSteps.get(i).setImgSrc(list.get(i));
                        }

                        createRecipes();
                    } else {
                        Toast.makeText(mContext, "上传图片失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }, mContext);

            JxUtils.toSubscribe(o_uploadFile, s_uploadFile);
        } else {
            createRecipes();
        }

    }

    private void createRecipes() {
        Observable o_submit = Network.getMainApi().createRecipes(getRecipesDate());
        Subscriber s_submit = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<String>>() {
            @Override
            public void onNext(Base<String> o) {

                if (o.getErrorCode() == 0) {

                }

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


        recipes.setRecipesBean(bean);


        return recipes;
    }


    MenuStepRVAdapter adapter;

    int step_number = 3;//当前总步骤数 默认为1
    public static List<DescribeStep> mSteps = new ArrayList<>();

    @Override
    public void initStepRecyclerView(RecyclerView rv) {
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
    public void setStepPicture(String url, int requestCode) {
        for (DescribeStep step : mSteps) {
            if (step.getResultCode() == requestCode) {
                step.setPicture_url(url);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void initDescribeStep() {
        mSteps = new ArrayList<>(step_number);
    }

    @Override
    public void start() {
        mContext = menuCreateView.getContext();
    }
}
