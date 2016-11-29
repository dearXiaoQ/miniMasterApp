package com.masterdroup.minimasterapp;

import android.app.Application;
import android.content.Context;
/**
 * Created by 11473 on 2016/11/29.
 */

public class App extends Application {
    public static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
