package com.mastergroup.smartcook.model;

/**
 * Created by xiaoQ on 2017/6/21.
 */

public class Favorites {
    String date;

    FavoritesUser user;

    public FavoritesUser getUser() {
        return user;
    }

    public void setUser(FavoritesUser user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public class FavoritesUser{
        String name;
        String headUrl;
        String _id;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
