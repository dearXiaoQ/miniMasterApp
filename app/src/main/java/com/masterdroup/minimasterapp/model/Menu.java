package com.masterdroup.minimasterapp.model;

/**
 * Created by 11473 on 2016/12/15.
 */

public class Menu {

    String score;
    String menu_name;
    String user_name;
    String cover_url;
    String head_url;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "score='" + score + '\'' +
                ", menu_name='" + menu_name + '\'' +
                ", user_name='" + user_name + '\'' +
                ", cover_url='" + cover_url + '\'' +
                ", head_url='" + head_url + '\'' +
                '}';
    }
}
