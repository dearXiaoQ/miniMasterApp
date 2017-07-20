package com.mastergroup.smartcook.api;


import com.mastergroup.smartcook.model.Base;
import com.mastergroup.smartcook.model.Comment;
import com.mastergroup.smartcook.model.DetailRecipes;
import com.mastergroup.smartcook.model.FeedBack;
import com.mastergroup.smartcook.model.MenuID;
import com.mastergroup.smartcook.model.MyCollectionRecipes;
import com.mastergroup.smartcook.model.Null;
import com.mastergroup.smartcook.model.PhoneAndToken;
import com.mastergroup.smartcook.model.Recipes;
import com.mastergroup.smartcook.model.RecipesList;
import com.mastergroup.smartcook.model.Token;
import com.mastergroup.smartcook.model.Url;
import com.mastergroup.smartcook.model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
     * 登录  //机智云登录成功后，再登录smartCook服务器
     */
    @POST("miniMasterApp/user/signin")
    Observable<Base<Token>> login(@Body User body);

    /**
     * 登录  //先登录smartCook服务器，再登录机智云服务器
     */
    @POST("miniMasterApp/user/signin")
    Observable<Base<PhoneAndToken>> signin(@Body User body);

    /**
     * 意见反馈
     * */
    @POST("miniMasterApp/feedback/addFeedback/")
    Observable<Base<Null>> feedBackMsg(@Body FeedBack feedBack);

    @GET("miniMasterApp/user/getSmsCode/{phone}")
    Observable<Base<Null>> getVerification(@Path("phone") String phone);

    /** 忘记密码后，重置密码 */
   // @Headers({"X-Gmri-Application-Auth:944d8514354c7778a83128bdf57c1cf7"})
    @POST("miniMasterApp/user/resetPassword")
    Observable<Base<Null>> resetPassword(@Body RequestBody body);


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
     * @param index 页码
     * @param count 每页数量
     */
    @GET("miniMasterApp/recipes/getRecipesList/{index}/{count}")
    Observable<Base<RecipesList>> getRecipesList(@Path("index") int index, @Path("count") int count);

    @GET("miniMasterApp/recipes/search/{name}/{index}/{count}")
    Observable<Base<RecipesList>> searchRecipesList(@Path("name") String searchStr, @Path("index") int index, @Path("count") int count);

    /**
     * 获取轮播菜谱
     */
    @GET("miniMasterApp/recipes/getCarouselList")
    Observable<Base<RecipesList>> getBannerRecipesList();


    /**
     * 获取用户自身菜谱列表
     * @param index 页码
     * @param count 每页数量
     */
    @GET("miniMasterApp/recipes/listByOwner/{index}/{count}")
    Observable<Base<MyCollectionRecipes>> listByOwner(@Path("index") int index, @Path("count") int count);


    /**
     * 获取菜谱详情
     *
     * @param id 菜谱id
     */
    @GET("miniMasterApp/recipes/getRecipesDetail/{id}")
    Observable<Base<DetailRecipes.RecipesBean>> getRecipesDetail(@Path("id") String id);


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
     * 收藏菜谱
     * @param id 菜谱id
     */
    @POST("miniMasterApp/recipes/addFavorites/{id}")
    Observable<Base> addFavorites(@Path("id") String id);

    /**
     * 取消收藏菜谱
     * @param id
     */
    @POST("miniMasterApp/recipes/cancelFavorites/{id}")
    Observable<Base> cancelFavorites(@Path("id") String id);


    /**
     * 获取收藏列表
     */
    @GET("miniMasterApp/user/getFavoritesList")
    Observable<Base<MyCollectionRecipes>> getFavoritesList();


    /**
     * 增加评论
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