package com.masterdroup.minimasterapp.util;

import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.view.RoundProgressBar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;



/**
 * Created by xiaoQ on 2017/6/8.
 */

public class StringFormatCheckUtils {

    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str)throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str)throws PatternSyntaxException {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /** 昵称合法性检测 */
    public static int isNickNameLegal(String str)throws PatternSyntaxException {

        if(str.length() == 0) {
            return R.string.input_nickname;
        }

        if(str.length() < 2 )
            return  R.string.nickname_not_enough;

        if(str.length() > 12)
            return  R.string.nickname_over_enough;

        return R.string.nickname_right;
    }

    /** 验证码合法性检测 */
    public static int isVerificationLegal(String verification) {

        if(verification.length() == 0) {
            return R.string.input_verification;
        }

        if(verification.length() != 6) {
            return R.string.verification_not_enough;
        } else {

            return  R.string.verification_right;
        }

    }

    /** 密码合法性检测 */
    public static int isPasswordLegal(String pwd) {

        if(pwd.length() == 0) {
            return R.string.input_pwd;
        }

        if(pwd.length() < 6) {
            return R.string.pwd_not_enough;
        }

        if(pwd.length() > 12) {
            return R.string.pwd_over_enough;
        }

        return R.string.pwd_right;
    }


    /** 再次输入密码合法性检测 */
    public static int isAgainPwdLegal(String pwd, String againPwd) {

        if(againPwd.length() == 0) {
            return R.string.again_pwd_not_null;
        }

        if(!(againPwd.equals(pwd))){
            return R.string.pwd_again_error;
        } else {
            return R.string.again_pwd_right;
        }

    }

}
