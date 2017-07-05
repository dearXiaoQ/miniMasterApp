package com.mastergroup.smartcook.model;

/**
 * Created by 11473 on 2016/12/23.
 * 菜谱步骤
 */

public class Step {

    public int stepNo;
    String des;
    String picture_url;//图片本地路径
    String picture_server_url;//图片服务器路径
    int resultCode;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getStepNo() {
        return stepNo;
    }

    public void setStepNo(int stepNo) {
        this.stepNo = stepNo;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getPicture_server_url() {
        return picture_server_url;
    }

    public void setPicture_server_url(String picture_server_url) {
        this.picture_server_url = picture_server_url;
    }
}
