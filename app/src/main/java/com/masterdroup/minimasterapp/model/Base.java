package com.masterdroup.minimasterapp.model;

/**
 * Created by 11473 on 2016/11/29.
 */

public class Base<T> {

    /**
     * 错误码，0正常；其他，异常
     */
    Integer errorCode;

    /**
     * 错误信息
     */
    String message;


    /**
     * 返回结果
     */
    T res;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getRes() {
        return res;
    }

    public void setRes(T res) {
        this.res = res;
    }

    @Override
    public String toString() {
        return "Base{" +
                "errorCode=" + errorCode +
                ", message='" + message + '\'' +
                ", res=" + res +
                '}';
    }
}
