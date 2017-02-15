package com.masterdroup.minimasterapp.model;

/**
 * Cre ated by 11473 on 2016/11/29.
 */

public class User {

    private UserBean user;

    public UserBean getUserBean() {
        return user;
    }

    public void setUserBean(UserBean userBean) {
        user = userBean;
    }

    public class UserBean {
        private String name;
        private String password;

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
    }
}
