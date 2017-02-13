package com.masterdroup.minimasterapp.model;

import java.util.List;

/**
 * 食谱
 * Created by 11473 on 2017/2/13.
 */

public class Recipes {

    String name;//菜谱名称 必填字段

    List<String> powerProductKey;//加热设备产品号
    List<String> temperatureSensorProductKey;//温度反馈设备产品号

    Detail detail;//菜谱主要简介及封面图 必填字段

    List<DescribeStep> describeSteps;//烹饪前步骤准备

    List<CookingStep> cookingStep;

    class Detail {
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


    class DescribeStep {
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

    class CookingStep {
        String imgSrc;
        String describe;
        int temperaturek;
        int power;
        int duration;
        int triggerTemp;

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

        public int getTemperaturek() {
            return temperaturek;
        }

        public void setTemperaturek(int temperaturek) {
            this.temperaturek = temperaturek;
        }

        public int getPower() {
            return power;
        }

        public void setPower(int power) {
            this.power = power;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getTriggerTemp() {
            return triggerTemp;
        }

        public void setTriggerTemp(int triggerTemp) {
            this.triggerTemp = triggerTemp;
        }
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
        return describeSteps;
    }

    public void setDescribeSteps(List<DescribeStep> describeSteps) {
        this.describeSteps = describeSteps;
    }

    public List<CookingStep> getCookingStep() {
        return cookingStep;
    }

    public void setCookingStep(List<CookingStep> cookingStep) {
        this.cookingStep = cookingStep;
    }
}
