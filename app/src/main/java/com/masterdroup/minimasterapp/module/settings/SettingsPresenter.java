package com.masterdroup.minimasterapp.module.settings;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.masterdroup.minimasterapp.App;
import com.masterdroup.minimasterapp.api.Network;
import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.Null;
import com.masterdroup.minimasterapp.model.Url;
import com.masterdroup.minimasterapp.model.User;
import com.masterdroup.minimasterapp.module.progress.ProgressSubscriber;
import com.masterdroup.minimasterapp.module.welcomeModule.WelcomeActivity;
import com.masterdroup.minimasterapp.util.JxUtils;
import com.masterdroup.minimasterapp.util.Utils;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by 11473 on 2017/2/20.
 */

public class SettingsPresenter implements Contract.Presenter {

    Contract.View mView;

    Observable o_uploadFile, o_upDate, o_delete, o_userInfo;
    Subscriber s_uploadFile, s_upDate, s_delete, s_userInfo;


    public SettingsPresenter(Contract.View mView) {
        this.mView = mView;
        mView = Utils.checkNotNull(mView, " cannot be null!");
        mView.setPresenter(this);
    }


    @Override
    public void upDate(final User user, String headLocalUrl) {


        if (FileUtils.isFileExists(headLocalUrl)) {

            File file = new File(headLocalUrl);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestBody);
            o_uploadFile = Network.getMainApi().uploadFile(body);//上传图片接口

            s_uploadFile = new ProgressSubscriber<>(new ProgressSubscriber.SubscriberOnNextListener<Base<Url>>() {
                @Override
                public void onNext(Base<Url> urlBase) {
                    if (urlBase.getErrorCode() == 0) {
                        Toast.makeText(mView.onGetContext(), "上传图片成功！", Toast.LENGTH_SHORT).show();
                        User.UserBean bean = user.getUser();
                        bean.setHeadUrl(urlBase.getRes().getUrl().get(0));
                        user.setUser(bean);
                        upDate(user);
                        mView.putUserHeadServerUrl(urlBase.getRes().getUrl().get(0));
                    } else {
                        Toast.makeText(mView.onGetContext(), "上传图片失败！", Toast.LENGTH_SHORT).show();
                    }{
                        Toast.makeText(mView.onGetContext(), "上传图片失败！", Toast.LENGTH_SHORT).show();
                    }
                }

            }, mView.onGetContext());
            JxUtils.toSubscribe(o_uploadFile, s_uploadFile);
        } else {
            upDate(user);
        }
    }


    private void upDate(User user) {
        o_upDate = Network.getMainApi().userInfoUpDate(user);
        s_upDate = new ProgressSubscriber<>(new ProgressSubscriber.SubscriberOnNextListener<Base<Null>>() {
            @Override
            public void onNext(Base<Null> o) {
                if (o.getErrorCode() == 0)
                    Toast.makeText(mView.onGetContext(), "个人资料更新成功！", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(mView.onGetContext(), "个人资料更新失败！", Toast.LENGTH_SHORT).show();
                    //// TODO: 2017/2/21 删除已上传的图片
                    deleteServerHeadPictrue(mView.getUserHeadServerUrl());

                }

            }

        }, mView.onGetContext());
        JxUtils.toSubscribe(o_upDate, s_upDate);

    }

    private void deleteServerHeadPictrue(String head_server_url) {

        o_delete = Network.getMainApi().deleteFile(head_server_url);

        s_delete = new ProgressSubscriber<>(new ProgressSubscriber.SubscriberOnNextListener<Base<Null>>() {
            @Override
            public void onNext(Base<Null> urlBase) {
                if (urlBase.getErrorCode() == 0) {
                    LogUtils.d("Network_删除图片文件", "删除图片文件成功！");
                } else
                    LogUtils.d("Network_删除图片文件", "删除图片文件失败！" + urlBase.getMessage());
            }

        }, mView.onGetContext());
        JxUtils.toSubscribe(o_delete, s_delete);
    }

    @Override
    public void start() {

    }


    @Override
    public void getUserDate() {

        o_userInfo = Network.getMainApi().getUserInfo();

        s_userInfo = new ProgressSubscriber<>(new ProgressSubscriber.SubscriberOnNextListener<Base<User>>() {
            @Override
            public void onNext(Base<User> o) {
                if (o.getErrorCode() == 0)
                    mView.setUserDate(o.getRes());

                else {
                    // TODO: 2017/2/21 处理更新用户信息页面
                }
            }

        }, mView.onGetContext());

        JxUtils.toSubscribe(o_userInfo, s_userInfo);
    }

    @Override
    public void logout() {
        App.spUtils.clear();
        Activity activity = (Activity) mView.onGetContext();
        activity.startActivity(new Intent(activity, WelcomeActivity.class));
        activity.finish();
    }

}
