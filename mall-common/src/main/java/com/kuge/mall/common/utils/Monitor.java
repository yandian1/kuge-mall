package com.kuge.mall.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * created by xbxie on 2024/8/13
 */
public class Monitor {
    public static void printNow() {
        printNow("yyyy-MM-dd HH:mm:ss");
    }

    public static void printNow(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        System.out.println(sdf.format(date));
    }

    public static void printNow(String prefix, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        System.out.println(prefix + sdf.format(date));
    }
}
