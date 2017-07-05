package com.mastergroup.smartcook.model;

import java.io.Serializable;

/**
 * Created by jun on 2017/3/21.
 */

public class Like implements Serializable{

    LikeUser user;
    String date;



    public LikeUser getUser() {
        return user;
    }

    public void setUser(LikeUser user) {
        this.user = user;
    }

    public String getDate() {return date; }

    public void setDate(String date) {this.date = date;}

    public class LikeUser implements Serializable{
        String name;
        String headUrl;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String userHeadUrl) {
            this.headUrl = userHeadUrl;
        }
    }

}
