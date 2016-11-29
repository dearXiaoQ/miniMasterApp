package com.masterdroup.minimasterapp.util;

import android.content.Context;
import android.os.Environment;

/**
 * Description: 有关文件操作的工具类
 * Created by andmobi003 on 2016/7/13 15:34
 */
public class FileUtils {


    /**
     * 获取缓存文件夹：
     * 先判断是否有sd卡 当SD卡存在或者SD卡不可被移除的时候，就调用getExternalCacheDir()方法来获取缓存路径，
     * 否则就调用getCacheDir()方法来获取缓存路径。
     * 前者获取到的就是 /sdcard/Android/data/<application package>/cache 这个路径，
     * 而后者获取到的是 /data/data/<application package>/cache 这个路径。
     */
    public static String getDiskCacheDir(Context context) {

        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;

    }

}
