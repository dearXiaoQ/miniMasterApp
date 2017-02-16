package com.masterdroup.minimasterapp.model;

/**
 * Created by 11473 on 2017/2/13.
 */

public class Token {
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                '}';
    }
}
