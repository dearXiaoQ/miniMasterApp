package com.mastergroup.smartcook.model;

/**
 * Created by xiaoQ on 2017/6/23.
 */

class FavoritesHomePager {

    String _id;
    String date;
    String user;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }
}
