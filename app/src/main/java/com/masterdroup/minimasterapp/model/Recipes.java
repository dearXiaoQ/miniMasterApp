package com.masterdroup.minimasterapp.model;

import java.util.List;

/**
 * 食谱
 * Created by 11473 on 2017/2/13.
 */

public class Recipes {

    public RecipesBean recipes;

    public RecipesBean getRecipesBean() {
        return recipes;
    }

    public void setRecipesBean(RecipesBean recipes) {
        this.recipes = recipes;
    }


    public class RecipesBean {

        public RecipesBean() {

        }

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

        Detail detail;//菜谱主要简介及封面图 必填字段

        List<DescribeStep> describeStep;//烹饪前步骤准备

        List<CookingStep> cookingStep;
        Owner owner;
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

            OwnerUid ownerUid;

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

            public OwnerUid getOwnerUid() {
                return ownerUid;
            }

            public void setOwnerUid(OwnerUid ownerUid) {
                this.ownerUid = ownerUid;
            }
        }



        public Owner getOwner() {
            return owner;
        }

        public void setOwner(Owner owner) {
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

        public Detail getDetail() {
            return detail;
        }

        public void setDetail(Detail detail) {
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

    @Override
    public String toString() {
        return "Recipes{" +
                "recipes=" + recipes +
                '}';
    }
}
