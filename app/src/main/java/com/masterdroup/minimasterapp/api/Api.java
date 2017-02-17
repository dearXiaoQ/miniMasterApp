package com.masterdroup.minimasterapp.api;

import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.Menu;
import com.masterdroup.minimasterapp.model.Null;
import com.masterdroup.minimasterapp.model.Token;
import com.masterdroup.minimasterapp.model.User;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 11473 on 2016/11/29.
 */

public interface Api {


    /**
     * 注册
     */
    @POST("miniMasterApp/user/signup")
    Observable<Base<Null>> registered(@Body User body);

    /**
     * 登录
     */
    @POST("miniMasterApp/user/signin")
    Observable<Base<Token>> login(@Body User body);

    /**
     * 用户更新信息接口
     */
    @FormUrlEncoded
    @POST("miniMasterApp/user/infoUpdate")
    Observable<Base<Null>> userinfoUpdate(@Field("user") User user);

    /**
     * 获取用户信息接口
     */
    @GET("miniMasterApp/user/info")
    Observable<Base<User>> getUserInfo();


    /**
     * 获取菜谱详情
     */
    @GET("?")
    Observable<Base<Menu>> menuInfo(@Field("id") int id);
}