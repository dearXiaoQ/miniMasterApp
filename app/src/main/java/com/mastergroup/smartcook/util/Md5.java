package com.mastergroup.smartcook.util;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by xiaoQ on 2017/6/14.
 */
/** App_id + scrpit */
public class Md5 {
    public static String getMd5 (String str){

        if(TextUtils.isEmpty(str))
            return "";

        MessageDigest md5 = null;

        try{

            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            String result = "";
            for(byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if(temp.length() == 1) {
                    temp = "0" + temp;
                } else {
                    result += temp;
                }
            }
            return  result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        return "";
    }
}
