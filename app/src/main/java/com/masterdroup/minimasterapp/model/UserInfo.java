package com.masterdroup.minimasterapp.model;

/**
 * 用于展示信息
 * Created by 11473 on 2017/2/16.
 */

public class UserInfo {

    String name;
    String password;
    String phoneNum;
    String headUrl;

    Meta meta;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    class Meta {

        String updateAt;
        String createAt;

        public String getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(String updateAt) {
            this.updateAt = updateAt;
        }

        public String getCreateAt() {
            return createAt;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }

        @Override
        public String toString() {
            return "Meta{" +
                    "updateAt='" + updateAt + '\'' +
                    ", createAt='" + createAt + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", meta=" + meta +
                '}';
    }
}
