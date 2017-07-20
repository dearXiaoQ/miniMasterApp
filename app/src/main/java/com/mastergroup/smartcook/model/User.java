package com.mastergroup.smartcook.model;

/**
 * 用于展示信息
 * Created by 11473 on 2017/2/16.
 */

public class User {
    private UserBean user;


    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }


    public class UserBean {
        String name;
        String password;
        String phoneNum;
        String headUrl;
        int age;
        String address;
        Meta meta;
        /** 用于重置密码的验证码 */
        String code;
        /** 个性签名 */
        String signature;

        int role;
        /** 1 male  2 female*/
        int sex;
        int __v;
        String uid;

        public UserBean() {

        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getSingnature() {return signature;}

        public void setSignature(String signature) {this.signature = signature;}

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

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

        public void setPhoneNum(String phone) {
            this.phoneNum = phone;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getCode() {return  code;}

        public void setCode(String code) {this.code = code;}



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
            return "UserBean{" +
                    "name='" + name + '\'' +
                    ", password='" + password + '\'' +
                    ", phoneNum='" + phoneNum + '\'' +
                    ", headUrl='" + headUrl + '\'' +
                    ", meta=" + meta +
                    ", role=" + role +
                    ", sex=" + sex +
                    ", __v=" + __v +
                    ", uid='" + uid + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "user=" + user +
                '}';
    }
}
