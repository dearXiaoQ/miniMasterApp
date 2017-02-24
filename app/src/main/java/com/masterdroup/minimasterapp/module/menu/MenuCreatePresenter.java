package com.masterdroup.minimasterapp.module.menu;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.masterdroup.minimasterapp.api.Network;
import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.Step;
import com.masterdroup.minimasterapp.module.progress.ProgressSubscriber;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.masterdroup.minimasterapp.util.JxUtils;
import com.masterdroup.minimasterapp.view.MenuStepRVAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by 11473 on 2017/2/22.
 */

public class MenuCreatePresenter implements Contract.MenuCreatePresenter {
    Contract.MenuCreateView menuCreateView;
    Context mContext;

    public MenuCreatePresenter(Contract.MenuCreateView menuCreateView) {
        this.menuCreateView = menuCreateView;
        menuCreateView.setPresenter(this);

    }

    @Override
    public void submitNewMenu() {
        Observable o_submit = Network.getMainApi().createRecipes(menuCreateView.getRecipesDate());
        Subscriber s_submit = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<String>>() {
            @Override
            public void onNext(Base<String> o) {

                if (o.getErrorCode() == 0) {

                }

            }
        }, menuCreateView.getContext());
        JxUtils.toSubscribe(o_submit, s_submit);
    }


    List<Step> mSteps = new ArrayList<>();

    MenuStepRVAdapter adapter;

    @Override
    public void initStepRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(mContext));

        Step step1 = new Step();
        step1.setStepNo(1);
        step1.setResultCode(1);
        mSteps.add(step1);
        adapter = new MenuStepRVAdapter(mContext, mSteps);
        rv.setAdapter(adapter);
        rv.setNestedScrollingEnabled(false);
    }

    @Override
    public void addStep() {

        Step step1 = new Step();
        step1.setStepNo(2);
        step1.setResultCode(2);
        mSteps.add(step1);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void setStepPicture(String url, int requestCode) {
        for (Step step : mSteps) {
            if (step.getResultCode() == requestCode) {
                step.setPicture_url(url);
                adapter.notifyDataSetChanged();
                //ImageLoader.getInstance().displayGlideImage(url, mIvMenuCover, this, false);
            }
        }

    }

    @Override
    public void start() {
        mContext = menuCreateView.getContext();
    }
}
