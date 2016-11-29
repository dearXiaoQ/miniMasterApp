package com.masterdroup.minimasterapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.masterdroup.minimasterapp.App;
import com.masterdroup.minimasterapp.Constant;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description:
 * Created by andmobi003 on 2016/7/20 15:15
 */
public class Network {
    private static Api mainApi;

    public static Api getMainApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (mainApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(MyOkHttpClient.getInstance(App.mContext).getOkHttpClient())
                    .baseUrl(Constant.BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            mainApi = retrofit.create(Api.class);
        }

        return mainApi;
    }
}
