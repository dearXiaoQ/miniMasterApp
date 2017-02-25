package com.masterdroup.minimasterapp.module.menu;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.blankj.utilcode.utils.FileUtils;
import com.masterdroup.minimasterapp.api.Network;
import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.Step;
import com.masterdroup.minimasterapp.model.Url;
import com.masterdroup.minimasterapp.model.User;
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

        for (final Step step : mSteps) {
            String localUrl = step.getPicture_url();
            if (FileUtils.isFileExists(localUrl)) {
                File file = new File(localUrl);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestBody);
                o_uploadFile = Network.getMainApi().uploadFile(body);//上传图片接口


                s_uploadFile = new ProgressSubscriber<>(new ProgressSubscriber.SubscriberOnNextListener<Base<Url>>() {
                    @Override
                    public void onNext(Base<Url> urlBase) {
                        if (urlBase.getErrorCode() == 0) {
                            step.setPicture_server_url(urlBase.getRes().getUrl());
                        } else {
                            Toast.makeText(mContext, "上传图片失败！", Toast.LENGTH_SHORT).show();
                        }
                    }


                }, mContext);


            }

            JxUtils.toSubscribe(o_uploadFile,s_uploadFile);
        }


//        s_uploadFile = new ProgressSubscriber<>(new ProgressSubscriber.SubscriberOnNextListener<Base<Url>>() {
//            @Override
//            public void onNext(Base<Url> urlBase) {
//                if (urlBase.getErrorCode() == 0) {
//                    step.setPicture_server_url(urlBase.getRes().getUrl());
//                } else {
//                    Toast.makeText(mContext, "上传图片失败！", Toast.LENGTH_SHORT).show();
//                }
//            }
//              JxUtils.toSubscribe(o_submit, s_submit)
//        }, mContext);


//        Observable o_submit = Network.getMainApi().createRecipes(menuCreateView.getRecipesDate());
//        Subscriber s_submit = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<String>>() {
//            @Override
//            public void onNext(Base<String> o) {
//
//                if (o.getErrorCode() == 0) {
//
//                }
//
//            }
//        }, menuCreateView.getContext());
//        JxUtils.toSubscribe(o_submit, s_submit);
    }


    List<Step> mSteps = new ArrayList<>();

    MenuStepRVAdapter adapter;

    static int step_number = 1;//当前总步骤数 默认为1


    @Override
    public void initStepRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(mContext));

        Step step = new Step();
        step.setStepNo(step_number);
        step.setResultCode(step_number);
        mSteps.add(step);
        adapter = new MenuStepRVAdapter(mContext, mSteps);
        rv.setAdapter(adapter);
        rv.setNestedScrollingEnabled(false);
    }

    @Override
    public void addStep() {
        step_number++;

        Step step = new Step();
        step.setStepNo(step_number);
        step.setResultCode(step_number);
        mSteps.add(step);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void setStepPicture(String url, int requestCode) {
        for (Step step : mSteps) {
            if (step.getResultCode() == requestCode) {
                step.setPicture_url(url);
                adapter.notifyDataSetChanged();

            }
        }

    }

    @Override
    public void start() {
        mContext = menuCreateView.getContext();
    }
}
