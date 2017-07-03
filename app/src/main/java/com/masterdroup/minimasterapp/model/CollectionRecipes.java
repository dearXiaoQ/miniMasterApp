package com.masterdroup.minimasterapp.model;

/**
 * Created by xiaoQ on 2017/6/29.
 */

import java.util.List;

/** 收藏列表的实体类 和个人菜谱  */
public class CollectionRecipes {

    public class RecipesBean {

        String _id;

        String name;

        String date; //用户收藏的时间

        Recipes.RecipesBean.Detail detail;

        List<Like> like;

        List<favorites> favorites;

        public void setLike(List<Like> like) {
            this.like = like;
        }

        public List<Like> getLike() {
            return like;
        }

        public void setFavorites(List<CollectionRecipes.favorites> favorites) {
            this.favorites = favorites;
        }

        public List<CollectionRecipes.favorites> getFavorites() {
            return favorites;
        }

        public void setDetail(Recipes.RecipesBean.Detail detail) {

            this.detail = detail;
        }

        public Recipes.RecipesBean.Detail getDetail() {
            return detail;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

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
    }

    /** 收藏列表-收藏实体类 */
    public class favorites{

        String date;
        String user;/** user id */

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


    public class Like{
        String date;
        String user;

        public void setUser(String user) {
            this.user = user;
        }

        public String getUser() {
            return user;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDate() {
            return date;
        }
    }

}
