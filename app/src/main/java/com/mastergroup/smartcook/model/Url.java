package com.mastergroup.smartcook.model;

import java.util.List;

/**
 * Created by 11473 on 2017/2/20.
 */

public class Url {

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    private List<String> url;


    @Override
    public String toString() {
        return "Url{" +
                "url=" + url +
                '}';
    }
}
