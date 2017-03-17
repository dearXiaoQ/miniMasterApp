package com.masterdroup.minimasterapp.view;

/**
 * Created by 11473 on 2017/3/17.
 */

public enum InductionCookerInfo {


    POWER_OFF("关"), POWER_ON("开"), POWER_400("400W"), POWER_500("500W"), POWER_800("800W"), POWER_1000("1000W"), POWER_1200("1200W"), POWER_1400("1400W"), POWER_1600("1600W"), POWER_1800("1800W"), POWER_2100("2100W");


    private String power_info;

    private InductionCookerInfo(String power_info) {
        this.power_info = power_info;
    }
}
