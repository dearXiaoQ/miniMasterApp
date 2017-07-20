package com.mastergroup.smartcook;

import android.net.wifi.ScanResult;
import com.mastergroup.smartcook.util.Md5;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11473 on 2016/11/29.
 */

public class Constant {

    public final static String BASEURL = "http://gmri.mastergroup.com.cn:3001/";

    public final static String SHARE_BASEURL = "http://gmri.mastergroup.com.cn:3001";

    public final static String BITMAP_FILE_NAME = "/cover/";


    public final static String APP_ID = "f0d68c68539243bc853b1298faf133ff";//机智云APPID
    public final static String APP_Secret = "42ebbf3d1f3b4ca6815a52066485d872";//机智云APP_Secret

    public final static String Md5Str = Md5.getMd5(APP_ID + APP_Secret);

    public static List<ScanResult> ssidList = new ArrayList<ScanResult>();



    /** 设备热点默认前缀 */
    public static String SoftAP_Start = "XPG-GAgent";


    /**
     * 判断用户登录状态 0：未登录 1：实名用户登录 2：匿名用户登录 3：匿名用户登录中 4：匿名用户登录中断
     */
    public static int loginStatus;

    /**
     * 获取设备列表
     */
    public static final int GETLIST = 0;

    /**
     * 刷新设备列表
     */
    public static final int UPDATALIST = 1;

    /**
     * 订阅成功前往控制页面
     */
    public static final int TOCONTROL = 2;

    /**
     * 通知
     */
    public static final int TOAST = 3;

    /**
     * 设备绑定
     */
    public static final int BOUND = 9;

    /**
     * 设备解绑
     */
    public static final int UNBOUND = 99;

    /**
     * 新设备提醒
     */
    public static final int SHOWDIALOG = 999;

    public static final int PULL_TO_REFRESH = 888;

}
