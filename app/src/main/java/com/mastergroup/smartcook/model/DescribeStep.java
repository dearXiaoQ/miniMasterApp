package com.mastergroup.smartcook.model;

/**
 * 烹饪前准备步骤
 * Created by 11473 on 2017/2/27.
 */

public class DescribeStep {

    public int stepNo;
    String describe;
    String picture_url;//图片本地路径
    String imgSrc;//图片服务器路径
    int resultCode;


    public int getStepNo() {
        return stepNo;
    }

    public void setStepNo(int stepNo) {
        this.stepNo = stepNo;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
