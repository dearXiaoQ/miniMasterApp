package com.masterdroup.minimasterapp.model;

import java.util.List;

/**
 * Created by xiaoQ on 2017/6/26.
 */
/** 菜谱详情页实体类 */
public class DetailRecipes {


    public class RecipesBean {

  /*      public RecipesBean() {

        }*/

        String _id;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        String name;//菜谱名称 必填字段

        List<String> powerProductKey;//加热设备产品号
        List<String> temperatureSensorProductKey;//温度反馈设备产品号

        Recipes.RecipesBean.Detail detail;//菜谱主要简介及封面图 必填字段

        List<DescribeStep> describeStep;//烹饪前步骤准备

        List<CookingStep> cookingStep;
        Recipes.RecipesBean.Owner owner;
        List<Like> like;
        List<Favorites> favorites;
        List<Comment>  commentList;


        public List<Comment> getComment() {
            return commentList;
        }

        public void setComment(List<Comment> comment) {
            this.commentList = comment;
        }

        public List<Favorites> getFavorite() {return favorites;}

        public void setFavorite(List<Favorites> favorites) {this.favorites = favorites;}

        public List<Like> getLikes() {
            return like;
        }

        public void setLikes(List<Like> likes) {
            this.like = likes;
        }

        List<Food> foodList;



        public List<Food> getFoodList() {
            return foodList;
        }

        public void setFoodList(List<Food> foodList) {
            this.foodList = foodList;
        }

        public class Detail {
            String imgSrc;
            String describe;

            public String getImgSrc() {
                return imgSrc;
            }

            public void setImgSrc(String imgSrc) {
                this.imgSrc = imgSrc;
            }

            public String getDescribe() {
                return describe;
            }

            public void setDescribe(String describe) {
                this.describe = describe;
            }
        }

        public class Owner {

            int ownerType;

            Recipes.RecipesBean.Owner.OwnerUid ownerUid;

            public class OwnerUid {


                String _id;
                String name;
                String headUrl;

                public String get_id() {
                    return _id;
                }

                public void set_id(String _id) {
                    this._id = _id;
                }

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
            }

            public int getOwnerType() {
                return ownerType;
            }

            public void setOwnerType(int ownerType) {
                this.ownerType = ownerType;
            }

            public Recipes.RecipesBean.Owner.OwnerUid getOwnerUid() {
                return ownerUid;
            }

            public void setOwnerUid(Recipes.RecipesBean.Owner.OwnerUid ownerUid) {
                this.ownerUid = ownerUid;
            }
        }



        public Recipes.RecipesBean.Owner getOwner() {
            return owner;
        }

        public void setOwner(Recipes.RecipesBean.Owner owner) {
            this.owner = owner;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getPowerProductKey() {
            return powerProductKey;
        }

        public void setPowerProductKey(List<String> powerProductKey) {
            this.powerProductKey = powerProductKey;
        }

        public List<String> getTemperatureSensorProductKey() {
            return temperatureSensorProductKey;
        }

        public void setTemperatureSensorProductKey(List<String> temperatureSensorProductKey) {
            this.temperatureSensorProductKey = temperatureSensorProductKey;
        }

        public Recipes.RecipesBean.Detail getDetail() {
            return detail;
        }

        public void setDetail(Recipes.RecipesBean.Detail detail) {
            this.detail = detail;
        }

        public List<DescribeStep> getDescribeSteps() {
            return describeStep;
        }

        public void setDescribeSteps(List<DescribeStep> describeSteps) {
            this.describeStep = describeSteps;
        }

        public List<CookingStep> getCookingStep() {
            return cookingStep;
        }

        public void setCookingStep(List<CookingStep> cookingStep) {
            this.cookingStep = cookingStep;
        }
    }

}
