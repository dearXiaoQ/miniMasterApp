package com.masterdroup.minimasterapp.api;

import android.content.Intent;

import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.Menu;
import com.masterdroup.minimasterapp.model.MenuID;
import com.masterdroup.minimasterapp.model.Null;
import com.masterdroup.minimasterapp.model.Recipes;
import com.masterdroup.minimasterapp.model.Token;
import com.masterdroup.minimasterapp.model.Url;
import com.masterdroup.minimasterapp.model.User;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
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
    @POST("miniMasterApp/user/infoUpdate")
    Observable<Base<Null>> userInfoUpDate(@Body User body);

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

    /**
     * 新增菜谱
     */
    @POST("/miniMasterApp/recipes/createRecipes")
    Observable<Base<MenuID>> createRecipes(@Body Recipes body);

    /**
     * 上传图片文件
     */

    @Multipart
    @POST("uploadFile")
    Observable<Base<Url>> uploadFile(@Part MultipartBody.Part file);

    /**
     * 删除图片文件
     */
    @POST("deleteFile/{fileName}")
    Observable<Base<Null>> deleteFile(@Path("fileName") String fileName);


}