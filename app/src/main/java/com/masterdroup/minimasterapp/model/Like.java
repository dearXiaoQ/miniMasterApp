package com.masterdroup.minimasterapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jun on 2017/3/21.
 */

public class Like implements Serializable{

    List<User> users;
    String date;

/*
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }
*/

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getDate() {return date; }

    public void setDate(String date) {this.date = date;}

}
