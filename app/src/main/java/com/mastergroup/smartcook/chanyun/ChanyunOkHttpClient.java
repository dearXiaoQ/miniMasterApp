package com.mastergroup.smartcook.chanyun;

import android.content.Context;

import com.blankj.utilcode.utils.LogUtils;
import com.mastergroup.smartcook.App;
import com.mastergroup.smartcook.Constant;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.util.DebugUtils;
import com.mastergroup.smartcook.util.FileUtils;
import com.mastergroup.smartcook.util.NetUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description: OkHttpClient的包装类
 * Created by xiaoQ on 2017/7/13 15:46
 */
public class ChanyunOkHttpClient {

    private static ChanyunOkHttpClient instance;
    private OkHttpClient okHttpClient;
    private Context mContext;


    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR;
    /**
     * 日志拦截器
     */
    private final Interceptor LoggingInterceptor;

    /**
     * token拦截器
     */
    private final Interceptor mTokenInterceptor;

    /**
     *  禅云拦截器
     */
    private final Interceptor mChanyunInterceptor;

    /**
     * MD5拦截器
     * */
    private final Interceptor mMd5Interceptor;

    private ChanyunOkHttpClient(final Context mContext) {
        this.mContext = mContext;
        REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request();
                if (!NetUtils.hasNetwork(mContext)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                    DebugUtils.d("Interceptor", "no network");
                }
                Response originalResponse = chain.proceed(request);
                if (NetUtils.hasNetwork(mContext)) {
                    //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                    String cacheControl = request.cacheControl().toString();
                    return originalResponse.newBuilder()
                            .header("Cache-Control", cacheControl)
                            .removeHeader("Pragma")
                            //.addHeader("X-Gmri-Application-Auth", "944d8514354c7778a83128bdf57c1cf7")
                            .build();
                } else {
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=3600")
                            .removeHeader("Pragma")
                            .build();
                }
            }
        };

        LoggingInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                long t1 = System.nanoTime();
                DebugUtils.d("Interceptor", String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
                Response response = chain.proceed(request);
                long t2 = System.nanoTime();
                DebugUtils.d("Interceptor", String.format("Received response for %s in %.1fms%n%s code: %d", response.request().url(), (t2 - t1) / 1e6d, response.headers(), response.code()));
                return response;
            }
        };



        mTokenInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                String token = App.spUtils.getString(App.mContext.getString(R.string.key_token));
                //    token = "abc123456";
                if (token != null) {
                    LogUtils.d("mTokenInterceptor________token值", token);
                    Request authorised = originalRequest.newBuilder()
                            .header("Authorization", token)
                            .build();
                    return chain.proceed(authorised);
                } else
                    return chain.proceed(originalRequest);
            }
        };



        mChanyunInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request authorised = originalRequest.newBuilder()
                        .header(Constant.CONTENT_TYPE,  Constant.CONTENT_TYPE_VALUE)
                        .header(Constant.AUTH_ORG_CODE, "organization code")
                        .header(Constant.CLIENT_SIGN_SHA256, "client sign sha256")
                        .build();

                return chain.proceed(authorised);

            }
        };

        mMd5Interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                String Md5 = "944d8514354c7778a83128bdf57c1cf7";
                if(Md5 != null) {
                    LogUtils.d("Md5___值 = ", Md5);
                    Request mdScript = originalRequest.newBuilder()
                            .header("X-Gmri-Application-Auth", Md5)
                            .build();
                    return chain.proceed(mdScript);
                } else {
                    return chain.proceed(originalRequest);
                }
            }
        };
    }

    public static ChanyunOkHttpClient getInstance(Context mContext) {
        if (instance == null) {
            synchronized (ChanyunOkHttpClient.class) {
                if (instance == null) {
                    instance = new ChanyunOkHttpClient(mContext);
                }
            }
        }
        return instance;
    }

    public OkHttpClient getOkHttpClient() {
        //设置网络缓存
        File cacheFile = new File(FileUtils.getDiskCacheDir(mContext), "network-cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50); //50Mb

        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(LoggingInterceptor)//设置拦截器  云端响应头拦截器需要同时设置networkInterceptors和interceptors
                .addInterceptor(mChanyunInterceptor)
          /*      .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .addNetworkInterceptor(mTokenInterceptor)
                .addNetworkInterceptor(mMd5Interceptor)*/
                .cache(cache).build();


        return okHttpClient;
    }

}