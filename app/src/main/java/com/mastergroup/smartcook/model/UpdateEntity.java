package com.mastergroup.smartcook.model;

/**
 * Created by xiaoQ on 2017/10/30.
 */

public class UpdateEntity {

    String AppName;
    String serverVersion;
    String updateUrl;
    String desc;

    public String getAppName() {
        return AppName;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public String getDesc() {
        return desc;
    }


    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public void setUpdateUrl(String updateUrl) {
        updateUrl = updateUrl;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
