package com.mastergroup.smartcook.model;

/**
 * Created by 11473 on 2017/2/23.
 */

public class MenuID {
    String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "MenuID{" +
                "_id='" + _id + '\'' +
                '}';
    }
}
