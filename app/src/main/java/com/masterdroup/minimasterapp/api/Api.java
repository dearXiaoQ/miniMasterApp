package com.masterdroup.minimasterapp.api;

import android.support.annotation.Nullable;

import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.Menu;
import com.masterdroup.minimasterapp.model.User;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 11473 on 2016/11/29.
 */

public interface Api {


    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("miniMasterApp/user/signup")
    Observable<Base<Nullable>> registered(@Field("name") String name, @Field("password") int password, @Field("phoneNun") String phoneNun);

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("miniMasterApp/user/signin")
    Observable<Base<Nullable>> login(@Field("name") String name, @Field("password") int password);

    /**
     * 用户更新信息接口
     */
    @FormUrlEncoded
    @POST("miniMasterApp/user/infoUpdate")
    Observable<Base<Nullable>> userinfoUpdate(@Field("user") User user);

    /**
     * 获取用户信息接口
     */
    @GET("miniMasterApp/user/info")
    Observable<Base<User>> userinfo(@Field("name") String name);


    /**
     * 获取菜谱详情
     */
    @GET("?")
    Observable<Base<Menu>> menuInfo(@Field("id") int id);
}