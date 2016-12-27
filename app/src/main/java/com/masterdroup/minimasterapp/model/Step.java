package com.masterdroup.minimasterapp.model;

/**
 * Created by 11473 on 2016/12/23.
 * 菜谱步骤
 */

public class Step {

    public int stepNo;
    String des;
    String picture_url;

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
}
