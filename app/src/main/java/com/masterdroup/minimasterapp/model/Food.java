package com.masterdroup.minimasterapp.model;

/**
 * Created by 11473 on 2017/3/11.
 */

public class Food {

    String foodType;
    int amount;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Food{" +
                "foodType='" + foodType + '\'' +
                ", amount=" + amount +
                '}';
    }
}
