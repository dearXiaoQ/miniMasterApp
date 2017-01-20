package com.masterdroup.minimasterapp.model;

/**
 * Cre ated by 11473 on 2016/11/29.
 */

public class User {

    private String name;
    private String password;
    private String phoneNum;
    private String headUrl;
    private Meta meta;

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

    private class Meta {
        String updateAt;//最近活动时间
        String createAt;//创建时间

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
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", meta=" + meta +
                '}';
    }
}
