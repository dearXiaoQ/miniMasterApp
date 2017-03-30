package com.masterdroup.minimasterapp.api;


import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.Comment;
import com.masterdroup.minimasterapp.model.Menu;
import com.masterdroup.minimasterapp.model.MenuID;
import com.masterdroup.minimasterapp.model.Null;
import com.masterdroup.minimasterapp.model.Recipes;
import com.masterdroup.minimasterapp.model.RecipesList;
import com.masterdroup.minimasterapp.model.Token;
import com.masterdroup.minimasterapp.model.Url;
import com.masterdroup.minimasterapp.model.User;

import okhttp3.MultipartBody;
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
    Observable<Base> userInfoUpDate(@Body User body);

    /**
     * 获取用户信息接口
     */
    @GET("miniMasterApp/user/info")
    Observable<Base<User>> getUserInfo();


    /**
     * 新增菜谱
     */
    @POST("miniMasterApp/recipes/createRecipes")
    Observable<Base<MenuID>> createRecipes(@Body Recipes body);


    /**
     * 获取菜谱列表
     *
     * @param index 页码
     * @param count 每页数量
     */
    @GET("miniMasterApp/recipes/getRecipesList/{index}/{count}")
    Observable<Base<RecipesList>> getRecipesList(@Path("index") int index, @Path("count") int count);

    /**
     * 获取用户自身菜谱列表
     *
     * @param index 页码
     * @param count 每页数量
     */
    @GET("miniMasterApp/recipes/listByOwner/{index}/{count}")
    Observable<Base<RecipesList>> listByOwner(@Path("index") int index, @Path("count") int count);


    /**
     * 获取菜谱详情
     *
     * @param id 菜谱id
     */
    @GET("miniMasterApp/recipes/getRecipesDetail/{id}")
    Observable<Base<Recipes.RecipesBean>> getRecipesDetail(@Path("id") String id);


    /**
     * 删除菜谱
     *
     * @param _id 菜谱id
     */
    @FormUrlEncoded
    @POST("miniMasterApp/recipes/deleteRecipes")
    Observable<Base<Null>> deleteRecipes(@Field("_id") MenuID _id);


    /**
     * 菜谱点赞
     *
     * @param id 菜谱id
     */
    @POST("miniMasterApp/recipes/addFollower/{id}")
    Observable<Base> addFollower(@Path("id") String id);

    /**
     * 菜谱取消点赞
     *
     * @param id 菜谱id
     */
    @POST("miniMasterApp/recipes/cancelFollower/{id}")
    Observable<Base> cancelFollower(@Path("id") String id);


    /**
     * 增加评论
     *
     * @param id 菜谱id
     */
    @POST("miniMasterApp/recipes/addComment/{id}")
    Observable<Base> addComment(@Path("id") String id, @Body Comment body);


    /**
     * 上传图片文件
     */

    @Multipart
    @POST("uploadFile")
    Observable<Base<Url>> uploadFile(@Part MultipartBody.Part file);


    /**
     * 上传图片文件 多文件
     */

    @POST("uploadFile")
    Observable<Base<Url>> uploadFiles(@Body MultipartBody multipartBody);

    /**
     * 删除图片文件
     */
    @POST("deleteFile/{fileName}")
    Observable<Base<Null>> deleteFile(@Path("fileName") String fileName);


}