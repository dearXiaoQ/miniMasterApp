package com.masterdroup.minimasterapp.util;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 11473 on 2017/2/16.
 */

public class JxUtils {


    public static void toSubscribe(Observable o, Subscriber s) {
        o
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }


    public static Subscription toSubscribeRe(Observable o, Subscriber s) {
        return o
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
