package com.mastergroup.smartcook.model;

/**
 * Created by xiaoQ on 2017/6/26.
 */

public class LikeHomePager {
    String _id;
    String date;
    String user;

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }
}
