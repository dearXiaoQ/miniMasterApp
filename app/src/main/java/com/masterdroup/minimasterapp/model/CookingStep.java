package com.masterdroup.minimasterapp.model;

/**
 * 烹饪前准备步骤
 * Created by 11473 on 2017/2/27.
 */

public class CookingStep {

    public int stepNo;
    String describe;
    String picture_url;//图片本地路径
    String imgSrc;//图片服务器路径
    int resultCode;
    int temperature;//温度
    int power;//火力
    int duration;//时间
    int triggerTemp;//温度变化率，1S钟温度下降的幅值


    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTriggerTemp() {
        return triggerTemp;
    }

    public void setTriggerTemp(int triggerTemp) {
        this.triggerTemp = triggerTemp;
    }

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
