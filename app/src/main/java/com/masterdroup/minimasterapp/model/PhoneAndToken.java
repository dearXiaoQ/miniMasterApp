package com.masterdroup.minimasterapp.model;

/**
 * Created by xiaoQ on 2017/6/9.
 */

public class PhoneAndToken {

    String phone;

    String token;

    public String getToken() {return  token;}

    public  void setToken(String token) {this.token = token;}

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "PhoneAndToken{" +
                "phone='" + phone + '\'' + "token='" + token + '\'' +
                '}';
    }

}
