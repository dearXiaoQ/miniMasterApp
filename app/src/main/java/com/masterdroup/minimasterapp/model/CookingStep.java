package com.masterdroup.minimasterapp.model;

/**
 *
 * 菜谱烹饪步骤
 * Created by 11473 on 2016/12/28.
 */

public class CookingStep {

    String time;
    int power;
    String pic;
    String div;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDiv() {
        return div;
    }

    public void setDiv(String div) {
        this.div = div;
    }

    @Override
    public String toString() {
        return "CookingStep{" +
                "time='" + time + '\'' +
                ", power=" + power +
                ", pic='" + pic + '\'' +
                ", div='" + div + '\'' +
                '}';
    }
}
