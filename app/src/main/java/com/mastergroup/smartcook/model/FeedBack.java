package com.mastergroup.smartcook.model;

/**
 * Created by xiaoQ on 2017/7/10.
 */

public class FeedBack {
    String feedback; //反馈内容
    String type;     //反馈类型 1、意见建议， 2、功能不能用，3其他

    public String getFeedback() {
        return feedback;
    }

    public String getType() {
        return  type;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setType(String type) {
        this.type = type;
    }
}

