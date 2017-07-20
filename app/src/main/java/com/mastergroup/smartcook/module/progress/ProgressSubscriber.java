package com.mastergroup.smartcook.module.progress;

import android.content.Context;
import com.blankj.utilcode.utils.LogUtils;
import rx.Subscriber;

/**
 * Created by 11473 on 2017/2/16.
 */

public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {
    private SubscriberOnNextListener mSubscriberOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;

    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener,Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }


    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }


    @Override
    public void onCompleted() {
        //停止ProgressDialog
        LogUtils.d("ProgressSubscriber", "onCompleted()");
        dismissProgressDialog();

    }

    @Override
    public void onError(Throwable e) {
        //处理错误  同时也停止ProgressDialog
        LogUtils.e("ProgressSubscriber", "onError()");
        dismissProgressDialog();
        e.printStackTrace();

    }

    @Override
    public void onNext(T t) {
        LogUtils.d("ProgressSubscriber", "onNext()");
        LogUtils.d("ProgressSubscriber_返回信息", t.toString());
        mSubscriberOnNextListener.onNext(t);
    }

    @Override
    public void onStart() {
        //启动一个ProgressDialog
        LogUtils.d("ProgressSubscriber", "onStart()");
        showProgressDialog();
        super.onStart();
    }


    public interface SubscriberOnNextListener<T> {
        void onNext(T t);
    }


    @Override
    public void onCancelProgress() {
        LogUtils.d("ProgressSubscriber-ProgressCancelListener", "onCancelProgress()");
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }

}



