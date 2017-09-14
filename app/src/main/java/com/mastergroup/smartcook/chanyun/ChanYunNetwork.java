package com.mastergroup.smartcook.chanyun;

import com.mastergroup.smartcook.App;
import com.mastergroup.smartcook.Constant;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description:
 * Created by andmobi003 on 2017/9/13 15:05
 */
public class ChanYunNetwork {


    private static ChanYunApi chanYunApi;

    public static ChanYunApi getChanYunApi() {
        if (chanYunApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(ChanyunOkHttpClient.getInstance(App.mContext).getOkHttpClient())
                    .baseUrl(Constant.CHANYUN_BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            chanYunApi = retrofit.create(ChanYunApi.class);
        }

        return chanYunApi;
    }

}
